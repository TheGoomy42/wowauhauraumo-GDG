package com.wowauhauraumo.dungeon.managers;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;

/**
 * Class to process input. When relevant input is received, this class sets the referenced value in
 * GameKeys to true.
 * 
 * @deprecated
 * 
 * @author TheGoomy42
 */
public class InputProcessor extends InputAdapter {
	
	@Override
	public boolean keyDown(int key) {
		// we only need these keys
		switch(key) {
		case Keys.UP:
			GameKeys.setKey(GameKeys.Keys.UP, true);
			break;
		case Keys.DOWN:
			GameKeys.setKey(GameKeys.Keys.DOWN, true);
			break;
		case Keys.LEFT:
			GameKeys.setKey(GameKeys.Keys.LEFT, true);
			break;
		case Keys.RIGHT:
			GameKeys.setKey(GameKeys.Keys.RIGHT, true);
			break;
		}
		
		return true;
	}
	
	@Override
	public boolean keyUp(int key) {
		// we only need these keys
		switch(key) {
		case Keys.UP:
			GameKeys.setKey(GameKeys.Keys.UP, false);
			break;
		case Keys.DOWN:
			GameKeys.setKey(GameKeys.Keys.DOWN, false);
			break;
		case Keys.LEFT:
			GameKeys.setKey(GameKeys.Keys.LEFT, false);
			break;
		case Keys.RIGHT:
			GameKeys.setKey(GameKeys.Keys.RIGHT, false);
			break;
		}
		
		return true;
	}
	
}
