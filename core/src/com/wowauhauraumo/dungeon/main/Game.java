package com.wowauhauraumo.dungeon.main;

import static com.esotericsoftware.minlog.Log.debug;
import static com.esotericsoftware.minlog.Log.info;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.wowauhauraumo.dungeon.managers.InputProcessor;

/**
 * This class controls the entire game and overrides LIbGDX's create() and render() methods.
 * NOTE: The use of LibGDX Screen has not yet been fully implemented. There are no main menu or inventory
 * screens as of yet.
 * 
 * @author TheGoomy42
 */
public class Game extends com.badlogic.gdx.Game {
	
	// the dimensions in pixels of the display surface
	public static int width, height;
	
	// sprite batch
	private SpriteBatch sb;
	// camera for the game map
	private OrthographicCamera cam;
	// camera for the HUD
	private OrthographicCamera hudcam;
	
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
		
		// start receiving input
		Gdx.input.setInputProcessor(new InputProcessor());
		debug("Input processor created");
		
		// initialise objects
		sb = new SpriteBatch();
		cam = new OrthographicCamera();
		cam.setToOrtho(false, width, height);
		debug("Main camera initialised");
		hudcam = new OrthographicCamera();
		hudcam.setToOrtho(false, width, height);
		debug("HUD camera initialised");
		
		setScreen(new MenuScreen(this));
		debug("Screen set to play screen");
		
		info("Game created");
	}

	@Override
	public void render () {
		accum += Gdx.graphics.getDeltaTime();
		// run if the current time waited is above what we want
		while(accum >= STEP) {
			super.render();
			// reset timer
			accum -= STEP;
		}
	}
}
