package roadroller;

import javax.microedition.lcdui.Display;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import roadroller.canvas.RoadRollerCanvas;
import roadroller.helper.DebugMessageManager;

/**
 * RoadRoller MIDlet class
 * 
 * this Midlet runs the game, creates our canvas, and is responsible to free the resources in pause mode
 * 
 * @author Felix, Patrick
 *
 */
public class RoadRoller extends MIDlet {

	// make getter
	public static Display display;
	private RoadRollerCanvas canvas;
	private Thread gameThread;

/**
 * Creates our canvas
 */
	public RoadRoller() {
		this.canvas = RoadRollerCanvas.getInstance();
		this.canvas.setMidlet(this);
	}

	
	/**
	 * Is called when our app gets  interrupted somehow.
	 * It is not called when the game is quite with the exit button in the menu.
	 * If the exit button will be pressed all resources gets freed and notifyDestroyed will be called in the Canvas Class.
	 * @see RoadRollerCanvas#destroyGame()
	 * @see #notifyDestroyed()
	 */
	protected void destroyApp(boolean unconditional)
			throws MIDletStateChangeException {
		
		canvas.destroyGame();
	
	}

	/**
	 * If this method is called the GameThread will quite and free resources
	 */
	protected void pauseApp() {
		canvas.pauseGameAndFreeResources();
	}

	/**
	 * Starts the GameThread 
	 * If pausedApp() is called before it resets the resources and resumes the game
	 */
	protected void startApp() throws MIDletStateChangeException {
		
		display = Display.getDisplay(this);
		display.setCurrent(canvas);
		DebugMessageManager.getInstance().printDebug(
					"Screen Initialised (" + canvas.getWidth() + "x"
							+ canvas.getHeight() + ")");
		if(!canvas.isFirstExecution()) {
			canvas.resetResourcesAndResume();
		}
		gameThread = new Thread(canvas);
		gameThread.start();
		
		
	}

	
	

}
