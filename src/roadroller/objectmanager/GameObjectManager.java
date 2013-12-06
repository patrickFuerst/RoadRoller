package roadroller.objectmanager;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Random;
import java.util.Vector;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.lcdui.game.LayerManager;
import javax.microedition.lcdui.game.Sprite;
import javax.microedition.lcdui.game.TiledLayer;

import roadroller.canvas.RoadRollerCanvas;
import roadroller.gameobject.Ball;
import roadroller.gameobject.BloodSplatter;
import roadroller.gameobject.Car;
import roadroller.gameobject.Enemy;
import roadroller.gameobject.GameObject;
import roadroller.gameobject.Granny;
import roadroller.gameobject.PlayerObject;
import roadroller.gameobject.powerups.FuelDrain;
import roadroller.gameobject.powerups.FuelUp;
import roadroller.gameobject.powerups.IPowerUp;
import roadroller.gameobject.powerups.SlowDown;
import roadroller.gameobject.powerups.SpeedUp;
import roadroller.helper.Constants;
import roadroller.helper.DebugMessageManager;
import roadroller.ressourcemanager.RessourceManager;
import roadroller.score.Score;

/**
 * GameObjectManager, manages all Objects and is responsible for the behavior of the game in the running mode.
 * 
 * Includes the player and all sprites. Responds to key Input. 
 * 
 * Singleton Class
 *  
 * @author Felix, patrickfuerst
 * 
 */

public class GameObjectManager {

	private static GameObjectManager singleton = null;
	
	/**
	 * needed for the random object spawn mechanism - indicates the number of
	 * different spawnable objects available in the game !!!has to be synched
	 * with the switch statement in spawnObjects()!!!
	 */
	private final int numOfSpwanableClasses = 3;
	
	
	//private final int maxObjectsIngame = 20;
	
	/**
	 * the player object
	 */
	private PlayerObject player;
	/**
	 * the players Name ... used for player creation
	 */
	private String playerName;
	
	
	/**
	 * list containing all objects tracked by the game manager - excluding the
	 * player object
	 */
	private Vector obstacles;

	private Vector powerUps;
	
	/**
	 * explosions, blood, gore
	 */
	private Vector decals;

	/**
	 * layer manager used in the game
	 */
	private LayerManager layerManager;

	/**
	 * current limit of concurrently active objects in the game
	 */
	private int currentSpawnLimit;

	/**
	 * minimum time between spawning of new objects
	 */
	private int spawnPeriod;

	/**
	 * keeps track of the Spawn time
	 */
	private int spawnTimer;

	/**
	 * Object for background Is initialized with the ObjectManager
	 * 
	 */
	private TiledLayer background;
	/**
	 * Object for background 2 Is initialized with the ObjectManager
	 * 
	 */
	private TiledLayer background2;
	/**
	 * size of the background without the repeated part If background 3/3 ->
	 * background size is 2/3 Because you have to repeat one part with size of
	 * the screen
	 * 
	 */
	
	/**
	 * Background size without the repeated part of the Background
	 */
	private int backgroundSizeY;
	
	private int powerUpSpawnTimer;
	private int numOfPowerUpClasses;
	
	/**
	 * keeps Track of the elapsed time
	 */
	private int timeTrack;
	
	/**
	 * Indicates if startphase is still running
	 */
	private boolean startPhase;
	
	/**
	 * String for the Countdown in the startphase
	 */
	private String countDown;
	/**
	 * keeps track of the time for startphase
	 */
	private int startPhaseTimer;
	
	/**
	 * to random position the obstacles.
	 */
	private Random rand;
	
	/**
	 * Calls initVariables() and initObjects() and sets a default Name.
	 * @see #initVariables()
	 * @see #initObjects()
	 */
	private GameObjectManager() {
		super();
		this.playerName = "No Name";
		initVariables();
		initObjects();
	}

	/**
	 * Initializes all variables with default values.
	 */
	private void initVariables() {
		this.currentSpawnLimit = 3;
		this.spawnTimer = 0;
		this.spawnPeriod = 2000;
		this.powerUpSpawnTimer=0;
		this.numOfPowerUpClasses = 4;
		this.timeTrack = 0;
		this.startPhase = true;
		this.countDown = "3";
		this.startPhaseTimer = 0;
	}

