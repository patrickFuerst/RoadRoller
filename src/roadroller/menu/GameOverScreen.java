package roadroller.menu;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.m2g.SVGImage;
import javax.microedition.m2g.ScalableImage;

import org.w3c.dom.Document;
import org.w3c.dom.svg.SVGElement;

import roadroller.helper.Constants;
import roadroller.helper.DebugMessageManager;

/**
 * This class holds the GameOver Screen
 * 
 * the class reads the according SVG file and manipulates it according to the playername and the score.
 * 
 * @author Felix
 *
 */
public class GameOverScreen {

	private ScalableImage scalableImage;
	private Document doc;
	
	/**
	 * Initializes to screen.
	 * 
	 * @param playerName the playername to be displayed
	 * @param scoredPoints the score to be displayed
	 */
	public GameOverScreen(String playerName, int scoredPoints) {
		initSVG();
		initScore(playerName, scoredPoints);
	}
	
	private void initScore(String name, int points) {
		SVGElement e;
		
		e = (SVGElement) doc.getElementById("playername");
		e.setTrait("#text", name + " scored");
		e = (SVGElement) doc.getElementById("playerscore");
		e.setTrait("#text", Integer.toString(points));
		
	}

	private void initSVG() {
		InputStream is = getClass().getResourceAsStream(
				Constants.svgDir + "gameOver.svg");
		try {
			scalableImage = SVGImage.createImage(is, null);
			is.close();
			doc = ((SVGImage) scalableImage).getDocument();
		} catch (IOException e) {
			DebugMessageManager.getInstance().printError(this, e.getMessage());
		}
		
	}

	/**
	 * Use this ScalableImage to draw the GameOver-screen.
	 * 
	 * @return the screen as a ScalableImage
	 */
	public ScalableImage getScalableImage() {
		return this.scalableImage;
	}
}
