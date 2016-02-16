package com.wowauhauraumo.dungeon.util;

/**
 * Generic enum to be used wherever you need a four-way direction.
 */
public enum Direction {
    NORTH(0, 1), SOUTH(0, -1), EAST(1, 0), WEST(-1, 0), NONE(0, 0);

    public final int xMod;
    public final int yMod;

    Direction(int xMod, int yMod) {
        this.xMod = xMod;
        this.yMod = yMod;
    }
}
