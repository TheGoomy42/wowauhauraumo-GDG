package com.wowauhauraumo.dungeon.collisions;

import com.badlogic.gdx.math.Shape2D;

/**
 * An interface that should be implemented by every object that collides on the BossScreen.
 * <p/>
 * The object itself should check collisions directly with another object using {@link #isColliding}. (In projectiles
 * this is probably best done in the projectile, not the player.)
 * <p/>
 * Call order: {@link #isColliding} -> {@link #getHitbox} -> (if colliding) {@link #onCollide}
 *
 * @author Louis Van Steene
 */
public interface Collidable {

    /**
     * Called in {@link #isColliding} when a collision is detected.
     */
    void onCollide(Collidable other);

    /**
     * Checks if another {@link Collidable} object is colliding with this one.
     * <p/>
     * Uses {@link #getHitbox} for the actual math object. Calls {@link #onCollide} on this and {@code other} if
     * returning true.
     *
     * @param other the other object to check against
     * @return true  if the objects are colliding
     */
    boolean isColliding(Collidable other);

    /**
     * Called when calculating collisions.
     *
     * @return a {@link Shape2D} implementation
     */
    Shape2D getHitbox();

}
