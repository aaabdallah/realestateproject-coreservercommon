package aaacs.coreserver.commons.communication;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.Vector;

//import aaacs.coreserver.administration.Configurator;
import aaacs.coreserver.commons.utilities.Scrambler;
import aaacs.coreserver.commons.validation.MsgArgsPair;
import aaacs.coreserver.commons.exceptions.CSWrapperException;

/**
 * @author Abu Abd-Allah, Nov 24, 2006
 * 
 * Encapsulates error information in a single unit.
 */
public class ErrorReport implements Serializable
{
	public enum Source
	{
		ACTIONFORMAT ("error.Source.ActionFormat"),
		DATABASE ("error.Source.Database"),
		DATARESULTS ("error.Source.DataResults"),
		INTERNAL ("error.Source.Internal"),
		PARAMETER ("error.Source.Parameter"),
		TIMEOUT ("error.Source.Timeout"),
		UNSPECIFIED ("error.Source.Unspecified");
		
		public final String value;
		Source(String v) { value = v; }
	}
	// ----- Static Members ---------------------------------------------------
	/**
	 * Serialization Version Number
	 */
	private static final long serialVersionUID = 1000L;
	
	private static boolean reportExceptions = false;
	private static String defaultExceptionsResources = null;
	private static String defaultActionResources = null;

	public static String getDefaultActionResources() { return defaultActionResources; }
	public static String getDefaultExceptionsResources() { return defaultExceptionsResources; }
	public static boolean isReportExceptions() { return reportExceptions; }
	public static void setDefaultActionResources(String defaultActionResources) 
	{ 
		ErrorReport.defaultActionResources = defaultActionResources;
	}
	public static void setDefaultExceptionsResources(String defaultExceptionsResources)
	{
		ErrorReport.defaultExceptionsResources = defaultExceptionsResources;
	}
	public static void setReportExceptions(boolean reportExceptions)
	{
		ErrorReport.reportExceptions = reportExceptions;
	}	

	// ----- Instance Members -------------------------------------------------
	/** The (major) system or layer in which the error occurred. */
	private String system = "error.System.CoreServer"; // default value
	/** Error source ("The error occurred due to a problem with: ..."). */
	private Source source = null;
	/** Error type - action specific overall title */
	private String type = null;
	private long userIdk = -1;
	private String actionName = null;
	private short actionVersion = 0;
	private long actionTime = 0;

	/*
	 * In the three data members that follow is the modeling of the following
	 * idea: with each error report, store groups of messages. Each group of
	 * messages has a unique key tied to that group. And each group of
	 * messages has an optional exception also tied to that group.
	 */
	/** 
	 * Error strings with parameterized messageArguments a la MessageFormat.
	 * Note that a "key" needs to be supplied with each group (or list) of
	 * messages (and likewise the arguments). What that key means varies
	 * depending on the *source* of the error. For example, a Parameter
	 * source error could use the keys to indicate the parameters in which
	 * the errors occurred, where the messages represent the precise details.
	 * 
	 * In general, we call each key and its related contents an ITEM. So
	 * every ErrorReport has items which vary in their meaning.
	 */
	private Map<String, List<String>> messages = 
		new LinkedHashMap<String, List<String>>();
	private Map<String, List<Object[]>> messageArguments = 
		new LinkedHashMap<String, List<Object[]>>();
	/** Exceptions that caused the errors. OBSOLETE; see right below. */
	//private List<Exception> messageExceptions = new Vector<Exception>();
	/**
	 * Some exceptions (notably PostgreSQL exceptions) are not
	 * serializable, so attaching them to the error report caused crashes.
	 * Hence, we save the exception information in "blown-up" format.
	 * For each message, we may save a chain of throwable snapshots. Note
	 * that a single throwable corresponds to a *list* of throwable
	 * snapshots - one snapshot represents one segment of the real
	 * throwable cause-chain.
	 */
	private Map<String, List<ThrowableSnapshot>> throwableChains = 
		new LinkedHashMap<String, List<ThrowableSnapshot>>();

	/* 
	 * Other possible extensions to this class:
	 * 
	 * suberror information: note: this might be a very shallow reference
	 * here as in a String referring to another ErrorReport by name (you'd
	 * need to add a name attribute to this class). But the real structure
	 * of connecting ErrorReports together is something that ought to be
	 * handled outside of this class - this is a "node" in a greater structure
	 * that needs to be designed. So this is not done yet.
	 * 
	 * location of error: not really covered as good as possible. but the
	 * stacktrace is pretty good if exceptions are turned on. So this is
	 * partially done.
	 * 
	 * ability to extend with more information. This is not done at all.
	 */

