package com.wowauhauraumo.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.wowauhauraumo.dungeon.main.Game;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		config.width = 700;
		config.height = 560;
		config.title = "Generic Dungeon Game";
		config.resizable = false;
		
		new LwjglApplication(new Game(), config);
	}
}
