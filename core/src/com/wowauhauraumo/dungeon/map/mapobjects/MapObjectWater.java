package com.wowauhauraumo.dungeon.map.mapobjects;

/**
 * Object to represent terrain where the player needs a boat to travel. Collides unless the player is in a boat.
 */
public class MapObjectWater extends MapObject {
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
     * @return true if the MapObject should prevent movement
     */
    @Override
    public boolean collide(boolean isSailing, boolean isFlying) {
        return !(isSailing || isFlying);
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
