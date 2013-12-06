package roadroller.menu;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.m2g.SVGAnimator;
import javax.microedition.m2g.SVGImage;
import javax.microedition.m2g.ScalableGraphics;
import javax.microedition.m2g.ScalableImage;

import org.w3c.dom.Document;
import org.w3c.dom.svg.SVGAnimationElement;
import org.w3c.dom.svg.SVGElement;

import roadroller.canvas.RoadRollerCanvas;
import roadroller.helper.Constants;
import roadroller.helper.DebugMessageManager;
import roadroller.helper.L10nResources;

/**
 * Class holding the In Game Menu
 * 
 * this class contains and draws the ingame menus.
 * use the paint(Graphics) method to draw the menu.
 * this menu changes contents by itself according to user input entered with the inputKeyState(int) method.
 * 
 * this class calls startNewGame/resume/toggleMusic/destroyGame/getMidlet methods in the RoadRollerCanvas class.
 * 
 * @see RoadRollerCanvas#startNewGame()()
 * @see RoadRollerCanvas#resumeGame()
 * @see RoadRollerCanvas#toogleMusic()
 * @see RoadRollerCanvas#destroyGame()
 * @see RoadRollerCanvas#getMidlet()
 * 
 * @author Felix
 *
 */
public class InGameMenu {

	private static InGameMenu singleton;
	
	private final int MENU_STATE_INITIALISED = 0;
	private final int MENU_STATE_PAUSED = 1;
	private final int MENU_STATE_HIGHSCORE = 2;
	private final int MENU_STATE_HELP = 3;
	private final int MENU_STATE_GAMEOVER = 4;
	
	private L10nResources res;
	

	private int currentState;
	//indicates if menu is paused or not (used for returning to last state when returning from highscore/help state)
	private boolean mainMenuStatePaused;
	
	// retain a reference the specified image
	private ScalableImage mainMenuImage;
	//retain a reference to the current image used for drawing
	private ScalableImage currentImage;
	// retain an instance of a scalable graphics
	private ScalableGraphics scalableGraphics;
	private SVGAnimator animator;
	private Document doc;
	private String menu_selection_position[] = { "pos0", "pos1", "pos2", "pos3", "pos4", "pos5" };
	private int menu_selection_counter;

	private InGameMenu() {
		super();
//		initResourceManager();
		currentState = MENU_STATE_INITIALISED;
		mainMenuStatePaused = false;
		loadSVG();
		initAnimator();
		internationalizeMainMenu();
		resetCounter();
		currentImage = mainMenuImage;
	}

	/**
	 * initializes and starts the SVG animator
	 */
	private void initAnimator() {
		animator = SVGAnimator.createAnimator((SVGImage) mainMenuImage);
		animator.play();
		
	}

	private void internationalizeMainMenu() {
		String locale = System.getProperty("microedition.locale");
		res = L10nResources.getL10nResources(locale);
		
		SVGElement e;
		
		e = (SVGElement) doc.getElementById("menu_text_pos0");
		e.setTrait("#text", res.getString("RESUMEGAME"));
		
		e = (SVGElement) doc.getElementById("menu_text_pos1");
		e.setTrait("#text", res.getString("NEWGAME"));
		
		e = (SVGElement) doc.getElementById("menu_text_pos2");
		e.setTrait("#text", res.getString("HIGHSCORES"));
		
		e = (SVGElement) doc.getElementById("menu_text_pos3");
		e.setTrait("#text", res.getString("HELP"));
		
		e = (SVGElement) doc.getElementById("menu_text_pos4");
		e.setTrait("#text", res.getString("MUSICOFF"));
		
		e = (SVGElement) doc.getElementById("menu_text_pos5");
		e.setTrait("#text", res.getString("EXIT"));
	}

//	private void initResourceManager() {
//		
//	}

	/**
	 * method used to retrieve the singleton instance of InGameMenu
	 * 
	 * @return the unique InGameMenu instance
	 */
	public static InGameMenu getInstance() {
		if (singleton == null) {
			singleton = new InGameMenu();
		}
		return singleton;
	}

	private void loadSVG() {
		InputStream is = getClass().getResourceAsStream(
				Constants.svgDir + "mainMenu.svg");
		try {
			mainMenuImage = SVGImage.createImage(is, null);
			is.close();
//			animator = SVGAnimator.createAnimator((SVGImage) mainMenuImage);
//			animator.play();
			doc = ((SVGImage) mainMenuImage).getDocument();
			scalableGraphics = ScalableGraphics.createInstance();
		} catch (IOException e) {
			DebugMessageManager.getInstance().printError(this, e.getMessage());
		}
	}

	private void resetCounter() {
		SVGAnimationElement textAnim;
		switch(currentState) {
		case MENU_STATE_INITIALISED:
			menu_selection_counter = 1;
			textAnim = (SVGAnimationElement) doc.getElementById(menu_selection_position[menu_selection_counter]);
			textAnim.beginElementAt(0);
			break;
		case MENU_STATE_PAUSED:
			menu_selection_counter = 0;
			textAnim = (SVGAnimationElement) doc.getElementById(menu_selection_position[menu_selection_counter]);
			textAnim.beginElementAt(0);
			break;
		default:
			break;
		}

	}
	
