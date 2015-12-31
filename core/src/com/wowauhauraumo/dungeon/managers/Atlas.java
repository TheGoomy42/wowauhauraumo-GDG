package com.wowauhauraumo.dungeon.managers;

import static com.esotericsoftware.minlog.Log.debug;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Content management system using multiple atlases to store textures.
 * This can probably be cleaned up further but right now it's not necessary.
 * 
 * @see com.wowauhauraumo.dungeon.managers.Content
 * @author TheGoomy42
 */
public class Atlas {
	private static TextureAtlas atlasWorld;  // Contains all sprites used while in the world (i.e. PlayScreen)
	private static TextureAtlas atlasBattle; // Contains all sprites used while in battle
//	private TextureAtlas atlasInventory;     // will contain all sprites for items in inventory
	private static TextureAtlas atlasGUI;           // will contain textures for UI
	
	public static final HashMap<String, TextureRegion> worldTextures = new HashMap<String, TextureRegion>();
	public static final HashMap<String, TextureRegion> battleTextures = new HashMap<String, TextureRegion>();
	public static final HashMap<String, TextureRegion> guiTextures = new HashMap<String, TextureRegion>();
//	public static final Skin skin;
	
	static {
		debug("Atlas initialized. Loading textures...");
		atlasWorld = new TextureAtlas(Gdx.files.internal("sprites/world.pack"));
		atlasBattle = new TextureAtlas(Gdx.files.internal("sprites/battle.pack"));
		atlasGUI = new TextureAtlas(Gdx.files.internal("sprites/gui.pack"));
		loadTextures();
	}
	
	private static void loadTextures() {
		worldTextures.put("warrior", atlasWorld.findRegion("warrior"));
		
		for(int i=0;i<6;i++) {
			battleTextures.put("warrior"+i, atlasBattle.findRegion("warrior"+i));
			battleTextures.put("bmage"+i, atlasBattle.findRegion("bmage"+i));
			battleTextures.put("monk"+i, atlasBattle.findRegion("monk"+i));
			battleTextures.put("wmage"+i, atlasBattle.findRegion("wmage"+i));
		}
		guiTextures.put("cursor", atlasGUI.findRegion("HandCursor"));
	}
}
