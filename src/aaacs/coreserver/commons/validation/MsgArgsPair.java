package aaacs.coreserver.commons.validation;

import java.io.Serializable;

/**
 * For encapsulating an unlocalized message with its potential arguments (to be
 * used with <code>java.text.MessageFormat</code>).
 * @author Abu Abd-Allah
 */
public class MsgArgsPair implements Serializable
{
	// ----- Static Members ---------------------------------------------------
	/**
	 * Serialization Version Number
	 */
	private static final long serialVersionUID = 1000L;

	// ----- Instance Members -------------------------------------------------
	private String msg;
	private Object[] args;
	
	public MsgArgsPair(String msg, Object[] args)
	{
		this.msg = msg;
		this.args = args;
	}

	public MsgArgsPair(String msg)
	{
		this(msg, null);
	}

	public MsgArgsPair()
	{
		this(null, null);
	}
	
	public Object[] getArgs()
	{
		return args;
	}
	public String getMsg()
	{
		return msg;
	}
	public void setArgs(Object[] args)
	{
		this.args = args;
	}
	public void setMsg(String msg)
	{
		this.msg = msg;
	}
}