	/**
	 * Orders the InGameMenu to prepare the Start Screen
	 */
	public void prepareStartMenu() {

//		SVGElement e = (SVGElement) doc.getElementById("menu_text_newGame");
//		e.setTrait("#text", label_newGame);
		this.currentState = MENU_STATE_INITIALISED;
		this.mainMenuStatePaused =false;
		currentImage = mainMenuImage;
		resetCounter();
	}

	/**
	 * Orders the InGameMenu to prepare the Pause Screen
	 */
	public void preparePauseMenu() {

//		SVGElement e = (SVGElement) doc.getElementById("menu_text_newGame");
//		e.setTrait("#text", label_resumeGame);
		this.currentState = MENU_STATE_PAUSED;
		this.mainMenuStatePaused = true;
		currentImage = mainMenuImage;
		resetCounter();
	}
	
	/**
	 * Orders the InGameMenu to show the GameOverScreen
	 * 
	 * @param name the player name
	 * @param score the scored points
	 */
	public void prepareGameOverScreen(String name, int score){
		this.currentState = MENU_STATE_GAMEOVER;
		currentImage = (new GameOverScreen(name, score)).getScalableImage();
	}
	
	private void prepareHelpScreen() {
		this.currentState = MENU_STATE_HELP;
		currentImage = (new HelpScreen(res)).getScalableImage();
	}
	
	private void prepareHighscoreState() {
		this.currentState = MENU_STATE_HIGHSCORE;
		currentImage = (new highscoreMenu()).getScalableImage();
	}

	/**
	 * handles keyboard input
	 * 
	 * @param key the key pressed
	 */
	public void inputKeyState(int key) {
		SVGAnimationElement textAnim;
		switch (key) {
		case GameCanvas.LEFT_PRESSED:
			break;
		case GameCanvas.RIGHT_PRESSED:
			break;
		case GameCanvas.UP_PRESSED:
			//only used in mainMenu
			if ( (currentState == MENU_STATE_INITIALISED && menu_selection_counter > 1) || (currentState == MENU_STATE_PAUSED && menu_selection_counter > 0) ){
				menu_selection_counter--;
				textAnim = (SVGAnimationElement) doc
				.getElementById(menu_selection_position[menu_selection_counter]);
				textAnim.beginElementAt(0);
//				DebugMessageManager.getInstance().printDebug(
//						textAnim.toString());

			}
			break;
			
		case GameCanvas.DOWN_PRESSED:
			//only used in mainMenu
			if(currentState == MENU_STATE_INITIALISED || currentState == MENU_STATE_PAUSED){
				if (menu_selection_counter < menu_selection_position.length - 1) {
					menu_selection_counter++;
					textAnim = (SVGAnimationElement) doc
					.getElementById(menu_selection_position[menu_selection_counter]);
					textAnim.beginElementAt(0);
//					DebugMessageManager.getInstance().printDebug(
//							textAnim.toString());

				}
			}
			break;
			
		case GameCanvas.FIRE_PRESSED:
			switch(currentState) {
			case MENU_STATE_INITIALISED:
			case MENU_STATE_PAUSED:
				switch(menu_selection_counter) {
				case 0:
					//resumeGame
					RoadRollerCanvas.getInstance().resumeGame();
					break;
				case 1:
					//newGame
					RoadRollerCanvas.getInstance().startNewGame();
					break;
				case 2:
					//highscore
					prepareHighscoreState();
					break;
				case 3:
					//help
					prepareHelpScreen();
					break;
				case 4:
					//music
					RoadRollerCanvas.getInstance().toogleMusic();
					if(RoadRollerCanvas.getInstance().isMusicOn()) {
						SVGElement e = (SVGElement) doc.getElementById("menu_text_pos4");
						e.setTrait("#text", res.getString("MUSICOFF"));
					} else {
						SVGElement e = (SVGElement) doc.getElementById("menu_text_pos4");
						e.setTrait("#text", res.getString("MUSICON"));
					}
					break;
				case 5:
					//exit
					RoadRollerCanvas.getInstance().destroyGame();
					destroyInGameMenu();
//					RoadRollerCanvas.getInstance().getMidlet().notifyDestroyed();
//					DebugMessageManager.getInstance().printDebug("GAME DESTROYED");
					break;
				}
				break;
			case MENU_STATE_HIGHSCORE:
				//return to mainMenu
				if(mainMenuStatePaused){
					preparePauseMenu();
				} else prepareStartMenu();
				break;
			case MENU_STATE_HELP:
				//return to mainMenu
				if(mainMenuStatePaused){
					preparePauseMenu();
				} else prepareStartMenu();
				break;
			case MENU_STATE_GAMEOVER:
				//return to main menu
				prepareStartMenu();
				break;
			}
			break;
		
			
		}

	}
	
	private void destroyInGameMenu() {
		this.animator.stop();
	}

	/**
	 * renders the menu to the given Graphics
	 * 
	 * @param g
	 */
	public void paint(Graphics g) {
		scalableGraphics.bindTarget(g);
		currentImage.setViewportWidth(RoadRollerCanvas.getInstance()
				.getWidth());
		currentImage.setViewportHeight(RoadRollerCanvas.getInstance()
				.getHeight());
		scalableGraphics.render(0, 0, currentImage);

		// release the graphics context
		scalableGraphics.releaseTarget();
	}
	
	public void freeResources() {
		this.destroyInGameMenu();
	}
	
	public void resetResources(){
		this.initAnimator();
	}
}
