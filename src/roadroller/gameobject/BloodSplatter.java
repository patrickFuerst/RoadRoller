package roadroller.gameobject;

import java.io.IOException;

import roadroller.helper.Constants;
import roadroller.ressourcemanager.RessourceManager;

/**
 * A simple animated BloodSplatter effect.
 * 
 * @author Felix
 *
 */
public class BloodSplatter extends SpriteObject {

	/**
	 * sprite specific constants
	 */
	public static final String imgFile = Constants.imgDir + "blood_50x50x3.png";
	public static final int frameWidth = 50;
	public static final int frameHeight = 50;
	public static final int[] frameSequence = { 0, 1, 2, 1, 2, 1, 0 };
	private int framePos;

	/**
	 * Construct a Bloodsplatter object
	 * 
	 * @param xPos x-position
	 * @param yPos y-position
	 * @throws IOException thrown if image file cannot be read
	 */
	public BloodSplatter(int xPos, int yPos) throws IOException {
		super(RessourceManager.getInstance().getBloodSplatterImage(),
				frameWidth, frameHeight, frameSequence, 24, 24, xPos, yPos);
		framePos = 0;
	}

	/* (non-Javadoc)
	 * @see roadroller.gameobject.GameObject#update(int)
	 */
	public void update(int playerYSpeed) {
		if (framePos < frameSequence.length) {
			framePos++;
			this.nextFrame();
		}

	}
	

}
