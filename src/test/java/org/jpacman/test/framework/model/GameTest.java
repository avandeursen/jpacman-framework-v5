package org.jpacman.test.framework.model;


import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.mock;

import java.util.Observable;
import java.util.Observer;

import org.jpacman.framework.factory.DefaultGameFactory;
import org.jpacman.framework.factory.FactoryException;
import org.jpacman.framework.factory.IGameFactory;
import org.jpacman.framework.factory.MapParser;
import org.jpacman.framework.model.Direction;
import org.jpacman.framework.model.Food;
import org.jpacman.framework.model.Game;
import org.jpacman.framework.model.Ghost;
import org.jpacman.framework.model.IBoardInspector.SpriteType;
import org.jpacman.framework.model.Player;
import org.jpacman.framework.model.Tile;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;


/**
 * Test suite for games.
 *
 * The key functionality involves moving ghosts and
 * players, which is tested according to a decision 
 * table (see the docs file).
 * 
 * The test suite is open for extension for testing
 * Game subclasses.
 * 
 *  @author: Arie van Deursen, March 2012 
 */
@RunWith(MockitoJUnitRunner.class)
public class GameTest {
	
		
	/**
	 * Factory method to create a new gameFactory.
	 * Subclasses can override this method to provide
	 * a specialized factory.
	 * @return The new game factory.
	 */
	public IGameFactory makeFactory() {
		return new DefaultGameFactory();
	}
	
	/**
	 * Simply create a single row game.
	 * The resulting game is returned, and can also
	 * be obtained via getGame().
	 * 
	 * @param singleRow String representation of one row
	 * @throws FactoryException If singleRow contains illegal chars.
	 * @return The game created while parsing the row.
	 */
	protected Game makePlay(String singleRow) throws FactoryException {
		MapParser p = new MapParser(makeFactory());
		Game theGame = p.parseMap(new String[]{singleRow});
		return theGame;
	}
	
	
	/**
	 * Simple sanity checks for the initial setup.
	 * 
	 * @throws FactoryException Never.
	 */
	@Test
	public void testInitialSetting() throws FactoryException {
		Game g = makePlay("P");
		assertEquals(g.getPlayer(), g.getBoard().spriteAt(0, 0));
		assertThat(tileAt(g, 0, 0), equalTo(g.getPlayer().getTile()));
		assertEquals(SpriteType.PLAYER, g.getBoard().spriteTypeAt(0, 0));
		assertEquals(0, g.getPlayer().getPoints());
		assertTrue(g.getPlayer().isAlive());
		assertEquals(Direction.LEFT, g.getPlayer().getDirection());
	}

	
	/**
	 * Test situation that player moves to 
	 * an empty cell.
	 * 
	 * @throws FactoryException Can't happen.
	 */
	@Test
	public void testC1a_PlayerMovesToEmpty() throws FactoryException {		
		Game g = makePlay("P #");
		g.movePlayer(Direction.RIGHT);
		
		assertEquals("Player moved", tileAt(g, 1, 0), g.getPlayer().getTile());
		assertEquals("No food eaten.", 0, g.getPlayer().getPoints());
		assertEquals(Direction.RIGHT, g.getPlayer().getDirection());
	}

	/**
	 * Test situation that player moves to 
	 * an empty cell.
	 * 
	 * @throws FactoryException Can't happen.
	 */
	@Test
	public void testC1b_GhostMovesToEmpty() throws FactoryException {
		Game g = makePlay("G #");
		Ghost theGhost = (Ghost) g.getBoard().spriteAt(0, 0);

		g.moveGhost(theGhost, Direction.RIGHT);
		
		assertEquals("Ghost moved", tileAt(g, 1, 0), theGhost.getTile());
	}


	/**
	 * Test that player tries to move into a wall;
	 * This should not be possible.
	 * 
	 * @throws FactoryException Can't happen.
	 */
	@Test
	public void testC2a_PlayerMovesToWall() throws FactoryException {		
		Game g = makePlay("#P.");
		g.movePlayer(Direction.LEFT);
		assertThat("Still at the start", 
				tileAt(g, 1, 0), equalTo(g.getPlayer().getTile()));
	}
	
