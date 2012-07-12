package org.jpacman.framework.model;

/**
 * Keep track of points (still) in the game.
 * 
 * @author Arie van Deursen, TU Delft, Jan 21, 2012
 */
public class PointManager implements IPointInspector {
	
	private int pointsPutOnBoard = 0;
	private int pointsEarned = 0;
	
	/**
	 * @return True iff points are positive.
	 */
	protected boolean invariant() {
		return pointsEarned >= 0
			&& pointsEarned <= pointsPutOnBoard;
	}
	
	/**
	 * While building the game, keep track of the
	 * total number of points.
	 * @param delta Points to be added to the game.
	 */
	public void addPointsToBoard(int delta) {
		assert delta >= 0;
		pointsPutOnBoard += delta;
		assert invariant();
	}
	
	/**
	 * While playing, eat food.
	 * @param delta Amount of food consumed.
	 */
	private void consumePointsOnBoard(int delta) {
		pointsEarned += delta;
		assert invariant();
	}
	
	/**
	 * While playing, let the player consume food.
	 * @param p Player actually eating.
	 * @param delta Amount of food eaten.
	 */
	public void consumePointsOnBoard(Player p, int delta) {
		p.addPoints(delta);
		consumePointsOnBoard(delta);
		assert invariant();
	}
		
	
	/**
	 * The game is over if everything has been eaten.
	 * @return Whether all points have been consumed.
	 */
	@Override
	public boolean allEaten() {
		assert invariant();
		return pointsEarned == pointsPutOnBoard;
	}

	/**
	 * @return The total amount of points actually consumed so far.
	 */
	@Override
	public int getFoodEaten() {
		return pointsEarned;
	}
	
	/**
	 * @return The total amount of food put in the game.
	 */
	@Override
	public int totalFoodInGame() {
		return pointsPutOnBoard;
	}

}
