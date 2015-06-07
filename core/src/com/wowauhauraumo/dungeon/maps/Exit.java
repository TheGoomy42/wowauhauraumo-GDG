package com.wowauhauraumo.dungeon.maps;

import static com.wowauhauraumo.dungeon.managers.B2DVars.BIT_ENTITY;
import static com.wowauhauraumo.dungeon.managers.B2DVars.BIT_EXIT;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

public class Exit {
	
	private Body body;
	private Vector2 exitTo;
	private World world;
	
	public Exit(Shape shape, World world, Vector2 exitTo) {
		this.world = world;
		this.exitTo = exitTo;
		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		
		bdef.type = BodyType.StaticBody;
		body = world.createBody(bdef);

        fdef.shape = shape;
        fdef.filter.categoryBits = BIT_EXIT;
        fdef.filter.maskBits = BIT_ENTITY;
		
		body.createFixture(fdef).setUserData(this);
	}
	
	public void dispose() {
		world.destroyBody(body);
	}
	
	public Body getBody() { return body; }
	public Vector2 getExitTo() { return exitTo; }
	
}
