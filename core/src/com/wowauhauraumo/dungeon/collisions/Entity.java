package com.wowauhauraumo.dungeon.collisions;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.wowauhauraumo.dungeon.content.Atlas;
import com.wowauhauraumo.dungeon.map.Map;
import com.wowauhauraumo.dungeon.screens.PlayScreen;
import com.wowauhauraumo.dungeon.util.Direction;

/**
 * Superclass of the player, NPCs and enemies, but not game objects, in the PlayScreen.
 * <p/>
 * Used for rendering and collision logic, not for storing of stats. Controls tile based movement. NPCs will also be
 * able to give quests, however the nature of these quests should be managed externally.
 *
 * @author Louis Van Steene
 */
public abstract class Entity {
    protected float x;
    protected float y;
    protected int column;
    protected int row;
    protected float width;
    protected float height;

    protected PlayScreen screen;
    protected Map map; // reference to the current game map
    protected boolean moving;
    protected Direction direction;
    protected float distanceMoved;
    protected int tileSize;
    protected float moveSpeed;

    // animation
    private TextureRegion[] downSprites, upSprites, leftSprites, rightSprites;
    private TextureRegion[] currentSprites;

    protected Entity(String spritePrefix, PlayScreen screen, Map map, int row, int column) {
        this.screen = screen;
        this.map = map;

        width = 16;
        height = 16;

        tileSize = map.getTileSize();
        setRow(row);
        setColumn(column);
        moveSpeed = tileSize / 16f;

        // set sprites
        downSprites = new TextureRegion[]{
                Atlas.getWorldTexture(spritePrefix + '0'), Atlas.getWorldTexture(spritePrefix + '1')
        };
        upSprites = new TextureRegion[]{
                Atlas.getWorldTexture(spritePrefix + '2'), Atlas.getWorldTexture(spritePrefix + '3')
        };
        leftSprites = new TextureRegion[]{
                Atlas.getWorldTexture(spritePrefix + '5'), Atlas.getWorldTexture(spritePrefix + '4')
        };

        TextureRegion side0 = new TextureRegion(Atlas.getWorldTexture(spritePrefix + '5'));
        TextureRegion side1 = new TextureRegion(Atlas.getWorldTexture(spritePrefix + '4'));
        side0.flip(true, false);
        side1.flip(true, false);
        rightSprites = new TextureRegion[]{side0, side1};

        // set animations
        currentSprites = leftSprites;
    }

    /**
     * Move an entity to another cell in the grid.
     *
     * @param direction the direction to move towards
     */
    protected void move(Direction direction) {
        if(!moving) {
            moving = true;
            this.direction = direction;
            face(direction);
            distanceMoved = 0;
        }
    }

    /**
     * Aligns an entity to the grid. Should be called after the entity has moved, in case of precision errors.
     * <p/>
     * (This function might not be necessary, but I want to be sure)
     */
    protected void align() {
        x -= x % tileSize;
        y -= y % tileSize;
    }

    /**
     * Changes the current sprites in use to that of the corresponding direction.
     *
     * @param direction the direction to face
     */
    protected void face(Direction direction) {
        switch(direction) {
            case NORTH:
                currentSprites = upSprites;
                break;
            case SOUTH:
                currentSprites = downSprites;
                break;
            case EAST:
                currentSprites = rightSprites;
                break;
            case WEST:
                currentSprites = leftSprites;
                break;
            case NONE:
                currentSprites = leftSprites;
                break;
        }
    }

    /**
     * Moves and draws the entity.
     *
     * @param sb    SpriteBatch to draw to
     * @param delta the time since render was last called
     */
    public void render(SpriteBatch sb, float delta) {
        // move sprite along the grid and animate
        if(moving) {
            float dx = direction.xMod * moveSpeed;
            float dy = direction.yMod * moveSpeed;

            setX(x + dx);
            setY(y + dy);
            distanceMoved += Math.abs(dx + dy);

            // repeat until an entire tile moved
            if(distanceMoved >= tileSize) {
                moving = false;
                align();
            }
        }

        // draw current frame in looping animation
        sb.begin();
        sb.draw(currentSprites[(distanceMoved > tileSize / 2) ? 1 : 0], x, y, width, height);
        sb.end();
    }

    public Vector2 getPosition() {
        return new Vector2(x, y);
    }

    protected void setX(float x) {
        this.x = x;
        this.column = (int) Math.floor(x / tileSize);
    }

    protected void setY(float y) {
        this.y = y;
        this.row = (int) Math.floor(y / tileSize);
    }

    public void setRow(int row) {
        this.row = row;
        this.y = map.getRows() * tileSize - (row + 1) * tileSize;
    }

    public void setColumn(int column) {
        this.column = column;
        this.x = column * tileSize;
    }

}
