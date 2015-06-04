package com.wowauhauraumo.dungeon.states;

import static com.wowauhauraumo.dungeon.managers.B2DVars.BIT_EMPTY;
import static com.wowauhauraumo.dungeon.managers.B2DVars.BIT_ENTITY;
import static com.wowauhauraumo.dungeon.managers.B2DVars.BIT_WALL;
import static com.wowauhauraumo.dungeon.managers.B2DVars.PPM;

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
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.wowauhauraumo.dungeon.entities.Player;
import com.wowauhauraumo.dungeon.main.Game;
import com.wowauhauraumo.dungeon.managers.GameContactListener;
import com.wowauhauraumo.dungeon.managers.GameKeys;
import com.wowauhauraumo.dungeon.managers.GameStateManager;
import com.wowauhauraumo.util.pathfinding.Mover;
import com.wowauhauraumo.util.pathfinding.TileBasedMap;

public class Play extends GameState implements TileBasedMap {

	// box2d stuff
	//private FPSLogger logger;
	private World world;
	@SuppressWarnings("unused")
	private Box2DDebugRenderer renderer;
	private OrthographicCamera b2dcam;
	
	private Player player;
	private float moveSpeed = 0.79f;
	private boolean[] playerColliding = new boolean[4];
	
	private TiledMap tileMap;
	private OrthogonalTiledMapRenderer tmr;
	private float tileSize;
	private final float mapscale = 1f;
	private int mapWidth;
	private int mapHeight;
	private boolean[][] visited;
	
	public Play(GameStateManager gsm) {
		super(gsm);
		
		// create box2d world etc.
		world = new World(new Vector2(0, 0), true);
		world.setContactListener(new GameContactListener(this));
		renderer = new Box2DDebugRenderer();
		//logger = new FPSLogger();
		
		createPlayer();
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
		shape.setAsBox(6 / PPM, 6 / PPM);
		
		// create main fixture
		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		fdef.filter.categoryBits = BIT_ENTITY;
		fdef.filter.maskBits = BIT_WALL;
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
		
		player = new Player(body);
	}
	
	private void createTiles() {
		tileMap = new TmxMapLoader().load("maps/town.tmx");
		tmr = new OrthogonalTiledMapRenderer(tileMap, mapscale);
		tileSize = (Integer) tileMap.getProperties().get("tilewidth");
		
		TiledMapTileLayer layer;
		
		layer = (TiledMapTileLayer) tileMap.getLayers().get("wall");
		createLayer(layer, BIT_WALL);
		layer = (TiledMapTileLayer) tileMap.getLayers().get("floor");
		createLayer(layer, BIT_EMPTY);
		
		mapWidth = (int) ((Integer) tileMap.getProperties().get("width") * mapscale);
		mapHeight = (int) ((Integer) tileMap.getProperties().get("height") * mapscale);
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
	
	public void setPlayerCollding(int i, boolean b) {
		playerColliding[i] = b;
	}
	
	public void handleInput() {
		
		Vector2 movement = new Vector2(0,0);
		
		if(GameKeys.isDown(GameKeys.UP)) {
			movement.add(0, moveSpeed);
		} else if(GameKeys.isDown(GameKeys.DOWN)) {
			movement.add(0, -moveSpeed);
		} else if(GameKeys.isDown(GameKeys.RIGHT)) {
			movement.add(moveSpeed, 0);
			player.setRight(true);
		} else if(GameKeys.isDown(GameKeys.LEFT)) {
			movement.add(-moveSpeed, 0);
			player.setRight(false);
		}
		player.getBody().setLinearVelocity(movement);
	}
	
	public void update(float delta) {
		
		handleInput();
		
		player.update(delta, playerColliding);
		world.step(delta, 6, 2);
		
		//logger.log();
	}
	
	public void render() {
		
		// draw tile map
		tmr.setView(cam);
		tmr.render();
		
		// set camera to follow player
		// need to check if the camera is off the screen
		Vector2 pos = new Vector2(Game.width / 2, Game.height / 2);
		if(player.getPosition().x * PPM - Game.width / 2 > 0) {
			if(player.getPosition().x * PPM + Game.width / 2 < mapWidth * tileSize) {
				pos.x = player.getPosition().x * PPM;
			} else {
				pos.x = mapWidth * tileSize - Game.width / 2;
			}
		}
		if(player.getPosition().y * PPM - Game.height / 2 > 0) {
			if(player.getPosition().y * PPM + Game.height / 2 < mapHeight * tileSize) {
				pos.y = player.getPosition().y * PPM;
			} else {
				pos.y = mapHeight * tileSize - Game.height / 2;
			}
		}
		cam.position.set(pos, 0);
		b2dcam.position.set(pos.x / PPM, pos.y / PPM, 0);
		b2dcam.update();
		cam.update();
		
		// draw player last (on top)
		sb.setProjectionMatrix(cam.combined);
		player.render(sb);
		
		// draw box2d debug world
		//renderer.render(world, b2dcam.combined);
	}
	
	public void dispose() {
		world.dispose();
	}

	@Override
	public int getWidthInTiles() {
		return (Integer) tileMap.getProperties().get("width");
	}

	@Override
	public int getHeightInTiles() {
		return (Integer) tileMap.getProperties().get("height");
	}

	@Override
	public boolean blocked(Mover mover, int x, int y) {
		TiledMapTileLayer layer = (TiledMapTileLayer) (tileMap.getLayers().get("blocked"));
		Cell cell = layer.getCell(x, y);
		if(cell == null) return false;
		return true;
	}

	@Override
	public float getCost(Mover mover, int sx, int sy, int tx, int ty) {
		return 1;
	}

	@Override
	public void pathFinderVisited(int x, int y) {
		visited[x][y] = true;
	}
	
}
