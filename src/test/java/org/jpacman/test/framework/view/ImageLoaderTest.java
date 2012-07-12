package org.jpacman.test.framework.view;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import java.awt.Image;

import org.jpacman.framework.factory.FactoryException;
import org.jpacman.framework.model.Direction;
import org.jpacman.framework.view.ImageLoader;

import org.junit.Before;
import org.junit.Test;

/**
 * Fairly basic test cases for the image factory.
 * @author Arie van Deursen, TU Delft, created 2007.
 */
public class ImageLoaderTest {

    /**
     * The factory under test.
     */
    private ImageLoader imf;

    /**
     * Actually create the image factory.
     * @throws FactoryException if images can't be found.
     */
    @Before public void setUp() throws FactoryException {
        imf = new ImageLoader();
        imf.loadImages();
    }

    /**
     * Are images for player properly loaded?
     */
    @Test public void testPlayer() {
        Image up = imf.player(Direction.UP, 1);
        Image down = imf.player(Direction.DOWN, 1);
        assertNotSame(up, down);
    }

    /**
     * Are monster images properly loaded?
     */
    @Test public void testMonster() {
        Image m1 = imf.monster(0);
        Image m2 = imf.monster(0);
        assertEquals(m1, m2);
    }
    
}