package org.jpacman.framework.model;

import org.jpacman.framework.model.IBoardInspector.SpriteType;

/**
 * A ghost element on the board.
 * 
 * @author Arie van Deursen, TU Delft, Feb 10, 2012
 */
public class Ghost extends Sprite {

	/**
	 * @return That this sprite is a ghost.
	 */
	@Override
	public SpriteType getSpriteType() {
		return SpriteType.GHOST;
	}

}
