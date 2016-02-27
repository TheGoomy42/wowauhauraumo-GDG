package com.wowauhauraumo.dungeon.map.mapobjects;

import com.badlogic.gdx.math.Vector2;
import com.wowauhauraumo.dungeon.screens.PlayScreen;

/**
 * Object to represent anything that you can interact with on a map.
 * <p/>
 * <p/>
 * Types of MapObject: - Portal - teleports the player to another map - states: ACTIVE, INACTIVE - special vars:
 * destination - Hill - cannot be walked through but can be flown over - states: NONE - Door - can be opened - states:
 * CLOSED, OPEN - Water - cannot be walked through but can be flown/sailed over - states: NONE - Treasure - can be
 * opened with the items placed into inventory - states: OPEN, CLOSED - special vars: loot - Lava - deal 1 dmg to all
 * characters when walked over - states: NONE - BossStone - initiates bullet hell boss battle - states: READY, USED -
 * special vars: bossType
 */
public abstract class MapObject {
    protected String name;
    protected Vector2 position;
    protected State state;
    protected String[] specialVars;

    /**
     * Changes the MapObject state. This can do whatever it needs, e.g. a door opening would change its sprite to be an
     * open door.
     * <p/>
     * No default method as the state should not be able to be changed externally, overriding criteria.
     *
     * @param state the state to change to
     */
    public abstract void changeState(State state);

    /**
     * Performs collision logic and do anything that should happen on collision, e.g. open the door, start a battle
     *
     * @param isSailing if the player is on a boat
     * @param isFlying  if the player is on an airship
     * @param screen    the play screen
     * @return true if the MapObject should prevent movement
     */
    public abstract boolean collide(boolean isSailing, boolean isFlying, PlayScreen screen);

    /**
     * Sets properties when the map is loaded.
     *
     * @param state       the initial state
     * @param specialVars an array of parameters
     */
    public void setProperties(String name, State state, String[] specialVars, Vector2 position) {
        this.name = name;
        this.state = state;
        this.specialVars = specialVars;
        this.position = position;
    }

    /**
     * Draws the MapObject as a sprite
     *
     * @param delta the time since the last render
     */
    public abstract void render(float delta);

    public String getName() {
        return name;
    }

    public State getState() {
        return state;
    }

    public String[] getSpecialVars() {
        return specialVars;
    }

    public Vector2 getPosition() { return position; }
}
