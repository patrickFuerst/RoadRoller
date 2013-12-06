/**
 * 
 */
package roadroller.gameobject;

import java.io.IOException;

import roadroller.helper.Constants;
import roadroller.ressourcemanager.RessourceManager;

/**
 * A Granny walking around on the street.
 * 
 * @author Felix
 * 
 */
public class Granny extends SpriteObject implements Enemy {

	/**
	 * sprite specific constants
	 */
	public static final String imgFile = Constants.imgDir
			+ "woman_30x30x27.png";
	public static final int frameWidth = 30;
	public static final int frameHeight = 30;
	public static final int[] frameSequence = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
			10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26 };

	private int dx = 1;
	private boolean walkRight = true;

	/**
	 * Constructs the Granny object.
	 * 
	 * @param xPos the x-position
	 * @param yPos the y-position
	 * @throws IOException thrown if image file for sprite creation is not found
	 */
	public Granny(int xPos, int yPos) throws IOException {
		super(RessourceManager.getInstance().getGrannyImage(), frameWidth,
				frameHeight, frameSequence, 15, 15, xPos, yPos);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see roadroller.gameobject.GameObject#update()
	 */
	public void update(int playerYSpeed) {
		// let granny walk around
		if (walkRight) {
			// check if there's space left
			if (getX() + getWidth() < 220 - dx) {
				// walk right
				move(dx, 0);
				nextFrame();
			} else {
				// start walking left
				move(-dx, 0);
				this.walkRight = false;
				setTransform(TRANS_MIRROR);
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
				this.walkRight = true;
				setTransform(TRANS_NONE);
				nextFrame();
			}
		}
	}

	/* (non-Javadoc)
	 * @see roadroller.gameobject.Enemy#cashScore()
	 */
	public int cashScore() {

		return Constants.GRANNY_POINTS_PER_HIT;
	}
}
