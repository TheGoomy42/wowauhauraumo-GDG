package com.wowauhauraumo.dungeon.managers;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.wowauhauraumo.dungeon.maps.Map.Portal;
import com.wowauhauraumo.dungeon.states.PlayScreen;

public class GameContactListener implements ContactListener {
	
	private PlayScreen play;
	
	public GameContactListener(PlayScreen state) {
		this.play = state;
	}

	// called when two fixtures start to collide
	@Override
	public void beginContact(Contact contact) {
		Fixture fa = contact.getFixtureA();
		Fixture fb = contact.getFixtureB();
		
		// stop player animations
		if((fa.getUserData() == "playerTop" && !fb.isSensor()) || (fb.getUserData() == "playerTop" && !fa.isSensor())) {
			play.setPlayerCollding(0, true);
		} 
		if((fa.getUserData() == "playerBot" && !fb.isSensor()) || (fb.getUserData() == "playerBot" && !fa.isSensor())) {
			play.setPlayerCollding(1, true);
		} 
		if((fa.getUserData() == "playerL" && !fb.isSensor()) || (fb.getUserData() == "playerL" && !fa.isSensor())) {
			play.setPlayerCollding(2, true);
		} 
		if((fa.getUserData() == "playerR" && !fb.isSensor()) || (fb.getUserData() == "playerR" && !fa.isSensor())) {
			play.setPlayerCollding(3, true);
		}
		
		if(fa.getUserData() instanceof Portal && fb.getUserData() == "player") {
			play.playerTravel((Portal) fa.getUserData());
		} else if(fb.getUserData() instanceof Portal && fa.getUserData() == "player") {
			play.playerTravel((Portal) fb.getUserData());
		}
	}

	// called when two fixtures cease to collide
	@Override
	public void endContact(Contact contact) {
		Fixture fa = contact.getFixtureA();
		Fixture fb = contact.getFixtureB();
		
		// restart player animations
		if((fa.getUserData() == "playerTop" && !fb.isSensor()) || (fb.getUserData() == "playerTop" && !fa.isSensor())) {
			play.setPlayerCollding(0, false);
		} else if((fa.getUserData() == "playerBot" && !fb.isSensor()) || (fb.getUserData() == "playerBot" && !fa.isSensor())) {
			play.setPlayerCollding(1, false);
		} else if((fa.getUserData() == "playerL" && !fb.isSensor()) || (fb.getUserData() == "playerL" && !fa.isSensor())) {
			play.setPlayerCollding(2, false);
		} else if((fa.getUserData() == "playerR" && !fb.isSensor()) || (fb.getUserData() == "playerR" && !fa.isSensor())) {
			play.setPlayerCollding(3, false);
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
