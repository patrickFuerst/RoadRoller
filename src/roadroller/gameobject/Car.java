package roadroller.gameobject;

import java.io.IOException;

import roadroller.helper.Constants;
import roadroller.ressourcemanager.RessourceManager;

/**
 * Class representing the Car object
 * @author Patrick
 * 
 */
public class Car extends SpriteObject implements Enemy {

	/**
	 * Sprite-specific constants:
	 */
	public static final String imgFile = Constants.imgDir
			+ "carsprite_34x56x3.png";
	public static final int frameWidth = 34;
	public static final int frameHeight = 56;
	public static final int[] frameSequence = { 0, 0, 0, 1, 1, 1, 2 };
	private int framePos;
	
	
	/**
	 * Constructs the car object.
	 * 
	 * @param xPos the x-position
	 * @param yPos the y-position
	 * @throws IOException thrown if image file for sprite creation is not found
	 */
	public Car(int xPos, int yPos) throws IOException {
		super(RessourceManager.getInstance().getCarImage(), frameWidth,
				frameHeight, frameSequence, 15, 15, xPos, yPos);
		framePos = 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see roadroller.gameobject.GameObject#update()
	 */
	public void update(int playerYSpeed) {

	}

	/* (non-Javadoc)
	 * @see roadroller.gameobject.Enemy#cashScore()
	 */
	public int cashScore() {
		return Constants.CAR_POINTS_PER_FRAME;
	}
	
	/* (non-Javadoc)
	 * @see javax.microedition.lcdui.game.Sprite#nextFrame()
	 */
	public void nextFrame(){
		if (framePos < frameSequence.length-1) {
			framePos++;
			super.nextFrame();
		}
	}

}
