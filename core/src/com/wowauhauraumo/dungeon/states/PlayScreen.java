package com.wowauhauraumo.dungeon.states;

import static com.wowauhauraumo.dungeon.managers.B2DVars.PPM;
import static com.esotericsoftware.minlog.Log.*;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.wowauhauraumo.dungeon.entities.Player;
import com.wowauhauraumo.dungeon.main.Game;
import com.wowauhauraumo.dungeon.managers.GameContactListener;
import com.wowauhauraumo.dungeon.managers.GameKeys;
import com.wowauhauraumo.dungeon.maps.Map;
import com.wowauhauraumo.dungeon.maps.Map.Areas;
import com.wowauhauraumo.dungeon.maps.Map.Portal;

/**
 * The main class for the game. Contains basically everything. Currently in the form of a game state.
 * 
 * @author TheGoomy42
 */
public class PlayScreen implements Screen {

	//private Game game;
	
	private SpriteBatch sb;
	private OrthographicCamera cam;
	//private OrthographicCamera hudcam;
	
	// box2d physics world
	private World world;
	
	@SuppressWarnings("unused")          // (not always used, this to stop annoying warning)
	private Box2DDebugRenderer renderer; // renderer for debugging - shows all box2d objects on cam
	private OrthographicCamera b2dcam;
	
	// logs the current fps
	//private FPSLogger logger; 
	
	private Player player;
	private final float moveSpeedOverworld = 0.5f;
	private final float moveSpeedNormal = 0.79f;
	private float moveSpeed = moveSpeedNormal;
	// one boolean for each side of the player
	private boolean[] playerColliding = new boolean[4];
	
	// map stuff
	private Map map;
	private boolean shouldTravel; // whether the player should travel to a new map next tick
	private Vector2 newSpawn;     // coords of where the player should be sent
	private Portal fromPortal;        // the portal object that the player is travelling through
	private Areas newMap;         // The identifier of the new area
	
	/**
	 * Constructor. Creates Box2D physics world, player and map
	 * @param gsm Reference to the GameStateManager
	 */
	public PlayScreen(Game game) {
		//this.game = game;
		sb = game.getSpriteBatch();
		cam = game.getCamera();
		//hudcam = game.getHUDCamera();
		
		// create box2d world etc.
		world = new World(new Vector2(0, 0), true);
		world.setContactListener(new GameContactListener(this));
		
		player = new Player(world);
		
		map = new Map();
		map.createMap(Areas.TOWN, world);
		
		// box2d debug renderer
		renderer = new Box2DDebugRenderer();
		b2dcam = new OrthographicCamera();
		b2dcam.setToOrtho(false, Game.width / PPM, Game.height / PPM);
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
		debug("Player has entered a portal.");
		if(p != null) {
			if(p.isActive()) {
				fromPortal = p;
				shouldTravel = true;
			} else {
				debug("Portal is inactive.");
			}
		} else {
			error("Portal is null! Cancelling player teleport...");
		}
	}
	
	public void reactivatePortal(Portal p) {
		info("Reactivating portal...");
		p.setActive(true);
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
	
	/**
	 * Update method contains game logic and basically everything that doesn't involve drawing
	 * graphics to the screen. Called in the overwritten render(float delta) method.
	 * 
	 * @param delta
	 */
	public void update(float delta) {
		
		// if the player wants to go through a portal
		if(shouldTravel) {
			// this is the id of the map the player wants to go to
			newMap = fromPortal.getEntranceToMap();
			int id = fromPortal.getEntranceToId();
			info("Player teleporting to map " + newMap.toString() + " spawn " + id);
			// creates the new map, discarding the old one
			map.createMap(newMap, world);
			// gets the location of the spawn
			newSpawn = map.getPortal(id).getBody().getPosition();
			// stop the player moving
			player.getBody().setLinearVelocity(0, 0);
			// move the player to the correct spawn in the new map
			player.getBody().setTransform(newSpawn.x, newSpawn.y, 0);
			info("Player successfully teleported to map " + newMap.toString());
			// make the player slower in the overworld
			if(newMap == Areas.OVERWORLD)
				moveSpeed = moveSpeedOverworld;
			else
				moveSpeed = moveSpeedNormal;
			// cancel all movement and input
			GameKeys.resetKeys();
			player.getBody().setLinearVelocity(0, 0);
			player.setMoving(false);
			// don't run this again
			shouldTravel = false;
			return;
		}
		
		// get user input
		handleInput();
		
		// update the player and world
		player.update(delta, playerColliding);
		world.step(delta, 6, 2);
	}
	
	public void render() {
		
		// set camera to follow player
		// need to check if the camera is off the screen
		// get the width and height of the map
		int w = map.getWidth() * map.getTileSize();
		int h = map.getHeight() * map.getTileSize();
		// the player's position
		Vector2 playerPos = player.getPosition();
		// a Vector2 containing the new position of the camera
		Vector2 pos = new Vector2(Game.width / 2, Game.height / 2);
		// no clue how this works
		if(playerPos.x * PPM - Game.width / 2 > 0) {
			if(playerPos.x * PPM + Game.width / 2 < w) {
				pos.x = playerPos.x * PPM;
			} else {
				pos.x = w - Game.width / 2;
			}
		}
		if(playerPos.y * PPM - Game.height / 2 > 0) {
			if(playerPos.y * PPM + Game.height / 2 < h) {
				pos.y = playerPos.y * PPM;
			} else {
				pos.y = h - Game.height / 2;
			}
		}
		// set the camera's position to this Vector2
		cam.position.set((int) pos.x, (int) pos.y, 0);
		
		// update the cameras
		b2dcam.position.set(pos.x / PPM, pos.y / PPM, 0);
		b2dcam.update();
		cam.update();
		
		// draw tile map
		map.render(cam);
		
		// draw player last (on top)
		sb.setProjectionMatrix(cam.combined);
		player.render(sb);
		
		// draw box2d debug world
		//renderer.render(world, b2dcam.combined);
	}
	
	// The following methods are for the Game/Screen process and should be
	// done at some point

	@Override
	public void render(float delta) {
		update(delta);
		render();
	}

	@Override
	public void show() {  }

	@Override
	public void resize(int width, int height) {  }

	@Override
	public void pause() {  }

	@Override
	public void resume() {  }

	@Override
	public void hide() {  }
	
	@Override
	public void dispose() {
		world.dispose();
	}
}
