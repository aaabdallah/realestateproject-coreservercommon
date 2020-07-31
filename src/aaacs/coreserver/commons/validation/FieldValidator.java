package aaacs.coreserver.commons.validation;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * @author Ahmed A. Abd-Allah
 * Created on Nov 1, 2006
 *
 * Some simple validation routines.
 */
public class FieldValidator
{
	// ----- Instance members -------------------------------------------------
	protected String name = null;
	protected Boolean allowNullElement = true;
	protected Double invalidMinimum = null;
	protected Double invalidMaximum = null;
	protected String invalidRegex = null;
	protected Object[] invalidChoices = null;
	protected String useInvalidChoicesAs = null; // "containingSet" "bitMasks"
	protected Double validMinimum = null;
	protected Double validMaximum = null;
	protected Integer maxFracLength = null;
	protected String validRegex = null;
	protected Object[] validChoices = null;
	protected String useValidChoicesAs = null; // "containingSet" "bitMasks"
	protected Timestamp invalidEarliest = null;
	protected Timestamp invalidLatest = null;
	protected Timestamp validEarliest = null;
	protected Timestamp validLatest = null;

	public FieldValidator(String name)
	{
		this.name = name;
	}
	
	public FieldValidator()
	{
		this(null);
	}
	
	// ----- Some minor helper methods ----------------------------------------
	private <T> boolean contains(T[] array, T element)
	{
		for (T a : array)
			if (a.equals(element)) return true;
		return false;
	}

	/* This is the closest I got to converting the makeBitMask methods to use
	 * generics. But it doesn't work since Java does not support casting from
	 * a Byte to a Long automatically. Plus it looks terrible, and is slow due
	 * to all the casting going on. Sour grapes you say.
	private <T> T makeBitMask(T[] array)
	{
		T bitMask = (T) new Long(0);
		for (T a : array) bitMask = (T) new Long(((Long) bitMask | (Long) a));
		return bitMask;
	}
	*/

	private Byte makeBitMask(Byte[] array)
	{
		byte bitMask = 0;
		for (Byte a : array) bitMask = (byte) (bitMask | a);
		return bitMask;
	}

	private Short makeBitMask(Short[] array)
	{
		short bitMask = 0;
		for (Short a : array) bitMask = (short) (bitMask | a);
		return bitMask;
	}

	private Integer makeBitMask(Integer[] array)
	{
		int bitMask = 0;
		for (Integer a : array) bitMask = (int) (bitMask | a);
		return bitMask;
	}

	private Long makeBitMask(Long[] array)
	{
		long bitMask = 0;
		for (Long a : array) bitMask = (bitMask | a);
		return bitMask;
	}
	
	protected Object[] gatherArgs(Object ... args)
	{
		return args;
	}

	// ----- Setters (no need for getters) ------------------------------------
	public FieldValidator clearChecks()
	{
		name = null;
		allowNullElement = true;
		invalidMinimum = null;
		invalidMaximum = null;
		invalidRegex = null;
		invalidChoices = null;
		useInvalidChoicesAs = null;
		validMinimum = null;
		validMaximum = null;
		maxFracLength = null;
		validRegex = null;
		validChoices = null;
		useValidChoicesAs = null;
		invalidEarliest = null;
		invalidLatest = null;
		validEarliest = null;
		validLatest = null;
		return this;
	}

	public FieldValidator setName(String name)
	{
		this.name = name;
		return this;
	}

	public FieldValidator setAllowNullElement(Boolean allowNull)
	{
		this.allowNullElement = allowNull;
		return this;
	}

	public FieldValidator setInvalidChoices(Object[] invalidChoices)
	{
		this.invalidChoices = invalidChoices;
		return this;
	}

	public FieldValidator setInvalidMaximum(Double invalidMaximum)
	{
		this.invalidMaximum = invalidMaximum;
		return this;
	}

	public FieldValidator setInvalidMinimum(Double invalidMinimum)
	{
		this.invalidMinimum = invalidMinimum;
		return this;
	}

	public FieldValidator setInvalidRegex(String invalidRegex)
	{
		this.invalidRegex = invalidRegex;
		return this;
	}

	public FieldValidator setUseInvalidChoicesAs(String useInvalidChoicesAs)
	{
		this.useInvalidChoicesAs = useInvalidChoicesAs;
		return this;
	}

	public FieldValidator setUseValidChoicesAs(String useValidChoicesAs)
	{
		this.useValidChoicesAs = useValidChoicesAs;
		return this;
	}

	public FieldValidator setValidChoices(Object ... validChoices)
	{
		this.validChoices = validChoices;
		return this;
	}

	public FieldValidator setMaxFracLength(Integer validMaxFracLength)
	{
		this.maxFracLength = validMaxFracLength;
		return this;
	}

	public FieldValidator setValidMaximum(Double validMaximum)
	{
		this.validMaximum = validMaximum;
		return this;
	}

	public FieldValidator setValidMinimum(Double validMinimum)
	{
		this.validMinimum = validMinimum;
		return this;
	}

	public FieldValidator setValidRegex(String validRegex)
	{
		this.validRegex = validRegex;
		return this;
	}

	public FieldValidator setInvalidEarliest(Timestamp invalidEarliest)
	{
		this.invalidEarliest = invalidEarliest;
		return this;
	}

	public FieldValidator setInvalidLatest(Timestamp invalidLatest)
	{
		this.invalidLatest = invalidLatest;
		return this;
	}

	public FieldValidator setValidEarliest(Timestamp validEarliest)
	{
		this.validEarliest = validEarliest;
		return this;
	}

