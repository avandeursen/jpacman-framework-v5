package org.jpacman.framework.model;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * An individual tile in the game, with coordinates
 * and associations with the containing board and the 
 * sprites on the cell.
 * 
 * @author Arie van Deursen, TU Delft, December, 2011
 */
public class Tile {

	private final int x;
	private final int y;
	private final Deque<Sprite> sprites;
	
	/**
	 * Create a new tile for the given location.
	 * @param x horizontal coordinate
	 * @param y vertical coordinate
	 */
	public Tile(int x, int y) {
		this.x = x;
		this.y = y;
		sprites = new ArrayDeque<Sprite>();
	}
	
	/**
	 * Any sprite on this tile should have its location
	 * set so that it indeed points to this tile.
	 * @return true iff this invariant holds
	 */
	protected boolean tileInvariant() {
		boolean result = true;
		for (Sprite s : sprites) {
			result = result && this.equals(s.getTile());
		}
		return result;
	}
	
	/**
	 * @return The horizontal X coordinate
	 */
	public int getX() {
		return x;
	}

	/**
	 * @return The vertical Y coordinate
	 */
	public int getY() {
		return y;
	}

	/**
	 * @return The most recently added, hence visible, sprite.
	 */
	public Sprite topSprite() {
		return sprites.peekLast();
	}
	
	/**
	 * @param sprite possibly living on this tile
	 * @return True iff sprite is on this tile.
	 */
	public boolean containsSprite(Sprite sprite) {
		return sprites.contains(sprite);
	}

	/**
	 * Remove one of the sprites from this tile.
	 * @param sprite The sprite to be removed.
	 */
	protected void dropSprite(Sprite sprite) {
		assert tileInvariant();
		assert sprite != null;
		sprites.remove(sprite);
		assert tileInvariant();
	}
	
	/**
	 * Add a sprite to the given tile.
	 * @param sprite The sprite to be added.
	 */
	protected void addSprite(Sprite sprite) {
		assert tileInvariant();

		assert sprite != null;
		assert !containsSprite(sprite) : "Pre: sprite not yet on tile.";
		
		sprites.addLast(sprite);
		
		assert containsSprite(sprite) : "Post: sprite on tile.";
		assert tileInvariant();
	}
	
	@Override
	public String toString() {
		return "[" + getX() + "," + getY() + "]";
	}
}
