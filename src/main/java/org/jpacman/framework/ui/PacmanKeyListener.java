package org.jpacman.framework.ui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Map keyboard events to jpacman events.
 * 
 * @author Arie van Deursen, TU Delft, Jan 29, 2012
 */
public class PacmanKeyListener implements KeyListener {
	
	/**
	 * The interface to the underlying model.
	 */
	private final IPacmanInteraction modelEvents;

	/**
	 * Create a new keyboard listener, given a handler
	 * for model events keyboard events should be mapped to.
	 * 
	 * @param me Events the model can handle.
	 */
	PacmanKeyListener(IPacmanInteraction me) {
		modelEvents = me;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// nothing.		
	}

	@Override
	public void keyPressed(KeyEvent event) {
		int code;

		code = event.getKeyCode();

		switch (code) {
		case KeyEvent.VK_UP: // or
		case KeyEvent.VK_K:
			modelEvents.up();
			break;
		case KeyEvent.VK_DOWN: // or
		case KeyEvent.VK_J:
			modelEvents.down();
			break;
		case KeyEvent.VK_LEFT: // or
		case KeyEvent.VK_H:
			modelEvents.left();
			break;
		case KeyEvent.VK_RIGHT: // or
		case KeyEvent.VK_L:
			modelEvents.right();
			break;
		case KeyEvent.VK_Q:
			modelEvents.stop();  
			break;
		case KeyEvent.VK_X:
			modelEvents.exit(); 
			break;
		case KeyEvent.VK_S:
			modelEvents.start(); 
			break;
		default:
			// all other events ignored.
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// nothing.
	}
}