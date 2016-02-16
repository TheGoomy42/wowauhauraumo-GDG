package com.wowauhauraumo.dungeon.collisions;

import com.wowauhauraumo.dungeon.map.Map;
import com.wowauhauraumo.dungeon.screens.PlayScreen;

/**
 * A world entity that either follows a rigid movement pattern or remains still.
 * <p/>
 * Only used in {@link PlayScreen}. Can interact with the player to give dialogue and quests, however quests are managed
 * externally.
 *
 * @author Louis Van Steene
 */
public class NpcEntity extends Entity {

    protected NpcEntity(String spritePrefix, PlayScreen screen, Map map, int row, int column) {
        super(spritePrefix, screen, map, row, column);
    }
}
