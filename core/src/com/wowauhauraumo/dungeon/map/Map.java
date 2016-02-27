package com.wowauhauraumo.dungeon.map;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.wowauhauraumo.dungeon.map.mapobjects.MapObject;

import static com.esotericsoftware.minlog.Log.error;

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
     * Check the tiled map properties for the destination map of the portal at the given position
     * @param position a Vector2 coordinate of the tile
     * @return a class containing the area representing the map the portal leads to and the spawn position
     */
    public PortalData getDataFromPortal(Vector2 position) {
        // stored in special vars so get like any other special var
        String[] data = mapLoader.getObject(position).getSpecialVars();
        if(data == null) {
            error("MAP", "Found no portal data for portal at: " + mapLoader.vectorToString(position));
            return null;
        }
        return new PortalData(Area.valueOf(data[0]), mapLoader.stringToVector(data[1]));
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

    /**
     * Small class to store data stored inside a PortalMapObject.
     */
    public class PortalData {
        public Area destination;
        public Vector2 spawnPosition;

        public PortalData(Area a, Vector2 v) {
            destination = a;
            spawnPosition = v;
        }
    }
}
