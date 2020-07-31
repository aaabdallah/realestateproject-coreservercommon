package aaacs.coreserver.commons.logging;

import java.util.logging.Logger;

/**
 * @author Ahmed A. Abd-Allah
 * Created on Oct 30, 2006
 *
 * Logger augmented with Core Server's specific log levels.
 */
public class CSLogger extends Logger
{
	public CSLogger(String name, String resourceBundleName)
	{
		super(name, resourceBundleName);
	}
	
	public void fatalError(String msg, Object[] params)
	{
		log(CSLogLevel.FATALERROR, msg, params);
	}

	public void fatalError(String msg, Throwable t)
	{
		log(CSLogLevel.FATALERROR, msg, t);
	}

	public void fatalError(String msg)
	{
		log(CSLogLevel.FATALERROR, msg);
	}

	public void error(String msg, Object[] params)
	{
		log(CSLogLevel.ERROR, msg, params);
	}

	public void error(String msg, Throwable t)
	{
		log(CSLogLevel.ERROR, msg, t);
	}

	public void error(String msg)
	{
		log(CSLogLevel.ERROR, msg);
	}

	public void warning(String msg, Object[] params)
	{
		log(CSLogLevel.WARNING, msg, params);
	}

	public void warning(String msg, Throwable t)
	{
		log(CSLogLevel.WARNING, msg, t);
	}

	public void warning(String msg)
	{
		log(CSLogLevel.WARNING, msg);
	}

	public void alert(String msg, Object[] params)
	{
		log(CSLogLevel.ALERT, msg, params);
	}

	public void alert(String msg, Throwable t)
	{
		log(CSLogLevel.ALERT, msg, t);
	}

	public void alert(String msg)
	{
		log(CSLogLevel.ALERT, msg);
	}

	public void information(String msg, Object[] params)
	{
		log(CSLogLevel.INFORMATION, msg, params);
	}

	public void information(String msg, Throwable t)
	{
		log(CSLogLevel.INFORMATION, msg, t);
	}

	public void information(String msg)
	{
		log(CSLogLevel.INFORMATION, msg);
	}

	public void details(String msg, Object[] params)
	{
		log(CSLogLevel.DETAILS, msg, params);
	}

	public void details(String msg, Throwable t)
	{
		log(CSLogLevel.DETAILS, msg, t);
	}

	public void details(String msg)
	{
		log(CSLogLevel.DETAILS, msg);
	}

	public void debug(String msg, Object[] params)
	{
		log(CSLogLevel.DEBUG, msg, params);
	}

	public void debug(String msg, Throwable t)
	{
		log(CSLogLevel.DEBUG, msg, t);
	}

	public void debug(String msg)
	{
		log(CSLogLevel.DEBUG, msg);
	}

	public void trace(String msg, Object[] params)
	{
		log(CSLogLevel.TRACE, msg, params);
	}

	public void trace(String msg, Throwable t)
	{
		log(CSLogLevel.TRACE, msg, t);
	}

	public void trace(String msg)
	{
		log(CSLogLevel.TRACE, msg);
	}
}
