package com.wowauhauraumo.dungeon.entities;

/**
 * A store for all of the names of enemies and jobs used in FF1 battles.
 * 
 * @author TheGoomy42
 */
public enum Sprite {
	/* jobs */ WARRIOR("warrior"), MONK("monk"), BMAGE("bmage"), WMAGE("wmage"), RMAGE("rmage"), 
	           THIEF("thief"),
	
	/* enemies */ IMP("imp");
	
	public final String texture;
	
	private Sprite(String t) {
		texture = t;
	}
}
