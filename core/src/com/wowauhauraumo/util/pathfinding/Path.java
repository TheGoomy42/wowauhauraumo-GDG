package com.wowauhauraumo.util.pathfinding;

import java.util.ArrayList;

public class Path {
	
	private ArrayList<Step> steps = new ArrayList<Step>();
	
	public int getLength() { return steps.size(); }
	public Step getStep(int i) { return steps.get(i); }
	public int getX(int i) { return getStep(i).x; }
	public int getY(int i) { return getStep(i).y; }
	public void appendStep(int x, int y) { steps.add(new Step(x, y)); }
	public void prependStep(int x, int y) { steps.add(0, new Step(x, y)); }
	/** wahey swag function here boyz
	 * .contains() apparently uses .equalto(), which uses hashCode() to check equivalence. This means that, if we give Step()
	 * the same x and y, it will appear to be equivalent to they new Step() we are making here.
	 * so much swag
	 */
	public boolean contains(int x, int y) { return steps.contains(new Step(x, y)); }
	
	
	public class Step {
		private int x;
		private int y;
		
		public Step(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		public int getX() { return x; }
		public int getY() { return y; }
		
		@Override
		public int hashCode() {
			return x * y;
		}
		
		@Override
		public boolean equals(Object other) {
			if(other instanceof Step) {
				Step o = (Step) other;
				
				return (o.x == x) && (o.y == y);
			}
			
			return false;
		}
	}
	
}