	public FieldValidator setValidLatest(Timestamp validLatest)
	{
		this.validLatest = validLatest;
		return this;
	}

	public Map<String, List<MsgArgsPair>> checkObject(Object parameter)
	{
		List<MsgArgsPair> results = new Vector<MsgArgsPair>();
		Map<String, List<MsgArgsPair>> mapResults = null;
		
		if (allowNullElement == false && parameter == null)
		{
			results.add( new MsgArgsPair("object.Null") );
			mapResults = new HashMap<String, List<MsgArgsPair>>();
			mapResults.put(name, results);
			return mapResults;
		}
		else if (parameter == null)
			return null;

		if (invalidRegex != null)
		{
			String parameterAsString = parameter.toString();
			if (parameterAsString.matches(invalidRegex))
				results.add( new MsgArgsPair("object.InInvalidRange", gatherArgs(invalidRegex)) );
		}

		if (invalidChoices != null && useInvalidChoicesAs != null)
		{
			if (useInvalidChoicesAs.equals("containingSet") && 
				contains(invalidChoices, parameter))
				results.add( new MsgArgsPair("object.InInvalidChoices", gatherArgs(invalidChoices)) );
		}

		if (validRegex != null)
		{
			String parameterAsString = parameter.toString();
			if (!parameterAsString.matches(validRegex))
				results.add( new MsgArgsPair("object.InInvalidRange", gatherArgs(validRegex)) );
		}

		if (validChoices != null && useValidChoicesAs != null)
		{
			if (useValidChoicesAs.equals("containingSet") &&
				!contains(validChoices, parameter))
				results.add( new MsgArgsPair("object.NotInValidChoices", gatherArgs(validChoices)) );
		}

		if (results.size() > 0)
		{
			mapResults = new HashMap<String, List<MsgArgsPair>>();
			mapResults.put(name, results);
			return mapResults;
		}
		return null;
	}

	// Java needs operator overloading to make generics useful here...
	// It is possible to write the following repetitive methods in a way to
	// compress the code. But I left it this way intentionally because I
	// believe that making them faster is better than saving code space since
	// this code is very stable and will not change most likely. And Allaah
	// knows best.
	public Map<String, List<MsgArgsPair>> checkByte(Byte parameter)
	{
		List<MsgArgsPair> results = new Vector<MsgArgsPair>();
		Map<String, List<MsgArgsPair>> mapResults = null;
		
		if (allowNullElement == false && parameter == null)
		{
			results.add( new MsgArgsPair("number.Null") );
			mapResults = new HashMap<String, List<MsgArgsPair>>();
			mapResults.put(name, results);
			return mapResults;
		}
		else if (parameter == null)
			return null;

		if ((invalidMinimum != null && invalidMaximum != null && 
				parameter >= invalidMinimum &&
				parameter <= invalidMaximum) ||
			(invalidMinimum != null && 
				parameter >= invalidMinimum) ||
			(invalidMaximum != null && 
				parameter <= invalidMaximum)
			)
			results.add( new MsgArgsPair("number.InInvalidRange", gatherArgs(invalidMinimum, invalidMaximum)) );
		
		if (invalidRegex != null)
		{
			String parameterAsString = Byte.toString(parameter);
			if (parameterAsString.matches(invalidRegex))
				results.add( new MsgArgsPair("number.InInvalidRange", gatherArgs(invalidRegex)) );
		}

		if (invalidChoices != null && useInvalidChoicesAs != null)
		{
			if (useInvalidChoicesAs.equals("containingSet") && 
				contains(invalidChoices, parameter))
				results.add( new MsgArgsPair("number.InInvalidChoices", gatherArgs(invalidChoices)) );
			else if (useInvalidChoicesAs.equals("bitMasks") &&
				(parameter & makeBitMask((Byte[]) invalidChoices)) > 0)
				results.add( new MsgArgsPair("number.InvalidBitsSet", gatherArgs(invalidChoices)) );
		}

		if (validMinimum != null && parameter < validMinimum)
			results.add( new MsgArgsPair("number.TooSmall", gatherArgs(validMinimum)) );

		if (validMaximum != null && parameter > validMaximum)
			results.add( new MsgArgsPair("number.TooLarge", gatherArgs(validMaximum)) );

		if (validRegex != null)
		{
			String parameterAsString = Byte.toString(parameter);
			if (!parameterAsString.matches(validRegex))
				results.add( new MsgArgsPair("number.NotInValidRange", gatherArgs(validRegex)) );
		}

		if (validChoices != null && useValidChoicesAs != null)
		{
			if (useValidChoicesAs.equals("containingSet") &&
				!contains(validChoices, parameter))
				results.add( new MsgArgsPair("number.NotInValidChoices", gatherArgs(validChoices)) );
			else if (useValidChoicesAs.equals("bitMasks") &&
				(parameter & ~makeBitMask((Byte[]) validChoices)) > 0)
				results.add( new MsgArgsPair("number.OtherThanValidBitsSet", gatherArgs(validChoices)) );
		}

		if (results.size() > 0)
		{
			mapResults = new HashMap<String, List<MsgArgsPair>>();
			mapResults.put(name, results);
			return mapResults;
		}
		return null;
	}

