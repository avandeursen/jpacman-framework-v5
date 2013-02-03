package org.jpacman.framework.controller;

/**
 * Interface for a controller which can be started
 * and stopped, and which generates ticks.
 * <p>
 *
 * @author Arie van Deursen, 3 September, 2003
 */

public interface IController {

    /**
     * Start the timer.
     */
    void start();

    /**
     * Stop the timer.
     */
    void stop();

    /**
     * Method that should be refined to conduct actual behavior.
     */
    void doTick();
}
