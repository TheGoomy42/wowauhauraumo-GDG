package com.wowauhauraumo.dungeon.entities;

import static com.esotericsoftware.minlog.Log.debug;
import static com.wowauhauraumo.dungeon.managers.B2DVars.BIT_ENTITY;
import static com.wowauhauraumo.dungeon.managers.B2DVars.BIT_EXIT;
import static com.wowauhauraumo.dungeon.managers.B2DVars.BIT_WALL;
import static com.wowauhauraumo.dungeon.managers.B2DVars.PPM;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.wowauhauraumo.dungeon.managers.Atlas;

public class Player extends B2DSprite {
	
	private TextureRegion[] upSprites;
	private TextureRegion[] downSprites;
	private TextureRegion[] sideSprites;
	
	private boolean moving;

	public Player(World world) {
		super();
		createPlayer(world);
		
		
		TextureRegion tex = Atlas.worldTextures.get("warrior");
		sprites = tex.split(16, 16)[0];
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
	
	
	
	private void createPlayer(World world) {
		debug("Creating player body...");
		// create bodydef
		BodyDef bdef = new BodyDef();
		bdef.position.set(100 / PPM, 110 / PPM);
		bdef.type = BodyType.DynamicBody;
		body = world.createBody(bdef);
		
		// create shape
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(7 / PPM, 7 / PPM);
		
		// create main fixture
		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		fdef.filter.categoryBits = BIT_ENTITY;
		fdef.filter.maskBits = BIT_WALL | BIT_EXIT;
		body.createFixture(fdef).setUserData("player");
		
		// create top sensor
		shape.setAsBox(8 / PPM, 2 / PPM, new Vector2(0, 7 / PPM), 0);
		fdef.shape = shape;
		fdef.filter.categoryBits = BIT_ENTITY;
		fdef.filter.maskBits = BIT_WALL;
		fdef.isSensor = true;
		body.createFixture(fdef).setUserData("playerTop");
		
		// create bottom sensor
		shape.setAsBox(8 / PPM, 2 / PPM, new Vector2(0, -7 / PPM), 0);
		fdef.shape = shape;
		fdef.filter.categoryBits = BIT_ENTITY;
		fdef.filter.maskBits = BIT_WALL;
		fdef.isSensor = true;
		body.createFixture(fdef).setUserData("playerBot");
		
		// create left sensor
		shape.setAsBox(2 / PPM, 8 / PPM, new Vector2(-7 / PPM, 0), 0);
		fdef.shape = shape;
		fdef.filter.categoryBits = BIT_ENTITY;
		fdef.filter.maskBits = BIT_WALL;
		fdef.isSensor = true;
		body.createFixture(fdef).setUserData("playerL");
		
		// create right sensor
		shape.setAsBox(2 / PPM, 8 / PPM, new Vector2(7 / PPM, 0), 0);
		fdef.shape = shape;
		fdef.filter.categoryBits = BIT_ENTITY;
		fdef.filter.maskBits = BIT_WALL;
		fdef.isSensor = true;
		body.createFixture(fdef).setUserData("playerR");
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
	public boolean isMoving() { return moving; }
	
}
