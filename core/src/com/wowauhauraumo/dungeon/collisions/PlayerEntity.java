package com.wowauhauraumo.dungeon.collisions;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.wowauhauraumo.dungeon.map.Map;
import com.wowauhauraumo.dungeon.map.mapobjects.MapObject;
import com.wowauhauraumo.dungeon.screens.PlayScreen;
import com.wowauhauraumo.dungeon.util.Direction;

import static com.esotericsoftware.minlog.Log.debug;

/**
 * The entity controlled by the user.
 * <p/>
 * Does not contain anything to do with player stats, inventory or party members.
 *
 * @author Louis Van Steene
 */
public class PlayerEntity extends Entity {
    public PlayerEntity(String spritePrefix, PlayScreen screen, Map map, int row, int column) {
        super(spritePrefix, screen, map, row, column);
    }

    /**
     * Check if the player should collide and performs map object collision logic
     *
     * @param direction the direction of movement
     * @param npcList   a list of the other entities that the player can collide with
     * @return true if the player can move in direction
     */
    private boolean canMove(Direction direction, Array<? extends NpcEntity> npcList) {
        int newRow = row + direction.yMod;
        int newColumn = column + direction.xMod;

        for(NpcEntity entity : npcList) {
            if(entity.row == newRow && entity.column == newColumn) {
                return false;
            }
        }

        MapObject o = map.getMapObject(new Vector2(newColumn, newRow));
        //noinspection SimplifiableIfStatement
        if(o == null) {
            return true;
        }
        else {
            debug("PLAYER ENTITY", "mapobject:" + o.getName());
            return !o.collide(false, false);
        }
    }

    @Override
    public void move(Direction direction) {
        if(!moving) {
            face(direction);
            if(canMove(direction, screen.getEntities())) {
                super.move(direction);
            }
        }
    }

    public void scaleMoveSpeed(float scale) { moveSpeed *= scale; }
}
