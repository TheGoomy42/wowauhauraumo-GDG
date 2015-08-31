package com.wowauhauraumo.dungeon.maps;

import static com.wowauhauraumo.dungeon.managers.B2DVars.BIT_ENTITY;
import static com.wowauhauraumo.dungeon.managers.B2DVars.BIT_EXIT;
import static com.wowauhauraumo.dungeon.managers.B2DVars.BIT_WALL;
import static com.wowauhauraumo.dungeon.managers.B2DVars.PPM;
import static com.esotericsoftware.minlog.Log.*;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
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
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pool.Poolable;

/**
 * This class creates and stores the current map the player is on.
 * It loads in the tilemap and turns that into world bodies.
 * It also creates 'Portal' and 'Spawn' objects from data in the .tmx file (see below).
 * It uses LibGDX pools to contain these.
 * Tilemaps are made in Tiled:
 * http://www.mapeditor.org/
 * 
 * TO CLEAR UP SOME TERMINOLOGY:
 * 
 * portal - a way to travel from one map to another
 * spawn - a location where the player should be sent upon arriving in a map
 * 
 * @author TheGoomy42
 */
public class Map {
	
	// the width and height, in pixels, of a tile
	private int tileSize;
	// the object that renders the map to the screen
	private OrthogonalTiledMapRenderer tmr;
	// used to scale the tilemap to be bigger smaller
	// currently not used (changing this might bug everything out)
	private final int mapscale = 1;
	// the dimensions of the map
	private int mapWidth;
	private int mapHeight;
	// a reference to the Box2D world
	private World world;
	// the tilemap
	private TiledMap tileMap;
	
	// a pool to store the tiles
	private TilePool tiles;
	private Array<Tile> currentTiles;     // used to manage the pool
	// a pool to store the portals
	private PortalPool portals;
	private Array<Portal> currentPortals; // used to manage the pool
	
//  originally used simple arrays for this
//	private Array<Exit> exits;
//	private Array<Opening> openings;
//	private Array<Portal> portals;
	
	public Map() {
		debug("Initialising map...");
		tiles = new TilePool();
		currentTiles = new Array<Tile>();
		portals = new PortalPool();
		currentPortals = new Array<Portal>();
		// original dodgy code
//		exits = new Array<Exit>(false, 5);
//		openings = new Array<Opening>(false, 5);
	}
	
	/**
	 * This public function is used to switch the currently loaded map, e.g. when the player enters a portal.
	 * 
	 * @param map the ID of the map to load
	 * @param world a reference to the Box2D world
	 */
	public void createMap(Areas map, World world) {
		this.world = world;
		
		// load in the tilemap
		loadMap(map);
		
		// free up all of the objects in the pools
		portals.freeAll(currentPortals);
		currentPortals.clear();
		tiles.freeAll(currentTiles);
		currentTiles.clear();
		
		
		createTiles();
		
//		more remnants of the old system
//		for(Body t : tiles) {
//			if(t.getUserData() == "tile") if(world.getBodyCount() > 0) this.world.destroyBody(t);
//		}
//		for(Portal p : portals) {
//			System.out.println("portal loop body count: " + world.getBodyCount());
//			if(p.getBody().getUserData() instanceof Portal) if(world.getBodyCount() > 0) p.dispose();
//		}
	}
	
	//     - get's a portal from the current map, deactivates it and returns a reference to it
	public Portal getPortal(int id) {
		// loop through the loaded portals for the provided id
		for(Portal pp : currentPortals) {
			if(pp.getId() == id) {
				// turn the portal off
				pp.setActive(false);
				return pp;
			}
		}
		
		error("No portal found with that id!");
		return null;
		
	}
	
	/**
	 * Loads in the tilemap .tmx file.
	 * 
	 * @param map the ID of the map to load
	 */
	private void loadMap(Areas map) {
		switch(map) {
		case TOWN:
			tileMap = new TmxMapLoader().load("maps/town.tmx");
			break;
		case OVERWORLD:
			tileMap = new TmxMapLoader().load("maps/overworld.tmx");
			break;
		case NULL:
			error("Trying to load a null map!");
			break;
		default:
			tileMap = new TmxMapLoader().load("maps/town.tmx");
			break;
		}
	}
	
