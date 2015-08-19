package com.wowauhauraumo.dungeon.maps;

import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

public class Opening extends MapUpdater {
	
	public Opening(Shape shape, World world, int entranceTo, int spawnId) {
		// THIS MIGHT NEED TO PASS THROUGH 'this' BECAUSE 'this' IN MAPUPDATER MIGHT REFERENCE MAPUPDATER ITSELF!!!!!
		super(shape, world);
		this.location = entranceTo;
		this.spawnId = spawnId;
	}
	
	public int getEntranceTo() { return location; }
	public int getSpawnId() { return spawnId; }
	
}