	public Map<String, List<MsgArgsPair>> checkShort(Short parameter)
	{
		List<MsgArgsPair> results = new Vector<MsgArgsPair>();
		Map<String, List<MsgArgsPair>> mapResults = null;
		
		if (allowNullElement == false && parameter == null)
		{
			results.add( new MsgArgsPair("number.Null") );
			mapResults = new HashMap<String, List<MsgArgsPair>>();
			mapResults.put(name, results);
			return mapResults;
		}
		else if (parameter == null)
			return null;

		if ((invalidMinimum != null && invalidMaximum != null && 
				parameter >= invalidMinimum &&
				parameter <= invalidMaximum) ||
			(invalidMinimum != null && 
				parameter >= invalidMinimum) ||
			(invalidMaximum != null && 
				parameter <= invalidMaximum)
			)
			results.add( new MsgArgsPair("number.InInvalidRange", gatherArgs(invalidMinimum, invalidMaximum)) );
		
		if (invalidRegex != null)
		{
			String parameterAsString = Short.toString(parameter);
			if (parameterAsString.matches(invalidRegex))
				results.add( new MsgArgsPair("number.InInvalidRange", gatherArgs(invalidRegex)) );
		}

		if (invalidChoices != null && useInvalidChoicesAs != null)
		{
			if (useInvalidChoicesAs.equals("containingSet") && 
				contains(invalidChoices, parameter))
				results.add( new MsgArgsPair("number.InInvalidChoices", gatherArgs(invalidChoices)) );
			else if (useInvalidChoicesAs.equals("bitMasks") &&
				(parameter & makeBitMask((Short[]) invalidChoices)) > 0)
				results.add( new MsgArgsPair("number.InvalidBitsSet", gatherArgs(invalidChoices)) );
		}

		if (validMinimum != null && parameter < validMinimum)
			results.add( new MsgArgsPair("number.TooSmall", gatherArgs(validMinimum)) );

		if (validMaximum != null && parameter > validMaximum)
			results.add( new MsgArgsPair("number.TooLarge", gatherArgs(validMaximum)) );

		if (validRegex != null)
		{
			String parameterAsString = Short.toString(parameter);
			if (!parameterAsString.matches(validRegex))
				results.add( new MsgArgsPair("number.NotInValidRange", gatherArgs(validRegex)) );
		}

		if (validChoices != null && useValidChoicesAs != null)
		{
			if (useValidChoicesAs.equals("containingSet") &&
				!contains(validChoices, parameter))
				results.add( new MsgArgsPair("number.NotInValidChoices", gatherArgs(validChoices)) );
			else if (useValidChoicesAs.equals("bitMasks") &&
				(parameter & ~makeBitMask((Short[]) validChoices)) > 0)
				results.add( new MsgArgsPair("number.OtherThanValidBitsSet", gatherArgs(validChoices)) );
		}

		if (results.size() > 0)
		{
			mapResults = new HashMap<String, List<MsgArgsPair>>();
			mapResults.put(name, results);
			return mapResults;
		}
		return null;
	}

	public Map<String, List<MsgArgsPair>> checkInteger(Integer parameter)
	{
		List<MsgArgsPair> results = new Vector<MsgArgsPair>();
		Map<String, List<MsgArgsPair>> mapResults = null;
		
		if (allowNullElement == false && parameter == null)
		{
			results.add( new MsgArgsPair("number.Null") );
			mapResults = new HashMap<String, List<MsgArgsPair>>();
			mapResults.put(name, results);
			return mapResults;
		}
		else if (parameter == null)
			return null;

		if ((invalidMinimum != null && invalidMaximum != null && 
				parameter >= invalidMinimum &&
				parameter <= invalidMaximum) ||
			(invalidMinimum != null && 
				parameter >= invalidMinimum) ||
			(invalidMaximum != null && 
				parameter <= invalidMaximum)
			)
			results.add( new MsgArgsPair("number.InInvalidRange", gatherArgs(invalidMinimum, invalidMaximum)) );
		
		if (invalidRegex != null)
		{
			String parameterAsString = Integer.toString(parameter);
			if (parameterAsString.matches(invalidRegex))
				results.add( new MsgArgsPair("number.InInvalidRange", gatherArgs(invalidRegex)) );
		}

		if (invalidChoices != null && useInvalidChoicesAs != null)
		{
			if (useInvalidChoicesAs.equals("containingSet") && 
				contains(invalidChoices, parameter))
				results.add( new MsgArgsPair("number.InInvalidChoices", gatherArgs(invalidChoices)) );
			else if (useInvalidChoicesAs.equals("bitMasks") &&
				(parameter & makeBitMask((Integer[]) invalidChoices)) > 0)
				results.add( new MsgArgsPair("number.InvalidBitsSet", gatherArgs(invalidChoices)) );
		}

		if (validMinimum != null && parameter < validMinimum)
			results.add( new MsgArgsPair("number.TooSmall", gatherArgs(validMinimum)) );

		if (validMaximum != null && parameter > validMaximum)
			results.add( new MsgArgsPair("number.TooLarge", gatherArgs(validMaximum)) );

		if (validRegex != null)
		{
			String parameterAsString = Integer.toString(parameter);
			if (!parameterAsString.matches(validRegex))
				results.add( new MsgArgsPair("number.NotInValidRange", gatherArgs(validRegex)) );
		}

		if (validChoices != null && useValidChoicesAs != null)
		{
			if (useValidChoicesAs.equals("containingSet") &&
				!contains(validChoices, parameter))
				results.add( new MsgArgsPair("number.NotInValidChoices", gatherArgs(validChoices)) );
			else if (useValidChoicesAs.equals("bitMasks") &&
				(parameter & ~makeBitMask((Integer[]) validChoices)) > 0)
				results.add( new MsgArgsPair("number.OtherThanValidBitsSet", gatherArgs(validChoices)) );
		}

		if (results.size() > 0)
		{
			mapResults = new HashMap<String, List<MsgArgsPair>>();
			mapResults.put(name, results);
			return mapResults;
		}
		return null;
	}

