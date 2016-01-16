package com.wowauhauraumo.dungeon.main;

import static com.esotericsoftware.minlog.Log.debug;
import static com.esotericsoftware.minlog.Log.info;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

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
	
	// random number generators
	private Random battleRandom; // for all checks in battle
	private Random encounterRandom; // for checking if the player should run into an enemy
	private Random statRandom; // for levelling up stats
	
	// the main screen - battles etc have to return here so keep a reference
	private PlayScreen playScreen;
	// screen showing encounters with enemies
	private BattleScreen battleScreen;
	
	// time between each frame
	public static final float STEP = 1/60f; // 60fps
	// the current amount of time waited
	private float accum;
	
	public SpriteBatch        getSpriteBatch    () { return sb;              }
	public OrthographicCamera getCamera         () { return cam;             } 
	public OrthographicCamera getHUDCamera      () { return hudcam;          }
	public Random             getBattleRandom   () { return battleRandom;    }
	public Random             getEncounterRandom() { return encounterRandom; }
	public Random             getStatRandom     () { return statRandom;      }
	
	
	// screen management
	public BattleScreen getBattleScreen() { // get a fresh battle screen
		// if we don't have a battle screen make a new one
		if(battleScreen == null) {
			battleScreen = new BattleScreen(this);
		} else {
			battleScreen.dispose();
		}
		return battleScreen;
	}
	
	public PlayScreen getPlayScreen() {
		if(playScreen == null) {
			setPlayScreen(new PlayScreen(this));
			playScreen.create();
		}
		return playScreen;
	}
	
	@Override
	public void setScreen(Screen screen) {
		super.setScreen(screen);
	}
	
	private void setPlayScreen(PlayScreen playScreen) { this.playScreen = playScreen; }
	
	@Override
	public void create () {
		// get width and height
		width = Gdx.graphics.getWidth() / 2;
		height = Gdx.graphics.getHeight() / 2;
		
		// initialise objects
		sb = new SpriteBatch();
		cam = new OrthographicCamera();
		cam.setToOrtho(false, width, height);
		debug("Main camera initialised");
		hudcam = new OrthographicCamera();
		hudcam.setToOrtho(false, width, height);
		debug("HUD camera initialised");
		
		// initialise random generators
		battleRandom = new Random();
		encounterRandom = new Random();
		statRandom = new Random();
		
		setScreen(new MenuScreen(this));
		debug("Screen set to menu screen");
		
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
