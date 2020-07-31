package aaacs.coreserver.commons.communication;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import aaacs.coreserver.commons.validation.MsgArgsPair;
import aaacs.coreserver.commons.exceptions.CoreServerException;
import aaacs.coreserver.commons.exceptions.ValidationException;

/**
 * @author Abu Abd-Allah
 * This is an unfortunate class. It is due to my understanding that some
 * exceptions - notably some PostgreSQL exceptions - are NOT serializable, 
 * and hence they cannot be sent out over the wire with an ErrorReport. 
 * Hence, this class is simply a snapshot of that throwable's contents. It
 * is compatible with the extra information in CoreServerExceptions and
 * ValidationExceptions.
 * 
 * NOTE: this class does NOT save the entire throwable chain. It is the
 * user's responsibility to loop over the chain and create a new snapshot
 * for whatever cause seems interesting.
 */
public class ThrowableSnapshot implements Serializable
{
	/**
	 * Serialization Version Number
	 */
	private static final long serialVersionUID = 1000L;

	private String className = null;
	private String message = null;
	private Object[] messageArguments = null;
	private String[] stackTrace = null;
	private List<String> reasons = null;
	private List<Object[]> reasonsArguments = null;
	private String validatedEntity = null;
	private String validationResourceFile = null;
	private Map<String, List<MsgArgsPair>> validationResults = null;

	public ThrowableSnapshot(Throwable t)
	{
		className = t.getClass().getName();
		message = t.getMessage() == null ? "No message supplied" : t.getMessage();
		stackTrace = traceToStringArray(t, 0); // don't get causes at all
		if (t instanceof CoreServerException)
		{
			messageArguments = ((CoreServerException) t).getMessageArguments();
			reasons = ((CoreServerException) t).getReasons();
			reasonsArguments = ((CoreServerException) t).getReasonsArguments();
		}
		if (t instanceof ValidationException)
		{
			validatedEntity = ((ValidationException) t).getEntityName();
			validationResourceFile = ((ValidationException) t).getEntityResourceFile();
			validationResults = ((ValidationException) t).getValidationResults();
		}
	}

	
	public String getMessage()
	{
		return message;
	}


	public Object[] getMessageArguments()
	{
		return messageArguments;
	}


	public String getClassName()
	{
		return className;
	}


	public List<String> getReasons()
	{
		return reasons;
	}


	public List<Object[]> getReasonsArguments()
	{
		return reasonsArguments;
	}


	public String[] getStackTrace()
	{
		return stackTrace;
	}


	public String getValidatedEntity()
	{
		return validatedEntity;
	}


	public String getValidationResourceFile()
	{
		return validationResourceFile;
	}


	public Map<String, List<MsgArgsPair>> getValidationResults()
	{
		return validationResults;
	}


	public void setMessage(String exceptionMessage)
	{
		this.message = exceptionMessage;
	}


	public void setMessageArguments(Object[] exceptionMessageArguments)
	{
		this.messageArguments = exceptionMessageArguments;
	}


	public void setClassName(String exceptionName)
	{
		this.className = exceptionName;
	}


	public void setReasons(List<String> exceptionReasons)
	{
		this.reasons = exceptionReasons;
	}


	public void setReasonsArguments(
		List<Object[]> exceptionReasonsArguments)
	{
		this.reasonsArguments = exceptionReasonsArguments;
	}


	public void setStackTrace(String[] exceptionTrace)
	{
		this.stackTrace = exceptionTrace;
	}


	public void setValidatedEntity(String validatedEntity)
	{
		this.validatedEntity = validatedEntity;
	}


	public void setValidationResourceFile(String validationResourceFile)
	{
		this.validationResourceFile = validationResourceFile;
	}


	public void setValidationResults(
		LinkedHashMap<String, List<MsgArgsPair>> validationResults)
	{
		this.validationResults = validationResults;
	}


	/* 
	 * The code handles causes for historical reasons; seemed like a shame to
	 * remove it. So the causeDepthToTrace was added.
	 */
	private String[] traceToStringArray(Throwable throwable, int causeDepthToTrace)
	{
		if (throwable == null || 
			throwable.getStackTrace() == null) return null;

		Throwable t = throwable;
		int i = 0, depth = 0;
		while (t != null && depth <= causeDepthToTrace)
		{
			i += t.getStackTrace().length;
			t = t.getCause();
			i++; // for the header message itself
			depth++;
		}

		if (i != 0)
		{
			String[] entireStack = new String[i];
			i = 0;
			depth = 0;
			
			t = throwable;
			boolean first = true;
			while (t != null && depth <= causeDepthToTrace)
			{
				entireStack[i++] = (first ? "Exception: " : "Caused by: ") + t.getClass().getName() + ": " + t.getMessage();
				for (StackTraceElement ste : t.getStackTrace())
					entireStack[i++] = "\tat " + ste.toString();
				t = t.getCause();
				first = false;
				depth++;
			}
			return entireStack;
		}
		return null;
	}
}
