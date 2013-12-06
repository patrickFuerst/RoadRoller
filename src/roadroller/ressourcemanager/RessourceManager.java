/**
 * 
 */
package roadroller.ressourcemanager;

import java.io.IOException;

import javax.microedition.lcdui.Image;

import roadroller.gameobject.Ball;
import roadroller.gameobject.BloodSplatter;
import roadroller.gameobject.Car;
import roadroller.gameobject.Granny;
import roadroller.gameobject.powerups.FuelDrain;
import roadroller.gameobject.powerups.FuelUp;
import roadroller.gameobject.powerups.SlowDown;
import roadroller.gameobject.powerups.SpeedUp;
import roadroller.helper.Constants;
import roadroller.helper.DebugMessageManager;

/**
 * precaches resources which are frequently needed (eg. for enemy sprites)
 * 
 * use the getters to retrieve the Images
 * 
 * @author Patrick, Felix
 * 
 */
public class RessourceManager {
	private Image grannyImage;
	private Image carImage;
	private Image level2SignImage;
	private Image bloodSplatterImage;
	private Image speedUpImage;
	private Image slowDownImage;
	private Image fuelUpImage;
	private Image fuelDrainImage;
	private Image ballImage;

	private static RessourceManager singleton;

	private RessourceManager() {
		super();
		initImages();
	}

	private void initImages() {
		// load Granny Image
		try {
			grannyImage = Image.createImage(Granny.imgFile);
		} catch (IOException e) {
			DebugMessageManager.getInstance().printError(this,
					"file not found: " + Granny.imgFile);
		}
		//load Obstacle
		try {
			carImage = Image.createImage(Car.imgFile);
		} catch (IOException e) {
			DebugMessageManager.getInstance().printError(this,
					"file not found: " + Car.imgFile);
		}
		// load BloodSplatter Image
		try {
			bloodSplatterImage = Image.createImage(BloodSplatter.imgFile);
		} catch (IOException e) {
			DebugMessageManager.getInstance().printError(this,
					"file not found: " + BloodSplatter.imgFile);
		}
		//load speedup- powerup image
		try {
			speedUpImage = Image.createImage(SpeedUp.imgFile);
		} catch (IOException e) {
			DebugMessageManager.getInstance().printError(this,
					"file not found: " + SpeedUp.imgFile);
		}
		//load slowdown powerup image
		try {
			slowDownImage = Image.createImage(SlowDown.imgFile);
		} catch (IOException e) {
			DebugMessageManager.getInstance().printError(this,
					"file not found: " + SlowDown.imgFile);
		}
		//load fuelUp powerup image
		try {
			fuelUpImage = Image.createImage(FuelUp.imgFile);
		} catch (IOException e) {
			DebugMessageManager.getInstance().printError(this,
					"file not found: " + FuelUp.imgFile);
		}
		//load fuelDrain powerup image
		try {
			fuelDrainImage = Image.createImage(FuelDrain.imgFile);
		} catch (IOException e) {
			DebugMessageManager.getInstance().printError(this,
					"file not found: " + FuelDrain.imgFile);
		}
		//load Level2Sign image
		try {
			level2SignImage = Image.createImage(Constants.imgDir + "Level2Sign.png");
		} catch (IOException e) {
			DebugMessageManager.getInstance().printError(this,
					"file not found: " + Constants.imgDir + "Level2Sign");
		}
		//load ball image
		try {
			ballImage = Image.createImage(Ball.imgFile);
		} catch (IOException e) {
			DebugMessageManager.getInstance().printError(this,
					"file not found: " + Ball.imgFile);
		}
	}

	/**
	 * method used to retrieve the singleton instance of Resource manager
	 * 
	 * @return the unique InGameMenu instance
	 */
	public static RessourceManager getInstance() {
		if (singleton == null) {
			singleton = new RessourceManager();
		}
		return singleton;
	}

	public Image getGrannyImage() {
		return grannyImage;
	}
	public Image getCarImage() {
		return carImage;
	}

	public Image getBloodSplatterImage() {
		return bloodSplatterImage;
	}

	public Image getSpeedUpImage() {
		return speedUpImage;
	}

	public Image getSlowDownImage() {
		return slowDownImage;
	}

	public Image getFuelUpImage() {
		return fuelUpImage;
	}

	public Image getFuelDrainImage() {
		return fuelDrainImage;
	}	
	public Image getLevel2SignImage() {
		return level2SignImage;
	}
	
	public Image getBallImage() {
		return ballImage;
	}

}
