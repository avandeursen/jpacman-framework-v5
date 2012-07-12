package org.jpacman.framework.controller;

/**
 * Interface for a controller which can be started
 * and stopped, and which generates ticks.
 * <p>
 *
 * @author Arie van Deursen, 3 September, 2003
 * @version $Id: IMonsterController.java 3394 2010-02-21 16:09:39Z arievandeursen $
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
