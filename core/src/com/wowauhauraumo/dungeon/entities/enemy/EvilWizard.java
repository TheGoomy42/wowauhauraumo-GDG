package com.wowauhauraumo.dungeon.entities.enemy;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.wowauhauraumo.dungeon.entities.Enemy;
import com.wowauhauraumo.dungeon.main.Game;

public class EvilWizard extends Enemy {

	public EvilWizard(Body body) {
		super(body);
		
		TextureRegion tex = Game.res.getRegion("chars", 3, 15);
		sprites = new TextureRegion[1];
		sprites[0] = tex;
		setAnimation(sprites, -1);
		
		aggroRange = 10;
	}
	
	
	
}