	public Map<String, List<MsgArgsPair>> checkLong(Long parameter)
	{
		List<MsgArgsPair> results = new Vector<MsgArgsPair>();
		Map<String, List<MsgArgsPair>> mapResults = null;
		
		if (allowNullElement == false && parameter == null)
		{
			results.add( new MsgArgsPair("number.Null") );
			mapResults = new HashMap<String, List<MsgArgsPair>>();
			mapResults.put(name, results);
			return mapResults;
		}
		else if (parameter == null)
			return null;

		if ((invalidMinimum != null && invalidMaximum != null && 
				parameter >= invalidMinimum &&
				parameter <= invalidMaximum) ||
			(invalidMinimum != null && 
				parameter >= invalidMinimum) ||
			(invalidMaximum != null && 
				parameter <= invalidMaximum)
			)
			results.add( new MsgArgsPair("number.InInvalidRange", gatherArgs(invalidMinimum, invalidMaximum)) );
		
		if (invalidRegex != null)
		{
			String parameterAsString = Long.toString(parameter);
			if (parameterAsString.matches(invalidRegex))
				results.add( new MsgArgsPair("number.InInvalidRange", gatherArgs(invalidRegex)) );
		}

		if (invalidChoices != null && useInvalidChoicesAs != null)
		{
			if (useInvalidChoicesAs.equals("containingSet") && 
				contains(invalidChoices, parameter))
				results.add( new MsgArgsPair("number.InInvalidChoices", gatherArgs(invalidChoices)) );
			else if (useInvalidChoicesAs.equals("bitMasks") &&
				(parameter & makeBitMask((Long[]) invalidChoices)) > 0)
				results.add( new MsgArgsPair("number.InvalidBitsSet", gatherArgs(invalidChoices)) );
		}

		if (validMinimum != null && parameter < validMinimum)
			results.add( new MsgArgsPair("number.TooSmall", gatherArgs(validMinimum)) );

		if (validMaximum != null && parameter > validMaximum)
			results.add( new MsgArgsPair("number.TooLarge", gatherArgs(validMaximum)) );

		if (validRegex != null)
		{
			String parameterAsString = Long.toString(parameter);
			if (!parameterAsString.matches(validRegex))
				results.add( new MsgArgsPair("number.NotInValidRange", gatherArgs(validRegex)) );
		}

		if (validChoices != null && useValidChoicesAs != null)
		{
			if (useValidChoicesAs.equals("containingSet") &&
				!contains(validChoices, parameter))
				results.add( new MsgArgsPair("number.NotInValidChoices", gatherArgs(validChoices)) );
			else if (useValidChoicesAs.equals("bitMasks") &&
				(parameter & ~makeBitMask((Long[]) validChoices)) > 0)
				results.add( new MsgArgsPair("number.OtherThanValidBitsSet", gatherArgs(validChoices)) );
		}

		if (results.size() > 0)
		{
			mapResults = new HashMap<String, List<MsgArgsPair>>();
			mapResults.put(name, results);
			return mapResults;
		}
		return null;
	}

	public Map<String, List<MsgArgsPair>> checkFloat(Float parameter)
	{
		List<MsgArgsPair> results = new Vector<MsgArgsPair>();
		Map<String, List<MsgArgsPair>> mapResults = null;
		
		if (allowNullElement == false && parameter == null)
		{
			results.add( new MsgArgsPair("number.Null") );
			mapResults = new HashMap<String, List<MsgArgsPair>>();
			mapResults.put(name, results);
			return mapResults;
		}
		else if (parameter == null)
			return null;

		if ((invalidMinimum != null && invalidMaximum != null && 
				parameter >= invalidMinimum &&
				parameter <= invalidMaximum) ||
			(invalidMinimum != null && 
				parameter >= invalidMinimum) ||
			(invalidMaximum != null && 
				parameter <= invalidMaximum)
			)
			results.add( new MsgArgsPair("number.InInvalidRange", gatherArgs(invalidMinimum, invalidMaximum)) );

		if (invalidRegex != null)
		{
			String parameterAsString = Float.toString(parameter);
			if (parameterAsString.matches(invalidRegex))
				results.add( new MsgArgsPair("number.InInvalidRange", gatherArgs(invalidRegex)) );
		}

		if (invalidChoices != null && useInvalidChoicesAs != null)
		{
			if (useInvalidChoicesAs.equals("containingSet") && 
				contains(invalidChoices, parameter))
				results.add( new MsgArgsPair("number.InInvalidChoices", gatherArgs(invalidChoices)) );
		}

		if (validMinimum != null && parameter < validMinimum)
			results.add( new MsgArgsPair("number.TooSmall", gatherArgs(validMinimum)) );

		if (validMaximum != null && parameter > validMaximum)
			results.add( new MsgArgsPair("number.TooLarge", gatherArgs(validMaximum)) );

		if (maxFracLength != null &&
			Float.toString(parameter - parameter.longValue()).length() >
				maxFracLength + 2) // "0.123" (3 + 2)
			results.add( new MsgArgsPair("number.InvalidFracLen", gatherArgs(maxFracLength)) );

		if (validRegex != null)
		{
			String parameterAsString = Float.toString(parameter);
			if (!parameterAsString.matches(validRegex))
				results.add( new MsgArgsPair("number.NotInValidRange", gatherArgs(validRegex)) );
		}

		if (validChoices != null && useValidChoicesAs != null)
		{
			if (useValidChoicesAs.equals("containingSet") &&
				!contains(validChoices, parameter))
				results.add( new MsgArgsPair("number.NotInValidChoices", gatherArgs(validChoices)) );
		}

		if (results.size() > 0)
		{
			mapResults = new HashMap<String, List<MsgArgsPair>>();
			mapResults.put(name, results);
			return mapResults;
		}
		return null;
	}

