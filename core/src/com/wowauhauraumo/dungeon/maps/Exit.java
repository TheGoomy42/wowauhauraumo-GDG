package com.wowauhauraumo.dungeon.maps;

import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

public class Exit extends MapUpdater {
	
	public Exit(Shape shape, World world, int spawnId, int location) {
		super(shape, world);
		this.spawnId = spawnId;
		this.location = location;
	}
	
	public int getSpawnId() { return spawnId; }
	public int getExitTo() { return location; }
	
}
