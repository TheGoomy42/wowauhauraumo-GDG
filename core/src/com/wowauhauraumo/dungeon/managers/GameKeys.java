package com.wowauhauraumo.dungeon.managers;

public class GameKeys {
	
	public static boolean[] pkeys;
	public static boolean[] keys;
	
	public static final int NUM_KEYS = 5;
	public static final int UP = 0;
	public static final int DOWN = 1;
	public static final int LEFT = 2;
	public static final int RIGHT = 3;
	public static final int SPACE = 4;
	
	static {
		keys = new boolean[NUM_KEYS];
		pkeys = new boolean[NUM_KEYS];
	}
	
	public static void update() {
		for(int i = 0; i < NUM_KEYS; i++) {
			pkeys[i] = keys[i];
		}
	}
	
	public static void setKey(int key, boolean b) { keys[key] = b; }
	public static boolean isDown(int key) { return keys[key]; }
	public static boolean isPressed(int key) { return keys[key] && !pkeys[key]; }
	
}
