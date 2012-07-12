package org.jpacman.framework.ui;

import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.jpacman.framework.model.IPointInspector;

/**
 * A panel for displaying the points earned so far in the game.
 * 
 * @author Arie van Deursen, TU Delft, Jan 23, 2012
 */
public class PointsPanel extends JPanel implements Observer {

	private static final long serialVersionUID = -6773251381947430351L;

	private static final int PANEL_HEIGHT = 45;
	private static final int PANEL_WIDTH = 100;
	
	private IPointInspector pointInspector;
	
	private JTextField eatenField;
	
	/**
	 * Initialize the UI fields displaying the points.
	 * 
	 * @param points Inspector for points as maintained by model.
	 */
	public void initialize(IPointInspector points) {
		assert points != null;
		
		pointInspector = points;
		
        JLabel pointsLabel = new JLabel("Points: ");
        final int eatenWidth = 7;
        eatenField = new JTextField("0", eatenWidth);
        eatenField.setEditable(false);
        eatenField.setHorizontalAlignment(SwingConstants.RIGHT);
        eatenField.setName("jpacman.points");

        add(pointsLabel);
        add(eatenField);

        setName("jpacman.points.panel");
        setSize(PANEL_WIDTH, PANEL_HEIGHT);
        displayPoints();
	}

	@Override
	public void update(Observable o, Object arg) {
		displayPoints();
	}
	
	private void displayPoints() {
		assert pointInspector != null;
		String points = 
				pointInspector.getFoodEaten() 
				+ " / " 
				+ pointInspector.totalFoodInGame();
		eatenField.setText(points);
	}
}
