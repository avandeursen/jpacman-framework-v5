package org.jpacman.framework.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.jpacman.framework.model.IBoardInspector.SpriteType;

/**
 * Keep track of all activity that is going on during a game.
 * Provide access to key sprites in the game, such as the player
 * and the ghosts.
 * 
 * Provide functionality to make moves on the board, and keep
 * track of the points.
 * 
 * @author Arie van Deursen, TU Delft, Jan 28, 2012
 */
public class Game extends Observable 
	implements IGameInteractor {
	
	/**
	 * The underlying board.
	 */
	private Board theBoard;
	
	/**
	 * The object to keep track of the points.
	 */
	private final PointManager pointManager = new PointManager();
	
	/**
	 * The lead player.
	 */
	private Player thePlayer;
	
	/**
	 * All the ghosts in the game.
	 */
	private final List<Ghost> ghosts = new ArrayList<Ghost>();
	
	/**
	 * @param b The underlying board.
	 */
	public void setBoard(Board b) {
		assert b != null : "New board should not be null.";
		theBoard = b;
	}
	
	@Override
	public void movePlayer(Direction dir) {
		assert theBoard != null : "Board can't be null when moving";
		Tile target = theBoard.tileAtDirection(thePlayer.getTile(), dir);
		if (tileCanBeOccupied(target) && thePlayer.isAlive()) {
			Sprite currentContent = target.topSprite();
			eatFood(thePlayer, currentContent);
			dieIfGhost(thePlayer, currentContent);
			thePlayer.deoccupy();
			thePlayer.occupy(target);
			thePlayer.setDirection(dir);
			notifyViewers();
		}
	}
		
	/** 
	 * Player intends to move towards tile already occupied: 
	 * if there's food there, eat it.
	 * @param p The player
	 * @param currentSprite who is currently occupying the tile.
	 */
	private void eatFood(Player player, Sprite currentSprite) {
		if (currentSprite instanceof Food) {
			Food food = (Food) currentSprite;
			pointManager.consumePointsOnBoard(player, food.getPoints());
			food.deoccupy();
		}
	}
	
	/**
	 * Player intends to move towards an occupied tile:
	 * if there's a ghost there, the game is over.
	 * @param p The player
	 * @param currentSprite
	 */
	private void dieIfGhost(Player p, Sprite currentSprite) {
		if (currentSprite instanceof Ghost) {
			p.die();
		}
	}
	
	@Override
	public void moveGhost(Ghost theGhost, Direction dir) {
		Tile target = theBoard.tileAtDirection(theGhost.getTile(), dir);
		if (tileCanBeOccupied(target)) {
			Sprite currentContent = target.topSprite();
			if (currentContent instanceof Player) {
				((Player) currentContent).die();
			}
			theGhost.deoccupy();
			theGhost.occupy(target);
			notifyViewers();
		} 
	}

	/**
	 * Check if there's room on the target tile for another sprite.
	 * @param target Tile to be occupied by another sprite.
	 * @return true iff target tile can be occupied.
	 */
	private boolean tileCanBeOccupied(Tile target) {
		assert target != null : "PRE: Argument can't be null";
		Sprite currentOccupier = target.topSprite();
		return 
			currentOccupier == null 
			|| currentOccupier.getSpriteType() != SpriteType.WALL;
	}
	
	/**
	 * A player is added to the game.
	 * @param p The player to be added.
	 */
	public void addPlayer(Player p) {
		thePlayer = p;
	}
	
	/**
	 * Another ghost is added to the game.
	 * @param g The ghost to be added.
	 */
	public void addGhost(Ghost g) {
		ghosts.add(g);
	}
	
	/**
	 * Another piece of food is added to the game.
	 * @param f The food to be added.
	 */
	public void addFood(Food f) {
		pointManager.addPointsToBoard(f.getPoints());
	}
	
	/**
	 * @return The underlying board.
	 */
	public Board getBoard() {
		return theBoard;
	}
	
	/**
	 * @return The player.
	 */
	public Player getPlayer() {
		return thePlayer;
	}
	
	/**
	 * Attach an observer interested in state changes.
	 * @param o The observer listening to this game.
	 */
	@Override
	public void attach(Observer o) {
		assert o != null : "Can't add a null observer.";
		addObserver(o);
	}
	
    /**
     * Warn the observers that the state has changed.
     */
    protected void notifyViewers() {
        setChanged();
        notifyObservers();
    }

    /**
     * @return The helper class for keeping track of points.
     */
	public PointManager getPointManager() {
		return pointManager;
	}

	/**
	 * @return A copy of the list of the ghosts in the game.
	 */
	@Override
	public List<Ghost> getGhosts() {
        List<Ghost> result = new ArrayList<Ghost>();
        result.addAll(ghosts);
		return result;
	}

	@Override
	public IBoardInspector getBoardInspector() {
		return getBoard();
	}

	@Override
	public boolean died() {
		return !getPlayer().isAlive();
	}

	@Override
	public boolean won() {
		return pointManager.allEaten();
	}

}
