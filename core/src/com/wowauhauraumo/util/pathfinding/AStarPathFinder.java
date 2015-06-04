package com.wowauhauraumo.util.pathfinding;

import java.util.ArrayList;

public class AStarPathFinder {
	
	private ArrayList<Node> closed = new ArrayList<Node>();
	private SortedList open = new SortedList();
	
	private TileBasedMap map;
	private int maxSearchDistance;
	
	private Node[][] nodes;
	private boolean allowDiagMovement;
	
	private AStarHeuristic heuristic;
	
	public AStarPathFinder(TileBasedMap map, int maxSearchDistance, boolean allowDiagMovement, AStarHeuristic heuristic) {
		this.heuristic = heuristic;
		this.map = map;
		this.maxSearchDistance = maxSearchDistance;
		this.allowDiagMovement = allowDiagMovement;
		
		nodes = new Node[map.getWidthInTiles()][map.getHeightInTiles()];
		for(int x = 0; x < map.getWidthInTiles(); x++) {
			for(int y = 0; y < map.getHeightInTiles(); y++) {
				nodes[x][y] = new Node(x, y);
			}
		}
	}
	
	public Path findPath(Mover mover, int sx, int sy, int tx, int ty) {
		if(map.blocked(mover, tx, ty)) {
			return null;
		}
		
		nodes[sx][sy].cost = 0;
		nodes[sx][sy].depth = 0;
		closed.clear();
		open.clear();
		addToOpen(nodes[sx][sy]);
		
		nodes[tx][ty].parent = null;
		
		int maxDepth = 0;
		while(open.size() != 0 && maxDepth < maxSearchDistance) {
			Node current = getFirstInOpen();
			if(current == nodes[tx][ty]) {
				break;
			}
			removeFromOpen(current);
			addToClosed(current);
			
			for(int x = -1; x < 2; x++) {
				for(int y = -1; y < 2; y++) {
					if(x == 0 && y == 0) {
						continue;
					}
					
					if(!allowDiagMovement) {
						if(x != 0 && y != 0) {
							continue;
						}
					}
					
					int xp = x + current.x;
					int yp = x + current.x;
					
					if(isValidLocation(mover, sx, sy, xp, yp)) {
						int nextStepCost = (int) (current.cost + getMovementCost(mover, current.x, current.y, xp, yp));
						Node neighbour = nodes[xp][yp];
						map.pathFinderVisited(xp, yp);
						
						if(nextStepCost < neighbour.cost) {
							if(inOpenList(neighbour)) {
								removeFromOpen(neighbour);
							}
							if(inClosedList(neighbour)) {
								removeFromClosed(neighbour);
							}
						}
						
						if(!inOpenList(neighbour) && !inClosedList(neighbour)) {
							neighbour.cost = nextStepCost;
							maxDepth = Math.max(maxDepth, neighbour.setParent(current));
							neighbour.heuristic = getHeuristicCost(mover, xp, yp, tx, ty);
							open.add(neighbour);
						}
					}
				}
			}
		}
		
		if(nodes[tx][ty].parent == null) {
			return null;
		}
		
		Path path = new Path();
		Node target = nodes[tx][ty];
		while(target != nodes[sx][sy]) {
			path.prependStep(target.x, target.y);
			target = target.parent;
		}
		
		path.prependStep(sx, sy);
		
		return path;
	}
	
	// getters/setters for the lists
	private Node getFirstInOpen() { return open.first(); }
	
	private void addToOpen(Node node) { open.add(node); }
	
	private boolean inOpenList(Node node) { return open.contains(node); }
	
	private void removeFromOpen(Node node) { open.remove(node); }
	
	private void addToClosed(Node node) { closed.add(node); }
	
	private boolean inClosedList(Node node) { return closed.contains(node); }
	
	private void removeFromClosed(Node node) { closed.remove(node); }
	
	/** checks if a given location is valid for the specified mover */
	private boolean isValidLocation(Mover mover, int sx, int sy, int x, int y) {
		boolean invalid = (x < 0) || (y < 0) || (x >= map.getWidthInTiles()) || (y >= map.getHeightInTiles());
		
		if(!invalid && (sx != x || sy != y)) {
			invalid = map.blocked(mover, x, y);
		}
		
		return !invalid;
	}
	
	private float getHeuristicCost(Mover mover, int x, int y, int tx, int ty) {
		return heuristic.getCost(map, mover, x, y, tx, ty);
	}
	
	private float getMovementCost(Mover mover, int sx, int sy, int tx, int ty) {
		return map.getCost(mover, sx, sy, tx, ty);
	}
	
	private class SortedList {
		private ArrayList<Node> list = new ArrayList<Node>();
		
		public Node first() { return list.get(0); }
		
		public void clear() { list.clear(); }
		
		public void add(Node o) { list.add(o); }
		
		public void remove(Node o) { list.remove(o); }
		
		public int size() { return list.size(); }
		
		public boolean contains(Node o) { return list.contains(o); }
	}
	
	private class Node implements Comparable<Node> {
		private int x;
		private int y;
		private float cost;
		private Node parent;
		private float heuristic;
		private int depth;
		
		public Node(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		public int setParent(Node parent) {
			depth = parent.depth + 1;
			this.parent = parent;
			
			return depth;
		}
		
		public int compareTo(Node other) {
			Node o = (Node) other;
			
			float f = heuristic + cost;
			float of = o.heuristic + o.cost;
			
			if(f < of) {
				return -1;
			} else if(f > of) {
				return 1;
			} else {
				return 0;
			}
		}
	}
	
}
