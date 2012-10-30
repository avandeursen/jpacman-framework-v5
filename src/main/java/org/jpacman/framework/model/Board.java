package org.jpacman.framework.model;

/**
 * The rectangular board containing the sprites.
 * 
 * @author Arie van Deursen, TU Delft, Jan 22, 2012
 */
public class Board implements IBoardInspector {

	private final int height;
	private final int width;
	private final Tile[][] tiles;
	
	/**
	 * Create a new board.
	 * @param w Width of the board
	 * @param h Height of the board.
	 */
	public Board(int w, int h) {
		assert w >= 0 : "PRE1: width should be >= 0 but is " + w;
		assert h >= 0 : "PRE2: height should be >= 0 but is " + h;
		width = w;
		height = h;
		tiles = new Tile[width][height];
		
		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				tiles[x][y] = new Tile(x, y); 
			}
		}
		
		assert tileInvariant() : "POST: invariant should hold";
	}
	
	/**
	 * Example invariant that should hold for the various
	 * tiles stored in the tiles array. 
	 * (May be a bit overkill to actually check this rather
	 * straightforward invariant. The example also illustrates
	 * the lack of universal quantification that would
	 * make this invariant more natural).
	 * @return True iff all tiles are created correctly.
	 */
	protected final boolean tileInvariant() {
		boolean result = true;
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				result = result 
				&& (tileAt(x, y).getX() == x) 
				&& (tileAt(x, y).getY() == y);
			}
		}
		return result;
	}
	
	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public int getWidth() {
		return width;
	}

	/**
	 * Put a sprite at a given position.
	 * @param s Sprite to be put on the board
	 * @param x x-coordinate
	 * @param y y-coordinate
	 */
	public void put(Sprite s, int x, int y) {
		assert withinBorders(x, y) : "PRE1: " + onBoardMessage(x, y);	
		assert s != null : "PRE2: Sprite not null";
		assert s.getTile() == null : "PRE3: Sprite should not occupy" + s.getTile();
		s.occupy(tileAt(x, y));
	}
	
	/**
	 * Verify that the given location falls within the
	 * borders of the board.
	 * @param x x-coordinate
	 * @param y y-coordinate
	 * @return True iff (x,y) falls within the board.
	 */
	public boolean withinBorders(int x, int y) {
		return
			x >= 0 && x < width 
			&& y >= 0 && y < height;
	}

	@Override
	public Sprite spriteAt(int x, int y) {
		assert withinBorders(x, y) : "PRE: " + onBoardMessage(x, y);		
		return tileAt(x, y).topSprite();
	}
	
	@Override
	public SpriteType spriteTypeAt(int x, int y) {
		assert withinBorders(x, y) : "PRE: " + onBoardMessage(x, y);	
		Sprite s = spriteAt(x, y);
		SpriteType result;
		if (s == null) {
			result = SpriteType.EMPTY;
		} else {
			result = s.getSpriteType();
		}
		return result;
	}

	/**
	 * @param x x-coordinate
	 * @param y y-coordinate
	 * @return The tile at the given (x,y) place
	 */
	public Tile tileAt(int x, int y) {
		assert withinBorders(x, y) : "PRE: " + onBoardMessage(x, y);
		return tiles[x][y];
	}
	
	/**
	 * Return a tile at position (x+dx, y+dy) from current
	 * tile at (x,y).
	 * @param start Reference tile / starting point.
	 * @param dx delta in x direction
	 * @param dy delta in y direction
	 * @return Tile at (x+dx, y+dy)
	 */
	public Tile tileAtOffset(Tile start, int dx, int dy) {
		assert start != null : "PRE1: start tile should not be null.";
		assert withinBorders(start.getX(), start.getY())
				: "PRE2" +  onBoardMessage(start.getX(), start.getY());	
		
        int newx = tunnelledCoordinate(start.getX(), getWidth(), dx);
        int newy = tunnelledCoordinate(start.getY(), getHeight(), dy);
        assert withinBorders(newx, newy);
        
        Tile result = tileAt(newx, newy);
        
        assert result != null : "POST: result should not be null.";
        return result;
	}
	
	/**
	 * From starting coordinate 0 <= current < max, add delta to the current
	 * position, but taking care of wormholes.
	 * @param current Starting position
	 * @param max Maximum length of the range
	 * @param delta Increment to make on the delta (positive or negative).
	 * @return current + delta within [0..max]
	 */
	private int tunnelledCoordinate(int current, int max, int delta) {
		assert current >= 0 : "PRE: current should be >= 0 but is " + current;
		assert current < max : "PRE: current should be < max but is " + current;

		// additional max needed if (current + delta) < 0.
		int result = ((current + delta) % max + max) % max;

		assert result >= 0 : "POST: result should be >= 0, but is " + result;
		assert result < max : "POST: result should be < max, but is " + max;

		return result;
	}
	
	/**
	 * Obtain the tile in the given direction.
	 * @param t Starting position
	 * @param dir Direction
	 * @return Tile in direction from the given starting position.
	 */
	public Tile tileAtDirection(Tile t, Direction dir) {
		return tileAtOffset(t, dir.getDx(), dir.getDy());
	}
	
	/**
	 * Convenience method to yield a useful error message in case
	 * of an assertion failure due to a cell that is not on the board.
	 * @param x xcoordinate
	 * @param y ycoordinate
	 * @return Message including actual values of x, y, and board dimensions.
	 */
	private String onBoardMessage(int x, int y) {
		return
			"(" + x + ", " + y + ")"
			+ "not on board of size "
			+ getWidth() + " * " + getHeight();
	}
}
