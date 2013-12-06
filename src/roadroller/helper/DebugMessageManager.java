package roadroller.helper;

/**
 * Message Logger
 * 
 * enable or disable Message printing to standard output. 
 * 
 * @author Felix
 *
 */
public class DebugMessageManager {

	private static DebugMessageManager singleton = null;
	private boolean printErrors;
	private boolean printDebug;

	private DebugMessageManager() {
		super();
		this.printDebug = false;
		this.printErrors = false;
	}

	public static DebugMessageManager getInstance() {
		if (singleton == null) {
			singleton = new DebugMessageManager();
		}
		return singleton;
	}

	public void printError(Object c, String errorMessage) {
		System.err.println("ERROR in " + c.getClass().getName() + ": "
				+ errorMessage);
	}

	public void printDebug(String debugMessage) {
		System.out.println("DEBUG: " + debugMessage);
	}

	public boolean isPrintingErrors() {
		return printErrors;
	}

	public void setPrintErrors(boolean printErrors) {
		this.printErrors = printErrors;
	}

	public boolean isPrintingDebug() {
		return printDebug;
	}

	public void setPrintDebug(boolean printDebug) {
		this.printDebug = printDebug;
	}

}
