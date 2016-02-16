package com.wowauhauraumo.dungeon.map.mapobjects;

/**
 * Object representing a door. Opens when the player walks through, unless the door is locked and the player doesn't
 * have a key.
 */
public class MapObjectDoor extends MapObject {
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
        if(this.state == State.LOCKED) {
            // check if the player has a key
            // display message otherwise
            return true;
        }
        else if(state == State.CLOSED) {
            state = State.OPEN;
        }
        return false;
    }

    /**
     * Draws the MapObject as a sprite
     *
     * @param delta the time since the last render
     */
    @Override
    public void render(float delta) {
        // draw sprite based on open/closed
    }
}
