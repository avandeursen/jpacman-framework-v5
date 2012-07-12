package org.jpacman.framework.model;

/**
 * Directions in which sprites can move.
 * 
 * @author Arie van Deursen, TU Delft, Jan 23, 2012
 */
public enum Direction {
	
	UP(0, -1), 
	DOWN(0, 1),
	LEFT(-1, 0),
	RIGHT(1, 0);
	
	private int dx, dy;
	
	/**
	 * Create a new direction.
	 * @param dx horizontal move
	 * @param dy vertical move
	 */
	Direction(int dx, int dy) {
		this.dx = dx;
		this.dy = dy;
	}
	
	/**
	 * @return Number of cells to move in horizontal direction
	 */
	public int getDx() {
		return dx;
	}
	
	/**
	 * @return Number of cells to move in vertical direction.
	 */
	public int getDy() {
		return dy;
	}
}
