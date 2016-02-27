package com.wowauhauraumo.dungeon.map.mapobjects;

import com.wowauhauraumo.dungeon.screens.PlayScreen;

/**
 * Map object for a treasure chest. Stores loot and can be opened. When open is is no longer interactable. Always
 * collides with the player.
 */
public class MapObjectTreasure extends MapObject {
    /**
     * Changes the MapObject state. This can do whatever it needs, e.g. a door opening would change its sprite to be an
     * open door.
     *
     * @param state the state to change to
     */
    @Override
    public void changeState(State state) {
        if(state == State.OPEN) { // the player is trying to open the chest
            if(this.state == State.LOCKED) {
                // check if the player has a key
                // otherwise display message
            }
            else if(this.state == State.OPEN) {
                // display message
            }
            else if(this.state == State.CLOSED) {
                // add items to inventory
                // display message
                this.state = State.OPEN;
            }
        }
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
        return true; // always collides
    }

    /**
     * Draws the MapObject as a sprite
     *
     * @param delta the time since the last render
     */
    @Override
    public void render(float delta) {
        // draw sprite
    }
}
