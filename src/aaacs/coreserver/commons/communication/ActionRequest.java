package aaacs.coreserver.commons.communication;

import java.io.Serializable;
import java.util.Locale;

/**
 * @author Ahmed A. Abd-Allah
 * Created on Aug 20, 2003 at 8:41:16 PM.
 * 
 * Represents the action request which is sent to the Core Server from the
 * "outside". This is a coarse-grained object.
 */
public class ActionRequest extends ActionCommunication implements Serializable
{
	// ----- Static Data Members ----------------------------------------------
	/**
	 * Serialization Version Number
	 */
	private static final long serialVersionUID = 1000L;

	// ----- Instance members -------------------------------------------------
	private String actionName;
	private short actionVersion;

	public ActionRequest(Locale inLocale, LoginToken inLoginToken, 
		Parameters inParameters, 
		String inActionName, short inActionVersion)
	{
		super(inLocale, inLoginToken, inParameters);

		setActionName(inActionName);
		setActionVersion(inActionVersion);
	}
	
	public ActionRequest()
	{
		super();
		setActionName(null);
		setActionVersion((short) 0);
	}

	public boolean hasWellFormattedActionNameVersion()
	{
		return (actionName != null && actionName.length() > 0 && 
			actionVersion > 0);
	}

	public String getActionName() { return actionName; }
	public short getActionVersion() { return actionVersion; }

	public void setActionName(String string) { actionName = string; }
	public void setActionVersion(short s) { actionVersion = s; }
}
