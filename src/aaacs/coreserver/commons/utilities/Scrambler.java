package aaacs.coreserver.commons.utilities;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Scrambler
{
	/* The prefix that is added to scrambled keys for recognition purposes
	 */
	private static final String scrambledKeyPrefix = "SKPF";
	private static final String pScrambledKeyPrefix = "SKPFP";
	private static final String nScrambledKeyPrefix = "SKPFN";

	/* LEFT HERE FOR HISTORICAL & JUST-IN-CASE REVIEW; THIS CODE IS OBSOLETE.
	 * The length of scrambled keys
	 */
	//private static final int scrambledKeyLength = 25;

	/* Controls whether to scramble or not.
	 */
	private static boolean scrambling = true;

	public static String scramble(long l)
	{
		if (scrambling)
		{
			String s = Long.toString(Long.reverse(l), Character.MAX_RADIX).toUpperCase();
			if (s.startsWith("-"))
				return nScrambledKeyPrefix + s.substring(1);
			else
				return pScrambledKeyPrefix + s;
		}
		else
			return Long.toString(l);
	}
	
	public static long descramble(String s)
	{
		if (scrambling)
		{
			if (s.startsWith(nScrambledKeyPrefix))
				return Long.reverse(Long.parseLong("-" + s.substring(nScrambledKeyPrefix.length()), Character.MAX_RADIX));
			else if (s.startsWith(pScrambledKeyPrefix))
				return Long.reverse(Long.parseLong(s.substring(pScrambledKeyPrefix.length()), Character.MAX_RADIX));
		}
		return Long.parseLong(s);
	}
	
	public static String descrambleKeys(String string)
	{
		String copy = string;
		
		if (string != null)
		{
			Pattern keyPattern = Pattern.compile("(" + scrambledKeyPrefix + "[A-Z0-9]+)[^A-Z0-9]");
			Matcher keyPatternMatcher = keyPattern.matcher(string);

			while (keyPatternMatcher.find())
			{
				String possibleKey = keyPatternMatcher.group(1);	
				try
				{
					String unscrambledKey = Long.toString(descramble(possibleKey));
					if (unscrambledKey != null)
						copy = copy.replaceFirst(possibleKey, unscrambledKey);
				}
				catch (Exception e)
				{
				}
			}
		}
		return copy;
	}
}
