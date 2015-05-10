package com.wowauhauraumo.dungeon.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public abstract class Enemy extends B2DSprite {
	
	protected boolean aggro;
	protected int aggroRange;

	public Enemy(Body body) {
		super(body);
	}
	
	public void update(float delta, Player player) {
		// decide if and where to move
		Vector2 playerDist = player.getPosition().add(-getPosition().x, -getPosition().y);
		
		if(aggro) {
			// move towards player
		} else {
			if(Math.abs(playerDist.x) < aggroRange) {
				aggro = true;
			}
		}
	}

}
