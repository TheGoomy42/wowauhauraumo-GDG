package com.wowauhauraumo.dungeon.map.mapobjects;

import com.wowauhauraumo.dungeon.screens.PlayScreen;

/**
 * Object to store lava, ice and similar tiles. Does not collide, but deals one damage to the player upon intersection.
 */
public class MapObjectLava extends MapObject {
    /**
     * Changes the MapObject state. This can do whatever it needs, e.g. a door opening would change its sprite to be an
     * open door.
     *
     * @param state the state to change to
     */
    @Override
    public void changeState(State state) {
        // nothing to see here
    }

    /**
     * Performs collision logic and do anything that should happen on collision, e.g. open the door, start a battle
     *
     * @param isSailing if the player is on a boat
     * @param isFlying  if the player is on an airship
     * @param screen    the play screen
     * @return true if the MapObject should prevent movement
     */
    @Override
    public boolean collide(boolean isSailing, boolean isFlying, PlayScreen screen) {
        // damage player, but don't collide
        return false;
    }

    /**
     * Draws the MapObject as a sprite
     *
     * @param delta the time since the last render
     */
    @Override
    public void render(float delta) {
        // nothing to see here
        // drawn on tilemap
    }
}
