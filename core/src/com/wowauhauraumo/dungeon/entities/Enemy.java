//package com.wowauhauraumo.dungeon.entities;
//
//import com.badlogic.gdx.math.Vector2;
//import com.badlogic.gdx.physics.box2d.Body;
//import com.wowauhauraumo.util.pathfinding.PathFinder;
//
//public abstract class Enemy extends B2DSprite {
//	
//	protected boolean aggro;
//	protected int aggroRange;
//	
//	protected PathFinder pathFinder;
//
//	public Enemy(Body body, PathFinder pathFinder) {
//		super(body);
//		this.pathFinder = pathFinder;
//		aggro = false;
//	}
//	
//	public void update(float delta, Player player) {
//		super.update(delta);
//		// decide if and where to move
//		Vector2 playerDist = player.getPosition().add(-getPosition().x, -getPosition().y);
//		
//		if(aggro) {
//			// path towards player, use A* algorithm at some point, path only into range
//			pathFinder.findPath(this, getPosition().x, getPosition().y, player.getPosition().x, player.getPosition().y);
//		} else {
//			if(Math.abs(playerDist.x - getPosition().x) < (int) aggroRange / 2 &&
//					Math.abs(playerDist.y - getPosition().y) < (int) aggroRange / 2) {
//				aggro = true;
//				System.out.println("aggroed");
//			}
//		}
//	}
//
//}
