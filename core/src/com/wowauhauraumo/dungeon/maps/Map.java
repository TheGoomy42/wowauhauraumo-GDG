package com.wowauhauraumo.dungeon.maps;

import static com.wowauhauraumo.dungeon.managers.B2DVars.BIT_EMPTY;
import static com.wowauhauraumo.dungeon.managers.B2DVars.BIT_ENTITY;
import static com.wowauhauraumo.dungeon.managers.B2DVars.BIT_WALL;
import static com.wowauhauraumo.dungeon.managers.B2DVars.PPM;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
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
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class Map {
	
	private int tileSize;
	private OrthogonalTiledMapRenderer tmr;
	private final int mapscale = 1;
	private int mapWidth;
	private int mapHeight;
	private World world;
	private TiledMap tileMap;
	private Array<Body> tiles;
	private Array<Exit> exits;
	
	public Map() {
		tiles = new Array<Body>(false, 100);
		exits = new Array<Exit>(false, 5);
	}
	
	public void createMap(int map, World world) {
		this.world = world;
		for(Body tile : tiles) {
			this.world.destroyBody(tile);
		}
		for(Exit exit : exits) {
			exit.dispose();
		}
		switch(map) {
		case Maps.TOWN:
			tileMap = new TmxMapLoader().load("maps/town.tmx");
			break;
		case Maps.OVERWORLD:
			tileMap = new TmxMapLoader().load("maps/overworld.tmx");
		default:
			new TmxMapLoader().load("maps/town.tmx");
			break;
		}
		createTiles();
	}
	
	private void createTiles() {
		tmr = new OrthogonalTiledMapRenderer(tileMap, mapscale);
		tileSize = (Integer) tileMap.getProperties().get("tilewidth");
		
		TiledMapTileLayer layer;
		
		layer = (TiledMapTileLayer) tileMap.getLayers().get("wall");
		createLayer(layer, BIT_WALL);
		layer = (TiledMapTileLayer) tileMap.getLayers().get("floor");
		createLayer(layer, BIT_EMPTY);
		MapObjects objects = tileMap.getLayers().get("exits").getObjects();
		createExit(objects);
		
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
				
				tiles.add(world.createBody(bdef));
				tiles.peek().createFixture(fdef);
			}
		}
	}
	
	private void createExit(MapObjects objects) {
		
		for(MapObject object : objects) {
			if(object instanceof PolylineMapObject) {
				float[] vertices = ((PolylineMapObject) object).getPolyline().getTransformedVertices();
		        Vector2[] worldVertices = new Vector2[vertices.length / 2];

		        for (int i = 0; i < vertices.length / 2; ++i) {
		            worldVertices[i] = new Vector2();
		            worldVertices[i].x = vertices[i * 2] / PPM * mapscale;
		            worldVertices[i].y = vertices[i * 2 + 1] / PPM * mapscale;
		        }

		        ChainShape chain = new ChainShape(); 
		        chain.createChain(worldVertices);

		        exits.add(new Exit(chain, world, 
		        		new Vector2(Integer.parseInt((String) object.getProperties().get("overworldX")), 
		        				Integer.parseInt((String) object.getProperties().get("overworldY")))));
			}
		}
	}
	
	public void render(OrthographicCamera cam) {
		tmr.setView(cam);
		tmr.render();
	}
	
	public int getWidth() { return mapWidth; }
	public int getHeight() { return mapHeight; }
	public int getTileSize() { return tileSize; }
	
	public class Maps {
		public static final int OVERWORLD = 0;
		public static final int TOWN = 1;
	}
	
}
