  /**
 * 
 */
package roadroller.gameobject;

import java.io.IOException;

import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;

/**
 * A object using a Sprite
 * 
 * used to construct any visible gameobjects except the background.
 * 
 * @author Felix
 * 
 */
public abstract class SpriteObject extends Sprite implements GameObject {

//	public final int SPRITE_REFERENCE_TOP_LEFT = 1;
//	public final int SPRITE_REFERENCE_MIDDLE = 2;

	/**
	 * Creates a new Sprite-GameObject
	 * 
	 * @param spriteFile location of Image-file
	 * @param frameWidth the sprites framewitdth
	 * @param frameHeight the sprites frameheight
	 * @param frameSequence array containing the frame sequence
	 * @param xReference x- reference for sprite placement
	 * @param yReference y- reference for sprite placement
	 * @param xPos
	 *            the x position depending on reference (actual Sprite is placed
	 *            on canvas position [xPos - xReference, yPos - yReference])
	 * @param yPos
	 *            the y position depending on reference (actual Sprite is placed
	 *            on canvas position [xPos - xReference, yPos - yReference])
	 * @throws IOException thrown if file not readable
	 */
	public SpriteObject(String spriteFile, int frameWidth, int frameHeight,
			int[] frameSequence, int xReference, int yReference, int xPos,
			int yPos) throws IOException {
		super(Image.createImage(spriteFile), frameWidth, frameHeight);
		this.setFrameSequence(frameSequence);

		this.defineReferencePixel(xReference, yReference);
		this.setPosition(xPos - xReference, yPos - yReference);
	}

	/**
	 * Creates a new Sprite-GameObject
	 * 
	 * @param img Image Object
	 * @param frameWidth the sprites framewitdth
	 * @param frameHeight the sprites frameheight
	 * @param frameSequence array containing the frame sequence
	 * @param xReference x- reference for sprite placement
	 * @param yReference y- reference for sprite placement
	 * @param xPos
	 *            the x position depending on reference (actual Sprite is placed
	 *            on canvas position [xPos - xReference, yPos - yReference])
	 * @param yPos
	 *            the y position depending on reference (actual Sprite is placed
	 *            on canvas position [xPos - xReference, yPos - yReference])
	 * @throws IOException thrown if file not readable
	 */
	public SpriteObject(Image img, int frameWidth, int frameHeight,
			int[] frameSequence, int xReference, int yReference, int xPos,
			int yPos) throws IOException {
		super(img, frameWidth, frameHeight);
		this.setFrameSequence(frameSequence);

		this.defineReferencePixel(xReference, yReference);
		this.setPosition(xPos - xReference, yPos - yReference);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see roadroller.gameobject.GameObject#getLayer()
	 */
	public Sprite getSprite() {

		return this;
	}
	
	
}
