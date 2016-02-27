package com.wowauhauraumo.dungeon.map.mapobjects;

import com.wowauhauraumo.dungeon.screens.PlayScreen;

/**
 * Object to represent a portal. Transports the player to another map. Needs a destination.
 */
public class MapObjectPortal extends MapObject {
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
     * Portals never prevent movement, so this function always returns true. However, this function updates logic in the
     * play state.
     *
     * @param isSailing if the player is on a boat
     * @param isFlying  if the player is on an airship
     * @param screen    a reference to the play screen
     * @return true
     */
    @Override
    public boolean collide(boolean isSailing, boolean isFlying, PlayScreen screen) {
        screen.enterPortal(position);
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
