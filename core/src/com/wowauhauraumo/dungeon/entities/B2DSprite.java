package com.wowauhauraumo.dungeon.entities;

import static com.wowauhauraumo.dungeon.managers.B2DVars.PPM;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.wowauhauraumo.dungeon.managers.Animation;

public class B2DSprite {
	
	protected Body body;
	protected Animation animation;
	protected float width;
	protected float height;
	protected TextureRegion[] sprites;
	protected boolean left;
	
	public B2DSprite(Body body) {
		this.body = body;
		left = false;
	}
	
	public void setAnimation(TextureRegion[] reg, float delay) {
		animation = new Animation(reg, delay);
		width = reg[0].getRegionWidth();
		height = reg[0].getRegionHeight();
	}
	
	public void update(float delta) { 
		animation.update(delta);
	}
	
	public void render(SpriteBatch sb) {
		sb.begin();
		sb.draw(animation.getFrame(), body.getPosition().x * PPM - width / 2, body.getPosition().y * PPM - height / 2);
		sb.end();
	}
	
	public Body getBody() { return body; }
	public Vector2 getPosition() { return body.getPosition(); } 
	
	public void setLeft(boolean left) {
		if(this.left != left) {
			for(TextureRegion i : sprites) {
				i.flip(true, false);
			}
			this.left = left;
		}
	}
	
}
