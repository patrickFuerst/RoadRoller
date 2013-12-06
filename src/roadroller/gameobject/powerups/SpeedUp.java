/**
 * 
 */
package roadroller.gameobject.powerups;

import java.io.IOException;

import roadroller.canvas.RoadRollerCanvas;
import roadroller.gameobject.SpriteObject;
import roadroller.helper.Constants;
import roadroller.objectmanager.GameObjectManager;
import roadroller.ressourcemanager.RessourceManager;

/**
 * SpeedUp Powerup.
 * 
 * this class represents the SpeedUp powerup.
 * 
 * its powers increase the steamrollers speed.
 * 
 * @author Felix
 *
 */
public class SpeedUp extends SpriteObject implements IPowerUp{

	/**
	 * Sprite-Specific constants:
	 */
	public static final String imgFile = Constants.imgDir + "speedUp30x16x5.png";
	public static final int frameWidth = 30;
	public static final int frameHeight = 16;
	public static final int[] frameSequence = {0,1,2,3,4};

	private int lifeTime = 5000;
	private boolean active;

	public SpeedUp(int xPos,
			int yPos) throws IOException {
		super(RessourceManager.getInstance().getSpeedUpImage(), frameWidth, frameHeight, frameSequence, 15, 8,
				xPos, yPos);
		this.active = false;
	}

	/* (non-Javadoc)
	 * @see roadroller.gameobject.GameObject#update(int)
	 */
	public void update(int playerYSpeed) {
		nextFrame();
		if(active) {
			//decrease lifetime
			lifeTime -= RoadRollerCanvas.getInstance().getMsPerFrame();
		}
	}

	public void applyPowers() {
		this.active = true;
		GameObjectManager.getInstance().getPlayer().increaseSpeedBy(Constants.POWERUP_SPEEDUP_deltaS);
		
	}

	public int timeLeftToLive() {
		// TODO Auto-generated method stub
		return lifeTime;
	}

	public void undoPowers() {
		this.active=false;
		GameObjectManager.getInstance().getPlayer().decreaseSpeedBy(Constants.POWERUP_SPEEDUP_deltaS);
	}

	public boolean isActive() {
		// TODO Auto-generated method stub
		return active;
	}

}
