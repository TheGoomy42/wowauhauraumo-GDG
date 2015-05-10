package com.wowauhauraumo.dungeon.managers;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Content {
	
	private HashMap<String, Texture> textures;
	
	public Content() {
		textures = new HashMap<String, Texture>();
	}
	
	public void loadTexture(String path, String key) {
		Texture tex = new Texture(Gdx.files.internal(path));
		textures.put(key, tex);
	}
	
	public Texture getTexture(String key) {
		return textures.get(key);
	}
	
	public TextureRegion getRegion(String key, int row, int col) {
		return TextureRegion.split(textures.get(key), 32, 32)[col][row];
	}
	
	public void disposeTexture(String key) {
		Texture tex = textures.get(key);
		if(tex != null) tex.dispose();
	}
	
}
