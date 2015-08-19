package com.wowauhauraumo.dungeon.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.wowauhauraumo.dungeon.main.Game;

public class Player extends B2DSprite {
	
	private TextureRegion[] upSprites;
	private TextureRegion[] downSprites;
	private TextureRegion[] sideSprites;
	
	private boolean moving;

	public Player(Body body) {
		super(body);
		
		Texture tex = Game.res.getTexture("warrior");
		sprites = TextureRegion.split(tex, 16, 16)[0];
		upSprites = new TextureRegion[2];
		downSprites = new TextureRegion[2];
		sideSprites = new TextureRegion[2];
		downSprites[0] = sprites[0];
		downSprites[1] = sprites[1];
		upSprites[0] = sprites[2];
		upSprites[1] = sprites[3];
		sideSprites[0] = sprites[4];
		sideSprites[1] = sprites[5];
		
		moving = false;
		
		setAnimation(sideSprites, 1/4f);
	}
	
	public void update(float delta, boolean[] collisions) {
		Vector2 vector = getBody().getLinearVelocity();
		if(moving) {
			if(vector.y > 0) {
				animation.switchFrames(upSprites);
				if(!collisions[0]) super.update(delta);
			} else if(vector.y < 0) {
				animation.switchFrames(downSprites);
				if(!collisions[1]) super.update(delta);
			} else if(vector.x < 0) {
				animation.switchFrames(sideSprites);
				if(!collisions[2]) super.update(delta);
			} else if(vector.x > 0) {
				animation.switchFrames(sideSprites);
				if(!collisions[3]) super.update(delta);
			}
		} else {
			animation.setCurrentFrame(0);
		}
	}
	
	public void setMoving(boolean b) { moving = b; }
	
}
