package com.wowauhauraumo.dungeon.managers;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.wowauhauraumo.dungeon.states.Play;

public class GameContactListener implements ContactListener {
	
	Play state;
	
	public GameContactListener(Play state) {
		this.state = state;
	}

	// called when two fixtures start to collide
	@Override
	public void beginContact(Contact contact) {
		Fixture fa = contact.getFixtureA();
		Fixture fb = contact.getFixtureB();
		
//		if(fa.getUserData() != "player" && fb.getUserData() != "player") {
//			System.out.println("------------START--------------");
//			System.out.println("fa: " + fa.getUserData());
//			System.out.println("fb: " + fb.getUserData());
//		}
		
		if((fa.getUserData() == "playerTop" && !fb.isSensor()) || (fb.getUserData() == "playerTop" && !fa.isSensor())) {
			state.setPlayerCollding(0, true);
		} 
		if((fa.getUserData() == "playerBot" && !fb.isSensor()) || (fb.getUserData() == "playerBot" && !fa.isSensor())) {
			state.setPlayerCollding(1, true);
		} 
		if((fa.getUserData() == "playerL" && !fb.isSensor()) || (fb.getUserData() == "playerL" && !fa.isSensor())) {
			state.setPlayerCollding(2, true);
		} 
		if((fa.getUserData() == "playerR" && !fb.isSensor()) || (fb.getUserData() == "playerR" && !fa.isSensor())) {
			state.setPlayerCollding(3, true);
		}
	}

	// called when two fixtures cease to collide
	@Override
	public void endContact(Contact contact) {
		Fixture fa = contact.getFixtureA();
		Fixture fb = contact.getFixtureB();
		
//		if(fa.getUserData() != "player" && fb.getUserData() != "player") {
//			System.out.println("------------END--------------");
//			System.out.println("fa: " + fa.getUserData());
//			System.out.println("fb: " + fb.getUserData());
//		}
		
		if((fa.getUserData() == "playerTop" && !fb.isSensor()) || (fb.getUserData() == "playerTop" && !fa.isSensor())) {
			state.setPlayerCollding(0, false);
		} else if((fa.getUserData() == "playerBot" && !fb.isSensor()) || (fb.getUserData() == "playerBot" && !fa.isSensor())) {
			state.setPlayerCollding(1, false);
		} else if((fa.getUserData() == "playerL" && !fb.isSensor()) || (fb.getUserData() == "playerL" && !fa.isSensor())) {
			state.setPlayerCollding(2, false);
		} else if((fa.getUserData() == "playerR" && !fb.isSensor()) || (fb.getUserData() == "playerR" && !fa.isSensor())) {
			state.setPlayerCollding(3, false);
		}
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
