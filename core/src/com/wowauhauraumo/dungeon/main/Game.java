package com.wowauhauraumo.dungeon.main;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.wowauhauraumo.dungeon.managers.Content;
import com.wowauhauraumo.dungeon.managers.GameKeys;
import com.wowauhauraumo.dungeon.managers.GameStateManager;
import com.wowauhauraumo.dungeon.managers.InputProcessor;

public class Game extends ApplicationAdapter {
	
	public static int width, height;
	
	private SpriteBatch sb;
	private OrthographicCamera cam;
	private OrthographicCamera hudcam;
	
	private GameStateManager gsm;
	public static Content res;
	
	// fps
	public static final float STEP = 1/60f;
	private float accum;
	
	public SpriteBatch getSpriteBatch() { return sb; }
	public OrthographicCamera getCamera() { return cam; } 
	public OrthographicCamera getHUDCamera() { return hudcam; }
	
	@Override
	public void create () {
		// get width and height
		width = Gdx.graphics.getWidth() / 2;
		height = Gdx.graphics.getHeight() / 2;
		
		res = new Content();
		
		// load images
		res.loadTexture("warrior.png", "warrior");
		
		Gdx.input.setInputProcessor(new InputProcessor());
		
		sb = new SpriteBatch();
		cam = new OrthographicCamera();
		cam.setToOrtho(false, width, height);
		hudcam = new OrthographicCamera();
		hudcam.setToOrtho(false, width, height);
		
		gsm = new GameStateManager(this);
	}

	@Override
	public void render () {
		
		accum += Gdx.graphics.getDeltaTime();
		while(accum >= STEP) {
			// black out screen
			Gdx.gl.glClearColor(0, 0, 0, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			accum -= STEP;
			gsm.update(STEP);
			gsm.render();
			GameKeys.update();
		}
	}
}
