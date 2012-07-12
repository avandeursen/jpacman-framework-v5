package org.jpacman.framework.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import org.jpacman.framework.controller.IController;

/**
 * The primary responsibility of this class is
 * to trigger the board viewer to display the
 * next animation.
 *
 * @author Arie van Deursen, 2007.
 * @version $Id: Animator.java 4222 2011-01-24 11:28:49Z arievandeursen $
 *
 */
public class Animator implements IController {

    /**
     * The viewer that must be informed to show the
     * next animation.
     */
    private final BoardView boardViewer;

    /**
     * The timer used.
     */
    private final Timer timer;

    /**
     * The delay between two animations.
     */
    private static final int DELAY = 200;

    /**
     * Create an animator for a particular board viewer.
     * @param bv The view to be animated.
     */
    public Animator(BoardView bv) {
        boardViewer = bv;
        timer = new Timer(DELAY,
                new ActionListener() {
            @Override
			public void actionPerformed(ActionEvent e) {
                doTick();
            }
        }
        );
    }

    /**
     * Stop triggering animation events.
     */
    @Override
	public void stop() {
        timer.stop();
    }

    /**
     * Start triggering animation events.
     */
    @Override
	public void start()  {
        timer.start();
    }
    
    @Override
    public void doTick() { 
    	boardViewer.nextAnimation();
    }
}
