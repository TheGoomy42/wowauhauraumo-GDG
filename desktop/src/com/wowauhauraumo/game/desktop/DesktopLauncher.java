package com.wowauhauraumo.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.wowauhauraumo.dungeon.main.Game;

/**
 * The desktop wrapper for the game. Contains main() method.
 * 
 * @author TheGoomy42
 */
public class DesktopLauncher {
	/**
	 * Entry point
	 * @param arg
	 */
	public static void main (String[] arg) {
		// Configures the window's properties
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		config.width = 700;
		config.height = 560;
		config.title = "Generic Dungeon Game";
		config.resizable = false;
		
		// creates the application with an instance of the Game class
		new LwjglApplication(new Game(), config);
	}
}
