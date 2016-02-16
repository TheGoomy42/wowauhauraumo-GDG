package com.wowauhauraumo.dungeon.content;

/**
 * Stores the text prefixes for the different jobs' sprites.
 */
public enum Sprite {
    WARRIOR("warrior"), WMAGE("wmage"), BMAGE("bmage"), RMAGE("rmage"), MONK("monk"), THIEF("thief");

    private final String prefix;

    Sprite(String prefix) {
        this.prefix = prefix;
    }

    public String getTexture() { return prefix; }
}
