package org.jpacman.framework.model;

import org.jpacman.framework.factory.DefaultGameFactory;
import org.jpacman.framework.factory.FactoryException;
import org.jpacman.framework.factory.IGameFactory;
import org.jpacman.framework.factory.MapParser;

/**
 * A level in JPacman, which is stored in a file.
 * 
 * @author Arie van Deursen, TU Delft, Feb 25, 2012
 */
public class Level {
	
	/**
     * Default board used.
     */
    static final String DEFAULT_BOARD = "board.txt";

	/**
	 * The factory used for creating the board.
	 */
    private IGameFactory gameFactory = new DefaultGameFactory();
    
    /**
     * Result of parsing the map file.
     */
    private Game parseResult = null;
    
    /**
     * The default map used if none is specified.
     */
	private String mapFile = DEFAULT_BOARD;
	
	/**
	 * Provide the name of the map file to be read.
	 * @param fileName Map file name.
	 */
	public void setMapFile(String fileName) {
		assert fileName != null;
		mapFile = fileName;
	}
	
	/**
	 * Provide the factory to create game objects.
	 * @param factory For making game objects.
	 */
	public void setFactory(IGameFactory factory) {
		assert factory != null;
		gameFactory = factory;
	}
	
	/**
	 * Actually parse the given map file.
	 * @return The resulting game.
	 * @throws FactoryException If file can't be read.
	 */
	public IGameInteractor parseMap() throws FactoryException {
		assert mapFile != null;
		assert gameFactory != null;
		MapParser parser = 
				new MapParser(gameFactory);
		parseResult = parser.parseFromFile(getMapFile());
		return parseResult;
	}

	/**
	 * @return The name of the file containing the map.
	 */
	public String getMapFile() {
		return mapFile;
	}
	
	/**
	 * @return The result from the last parse.
	 */
	public Game getGame() {
		return parseResult;
	}
}