	public ErrorReport(String system, Source source, String type, long userIdk, 
		String actionName, short actionVersion, long actionTime)
	{
		this.system = system;
		this.source = source;
		this.type = type;
		this.userIdk = userIdk;
		this.actionName = actionName;
		this.actionVersion = actionVersion;
		this.actionTime = actionTime;		
	}

	/*public ErrorReport(Source source, String type, long userIdk, 
		String actionName, short actionVersion, long actionTime)
	{
		this("error.System.CoreServer", source, type, userIdk, actionName, actionVersion, actionTime);
	}*/

	public ErrorReport(String system, Source source, String type, ActionRequest request)
	{
		this(system, source, type, request.getLoginToken().getUserIdk(), request.getActionName(),
			request.getActionVersion(), request.getTimeReceived());
	}
	
	public ErrorReport(String system, Source source, String type)
	{
		this(system, source, type, -1, 
			null, (short) Short.MIN_VALUE, Long.MIN_VALUE);
	}

	public ErrorReport(String system, Source source)
	{
		this(system, source, "action.UnspecifiedErrorType", -1, 
			null, (short) Short.MIN_VALUE, Long.MIN_VALUE);
	}

	public String getSystem() { return system; }
	public ErrorReport setSystem(String system) { this.system = system; return this; }
	public Source getSource() { return source; }
	public ErrorReport setSource(Source source) { this.source = source; return this; }
	public String getType() { return type; }
	public ErrorReport setType(String type) { this.type = type; return this; }	
	public long getUserIdk() { return userIdk; }
	public ErrorReport setUserIdk(long userIdk) { this.userIdk = userIdk; return this; }
	public String getActionName() { return actionName; }
	public ErrorReport setActionName(String actionName) 
	{ this.actionName = actionName; return this; }
	public short getActionVersion() { return actionVersion; }
	public ErrorReport setActionVersion(short actionVersion) 
	{ this.actionVersion = actionVersion; return this; }
	public long getActionTime() { return actionTime; }
	public ErrorReport setActionTime(long actionTime) 
	{ this.actionTime = actionTime; return this; }

	public Set<String> getItems() { return messages.keySet(); }
	public List<String> getMessages(String item) { return messages.get(item); }
	public List<Object[]> getMessageArguments(String item) { return messageArguments.get(item); }
	public List<ThrowableSnapshot> getThrowableChain(String item) { return throwableChains.get(item); }
	
	public boolean hasItems() { return messages.size() > 0; }

	// ------------------------------------------------------------------------
	// A bunch of addItem methods to support adding different forms of 
	// information to the error.
	
	private List<String> getMsgs(List<MsgArgsPair> list)
	{
		if (list == null || list.isEmpty()) return null;
		
		List<String> results = new Vector<String>();
		for (MsgArgsPair oneMsgArgs : list)
			results.add(oneMsgArgs.getMsg());
		return results;
	}
	
	private List<Object[]> getArgs(List<MsgArgsPair> list)
	{
		if (list == null || list.isEmpty()) return null;
		
		List<Object[]> results = new Vector<Object[]>();
		for (MsgArgsPair oneMsgArgs : list)
			results.add(oneMsgArgs.getArgs());
		return results;
	}

	public ErrorReport addItem(String item, List<String> msgs,
		List<Object[]> args, Throwable t)
	{
		// *MUST* supply some item, natural or artificial
		if (item == null || item.trim().equals(""))
			return this;
		
		// remove previous entries associated with that item
		messages.remove(item);
		messageArguments.remove(item);
		throwableChains.remove(item);
		
		messages.put(item, msgs);
		messageArguments.put(item, args);

		if (t != null && reportExceptions)
		{
			List<ThrowableSnapshot> chain = new Vector<ThrowableSnapshot>();
			if (t instanceof CSWrapperException)
				t = t.getCause(); // ignore the wrapper... see the class for its raison d'etre
			while (t != null)
			{
				chain.add( new ThrowableSnapshot(t) );
				t = t.getCause();
			}
			throwableChains.put(item, chain);
		}
		return this;
	}
	
	public ErrorReport addItem(String item, List<String> msgs, List<Object[]> args)
	{
		return addItem(item, msgs, args, null);
	}
	
	public ErrorReport addItem(String item, List<MsgArgsPair> listMsgArgs)
	{
		return addItem(item, getMsgs(listMsgArgs), getArgs(listMsgArgs), null);
	}
	
