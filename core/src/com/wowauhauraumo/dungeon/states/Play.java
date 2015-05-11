package com.wowauhauraumo.dungeon.states;

import static com.wowauhauraumo.dungeon.managers.B2DVars.BIT_EMPTY;
import static com.wowauhauraumo.dungeon.managers.B2DVars.BIT_ENTITY;
import static com.wowauhauraumo.dungeon.managers.B2DVars.BIT_WALL;
import static com.wowauhauraumo.dungeon.managers.B2DVars.PPM;

import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.wowauhauraumo.dungeon.entities.Player;
import com.wowauhauraumo.dungeon.entities.enemy.EvilWizard;
import com.wowauhauraumo.dungeon.main.Game;
import com.wowauhauraumo.dungeon.managers.GameContactListener;
import com.wowauhauraumo.dungeon.managers.GameKeys;
import com.wowauhauraumo.dungeon.managers.GameStateManager;

public class Play extends GameState {

	// box2d stuff
	private FPSLogger logger;
	private World world;
	//private Box2DDebugRenderer renderer;
	private OrthographicCamera b2dcam;
	
	private Player player;
	private float moveSpeed = 2f;
	
	private EvilWizard enemy;
	
	private TiledMap tileMap;
	private OrthogonalTiledMapRenderer tmr;
	private float tileSize;
	private final float mapscale = 1f;
	
	public Play(GameStateManager gsm) {
		super(gsm);
		
		// create box2d world etc.
		world = new World(new Vector2(0, 0), true);
		world.setContactListener(new GameContactListener());
		//renderer = new Box2DDebugRenderer();
		logger = new FPSLogger();
		
		createPlayer();
		createEnemy();
		createTiles();
		
		// box2d camera
		b2dcam = new OrthographicCamera();
		b2dcam.setToOrtho(false, Game.width / PPM, Game.height / PPM);
	}
	
	private void createPlayer() {
		// create bodydef
		BodyDef bdef = new BodyDef();
		bdef.position.set(100 / PPM, 110 / PPM);
		bdef.type = BodyType.DynamicBody;
		Body body = world.createBody(bdef);
		
		// create shape
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(16 / PPM, 16 / PPM);
		
		// create main fixture
		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		fdef.filter.categoryBits = BIT_ENTITY;
		fdef.filter.maskBits = BIT_WALL;
		body.createFixture(fdef).setUserData("player");
		
		player = new Player(body);
	}
	
	private void createEnemy() {
		// bodydef
		BodyDef bdef = new BodyDef();
		bdef.position.set(120 / PPM, 100 / PPM);
		bdef.type = BodyType.DynamicBody;
		Body body = world.createBody(bdef);
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(16 / PPM, 16 / PPM);
		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		fdef.filter.categoryBits = BIT_ENTITY;
		fdef.filter.maskBits = BIT_WALL;
		body.createFixture(fdef).setUserData("enemy");
		
		enemy = new EvilWizard(body);
	}
	
	private void createTiles() {
		tileMap = new TmxMapLoader().load("maps/test.tmx");
		tmr = new OrthogonalTiledMapRenderer(tileMap, mapscale);
		tileSize = (Integer) tileMap.getProperties().get("tilewidth");
		
		TiledMapTileLayer layer;
		
		layer = (TiledMapTileLayer) tileMap.getLayers().get("blocked");
		createLayer(layer, BIT_WALL);
		layer = (TiledMapTileLayer) tileMap.getLayers().get("walkable");
		createLayer(layer, BIT_EMPTY);
		
	}
	
	private void createLayer(TiledMapTileLayer layer, short bits) {
		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		
		for(int row = 0; row < layer.getHeight(); row++) {
			for(int col = 0; col < layer.getWidth(); col++) {
				Cell cell = layer.getCell(col, row);
				
				if(cell == null) continue;
				if(cell.getTile() == null) continue;
				bdef.type = BodyType.StaticBody;
				bdef.position.set((col + 0.5f) * tileSize / PPM * mapscale, (row + 0.5f) * tileSize / PPM * mapscale);
				
				// not a normal box because that has problems with dragging along
				ChainShape cs = new ChainShape();
				Vector2[] v = new Vector2[5];
				v[0] = new Vector2(-tileSize / 2 / PPM * mapscale, -tileSize / 2 / PPM * mapscale);
				v[1] = new Vector2(-tileSize / 2 / PPM * mapscale, tileSize / 2 / PPM * mapscale);
				v[2] = new Vector2(tileSize / 2 / PPM * mapscale, tileSize / 2 / PPM * mapscale);
				v[3] = new Vector2(tileSize / 2 / PPM * mapscale, -tileSize / 2 / PPM * mapscale);
				v[4] = v[0];
				cs.createChain(v);
				fdef.friction = 0;
				fdef.shape = cs;
				fdef.filter.categoryBits = bits;
				fdef.filter.maskBits = BIT_ENTITY;
				fdef.isSensor = false;
				
				world.createBody(bdef).createFixture(fdef);
			}
		}
	}
	
	public void handleInput() {
		
		Vector2 movement = new Vector2(0,0);
		
		// vertical movement
		if(GameKeys.isDown(GameKeys.UP)) {
			movement.add(0, moveSpeed);
		} else if(GameKeys.isDown(GameKeys.DOWN)) {
			movement.add(0, -moveSpeed);
		}
		// horizontal movement
		if(GameKeys.isDown(GameKeys.RIGHT)) {
			movement.add(moveSpeed, 0);
			player.setLeft(false);
		} else if(GameKeys.isDown(GameKeys.LEFT)) {
			player.setLeft(true);
			movement.add(-moveSpeed, 0);
		}
		player.getBody().setLinearVelocity(movement);
	}
	
	public void update(float delta) {
		
		handleInput();
		
		player.update(delta);
		enemy.update(delta, player);
		world.step(delta, 6, 2);
		
		logger.log();
	}
	
	public void render() {
		
		// draw tile map
		tmr.setView(cam);
		tmr.render();
		
		// set camera to follow player
		// need to check if the camera is off the screen
		Vector2 pos = new Vector2(Game.width / 2, Game.height / 2);
		if(player.getPosition().x * PPM - Game.width / 2 > 0) {
			if(player.getPosition().x * PPM + Game.width / 2 < (Integer) tileMap.getProperties().get("width") * tileSize) {
				pos.x = player.getPosition().x * PPM;
			} else {
				pos.x = (Integer) tileMap.getProperties().get("width") * tileSize - Game.width / 2;
			}
		}
		if(player.getPosition().y * PPM - Game.height / 2 > 0) {
			if(player.getPosition().y * PPM + Game.height / 2 < (Integer) tileMap.getProperties().get("height") * tileSize) {
				pos.y = player.getPosition().y * PPM;
			} else {
				pos.y = (Integer) tileMap.getProperties().get("height") * tileSize - Game.height / 2;
			}
		}
		cam.position.set(pos, 0);
		cam.update();
		
		// draw enemy
		enemy.render(sb);
		sb.setProjectionMatrix(cam.combined);
		
		// draw player last (on top)
		player.render(sb);
		
		// draw box2d debug world
		//renderer.render(world, b2dcam.combined);
	}
	
	public void dispose() {
		world.dispose();
	}
	
}
