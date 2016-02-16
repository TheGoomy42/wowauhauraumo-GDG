package com.wowauhauraumo.dungeon.map;

/**
 * Stores the names of all of the maps and their names.
 */
public enum Area {
    NULL, OVERWORLD(false, "maps/object_test");

    public final boolean friendly;
    public final String name;

    Area() {
        friendly = true;
        name = null;
    }

    Area(boolean friendly, String name) {
        this.friendly = friendly;
        this.name = name;
    }
}
