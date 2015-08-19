package com.wowauhauraumo.dungeon.maps;

import static com.wowauhauraumo.dungeon.managers.B2DVars.BIT_ENTITY;
import static com.wowauhauraumo.dungeon.managers.B2DVars.BIT_EXIT;
import static com.wowauhauraumo.dungeon.managers.B2DVars.BIT_WALL;
import static com.wowauhauraumo.dungeon.managers.B2DVars.PPM;

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

/*
 * TO CLEAR UP SOME TERMINOLOGY:
 * 
 * portal - a way to travel from one map to another
 * spawn - a location where the player should be sent upon arriving in a map
 */

//TODO add a logger library to record what is happening

public class Map {
	
	private int tileSize;
	private OrthogonalTiledMapRenderer tmr;
	private final int mapscale = 1;
	private int mapWidth;
	private int mapHeight;
	private World world;
	private TiledMap tileMap;
	
	private TilePool tiles;
	private Array<Tile> currentTiles;
	
	private PortalPool portals;
	private Array<Portal> currentPortals;
//	private Array<Exit> exits;
//	private Array<Opening> openings;
//	private Array<Portal> portals;
	
	public Map() {
		tiles = new TilePool();
		currentTiles = new Array<Tile>(true, 100);
//		exits = new Array<Exit>(false, 5);
//		openings = new Array<Opening>(false, 5);
		portals = new PortalPool();
		currentPortals = new Array<Portal>();
	}
	
	private void loadMap(MapTypes map) {
		switch(map) {
		case TOWN:
			tileMap = new TmxMapLoader().load("maps/town.tmx");
			break;
		case OVERWORLD:
			tileMap = new TmxMapLoader().load("maps/overworld.tmx");
			break;
		case NULL:
			// log
			break;
		default:
			tileMap = new TmxMapLoader().load("maps/town.tmx");
			break;
		}
	}
	
	public void createMap(MapTypes map, World world) {
		this.world = world;
		
		loadMap(map);
		
//		for(Portal p : portals) {
//			System.out.println("portal loop body count: " + world.getBodyCount());
//			if(p.getBody().getUserData() instanceof Portal) if(world.getBodyCount() > 0) p.dispose();
//		}
		portals.freeAll(currentPortals);
		currentPortals.clear();
//		for(Body t : tiles) {
//			if(t.getUserData() == "tile") if(world.getBodyCount() > 0) this.world.destroyBody(t);
//		}
		tiles.freeAll(currentTiles);
		currentTiles.clear();
		createTiles();
	}
	
	//TODO make this method run when the map is loaded
	public Vector2 getSpawnCoords(MapTypes map, int spawnId) {
		loadMap(map);
		MapObjects objects = tileMap.getLayers().get("spawns").getObjects();
		for(MapObject s : objects) {
			if(Integer.parseInt((String) s.getProperties().get("spawnId")) == spawnId) {
				Vector2 v = new Vector2((Float) s.getProperties().get("x"), (Float) s.getProperties().get("y"));
				return v;
			}
		}
		
		return null;
	}
	
	private void createTiles() {
		tmr = new OrthogonalTiledMapRenderer(tileMap, mapscale);
		tileSize = (Integer) tileMap.getProperties().get("tilewidth");
		
		TiledMapTileLayer layer;
		
		layer = (TiledMapTileLayer) tileMap.getLayers().get("wall");
		createLayer(layer, BIT_WALL);
//		layer = (TiledMapTileLayer) tileMap.getLayers().get("floor");
//		createLayer(layer, BIT_EMPTY);
		MapObjects objects = tileMap.getLayers().get("portals").getObjects();
		createPortals(objects);
		
		mapWidth = (int) ((Integer) tileMap.getProperties().get("width") * mapscale);
		mapHeight = (int) ((Integer) tileMap.getProperties().get("height") * mapscale);
	}
	
