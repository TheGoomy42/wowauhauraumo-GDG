package com.wowauhauraumo.dungeon.states;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.wowauhauraumo.dungeon.main.Game;
import com.wowauhauraumo.dungeon.managers.GameStateManager;

public abstract class GameState {
	
	protected GameStateManager gsm;
	protected Game game;
	
	protected SpriteBatch sb;
	protected OrthographicCamera cam;
	protected OrthographicCamera hudcam;
	
	protected GameState(GameStateManager gsm) {
		this.gsm = gsm;
		game = gsm.getGame();
		sb = game.getSpriteBatch();
		cam = game.getCamera();
		hudcam = game.getHUDCamera();
	}
	
	public abstract void handleInput();
	public abstract void update(float delta);
	public abstract void render();
	public abstract void dispose();
	 
}
