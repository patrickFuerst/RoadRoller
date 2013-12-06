/**
 * 
 */
package roadroller.canvas;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.TextField;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.media.PlayerListener;
import javax.microedition.media.control.VideoControl;
import javax.microedition.midlet.MIDlet;

import roadroller.RoadRoller;
import roadroller.helper.Constants;
import roadroller.helper.DebugMessageManager;
import roadroller.menu.InGameMenu;
import roadroller.objectmanager.GameObjectManager;

/**
 * coordinates the game loop.
 * 
 * This class is responsible for the game loop, music and the intro video playback.
 * first the intro is played back, then a form is displayed for entering the playername.
 * after all that the gameloop is started in puse mode, where the menu is called.
 * once the game gets started by the player, the game loop continues forwarding userinput to the objectmanager, 
 * updating the gamecontent via the objectmanager and finally renders the content to the display.
 * 
 * @author Patrick, Felix
 * 
 */
public class RoadRollerCanvas extends GameCanvas implements Runnable,
		PlayerListener, CommandListener {

	private boolean running;
	private boolean paused;
	private boolean readName;
	private boolean musicOn;
	private boolean firstExecution;
	

	private int msPerFrame = 40;
	private static RoadRollerCanvas singleton = null;
	private GameObjectManager objectManager = null;
	private boolean introFinished;
	private Player vp;
	private Command start;
	private Form readNameForm;
	private TextField text;

	/**
	 * Reference to the Game menue
	 */
	private InGameMenu menue;

	/**
	 * Hold a reference to the midlet
	 */
	private MIDlet midlet;
	/**
	 * Player, to play midi files
	 */
	private Player midiPlayer = null;

//	/**
//	 * Tempo control fuer MidiPlayer
//	 */
//	private TempoControl tempoControl = null;

	private RoadRollerCanvas(boolean suppressKeyEvents) {
		super(suppressKeyEvents);
		this.setFullScreenMode(true);
		this.midlet = null;
		this.musicOn = true;
		this.running = true;
		this.readName = true;
		this.paused = true;
		introFinished = false;
		this.menue = InGameMenu.getInstance();
		this.objectManager = GameObjectManager.getInstance();
		initMidiPlayer();
		this.firstExecution=true;
	}

	/**
	 * method used to retrieve the singleton instance of RoadRollerCanvas
	 * 
	 * @return the unique RoadRollerCanvas instance
	 */
	public static RoadRollerCanvas getInstance() {
		if (singleton == null) {
			singleton = new RoadRollerCanvas(true);
		}
		return singleton;
	}
	
	/**
	 * this tells you if the Thread was started before, in case you want to resume
	 * @return status
	 */
	public boolean isFirstExecution() {
		return firstExecution;
	}
	
	/**
	 * Initializes the midi player
	 */
	private void initMidiPlayer() {
		try {
			InputStream is = getClass().getResourceAsStream(
					Constants.midDir + "ramgr.mid");
			midiPlayer = Manager.createPlayer(is, "audio/midi");
			midiPlayer.realize();
			is.close();

			/**
			 * unfortunately the following lines have no effect due to a bug in j2me.
			 * intentionally we wanted to adapt the music playback tempo to the tempo of the game.
			 */
			/*
			 * tempoControl = (TempoControl)
			 * midiPlayer.getControl("TempoControl");
			 * tempoControl.setTempo(120000); System.out.println((TempoControl)
			 * midiPlayer .getControl("TempoControl"));
			 */
		} catch (MediaException e) {
			DebugMessageManager.getInstance().printError(this,
					"Could not create a Player: " + e.getMessage());
		} catch (IOException e) {

			DebugMessageManager.getInstance().printError(this,
					"Could not open Midi-File " + e.getMessage());
		}
	}

	/**
	 * tells wheater music is on or not
	 * 
	 * @return music-status
	 */
	public boolean isMusicOn() {
		return musicOn;
	}

	/**
	 * Returns the time waited between gameloop updates in ms
	 * 
	 * @return current time waited between gameloop updates in ms
	 */
	public int getMsPerFrame() {
		return msPerFrame;
	}

	/**
	 * Set the Midlet from the Game
	 * 
	 * @param midlet
	 */
	public void setMidlet(MIDlet midlet) {
		this.midlet = midlet;

	}

	/**
	 * Returns the midlet holding this Canvas
	 * 
	 * @return MIDlet
	 */
	public MIDlet getMidlet() {
		return this.midlet;

	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		
		if(!introFinished) { // this ensures, that the intro is only played on mitdlet startup, not on resume
			this.firstExecution = false;
			DebugMessageManager.getInstance().printDebug("Intro Playback Started");
			// play Intro
			playIntro();

			DebugMessageManager.getInstance().printDebug("Video Player Started");

			// while intro is playing, check if the skip button is pressed
			while (!introFinished) {

				if (this.getKeyStates() == GameCanvas.FIRE_PRESSED) {
					skipIntro();

					// key debouncing
					try {
						Thread.sleep(300);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
			DebugMessageManager.getInstance().printDebug("Intro Playback Finished");
			readName();
		}
		
		while (running) {
			while (paused) {
				try {
					// TastenEntprellung
					Thread.sleep(300);
					menue.inputKeyState(this.getKeyStates());
					updateScreen();
				} catch (InterruptedException e) {
					DebugMessageManager.getInstance().printError(
							this,
							"something weired stopped the thread from pausing: "
									+ e.getMessage());
				}
			}

			try {
				Thread.sleep(msPerFrame);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			this.objectManager.updateGameState(this.getKeyStates());
			this.objectManager.updateGame();
			updateScreen();
		}
		DebugMessageManager.getInstance().printDebug("Game Thread stopped");
	}

	/**
	 * this method updates the display respectively to the gamestate
	 */
	public void updateScreen() {
		Graphics g = this.getGraphics();

		g.setColor(0x00990000);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		if (paused) {
			InGameMenu.getInstance().paint(g);
		} else {
			this.objectManager.paint(g);
		}

		this.flushGraphics();
	}

	/**
	 * Toggles the inGame Music
	 */
	public void toogleMusic() {
		musicOn = !musicOn;
	}

	/**
	 * pauses the game
	 * 
	 * sets game loop to pasued state - menu gets called, game does not get updated in pause mode
	 */
	public void pauseGame() {
		InGameMenu.getInstance().preparePauseMenu();
		paused = true;
		try {
			midiPlayer.stop();
		} catch (MediaException e) {
			DebugMessageManager.getInstance().printError(this,
					"Couldn't pause the player: " + e.getMessage());
		}
	}

	/**
	 * resume game
	 * 
	 * tells the gameloop to proceed updating the objectmanager.
	 */
	public void resumeGame() {
		paused = false;

		if (musicOn) {
			try {
				midiPlayer.start();
			} catch (MediaException e) {
				DebugMessageManager.getInstance().printError(this,
						"Couldn't restart the player:: " + e.getMessage());
			}
		}
	}

	/**
	 * starts a new game
	 * 
	 * resets all the game states  
	 */
	public void startNewGame() {
		// resets all game states
		objectManager.resetGameObjects();
		resumeGame();
		try {
			midiPlayer.setMediaTime(0);
		} catch (MediaException e) {
			DebugMessageManager.getInstance().printError(
					this,
					"Couldn't restart the player from beginning: "
							+ e.getMessage());
		}
	}

	/**
	 * gameover
	 * 
	 * tells the gameloop to show the gameover screen.
	 * 
	 * @param name playername to be displayed in gameoverscreen
	 * @param points points scored
	 */
	public void gameOver(String name, int points) {
		
		InGameMenu.getInstance().prepareGameOverScreen(name, points);
		paused = true;
		try {
			midiPlayer.stop();
		} catch (MediaException e) {
			DebugMessageManager.getInstance().printError(this,
					"Couldn't pause the player: " + e.getMessage());
		}
	}

	/**
	 * clears up resources used by the midi player and tells the thread to terminate
	 * 
	 * note: implicitly calls destroyMidiPlayer()
	 * 
	 * @see RoadRollerCanvas#destroyMidiPlayer()
	 */
	public void destroyGame() {

		destroyMidiPlayer();
		running = false;
		paused = false;
		this.midlet.notifyDestroyed();
		DebugMessageManager.getInstance().printDebug("GAME DESTROYED");
	}

	/**
	 * Destroy the MidiPlayer and release the resources
	 */
	private void destroyMidiPlayer() {

		midiPlayer.close();
		midiPlayer = null;
		DebugMessageManager.getInstance().printDebug("MIDI PLAYER DESTROYED");
	}

	/**
	 * Load the Intro from Res and Play it It can be skipped with the Fire
	 * Button
	 */
	public void playIntro() {

		try {
			InputStream is = getClass().getResourceAsStream(
					Constants.videoDir + "intro.mp4");
			vp = Manager.createPlayer(is, "video/mpeg");
			vp.addPlayerListener(this);

			vp.realize(); 

			VideoControl vc = (VideoControl) vp.getControl("VideoControl");

			if (vc == null)
				throw new RuntimeException("Video can not be played!");

			vc.initDisplayMode(VideoControl.USE_DIRECT_VIDEO, this);
			vc.setVisible(true);
			vc.setDisplayFullScreen(true);

			vp.prefetch();
			vp.start();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MediaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * From the PlayerListener in the playIntro. Check if the video reached the
	 * end
	 */
	public void playerUpdate(Player player, String event, Object eventData) {
		if (event == PlayerListener.END_OF_MEDIA) {
			skipIntro();

		}

	}

	/**
	 * called when skip button is pressed, or Intro finished stops and closes
	 * the player
	 */
	public void skipIntro() {

		introFinished = true;
		try {
			vp.stop();
		} catch (MediaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		vp.close();
		vp = null;

	}

	private void readName() {

		readNameForm = new Form("Player Name");
		text = new TextField("", "name", 10, TextField.ANY);
		readNameForm.append(text);
		start = new Command("Start", Command.OK, 1);
		readNameForm.addCommand(start);
		readNameForm.setCommandListener(this);
		RoadRoller.display.setCurrent(readNameForm);

	}

	/* (non-Javadoc)
	 * @see javax.microedition.lcdui.CommandListener#commandAction(javax.microedition.lcdui.Command, javax.microedition.lcdui.Displayable)
	 */
	public void commandAction(Command c, Displayable d) {

		if (c.equals(start) && d.equals(readNameForm)) {
			readName = false;
			RoadRoller.display.setCurrent(this);
			this.objectManager.setPlayerName(text.getString());
			DebugMessageManager.getInstance().printDebug(
					"Player Name: " + this.objectManager.getPlayerName());

		}

	}
	
	/**
	 * pauses the game and frees resources
	 */
	public void pauseGameAndFreeResources() {
//		this.pauseGame();
		this.paused=false;
		this.running= false;
		this.destroyMidiPlayer();
		this.menue.freeResources();
	}

	/**
	 * Resets resources and resumes game in last state(usually in paused state).
	 * 
	 * call this BEFORE STARTING THE THREAD
	 */
	public void resetResourcesAndResume() {
		this.paused=true;
		this.running=true;
		this.initMidiPlayer();
		this.menue.resetResources();
	}
}
