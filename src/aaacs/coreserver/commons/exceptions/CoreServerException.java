package aaacs.coreserver.commons.exceptions;

import java.io.Serializable;
import java.util.List;
import java.util.Vector;

/**
 * @author aaa
 * Created on Aug 19, 2003 at 12:41:28 AM.
 *
 * General-purpose exception class for all Core Server exceptions. Small
 * extra ability to attach reasons to the exception.
 */
public class CoreServerException extends Exception implements Serializable
{
	/*
	 * + Error message
	 * + Reasons
	 * Runtime phase
	 * Class, line
	 * + Stack Trace
	 * Layer
	 */
	// ----- Static members ---------------------------------------------------
	/**
	 * Serialization Version Number
	 */
	private static final long serialVersionUID = 1000L;

	// ----- Instance members -------------------------------------------------
	/**
	 * Store the reasons behind the exception
	 */
	private List<String> reasons = null;
	private List<Object[]> reasonsArguments = null;

	/**
	 * Store the arguments (if any) for localizing the message later.
	 */
	private Object[] messageArguments = null;

	public CoreServerException(String message, Object[] arguments, 
		Throwable cause, List<String> initialReasons, 
		List<Object[]> initialReasonsArguments)
	{
		super(message, cause);
		messageArguments = arguments;
		addReasons(initialReasons, initialReasonsArguments);
	}

	public CoreServerException(String message, Object[] arguments, Throwable cause)
	{
		this(message, arguments, cause, null, null);
	}

	public CoreServerException(String message, Throwable cause)
	{
		this(message, null, cause, null, null);
	}
	
	public CoreServerException(String message, Object[] arguments)
	{
		this(message, arguments, null, null, null);
	}

	public CoreServerException(String message)
	{
		this(message, null, null, null, null);
	}

	public boolean hasMessageArguments()
	{
		return messageArguments != null;
	}

	public Object[] getMessageArguments()
	{
		return messageArguments;
	}

	public boolean hasReasons()
	{
		return (reasons != null && !reasons.isEmpty());
	}

	public List<String> getReasons()
	{
		return reasons;
	}

	public List<Object[]> getReasonsArguments()
	{
		return reasonsArguments;
	}

	private void addReasons(List<String> additionalReasons,
		List<Object[]> additionalReasonsArguments)
	{
		if (additionalReasons != null && additionalReasons.size() > 0)
		{
			if (additionalReasonsArguments != null &&
				additionalReasonsArguments.size() != additionalReasons.size())
				return;

			boolean noArgs = true;
			if (additionalReasonsArguments != null)
				noArgs = false;
			
			if (reasons == null)
				reasons = new Vector<String>();

			int i=0;
			for (String reason : additionalReasons)
			{
				if (noArgs)
					addReason(reason, null);
				else
					addReason(reason, additionalReasonsArguments.get(i));
				i++;
			}
		}
	}
	
	private void addReason(String reason, Object[] arguments)
	{
		if (reason == null) 
			return;
		if (reasons == null)
			reasons = new Vector<String>();
		reasons.add(reason.trim());
		reasonsArguments.add(arguments);
	}
}
