/**
 * 
 */
package org.jpacman.test.framework.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jpacman.framework.controller.IController;
import org.jpacman.framework.controller.RandomGhostMover;
import org.jpacman.framework.factory.FactoryException;
import org.jpacman.framework.ui.MainUI;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Stefan Hugtenburg - Jesse Donkervliet
 *
 */
public class MainUITest {
	
	private MainUI mainui;

	/**
	 * Create a MainUI to test with.
	 */
	@Before
	public void setUp() {
		mainui = new MainUI();
	}

	/**
	 * Test to check the initialize function.
	 * This function should create a game, while not changing the GhostController.
	 * @throws FactoryException when calling MainUI.initialize() fails.
	 */
	@Test
	public void testInitialize() throws FactoryException {	
		assertNull(mainui.getGame());
		IController ghostmover = mainui.getGhostController();
		
		mainui.initialize();
		
		assertNotNull(mainui.getGame());
		assertEquals(mainui.getGhostController(), ghostmover);
	}
	
	/**
	 * Test for the withGhostController function.
	 * This setter should always work if createUI has not yet been called.
	 * @throws FactoryException if the initialize or createUI functions fail.
	 */
	@Test
	public void testWithGhostController() throws FactoryException {	
		mainui.initialize();
		assertNull(mainui.getGhostController());
		
		IController ghostmover1 = new RandomGhostMover(mainui.getGame());
		IController ghostmover2 = new RandomGhostMover(mainui.getGame());
		IController ghostmover3 = new RandomGhostMover(mainui.getGame());
		
		//Below we apply forced pointer comparison to check the setter.
		mainui.withGhostController(ghostmover1);
		assertTrue(mainui.getGhostController() == ghostmover1);
		
		mainui.withGhostController(ghostmover2);
		assertTrue(mainui.getGhostController() == ghostmover2);
		
		mainui.withGhostController(ghostmover3);
		assertTrue(mainui.getGhostController() == ghostmover3);
		
		//The createUI function should not affect our current GhostController.
		mainui.createUI();
		
		assertTrue(mainui.getGhostController() == ghostmover3);
		
	}
	
	/**
	 * Test to see the createUI function fail,
	 * because no GhostController has been set.
	 * @throws FactoryException if the initialize or createUI functions fail.
	 */
	@Test (expected = java.lang.AssertionError.class)
	public void testCreateUIWithoutGhostController() throws FactoryException {
		mainui.initialize();
		//Do not set a GhostController, this should cause an assertion error.
		mainui.createUI();
	}
	
	/**
	 * Test to see the withGhostController function fail,
	 * because the withGhostController should not be allowed to
	 * be called after the UI has been created by createUI.
	 * @throws FactoryException if the initialize or createUI functions fail.
	 */
	@Test (expected = java.lang.AssertionError.class)
	public void testChangeGhostControllerAfterUI() throws FactoryException {
		mainui.initialize();
		mainui.withGhostController(new RandomGhostMover(mainui.getGame()));
		mainui.createUI();
		//After creating the UI, the GhostController should not be allowed to be changed.
		//This should cause an assertion error.
		mainui.withGhostController(new RandomGhostMover(mainui.getGame()));
	}
	
	/**
	 * Test to see if the initializeNormalGame works properly
	 * and sets all necessary fields, while providing the game with a RandomGhostMover.
	 * @throws FactoryException if the initialize or createUI functions fail.
	 */
	@Test
	public void testInitializeNormalGame() throws FactoryException {
		mainui.initializeNormalGame();
		
		assertNotNull(mainui.getGame());
		assertTrue(mainui.getGhostController() instanceof RandomGhostMover);
		//Check for the existence of the UI.
		assertNotNull(mainui.eventHandler());
	}
		
}
