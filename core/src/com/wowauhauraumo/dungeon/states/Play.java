package com.wowauhauraumo.dungeon.states;

import static com.wowauhauraumo.dungeon.managers.B2DVars.BIT_ENTITY;
import static com.wowauhauraumo.dungeon.managers.B2DVars.BIT_EXIT;
import static com.wowauhauraumo.dungeon.managers.B2DVars.BIT_WALL;
import static com.wowauhauraumo.dungeon.managers.B2DVars.PPM;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.wowauhauraumo.dungeon.entities.Player;
import com.wowauhauraumo.dungeon.main.Game;
import com.wowauhauraumo.dungeon.managers.GameContactListener;
import com.wowauhauraumo.dungeon.managers.GameKeys;
import com.wowauhauraumo.dungeon.managers.GameStateManager;
import com.wowauhauraumo.dungeon.maps.Map;
import com.wowauhauraumo.dungeon.maps.Map.Areas;
import com.wowauhauraumo.dungeon.maps.Map.Portal;

/**
 * The main class for the game. Contains basically everything. Currently in the form of a game state.
 * 
 * @author TheGoomy42
 */
public class Play extends GameState {

	// box2d physics world
	private World world;
	
	@SuppressWarnings("unused")          // (not always used, this to stop annoying warning)
	private Box2DDebugRenderer renderer; // renderer for debugging - shows all box2d objects on cam
	private OrthographicCamera b2dcam;
	
	// logs the current fps
	//private FPSLogger logger; 
	
	private Player player;
	private float moveSpeed = 0.79f; //TODO this should be changed slightly in the overworld map
	// one boolean for each side of the player
	private boolean[] playerColliding = new boolean[4];
	
	// map stuff
	private Map map;
	private boolean shouldTravel; // whether the player should travel to a new map next tick
	private Vector2 newSpawn;     // coords of where the player should be sent
	private Portal portal;        // the portal object that the player is travelling through
	private Areas newMap;         // The identifier of the new area
	
	/**
	 * Constructor. Creates Box2D physics world, player and map
	 * @param gsm Reference to the GameStateManager
	 */
	public Play(GameStateManager gsm) {
		super(gsm);
		
		// create box2d world etc.
		world = new World(new Vector2(0, 0), true);
		world.setContactListener(new GameContactListener(this));
		
		createPlayer();
		
		map = new Map();
		map.createMap(Areas.TOWN, world);
		
		// box2d debug renderer
		renderer = new Box2DDebugRenderer();
		b2dcam = new OrthographicCamera();
		b2dcam.setToOrtho(false, Game.width / PPM, Game.height / PPM);
	}
	
	/**
	 * Temporary method to create the player body and fixtures, before passing it into a 
	 * new player class instance.
	 * TODO move this to the player class
	 */
	private void createPlayer() {
		// create bodydef
		BodyDef bdef = new BodyDef();
		bdef.position.set(100 / PPM, 110 / PPM);
		bdef.type = BodyType.DynamicBody;
		Body body = world.createBody(bdef);
		
		// create shape
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(6 / PPM, 6 / PPM);
		
		// create main fixture
		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		fdef.filter.categoryBits = BIT_ENTITY;
		fdef.filter.maskBits = BIT_WALL | BIT_EXIT;
		body.createFixture(fdef).setUserData("player");
		
		// create top sensor
		shape.setAsBox(5f / PPM, 2 / PPM, new Vector2(0, 7 / PPM), 0);
		fdef.shape = shape;
		fdef.filter.categoryBits = BIT_ENTITY;
		fdef.filter.maskBits = BIT_WALL;
		fdef.isSensor = true;
		body.createFixture(fdef).setUserData("playerTop");
		
		// create bottom sensor
		shape.setAsBox(5f / PPM, 2 / PPM, new Vector2(0, -7 / PPM), 0);
		fdef.shape = shape;
		fdef.filter.categoryBits = BIT_ENTITY;
		fdef.filter.maskBits = BIT_WALL;
		fdef.isSensor = true;
		body.createFixture(fdef).setUserData("playerBot");
		
		// create left sensor
		shape.setAsBox(2 / PPM, 5f / PPM, new Vector2(-7 / PPM, 0), 0);
		fdef.shape = shape;
		fdef.filter.categoryBits = BIT_ENTITY;
		fdef.filter.maskBits = BIT_WALL;
		fdef.isSensor = true;
		body.createFixture(fdef).setUserData("playerL");
		
		// create right sensor
		shape.setAsBox(2 / PPM, 5f / PPM, new Vector2(7 / PPM, 0), 0);
		fdef.shape = shape;
		fdef.filter.categoryBits = BIT_ENTITY;
		fdef.filter.maskBits = BIT_WALL;
		fdef.isSensor = true;
		body.createFixture(fdef).setUserData("playerR");
		
		// pass this through to a new Player instance
		player = new Player(body);
	}
	
