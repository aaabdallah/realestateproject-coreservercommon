package aaacs.coreserver.commons.communication;

import java.io.Serializable;

/**
 * @author aaa
 * Created on Aug 20, 2003 at 6:08:21 AM.
 *
 */
public class LoginToken implements Serializable
{
	// ----- Static members ---------------------------------------------------
	/**
	 * Serialization Version Number
	 */
	private static final long serialVersionUID = 1000L;
	
	/**
	 * This default value of a 20 minute timeout (expressed in milliseconds)
	 * should be overwritten by some value from the database. 
	 */
	private static int inactivityTimeout = 1000 * 60 * 20;
	public static int getInactivityTimeout() { return inactivityTimeout; }
	public static void setInactivityTimeout(int timeout)
	{
		if (timeout < 1000 * 60 * 10)
			inactivityTimeout = 1000 * 60 * 10; // minimum value: 10 minute timeout
		else
			inactivityTimeout = timeout;
	}
	//private static final String SEPARATOR = "::";
	private static final int supported_versions[] = {1};

	// ----- Instance members -------------------------------------------------
	private int version;
	private long userIdk;
	private long tokenTime;

	/**
	 * NOTE: This constructor will create an INACTIVE token.
	 */
	public LoginToken()
	{
		cancel();
	}

	/**
	 * NOTE: This constructor will create an ACTIVE token.
	 * @param inUserIdk the user to create the token for.
	 */
	public LoginToken(long inUserIdk)
	{
		version = 1;
		userIdk = inUserIdk;
		tokenTime = System.currentTimeMillis();
	}

	public long getTokenTime() { return tokenTime; }
	public long getUserIdk() { return userIdk; }
	public int getVersion() { return version; }

	// NO SETTERS ON PURPOSE
	// NO SETTERS ON PURPOSE
	// NO SETTERS ON PURPOSE

	public LoginToken refreshTokenTime() 
	{ 
		tokenTime = System.currentTimeMillis();
		return this; // for easy insertion in calls...lazy eh.
	}

	public boolean isValidVersion()
	{
		for (int v : supported_versions)
			if (version == v) return true;
		return false;
	}

	/**
	 * Determines whether the token is still active or not.
	 * @return true if active, false otherwise
	 */
	public boolean isActive()
	{
		long currentTime = System.currentTimeMillis();
		if (currentTime <= tokenTime + inactivityTimeout)
			return true;
		return false;
	}
	
	public boolean isCancelled()
	{
		if (version == 0 && userIdk == -1 && tokenTime == 0)
			return true;
		return false;
	}

	public void cancel()
	{
		version = 0;
		userIdk = -1;
		tokenTime = 0;
	}

	/*public void parse(String stringToken)
	{
		try
		{
			if (stringToken != null)
			{
				String pieces[] = stringToken.split(SEPARATOR);
				
				if (pieces.length == 3)
				{
					// check the version format
					int intVersion = Integer.parseInt(pieces[0]);
					if (!isValidVersion(intVersion))
						throw new RuntimeException();
			
					// check the user ID format (does NOT check in the database though)
					if (!isWellFormattedUserIdk(Long.parseLong(pieces[1])))
						throw new RuntimeException();
						
					// check the last access time format
					long stringTokenTime = Long.parseLong(pieces[2]);
					if (stringTokenTime < 0)
						throw new RuntimeException();
						
					// everything checks out: assign to object and return
					version = intVersion;
					userIdk = Long.parseLong(pieces[1]);
					tokenTime = stringTokenTime;
					return;
				}
				
				// not 3 pieces; error
				throw new RuntimeException();
			}
			else // null string; error
				throw new RuntimeException();
		}
		catch (Exception e)
		{
			cancel();
		}
	}

	public String toString()
	{
		return Integer.toString(version) + SEPARATOR + userIdk + SEPARATOR + tokenTime;
	}*/
}