	public Map<String, List<MsgArgsPair>> checkDouble(Double parameter)
	{
		List<MsgArgsPair> results = new Vector<MsgArgsPair>();
		Map<String, List<MsgArgsPair>> mapResults = null;
		
		if (allowNullElement == false && parameter == null)
		{
			results.add( new MsgArgsPair("number.Null") );
			mapResults = new HashMap<String, List<MsgArgsPair>>();
			mapResults.put(name, results);
			return mapResults;
		}
		else if (parameter == null)
			return null;

		if ((invalidMinimum != null && invalidMaximum != null && 
				parameter >= invalidMinimum &&
				parameter <= invalidMaximum) ||
			(invalidMinimum != null && 
				parameter >= invalidMinimum) ||
			(invalidMaximum != null && 
				parameter <= invalidMaximum)
			)
			results.add( new MsgArgsPair("number.InInvalidRange", gatherArgs(invalidMinimum, invalidMaximum)) );

		if (invalidRegex != null)
		{
			String parameterAsString = Double.toString(parameter);
			if (parameterAsString.matches(invalidRegex))
				results.add( new MsgArgsPair("number.InInvalidRange", gatherArgs(invalidRegex)) );
		}

		if (invalidChoices != null && useInvalidChoicesAs != null)
		{
			if (useInvalidChoicesAs.equals("containingSet") && 
				contains(invalidChoices, parameter))
				results.add( new MsgArgsPair("number.InInvalidChoices", gatherArgs(invalidChoices)) );
		}

		if (validMinimum != null && parameter < validMinimum)
			results.add( new MsgArgsPair("number.TooSmall", gatherArgs(validMinimum)) );

		if (validMaximum != null && parameter > validMaximum)
			results.add( new MsgArgsPair("number.TooLarge", gatherArgs(validMaximum)) );

		if (maxFracLength != null &&
			Double.toString(parameter - parameter.longValue()).length() >
				maxFracLength + 2) // "0.123" (3 + 2)
			results.add( new MsgArgsPair("number.InvalidFracLen", gatherArgs(maxFracLength)) );

		if (validRegex != null)
		{
			String parameterAsString = Double.toString(parameter);
			if (!parameterAsString.matches(validRegex))
				results.add( new MsgArgsPair("number.NotInValidRange", gatherArgs(validRegex)) );
		}

		if (validChoices != null && useValidChoicesAs != null)
		{
			if (useValidChoicesAs.equals("containingSet") &&
				!contains(validChoices, parameter))
				results.add( new MsgArgsPair("number.NotInValidChoices", gatherArgs(validChoices)) );
		}

		if (results.size() > 0)
		{
			mapResults = new HashMap<String, List<MsgArgsPair>>();
			mapResults.put(name, results);
			return mapResults;
		}
		return null;
	}

	public Map<String, List<MsgArgsPair>> checkBoolean(Boolean parameter)
	{
		List<MsgArgsPair> results = new Vector<MsgArgsPair>();
		Map<String, List<MsgArgsPair>> mapResults = null;
		
		if (allowNullElement == false && parameter == null)
		{
			results.add( new MsgArgsPair("boolean.Null") );
			mapResults = new HashMap<String, List<MsgArgsPair>>();
			mapResults.put(name, results);
			return mapResults;
		}
		else if (parameter == null)
			return null;

		if (invalidChoices != null && contains(invalidChoices, parameter))
			results.add( new MsgArgsPair("boolean.InInvalidChoices", gatherArgs(invalidChoices)) );

		if (validChoices != null && contains(validChoices, parameter))
			results.add( new MsgArgsPair("boolean.NotInValidChoices", gatherArgs(validChoices)) );

		if (results.size() > 0)
		{
			mapResults = new HashMap<String, List<MsgArgsPair>>();
			mapResults.put(name, results);
			return mapResults;
		}
		return null;
	}

	public Map<String, List<MsgArgsPair>> checkString(String parameter)
	{
		List<MsgArgsPair> results = new Vector<MsgArgsPair>();
		Map<String, List<MsgArgsPair>> mapResults = null;
		
		if (allowNullElement == false && parameter == null)
		{
			results.add( new MsgArgsPair("string.Null") );
			mapResults = new HashMap<String, List<MsgArgsPair>>();
			mapResults.put(name, results);
			return mapResults;
		}
		else if (parameter == null)
			return null;

		if ((invalidMinimum != null && invalidMaximum != null &&
				parameter.length() >= invalidMinimum &&
				parameter.length() <= invalidMaximum) ||
			(invalidMinimum != null &&
				parameter.length() >= invalidMinimum) ||
			(invalidMaximum != null &&
					parameter.length() <= invalidMaximum))
			results.add( new MsgArgsPair("string.LengthInInvalidRange", gatherArgs(invalidMinimum, invalidMaximum)) );

		if (invalidRegex != null)
		{
			if (parameter.matches(invalidRegex))
				results.add( new MsgArgsPair("string.InvalidExpression", gatherArgs(invalidRegex)) );
		}

		if (invalidChoices != null && useInvalidChoicesAs != null)
		{
			if (useInvalidChoicesAs.equals("containingSet") && 
				contains(invalidChoices, parameter))
				results.add( new MsgArgsPair("string.InInvalidChoices", gatherArgs(invalidChoices)) );
		}

		if (validMinimum != null && parameter.length() < validMinimum)
			results.add( new MsgArgsPair("string.TooShort", gatherArgs(validMinimum)) );

		if (validMaximum != null && parameter.length() > validMaximum)
			results.add( new MsgArgsPair("string.TooLong", gatherArgs(validMaximum)) );

		if (validRegex != null)
		{
			if (!parameter.matches(validRegex))
				results.add( new MsgArgsPair("string.InvalidExpression", gatherArgs(validRegex)) );
		}

		if (validChoices != null && useValidChoicesAs != null)
		{
			if (useValidChoicesAs.equals("containingSet") &&
				!contains(validChoices, parameter))
				results.add( new MsgArgsPair("string.NotInValidChoices", gatherArgs(validChoices)) );
		}

		if (results.size() > 0)
		{
			mapResults = new HashMap<String, List<MsgArgsPair>>();
			mapResults.put(name, results);
			return mapResults;
		}
		return null;
	}