	/**
	 * Set the playerColliding boolean to tell game logic if the player is colliding with
	 * a solid object and should stop moving in that direction.
	 * 
	 * @param i an integer representing which side of the player is colliding
	 * @param b true if that side is colliding
	 */
	public void setPlayerCollding(int i, boolean b) {
		playerColliding[i] = b;
	}
	
	/**
	 * Tells the game logic that the player should travel next tick
	 * 
	 * @param p the portal the player has just entered
	 */
	public void playerTravel(Portal p) {
		portal = p;
		shouldTravel = true;
	}
	
	////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Check if any keys are pressed
	 */
	public void handleInput() {
		
		Vector2 movement = new Vector2(0,0);
		
		if(GameKeys.isDown(GameKeys.Keys.UP)) {
			movement.add(0, moveSpeed);
			player.setMoving(true);
		} else if(GameKeys.isDown(GameKeys.Keys.DOWN)) {
			movement.add(0, -moveSpeed);
			player.setMoving(true);
		} else if(GameKeys.isDown(GameKeys.Keys.RIGHT)) {
			movement.add(moveSpeed, 0);
			player.setRight(true);
			player.setMoving(true);
		} else if(GameKeys.isDown(GameKeys.Keys.LEFT)) {
			movement.add(-moveSpeed, 0);
			player.setRight(false);
			player.setMoving(true);
		} else {
			player.setMoving(false);
		}
		player.getBody().setLinearVelocity(movement);
	}
	
	public void update(float delta) {
		
		handleInput();
		
		player.update(delta, playerColliding);
		world.step(delta, 6, 2);
		
		if(shouldTravel) {
			newSpawn = map.getSpawnCoords(portal.getEndMap(), portal.getSpawnId());
			newMap = portal.getEndMap();
			map.createMap(newMap, world);
			player.getBody().setLinearVelocity(0, 0);
			player.getBody().setTransform((newSpawn.x  + player.getWidth() / 2) / PPM, (newSpawn.y + player.getHeight() / 2) / PPM, 0);
			shouldTravel = false;
		}
	}
	
	public void render() {
		
		// set camera to follow player
		// need to check if the camera is off the screen
		int w = map.getWidth() * map.getTileSize();
		int h = map.getHeight() * map.getTileSize();
		Vector2 pos = new Vector2(Game.width / 2, Game.height / 2);
		if(player.getPosition().x * PPM - Game.width / 2 > 0) {
			if(player.getPosition().x * PPM + Game.width / 2 < w) {
				pos.x = player.getPosition().x * PPM;
			} else {
				pos.x = w - Game.width / 2;
			}
		}
		if(player.getPosition().y * PPM - Game.height / 2 > 0) {
			if(player.getPosition().y * PPM + Game.height / 2 < h) {
				pos.y = player.getPosition().y * PPM;
			} else {
				pos.y = h - Game.height / 2;
			}
		}
		cam.position.set((int) pos.x, (int) pos.y, 0);
		b2dcam.position.set(pos.x / PPM, pos.y / PPM, 0);
		b2dcam.update();
		cam.update();
		
		// draw tile map
		map.render(cam);
		
		// draw player last (on top)
		sb.setProjectionMatrix(cam.combined);
		player.render(sb);
		
		// draw box2d debug world
//		renderer.render(world, b2dcam.combined);
	}
	
	public void dispose() {
		world.dispose();
	}
}
