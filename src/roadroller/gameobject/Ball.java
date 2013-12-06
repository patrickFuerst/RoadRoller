/**
 * 
 */
package roadroller.gameobject;

import java.io.IOException;

import roadroller.helper.Constants;
import roadroller.ressourcemanager.RessourceManager;

/**
 * A red Ball jumping across the street
 * @author falichs
 *
 */
public class Ball extends SpriteObject implements Enemy {

	/**
	 * sprite specific constants
	 */
	public static final String imgFile = Constants.imgDir
			+ "ball_30x30x10.png";
	public static final int frameWidth = 30;
	public static final int frameHeight = 30;
	public static final int[] frameSequence = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0 };
	private static final int xReference = 15;
	private static final int yReference = 15;
	
	private int dx = 4;
	private boolean jumpRight = true;

	/**
	 * Constructs the Ball object.
	 * 
	 * @param xPos the x-position
	 * @param yPos the y-position
	 * @throws IOException thrown if image file for sprite creation is not found
	 */
	public Ball(int xPos,
			int yPos) throws IOException {
		super(RessourceManager.getInstance().getBallImage(), frameWidth, frameHeight, frameSequence, xReference,
				yReference, xPos, yPos);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see roadroller.gameobject.GameObject#update(int)
	 */
	public void update(int playerYSpeed) {
		// let ball jump across the street
		if (jumpRight) {
			// check if there's space left
			if (getX() + getWidth() < 240 - dx) {
				
				move(dx, 0);
				nextFrame();
			} else {
				
				move(-dx, 0);
				this.jumpRight = false;
				nextFrame();
			}
		} else {
			// check if there's space left
			if (getX()-20 > dx) {
				// walk left
				move(-dx, 0);
				nextFrame();
			} else {
				// start walking right
				move(dx, 0);
				this.jumpRight = true;
				nextFrame();
			}
		}

	}

	/* (non-Javadoc)
	 * @see roadroller.gameobject.Enemy#cashScore()
	 */
	public int cashScore() {
		return Constants.BALL_POINTS_PER_HIT;
	}

}
