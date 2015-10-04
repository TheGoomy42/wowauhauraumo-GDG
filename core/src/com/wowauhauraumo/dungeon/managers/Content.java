package com.wowauhauraumo.dungeon.managers;

import static com.esotericsoftware.minlog.Log.debug;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

//TODO replace this entire system to work with texture atlases
/**
 * Manager class containing entity textures.
 * Replaced by Atlas.
 * 
 * @deprecated
 * @see com.wowauhauraumo.dungeon.managers.Atlas
 * @author TheGoomy42
 */
public class Content {
	
	private HashMap<String, Texture> textures;
	
	public Content() {
		textures = new HashMap<String, Texture>();
	}
	
	public void loadTexture(String path, String key) {
		if(textures.containsKey(key)) {
			debug("Texture not loaded: duplicate key used '" + key + "'");
		}
		Texture tex = new Texture(Gdx.files.internal(path));
		textures.put(key, tex);
	}
	
	public Texture getTexture(String key) {
		return textures.get(key);
	}
	
	public TextureRegion getRegion(String key, int row, int col) {
		return getRegion(key, row, col, 32, 32);
	}
	
	public TextureRegion getRegion(String key, int row, int col, int width, int height) {
		return TextureRegion.split(textures.get(key), width, height)[col][row];
	}
	
	public void disposeTexture(String key) {
		Texture tex = textures.get(key);
		if(tex != null) tex.dispose();
	}
	
}