	/*public ErrorReport addItem2(Map<String, List<String>> itemMessages)
	{
		if (itemMessages != null && itemMessages.size() > 0)
			for (String item : itemMessages.keySet())
				addItem(item, itemMessages.get(item));
		return this;
	}*/
	
	public ErrorReport addItem(Map<String, List<MsgArgsPair>> itemMessages)
	{
		if (itemMessages != null && itemMessages.size() > 0)
			for (String item : itemMessages.keySet())
				addItem(item, getMsgs(itemMessages.get(item)), getArgs(itemMessages.get(item)));
		return this;
	}
	
	public ErrorReport addItem(String item, String msg, Object[] msgArgs)
	{
		Vector<String> msgs = new Vector<String>();
		Vector<Object[]> args = new Vector<Object[]>();
		msgs.add(msg);
		args.add(msgArgs);
		return addItem(item, msgs, args, null);
	}

	public ErrorReport addItem(String item, MsgArgsPair msgArgs)
	{
		return addItem(item, msgArgs.getMsg(), msgArgs.getArgs());
	}

	public ErrorReport addItem(String item, String msg)
	{
		Vector<String> msgs = new Vector<String>();
		msgs.add(msg);
		return addItem(item, msgs, null, null);
	}
	
	public ErrorReport addItem(String item)
	{
		return addItem(item, null, null, null);
	}
	
	// End of addItem variants
	// ------------------------------------------------------------------------

	private String getString(ResourceBundle res, String key)
	{
		try
		{
			if (res == null) return key;
			return res.getString(key);
		}
		catch (Exception e)
		{
			return key;
		}
	}
	
	private String fmt(Locale locale, String pattern, Object[] args)
	{
		// This is for the off-chance that a null pattern is sent.
		if (pattern == null) pattern = "<no message supplied>";

		return (new MessageFormat(pattern, locale)).format(args, new StringBuffer(), null).toString();
	}

