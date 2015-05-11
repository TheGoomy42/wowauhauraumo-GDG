package com.wowauhauraumo.dungeon.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public abstract class Enemy extends B2DSprite {
	
	protected boolean aggro;
	protected int aggroRange;

	public Enemy(Body body) {
		super(body);
		aggro = false;
	}
	
	public void update(float delta, Player player) {
		super.update(delta);
		// decide if and where to move
		Vector2 playerDist = player.getPosition().add(-getPosition().x, -getPosition().y);
		
		if(aggro) {
			// path towards player, use A* algorithm at some point, path only into range
		} else {
			if(Math.abs(playerDist.x - getPosition().x) < (int) aggroRange / 2 &&
					Math.abs(playerDist.y - getPosition().y) < (int) aggroRange / 2) {
				aggro = true;
				System.out.println("aggroed");
			}
		}
	}

}
