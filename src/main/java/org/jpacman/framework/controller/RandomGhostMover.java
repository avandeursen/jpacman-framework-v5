package org.jpacman.framework.controller;

import org.jpacman.framework.model.Ghost;
import org.jpacman.framework.model.IGameInteractor;
import org.jpacman.framework.model.Direction;


/**
 * Example, simple ghost mover that just moves ghosts randomly.
 *
 * @author Arie van Deursen; Aug 18, 2003
 */
public class RandomGhostMover extends AbstractGhostMover {

    /**
     * Start a new mover with the given engine.
     *
     * @param theEngine Engine used.
     */
    public RandomGhostMover(final IGameInteractor theEngine) {
        super(theEngine);
    }

    /**
     * Actually conduct a random move in the underlying engine.
     */
    public void doTick() {
        synchronized (gameInteraction()) {
            Ghost theGhost = getRandomGhost();
            if (theGhost == null) {
                return;
            }
            int dirIndex = getRandomizer().nextInt(Direction.values().length);
            final Direction dir = Direction.values()[dirIndex];
            gameInteraction().moveGhost(theGhost, dir);
        }
    }
}
