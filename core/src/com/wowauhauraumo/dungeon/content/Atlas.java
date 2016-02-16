package com.wowauhauraumo.dungeon.content;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashMap;

import static com.esotericsoftware.minlog.Log.debug;

/**
 * Loads in texture atlases.
 * <p>
 * Loads all textures at launch and stores them in HashMaps.
 * Please tell me if there is a better alternative to using HashMaps.
 * 
 * @author TheGoomy42
 */
public class Atlas {
    private static TextureAtlas atlasWorld;  // Contains all sprites used while in the world (i.e. PlayScreen)
    private static TextureAtlas atlasBattle; // Contains all sprites used while in battle
//	private TextureAtlas atlasInventory;     // will contain all sprites for items in inventory
private static TextureAtlas atlasGUI;    // will contain textures for UI

    private static final HashMap<String, TextureRegion> worldTextures = new HashMap<>();
    private static final HashMap<String, TextureRegion> battleTextures = new HashMap<>();
    private static final HashMap<String, TextureRegion> guiTextures = new HashMap<>();
//	private static final Skin skin;

    static {
        debug("Atlas initialized. Loading textures...");
        atlasWorld = new TextureAtlas(Gdx.files.internal("sprites/world.pack"));
        atlasBattle = new TextureAtlas(Gdx.files.internal("sprites/battle.pack"));
        atlasGUI = new TextureAtlas(Gdx.files.internal("sprites/gui.pack"));
        loadTextures();
    }

    private static void loadTextures() {

        for(int i = 0; i < 6; i++) {
            battleTextures
                    .put(Sprite.WARRIOR.getTexture() + i, atlasBattle.findRegion(Sprite.WARRIOR.getTexture() + i));
            battleTextures.put(Sprite.BMAGE.getTexture() + i, atlasBattle.findRegion(Sprite.BMAGE.getTexture() + i));
            battleTextures.put(Sprite.MONK.getTexture() + i, atlasBattle.findRegion(Sprite.MONK.getTexture() + i));
            battleTextures.put(Sprite.WMAGE.getTexture() + i, atlasBattle.findRegion(Sprite.WMAGE.getTexture() + i));

            worldTextures.put(Sprite.WARRIOR.getTexture() + i, atlasWorld.findRegion(Sprite.WARRIOR.getTexture() + i));
        }
        guiTextures.put("cursor", atlasGUI.findRegion("HandCursor"));
    }

    public static TextureRegion getWorldTexture(String key) { return worldTextures.get(key); }

    public static TextureRegion getBattleTexture(String key) { return battleTextures.get(key); }

    public static TextureRegion getGuiTexture(String key) { return battleTextures.get(key); }
}
