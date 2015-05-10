package com.wowauhauraumo.dungeon.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.wowauhauraumo.dungeon.main.Game;

public class Player extends B2DSprite {

	public Player(Body body) {
		super(body);
		
		TextureRegion tex = Game.res.getRegion("chars", 0, 1);
		sprites = new TextureRegion[1];
		sprites[0] = tex;
		
		setAnimation(sprites, -1);
	}
	
}