	/**
	 * returns GameObjectManager singleton
	 * 
	 * @return GameObjectManager Singleton-Instance
	 */
	public static GameObjectManager getInstance() {
		if (singleton == null) {
			singleton = new GameObjectManager();
		}
		return singleton;
	}

	/**
	 * Creates all Objects with the default Constructor and creates the two different levels.
	 * 
	 */
	private void initObjects() {
		// init lists
		this.obstacles = new Vector();
		this.decals = new Vector();
		this.powerUps = new Vector();
		this.layerManager = new LayerManager();
		this.layerManager.setViewWindow(0, 0, 240, 320);
		this.rand = new Random();
//		this.rand.setSeed(320);
		// create player
		try {
			this.player = new PlayerObject(240 / 2, 320 - 45);
//			this.player.setSpeed(2);
			// this.objects.addElement(player);
			this.layerManager.append(player.getSprite());
		} catch (IOException e) {
			DebugMessageManager.getInstance().printError(this,
					"Player loading failed: " + e.getMessage());
		}

		try {
			this.createBackground();
			this.createBackground2();
		} catch (IOException e) {
			DebugMessageManager.getInstance().printError(this,
					"Background Load failed: " + e.getMessage());
		}
		this.layerManager.append(background);

	}
	
	/**
	 * Returns the Player name, set at the beginning of the game.
	 * @return PlayerName
	 */
	public String getPlayerName() {
		return playerName;
	}

	/**
	 * Sets the PlayerName, normally only at the beginning of the game.
	 * @param playerName
	 */
	public void setPlayerName(String playerName) {
		if(playerName == null || playerName == ""){
			this.playerName= "No Name";
		}
		else {
			this.playerName = playerName;
		}
			
		
	}

	/**
	 * Returns the player object
	 * @return player object
	 */
	public PlayerObject getPlayer() {
		return player;
	}

