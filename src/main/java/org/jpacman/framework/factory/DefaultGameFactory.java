package org.jpacman.framework.factory;

import org.jpacman.framework.model.Board;
import org.jpacman.framework.model.Food;
import org.jpacman.framework.model.Game;
import org.jpacman.framework.model.Ghost;
import org.jpacman.framework.model.Player;
import org.jpacman.framework.model.Wall;

/**
 * A factory for the classes related to the 
 * game, the board, and its content.
 * 
 * @author Arie van Deursen, TU Delft, Jan 22, 2012
 */
public class DefaultGameFactory implements IGameFactory {

	private transient Game theGame;
	
	@Override
	public Game makeGame() {
		theGame = new Game();
		return theGame;
	}
	
	@Override
	public Player makePlayer() {
		assert getGame() != null;
		Player p = new Player();
		getGame().addPlayer(p);
		return p;
	}

	@Override
	public Ghost makeGhost() {
		assert getGame() != null;
		Ghost g = new Ghost();
		getGame().addGhost(g);
		return g;
	}

	@Override
	public Food makeFood() {
		assert getGame() != null;
		Food f = new Food();
		getGame().addFood(f);
		return f;
	}

	@Override
	public Wall makeWall() {
		return new Wall();
	}

	@Override
	public Board makeBoard(int w, int h) {
		assert getGame() != null;
		Board b = new Board(w, h);
		getGame().setBoard(b);
		return b;
	}
		
	/**
	 * @return The game created by this factory.
	 */
	protected Game getGame() {
		assert theGame != null;
		return theGame;
	}
}
