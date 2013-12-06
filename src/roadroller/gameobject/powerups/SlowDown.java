package roadroller.gameobject.powerups;

import java.io.IOException;

import javax.microedition.lcdui.game.Sprite;

import roadroller.canvas.RoadRollerCanvas;
import roadroller.gameobject.SpriteObject;
import roadroller.helper.Constants;
import roadroller.objectmanager.GameObjectManager;
import roadroller.ressourcemanager.RessourceManager;

/**
 * SlowDown Powerup.
 * 
 * this class represents the SlowDown powerup.
 * 
 * its powers decrease the steamrollers speed.
 * 
 * @author Felix
 *
 */
public class SlowDown extends SpriteObject implements IPowerUp {
	
	/**
	 * Sprite-Specific constants:
	 */
	public static final String imgFile = Constants.imgDir + "slowDown30x16x5.png";
	public static final int frameWidth = 30;
	public static final int frameHeight = 16;
	public static final int[] frameSequence = {0,1,2,3,4};
	
	private int lifeTime = 5000;
	private boolean active;

	/**
	 * Constructs the powerup
	 * 
	 * @param xPos x-position where the sprite is to be placed
	 * @param yPos y-position where the sprite is to be placed
	 */
	public SlowDown(int xPos,
			int yPos) throws IOException {
		super(RessourceManager.getInstance().getSlowDownImage(), frameWidth, frameHeight, frameSequence, 15,
				8, xPos, yPos);
		super.getSprite().setTransform(Sprite.TRANS_MIRROR);
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

	/* (non-Javadoc)
	 * @see roadroller.gameobject.powerups.IPowerUp#applyPowers()
	 */
	public void applyPowers() {
		this.active = true;
		GameObjectManager.getInstance().getPlayer().decreaseSpeedBy(Constants.POWERUP_SLOWDOWN_deltaS);
	}

	/* (non-Javadoc)
	 * @see roadroller.gameobject.powerups.IPowerUp#timeLeftToLive()
	 */
	public int timeLeftToLive() {
		return lifeTime;
	}

	/* (non-Javadoc)
	 * @see roadroller.gameobject.powerups.IPowerUp#undoPowers()
	 */
	public void undoPowers() {
		this.active=false;
		GameObjectManager.getInstance().getPlayer().increaseSpeedBy(Constants.POWERUP_SLOWDOWN_deltaS);
	}

	/* (non-Javadoc)
	 * @see roadroller.gameobject.powerups.IPowerUp#isActive()
	 */
	public boolean isActive() {
		return active;
	}

}
