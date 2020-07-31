package aaacs.coreserver.commons.exceptions;

import java.io.Serializable;

import javax.ejb.ApplicationException;

/**
 * @author Abu Abd-Allah
 * According to the EJB 3.0 specification there are certain points where
 * only RuntimeExceptions are allowed. The problem is that those points
 * are very *interesting* points, and being able to throw other kinds of
 * exceptions would be useful. So this thin class is simply to allow that
 * facility, and the component that receives this exception should remove
 * the wrapper.
 */
@ApplicationException
public class CSWrapperException extends RuntimeException implements Serializable
{
	// ----- Static Members ---------------------------------------------------
	/**
	 * Serialization Version Number
	 */
	private static final long serialVersionUID = 1000L;

	public CSWrapperException(Throwable t)
	{
		super(t);
	}
}
