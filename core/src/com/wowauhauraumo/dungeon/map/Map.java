package com.wowauhauraumo.dungeon.map;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.wowauhauraumo.dungeon.map.mapobjects.MapObject;

/**
 * Renders the TiledMap and MapObjects. Contains functions to abstract the MapLoader, i.e. to update a map object, get a
 * map object at a location, change map, and it also stores variables of the current map (e.g. tileSize)
 *
 * @author Louis Van Steene
 */
public class Map {
    private MapLoader mapLoader;
    private OrthogonalTiledMapRenderer tiledMapRenderer;

    private TiledMap tiledMap;

    private Area area;

    public Map() {
        mapLoader = new MapLoader();
    }

    public void setMap(Area area) {
        mapLoader.free();
        tiledMap = mapLoader.loadMap(area);
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
    }

    /**
     * Render the tilemap
     *
     * @param cam the camera to render on
     */
    public void render(OrthographicCamera cam) {
        tiledMapRenderer.setView(cam);
        tiledMapRenderer.render();
    }

    /**
     * Get a MapObject so that you can interact with it.
     *
     * @param coord the location of the object to get
     * @return the MapObject at that location, or null if there isn't one
     */
    public MapObject getMapObject(Vector2 coord) {
        return mapLoader.getObject(coord);
    }

    public int getTileSize() {
        return (int) tiledMap.getProperties().get("tilewidth");
    }

    public int getColumns() { return (int) tiledMap.getProperties().get("width"); }

    public int getRows() { return (int) tiledMap.getProperties().get("height"); }

    public Area getArea() { return area; }
}
