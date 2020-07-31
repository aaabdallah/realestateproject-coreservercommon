package aaacs.coreserver.commons.communication;

import java.io.Serializable;
import java.util.Locale;

/**
 * @author Ahmed A. Abd-Allah
 * Created on Nov 17, 2003 at 12:31:40 PM.
 * 
 * Base class for any communication that occurs to/from the Core Server. This class
 * and its derived classes are responsible for localizing strings that have been
 * identified for localization from previous classes. Another responsibility of
 * this class and its children is the scrambling and descrambling of primary and
 * foreign keys (if supported) within passed in/out entities, as well as in login
 * tokens.
 * 
 * CORRECTION: it remains to be seen whether the responsibities mentioned above
 * are the domain of the delegate layer.
 * 
 * Note: the locale in this class is guaranteed to be set to some sane value; it
 * ought to never be null. The default is always <code>Locale.US</code>.
 */
public abstract class ActionCommunication implements Serializable
{
	// ----- Static members ---------------------------------------------------
	private static Locale defaultLocale = Locale.US;
	public static void setDefaultLocale(Locale newDefaultLocale)
	{
		if (newDefaultLocale != null)
			defaultLocale = newDefaultLocale;
		else
			defaultLocale = Locale.US;
	}

	// ----- Instance members -------------------------------------------------
	private LoginToken loginToken;
	private Locale locale;
	private Parameters parameters;
	// A transmission counter
	private byte txCounter = 0;
	private long timeReceived = 0;
	private long timeCreated = System.currentTimeMillis();

	public ActionCommunication(Locale inLocale, LoginToken inLoginToken, 
		Parameters inParameters)
	{
		setLoginToken(inLoginToken);
		if (inLocale != null)
			locale = inLocale;
		else
			locale = defaultLocale;
		setParameters(inParameters);
	}

	public ActionCommunication()
	{
		this(defaultLocale, null, null);
	}
	
	public Locale getLocale() { return locale; }
	public LoginToken getLoginToken() { return loginToken; }
	public byte getTxCounter() { return txCounter; }
	public long getTimeCreated() { return timeCreated; }
	public long getTimeReceived() { return timeReceived; }

	public void setLocale(Locale l) { if (l!=null) locale = l; }
	public void setLoginToken(LoginToken lt) 
	{ 
		if (lt != null) loginToken = lt;
		else loginToken = new LoginToken();
	}
	public void incrementTxCounter() { txCounter++; }
	// No setter for timeCreated obviously
	public void markTimeReceived() { timeReceived = System.currentTimeMillis(); }

	public Parameters getParameters()
	{
		//This was a *bad* idea: it is against tradition, and so was hard to debug.
		//if (parameters.size() == 0)
		//	return null;
		return parameters;
	}

	public void setParameters(Parameters inParameters)
	{
		parameters = inParameters;
	}
	
	public boolean hasParameters()
	{
		return (parameters != null && parameters.size() > 0);
	}

	public void clearParameters()
	{
		parameters = null;
	}
}
