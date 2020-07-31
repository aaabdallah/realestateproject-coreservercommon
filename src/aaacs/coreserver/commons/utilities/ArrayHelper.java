package aaacs.coreserver.commons.utilities;

/**
 * A really simple class just to do some small array manipulations.
 * 
 * @author Abu Abd-Allah
 */
public class ArrayHelper
{
	public static String toString(Object[] array, String bQuote, String eQuote)
	{
		if (array == null)
			return null;
		if (array.length == 0)
			return "";
		
		StringBuilder sb = new StringBuilder("");
		sb.append(array[0].toString());
		for (int i=1; i<array.length; i++)
		{
			sb.append(", ").append(bQuote).append(array[i].toString()).append(eQuote);
		}
		return sb.toString();
	}
}
