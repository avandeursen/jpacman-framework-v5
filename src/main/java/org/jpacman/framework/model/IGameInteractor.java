package org.jpacman.framework.model;

import java.util.List;
import java.util.Observer;

/**
 * Interface for manipulating the game.
 * 
 * @author Arie van Deursen, TU Delft, Jan 30, 2012
 */
public interface IGameInteractor {
	
	/**
	 * Move the player into the given direction.
	 * @param dir Direction to be moved to.
	 */
	void movePlayer(Direction dir);
	
	/**
	 * Add an observer keeping track of successful moves.
	 * @param o The observer interested in what's going on.
	 */
	void attach(Observer o);
	
	/**
	 * Move a ghost in the given direction.
	 * @param g The ghost to be moved
	 * @param dir The direction to be taken.
	 */
	void moveGhost(Ghost g, Direction dir);

	/**
	 * @return The ghosts present in the game.
	 */
	List<Ghost> getGhosts();

	/**
	 * @return The object keeping track of points.
	 */
	IPointInspector getPointManager();

	/**
	 * @return Inspection operations for the underlying board.
	 */
	IBoardInspector getBoardInspector();
	
	/**
	 * @return true iff the player has died.
	 */
	boolean died();
	
	/**
	 * @return true iff the player has won.
	 */
	boolean won();
}
