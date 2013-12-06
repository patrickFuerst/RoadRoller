package roadroller.menu;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.m2g.SVGImage;
import javax.microedition.m2g.ScalableImage;

import org.w3c.dom.Document;
import org.w3c.dom.svg.SVGElement;

import roadroller.helper.Constants;
import roadroller.helper.DebugMessageManager;
import roadroller.helper.L10nResources;

/**
 * class representing the help screen.
 * 
 * the class reads the according SVG file and replaces its contents by the according l10n translations.
 *  
 * @author Felix
 *
 */
public class HelpScreen {
	
	private ScalableImage scalableImage;
	private Document doc;
	
	/**
	 * initializes the screen according to the given resource file.
	 * 
	 * @param res must not be null!
	 */
	public HelpScreen(L10nResources res) {
		initSVG();
		initText(res);
	}
	
	private void initSVG() {
		InputStream is = getClass().getResourceAsStream(
				Constants.svgDir + "helpScreen.svg");
		try {
			scalableImage = SVGImage.createImage(is, null);
			is.close();
			doc = ((SVGImage) scalableImage).getDocument();
		} catch (IOException e) {
			DebugMessageManager.getInstance().printError(this, e.getMessage());
		}
		
	}
	
	private void initText(L10nResources res) {
		SVGElement e;
		e = (SVGElement) doc.getElementById("title");
		e.setTrait("#text", res.getString("HELP"));
		e = (SVGElement) doc.getElementById("line1");
		e.setTrait("#text", res.getString("HELPLINE1"));
		e = (SVGElement) doc.getElementById("line2");
		e.setTrait("#text", res.getString("HELPLINE2"));
		e = (SVGElement) doc.getElementById("line3");
		e.setTrait("#text", res.getString("HELPLINE3"));
		e = (SVGElement) doc.getElementById("line4");
		e.setTrait("#text", res.getString("HELPLINE4"));
		e = (SVGElement) doc.getElementById("line5");
		e.setTrait("#text", res.getString("HELPLINE5"));
		e = (SVGElement) doc.getElementById("line6");
		e.setTrait("#text", res.getString("HELPLINE6"));
		e = (SVGElement) doc.getElementById("line7");
		e.setTrait("#text", res.getString("HELPLINE7"));
		e = (SVGElement) doc.getElementById("line8");
		e.setTrait("#text", res.getString("HELPLINE8"));
		e = (SVGElement) doc.getElementById("line9");
		e.setTrait("#text", res.getString("HELPLINE9"));
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