	public Map<String, List<MsgArgsPair>> checkDate(Date parameter)
	{
		List<MsgArgsPair> results = new Vector<MsgArgsPair>();
		Map<String, List<MsgArgsPair>> mapResults = null;
		
		if (allowNullElement == false && parameter == null)
		{
			results.add( new MsgArgsPair("date.Null") );
			mapResults = new HashMap<String, List<MsgArgsPair>>();
			mapResults.put(name, results);
			return mapResults;
		}
		else if (parameter == null)
			return null;

		if ((invalidEarliest != null && invalidLatest != null && 
				parameter.compareTo(new Date(invalidEarliest.getTime())) >= 0 &&
				parameter.compareTo(new Date(invalidLatest.getTime())) <= 0) ||
			(invalidEarliest != null && 
				parameter.compareTo(new Date(invalidEarliest.getTime())) >= 0) ||
			(invalidLatest != null && 
				parameter.compareTo(new Date(invalidLatest.getTime())) <= 0)
			)
			results.add( new MsgArgsPair("date.InInvalidRange", gatherArgs(invalidEarliest, invalidLatest)) );
		
		if (invalidRegex != null)
		{
			if (parameter.toString().matches(invalidRegex))
				results.add( new MsgArgsPair("date.InInvalidRange", gatherArgs(invalidRegex)) );
		}

		if (invalidChoices != null && useInvalidChoicesAs != null)
		{
			if (useInvalidChoicesAs.equals("containingSet") && 
				contains(invalidChoices, parameter))
				results.add( new MsgArgsPair("date.InInvalidChoices", gatherArgs(invalidChoices)) );
		}

		if (validEarliest != null && 
			parameter.compareTo(new Date(validEarliest.getTime())) < 0)
			results.add( new MsgArgsPair("date.TooEarly", gatherArgs(validEarliest)) );

		if (validLatest != null && 
			parameter.compareTo(new Date(validLatest.getTime())) > 0)
			results.add( new MsgArgsPair("date.TooLate", gatherArgs(validLatest)) );

		if (validRegex != null)
		{
			if (!parameter.toString().matches(validRegex))
				results.add( new MsgArgsPair("date.NotInValidRange", gatherArgs(validRegex)) );
		}

		if (validChoices != null && useValidChoicesAs != null)
		{
			if (useValidChoicesAs.equals("containingSet") &&
				!contains(validChoices, parameter))
				results.add( new MsgArgsPair("date.NotInValidChoices", gatherArgs(validChoices)) );
		}

		if (results.size() > 0)
		{
			mapResults = new HashMap<String, List<MsgArgsPair>>();
			mapResults.put(name, results);
			return mapResults;
		}
		return null;
	}

	public Map<String, List<MsgArgsPair>> checkTimestamp(Timestamp parameter)
	{
		List<MsgArgsPair> results = new Vector<MsgArgsPair>();
		Map<String, List<MsgArgsPair>> mapResults = null;
		
		if (allowNullElement == false && parameter == null)
		{
			results.add( new MsgArgsPair("timestamp.Null") );
			mapResults = new HashMap<String, List<MsgArgsPair>>();
			mapResults.put(name, results);
			return mapResults;
		}
		else if (parameter == null)
			return null;

		if ((invalidEarliest != null && invalidLatest != null && 
				parameter.compareTo(invalidEarliest) >= 0 &&
				parameter.compareTo(invalidLatest) <= 0) ||
			(invalidEarliest != null && 
				parameter.compareTo(invalidEarliest) >= 0) ||
			(invalidLatest != null && 
				parameter.compareTo(invalidLatest) <= 0)
			)
			results.add( new MsgArgsPair("timestamp.InInvalidRange", gatherArgs(invalidEarliest, invalidLatest)) );
		
		if (invalidRegex != null)
		{
			if (parameter.toString().matches(invalidRegex))
				results.add( new MsgArgsPair("timestamp.InInvalidRange", gatherArgs(invalidRegex)) );
		}

		if (invalidChoices != null && useInvalidChoicesAs != null)
		{
			if (useInvalidChoicesAs.equals("containingSet") && 
				contains(invalidChoices, parameter))
				results.add( new MsgArgsPair("timestamp.InInvalidChoices", gatherArgs(invalidChoices)) );
		}

		if (validEarliest != null && parameter.compareTo(validEarliest) < 0)
			results.add( new MsgArgsPair("timestamp.TooEarly", gatherArgs(validEarliest)) );

		if (validLatest != null && parameter.compareTo(validLatest) > 0)
			results.add( new MsgArgsPair("timestamp.TooLate", gatherArgs(validLatest)) );

		if (validRegex != null)
		{
			if (!parameter.toString().matches(validRegex))
				results.add( new MsgArgsPair("timestamp.NotInValidRange", gatherArgs(validRegex)) );
		}

		if (validChoices != null && useValidChoicesAs != null)
		{
			if (useValidChoicesAs.equals("containingSet") &&
				!contains(validChoices, parameter))
				results.add( new MsgArgsPair("timestamp.NotInValidChoices", gatherArgs(validChoices)) );
		}

		if (results.size() > 0)
		{
			mapResults = new HashMap<String, List<MsgArgsPair>>();
			mapResults.put(name, results);
			return mapResults;
		}
		return null;
	}

