/**
 * 
 */
package roadroller.gameobject;

import java.io.IOException;

import javax.microedition.lcdui.game.Sprite;

import roadroller.helper.Constants;
import roadroller.objectmanager.GameObjectManager;

/**
 * player class - represents the steamroller
 * 
 * @author Felix, Patrick
 * 
 */
public class PlayerObject extends SpriteObject {

	/**
	 * Sprite-Spezifische Konstanten:
	 */
	public static final String imgFile = Constants.imgDir + "roller_48x48.png";
	public static final int frameWidth = 48;
	public static final int frameHeight = 48;
	public static final int[] frameSequence = { 0, 1, 2, 3, 4, 5, 4, 3, 2, 1, 0 };

	private int PLAYER_MAX_SPEED = 10;
	private int PLAYER_MIN_SPEED = 2;

	private int currentLane;
	private int steerDX;
	
	/**
	 * represents the maximum level of fuel for the steamroller
	 */
	private int fuelTankSize;
	/**
	 * fuel represents the players fuel level. fuel will constantly drain during game unless refilled by a powerup.
	 * if the fuel runs out the game is over.
	 */
	private int currentFuelLevel;
	private int score;
	private int speed;
	
	private boolean inLevel2;

	private int steerSequenceCounter = 0;
	private boolean steering = false;
	private boolean isSteeringLeft = false;

	/**
	 * Constructs the player object.
	 * 
	 * @param xPos the x-position
	 * @param yPos the y-position
	 * @throws IOException thrown if image file for sprite creation is not found
	 */
	public PlayerObject(int xPos, int yPos)
			throws IOException {
		super(imgFile, frameWidth, frameHeight, frameSequence, 24, 24, xPos,
				yPos);
		this.fuelTankSize = Constants.PLAYER_initialPlayerFuelLevel;
		this.currentFuelLevel = fuelTankSize;
		this.score = 0;
		this.currentLane = 2;
		this.steerDX = 36 / frameSequence.length;
		this.speed = Constants.PLAYER_initialPlayerSpeed;
		this.inLevel2 = false;
//		this.name = "No Name";
		

	}
	/**
	 * Returns the players current speed level.
	 * 
	 * @return current speed level
	 */
	public int getSpeed() {
		return speed;
	}
	
	/**
	 * set the players current speed level.
	 * 
	 * @param speed speedlevel
	 */
	public void setSpeed(int speed) {
		this.speed = speed;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see roadroller.gameobject.GameObject#update()
	 */
	public void update(int playerYSpeed) {
		// wenn der SteamRoller gerade eine kurve faehrt, dann sollte er nicht
		// unterbrochen werden

		if(score > Constants.PLAYER_scoreToLevel2 && !inLevel2){
			GameObjectManager.getInstance().changeToLevel2();
			speed = Constants.PLAYER_level2PlayerSpeed;
			inLevel2 = true;
		}
		
		if (steering) {
			if (steerSequenceCounter < frameSequence.length) {
				this.nextFrame();
				if (isSteeringLeft) {
					this.move(-steerDX, 0);
				} else {
					this.move(steerDX, 0);
				}
				steerSequenceCounter++;
			} else {
				steering = false;
				steerSequenceCounter = 0;
			}

		}

	}

	/**
	 * Steer to the next Lane on the left(if any)
	 */
	public void steerLeft() {
		steer(true);
	}

	/**
	 * Steer to the next Lane on the right(if any)
	 */
	public void steerRight() {
		steer(false);
	}

	private void steer(boolean steerLeft) {
		// checken ob sich der Player gerade in einer Animation befindet
		if (!this.steering) {

			if (steerLeft) {
				// check if player is allowed to move left
				if (this.currentLane > 0) {
					this.setTransform(Sprite.TRANS_MIRROR);
					this.steering = true;
					this.isSteeringLeft = true;
					this.currentLane--;
				}

			} else {
				// check if player is allowed to move right
				if (this.currentLane < 4) {
					this.setTransform(Sprite.TRANS_NONE);
					this.steering = true;
					this.isSteeringLeft = false;
					this.currentLane++;
				}

			}
		}
	}

	/**
	 * returns the index of the current lane, which the steamroller is rolling on.
	 * @return index of current lane
	 */
	public int getCurrentLane() {
		return currentLane;
	}

	/**
	 * returns the current gas level of the steamrollers tank.
	 * 
	 * @return current gas level
	 */
	public int getCurrentGasLevel() {
		return currentFuelLevel;
	}
	
	/**
	 * add fuel to the steamrollers gastank.
	 * 
	 * @param liters fuel in liters
	 */
	public void fuelUp(int liters) {
		currentFuelLevel+=liters;
		if(currentFuelLevel > fuelTankSize) {
			currentFuelLevel = fuelTankSize;
		}
	}
	
	/**
	 * drain fuel from the steamrollers gastank.
	 * 
	 * @param liters fuel in liters
	 */
	public void fuelDrain(int liters) {
		currentFuelLevel -= liters;
		if(currentFuelLevel <= 0) {
			currentFuelLevel = 0;
			GameObjectManager.getInstance().gameOver();
		}
	}

	/**
	 * returns the players current score.
	 * 
	 * @return score
	 */
	public int getScore() {
		return score;
	}

	/**
	 * increase players score by a given number of points.
	 * 
	 * @param value points to increase
	 */
	public void addScorePoints(int value) {
		this.score += value;
	}

	/**
	 * increase the steamrollers speed.
	 * 
	 * @param value level of speed increase
	 */
	public void increaseSpeedBy(int value) {
		// int speed = getySpeed();
		if (speed + value <= PLAYER_MAX_SPEED) {
			// setySpeed(speed + value);
			speed += value;
		}
	}

	/**
	 * decrease the steamrollers speed.
	 * 
	 * @param value level of speed decrease
	 */
	public void decreaseSpeedBy(int value) {
		// int speed = getySpeed();
		if (speed - value >= PLAYER_MIN_SPEED) {
			// setySpeed(speed - value);
			speed -= value;
		}
	}

	/* (non-Javadoc)
	 * @see roadroller.gameobject.SpriteObject#getSprite()
	 */
	public Sprite getSprite() {
		return this;
	}
}