	private void createLayer(TiledMapTileLayer layer, short bits) {
		ChainShape cs;
		
		for(int row = 0; row < layer.getHeight(); row++) {
			for(int col = 0; col < layer.getWidth(); col++) {
				Cell cell = layer.getCell(col, row);
				
				if(cell == null) continue;
				if(cell.getTile() == null) continue;
//				bdef.type = BodyType.StaticBody;
//				bdef.position.set((col + 0.5f) * tileSize / PPM * mapscale, (row + 0.5f) * tileSize / PPM * mapscale);
				
				// not a normal box because that has problems with dragging along
				Vector2[] v = new Vector2[5];
				v[0] = new Vector2(-tileSize / 2 / PPM * mapscale, -tileSize / 2 / PPM * mapscale);
				v[1] = new Vector2(-tileSize / 2 / PPM * mapscale, tileSize / 2 / PPM * mapscale);
				v[2] = new Vector2(tileSize / 2 / PPM * mapscale, tileSize / 2 / PPM * mapscale);
				v[3] = new Vector2(tileSize / 2 / PPM * mapscale, -tileSize / 2 / PPM * mapscale);
				v[4] = v[0];
				cs = new ChainShape();
				cs.createChain(v);
				
				Tile t = tiles.obtain();
				t.createBody(cs, row, col, bits);
				currentTiles.add(t);
				
//				fdef.friction = 0;
//				fdef.shape = cs;
//				fdef.filter.categoryBits = bits;
//				fdef.filter.maskBits = BIT_ENTITY;
//				fdef.isSensor = false;
//				
//				Body body = world.createBody(bdef);
//				body.createFixture(fdef);
//				body.setUserData("tile");
//				tiles.add(body);
			}
		}
	}
	

	
	private void createPortals(MapObjects objects) {
		for(MapObject object : objects) {
			Shape shape = null;
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
			} else if(object instanceof EllipseMapObject) {
				Vector2 loc = new Vector2(((EllipseMapObject) object).getEllipse().x / PPM * mapscale, 
						((EllipseMapObject) object).getEllipse().y / PPM * mapscale);
				float radius = ((EllipseMapObject) object).getEllipse().width / PPM * mapscale;
				
				shape = new CircleShape();
				((CircleShape) shape).setPosition(loc);
				((CircleShape) shape).setRadius(radius);
			}
			if(shape != null){
				Portal p = portals.obtain();
				p.createBody(shape);
				p.setMap(MapTypes.valueOf((String) object.getProperties().get("entranceTo")));
				p.setSpawnId(Integer.parseInt((String) object.getProperties().get("entranceToSpawnId")));
				currentPortals.add(p);
			}
//				portals.add(new Portal(shape,
//						   MapTypes.valueOf((String) object.getProperties().get("entranceTo")), 
//						   Integer.parseInt((String) object.getProperties().get("entranceToSpawnId"))));
		}
	}
	
	public void render(OrthographicCamera cam) {
		tmr.setView(cam);
		tmr.render();
	}
	
	public int getWidth() { return mapWidth; }
	public int getHeight() { return mapHeight; }
	public int getTileSize() { return tileSize; }
	
	public enum MapTypes {
		NULL, OVERWORLD, TOWN
	}
	
	private class TilePool extends Pool<Tile> {
		@Override
		protected Tile newObject() {
			return new Tile();
		}
	}
	
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
			if(body != null && world.getBodyCount() > 0) world.destroyBody(body);
		}
	}
	
	private class PortalPool extends Pool<Portal> {
		@Override
		protected Portal newObject() {
			return new Portal();
		}
	}
	
	public class Portal implements Poolable {
		private Body body;
		private int endSpawnId;
		private MapTypes map;
		
//		public Portal(Shape shape, MapTypes map, int endSpawnId) {
//			this.endSpawnId = endSpawnId;
//			this.map = map;
//		}
		
		public void createBody(Shape shape) {
			BodyDef bdef = new BodyDef();
			FixtureDef fdef = new FixtureDef();
			
			bdef.type = BodyType.StaticBody;
			body = world.createBody(bdef);

	        fdef.shape = shape;
	        fdef.filter.categoryBits = BIT_EXIT;
	        fdef.filter.maskBits = BIT_ENTITY;
	        fdef.isSensor = true;
			
			body.createFixture(fdef).setUserData(this);
		}
		
		@Override
		public void reset() {
			endSpawnId = 0;
			map = MapTypes.NULL;
			if(body != null && world.getBodyCount() > 0)
				world.destroyBody(body);
		}
		
		public void setSpawnId(int id) { endSpawnId = id; }
		public void setMap(MapTypes m) { map = m; } 
		
		public int getSpawnId() { return endSpawnId; }
		public MapTypes getEndMap() { return map; }
		public Body getBody() { return body; }
	}
	
//	OLD CRAPPY SYSTEM
//	private void createExit(MapObjects objects) {
//		for(MapObject object : objects) {
//			if(object instanceof PolylineMapObject) {
//				float[] vertices = ((PolylineMapObject) object).getPolyline().getTransformedVertices();
//		        Vector2[] worldVertices = new Vector2[vertices.length / 2];
//
//		        for (int i = 0; i < vertices.length / 2; ++i) {
//		            worldVertices[i] = new Vector2();
//		            worldVertices[i].x = vertices[i * 2] / PPM * mapscale;
//		            worldVertices[i].y = vertices[i * 2 + 1] / PPM * mapscale;
//		        }
//
//		        ChainShape chain = new ChainShape(); 
//		        chain.createChain(worldVertices);
//		        
//		        
//		        exits.add(new Exit(chain, world, 
//		        		MapTypes.valueOf((String) object.getProperties().get("endLoc")), 
//		        		Integer.parseInt((String) object.getProperties().get("endSpawnId"))));
//			}
//		}
//	}
//	
//	private void createEntrance(MapObjects objects) {
//		for(MapObject object : objects) {
//			if(object instanceof EllipseMapObject) {
//				Vector2 loc = new Vector2(((EllipseMapObject) object).getEllipse().x / PPM * mapscale, 
//						((EllipseMapObject) object).getEllipse().y / PPM * mapscale);
//				float radius = ((EllipseMapObject) object).getEllipse().width / PPM * mapscale;
//				
//				CircleShape circle = new CircleShape();
//				circle.setPosition(loc);
//				circle.setRadius(radius);
//				
//				System.out.println(object.getProperties().get("entranceTo"));
//				
//				openings.add(new Opening(circle, world, 
//						   MapTypes.valueOf((String) object.getProperties().get("entranceTo")), 
//						   Integer.parseInt((String) object.getProperties().get("entranceToSpawnId"))));
//			}
//		}
//	}
	
}
