/**
 * 
 */
package roadroller.gameobject.powerups;

import java.io.IOException;

import roadroller.gameobject.SpriteObject;
import roadroller.helper.Constants;
import roadroller.objectmanager.GameObjectManager;
import roadroller.ressourcemanager.RessourceManager;

/**
 * FuelUp Powerup
 * 
 * this class represents the FuelUp powerup.
 * 
 * its powers increase the fuel in the steamrollers gastank.
 * 
 * @author Felix
 *
 */
public class FuelUp extends SpriteObject implements IPowerUp {

	/**
	 * Sprite-Specific constants:
	 */
	public static final String imgFile = Constants.imgDir + "FuelUp_ 30x30.png";
	public static final int frameWidth = 30;
	public static final int frameHeight = 30;
	public static final int[] frameSequence = null;
	
	private boolean active;

	/**
	 * Constructs the powerup
	 * 
	 * @param xPos x-position where the sprite is to be placed
	 * @param yPos y-position where the sprite is to be placed
	 */
	public FuelUp(int xPos,
			int yPos) throws IOException {
		super(RessourceManager.getInstance().getFuelUpImage(), frameWidth, frameHeight, frameSequence, 17,
				17, xPos, yPos);
		this.active = false;
	}

	/* (non-Javadoc)
	 * @see roadroller.gameobject.GameObject#update(int)
	 */
	public void update(int playerYSpeed) {
		// nothing to do

	}

	/* (non-Javadoc)
	 * @see roadroller.gameobject.powerups.IPowerUp#applyPowers()
	 */
	public void applyPowers() {
		GameObjectManager.getInstance().getPlayer().fuelUp(Constants.POWERUP_FUELUP_litersPerApplication);
		this.active = true;
	}

	/* (non-Javadoc)
	 * @see roadroller.gameobject.powerups.IPowerUp#timeLeftToLive()
	 */
	public int timeLeftToLive() {
		
		return 0;
	}

	/* (non-Javadoc)
	 * @see roadroller.gameobject.powerups.IPowerUp#undoPowers()
	 */
	public void undoPowers() {
		this.active= false;

	}

	/* (non-Javadoc)
	 * @see roadroller.gameobject.powerups.IPowerUp#isActive()
	 */
	public boolean isActive() {
		// TODO Auto-generated method stub
		return this.active;
	}

}
