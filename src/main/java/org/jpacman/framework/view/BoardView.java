package org.jpacman.framework.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import org.jpacman.framework.factory.FactoryException;
import org.jpacman.framework.model.IBoardInspector;
import org.jpacman.framework.model.IBoardInspector.SpriteType;
import org.jpacman.framework.model.Player;
import org.jpacman.framework.model.Sprite;

/**
 * Draw a full board.
 * 
 * @author Arie van Deursen, TU Delft, Jan 27, 2012
 */
public class BoardView extends JPanel {

	/**
	 * Required for UI components.
	 */
	private static final long serialVersionUID = 6056336242748415878L;

    /**
     * Width of an individual cell, in pixels.
     */
    private static final int CELL_WIDTH = 20;

    /**
     * Height of an individual cell, in pixels.
     */
    private static final int CELL_HEIGHT = 20;

    /**
     * The horizontal gap between cells, in pixels.
     */
    public static final int CELL_HGAP = 1;

    /**
     * The vertical gap between cells, in pixels.
     */
    public static final int CELL_VGAP = 1;

    /**
     * Representation of the actual board.
     */
    private final IBoardInspector boardInspector;
    
    /**
     * The area to draw the cells on.
     */
    private BufferedImage drawArea;
    
    /**
     * The manager keeping track of images.
     */
    private final ImageLoader imageLoader;
    
    /**
     * Indicator for animation.
     */
    private int animationCount;
    
    /**
     * @return The board width measured in cells, >= 0.
     */
    private int worldWidth() {
        return boardInspector.getWidth();
    }

    /**
     * @return The board height measured in cells, >= 0.
     */
    private int worldHeight() {
        return boardInspector.getHeight();
    }
    
    /**
     * Create a new view for the board, given
     * an inspector of the actual board content.
     * @param board Model of the board.
     * @throws FactoryException 
     */
    public BoardView(IBoardInspector board) throws FactoryException {
    	boardInspector = board;
    	imageLoader = new ImageLoader(CELL_WIDTH, CELL_HEIGHT);
        setSize(windowWidth(), windowHeight());
        imageLoader.loadImages();
    }
    /**
     * The width of the board viewer in pixels.
     *
     * @return The width of the board viewer.
     */
    public final int windowWidth() {
        return (CELL_WIDTH + CELL_HGAP) * (worldWidth() + 1);
    }

    /**
     * The height of the board viewer in pixels.
     *
     * @return The height of the board viewer.
     */
    public final int windowHeight() {
        return (CELL_HEIGHT + CELL_VGAP) * (worldHeight() + 1);
    }
    
    /**
     * Create a drawing area on which the board can be drawn.
     * @return The 2D graphics area of the board.
     */
    private Graphics2D createDrawArea() {
    	if (drawArea == null) {
    		drawArea = 
    			(BufferedImage) createImage(windowWidth(), windowHeight());
    	}
    	Graphics2D g2 = drawArea.createGraphics();
        g2.setBackground(getBackground());
        g2.clearRect(0, 0, windowWidth(), windowHeight());
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        return g2;
    }
    
    /**
     * JComponent method invoked when the board needs to be drawn.
     * @param g The graphics to paint the board on.
     */
    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = createDrawArea();
        drawCells(g2);
        g2.dispose();
        g.drawImage(drawArea, 0, 0, this);
    }
    
    /**
     * Draw all cells on the board.
     * @param g2 The graphics to draw the cells on.
     */
    private void drawCells(Graphics2D g2) {
        final float strokeWidth = 5.0f;
        g2.setStroke(new BasicStroke(strokeWidth));

        for (int x = 0; x < worldWidth(); x++) {
            for (int y = 0; y < worldHeight(); y++) {
                drawCell(g2, x, y);
            }
        }
    }

	private void drawCell(Graphics2D g2, int x, int y) {
        Color fillColor = spriteColor(x, y);

        int startx = 2 * CELL_HGAP + (CELL_WIDTH + CELL_HGAP) * x;
        int starty = 2 * CELL_VGAP + (CELL_HEIGHT + CELL_VGAP) * y;
 
        Rectangle fullCell = fullArea(startx, starty);
        g2.setColor(Color.blue);
        g2.draw(fullCell);
        g2.setColor(fillColor);
        g2.fill(fullCell);

        if (boardInspector.spriteTypeAt(x, y) == SpriteType.FOOD) {
        	Rectangle centeredCell = centeredArea(startx, starty, 2);
        	g2.setColor(Color.black);
            g2.fill(fullCell);
            g2.setColor(Color.orange);  
            g2.fill(centeredCell);
        }
        
        Image img = spriteImage(boardInspector.spriteAt(x, y));
        if (img != null) {
        	g2.drawImage(img, startx, starty, this);
        }
 	}
	
	private Rectangle fullArea(int startx, int starty) {
		Point loc = new Point(startx, starty);
		Dimension dim = new Dimension(CELL_WIDTH, CELL_HEIGHT);
		return new Rectangle(loc, dim);
	}
	
	private Rectangle centeredArea(int startx, int starty, int radius) {
		assert radius <= CELL_WIDTH / 2;
		Point loc = new Point(
				startx + CELL_WIDTH / 2 - radius, 
				starty + CELL_HEIGHT / 2 - radius);
		Dimension dim = new Dimension(2 * radius + 1, 2 * radius + 1);
		return new Rectangle(loc, dim);
	}
	
	
	private Color spriteColor(int x, int y) {
		SpriteType st = boardInspector.spriteTypeAt(x, y);
		Color c = Color.yellow;
		switch (st) {
		case GHOST:
			c = Color.blue;
			break;
		case FOOD:
			c = Color.orange;
			break;
		case WALL:
			c = Color.green;
			break;
		case PLAYER:
			c = Color.yellow;
			break;
		case OTHER:
			c = Color.black;
			break;
		case EMPTY:
			c = Color.gray;
			break;
		default:
			assert false : "No other Sprite Types " + st;
		}
		return c;
	}
	
	/**
	 * @param sprite
	 * @return An image for this sprite.
	 */
    private Image spriteImage(Sprite sprite) {
        Image img = null;
        if (imageLoader != null && sprite != null) {
            if (sprite instanceof Player) {
                img = imageLoader.player(
                		((Player) sprite).getDirection(),
                        animationCount);
            }
            if (sprite.getSpriteType() == SpriteType.GHOST) { 
                 img = imageLoader.monster(animationCount);
            }
        }
        return img;
    }
	
    /**
     * Increment the animation counter, and redisplay,
     * so that the next animation becomes visible.
     */
    public void nextAnimation() {
        if (imageLoader != null) {
            animationCount = (animationCount + 1)
            % (imageLoader.monsterAnimationCount()
                    * imageLoader.playerAnimationCount());
            repaint();
        }
    }
    
}
