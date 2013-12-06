/**
 * 
 */
package roadroller.gameobject;

import javax.microedition.lcdui.game.Sprite;

/**
 * GameObject represents objects in the game
 * 
 * @author Felix
 * 
 */
public interface GameObject {

	/**
	 * updates the objects status and maybe its animation
	 * @param playerYSpeed speed of player in y-direction
	 */
	public abstract void update(int playerYSpeed);

	/**
	 * Does exactly what it says. you may want to use the sprite for the layermanager.
	 * @return the GameObjects Sprite Object
	 */
	public abstract Sprite getSprite();
}
