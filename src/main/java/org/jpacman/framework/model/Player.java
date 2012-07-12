package org.jpacman.framework.model;

import org.jpacman.framework.model.IBoardInspector.SpriteType;

/**
 * A player, responsible for keeping track of the
 * amount of food eaten, and whether or not he is still
 * alive.
 * 
 * @author Arie van Deursen, TU Delft, 2012.
 */
public class Player extends Sprite {
	
	private int points = 0;
	private boolean alive = true;
	private Direction direction = Direction.LEFT;
	
	/**
	 * @return The amount of food eaten by this player.
	 */
	public int getPoints() {
		return points;
	}
	
	/**
	 * @return True iff points are always positive.
	 */
	protected boolean playerInvariant() {
		return points >= 0 && spriteInvariant();
	}
	
	/**
	 * Increment the amount of food eaten.
	 * @param extraPoints Amount to be added.
	 * @return Current total of points.
	 */
	public int addPoints(int extraPoints) {
		assert isAlive();
		assert playerInvariant();
		points += extraPoints;
		assert playerInvariant();
		return points;
	}

	/**
	 * This player dies.
	 */
	public void die() {
		alive = false;
	}
	
	/**
	 * @return True iff the player is still alive.
	 */
	public boolean isAlive() {
		return alive;
	}
	
	/**
	 * @return That this sprite is a player.
	 */
	@Override
	public SpriteType getSpriteType() {
		return SpriteType.PLAYER;
	}
	
	/**
	 * @return The direction the player is moving to.
	 */
	public Direction getDirection() {
		return direction;
	}

	/**
	 * @param nextDir The new direction the player is moving to.
	 */
	public void setDirection(Direction nextDir) {
		direction = nextDir;
	}

	/**
	 * The player returns from the death.
	 */
	public void resurrect() {
		alive = true;
	}

	
}