	/**
	 * Changes the Level.
	 * Changes the Background and the speed of the player, removes the old background 
	 * and let«s the thread sleep for 1s.
	 * 
	 */
	public void changeToLevel2(){
		this.layerManager.insert(background2, this.layerManager.getSize()-1);
		this.layerManager.remove(background);
	
		Sprite level2 = new Sprite( RessourceManager.getInstance().getLevel2SignImage());
		level2.setPosition(0, 150);
		 this.layerManager.insert(level2, this.layerManager.getSize()-2);
		RoadRollerCanvas.getInstance().updateScreen();
		 this.layerManager.remove(level2);

		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			DebugMessageManager.getInstance().printError(this,
					"Failed to Pause for Level2: " + e.getMessage());
		}

	}
	
	/**
	 * Resets the game
	 * Calls initVariables() and initObjects().
	 * @see #initVariables()
	 * @see #initObjects()
	 */
	public void resetGameObjects() {
		this.initVariables();
		this.initObjects();
	}

	/**
	 * Checks the keystates and acts accordingly
	 * @param keyState
	 */
	public void updateGameState(int keyState) {

		switch (keyState) {
		case GameCanvas.LEFT_PRESSED:
			player.steerLeft();
			break;
		case GameCanvas.RIGHT_PRESSED:
			player.steerRight();
			break;
//		case GameCanvas.UP_PRESSED:
//			if (currentSpawnLimit < maxObjectsIngame) {
//				currentSpawnLimit++;
//				DebugMessageManager.getInstance().printDebug(
//						"SpawnLimit: " + currentSpawnLimit);
//			}
//			break;
//		case GameCanvas.DOWN_PRESSED:
//			if (currentSpawnLimit > 1) {
//				currentSpawnLimit--;
//				DebugMessageManager.getInstance().printDebug(
//						"SpawnLimit: " + currentSpawnLimit);
//			}
//			break;
		case GameCanvas.FIRE_PRESSED:
			RoadRollerCanvas.getInstance().pauseGame();
			break;
		}
	}

	/**
	 * Updates the whole game play
	 * Spawns all Objects, moves the Objects, collision detection
	 */
	public void updateGame() {

		if(startPhase){
			startPhaseTimer += RoadRollerCanvas.getInstance().getMsPerFrame();
			
		
			if(startPhaseTimer > 4000)
				startPhase = false;
			else if(startPhaseTimer > 3000)
				countDown = "Start";
			else if(startPhaseTimer > 2000)
				countDown = "1";
			else if(startPhaseTimer > 1000)
				countDown = "2";
			
		
		}else{
			// updateGame
			spawnPowerUps();
			spawnObjects();
			// get current speed
			int currentYSpeed = player.getSpeed();
			
			// Move Background and all obstacles with playerspeed

			move(currentYSpeed);
			
			// Collision detection
			checkObstacleCollision(currentYSpeed);
			
			//PowerUp Collection
			collectPowerUps(currentYSpeed);
			
			updateDecals();
			// update player
			player.update(currentYSpeed);
			
			//reduce fuel to get to an end
			
			timeTrack += RoadRollerCanvas.getInstance().getMsPerFrame();
			if(timeTrack >= Constants.TIME_FUELDRAIN){
				
				if(player.getSpeed() > Constants.PLAYER_initialPlayerSpeed)
					player.fuelDrain(2);
				else
					player.fuelDrain(1);

				timeTrack = 0;
			}
		}

				
		
		
	}

	/**
	 * Checks if the player collides with an Enemy 
	 * If so, the score will be updated, obstacles will be removed from the layerManager and the speed will be adjusted
	 * Also checks if the obstacles is beyond visibility
	 * @param currentYSpeed
	 */
	private void checkObstacleCollision(int currentYSpeed) {
		
		Enumeration elements = obstacles.elements();
		while (elements.hasMoreElements()) {
			
			GameObject current = (GameObject) elements.nextElement();
			
			if ( player.collidesWith(current.getSprite(), true)) {
				
				
				if(current instanceof Car){
					player.addScorePoints(((Enemy) current).cashScore());
					((Car) current).nextFrame();
				}else{
				
				player.addScorePoints(((Enemy) current).cashScore());
				drawSplatter(current.getSprite().getX(), current.getSprite()
						.getY());
				obstacles.removeElement(current);
				layerManager.remove(current.getSprite());
				
				DebugMessageManager.getInstance().printDebug(
						current.getClass().getName() + " collided with player");
				}
			}

			// check if current object is beyond visibility
			else if (current.getSprite().getY() > 320) {
				obstacles.removeElement(current);
				layerManager.remove(current.getSprite());
				// DebugMessageManager.getInstance().printDebug(
				// current.getClass().getName() + " removed form game");
			} else {
				current.update(currentYSpeed);
			}
		}
	}

	/**
	 * Checks if the player collides with a powerup
	 * If so, obstacles will be removed from the layerManager and the speed or fuel will be adjusted
	 * Also checks if the obstacles is beyond visibility
	 * @param currentYSpeed
	 */
	private void collectPowerUps(int currentYSpeed) {
		Enumeration list = powerUps.elements();
		while (list.hasMoreElements()) {
			
			IPowerUp current = (IPowerUp) list.nextElement();
			current.update(currentYSpeed);
			
			if(current.isActive()) {
					
				// lifetime check
				if(current.timeLeftToLive() <= 0) {
					current.undoPowers();
					powerUps.removeElement(current);
				}

			} else if(player.collidesWith(current.getSprite(), true)) {
				//auswerten
				current.applyPowers();
				layerManager.remove(current.getSprite());
			}
			
			
			// check if current object is beyond visibility
			else if (current.getSprite().getY() > 320) {
				if(!current.isActive()) {
					powerUps.removeElement(current);
					layerManager.remove(current.getSprite());
				}
				// DebugMessageManager.getInstance().printDebug(
				// current.getClass().getName() + " removed form game");
			} 
		}
	}

	/**
	 * Spawns all powerups.
	 * Depending on the random variable the powerups are spawn
	 */
	private void spawnPowerUps() {
		
		powerUpSpawnTimer += RoadRollerCanvas.getInstance().getMsPerFrame()*player.getSpeed();
		
		if (powerUpSpawnTimer >= Constants.GLOBAL_powerUpSpawnPeriod) {

			if (powerUps.size() < Constants.GLOBAL_powerUpSpawnLimit) {
				powerUpSpawnTimer = 0;
				
				int r = Math.abs(rand.nextInt());
				// create granny
				int x = r % 180;
				x += 20;
				// select a random class to spawn
				switch (r % numOfPowerUpClasses) {
				case 0:
					// spawn SpeedUP
					SpeedUp s;
					try {
						s = new SpeedUp(x, -10);
						powerUps.addElement(s);
						// not append because last has to be the background
						layerManager.insert(s, layerManager.getSize() - 1);
						DebugMessageManager.getInstance().printDebug(
								s.getClass().getName() + " spawned at x=" + x);
					} catch (IOException e) {
						DebugMessageManager.getInstance().printError(this,
								"Object loading failed: " + e.getMessage());
					}
					break;
				case 1:
					//spawn slowDown
					SlowDown s1;
					try {
						s1 = new SlowDown(x, -10);
						powerUps.addElement(s1);
						// not append because last has to be the background
						layerManager.insert(s1, layerManager.getSize() - 1);
						DebugMessageManager.getInstance().printDebug(
								s1.getClass().getName() + " spawned at x=" + x);
					} catch (IOException e) {
						DebugMessageManager.getInstance().printError(this,
								"Object loading failed: " + e.getMessage());
					}
					break;
				case 2:
					//spawn FuelUp
					FuelUp s2;
					try {
						s2 = new FuelUp(x, -10);
						powerUps.addElement(s2);
						// not append because last has to be the background
						layerManager.insert(s2, layerManager.getSize() - 1);
						DebugMessageManager.getInstance().printDebug(
								s2.getClass().getName() + " spawned at x=" + x);
					} catch (IOException e) {
						DebugMessageManager.getInstance().printError(this,
								"Object loading failed: " + e.getMessage());
					}
					break;
				case 3:
					//spawn FuelDrain
					FuelDrain s3;
					try {
						s3 = new FuelDrain(x, -10);
						powerUps.addElement(s3);
						// not append because last has to be the background
						layerManager.insert(s3, layerManager.getSize() - 1);
						DebugMessageManager.getInstance().printDebug(
								s3.getClass().getName() + " spawned at x=" + x);
					} catch (IOException e) {
						DebugMessageManager.getInstance().printError(this,
								"Object loading failed: " + e.getMessage());
					}
					break;
				}
			}
		}
		
	}

	/**
	 * updates all decals, for example the blood splatter and removes them if they are beyond visibility.
	 */
	private void updateDecals() {
		Enumeration list = decals.elements();
		while (list.hasMoreElements()) {
			GameObject current = (GameObject) list.nextElement();
			// System.out.println(current.getLayer().getY());
			if (current.getSprite().getY() > 320) {
				decals.removeElement(current);
				layerManager.remove(current.getSprite());
				DebugMessageManager.getInstance().printDebug(
						current.getClass().getName() + " removed form game");
			} else {
				current.update(0);
			}
		}
	}
	/**
	 * Moves all objects in the layermanager except the player
	 * @param speed
	 */
	private void move(int speed) {

		if (layerManager.getLayerAt(layerManager.getSize() - 1).getY() >= 0) {

			layerManager.getLayerAt(layerManager.getSize() - 1).setPosition(0,
					-backgroundSizeY);

		}
		// Layer size is min 2 because of player and background

		for (int i = 1; i < layerManager.getSize(); i++) {

			layerManager.getLayerAt(i).move(0, speed);
		}

	}

	/**
	 * Draws the BloodSplatter and will be added to the layermanager
	 * @param x
	 * @param y
	 */
	private void drawSplatter(int x, int y) {
		try {
			BloodSplatter s = new BloodSplatter(x, y);
			decals.addElement(s);
			layerManager.insert(s, layerManager.getSize() - 1);
		} catch (IOException e) {
			DebugMessageManager.getInstance().printError(this,
					"drawing sprite" + e.getMessage());
		}
	}

	/**
	 * Spawns all obstacles.
	 * Depending on the random variable 
	 */
	private void spawnObjects() {

		spawnTimer += RoadRollerCanvas.getInstance().getMsPerFrame()*player.getSpeed();
		if (spawnTimer >= spawnPeriod) {

			if (obstacles.size() < currentSpawnLimit) {
				spawnTimer = 0;
				int r = Math.abs(rand.nextInt());
				// select a random class to spawn
				switch (r % numOfSpwanableClasses) {
				case 0:
					// create granny
					int x = r % 180;
					x += 20;
					Granny g;
					try {
						g = new Granny(x, -10);
						obstacles.addElement(g);
						// not append because last has to be the background
						layerManager.insert(g, layerManager.getSize() - 1);
						DebugMessageManager.getInstance().printDebug(
								g.getClass().getName() + " spawned at x=" + x);
					} catch (IOException e) {
						DebugMessageManager.getInstance().printError(this,
								"Granny loading failed: " + e.getMessage());
					}

					break;
				case 1:
					// create car
					 int i = rand.nextInt(5);
					 if(i == 0 || i == 3)
						 x = 46;
					 else
						 x = 190;
					Car o;
					try {
						o = new Car(x, -40);
						obstacles.addElement(o);
						// not append because last has to be the background
						layerManager.insert(o, layerManager.getSize() - 1);
						DebugMessageManager.getInstance().printDebug(
								o.getClass().getName() + " spawned at x=" + x);
					} catch (IOException e) {
						DebugMessageManager.getInstance().printError(this,
								"Granny loading failed: " + e.getMessage());
					}

					break;
				case 2:
					// create red jumping ball
					int x2 = r % 180;
					Ball b;
					try {
						b = new Ball(x2, -10);
						obstacles.addElement(b);
						// not append because last has to be the background
						layerManager.insert(b, layerManager.getSize() - 1);
						DebugMessageManager.getInstance().printDebug(
								b.getClass().getName() + " spawned at x=" + x2);
					} catch (IOException e) {
						DebugMessageManager.getInstance().printError(this,
								"Ball loading failed: " + e.getMessage());
					}

					break;
				}
			}
		}
	}


	/**
	 * Creates the background for level1 as TiledLayer
	 * @throws IOException
	 */
	private void createBackground() throws IOException {

		// Load the image from Res
		Image backgroundImg = Image.createImage(Constants.imgDir
				+ "/background_30x32.png");

		// s backgroundSizeY = (int) (backgroundImg.getHeight() *
		// Constants.BG_RATIO);
		backgroundSizeY = 1280;
		// Create the background as TiledLayer
		background = new TiledLayer(8, 50, backgroundImg, 30, 32);

		int[] cells = new int[50 * 8];

		for (int i = 0; i < 50* 8; i++) {

			cells[i] = i + 1;

		}

		// set the background with the Tiles
		for (int i = 0; i < cells.length; i++) {
			int column = i % 8;
			int row = (i - column) / 8;
			background.setCell(column, row, cells[i]);
		}

		// set the location of the background
		// beginning is on the bottom, so set the origin of the background to
		// -backgroundSize
		background.setPosition(0, -backgroundSizeY);

	}
	/**
	 * Creates the background for level2 as TiledLayer
	 * @throws IOException
	 */	private void createBackground2() throws IOException {

		// Load the image from Res
		Image backgroundImg = Image.createImage(Constants.imgDir
				+ "/background_30x32-2.png");

		// s backgroundSizeY = (int) (backgroundImg.getHeight() *
		// Constants.BG_RATIO);
		backgroundSizeY = 1280;
		// Create the background as TiledLayer
		background2 = new TiledLayer(8, 50, backgroundImg, 30, 32);

		int[] cells = new int[50 * 8];

		for (int i = 0; i < 50* 8; i++) {

			cells[i] = i + 1;

		}

		// set the background with the Tiles
		for (int i = 0; i < cells.length; i++) {
			int column = i % 8;
			int row = (i - column) / 8;
			background2.setCell(column, row, cells[i]);
		}

		// set the location of the background
		// beginning is on the bottom, so set the origin of the background to
		// -backgroundSize
		background2.setPosition(0, -backgroundSizeY);

	}
	
	/**
	 * Saves the Current Score and calls gameOver() in GameCanvas
	 * @see RoadRollerCanvas#gameOver(String, int)
	 */
	public void gameOver(){
		DebugMessageManager.getInstance().printDebug("Saving Score for player " + playerName);
		Score.getInstance().saveScore(playerName, player.getScore());
		RoadRollerCanvas.getInstance().gameOver(playerName, player.getScore());
	}

	/**
	 * Draws all Objects in the Layermanager, the player score and the fuel level. If the game is in startphase, the countdown is drawn
	 * @param g
	 */
	public void paint(Graphics g) {
		// draw all objects
		this.layerManager.paint(g, 0, 0);
		g.setFont(Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_LARGE));
		g.drawString("SCORE:" + player.getScore(), 0, 0, Graphics.TOP
				| Graphics.LEFT);
		g.drawString("FUEL: " + player.getCurrentGasLevel(), 240, 0, Graphics.TOP | Graphics.RIGHT);
		g.setColor(255, 0, 0);
		if(startPhase)
			g.drawString(countDown,120, 160, Graphics.BASELINE | Graphics.HCENTER);

	}
}
