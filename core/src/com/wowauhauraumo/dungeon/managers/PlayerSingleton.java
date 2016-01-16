package com.wowauhauraumo.dungeon.managers;

import com.wowauhauraumo.dungeon.entities.PartyMember;

/**<p>
 * This class stores all the variables relative to the current player.
 * e.g. party members, score, current location, inventory....</p>
 * <p>
 * Should not be used more more broad things like screen instances, these should be in game</p>
 * <p>
 * In the form of a singleton - there is only one instance stored within
 * and accessible anywhere. We use getters and setters here to restrict access if necessary
 * and to provide checks if relevant.</p>
 * <p>
 * A singleton is probably better implemented using an enum but I don't really care right now.
 * Please implement that if you want.</p>
 * 
 * @author Louis Van Steene
 */
public final class PlayerSingleton {
	private static PlayerSingleton instance;
	
	// fields should all be private
	private PartyMember[] party;
	
	private PlayerSingleton() { } // no other class can instantiate
	
	public static PlayerSingleton getInstance() {
		if(instance == null) {
			instance = new PlayerSingleton();
		}
		return instance;
	}
	
	// getters
	public PartyMember[] getParty() { return party; }
	
	// setters
	public void setParty(PartyMember[] party) { this.party = party; }
}
