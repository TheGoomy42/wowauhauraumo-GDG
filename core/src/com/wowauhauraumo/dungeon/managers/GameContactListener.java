package com.wowauhauraumo.dungeon.managers;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

public class GameContactListener implements ContactListener {

	// called when two fixtures start to collide
	@Override
	public void beginContact(Contact contact) {
		
	}

	// called when two fixtures cease to collide
	@Override
	public void endContact(Contact contact) {

	}

	// collision detection
	// presolve
	// collision handling
	// postsolve
	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {  }

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {  }

}
