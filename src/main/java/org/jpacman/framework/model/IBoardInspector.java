package org.jpacman.framework.model;

/**
 * Interface for inspecting the content of the board.
 * 
 * @author Arie van Deursen, TU Delft, Jan 15, 2012
 */
public interface IBoardInspector {

	/**
	 * @return The width of the board, in number of cells.
	 */
	int getWidth();

	/**
	 * @return The height of the board, in number of cells.
	 */
	int getHeight();
	
	/**
	 * The predefined set of sprite types.
	 */
	public enum SpriteType { 
		PLAYER, 
		GHOST, 
		FOOD, 
		EMPTY, 
		WALL, 
		OTHER
	};
		
	/**
	 * @param x 
	 * @param y 
	 * @return The sprite at location (x,y).
	 */
	Sprite spriteAt(int x, int y);
	
	/**
	 * @param x x-coordinate
	 * @param y y-coordinate
	 * @return The kind of sprite at location (x,y).
	 */
	SpriteType spriteTypeAt(int x, int y);

	/**
	 * @param x x-coordinate
	 * @param y y-coordinate
	 * @return The tile at the given location.
	 */
	Tile tileAt(int x, int y);	
}
