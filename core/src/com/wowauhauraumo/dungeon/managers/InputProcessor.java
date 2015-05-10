package com.wowauhauraumo.dungeon.managers;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;

public class InputProcessor extends InputAdapter {
	
	public boolean keyDown(int key) {
		switch(key) {
		case Keys.UP:
			GameKeys.setKey(GameKeys.UP, true);
			break;
		case Keys.DOWN:
			GameKeys.setKey(GameKeys.DOWN, true);
			break;
		case Keys.LEFT:
			GameKeys.setKey(GameKeys.LEFT, true);
			break;
		case Keys.RIGHT:
			GameKeys.setKey(GameKeys.RIGHT, true);
			break;
		}
		
		return true;
	}
	
	public boolean keyUp(int key) {
		switch(key) {
		case Keys.UP:
			GameKeys.setKey(GameKeys.UP, false);
			break;
		case Keys.DOWN:
			GameKeys.setKey(GameKeys.DOWN, false);
			break;
		case Keys.LEFT:
			GameKeys.setKey(GameKeys.LEFT, false);
			break;
		case Keys.RIGHT:
			GameKeys.setKey(GameKeys.RIGHT, false);
			break;
		}
		
		return true;
	}
	
}
