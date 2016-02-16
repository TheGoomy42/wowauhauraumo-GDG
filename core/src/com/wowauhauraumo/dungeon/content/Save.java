package com.wowauhauraumo.dungeon.content;

/**
 * Manages the saving and loading of the save files (JSON format)
 * <p/>
 * Saves player statistics, i.e. name, party members, inventories, to the profile save (save/save0/profile.json). Saves
 * quests & progress to the quests save (save/save0/quests.json) Saves settings to the global save
 * (save/settings.json).
 * <p/>
 * Map saving and loading is not handled here, but rather in MapLoader and should only be accessed by Map.
 *
 * @author Louis Van Steene
 */
public class Save {
    public static final String TMX_FILE_EXTENSION = ".tmx";
    public static final String DATA_DIR = ".wowauhauraumo/";

}