	public Map<String, List<MsgArgsPair>> checkGroups(Groups parameter)
	{
		List<MsgArgsPair> results = new Vector<MsgArgsPair>();
		Map<String, List<MsgArgsPair>> mapResults = null;
		
		if (allowNullElement == false && parameter == null)
		{
			results.add( new MsgArgsPair("groups.Null") );
			mapResults = new HashMap<String, List<MsgArgsPair>>();
			mapResults.put(name, results);
			return mapResults;
		}
		else if (parameter == null)
			return null;

		if (!Groups.isValidGroups(parameter.toString()))
			results.add( new MsgArgsPair("groups.Invalid") );

		if (results.size() > 0)
		{
			mapResults = new HashMap<String, List<MsgArgsPair>>();
			mapResults.put(name, results);
			return mapResults;
		}
		return null;
	}
}


/*
These are the old validation routines...

public String checkString(String parameter, boolean allowNullElement, Integer minlength, Integer maxlength, 
String choices[], boolean checkCase)
{
if (allowNullElement == true && parameter == null)
	return null;

if (parameter == null)
	return msgMgr.getString(locale, "string.Null");

if (minlength != null && parameter.length() < minlength.intValue())
	return msgMgr.getString(locale, "string.TooShort");

if (maxlength != null && parameter.length() > maxlength.intValue())
	return msgMgr.getString(locale, "string.TooLong");

if (choices != null)
{
	boolean found = false;
	
	for (int i=0; i<choices.length; i++)
	{
		if (choices[i] != null)
		{
			if ((checkCase && parameter.equals(choices[i])) ||
				(parameter.equalsIgnoreCase(choices[i])) )
			{
				found = true;
				break;
			}
		}
	}
	if (found != true)
		return msgMgr.getString(locale, "string.NotValidChoice");
}

return null;
}

public String checkShort(short parameter, Short minimum, Short maximum,
short choices[])
{
if (minimum != null && parameter < minimum.shortValue())
	return msgMgr.getString(locale, "number.TooSmall");

if (maximum != null && parameter > maximum.shortValue())
	return msgMgr.getString(locale, "number.TooLarge");

if (choices != null)
{
	boolean found = false;
	
	for (int i=0; i<choices.length; i++)
	{
		if (parameter == choices[i])
		{
			found = true;
			break;
		}
	}
	if (found != true)
		return msgMgr.getString(locale, "number.NotValidChoice");
}

return null;
}

public String checkInteger(int parameter, Integer minimum, Integer maximum,
int choices[])
{
if (minimum != null && parameter < minimum.intValue())
	return msgMgr.getString(locale, "number.TooSmall");

if (maximum != null && parameter > maximum.intValue())
	return msgMgr.getString(locale, "number.TooLarge");

if (choices != null)
{
	boolean found = false;
	
	for (int i=0; i<choices.length; i++)
	{
		if (parameter == choices[i])
		{
			found = true;
			break;
		}
	}
	if (found != true)
		return msgMgr.getString(locale, "number.NotValidChoice");
}

return null;
}

public String checkLong(long parameter, Long minimum, Long maximum,
long choices[])
{
if (minimum != null && parameter < minimum.longValue())
	return msgMgr.getString(locale, "number.TooSmall");

if (maximum != null && parameter > maximum.longValue())
	return msgMgr.getString(locale, "number.TooLarge");

if (choices != null)
{
	boolean found = false;
	
	for (int i=0; i<choices.length; i++)
	{
		if (parameter == choices[i])
		{
			found = true;
			break;
		}
	}
	if (found != true)
		return msgMgr.getString(locale, "number.NotValidChoice");
}

return null;
}

public String checkFloat(float parameter, Float minimum, Float maximum,
float choices[])
{
if (minimum != null && parameter < minimum.floatValue())
	return msgMgr.getString(locale, "number.TooSmall");

if (maximum != null && parameter > maximum.floatValue())
	return msgMgr.getString(locale, "number.TooLarge");

if (choices != null)
{
	boolean found = false;
	
	for (int i=0; i<choices.length; i++)
	{
		if (parameter == choices[i])
		{
			found = true;
			break;
		}
	}
	if (found != true)
		return msgMgr.getString(locale, "number.NotValidChoice");
}

return null;
}

public String checkDouble(double parameter, Double minimum, Double maximum,
double choices[])
{
if (minimum != null && parameter < minimum.doubleValue())
	return msgMgr.getString(locale, "number.TooSmall");

if (maximum != null && parameter > maximum.doubleValue())
	return msgMgr.getString(locale, "number.TooLarge");

if (choices != null)
{
	boolean found = false;
	
	for (int i=0; i<choices.length; i++)
	{
		if (parameter == choices[i])
		{
			found = true;
			break;
		}
	}
	if (found != true)
		return msgMgr.getString(locale, "number.NotValidChoice");
}

return null;
}

public String checkTimestamp(Timestamp parameter, boolean allowNullElement, Timestamp earliest, Timestamp latest,
Timestamp choices[])
{
if (allowNullElement == true && parameter == null)
	return null;

if (parameter == null)
	return msgMgr.getString(locale, "timestamp.Null");
	
if (earliest != null && parameter.before(earliest))
	return msgMgr.getString(locale, "timestamp.TooEarly");

if (latest != null && parameter.after(latest))
	return msgMgr.getString(locale, "timestamp.TooLate");

Calendar calendar = Calendar.getInstance();
try { calendar.setTime( parameter ); }
catch (Exception e) { return msgMgr.getString(locale, "timestamp.NotValidChoice"); }
if (calendar.get(Calendar.YEAR) == 1000)
	return msgMgr.getString(locale, "timestamp.NotValidChoice");

if (choices != null)
{
	boolean found = false;
	
	for (int i=0; i<choices.length; i++)
	{
		if (parameter.equals(choices[i]))
		{
			found = true;
			break;
		}
	}
	if (found != true)
		return msgMgr.getString(locale, "timestamp.NotValidChoice");
}

return null;
}

public String checkPrimaryKey(PrimaryKeyHolder parameter, boolean allowNullElement, boolean checkIfUserSupplied, PrimaryKeyHolder choices[])
{
if (allowNullElement == true && parameter == null)
	return null;

if (parameter == null)
	return msgMgr.getString(locale, "primarykey.Null");

if (checkIfUserSupplied && !parameter.isUserKey())
	return msgMgr.getString(locale, "primarykey.NotUserSupplied");

if (choices != null)
{
	boolean found = false;

	for (int i=0; i<choices.length; i++)
	{
		if (parameter.equals(choices[i]))
		{
			found = true;
			break;
		}
	}
	if (found != true)
		return msgMgr.getString(locale, "primarykey.NotValidChoice");
}

return null;
}

public String checkGroups(Groups parameter, boolean allowNullElement)
{
if (allowNullElement == true && parameter == null)
	return null;

if (parameter == null)
	return msgMgr.getString(locale, "groups.Null");

if (!Groups.isValidGroups(parameter.toString()))
	return msgMgr.getString(locale, "groups.BadFormat");

return null;
}

// ------------------------------------------------------------------------
// The following validation functions are similar to the above group but 
// they take as their input parameter to check a String value that must be parsed.

public String checkShort(String parameter, boolean allowNullElement, Short minimum, Short maximum,
short choices[])
{
if (allowNullElement == true && parameter == null)
	return null;

if (parameter == null)
	return msgMgr.getString(locale, "number.Null");

try
{
	return checkShort((new Short(parameter)).shortValue(), minimum,
		maximum, choices);
}
catch (NumberFormatException nfe)
{
	return msgMgr.getString(locale, "number.BadFormat");
}
}

public String checkInteger(String parameter, boolean allowNullElement, Integer minimum, Integer maximum,
int choices[])
{
if (allowNullElement == true && parameter == null)
	return null;

if (parameter == null)
	return msgMgr.getString(locale, "number.Null");

try
{
	return checkInteger((new Integer(parameter)).intValue(), minimum,
		maximum, choices);
}
catch (NumberFormatException nfe)
{
	return msgMgr.getString(locale, "number.BadFormat");
}
}

public String checkLong(String parameter, boolean allowNullElement, Long minimum, Long maximum,
long choices[])
{
if (allowNullElement == true && parameter == null)
	return null;

if (parameter == null)
	return msgMgr.getString(locale, "number.Null");

try
{
	return checkLong((new Long(parameter)).longValue(), minimum,
		maximum, choices);
}
catch (NumberFormatException nfe)
{
	return msgMgr.getString(locale, "number.BadFormat");
}
}

public String checkFloat(String parameter, boolean allowNullElement, Float minimum, Float maximum,
float choices[])
{
if (allowNullElement == true && parameter == null)
	return null;

if (parameter == null)
	return msgMgr.getString(locale, "number.Null");

try
{
	return checkFloat((new Float(parameter)).floatValue(), minimum,
		maximum, choices);
}
catch (NumberFormatException nfe)
{
	return msgMgr.getString(locale, "number.BadFormat");
}
}

public String checkDouble(String parameter, boolean allowNullElement, Double minimum, Double maximum,
double choices[])
{
if (allowNullElement == true && parameter == null)
	return null;

if (parameter == null)
	return msgMgr.getString(locale, "number.Null");

try
{
	return checkDouble((new Double(parameter)).doubleValue(), minimum,
		maximum, choices);
}
catch (NumberFormatException nfe)
{
	return msgMgr.getString(locale, "number.BadFormat");
}
}

public String checkTimestamp(String parameter, boolean allowNullElement, Timestamp earliest, Timestamp latest,
Timestamp choices[])
{
if (allowNullElement == true && parameter == null)
	return null;

if (parameter == null)
	return msgMgr.getString(locale, "timestamp.Null");

try
{
	//new Timestamp(DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG).parse(parameter).getTime()),
	return checkTimestamp(Timestamp.valueOf(parameter),
		allowNullElement, earliest, latest, choices );
}
catch (Exception pe)
{
	return msgMgr.getString(locale, "timestamp.BadFormat");
}
}

public String checkGroups(String parameter, boolean allowNullElement)
{
if (allowNullElement == true && parameter == null)
	return null;

if (parameter == null)
	return msgMgr.getString(locale, "groups.Null");

if (!Groups.isValidGroups(parameter))
	return msgMgr.getString(locale, "groups.BadFormat");

return null;
}

*/