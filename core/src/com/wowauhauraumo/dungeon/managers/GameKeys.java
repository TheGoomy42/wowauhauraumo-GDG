package com.wowauhauraumo.dungeon.managers;

/**
 * A class containing static methods that control what keys are pressed.
 * 
 * TODO completely remake input system so that it works
 * 
 * @deprecated
 * 
 * @author TheGoomy42
 */
public class GameKeys {
	
	// keys that were down last tick
	public static boolean[] pkeys;
	// keys that are down now
	public static boolean[] keys;
	
	/**
	 * Enum to store the different keys used in the game.
	 * 
	 * @author TheGoomy42
	 */
	public enum Keys {
		UP(0), DOWN(1), LEFT(2), RIGHT(3), SPACE(4);
		
		private final int id;
		
		Keys(int id) {
			this.id = id;
		}
		
		public int getId() { return id; }
		private static final int size = Keys.values().length;
	}
	
	// initialise arrays
	static {
		keys = new boolean[Keys.size];
		pkeys = new boolean[Keys.size];
	}
	
	public static void update() {
		// sets pkeys to keys
		for(int i = 0; i < Keys.size; i++) {
			pkeys[i] = keys[i];
		}
	}
	
	/**
	 * Set all keys to not pressed.
	 */
	public static void resetKeys() {
		for(int i = 0; i < keys.length; i++) {
			keys[i] = false;
		}
	}
	
	/** set a key to be pressed */
	public static void setKey(Keys key, boolean b) { keys[key.getId()] = b; }
	/** 
	 * check if a key is down
	 * 'down' means that the key is being held
	 * 'pressed' means that the key has only just gone down
	 */
	public static boolean isDown(Keys key) { return keys[key.getId()]; }
	public static boolean isPressed(Keys key) { return keys[key.getId()] && !pkeys[key.getId()]; }
	
}