	/**
	 * Create the Tile and Portal instances using the loaded tilemap.
	 */
	private void createTiles() {
		tmr = new OrthogonalTiledMapRenderer(tileMap, mapscale);
		tileSize = (Integer) tileMap.getProperties().get("tilewidth");
		
		TiledMapTileLayer layer;
		
		// get the wall layer and create Box2D bodies that collide with the player
		layer = (TiledMapTileLayer) tileMap.getLayers().get("wall");
		createLayer(layer, BIT_WALL);
//		just realised that there is no point in creating a ton of
//		bodies that don't collide with anything
//		layer = (TiledMapTileLayer) tileMap.getLayers().get("floor");
//		createLayer(layer, BIT_EMPTY);
		// get the portal layer and create their objects
		MapObjects objects = tileMap.getLayers().get("portals").getObjects();
		createPortals(objects);
		
		// get the dimensions of the tilemap
		mapWidth = (int) ((Integer) tileMap.getProperties().get("width") * mapscale);
		mapHeight = (int) ((Integer) tileMap.getProperties().get("height") * mapscale);
	}
	
	/**
	 * Creates Tiles using a pool.
	 * 
	 * @param layer The layer to create bodies for
	 * @param bits Collision bits
	 */
	private void createLayer(TiledMapTileLayer layer, short bits) {
		// Bodies are made of ChainShapes because apparently normal rectangles have trouble
		ChainShape cs;
		
		// loop through the entire layer
		for(int row = 0; row < layer.getHeight(); row++) {
			for(int col = 0; col < layer.getWidth(); col++) {
				Cell cell = layer.getCell(col, row);
				
				// only create a body if there is a tile present
				if(cell == null) continue;
				if(cell.getTile() == null) continue;
				
				// not a normal box because that has problems with dragging along
				Vector2[] v = new Vector2[5];
				v[0] = new Vector2(-tileSize / 2 / PPM * mapscale, -tileSize / 2 / PPM * mapscale);
				v[1] = new Vector2(-tileSize / 2 / PPM * mapscale, tileSize / 2 / PPM * mapscale);
				v[2] = new Vector2(tileSize / 2 / PPM * mapscale, tileSize / 2 / PPM * mapscale);
				v[3] = new Vector2(tileSize / 2 / PPM * mapscale, -tileSize / 2 / PPM * mapscale);
				v[4] = v[0];
				cs = new ChainShape();
				cs.createChain(v);
				
				// add this tile to the pool
				Tile t = tiles.obtain();
				t.createBody(cs, row, col, bits);
				currentTiles.add(t);
			}
		}
	}

	/**
	 * Creates portals using a pool.
	 * 
	 * @param objects The collection of portal objects
	 */
	private void createPortals(MapObjects objects) {
		for(MapObject object : objects) {
			// the maps use both Polylines and Ellipses as portals
			Shape shape = null; // we can create a body with any kind of shape
			float x = 0, y = 0;
			if(object instanceof PolylineMapObject) {
				float[] vertices = ((PolylineMapObject) object).getPolyline().getTransformedVertices();
		        Vector2[] worldVertices = new Vector2[vertices.length / 2];

		        for (int i = 0; i < vertices.length / 2; ++i) {
		            worldVertices[i] = new Vector2();
		            worldVertices[i].x = vertices[i * 2] / PPM * mapscale;
		            worldVertices[i].y = vertices[i * 2 + 1] / PPM * mapscale;
		        }

		        shape = new ChainShape();
		        ((ChainShape) shape).createChain(worldVertices);
		        // chain shape portals don't currently need positions as they are one way
			} else if(object instanceof EllipseMapObject) {
				float radius = ((EllipseMapObject) object).getEllipse().width / PPM * mapscale;
				
				shape = new CircleShape();
				x = ((EllipseMapObject) object).getEllipse().x / PPM * mapscale;
				y = ((EllipseMapObject) object).getEllipse().y / PPM * mapscale;
				((CircleShape) shape).setRadius(radius);
			}
			// if we actually created a shape
			// (this should only fail if a map contains portals that aren't the above shapes)
			if(shape != null) {
				// get a portal from the pool
				Portal p = portals.obtain();
				// set portal properties
				p.createBody(shape, x, y);
				p.setEntranceToMap(Areas.valueOf((String) object.getProperties().get("entranceTo")));
				p.setEntranceToId(Integer.parseInt((String) object.getProperties().get("entranceToSpawnId")));
				p.setId(Integer.parseInt((String) object.getProperties().get("id")));
				p.setOneWay(OneWayType.valueOf((String) object.getProperties().get("oneWay")));
				// add to the active portal array
				currentPortals.add(p);
			} else error("Tried to create a portal with an unrecognised shape!");
		}
	}
	
