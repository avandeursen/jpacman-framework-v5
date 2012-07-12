package org.jpacman.test.framework.accept;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.jpacman.framework.factory.FactoryException;
import org.jpacman.framework.model.Direction;
import org.jpacman.framework.model.IBoardInspector;
import org.jpacman.framework.model.Tile;
import org.junit.Before;
import org.junit.Test;

/**
 * Acceptance test suite for ghost moves, exercising JPacman via the ui.
 * 
 * When asserting the results, the key effects are validated only,
 * as expressed in the "then" clause of the BDD scenarios.
 * Rigorous testing of all relevant effects is done in the unit
 * tests.
 *
 * The test harness from the superclass is reused in this test. 
 * 
 * @author Arie van Deursen, TU Delft, Feb 4, 2012
 */
public class MoveGhostStoryTest extends AbstractAcceptanceTest {
	
	// Emtpy tile on the board next to the ghost.		
	private Tile emptyTile;
	
	// Food tile on the board next to the ghost.
	private Tile foodTile;
	
	/**
	 * Setup the game, given a simpler map to
	 * improve controllability.
	 * 
	 * @throws FactoryException When map loading fails.
	 * @throws InterruptedException When controllers fail.
	 */
	@Override
	@Before
	public void setUp() throws FactoryException, InterruptedException {
		super.setUp();
		emptyTile = tileAt(2, 2);
		foodTile = tileAt(2, 0);
	}
	
	/**
	 * Test that a ghost can move towards an empty tile.
	 */
	@Test
	public void test_S3_1_GhostEmpty() {
		// given
		getEngine().start();
		// when
		getUI().getGame().moveGhost(theGhost(), Direction.DOWN);		
		// then
		assertEquals(emptyTile, theGhost().getTile());
	}
	
	/**
	 * Test that a ghost can move over food.
	 */
	@Test
	public void test_S3_2_GhostFood() {
		// given
		getEngine().start();
		// when
		getUI().getGame().moveGhost(theGhost(), Direction.UP);		
		// then
		assertEquals(foodTile, theGhost().getTile());
		assertEquals(IBoardInspector.SpriteType.GHOST, foodTile.topSprite().getSpriteType());
	}
	
	/**
	 * Thest that a ghost can leave the food once he's on it.
	 */
	@Test
	public void test_S3_3_GhostLeavesFood() {
		// given
		test_S3_2_GhostFood();
		// when
		getUI().getGame().moveGhost(theGhost(), Direction.DOWN);		
		// then
		assertEquals(IBoardInspector.SpriteType.FOOD, foodTile.topSprite().getSpriteType());
	}

	/**
	 * Test that the ghost causes the player's death if the ghost bumps into the player.
	 */
	@Test
	public void test_S3_4_GhostKillsPlayer() {
		// given
		getEngine().start();
		// when
		getUI().getGame().moveGhost(theGhost(), Direction.LEFT);		
		// then
		assertFalse(getPlayer().isAlive());
	}
}
