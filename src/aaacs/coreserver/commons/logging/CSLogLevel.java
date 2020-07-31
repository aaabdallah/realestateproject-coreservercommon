package aaacs.coreserver.commons.logging;

import java.util.ResourceBundle;
import java.util.logging.Level;

/**
 * @author Ahmed A. Abd-Allah
 * Created on Oct 29, 2006
 *
 * Add specific logging levels to the Core Server.
 */
public class CSLogLevel extends java.util.logging.Level
{
	// ----- Static Data Members ----------------------------------------------
	/**
	 * Serialization Version Number
	 */
	private static final long serialVersionUID = 1000L;

	public static CSLogLevel FATALERROR = null;
	public static CSLogLevel ERROR = null;
	public static CSLogLevel WARNING = null;
	public static CSLogLevel ALERT = null;
	public static CSLogLevel INFORMATION = null;
	public static CSLogLevel DETAILS = null;
	public static CSLogLevel DEBUG = null;
	public static CSLogLevel TRACE = null;

	//public static void initializeNewLevels(String resourceBundleName)
	static
	{
		ResourceBundle res = ResourceBundle.getBundle("aaacs.coreserver.commons.resources.loglevels");
		FATALERROR = new CSLogLevel(res.getString("logging.FatalErrorLevel"), 60000);
		ERROR = new CSLogLevel(res.getString("logging.ErrorLevel"), 50000);
		WARNING = new CSLogLevel(res.getString("logging.WarningLevel"), 45000);
		ALERT = new CSLogLevel(res.getString("logging.AlertLevel"), 40000);
		INFORMATION = new CSLogLevel(res.getString("logging.InformationLevel"), 30000);
		DETAILS = new CSLogLevel(res.getString("logging.DetailsLevel"), 25000);
		DEBUG = new CSLogLevel(res.getString("logging.DebugLevel"), 20000);
		TRACE = new CSLogLevel(res.getString("logging.TraceLevel"), 10000);
	}

	
	public static Level parse(String level)
	{
		return Level.parse(level);
	}

	/**
	 * @param name
	 * @param value
	 */
	public CSLogLevel(String name, int value)
	{
		super(name, value);
	}

	/**
	 * @param name
	 * @param value
	 * @param resourceBundleName
	 */
	public CSLogLevel(String name, int value, String resourceBundleName)
	{
		super(name, value, resourceBundleName);
	}
}
