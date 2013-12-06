package roadroller.menu;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.m2g.SVGImage;
import javax.microedition.m2g.ScalableImage;

import org.w3c.dom.Document;
import org.w3c.dom.svg.SVGElement;

import roadroller.helper.Constants;
import roadroller.helper.DebugMessageManager;
import roadroller.score.Score;

/**
 * class representing the Highscore screen.
 * 
 * the class reads the according SVG file and manipulates it according to the scores stored by recordstore class (roadroller.score.Score)
 * 
 * @see Score
 * 
 * @author Felix
 *
 */
public class highscoreMenu {
	private ScalableImage scalableImage;
	private Document doc;
	
	/**
	 * initializes the Highscore Screen.
	 * 
	 */
	public highscoreMenu() {
		initSVG();
		setScores();
	}
	
	private void initSVG() {
		InputStream is = getClass().getResourceAsStream(
				Constants.svgDir + "highscore.svg");
		try {
			scalableImage = SVGImage.createImage(is, null);
			is.close();
			doc = ((SVGImage) scalableImage).getDocument();
		} catch (IOException e) {
			DebugMessageManager.getInstance().printError(this, e.getMessage());
		}
		
	}
	
	private void setScores() {
		Score score = Score.getInstance();
		String[] names = score.getNames();
		int[] scores = score.getValues();
		//read the first 5 scores
		SVGElement e;
		
		e = (SVGElement) doc.getElementById("firstName");
		e.setTrait("#text", "1st: "+ names[0]);
		e = (SVGElement) doc.getElementById("firstScore");
		e.setTrait("#text", Integer.toString(scores[0]));
		
		e = (SVGElement) doc.getElementById("secondName");
		e.setTrait("#text", "2nd: "+ names[1]);
		e = (SVGElement) doc.getElementById("secondScore");
		e.setTrait("#text", Integer.toString(scores[1]));
		
		e = (SVGElement) doc.getElementById("thirdName");
		e.setTrait("#text", "3rd: "+ names[2]);
		e = (SVGElement) doc.getElementById("thirdScore");
		e.setTrait("#text", Integer.toString(scores[2]));
		
		e = (SVGElement) doc.getElementById("fourthName");
		e.setTrait("#text", "4th: "+ names[3]);
		e = (SVGElement) doc.getElementById("fourthScore");
		e.setTrait("#text", Integer.toString(scores[3]));
		
		e = (SVGElement) doc.getElementById("fifthName");
		e.setTrait("#text", "5th: "+ names[4]);
		e = (SVGElement) doc.getElementById("fifthScore");
		e.setTrait("#text", Integer.toString(scores[4]));
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