	/**
	 * Test situation that ghost moves to 
	 * a wall.
	 * 
	 * @throws FactoryException Can't happen.
	 */
	@Test
	public void testC2b_GhostMovesToWall() throws FactoryException {
		Game g = makePlay(" G#");
		Ghost theGhost = (Ghost) g.getBoard().spriteAt(1, 0);

		g.moveGhost(theGhost, Direction.RIGHT);
		
		assertEquals("Ghost not moved", tileAt(g, 1, 0), theGhost.getTile());
	}

	
	/**
	 * Test that player dies if he bumps into a ghost.
	 * 
	 * @throws FactoryException Never.
	 */
	@Test
	public void testC3_PlayerMovesToGhost() throws FactoryException {		
		Game g = makePlay("PG#");
		Player p = g.getPlayer();
		
		g.movePlayer(Direction.RIGHT);
		
		assertFalse("Move kills player", p.isAlive());		
		assertThat(tileAt(g, 1, 0), equalTo(p.getTile()));
	}
	
	/**
	 * Test that a player indeed consumes food if he enters food.
	 * 
	 * @throws FactoryException Never.
	 */
	@Test
	public void testC5_PlayerMovesToFood() throws FactoryException {		
		Game game = makePlay("P.#");
		Food food = (Food) game.getBoard().spriteAt(1, 0);
		Player player = game.getPlayer();

		game.movePlayer(Direction.RIGHT);
		
		Tile newTile = tileAt(game, 1, 0);
		assertEquals("Food added", food.getPoints(), player.getPoints());
		assertEquals("Player moved", newTile.topSprite(), player);
		assertFalse("Food gone", newTile.containsSprite(food));
	}

	/**
	 * Test what happens if the ghost moves into the player.
	 * 
	 * @throws FactoryException Never.
	 */
	@Test
	public void testC4_GhostMovesToPlayer() throws FactoryException {		
		Game game = makePlay("PG#");
		Ghost theGhost = (Ghost) game.getBoard().spriteAt(1, 0);

		game.moveGhost(theGhost, Direction.LEFT);
		assertFalse("Move kills player", game.getPlayer().isAlive());
		
		Tile newTile = theGhost.getTile();
		assertThat(tileAt(game, 0, 0), equalTo(newTile));
	}
	
	/**
	 * Test situation that player moves to 
	 * a food cell.
	 * 
	 * @throws FactoryException Can't happen.
	 */
	@Test
	public void testC6_GhostMovesToFood() throws FactoryException {
		Game game = makePlay("G.#");
		Ghost theGhost = (Ghost) game.getBoard().spriteAt(0, 0);

		game.moveGhost(theGhost, Direction.RIGHT);		
		assertEquals("Ghost moved", tileAt(game, 1, 0), theGhost.getTile());

		game.moveGhost(theGhost, Direction.LEFT);
		assertEquals(SpriteType.FOOD, game.getBoard().spriteTypeAt(1, 0));
	}

	
	/**
	 * Test that the observers are informed after a player move.
	 * 
	 * @throws FactoryException Never.
	 */
	@Test 
	public void testObserverAfterPlayerMove() throws FactoryException {
		Observer anObserver = mock(Observer.class);
		Game g = makePlay("P #");
		g.addObserver(anObserver);
		
		g.movePlayer(Direction.RIGHT);
		verify(anObserver).update(any(Observable.class), anyObject());
	}
	
	/**
	 * Test that the observers are informed after a ghost move.
	 * 
	 * @throws FactoryException Never.
	 */
	@Test 
	public void testObserverAfterGhostMove() throws FactoryException {
		// given
		Observer anObserver = mock(Observer.class);
		Game game = makePlay("G #");
		game.addObserver(anObserver);
		Ghost ghost = (Ghost) game.getBoard().spriteAt(0, 0); 
		// when
		game.moveGhost(ghost, Direction.RIGHT);
		// then
		verify(anObserver).update(any(Observable.class), anyObject());
	}
	
	/**
	 * Test that tunnels (empty cells on the boarder) 
	 * are properly handled.
	 * 
	 * @throws FactoryException Never.
	 */
	@Test 
	public void testTunneledMove() throws FactoryException {
		Game g = makePlay("P# ");
		g.movePlayer(Direction.LEFT);
		
		Tile newTile = g.getPlayer().getTile();
		assertThat("Player moved", tileAt(g, 2, 0), equalTo(newTile));
	}

	
	/**
	 * Convenience method to make assertion checking more natural.
	 * 
	 * @param g Game containing the board
	 * @param x x-coordinate
	 * @param y y-coordinate
	 * @return The tile at the given location in the board under test.
	 */
	protected Tile tileAt(Game g, int x, int y) {
		return g.getBoard().tileAt(x, y);
	}

	
	
}
