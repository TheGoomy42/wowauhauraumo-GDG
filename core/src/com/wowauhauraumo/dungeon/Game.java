package com.wowauhauraumo.dungeon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.wowauhauraumo.dungeon.screens.*;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Random;

import static com.esotericsoftware.minlog.Log.debug;

/**
 * Controls the game loop and the current screen, and stores graphics objects.
 * <p>
 * Interfaces with the LibGDX application listener containing the render() game loop function. 
 * The game class does not display anything directly, but rather uses screens to abstract this 
 * process into logical sections, being:
 * <p><ul>
 * <li>{@link MenuScreen} - the main menu for the game
 * <li>{@link PlayScreen} - world exploration
 * <li>{@link BattleScreen} - encounter (FF1-style) battles
 * <li>{@code BossScreen} - bullet hell-style battles
 * </ul><p>
 *
 * @author Louis Van Steene
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

    private static final HashMap<Class<? extends Screen>, Screen> classToScreenMapping;

    // fps management
    public static final float STEP = 1 / 60f;
    private float timer;

    static {
        //
        classToScreenMapping = new HashMap<>();
        classToScreenMapping.put(MenuScreen.class, null);
        classToScreenMapping.put(PlayScreen.class, null);
        classToScreenMapping.put(BattleScreen.class, null);
        classToScreenMapping.put(BossBattleScreen.class, null);
        classToScreenMapping.put(SettingsScreen.class, null);
    }

    // screen management
    public void setScreen(Class<? extends Screen> clazz) {
        // only ever have one instance of each screen, but dispose each time
        final Screen instance = classToScreenMapping.get(clazz);

        debug("GAME", "Got instance from map: " + instance);

        if(instance == null) {
            try {
                debug("GAME", "Creating new instance");
                classToScreenMapping.put(clazz, clazz.getDeclaredConstructor(Game.class).newInstance(this));
            } catch(InstantiationException | IllegalAccessException e) {
                throw new IllegalArgumentException("No screen exists with name: " + clazz.getName(), e);
            } catch(NoSuchMethodException e) {
                throw new IllegalArgumentException("Screen does not have valid constructor: " + clazz.getName(), e);
            } catch(InvocationTargetException e) {
                throw new IllegalArgumentException("Failed while invoking constructor in class: " + clazz.getName(), e);
            }
        }
        else {
            debug("GAME", "Resetting instance");
            // needs to be cleaned - can this just be instance?
            classToScreenMapping.get(clazz).dispose();
        }

        setScreen(classToScreenMapping.get(clazz));
    }

    public void resetPlayScreen() {
        PlayScreen playScreen = (PlayScreen) classToScreenMapping.get(PlayScreen.class);
        if(playScreen == null) return;

        playScreen.shouldDispose();
    }

    @Override
    public void create() {
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

        // set initial screen
        setScreen(MenuScreen.class);
    }

    @Override
    public void render() {
        // control fps
        timer += Gdx.graphics.getDeltaTime();
        while(timer >= STEP) {
            super.render();
            // reset timer
            timer -= STEP;
        }
    }


    public SpriteBatch getSpriteBatch() { return sb; }

    public OrthographicCamera getCamera() { return cam; }

    public OrthographicCamera getHUDCamera() { return hudcam; }

    public Random getBattleRandom() { return battleRandom; }

    public Random getStatRandom() { return statRandom; }

    public Random getEncounterRandom() { return encounterRandom; }
}
