package org.jpacman.framework.factory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.jpacman.framework.model.Board;
import org.jpacman.framework.model.Game;
import org.jpacman.framework.model.Sprite;

/**
 * Turn a textual ASCII board representation into an
 * actual board.
 *
 * @author Arie van Deursen, TU Delft, Jan 20, 2012
 */
public class MapParser {

	/**
	 * Characters used to represent various sprites.
	 */
	public static final char PLAYER = 'P';
	public static final char GHOST = 'G';
	public static final char FOOD = '.';
	public static final char WALL = '#';
	public static final char EMPTY = ' ';

	/**
	 * The factory used to actually instantiate objects.
	 */
	private final IGameFactory factory;

	/**
	 * The board that we are constructing.
	 */
	private Board theBoard;

	/**
	 * A fresh parser.
	 * @param f the factory used to instantiate objects.
	 */
	public MapParser(IGameFactory f) {
		factory = f;
	}
		
	/**
	 * Parse a textual representation of a board,
	 * and create the relevant corresponding objects.
	 * 
	 * @param map Rectangular list of strings, one for each row.
	 * @return A game representing the full board.
	 * @throws FactoryException if input was in wrong format.
	 */
	public Game parseMap(String[] map) throws FactoryException {
        assert map != null;
        
        int height = map.length;
        if (height == 0) {
        	throw new FactoryException("Empty map encountered.");
        }

        int width = map[0].length();
        if (width == 0) {
        	throw new FactoryException("Empty row encountered.");
        }
        
        Game theGame = factory.makeGame();
        theBoard = factory.makeBoard(width, height);
        
        for (int y = 0; y < height; y++) {
            if (map[y].length() != width) {
            	throw new FactoryException(
            			"Row " + y + " has incorrect length.");
            }
            for (int x = 0; x < width; x++) {
             	addSprite(map[y].charAt(x), x, y);
            }
        }

        return theGame;
	}
	
	/**
	 * @param spriteCode The sort of sprite to be added
	 * @param x x-axis
	 * @param y y-axis
	 * @throws FactoryException if the code is illegal.
	 */
	protected void addSprite(char spriteCode, int x, int y) throws FactoryException {
		assert theBoard != null : "Empty board: (" + x + ", " + y + ")->" + spriteCode;
		Sprite theSprite = getSprite(spriteCode);
		if (theSprite != null) {
			theBoard.put(theSprite, x, y);
		}
	}

	/**
	 * Create a new sprite.
	 * @param spriteCode the char representing a sprite type
	 * @return a new sprite object.
	 * @throws FactoryException if the char can't be handled.
	 */
	protected Sprite getSprite(char spriteCode) throws FactoryException {
		Sprite theSprite = null;
		switch (spriteCode) {
		case PLAYER:
			theSprite = factory.makePlayer();
			break;
		case GHOST:
			theSprite = factory.makeGhost();
			break;
		case WALL:
			theSprite = factory.makeWall();
			break;
		case FOOD:
			theSprite = factory.makeFood();
			break;
		case EMPTY:
			// nothing.
			break;
		default:
			invalidSprite(spriteCode);
		}
		return theSprite;
	}

	/**
	 * Handle an illegal character.
	 * @param spriteCode char representing a sprite
	 * @throws FactoryException to tell that the char can't be handled.
	 */
	protected void invalidSprite(char spriteCode) throws FactoryException {
		throw new FactoryException("Illegal sprite code " + spriteCode);
	}

    /**
     * Provide a stream for a file that lives on the class path.
     * @param fileName The name of the resource to be loaded
     * @return an input stream for the file, never null.
     * @throws FactoryException If the file is not found.
     */
    private static InputStream getResourceStream(String fileName) throws FactoryException {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        InputStream result = cl.getResourceAsStream(fileName);
        if (result == null) {
            throw new FactoryException("Resource: " + fileName + " not found on classpath:" 
                    + System.getProperty("java.class.path"));
        }
        return result;
    }
    
    /**
     * Parse a map contained in a file.
     * @param fileName File containing the map
     * @return A game representing the board on the map
     * @throws FactoryException If file/contents are not ok.
     */
    public Game parseFromFile(String fileName) throws FactoryException {
    	return parseMap(getMap(fileName));
    }
    
    /**
     * Return the map contained in the file.
     * @param fileName Resource on class path containing the map
     * @return The map as a series of strings.
     * @throws FactoryException If reading the map file fails.
     */
    private String[] getMap(String fileName) throws FactoryException {
        assert fileName != null;
        BufferedReader br = 
                new BufferedReader(new InputStreamReader(getResourceStream(fileName)));
        return getMap(br);
    }
    
    /**
     * Return the map provided by the reader.
     * @param br Reader to access lines representing a map
     * @return The map as a series of strings.
     * @throws FactoryException If reading the map file fails.
     */
    public String[] getMap(BufferedReader br) throws FactoryException {
        assert br != null;
        List<String> mapList = new ArrayList<String>();
        String[] mapString = null;

        try {
             while (br.ready()) {
                mapList.add(br.readLine());
            }
            mapString = new String[mapList.size()];
            mapList.toArray(mapString);
            br.close();
        } catch (IOException e) {
            throw new FactoryException("Problem reading file ", e);
        }
        return mapString;
    }
}