	/**
	 * Render the tilemap
	 * 
	 * @param cam The camera to use to render
	 */
	public void render(OrthographicCamera cam) {
		tmr.setView(cam);
		tmr.render();
	}
	
	public int getWidth() { return mapWidth; }
	public int getHeight() { return mapHeight; }
	public int getTileSize() { return tileSize; }
	
	/**
	 * Contains all of the map identifiers.
	 * 
	 * @author TheGoomy42
	 */
	public enum Areas {
		NULL, OVERWORLD, TOWN
	}
	
	/**
	 * This pool is used to store all of the tiles.
	 * 
	 * @author TheGoomy42
	 */
	private class TilePool extends Pool<Tile> {
		@Override
		protected Tile newObject() {
			return new Tile();
		}
	}
	
	/**
	 * The tile class. Basically a Poolable container for the Box2D body
	 * 
	 * @author TheGoomy42
	 */
	private class Tile implements Poolable {
		private Body body;
		
		public void createBody(Shape shape, int row, int col, short bits) {
			BodyDef bdef = new BodyDef();
			FixtureDef fdef = new FixtureDef();
			
			bdef.type = BodyType.StaticBody;
			bdef.position.set((col + 0.5f) * tileSize / PPM * mapscale, (row + 0.5f) * tileSize / PPM * mapscale);
			fdef.friction = 0;
			fdef.shape = shape;
			fdef.filter.categoryBits = bits;
			fdef.filter.maskBits = BIT_ENTITY;
			fdef.isSensor = false;
			
			body = world.createBody(bdef);
			body.createFixture(fdef);
			body.setUserData(this);
		}

		@Override
		public void reset() {
			// destroy it's body
			if(body != null && world.getBodyCount() > 0) world.destroyBody(body);
		}
	}
	
	/**
	 * This pool is used to store all of the portals.
	 * 
	 * @author TheGoomy42
	 */
	private class PortalPool extends Pool<Portal> {
		@Override
		protected Portal newObject() {
			return new Portal();
		}
	}
	
	/**
	 * The portal class. Poolable object that contains properties for every portal.
	 * 
	 * @author TheGoomy42
	 */
	public class Portal implements Poolable {
		private Body body;
		private int entranceToId;
		private int id;
		private Areas map;
		private OneWayType oneWay;
		private boolean active;
		
		public void createBody(Shape shape, float x, float y) {
			active = true;
			
			BodyDef bdef = new BodyDef();
			FixtureDef fdef = new FixtureDef();
			
			bdef.type = BodyType.StaticBody;
			body = world.createBody(bdef);

	        fdef.shape = shape;
	        fdef.filter.categoryBits = BIT_EXIT;
	        fdef.filter.maskBits = BIT_ENTITY;
	        fdef.isSensor = true;
			
			body.createFixture(fdef).setUserData(this);
			body.setTransform(x, y, 0);
		}
		
		@Override
		public void reset() {
			// reset all properties and destroy the body
			entranceToId = 0;
			id = 0;
			map = Areas.NULL;
			active = true;
			oneWay = OneWayType.TWO_WAY;
			if(body != null && world.getBodyCount() > 0)
				world.destroyBody(body);
		}
		
		public void setEntranceToId(int id) { entranceToId = id; }
		public void setId(int id) { this.id = id; }
		public void setEntranceToMap(Areas m) { map = m; }
		public void setActive(boolean b) { active = b; }
		public void setOneWay(OneWayType e) { oneWay = e; }
		
		public boolean isActive() {
			// cater for one way portals
			switch(oneWay) {
			case ALWAYS_ON:
				return true;
			case ALWAYS_OFF:
				return false;
			case TWO_WAY:
			default:
				return active;
			}
		}
		public OneWayType oneWayType() { return oneWay; }
		public int getEntranceToId() { return entranceToId; }
		public int getId() { return id; }
		public Areas getEntranceToMap() { return map; }
		public Body getBody() { return body; }
		
	}
	private enum OneWayType {
		ALWAYS_ON, ALWAYS_OFF, TWO_WAY
	}
	
//	/**
//	 * A one-way portal that you can not enter, effectively a spawn.
//	 * 
//	 * @author TheGoomy42
//	 */
//	public class TrapPortal extends Portal {
//		@Override
//		public void setActive(boolean b) { debug("Trying to set TrapPortal's active variable. Doing nothing..."); }
//		@Override
//		public boolean isActive() { return false; }
//	}
}
