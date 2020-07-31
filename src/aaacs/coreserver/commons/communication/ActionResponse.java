package aaacs.coreserver.commons.communication;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

/**
 * @author Ahmed A. Abd-Allah
 * Created on Aug 20, 2003 at 8:41:16 PM.
 * 
 * Represents the action response which is sent from the Core Server to the
 * "outside". This is a coarse-grained object.
 */
public class ActionResponse extends ActionCommunication implements Serializable
{
	// ----- Static Data Members ----------------------------------------------
	/**
	 * Serialization Version Number
	 */
	private static final long serialVersionUID = 1000L;

	// ----- Instance members -------------------------------------------------
	private List<ErrorReport> errorReports = null;

	public ActionResponse(Locale inLocale, LoginToken inLoginToken, 
		Parameters inParameters, 
		List<ErrorReport> inErrorReports)
	{
		super(inLocale, inLoginToken, inParameters);
		addErrorReports(inErrorReports);
	}
	
	public ActionResponse()
	{
		super();
		errorReports = null;
	}

	public boolean hasErrorReports()
	{
		return (errorReports != null && !errorReports.isEmpty());
	}

	public void addErrorReport(ErrorReport errorReport)
	{
		if (errorReports == null)
			errorReports = new Vector<ErrorReport>();

		if (errorReport != null)
			errorReports.add(errorReport);
	}

	public void addErrorReports(List<ErrorReport> reports)
	{
		if (reports == null) return;

		for (ErrorReport report : reports)
			addErrorReport(report);
	}
	public List<ErrorReport> getErrorReports()
	{
		if (errorReports.isEmpty())
			return null;
		return errorReports;
	}
}
