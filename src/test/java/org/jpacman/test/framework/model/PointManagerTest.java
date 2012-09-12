package org.jpacman.test.framework.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.jpacman.framework.model.Player;
import org.jpacman.framework.model.PointManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Test the basic behavior of a point manager.
 * @author Arie van Deursen, TU Delft, Mar 1, 2012
 */
@RunWith(MockitoJUnitRunner.class)
public class PointManagerTest {
	
	private PointManager pm;
	private final int totalPoints = 10;
	@Mock private Player player;
	
	/**
	 * Create a point manager, and give it some initial points.
	 */
	@Before public void setUp() {
		pm = new PointManager();
		pm.addPointsToBoard(totalPoints);
	}

	/**
	 * After food has been added, there's more to eat.
	 */
	@Test public void testAdd() {
		assertFalse(pm.allEaten());
	}
	
	/**
	 * After half has been eaten, there's still more to eat.
	 */
	@Test public void testEatSomeFood() {
		final int pointsToEat = totalPoints / 2;
		pm.consumePointsOnBoard(player, pointsToEat);
		assertFalse(pm.allEaten());
		assertEquals(pointsToEat, pm.getFoodEaten());
	}
	
	/**
	 * Detect that all has been eaten.
	 */
	@Test public void testEatAll() {
		pm.consumePointsOnBoard(player, totalPoints);
		assertTrue(pm.allEaten());
	}
}
