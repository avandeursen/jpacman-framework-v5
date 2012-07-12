package org.jpacman.framework.model;

import org.jpacman.framework.model.IBoardInspector.SpriteType;

/**
 * A Wall element.
 * 
 * @author Arie van Deursen, TU Delft, Feb 10, 2012
 */
public class Wall extends Sprite {

	/**
	 * @return That this sprite is a wall.
	 */
	@Override
	public SpriteType getSpriteType() {
		return SpriteType.WALL;
	}

}
