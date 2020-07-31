package aaacs.coreserver.commons.generation;

public class GenerationException extends Exception
{
	/**
	 * Serialization Version Number
	 */
	private static final long serialVersionUID = 1000L;
	
	public GenerationException()
	{
	}
	
	public GenerationException(String message)
	{
		super(message);
	}

	public GenerationException(Throwable t)
	{
		super(t);
	}
}
