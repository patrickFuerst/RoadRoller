package roadroller.helper;

/**
 * Class Containing Game specific Constants
 * @author Felix, Patrick
 *
 */
public final class Constants {

	/**
	 * path to the directory where SVG files are stored
	 */
	public static final String svgDir = "/svg/";
	
	/**
	 * path to the directory where image files are stored
	 */
	public static final String imgDir = "/images/";
	
	/**
	 * path to the directory where midi files are stored
	 */
	public static final String midDir = "/midi/";
	
	/**
	 * path to the directory where video files are stored
	 */
	public static final String videoDir = "/video/";
	
	/**
	 * time in ms between spawning of powerups
	 */
	public static final int GLOBAL_powerUpSpawnPeriod = 5000;
	
	/**
	 * limit of powerups on screen
	 */
	public static final int GLOBAL_powerUpSpawnLimit = 1;
	
	/**
	 * initial player speed in level 2
	 */
	public static final int PLAYER_level2PlayerSpeed = 8;
	
	
	/**
	 * score needed to access level 2
	 */
	public static final int PLAYER_scoreToLevel2 = 10000;
	
	
	/**
	 * initial player speed in level 1
	 */
	public static final int PLAYER_initialPlayerSpeed = 5;
	
	/**
	 * initial fuel level
	 */
	public static final int PLAYER_initialPlayerFuelLevel = 150;
	
	/**
	 * level of speed increase by SpeedUp Powerup
	 */
	public static final int POWERUP_SPEEDUP_deltaS = 2;
	
	/**
	 * level of speed decrease by SlowDown Powerup
	 */
	public static final int POWERUP_SLOWDOWN_deltaS = 2;
	
	
	/**
	 * liters of fuel added by fuelUp powerup
	 */
	public static final int POWERUP_FUELUP_litersPerApplication = 20;
	
	/**
	 * liters of fuel reduced by fuelDrain powerup
	 */
	public static final int POWERUP_FUELDRAIN_litersPerApplication = 20;
	
	/**
	 * The time (in ms) when the fuel should be drained for 1 liter,
	 * so that the game gets to an end.
	 */
	public static final int TIME_FUELDRAIN = 500;
	
	/**
	 * Amount of points you score if you roll over a granny.
	 */
	public static final int GRANNY_POINTS_PER_HIT = 400;
	
	/**
	 * Amount of points you score per frame while rolling over a parked car
	 */
	public static final int CAR_POINTS_PER_FRAME = 10;
	
	/**
	 * Amount of points you score if you roll over a jumping ball
	 */
	public static final int BALL_POINTS_PER_HIT = 600;
}
