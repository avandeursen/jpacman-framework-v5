package org.jpacman.framework.model;

/**
 * Get informed about the number of points
 * earned and the points still available.
 * 
 * @author Arie van Deursen, TU Delft, Jan 21, 2012
 */
public interface IPointInspector {

	/**
	 * @return The amount of food eaten so far.
	 */
	int getFoodEaten();

	/**
	 * @return The total amount of food in the game.
	 */
	int totalFoodInGame();

	/**
	 * @return True iff all food has been eaten.
	 */
	boolean allEaten();
}
