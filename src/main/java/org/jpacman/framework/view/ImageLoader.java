package org.jpacman.framework.view;

import java.awt.Image;
import java.io.IOException;
import java.net.URL;

import javax.swing.ImageIcon;

import org.jpacman.framework.factory.FactoryException;
import org.jpacman.framework.model.Direction;

/**
 * The responsibilities of this class include obtaining images from file,
 * that can be used for animations of the player and the monsters in Pacman.
 *
 * @author Arie van Deursen, Delft University of Technology, May 2007
 *
 */

public class ImageLoader {

    /**
     * Animation sequence of images for monsters.
     */
    private Image[] monsterImage;

    /**
     * Animation sequence of images for the player.
     */
    private Image[][] playerImage;  
    
    /**
     * Width of the images.
     */
    private int width = -1;
    
    /**
     * Height of the images.
     */
    private int height = -1;
    
    /**
     * Create an empty (non intialized) image factory.
     */
    public ImageLoader() { /* Nothing needs to be done */ }
    
    /**
     * Create an empty (non initialized) image factory
     * requiring that all images are of the given (width, height).
     * @param w requested image width
     * @param h requested image height
     */
    public ImageLoader(int w, int h) { 
        width = w;
        height = h;
    }
     
    
    /**
     * Read images for player and monsters from file.
     * Different images exist for different phases of the animation.
     * @throws FactoryException if the images can't be found.
     */
    public void loadImages() throws FactoryException {
    	try {
    		monsterImage = new Image[]{
    				getImage("Ghost1.gif"),
    				getImage("Ghost2.gif") };

    		String[] sequence = new String[]{"2", "3", "4", "3", "2"};
    		playerImage = new Image[Direction.values().length][sequence.length + 1];
    		for (Direction d : Direction.values()) {
    			int dir = d.ordinal();
    			playerImage[dir][0] = getImage("PacMan1.gif");
    			for (int seq = 0; seq < sequence.length; seq++) {
    				String name = "PacMan" + sequence[seq] + d.toString().toLowerCase() + ".gif";
    				playerImage[dir][seq + 1] = getImage(name);
    			}
    		}
    	} catch (IOException io) {
    		throw new FactoryException("Can't load images", io);
    	}
    }

    /**
     * @return Number of different monster animation steps
     */
    public int monsterAnimationCount() {
        assert monsterImage != null : "Monster image should not be null.";
        int result = monsterImage.length;
        assert result >= 0;
        return result;
    }

    /**
     * @return Number of different player animation steps
     */
    public int playerAnimationCount() {
        assert playerImage != null;
        assert playerImage[0] != null;
        return playerImage[0].length;
    }


    /**
     * Get a player (pizza slice) in the appropriate direction at the
     * given animation sequence.
     * 
     * @param dir Direction pacman is moving to.
     * @param anim Animation step
     * @return Player image in appropriate direction.
     */
    public Image player(Direction dir, int anim) {
        assert anim >= 0;
        Image img = null;
        int dirIndex = dir.ordinal();
        img = playerImage[dirIndex][anim % playerAnimationCount()];
        assert img != null;
        return img;
    }

    /**
     * Obtain a picture of a monster.
     * @param animationIndex counter indicating which animation to use.
     * @return The monster image at the given animation index.
     */
    public Image monster(int animationIndex) {
        assert animationIndex >= 0;
        return monsterImage[animationIndex % monsterAnimationCount()];
    }

    /**
     * Obtain an image from a file / resource that can
     * be found on the classpath.
     * @param name The file containg, e.g., a .gif picture.
     * @return The corresponding Image.
     * @throws IOException If file can't be found.
     */
    private Image getImage(String name) throws IOException {
        assert name != null;
        
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        URL picfile = cl.getResource(name);
        if (picfile == null) {
            throw new IOException("Can't load image: "  + name);
        }
        Image result = new ImageIcon(picfile).getImage();
        assert result != null;
 
        return resize(result);
    }
    
     
    /**
     * Resize a given image to the required dimension.
     * @param im The image
     * @return The resized image.
     */
    Image resize(Image im) {
        assert im != null;
        Image result = im;
        if (width > 0 && height > 0) {
            int w = im.getWidth(null);        
            int h = im.getHeight(null);
            if (w != width || h != height) {
                result = im.getScaledInstance(width, height, java.awt.Image.SCALE_DEFAULT);
            }
        }
        assert result != null;
        return result;
    }
}
