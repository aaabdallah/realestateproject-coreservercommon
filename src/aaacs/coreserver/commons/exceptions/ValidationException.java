package aaacs.coreserver.commons.exceptions;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import aaacs.coreserver.commons.validation.MsgArgsPair;

/**
 * @author Abu Abd-Allah
 * 
 * An exception for validation problems of entities.
 */
public class ValidationException extends CoreServerException implements Serializable
{
	// ----- Static members ---------------------------------------------------
	/**
	 * Serialization Version Number
	 */
	private static final long serialVersionUID = 1000L;

	// ----- Instance members -------------------------------------------------
	/**
	 * The classname of the entity which was being validated.
	 */
	private String entityName = null;
	/**
	 * The resources file of the entity which was being validated.
	 */
	private String entityResourceFile = null;
	/**
	 * Store the validation results that caused this exception.
	 */
	private Map<String, List<MsgArgsPair>> validationResults = null;

	public ValidationException(String inEntityName, String inEntityResourceFile, 
		String message, Object[] arguments,
		Map<String, List<MsgArgsPair>> validationResults)
	{
		super(message, arguments);
		entityName = inEntityName;
		entityResourceFile = inEntityResourceFile;
		if (validationResults != null && validationResults.size() > 0)
			this.validationResults = validationResults;
	}
	
	public ValidationException(String inEntityName, String inEntityResourceFile,
		String message, Map<String, List<MsgArgsPair>> validationResults)
	{
		this(inEntityName, inEntityResourceFile, message, null, validationResults);
	}
	
	public String getEntityName()
	{
		return entityName;
	}

	public String getEntityResourceFile()
	{
		return entityResourceFile;
	}

	public Map<String, List<MsgArgsPair>> getValidationResults()
	{
		return validationResults;
	}
}
