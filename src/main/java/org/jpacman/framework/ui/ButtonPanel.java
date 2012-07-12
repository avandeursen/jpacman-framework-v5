package org.jpacman.framework.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jpacman.framework.ui.PacmanInteraction.MatchState;

/**
 * A panel containing the buttons for controlling
 * JPacman.
 * 
 * @author Arie van Deursen, TU Delft, Jan 21, 2012
 */
public class ButtonPanel extends JPanel implements Observer {
	
	private static final long serialVersionUID = 5078677478811886963L;

	private static final int BUTTON_WIDTH = 80;
	private static final int BUTTON_HEIGHT = 45;
	private int buttonCount = 0;
	
	private PacmanInteraction pacmanInteractor;
	
	private JFrame parent;
	
	/**
	 * Set the listener capable of exercising the
	 * requested events.
	 * @param pi The new pacman interactor
	 * @return Itself for fluency.
	 */
	public ButtonPanel withInteractor(PacmanInteraction pi) {
		pacmanInteractor = pi;
		pi.addObserver(this);
		return this;
	}
	
	/**
	 * Obtain the handler capable of dealing with
	 * button events.
	 * @return The pacman interactor.
	 */
	public IPacmanInteraction getPacmanInteractor() {
		return pacmanInteractor;
	}
	
    /**
     * @return True iff precisely one of the start/stop buttons is enabled.
     */
    protected boolean invariant() {
    	return 
    		startButton.isEnabled() ^ stopButton.isEnabled();
    }
	
	private JButton startButton;
	private JButton stopButton;
	
    /**
     * Actually create the buttons.
     */
    public void initialize() {    	
    	startButton = new JButton("Start");
    	stopButton = new JButton("Stop");
    	initializeStartButton();
    	initializeStopButton();
    	
    	JButton exitButton = createExitButton();
    	    	
        setName("jpacman.buttonPanel");
        addButton(startButton);
        addButton(stopButton);
        addButton(exitButton);       
     }
    
    /**
     * Add a button to the panel, resetting the
     * width of the panel accordingly.
     * @param button The button to be added.
     */
    public void addButton(JButton button) {
    	assert button != null;
    	add(button);
    	buttonCount++;
    	setPanelSize();
    }

    /**
     * Set the size of the panel depending
     * on the number of buttons.
     */
	protected void setPanelSize() {
        setSize(BUTTON_WIDTH * buttonCount, BUTTON_HEIGHT);
	}
    
    /**
     * Create the start button.
     */
    protected void initializeStartButton() {
    	startButton.addActionListener(new ActionListener() {
    		@Override
			public void actionPerformed(ActionEvent e) {
    			start();
    		}
    	});
    	startButton.setName("jpacman.start");
    	startButton.requestFocusInWindow();
     }
    
    /**
     * Create the stop button.
     */
    protected void initializeStopButton() {
     	stopButton.setEnabled(false);
    	stopButton.addActionListener(new ActionListener() {
    		@Override
			public void actionPerformed(ActionEvent e) {
    			pause();
    		}
    	});
    	stopButton.setName("jpacman.stop");
    }
    
    /**
     * @return A new button to exit the game.
     */
    protected JButton createExitButton() {
    	JButton exitButton = new JButton("Exit");
    	exitButton.addActionListener(new ActionListener() {
    		@Override
			public void actionPerformed(ActionEvent e) {
    			getPacmanInteractor().exit();
    		}
    	});
    	return exitButton;
    }
    
    public void pause() {
		assert invariant();
		getPacmanInteractor().stop();
		// ensure the full window has the focus.
		enableStartStop();
		parent.requestFocusInWindow();
		assert invariant();   	
    }
    
    public void start() {
		assert pacmanInteractor != null : "PRE: Listeners initialized.";
		assert invariant();
		getPacmanInteractor().start();
		// ensure the full window has the focus.
		enableStartStop();
		parent.requestFocusInWindow();
		assert invariant();
    }
    
    /**
     * Provide the parent window.
     * @param parent The containing parent window
     * @return Itself for fluency.
     */
    public ButtonPanel withParent(JFrame parent) {
    	this.parent = parent;
    	return this;
    }

	@Override
	public void update(Observable o, Object arg) {
		enableStartStop();
	}
		
	private void enableStartStop() {
		MatchState s = pacmanInteractor.getCurrentState();
		if (s == MatchState.PAUSING){ 
			stopButton.setEnabled(false);
			startButton.setEnabled(true);
		} else if (s == MatchState.PLAYING){
			stopButton.setEnabled(true);
			startButton.setEnabled(false);
		} else {
			// game over
			stopButton.setEnabled(false);
			startButton.setEnabled(false);			
		}
	}
}
