package org.jpacman.framework.factory;

/**
 * Representation of things that can go wrong when loading
 * games / maps from file.
 * 
 * @author Arie van Deursen, January 2009.
 */

public class FactoryException extends Exception {

    /**
     * ID possibly used for serialization.
     */
    private static final long serialVersionUID = -7677883417366623357L;

    /**
     * A new game loading exception.
     * @param message the given explanation.
     */
    public FactoryException(String message) {
        super(message);
    }

    /**
     * A new game loading exception.
     * @param message Explanation of context
     * @param cause thrown earlier and propagated here.
     */
    public FactoryException(String message, Throwable cause) {
        super(message, cause);
    }
}