	/**
	 * Formats the errorReport into the following XML snippet:
	<code>
	<br>
	<errorReport>
		<system></system>
		<source></source>
		<type></type>
		<userIdk></userIdk>
		<actionName></actionName>
		<actionVersion></actionVersion>
		<actionTime></actionTime>
		
		<items>
			<item>*
				<name></name>
				<messages>+
					<message></message>*
				</messages>
				<throwableChain>
					<throwable type="">*
						<throwableMessage></throwableMessage>
						<reasons> (if there are any)
							<reason></reason>*
						</reasons>
						<validationResults> (if there are any)
							<entityName></entityName>
							<validationResult>*
								<fieldName></fieldName>
								<results>
									<result></result>*
								</results>
							</validationResult>
						</validationResults>
						<stackTrace>
						</stackTrace>
					</throwable>
				</throwableChain>
			</item>
		</items>

	</errorReport>
	</br>
	</code>
	 */
	public String format(Locale locale)
	{
		ResourceBundle exceptionsRes = null;
		ResourceBundle actionRes = null;
		
		try // it is imperative that this method work regardless of localization errors
		{
			exceptionsRes =
				ResourceBundle.getBundle(defaultExceptionsResources, locale);
			actionRes =
				ResourceBundle.getBundle(defaultActionResources, locale);
		}
		catch (Exception e) {} // the error will show up on screen obviously

		StringBuilder result = new StringBuilder("<errorReport>\n");
		try
		{
			if (system != null)
				result.append("\t<system>" + getString(exceptionsRes, system) + "</system>\n");
			if (source != null)
				result.append("\t<source>" + getString(exceptionsRes, source.value) + "</source>\n");
			if (type != null)
				result.append("\t<type>" + getString(exceptionsRes, type) + "</type>\n");
			if (userIdk != -1)
				result.append("\t<userIdk>" + Scrambler.scramble(userIdk) + "</userIdk>\n");
			if (actionName != null)
				result.append("\t<actionName>" + getString(actionRes, actionName) + "</actionName>\n");
			if (actionVersion != Short.MIN_VALUE)
				result.append("\t<actionVersion>" + NumberFormat.getNumberInstance(locale).format(actionVersion) + "</actionVersion>\n");
			if (actionTime != Long.MIN_VALUE)
				result.append("\t<actionTime>" + 
					DateFormat.getDateTimeInstance(DateFormat.MEDIUM, 
						DateFormat.MEDIUM, locale).format(new Date(actionTime))
					+ "</actionTime>\n");

			if (messages.size() > 0)
			{
				result.append("\t<items>\n");
				int i = 0;
				for (String item : messages.keySet())
				{
					result.append("\t\t<item>\n");
					result.append("\t\t\t<name>" + item + "</name>\n");
					
					List<String> msgs = messages.get(item);
					if (msgs != null && msgs.size() > 0)
					{
						result.append("\t\t\t<messages>\n");
						int msgCntr = 0;
						List<Object[]> args = messageArguments.get(item);
						Object[] oneMsgArgs = null;
						for (String msg : msgs)
						{
							oneMsgArgs = null;
							if (args != null)
								oneMsgArgs = args.get(msgCntr);
							result.append("\t\t\t\t<message>" + 
								fmt(locale, getString(exceptionsRes, msg), oneMsgArgs) +
								"</message>\n");
							msgCntr++;
						}
						result.append("\t\t\t</messages>\n");
					}

					if (throwableChains.get(item) != null)
					{
						result.append("\t\t\t<throwableChain>\n");

						List<ThrowableSnapshot> chain = (List<ThrowableSnapshot>) throwableChains.get(item);
						for (ThrowableSnapshot ts : chain)
						{
							result.append("\t\t\t\t<throwable type=\"" + ts.getClassName() + "\">\n");
							result.append("\t\t\t\t\t<throwableMessage>");
							result.append(fmt(locale, getString(exceptionsRes, ts.getMessage()), 
								ts.getMessageArguments()));
							result.append("</throwableMessage>\n");
							
							if (ts.getReasons() != null)
							{
								result.append("\t\t\t\t\t<reasons>\n");
								List<String> reasons = ts.getReasons();
								List<Object[]> reasonsArguments = ts.getReasonsArguments();
								
								int j = 0;
								for (String reason : reasons)
								{
									Object[] reasonArguments = reasonsArguments.get(j);
									result.append("\t\t\t\t\t\t<reason>");
									result.append(fmt(locale, getString(exceptionsRes, reason), reasonArguments));
									result.append("\t\t\t\t\t\t</reason>\n");
									j++;
								}
								result.append("\t\t\t\t\t</reasons>\n");
							}
							
							if (ts.getValidatedEntity() != null)
							{
								Map<String, List<MsgArgsPair>> vldResults = ts.getValidationResults();
	
								ResourceBundle entityRes = null;
								try // it is imperative that this method work regardless of localization errors
								{
									entityRes =
										ResourceBundle.getBundle(ts.getValidationResourceFile(), locale);
								}
								catch (Exception e2) {} // the error will show up on screen obviously
	
								result.append("\t\t\t\t\t<entityName>" + 
									getString(entityRes, ts.getValidatedEntity()) + "</entityName>\n");
								if (vldResults != null && vldResults.size() > 0)
								{
									result.append("\t\t\t\t\t<validationResults>\n");
									int j = 0;
									for (String fieldName : vldResults.keySet())
									{
										result.append("\t\t\t\t\t\t<validationResult>\n");
										result.append("\t\t\t\t\t\t\t<fieldName>" + 
											getString(entityRes, ts.getValidatedEntity()+"."+fieldName) + "</fieldName>\n");
	
										List<MsgArgsPair> fieldVldResults = vldResults.get(fieldName);
										result.append("\t\t\t\t\t\t\t<results>\n");
										for (MsgArgsPair fieldVldResult : fieldVldResults)
										{
											result.append("\t\t\t\t\t\t\t\t<result>" +
												fmt(locale, getString(exceptionsRes, fieldVldResult.getMsg()), fieldVldResult.getArgs()) +
												"</result>\n");
										}
										result.append("\t\t\t\t\t\t\t</results>\n");
										result.append("\t\t\t\t\t\t</validationResult>\n");
										j++;
									}								
									result.append("\t\t\t\t\t</validationResults>\n");								
								}
							}
							
							if (ts.getStackTrace() != null)
							{
								result.append("\t\t\t\t\t<stackTrace>\n");
								for (String ste : ts.getStackTrace())
									//result.append("\t\t\t\t\t\t" + ste + "\n");
									result.append(ste + "\n");
								result.append("\t\t\t\t\t</stackTrace>\n");
							}
							result.append("\t\t\t\t</throwable>\n");
						}
						result.append("\t\t\t</throwableChain>\n");
					}

					result.append("\t\t</item>\n");
					i++;
				}
				result.append("\t</items>\n");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		result.append("</errorReport>");
		return result.toString();
	}
}
