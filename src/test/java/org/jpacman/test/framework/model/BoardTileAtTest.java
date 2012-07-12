package org.jpacman.test.framework.model;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;

import org.jpacman.framework.model.Board;
import org.jpacman.framework.model.Direction;
import org.jpacman.framework.model.Tile;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * Test the Board.tileAt method by means of a series of test cases with
 * Parameterized data.
 * 
 * @author Arie van Deursen, TU Delft, Feb 10, 2012
 */
@RunWith(Parameterized.class)
public class BoardTileAtTest {

	private final int startx, starty;
	private final Direction dir;
	private final int nextx, nexty;
	private final Board board;

	private static final int WIDTH = 10;
	private static final int HEIGHT = 20;

	/**
	 * Create a new test case obtaining the tile
	 * from starting point (x, y) into the given direction.
	 * The result should be the new point (nx, ny).
	 * @param x Start x
	 * @param y Start y
	 * @param d way to go
	 * @param nx Next x
	 * @param ny Next y
	 */
	public BoardTileAtTest(int x, int y, Direction d, int nx, int ny) {
		startx = x;
		starty = y;
		dir = d;
		nextx = nx;
		nexty = ny;
		board = new Board(WIDTH, HEIGHT);
	}

	/** 
	 * The actual test case.
	 */
	@Test
	public void testTileAtDirection() {
		Tile source = board.tileAt(startx, starty);
		Tile actual = board.tileAtDirection(source, dir);
		Tile desired = board.tileAt(nextx, nexty);
		assertEquals(desired, actual);
	}

	/**
	 * List of (x,y)/Direction/(newx,newy) data points.
	 * @return Test data to be fed to constructor.
	 */
	@Parameters
	public static Collection<Object[]> data() {
		Object[][] values = new Object[][] {
				// x-axis boundaries, y random inpoints
				// left boundary
				{ 2, 2, Direction.UP, 2, 1 },
				{ 2, 2, Direction.DOWN, 2, 3 },
				{ 2, 2, Direction.LEFT, 1, 2 },
				{ 2, 2, Direction.RIGHT, 3, 2 },
				// worm holes
				{ 0, 2, Direction.LEFT, WIDTH - 1, 2 },
				{ WIDTH - 1, 2, Direction.RIGHT, 0, 2 },
				{ 2, 0, Direction.UP, 2, HEIGHT - 1 },
				{ 2, HEIGHT - 1, Direction.DOWN, 2, 0 } };
		return Arrays.asList(values);
	}

}
