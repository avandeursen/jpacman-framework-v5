package org.jpacman.framework.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.jpacman.framework.controller.IController;
import org.jpacman.framework.model.Direction;
import org.jpacman.framework.model.IGameInteractor;

/**
 * Class mapping events triggered by the UI to 
 * actions in the underlying Pacman model.
 * 
 * @author Arie van Deursen, TU Delft, Jan 30, 2012
 */
public class PacmanInteraction extends Observable implements IPacmanInteraction, Observer {
	
	/**
	 * The states a match can be in.
	 */
	public enum MatchState {
		PLAYING("Playing"),
		PAUSING("Halted"),
		WON("You have won :-)"),
		LOST("You have lost :-(");
		
		private String theMessage;
		
		/**
		 * Create one of the states.
		 * @param m The message for this state.
		 */
		MatchState(String m) {
			theMessage = m;
		}
		
		/**
		 * @return The message that belongs to the current state.
		 */
		String message() {
			return theMessage;
		}
	}
	
	/**
	 * The state of the ongoing match.
	 * Initially, we're just waiting.
	 */
	private MatchState currentState = MatchState.PAUSING;
	
	/**
	 * Window to be deleted upon game exit.
	 */
	private IDisposable disposableWindow;
	
	/**
	 * Model of the game which can execute basic commands.
	 */
	private IGameInteractor gameInteractor;
	
	/**
	 * Various controllers that may have to stopped or restarted.
	 */
	private final List<IController> controllers =
			new ArrayList<IController>();
	

	/**
	 * Verify consistency with the state of the game.
	 */
	private boolean invariant() {
		return
			currentState == MatchState.WON 
				&& gameInteractor.won()
			||
			currentState == MatchState.LOST 
				&& gameInteractor.died()
			||
			(currentState == MatchState.PLAYING || currentState == MatchState.PAUSING)
				&& !(gameInteractor.died() || gameInteractor.won());
	}
	
	@Override
	public void start() {
		assert invariant();
		if (currentState == MatchState.PAUSING) { 
			startControllers();
		}
		updateState(MatchState.PLAYING);
		assert invariant();
	}

	@Override
	public void stop() {
		assert invariant() : "Unexpected state in " + currentState;
		if (currentState == MatchState.PLAYING) {
			stopControllers();
			updateState(MatchState.PAUSING);
		}
		assert invariant();
	}

	@Override
	public void exit() {
		assert invariant();
		stopControllers();
		disposableWindow.dispose();
	}

	@Override
	public void up() {
		movePlayer(Direction.UP);
	}

	@Override
	public void down() {
		movePlayer(Direction.DOWN);
	}

	@Override
	public void left() {
		movePlayer(Direction.LEFT);
	}

	@Override
	public void right() {
		movePlayer(Direction.RIGHT);
	}
	
	/**
	 * Move the player in the given direction,
	 * provided we are in the playing state.
	 * @param dir New direction.
	 */
	private void movePlayer(Direction dir) {
		assert invariant() : currentState;
		if (currentState == MatchState.PLAYING) {
			gameInteractor.movePlayer(dir);
			updateState();
		}
		// else: ignore move event.
		assert invariant();
	}
	
	/**
	 * Add an external controller, which should be stopped/started
	 * via the ui.
	 * @param controller The controller to be added.
	 * @return Itself, for fluency.
	 */
	public PacmanInteraction controlling(IController controller) {
		controllers.add(controller);
		return this;
	}
	
	/**
	 * @return The current state of the game.
	 */
	public MatchState getCurrentState() {
		return currentState;
	}	
	
	/**
	 * Provide the main window that has to be disposed off during exit.
	 * @param win Main window
	 * @return Itself, for fluency.
	 */
	public PacmanInteraction withDisposable(IDisposable win) {
		disposableWindow = win;
		return this;
	}
	
	/**
	 * Provide the interactor towards the model of the game.
	 * @param igame Interactor
	 * @return itself for fluency.
	 */
	public PacmanInteraction withGameInteractor(IGameInteractor igame) {
		gameInteractor = igame;
		return this;
	}
	
	private void stopControllers() {
		for (IController c : controllers) {
			c.stop();
		}		
	}
	
	private void startControllers() {
		for (IController c : controllers) {
			c.start();
		}		
	}
	
	/**
	 * @return The handle to interact with the underlying game.
	 */
	protected IGameInteractor getGame() {
		return gameInteractor;
	}

	@Override
	public void update(Observable o, Object arg) {
		updateState();
	}
	
	/**
	 * The state of the external game may have changed.
	 * Verify whether the game was lost/won,
	 * and if so update the state accordingly.
	 */
	public void updateState() {
		// invariant may have been invalidated by outside world.
		if (currentState == MatchState.PLAYING && gameInteractor.died()) {
			updateState(MatchState.LOST);
			stopControllers();
		} else if (currentState == MatchState.PLAYING && gameInteractor.won()) {
			updateState(MatchState.WON);
			stopControllers();
		} else if (currentState == MatchState.WON && !gameInteractor.won()
				|| currentState == MatchState.LOST && !gameInteractor.died()) {
			updateState(MatchState.PAUSING);
			stopControllers();
		}
		assert invariant();
	}
	
	private void updateState(MatchState nextState) {
		currentState = nextState;
		setChanged();
		notifyObservers();
	}
}
