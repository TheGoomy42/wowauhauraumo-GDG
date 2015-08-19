package com.wowauhauraumo.dungeon.managers;

public class GameKeys {
	
	// keys that were down last tick
	public static boolean[] pkeys;
	// keys that are down now
	public static boolean[] keys;
	
//	public static final int NUM_KEYS = 5;
//	public static final int UP = 0;
//	public static final int DOWN = 1;
//	public static final int LEFT = 2;
//	public static final int RIGHT = 3;
//	public static final int SPACE = 4;
	
	/**
	 * Enum to store the different keys used in the game
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
	// not entirely sure how static blocks work
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
