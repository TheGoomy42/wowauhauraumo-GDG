package com.wowauhauraumo.dungeon.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import com.wowauhauraumo.dungeon.content.Save;
import com.wowauhauraumo.dungeon.map.mapobjects.*;
import sun.plugin.dom.exception.InvalidStateException;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import static com.esotericsoftware.minlog.Log.error;

/**
 * Loads in a .tmx file and its corresponding profile json file, if it exists. Stores a HashMap < Vector2, MapObject >
 * of any map objects present.
 * <p/>
 * The .tmx file stores two layers: a floor layer which is freely walkable (movement can be restricted to vehicles via
 * tile properties) and an object layer, which stores data based on tile properties. These tile properties specify a
 * name which is then looked up in the corresponding json file, if it exists, to get its current state (e.g. has a chest
 * been opened).
 * <p/>
 * If the json file does not exist is should be created, storing all of the default states of the map objects, which are
 * stored in the tile map. This json file should be updated every time a map object is modified.
 *
 * @author Louis Van Steene
 */
class MapLoader {
    private HashMap<Vector2, MapObject> objectMap;

    private static final HashMap<Class<? extends MapObject>, String> classToStringMapping;
    private static final HashMap<String, Class<? extends MapObject>> stringToClassMapping;

    private FileHandle saveFile;
    private boolean loaded;
    private Area area;

    MapLoader() {
        objectMap = new HashMap<>();
    }

    static {
        classToStringMapping = new HashMap<>();
        stringToClassMapping = new HashMap<>();
        addMapping(MapObjectHill.class, "hill");
        addMapping(MapObjectPortal.class, "portal");
        addMapping(MapObjectTreasure.class, "treasure");
        addMapping(MapObjectDoor.class, "door");
        addMapping(MapObjectWater.class, "water");
        addMapping(MapObjectLava.class, "lava");
    }

    private static void addMapping(Class<? extends MapObject> objectClass, String name) {
        if(stringToClassMapping.containsKey(name)) {
            throw new IllegalArgumentException("ID is already registered: " + name);
        }
        else if(objectClass == null) {
            throw new IllegalArgumentException("Cannot register null class for ID: " + name);
        }
        else {
            classToStringMapping.put(objectClass, name);
            stringToClassMapping.put(name, objectClass);
        }
    }

    /**
     * Loads in a new tile map.
     *
     * @param map the map to load
     */
    TiledMap loadMap(Area map) {
        free();
        this.area = map;
        // load tmx
        TmxMapLoader loader = new TmxMapLoader();
        TiledMap tiledMap = loader.load(map.name + Save.TMX_FILE_EXTENSION);

        // get the object layer
        TiledMapTileLayer layer = (TiledMapTileLayer) tiledMap.getLayers().get("objects");

        // find objects
        // loop through all cells on layer
        for(int col = 0; col < layer.getWidth(); col++) {
            for(int row = 0; row < layer.getHeight(); row++) {
                TiledMapTileLayer.Cell cell = layer.getCell(col, row);

                // check that there is an object on this tile
                if(cell == null) continue;
                if(cell.getTile() == null) continue;
                if(cell.getTile().getProperties().get("name") == "") continue;

                // this cell is an object so create one for it
                String name = (String) cell.getTile().getProperties().get("name");

                if(stringToClassMapping.get(name) == null)
                    throw new NullPointerException("No map object found with name: " + name);

                try {
                    MapObject o = stringToClassMapping.get(name).getDeclaredConstructor().newInstance();
                    String specialVars = (String) cell.getTile().getProperties().get("special_vars");
                    // set default properties stored in tiledmap
                    o.setProperties(name, State.valueOf((String) cell.getTile().getProperties().get("default_state")),
                                    specialVars.split(","), new Vector2(col, row));
                    objectMap.put(new Vector2(col, row), o);
                } catch(InstantiationException | IllegalAccessException e) {
                    throw new IllegalArgumentException("No MapObject found with ID: " + name, e);
                } catch(NoSuchMethodException | InvocationTargetException e) {
                    error("MAPLOADER", "Failed to initialise MapObject");
                    throw new IllegalArgumentException(e);
                }
            }
        }

        // load map object properties
        loadJson();

        loaded = true;

        return tiledMap;
    }

    private void loadJson() {
        // check to see if there is a save/save0/[Area].json file present
        saveFile = Gdx.files.external(Save.DATA_DIR + "/save/save3/" + area.name + ".json");

        if(!saveFile.exists()) {
            try {
                saveFile.parent().mkdirs();
                if(!saveFile.file().createNewFile()) throw new IOException("JSON file already exists!");
                writeJson(saveFile);
            } catch(IOException e) {
                throw new IllegalArgumentException("Could not write JSON: " + saveFile.path(), e);
            }
        }
        else {
            // read in file
            JsonReader jsonReader = new JsonReader();
            JsonValue results = jsonReader.parse(saveFile);
            for(Vector2 coord : objectMap.keySet()) {
                JsonValue record = results.get(vectorToString(coord));
                if(record != null) {
                    objectMap.get(coord).setProperties(record.get("name").asString(),
                                                       State.valueOf(record.get("state").asString()),
                                                       record.get("special").asStringArray(), coord);
                }
                else {
                    // theoretically should try to add the map object
                    // for now just fail
                    error("JSON", "Save file incomplete for coord: " + vectorToString(coord));
                    throw new InvalidStateException("Could not load JSON: " + saveFile.path());
                }
            }
        }
    }

    private void writeJson(FileHandle file) throws IOException {
        try(
                FileWriter fileWriter = new FileWriter(file.file());
                JsonWriter jsonWriter = new JsonWriter(fileWriter)
        ) {
            // write default values for new file
            jsonWriter.object(); // enclosing object
            for(Vector2 coord : objectMap.keySet()) {
                MapObject mapObject = objectMap.get(coord);

                jsonWriter.object(vectorToString(coord))
                          .set("name", mapObject.getName())
                          .set("state", mapObject.getState())
                          .array("special");
                for(String var : mapObject.getSpecialVars()) {
                    jsonWriter.value(var);
                }
                jsonWriter.pop(); // close array
                jsonWriter.pop(); // close object
            }
        } // close streams automatically
    }

    private void free() {
        if(!loaded) return;
        try {
            writeJson(saveFile);
        } catch(IOException e) {
            throw new IllegalArgumentException("Could not save map to json.", e);
        }
        objectMap.clear();
        loaded = false;
    }

    /**
     * Changes an objects state.
     *
     * @param coord    the coordinate of the object
     * @param newState the state to change it to
     */
    void editObject(Vector2 coord, State newState) {
        objectMap.get(coord).changeState(newState);
    }


    // helper methods

    Vector2 stringToVector(String string) {
        return new Vector2(Integer.parseInt(string.split(":")[0]), Integer.parseInt(string.split(":")[1]));
    }

    String vectorToString(Vector2 vector) {
        return (int) vector.x + ":" + (int) vector.y;
    }

    MapObject getObject(Vector2 coord) { return objectMap.get(coord); }
}
