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

/**
 * This class controls the entire game and overrides LIbGDX's create() and render() methods.
 * 
 * @author TheGoomy42
 */
public class Game extends ApplicationAdapter {
	
	// the dimensions in pixels of the display surface
	public static int width, height;
	
	// sprite batch
	private SpriteBatch sb;
	// camera for the game map
	private OrthographicCamera cam;
	// camera for the HUD
	private OrthographicCamera hudcam;
	
	// GameStateManager instance
	private GameStateManager gsm;
	
	// store for all game textures
	public static Content res;
	
	// time between each frame
	public static final float STEP = 1/60f; // 60fps
	// the current amount of time waited
	private float accum;
	
	public SpriteBatch getSpriteBatch() { return sb; }
	public OrthographicCamera getCamera() { return cam; } 
	public OrthographicCamera getHUDCamera() { return hudcam; }
	
	@Override
	public void create () {
		// get width and height
		width = Gdx.graphics.getWidth() / 2;
		height = Gdx.graphics.getHeight() / 2;
		
		// load in textures
		res = new Content();
		loadTextures();
		
		// start receiving input
		Gdx.input.setInputProcessor(new InputProcessor());
		
		// initialise objects
		sb = new SpriteBatch();
		cam = new OrthographicCamera();
		cam.setToOrtho(false, width, height);
		hudcam = new OrthographicCamera();
		hudcam.setToOrtho(false, width, height);
		
		// initialise GameStateManager
		gsm = new GameStateManager(this);
	}
	
	/**
	 * Load in animation sprites
	 */
	private void loadTextures() {
		res.loadTexture("sprites/warrior.png", "warrior");
	}

	@Override
	public void render () {
		accum += Gdx.graphics.getDeltaTime();
		// run if the current time waited is above what we want
		while(accum >= STEP) {
			// black out screen
			Gdx.gl.glClearColor(0, 0, 0, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			// update input
			GameKeys.update();
			// run update and render from GameStateManager
			gsm.update(STEP);
			gsm.render();
			// reset timer
			accum -= STEP;
		}
	}
}
