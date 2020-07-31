package aaacs.coreserver.commons.validation;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;

/**
 * @author Ahmed A. Abd-Allah
 * Created on Nov 1, 2006
 *
 * Some simple validation routines for validating the parameters sent to session
 * beans. In general, these routines are coarse, not intended for fine checking as
 * the FieldValidator does for entities. It focuses on data structures such as
 * arrays, lists, and maps. However, it does make use of the FieldValidator, so it 
 * can do fine checking as well of the underlying elements of the structure. Set
 * <code>checkIndividualElements</code> for checking of the underlying elements.
 */
public class ParameterValidator extends FieldValidator
{
	/* LOCALE SENSITIVE (?)
	 * absence of null
	 * type checking
	 * string to number format checking
	 * string to date format checking
	 * array checking
	 * Vector checking: nulls, type, minlength, maxlength 
	 * map checking: null, null keys/vals, typeofkeys, typeofvals, minsize, maxsize,
	 * 		individual values
	 * parameter name checking (is it in the appropriate resources file?)
	 * FieldValidator stuff
	 */
	// ----- Instance members -------------------------------------------------
	//private String parameterName = null;
	protected Locale locale = Locale.US; // protected just to remove the warning temporarily
	private Boolean allowNullStructure = false;
	private Integer validMinimumSize = null;
	private Integer validMaximumSize = null;
	private Boolean checkIndividualElements = true;

	public ParameterValidator(String parameterName)
	{
		super(parameterName);
	}
	
	public ParameterValidator()
	{
		this(null);
	}
	
	// ----- Setters (no need for getters) ------------------------------------
	public ParameterValidator clearChecks()
	{
		super.clearChecks();
		//locale = Locale.US;
		allowNullStructure = false;
		validMinimumSize = null;
		validMaximumSize = null;
		checkIndividualElements = true;
		return this;
	}

	// BE VERY CAREFUL WHEN USING THIS, COULD LEAD TO NULL POINTER PROBLEMS
	public ParameterValidator setAllowNullStructure(Boolean allowNullStructure)
	{
		this.allowNullStructure = allowNullStructure;
		return this;
	}
	public ParameterValidator setCheckIndividualElements(Boolean checkIndividualElements)
	{
		this.checkIndividualElements = checkIndividualElements;
		return this;
	}

	public ParameterValidator setLocale(Locale locale)
	{
		this.locale = locale;
		return this;
	}

	public ParameterValidator setValidMaximumSize(Integer validMaximumSize)
	{
		this.validMaximumSize = validMaximumSize;
		return this;
	}

	public ParameterValidator setValidMinimumSize(Integer validMinimumSize)
	{
		this.validMinimumSize = validMinimumSize;
		return this;
	}
	
	// We need to wrap the super class's setters to maintain proper chaining, otherwise
	// "return this" in the super class will only return a reference to an object with
	// that class obviously - not good in a chain of setters mixed with setters from
	// *this* (the derived) class.
	public ParameterValidator setName(String name)
	{
		super.setName(name);
		return this;
	}

	public ParameterValidator setAllowNullElement(Boolean allowNull)
	{
		super.setAllowNullElement(allowNull);
		return this;
	}

	public ParameterValidator setInvalidChoices(Object[] invalidChoices)
	{
		super.setInvalidChoices(invalidChoices);
		return this;
	}

	public ParameterValidator setInvalidMaximum(Double invalidMaximum)
	{
		super.setInvalidMaximum(invalidMaximum);
		return this;
	}

	public ParameterValidator setInvalidMinimum(Double invalidMinimum)
	{
		super.setInvalidMinimum(invalidMinimum);
		return this;
	}

	public ParameterValidator setInvalidRegex(String invalidRegex)
	{
		super.setInvalidRegex(invalidRegex);
		return this;
	}

	public ParameterValidator setUseInvalidChoicesAs(String useInvalidChoicesAs)
	{
		super.setUseInvalidChoicesAs(useInvalidChoicesAs);
		return this;
	}

	public ParameterValidator setUseValidChoicesAs(String useValidChoicesAs)
	{
		super.setUseValidChoicesAs(useValidChoicesAs);
		return this;
	}

	public ParameterValidator setValidChoices(Object ... validChoices)
	{
		super.setValidChoices(validChoices);
		return this;
	}

	public ParameterValidator setMaxFracLength(Integer maxFracLength)
	{
		super.setMaxFracLength(maxFracLength);
		return this;
	}

	public ParameterValidator setValidMaximum(Double validMaximum)
	{
		super.setValidMaximum(validMaximum);
		return this;
	}

	public ParameterValidator setValidMinimum(Double validMinimum)
	{
		super.setValidMinimum(validMinimum);
		return this;
	}

	public ParameterValidator setValidRegex(String validRegex)
	{
		super.setValidRegex(validRegex);
		return this;
	}

	public ParameterValidator setInvalidEarliest(Timestamp invalidEarliest)
	{
		super.setInvalidEarliest(invalidEarliest);
		return this;
	}

	public ParameterValidator setInvalidLatest(Timestamp invalidLatest)
	{
		super.setInvalidLatest(invalidLatest);
		return this;
	}

	public ParameterValidator setValidEarliest(Timestamp validEarliest)
	{
		super.setValidEarliest(validEarliest);
		return this;
	}

	public ParameterValidator setValidLatest(Timestamp validLatest)
	{
		super.setValidLatest(validLatest);
		return this;
	}
	
	// ----- Helper methods for checking nulls & types ------------------------
	
	// For checking against nulls...
	protected void checkNullStructure(Object value, String msgValueNull)
	{
		if (!allowNullStructure && value == null) 
			throw new IllegalArgumentException(msgValueNull);		
	}

	protected void checkArrayNullElements(Object[] value)
	{
		for (Object o : value)
			if (!allowNullElement &&  o == null) 
				throw new IllegalArgumentException("array.NullElements");
	}

	protected void checkListNullElements(List<?> value)
	{
		for (Object o : value)
			if (!allowNullElement && o == null) 
				throw new IllegalArgumentException("list.NullElements");
	}

	protected void checkMapNullKeysValues(Map<?,?> map)
	{
		for (Object key : map.keySet())
		{
			if (key == null) throw new IllegalArgumentException("map.NullKeys");
			if (!allowNullElement && map.get(key) == null) 
				throw new IllegalArgumentException("map.NullValues");
		}
	}
	
	/**
	 * This class only supports certain classes. This method is the filter.
	 */
	protected void checkIfSupportedBasicClass(Class<?> c)
	{
		if (c.equals(Object.class) || // try to avoid this when possible - it can be
									  // be used to defeat this whole class.
			c.equals(Byte.class) || c.equals(Short.class) || 
			c.equals(Integer.class) || c.equals(Long.class) ||
			c.equals(Float.class) || c.equals(Double.class) ||
			c.equals(Boolean.class) || c.equals(String.class) ||
			c.equals(Date.class) || c.equals(Timestamp.class) ||
			Map.class.isAssignableFrom(c) ||
			List.class.isAssignableFrom(c))
			return;
		throw new IllegalArgumentException("parameterType.Unsupported");
	}

	// For checking the types of the elements/keys/values...
	protected void checkArrayElementTypes(Object[] array, Class<?> elementClass)
	{
		checkIfSupportedBasicClass(elementClass);
		for (Object element : array)
		{
			if (element == null) continue;
			if (!elementClass.isAssignableFrom(element.getClass()))
				throw new IllegalArgumentException("array.UnexpectedType");
		}
	}

	protected void checkListElementTypes(List<?> list, Class<?> elementClass)
	{
		checkIfSupportedBasicClass(elementClass);
		for (Object element : list)
		{
			if (element == null) continue;
			if (!elementClass.isAssignableFrom(element.getClass())) 
				throw new IllegalArgumentException("list.UnexpectedType");
		}
	}

	protected void checkMapKeyTypes(Map<?, ?> map, Class<?> keyClass)
	{
		checkIfSupportedBasicClass(keyClass);
		for (Object key : map.keySet())
			if (!keyClass.isAssignableFrom(key.getClass()))
				throw new IllegalArgumentException("map.UnexpectedKeyType");
	}

	protected void checkMapValueTypes(Map<?, ?> map, Class<?> valueClass)
	{
		checkIfSupportedBasicClass(valueClass);
		for (Object value : map.values())
		{
			if (value == null) continue;
			if (!valueClass.isAssignableFrom(value.getClass()))
				throw new IllegalArgumentException("map.UnexpectedValueType");
		}
	}

	/**
	 * The next three methods work in concert with one another: they recursively descend
	 * into an array, Map, or List, and confirm that the types of the elements/keys/values
	 * at each level are correct. The expected types are inside the two arrays keyClasses
	 * and valueClasses. keyClasses is only used when that level represents a Map.
	 * If the level is an array or List or simply a basic primitive types, only the 
	 * valueClasses element for that level is relevant (the keyClasses element is unused 
	 * and may be set to null). So if you wanted to check a Map<String, Map<String, Long>>,
	 * then keyClasses = {String, String}, valueClasses={Map, Long}
	 * 
	 * Note: THESE THREE METHODS ARE VERY COMPUTATION-INTENSIVE, O(n^k) where k is
	 * the number of levels, and n is the average size of the elements per level.
	 * Be VERY CAREFUL about the size and depth of the parameters you create!
	 */
	protected void checkArrayTypesRecursively(Object[] array, 
		Class<?>[] keyClasses, Class<?>[] valueClasses, Integer level)
	{
		// check the types of the keys and values at this level
		checkArrayElementTypes(array, valueClasses[level]);

		// if we reached the end of the available type information, stop
		if (level >= keyClasses.length-1) 
			return;
		// else more information is available, so keep checking
		
		// Are the current level's elements arrays?
		if (valueClasses[level].isArray())
		{
			// interpret the next level array elements as the valueClasses being 
			// the array element types (so we have a List<Array>)
			for (Object o : array)
				checkArrayTypesRecursively((Object[]) o, keyClasses, valueClasses, level+1);			
		}
		// or are they lists?
		else if (List.class.isAssignableFrom(valueClasses[level]))
		{
			// interpret the next level array elements as the valueClasses being 
			// the list element types (so we have a List<List>)
			for (Object o : array)
				checkListTypesRecursively((List) o, keyClasses, valueClasses, level+1);
		}
		// or are they maps?
		else if (Map.class.isAssignableFrom(valueClasses[level]))
		{
			// interpret the next level array elements as key/value types of the map
			// (so we have a List<Map>)
			for (Object o : array)
				checkMapTypesRecursively((Map) o, keyClasses, valueClasses, level+1);
		}
		// Neither maps nor lists: not supported!
		else
			throw new IllegalArgumentException("parameterType.Unsupported");
	}
	
	protected void checkListTypesRecursively(List<?> list, 
		Class<?>[] keyClasses, Class<?>[] valueClasses, Integer level)
	{
		// check the types of the keys and values at this level
		checkListElementTypes(list, valueClasses[level]);

		// if we reached the end of the available type information, stop
		if (level >= keyClasses.length-1) 
			return;
		// else more information is available, so keep checking
		
		// Are the current level's elements arrays?
		if (valueClasses[level].isArray())
		{
			// interpret the next level array elements as the valueClasses being 
			// the array element types (so we have a List<Array>)
			for (Object o : list)
				checkArrayTypesRecursively((Object[]) o, keyClasses, valueClasses, level+1);
		}
		// or are they lists?
		else if (List.class.isAssignableFrom(valueClasses[level]))
		{
			// interpret the next level array elements as the valueClasses being 
			// the list element types (so we have a List<List>)
			for (Object o : list)
				checkListTypesRecursively((List) o, keyClasses, valueClasses, level+1);
		}
		// or are they maps?
		else if (Map.class.isAssignableFrom(valueClasses[level]))
		{
			// interpret the next level array elements as key/value types of the map
			// (so we have a List<Map>)
			for (Object o : list)
				checkMapTypesRecursively((Map) o, keyClasses, valueClasses, level+1);
		}
		// Neither maps nor lists: not supported!
		else
			throw new IllegalArgumentException("parameterType.Unsupported");
	}
	
	protected void checkMapTypesRecursively(Map<?, ?> map, 
		Class<?>[] keyClasses, Class<?>[] valueClasses, Integer level)
	{
		// check the types of the keys and values at this level
		checkMapKeyTypes(map, keyClasses[level]);
		checkMapValueTypes(map, valueClasses[level]);

		// if we reached the end of the available type information, stop
		if (level >= keyClasses.length-1) 
			return;
		// else more information is available, so keep checking
		
		// Are the current level's elements arrays?
		if (valueClasses[level].isArray())
		{
			// interpret the next level array elements as the valueClasses being 
			// the array element types (so we have a List<Array>)
			for (Object o : map.values())
				checkArrayTypesRecursively((Object[]) o, keyClasses, valueClasses, level+1);
		}
		// or are they lists?
		else if (List.class.isAssignableFrom(valueClasses[level]))
		{
			// interpret the next level array elements as the valueClasses being 
			// the list element types (so we have a Map<List>)
			for (Object o : map.values())
				checkListTypesRecursively((List) o, keyClasses, valueClasses, level+1);
		}
		// or are they maps?
		else if (Map.class.isAssignableFrom(valueClasses[level]))
		{
			// interpret the next level array elements as key/value types of the map
			// (so we have a Map<Map>)
			for (Object o : map.values())				
				checkMapTypesRecursively((Map) o, keyClasses, valueClasses, level+1);
		}
		// Neither maps nor lists: not supported!
		else
			throw new IllegalArgumentException("parameterType.Unsupported");
	}
	
	// ----- The actual validation methods ------------------------------------
	// Here are validation methods that are only found in ParameterValidator
	// and which focus on validating structures (arrays, lists, and maps)
	// whereas the FieldValidator methods focus on simple objects.
	public Map<String, List<MsgArgsPair>> checkByteList(Object parameterAsObject)
	{
		Map<String, List<MsgArgsPair>> allResults = 
			new LinkedHashMap<String, List<MsgArgsPair>>();
		List<MsgArgsPair> results = new Vector<MsgArgsPair>();

		if (allowNullStructure == false && parameterAsObject == null)
		{
			results.add( new MsgArgsPair("list.Null") );
			allResults.put(name, results);
			return allResults;
		}
		else if (parameterAsObject == null)
			return null;
		
		List list = null;
		try 
		{ 
			list = (List) parameterAsObject;
			for (Object o : list)
			{
				if (o != null && !o.getClass().equals(Byte.class))
					throw new ClassCastException();
				//Byte x = (Byte) o;
				//x = (true ? x : x); // just to avoid the compiler warning that b is unused
			}
		}
		catch (ClassCastException cce) 
		{ 
			results.add( new MsgArgsPair("list.UnexpectedType") );
			allResults.put(name, results);
			return allResults;
		}
		
		if (validMinimumSize != null && list.size() < validMinimumSize)
			results.add( new MsgArgsPair("list.TooShort", gatherArgs(validMinimumSize)) );

		if (validMaximumSize != null && list.size() > validMaximumSize)
			results.add( new MsgArgsPair("list.TooLong", gatherArgs(validMaximumSize)) );
		
		if (results != null && results.size() > 0)
			allResults.put(name, results);
		
		if (checkIndividualElements)
		{
			Map<String, List<MsgArgsPair>> tempResults = null;
			int i = 0;
			for (Object o : list)
			{
				Byte b = (Byte) o;
				tempResults = checkByte(b);
				if (tempResults != null)
					allResults.put(name+"["+i+"]", tempResults.get(name));
				i++;
			}
		}
		
		if (allResults.size() > 0)
			return allResults;
		return null;
	}

	public Map<String, List<MsgArgsPair>> checkByteArray(Object parameterAsObject)
	{
		Map<String, List<MsgArgsPair>> allResults = 
			new LinkedHashMap<String, List<MsgArgsPair>>();
		List<MsgArgsPair> results = new Vector<MsgArgsPair>();

		if (allowNullStructure == false && parameterAsObject == null)
		{
			results.add( new MsgArgsPair("array.Null") );
			allResults.put(name, results);
			return allResults;
		}
		else if (parameterAsObject == null)
			return null;
		
		Byte[] array = null;
		try 
		{ 
			array = (Byte[]) parameterAsObject;
		}
		catch (ClassCastException cce) 
		{ 
			results.add( new MsgArgsPair("array.UnexpectedType") );
			allResults.put(name, results);
			return allResults;
		}

		if (validMinimumSize != null && array.length < validMinimumSize)
			results.add( new MsgArgsPair("array.TooShort", gatherArgs(validMinimumSize)) );

		if (validMaximumSize != null && array.length > validMaximumSize)
			results.add( new MsgArgsPair("array.TooLong", gatherArgs(validMaximumSize)) );
		
		if (results != null && results.size() > 0)
			allResults.put(name, results);

		if (checkIndividualElements)
		{
			Map<String, List<MsgArgsPair>> tempResults = null;
			int i = 0;
			for (Byte b : array)
			{
				tempResults = checkByte(b);
				if (tempResults != null)
					allResults.put(name+"["+i+"]", tempResults.get(name));
				i++;
			}
		}
		if (allResults.size() > 0)
			return allResults;
		return null;
	}

	public Map<String, List<MsgArgsPair>> checkShortList(Object parameterAsObject)
	{
		Map<String, List<MsgArgsPair>> allResults = 
			new LinkedHashMap<String, List<MsgArgsPair>>();
		List<MsgArgsPair> results = new Vector<MsgArgsPair>();

		if (allowNullStructure == false && parameterAsObject == null)
		{
			results.add( new MsgArgsPair("list.Null") );
			allResults.put(name, results);
			return allResults;
		}
		else if (parameterAsObject == null)
			return null;
		
		List list = null;
		try 
		{ 
			list = (List) parameterAsObject;
			for (Object o : list)
			{
				if (o != null && !o.getClass().equals(Short.class))
					throw new ClassCastException();
				//Short x = (Short) o;
				//x = (true ? x : x); // just to avoid the compiler warning that b is unused
			}
		}
		catch (ClassCastException cce) 
		{ 
			results.add( new MsgArgsPair("list.UnexpectedType") );
			allResults.put(name, results);
			return allResults;
		}
		
		if (validMinimumSize != null && list.size() < validMinimumSize)
			results.add( new MsgArgsPair("list.TooShort", gatherArgs(validMinimumSize)) );

		if (validMaximumSize != null && list.size() > validMaximumSize)
			results.add( new MsgArgsPair("list.TooLong", gatherArgs(validMaximumSize)) );
		
		if (results != null && results.size() > 0)
			allResults.put(name, results);
		
		if (checkIndividualElements)
		{
			Map<String, List<MsgArgsPair>> tempResults = null;
			int i = 0;
			for (Object o : list)
			{
				Short b = (Short) o;
				tempResults = checkShort(b);
				if (tempResults != null)
					allResults.put(name+"["+i+"]", tempResults.get(name));
				i++;
			}
		}
		
		if (allResults.size() > 0)
			return allResults;
		return null;
	}

	public Map<String, List<MsgArgsPair>> checkShortArray(Object parameterAsObject)
	{
		Map<String, List<MsgArgsPair>> allResults = 
			new LinkedHashMap<String, List<MsgArgsPair>>();
		List<MsgArgsPair> results = new Vector<MsgArgsPair>();

		if (allowNullStructure == false && parameterAsObject == null)
		{
			results.add( new MsgArgsPair("array.Null") );
			allResults.put(name, results);
			return allResults;
		}
		else if (parameterAsObject == null)
			return null;
		
		Short[] array = null;
		try 
		{ 
			array = (Short[]) parameterAsObject;
		}
		catch (ClassCastException cce) 
		{ 
			results.add( new MsgArgsPair("array.UnexpectedType") );
			allResults.put(name, results);
			return allResults;
		}

		if (validMinimumSize != null && array.length < validMinimumSize)
			results.add( new MsgArgsPair("array.TooShort", gatherArgs(validMinimumSize)) );

		if (validMaximumSize != null && array.length > validMaximumSize)
			results.add( new MsgArgsPair("array.TooLong", gatherArgs(validMaximumSize)) );
		
		if (results != null && results.size() > 0)
			allResults.put(name, results);

		if (checkIndividualElements)
		{
			Map<String, List<MsgArgsPair>> tempResults = null;
			int i = 0;
			for (Short b : array)
			{
				tempResults = checkShort(b);
				if (tempResults != null)
					allResults.put(name+"["+i+"]", tempResults.get(name));
				i++;
			}
		}
		if (allResults.size() > 0)
			return allResults;
		return null;
	}

	public Map<String, List<MsgArgsPair>> checkIntegerList(Object parameterAsObject)
	{
		Map<String, List<MsgArgsPair>> allResults = 
			new LinkedHashMap<String, List<MsgArgsPair>>();
		List<MsgArgsPair> results = new Vector<MsgArgsPair>();

		if (allowNullStructure == false && parameterAsObject == null)
		{
			results.add( new MsgArgsPair("list.Null") );
			allResults.put(name, results);
			return allResults;
		}
		else if (parameterAsObject == null)
			return null;
		
		List list = null;
		try 
		{ 
			list = (List) parameterAsObject;
			for (Object o : list)
			{
				if (o != null && !o.getClass().equals(Integer.class))
					throw new ClassCastException();
				//Integer x = (Integer) o;
				//x = (true ? x : x); // just to avoid the compiler warning that b is unused
			}
		}
		catch (ClassCastException cce) 
		{ 
			results.add( new MsgArgsPair("list.UnexpectedType") );
			allResults.put(name, results);
			return allResults;
		}
		
		if (validMinimumSize != null && list.size() < validMinimumSize)
			results.add( new MsgArgsPair("list.TooShort", gatherArgs(validMinimumSize)) );

		if (validMaximumSize != null && list.size() > validMaximumSize)
			results.add( new MsgArgsPair("list.TooLong", gatherArgs(validMaximumSize)) );
		
		if (results != null && results.size() > 0)
			allResults.put(name, results);
		
		if (checkIndividualElements)
		{
			Map<String, List<MsgArgsPair>> tempResults = null;
			int i = 0;
			for (Object o : list)
			{
				Integer b = (Integer) o;
				tempResults = checkInteger(b);
				if (tempResults != null)
					allResults.put(name+"["+i+"]", tempResults.get(name));
				i++;
			}
		}
		
		if (allResults.size() > 0)
			return allResults;
		return null;
	}

	public Map<String, List<MsgArgsPair>> checkIntegerArray(Object parameterAsObject)
	{
		Map<String, List<MsgArgsPair>> allResults = 
			new LinkedHashMap<String, List<MsgArgsPair>>();
		List<MsgArgsPair> results = new Vector<MsgArgsPair>();

		if (allowNullStructure == false && parameterAsObject == null)
		{
			results.add( new MsgArgsPair("array.Null") );
			allResults.put(name, results);
			return allResults;
		}
		else if (parameterAsObject == null)
			return null;
		
		Integer[] array = null;
		try 
		{ 
			array = (Integer[]) parameterAsObject;
		}
		catch (ClassCastException cce) 
		{ 
			results.add( new MsgArgsPair("array.UnexpectedType") );
			allResults.put(name, results);
			return allResults;
		}

		if (validMinimumSize != null && array.length < validMinimumSize)
			results.add( new MsgArgsPair("array.TooShort", gatherArgs(validMinimumSize)) );

		if (validMaximumSize != null && array.length > validMaximumSize)
			results.add( new MsgArgsPair("array.TooLong", gatherArgs(validMaximumSize)) );
		
		if (results != null && results.size() > 0)
			allResults.put(name, results);

		if (checkIndividualElements)
		{
			Map<String, List<MsgArgsPair>> tempResults = null;
			int i = 0;
			for (Integer b : array)
			{
				tempResults = checkInteger(b);
				if (tempResults != null)
					allResults.put(name+"["+i+"]", tempResults.get(name));
				i++;
			}
		}
		if (allResults.size() > 0)
			return allResults;
		return null;
	}

	public Map<String, List<MsgArgsPair>> checkLongList(Object parameterAsObject)
	{
		Map<String, List<MsgArgsPair>> allResults = 
			new LinkedHashMap<String, List<MsgArgsPair>>();
		List<MsgArgsPair> results = new Vector<MsgArgsPair>();

		if (allowNullStructure == false && parameterAsObject == null)
		{
			results.add( new MsgArgsPair("list.Null") );
			allResults.put(name, results);
			return allResults;
		}
		else if (parameterAsObject == null)
			return null;
		
		List list = null;
		try 
		{ 
			list = (List) parameterAsObject;
			for (Object o : list)
			{
				if (o != null && !o.getClass().equals(Long.class))
					throw new ClassCastException();
				//Long x = (Long) o;
				//x = (true ? x : x); // just to avoid the compiler warning that b is unused
			}
		}
		catch (ClassCastException cce) 
		{ 
			results.add( new MsgArgsPair("list.UnexpectedType") );
			allResults.put(name, results);
			return allResults;
		}
		
		if (validMinimumSize != null && list.size() < validMinimumSize)
			results.add( new MsgArgsPair("list.TooShort", gatherArgs(validMinimumSize)) );

		if (validMaximumSize != null && list.size() > validMaximumSize)
			results.add( new MsgArgsPair("list.TooLong", gatherArgs(validMaximumSize)) );
		
		if (results != null && results.size() > 0)
			allResults.put(name, results);
		
		if (checkIndividualElements)
		{
			Map<String, List<MsgArgsPair>> tempResults = null;
			int i = 0;
			for (Object o : list)
			{
				Long b = (Long) o;
				tempResults = checkLong(b);
				if (tempResults != null)
					allResults.put(name+"["+i+"]", tempResults.get(name));
				i++;
			}
		}
		
		if (allResults.size() > 0)
			return allResults;
		return null;
	}

	public Map<String, List<MsgArgsPair>> checkLongArray(Object parameterAsObject)
	{
		Map<String, List<MsgArgsPair>> allResults = 
			new LinkedHashMap<String, List<MsgArgsPair>>();
		List<MsgArgsPair> results = new Vector<MsgArgsPair>();

		if (allowNullStructure == false && parameterAsObject == null)
		{
			results.add( new MsgArgsPair("array.Null") );
			allResults.put(name, results);
			return allResults;
		}
		else if (parameterAsObject == null)
			return null;
		
		Long[] array = null;
		try 
		{ 
			array = (Long[]) parameterAsObject;
		}
		catch (ClassCastException cce) 
		{ 
			results.add( new MsgArgsPair("array.UnexpectedType") );
			allResults.put(name, results);
			return allResults;
		}

		if (validMinimumSize != null && array.length < validMinimumSize)
			results.add( new MsgArgsPair("array.TooShort", gatherArgs(validMinimumSize)) );

		if (validMaximumSize != null && array.length > validMaximumSize)
			results.add( new MsgArgsPair("array.TooLong", gatherArgs(validMaximumSize)) );
		
		if (results != null && results.size() > 0)
			allResults.put(name, results);

		if (checkIndividualElements)
		{
			Map<String, List<MsgArgsPair>> tempResults = null;
			int i = 0;
			for (Long b : array)
			{
				tempResults = checkLong(b);
				if (tempResults != null)
					allResults.put(name+"["+i+"]", tempResults.get(name));
				i++;
			}
		}
		if (allResults.size() > 0)
			return allResults;
		return null;
	}

	public Map<String, List<MsgArgsPair>> checkFloatList(Object parameterAsObject)
	{
		Map<String, List<MsgArgsPair>> allResults = 
			new LinkedHashMap<String, List<MsgArgsPair>>();
		List<MsgArgsPair> results = new Vector<MsgArgsPair>();

		if (allowNullStructure == false && parameterAsObject == null)
		{
			results.add( new MsgArgsPair("list.Null") );
			allResults.put(name, results);
			return allResults;
		}
		else if (parameterAsObject == null)
			return null;
		
		List list = null;
		try 
		{ 
			list = (List) parameterAsObject;
			for (Object o : list)
			{
				if (o != null && !o.getClass().equals(Float.class))
					throw new ClassCastException();
				//Float x = (Float) o;
				//x = (true ? x : x); // just to avoid the compiler warning that b is unused
			}
		}
		catch (ClassCastException cce) 
		{ 
			results.add( new MsgArgsPair("list.UnexpectedType") );
			allResults.put(name, results);
			return allResults;
		}
		
		if (validMinimumSize != null && list.size() < validMinimumSize)
			results.add( new MsgArgsPair("list.TooShort", gatherArgs(validMinimumSize)) );

		if (validMaximumSize != null && list.size() > validMaximumSize)
			results.add( new MsgArgsPair("list.TooLong", gatherArgs(validMaximumSize)) );
		
		if (results != null && results.size() > 0)
			allResults.put(name, results);
		
		if (checkIndividualElements)
		{
			Map<String, List<MsgArgsPair>> tempResults = null;
			int i = 0;
			for (Object o : list)
			{
				Float b = (Float) o;
				tempResults = checkFloat(b);
				if (tempResults != null)
					allResults.put(name+"["+i+"]", tempResults.get(name));
				i++;
			}
		}
		
		if (allResults.size() > 0)
			return allResults;
		return null;
	}

	public Map<String, List<MsgArgsPair>> checkFloatArray(Object parameterAsObject)
	{
		Map<String, List<MsgArgsPair>> allResults = 
			new LinkedHashMap<String, List<MsgArgsPair>>();
		List<MsgArgsPair> results = new Vector<MsgArgsPair>();

		if (allowNullStructure == false && parameterAsObject == null)
		{
			results.add( new MsgArgsPair("array.Null") );
			allResults.put(name, results);
			return allResults;
		}
		else if (parameterAsObject == null)
			return null;
		
		Float[] array = null;
		try 
		{ 
			array = (Float[]) parameterAsObject;
		}
		catch (ClassCastException cce) 
		{ 
			results.add( new MsgArgsPair("array.UnexpectedType") );
			allResults.put(name, results);
			return allResults;
		}

		if (validMinimumSize != null && array.length < validMinimumSize)
			results.add( new MsgArgsPair("array.TooShort", gatherArgs(validMinimumSize)) );

		if (validMaximumSize != null && array.length > validMaximumSize)
			results.add( new MsgArgsPair("array.TooLong", gatherArgs(validMaximumSize)) );
		
		if (results != null && results.size() > 0)
			allResults.put(name, results);

		if (checkIndividualElements)
		{
			Map<String, List<MsgArgsPair>> tempResults = null;
			int i = 0;
			for (Float b : array)
			{
				tempResults = checkFloat(b);
				if (tempResults != null)
					allResults.put(name+"["+i+"]", tempResults.get(name));
				i++;
			}
		}
		if (allResults.size() > 0)
			return allResults;
		return null;
	}

	public Map<String, List<MsgArgsPair>> checkDoubleList(Object parameterAsObject)
	{
		Map<String, List<MsgArgsPair>> allResults = 
			new LinkedHashMap<String, List<MsgArgsPair>>();
		List<MsgArgsPair> results = new Vector<MsgArgsPair>();

		if (allowNullStructure == false && parameterAsObject == null)
		{
			results.add( new MsgArgsPair("list.Null") );
			allResults.put(name, results);
			return allResults;
		}
		else if (parameterAsObject == null)
			return null;
		
		List list = null;
		try 
		{ 
			list = (List) parameterAsObject;
			for (Object o : list)
			{
				if (o != null && !o.getClass().equals(Double.class))
					throw new ClassCastException();
				//Double x = (Double) o;
				//x = (true ? x : x); // just to avoid the compiler warning that b is unused
			}
		}
		catch (ClassCastException cce) 
		{ 
			results.add( new MsgArgsPair("list.UnexpectedType") );
			allResults.put(name, results);
			return allResults;
		}
		
		if (validMinimumSize != null && list.size() < validMinimumSize)
			results.add( new MsgArgsPair("list.TooShort", gatherArgs(validMinimumSize)) );

		if (validMaximumSize != null && list.size() > validMaximumSize)
			results.add( new MsgArgsPair("list.TooLong", gatherArgs(validMaximumSize)) );
		
		if (results != null && results.size() > 0)
			allResults.put(name, results);
		
		if (checkIndividualElements)
		{
			Map<String, List<MsgArgsPair>> tempResults = null;
			int i = 0;
			for (Object o : list)
			{
				Double b = (Double) o;
				tempResults = checkDouble(b);
				if (tempResults != null)
					allResults.put(name+"["+i+"]", tempResults.get(name));
				i++;
			}
		}
		
		if (allResults.size() > 0)
			return allResults;
		return null;
	}

	public Map<String, List<MsgArgsPair>> checkDoubleArray(Object parameterAsObject)
	{
		Map<String, List<MsgArgsPair>> allResults = 
			new LinkedHashMap<String, List<MsgArgsPair>>();
		List<MsgArgsPair> results = new Vector<MsgArgsPair>();

		if (allowNullStructure == false && parameterAsObject == null)
		{
			results.add( new MsgArgsPair("array.Null") );
			allResults.put(name, results);
			return allResults;
		}
		else if (parameterAsObject == null)
			return null;
		
		Double[] array = null;
		try 
		{ 
			array = (Double[]) parameterAsObject;
		}
		catch (ClassCastException cce) 
		{ 
			results.add( new MsgArgsPair("array.UnexpectedType") );
			allResults.put(name, results);
			return allResults;
		}

		if (validMinimumSize != null && array.length < validMinimumSize)
			results.add( new MsgArgsPair("array.TooShort", gatherArgs(validMinimumSize)) );

		if (validMaximumSize != null && array.length > validMaximumSize)
			results.add( new MsgArgsPair("array.TooLong", gatherArgs(validMaximumSize)) );
		
		if (results != null && results.size() > 0)
			allResults.put(name, results);

		if (checkIndividualElements)
		{
			Map<String, List<MsgArgsPair>> tempResults = null;
			int i = 0;
			for (Double b : array)
			{
				tempResults = checkDouble(b);
				if (tempResults != null)
					allResults.put(name+"["+i+"]", tempResults.get(name));
				i++;
			}
		}
		if (allResults.size() > 0)
			return allResults;
		return null;
	}

	public Map<String, List<MsgArgsPair>> checkBooleanList(Object parameterAsObject)
	{
		Map<String, List<MsgArgsPair>> allResults = 
			new LinkedHashMap<String, List<MsgArgsPair>>();
		List<MsgArgsPair> results = new Vector<MsgArgsPair>();

		if (allowNullStructure == false && parameterAsObject == null)
		{
			results.add( new MsgArgsPair("list.Null") );
			allResults.put(name, results);
			return allResults;
		}
		else if (parameterAsObject == null)
			return null;
		
		List list = null;
		try 
		{ 
			list = (List) parameterAsObject;
			for (Object o : list)
			{
				if (o != null && !o.getClass().equals(Boolean.class))
					throw new ClassCastException();
				//Boolean x = (Boolean) o;
				//x = (true ? x : x); // just to avoid the compiler warning that b is unused
			}
		}
		catch (ClassCastException cce) 
		{ 
			results.add( new MsgArgsPair("list.UnexpectedType") );
			allResults.put(name, results);
			return allResults;
		}
		
		if (validMinimumSize != null && list.size() < validMinimumSize)
			results.add( new MsgArgsPair("list.TooShort", gatherArgs(validMinimumSize)) );

		if (validMaximumSize != null && list.size() > validMaximumSize)
			results.add( new MsgArgsPair("list.TooLong", gatherArgs(validMaximumSize)) );
		
		if (results != null && results.size() > 0)
			allResults.put(name, results);
		
		if (checkIndividualElements)
		{
			Map<String, List<MsgArgsPair>> tempResults = null;
			int i = 0;
			for (Object o : list)
			{
				Boolean b = (Boolean) o;
				tempResults = checkBoolean(b);
				if (tempResults != null)
					allResults.put(name+"["+i+"]", tempResults.get(name));
				i++;
			}
		}
		
		if (allResults.size() > 0)
			return allResults;
		return null;
	}

	public Map<String, List<MsgArgsPair>> checkBooleanArray(Object parameterAsObject)
	{
		Map<String, List<MsgArgsPair>> allResults = 
			new LinkedHashMap<String, List<MsgArgsPair>>();
		List<MsgArgsPair> results = new Vector<MsgArgsPair>();

		if (allowNullStructure == false && parameterAsObject == null)
		{
			results.add( new MsgArgsPair("array.Null") );
			allResults.put(name, results);
			return allResults;
		}
		else if (parameterAsObject == null)
			return null;
		
		Boolean[] array = null;
		try 
		{ 
			array = (Boolean[]) parameterAsObject;
		}
		catch (ClassCastException cce) 
		{ 
			results.add( new MsgArgsPair("array.UnexpectedType") );
			allResults.put(name, results);
			return allResults;
		}

		if (validMinimumSize != null && array.length < validMinimumSize)
			results.add( new MsgArgsPair("array.TooShort", gatherArgs(validMinimumSize)) );

		if (validMaximumSize != null && array.length > validMaximumSize)
			results.add( new MsgArgsPair("array.TooLong", gatherArgs(validMaximumSize)) );
		
		if (results != null && results.size() > 0)
			allResults.put(name, results);

		if (checkIndividualElements)
		{
			Map<String, List<MsgArgsPair>> tempResults = null;
			int i = 0;
			for (Boolean b : array)
			{
				tempResults = checkBoolean(b);
				if (tempResults != null)
					allResults.put(name+"["+i+"]", tempResults.get(name));
				i++;
			}
		}
		if (allResults.size() > 0)
			return allResults;
		return null;
	}
	
	public Map<String, List<MsgArgsPair>> checkStringList(Object parameterAsObject)
	{
		Map<String, List<MsgArgsPair>> allResults = 
			new LinkedHashMap<String, List<MsgArgsPair>>();
		List<MsgArgsPair> results = new Vector<MsgArgsPair>();

		if (allowNullStructure == false && parameterAsObject == null)
		{
			results.add( new MsgArgsPair("list.Null") );
			allResults.put(name, results);
			return allResults;
		}
		else if (parameterAsObject == null)
			return null;
		
		List list = null;
		try 
		{ 
			list = (List) parameterAsObject;
			for (Object o : list)
			{
				if (o != null && !o.getClass().equals(String.class))
					throw new ClassCastException();
				//String x = (String) o;
				//x = (true ? x : x); // just to avoid the compiler warning that b is unused
			}
		}
		catch (ClassCastException cce) 
		{ 
			results.add( new MsgArgsPair("list.UnexpectedType") );
			allResults.put(name, results);
			return allResults;
		}
		
		if (validMinimumSize != null && list.size() < validMinimumSize)
			results.add( new MsgArgsPair("list.TooShort", gatherArgs(validMinimumSize)) );

		if (validMaximumSize != null && list.size() > validMaximumSize)
			results.add( new MsgArgsPair("list.TooLong", gatherArgs(validMaximumSize)) );
		
		if (results != null && results.size() > 0)
			allResults.put(name, results);
		
		if (checkIndividualElements)
		{
			Map<String, List<MsgArgsPair>> tempResults = null;
			int i = 0;
			for (Object o : list)
			{
				String b = (String) o;
				tempResults = checkString(b);
				if (tempResults != null)
					allResults.put(name+"["+i+"]", tempResults.get(name));
				i++;
			}
		}
		
		if (allResults.size() > 0)
			return allResults;
		return null;
	}

	public Map<String, List<MsgArgsPair>> checkStringArray(Object parameterAsObject)
	{
		Map<String, List<MsgArgsPair>> allResults = 
			new LinkedHashMap<String, List<MsgArgsPair>>();
		List<MsgArgsPair> results = new Vector<MsgArgsPair>();

		if (allowNullStructure == false && parameterAsObject == null)
		{
			results.add( new MsgArgsPair("array.Null") );
			allResults.put(name, results);
			return allResults;
		}
		else if (parameterAsObject == null)
			return null;
		
		String[] array = null;
		try 
		{ 
			array = (String[]) parameterAsObject;
		}
		catch (ClassCastException cce) 
		{ 
			results.add( new MsgArgsPair("array.UnexpectedType") );
			allResults.put(name, results);
			return allResults;
		}

		if (validMinimumSize != null && array.length < validMinimumSize)
			results.add( new MsgArgsPair("array.TooShort", gatherArgs(validMinimumSize)) );

		if (validMaximumSize != null && array.length > validMaximumSize)
			results.add( new MsgArgsPair("array.TooLong", gatherArgs(validMaximumSize)) );
		
		if (results != null && results.size() > 0)
			allResults.put(name, results);

		if (checkIndividualElements)
		{
			Map<String, List<MsgArgsPair>> tempResults = null;
			int i = 0;
			for (String b : array)
			{
				tempResults = checkString(b);
				if (tempResults != null)
					allResults.put(name+"["+i+"]", tempResults.get(name));
				i++;
			}
		}
		if (allResults.size() > 0)
			return allResults;
		return null;
	}

	public Map<String, List<MsgArgsPair>> checkDateList(Object parameterAsObject)
	{
		Map<String, List<MsgArgsPair>> allResults = 
			new LinkedHashMap<String, List<MsgArgsPair>>();
		List<MsgArgsPair> results = new Vector<MsgArgsPair>();

		if (allowNullStructure == false && parameterAsObject == null)
		{
			results.add( new MsgArgsPair("list.Null") );
			allResults.put(name, results);
			return allResults;
		}
		else if (parameterAsObject == null)
			return null;
		
		List list = null;
		try 
		{ 
			list = (List) parameterAsObject;
			for (Object o : list)
			{
				if (o != null && !o.getClass().equals(Date.class))
					throw new ClassCastException();
				//Date x = (Date) o;
				//x = (true ? x : x); // just to avoid the compiler warning that b is unused
			}
		}
		catch (ClassCastException cce) 
		{ 
			results.add( new MsgArgsPair("list.UnexpectedType") );
			allResults.put(name, results);
			return allResults;
		}
		
		if (validMinimumSize != null && list.size() < validMinimumSize)
			results.add( new MsgArgsPair("list.TooShort", gatherArgs(validMinimumSize)) );

		if (validMaximumSize != null && list.size() > validMaximumSize)
			results.add( new MsgArgsPair("list.TooLong", gatherArgs(validMaximumSize)) );
		
		if (results != null && results.size() > 0)
			allResults.put(name, results);
		
		if (checkIndividualElements)
		{
			Map<String, List<MsgArgsPair>> tempResults = null;
			int i = 0;
			for (Object o : list)
			{
				Date b = (Date) o;
				tempResults = checkDate(b);
				if (tempResults != null)
					allResults.put(name+"["+i+"]", tempResults.get(name));
				i++;
			}
		}
		
		if (allResults.size() > 0)
			return allResults;
		return null;
	}

	public Map<String, List<MsgArgsPair>> checkDateArray(Object parameterAsObject)
	{
		Map<String, List<MsgArgsPair>> allResults = 
			new LinkedHashMap<String, List<MsgArgsPair>>();
		List<MsgArgsPair> results = new Vector<MsgArgsPair>();

		if (allowNullStructure == false && parameterAsObject == null)
		{
			results.add( new MsgArgsPair("array.Null") );
			allResults.put(name, results);
			return allResults;
		}
		else if (parameterAsObject == null)
			return null;
		
		Date[] array = null;
		try 
		{ 
			array = (Date[]) parameterAsObject;
		}
		catch (ClassCastException cce) 
		{ 
			results.add( new MsgArgsPair("array.UnexpectedType") );
			allResults.put(name, results);
			return allResults;
		}

		if (validMinimumSize != null && array.length < validMinimumSize)
			results.add( new MsgArgsPair("array.TooShort", gatherArgs(validMinimumSize)) );

		if (validMaximumSize != null && array.length > validMaximumSize)
			results.add( new MsgArgsPair("array.TooLong", gatherArgs(validMaximumSize)) );
		
		if (results != null && results.size() > 0)
			allResults.put(name, results);

		if (checkIndividualElements)
		{
			Map<String, List<MsgArgsPair>> tempResults = null;
			int i = 0;
			for (Date b : array)
			{
				tempResults = checkDate(b);
				if (tempResults != null)
					allResults.put(name+"["+i+"]", tempResults.get(name));
				i++;
			}
		}
		if (allResults.size() > 0)
			return allResults;
		return null;
	}

	public Map<String, List<MsgArgsPair>> checkTimestampList(Object parameterAsObject)
	{
		Map<String, List<MsgArgsPair>> allResults = 
			new LinkedHashMap<String, List<MsgArgsPair>>();
		List<MsgArgsPair> results = new Vector<MsgArgsPair>();

		if (allowNullStructure == false && parameterAsObject == null)
		{
			results.add( new MsgArgsPair("list.Null") );
			allResults.put(name, results);
			return allResults;
		}
		else if (parameterAsObject == null)
			return null;
		
		List list = null;
		try 
		{ 
			list = (List) parameterAsObject;
			for (Object o : list)
			{
				if (o != null && !o.getClass().equals(Timestamp.class))
					throw new ClassCastException();
				//Timestamp x = (Timestamp) o;
				//x = (true ? x : x); // just to avoid the compiler warning that b is unused
			}
		}
		catch (ClassCastException cce) 
		{ 
			results.add( new MsgArgsPair("list.UnexpectedType") );
			allResults.put(name, results);
			return allResults;
		}
		
		if (validMinimumSize != null && list.size() < validMinimumSize)
			results.add( new MsgArgsPair("list.TooShort", gatherArgs(validMinimumSize)) );

		if (validMaximumSize != null && list.size() > validMaximumSize)
			results.add( new MsgArgsPair("list.TooLong", gatherArgs(validMaximumSize)) );
		
		if (results != null && results.size() > 0)
			allResults.put(name, results);
		
		if (checkIndividualElements)
		{
			Map<String, List<MsgArgsPair>> tempResults = null;
			int i = 0;
			for (Object o : list)
			{
				Timestamp b = (Timestamp) o;
				tempResults = checkTimestamp(b);
				if (tempResults != null)
					allResults.put(name+"["+i+"]", tempResults.get(name));
				i++;
			}
		}
		
		if (allResults.size() > 0)
			return allResults;
		return null;
	}

	public Map<String, List<MsgArgsPair>> checkTimestampArray(Object parameterAsObject)
	{
		Map<String, List<MsgArgsPair>> allResults = 
			new LinkedHashMap<String, List<MsgArgsPair>>();
		List<MsgArgsPair> results = new Vector<MsgArgsPair>();

		if (allowNullStructure == false && parameterAsObject == null)
		{
			results.add( new MsgArgsPair("array.Null") );
			allResults.put(name, results);
			return allResults;
		}
		else if (parameterAsObject == null)
			return null;
		
		Timestamp[] array = null;
		try 
		{ 
			array = (Timestamp[]) parameterAsObject;
		}
		catch (ClassCastException cce) 
		{ 
			results.add( new MsgArgsPair("array.UnexpectedType") );
			allResults.put(name, results);
			return allResults;
		}

		if (validMinimumSize != null && array.length < validMinimumSize)
			results.add( new MsgArgsPair("array.TooShort", gatherArgs(validMinimumSize)) );

		if (validMaximumSize != null && array.length > validMaximumSize)
			results.add( new MsgArgsPair("array.TooLong", gatherArgs(validMaximumSize)) );
		
		if (results != null && results.size() > 0)
			allResults.put(name, results);

		if (checkIndividualElements)
		{
			Map<String, List<MsgArgsPair>> tempResults = null;
			int i = 0;
			for (Timestamp b : array)
			{
				tempResults = checkTimestamp(b);
				if (tempResults != null)
					allResults.put(name+"["+i+"]", tempResults.get(name));
				i++;
			}
		}
		if (allResults.size() > 0)
			return allResults;
		return null;
	}

	/*
	 * Use the next three methods with caution: they rely on computationally expensive
	 * submethods.
	 */
	public Map<String, List<MsgArgsPair>> checkArray(
		Object parameterAsObject, Class<?>[] keyClasses, Class<?>[] valueClasses)
	{
		Map<String, List<MsgArgsPair>> allResults = 
			new LinkedHashMap<String, List<MsgArgsPair>>();
		List<MsgArgsPair> results = new Vector<MsgArgsPair>();

		if (keyClasses.length != valueClasses.length)
			throw new IllegalArgumentException("map.KeyValueClassDisparity");
		if (allowNullStructure == false && parameterAsObject == null)
		{
			results.add( new MsgArgsPair("array.Null") );
			allResults.put(name, results);
			return allResults;
		}
		else if (parameterAsObject == null)
			return null;

		Object[] array = null;
		try 
		{ 
			array = (Object[]) parameterAsObject;
		}
		catch (Exception e) 
		{ 
			results.add( new MsgArgsPair("array.UnexpectedType") );
			allResults.put(name, results);
			return allResults;
		}

		try { checkArrayTypesRecursively(array, keyClasses, valueClasses, 0); }
		catch (Exception e) { results.add( new MsgArgsPair("array.UnexpectedType") ); }
 
		if (validMinimumSize != null && array.length < validMinimumSize)
			results.add( new MsgArgsPair("array.TooShort", gatherArgs(validMinimumSize)) );

		if (validMaximumSize != null && array.length > validMaximumSize)
			results.add( new MsgArgsPair("array.TooLong", gatherArgs(validMaximumSize)) );
		
		if (results != null && results.size() > 0)
			allResults.put(name, results);

		if (checkIndividualElements)
		{
			int i = 0;
			for (Object o : array)
				if (!allowNullElement && o == null)
				{
					results = new Vector<MsgArgsPair>();
					results.add( new MsgArgsPair("array.NullElements") );
					allResults.put(name+"["+i+"]", results);
					i++;
				}
		}

		if (allResults.size() > 0)
			return allResults;
		return null;
	}

	public Map<String, List<MsgArgsPair>> checkList(
		Object parameterAsObject, Class<?>[] keyClasses, Class<?>[] valueClasses)
	{
		Map<String, List<MsgArgsPair>> allResults = 
			new LinkedHashMap<String, List<MsgArgsPair>>();
		List<MsgArgsPair> results = new Vector<MsgArgsPair>();

		if (keyClasses.length != valueClasses.length)
			throw new IllegalArgumentException("map.KeyValueClassDisparity");
		if (allowNullStructure == false && parameterAsObject == null)
		{
			results.add( new MsgArgsPair("list.Null") );
			allResults.put(name, results);
			return allResults;
		}
		else if (parameterAsObject == null)
			return null;

		List list = null;
		try 
		{ 
			list = (List) parameterAsObject;
		}
		catch (ClassCastException cce) 
		{ 
			results.add( new MsgArgsPair("list.UnexpectedType") );
			allResults.put(name, results);
			return allResults;
		}

		try { checkListTypesRecursively(list, keyClasses, valueClasses, 0); }
		catch (Exception e) { results.add( new MsgArgsPair("list.UnexpectedType") ); }

		if (validMinimumSize != null && list.size() < validMinimumSize)
			results.add( new MsgArgsPair("list.TooShort", gatherArgs(validMinimumSize)) );

		if (validMaximumSize != null && list.size() > validMaximumSize)
			results.add( new MsgArgsPair("list.TooLong", gatherArgs(validMaximumSize)) );
		
		if (results != null && results.size() > 0)
			allResults.put(name, results);

		if (checkIndividualElements)
		{
			int i = 0;
			for (Object o : list)
				if (!allowNullElement &&  o == null)
				{
					results = new Vector<MsgArgsPair>();
					results.add( new MsgArgsPair("list.NullElements") );
					allResults.put(name+"["+i+"]", results);
					i++;
				}
		}

		if (allResults.size() > 0)
			return allResults;
		return null;
	}

	public Map<String, List<MsgArgsPair>> checkMap(
		Object parameterAsObject, Class<?>[] keyClasses, Class<?>[] valueClasses)
	{
		Map<String, List<MsgArgsPair>> allResults = 
			new LinkedHashMap<String, List<MsgArgsPair>>();
		List<MsgArgsPair> results = new Vector<MsgArgsPair>();

		if (keyClasses.length != valueClasses.length)
			throw new IllegalArgumentException("map.KeyValueClassDisparity");
		if (allowNullStructure == false && parameterAsObject == null)
		{
			results.add( new MsgArgsPair("map.Null") );
			allResults.put(name, results);
			return allResults;
		}
		else if (parameterAsObject == null)
			return null;

		Map map = null;
		try 
		{ 
			map = (Map) parameterAsObject;
		}
		catch (ClassCastException cce) 
		{ 
			results.add( new MsgArgsPair("map.UnexpectedType") );
			allResults.put(name, results);
			return allResults;
		}

		try { checkMapTypesRecursively(map, keyClasses, valueClasses, 0); }
		catch (Exception e) { results.add( new MsgArgsPair("map.UnexpectedType") ); }

		if (validMinimumSize != null && map.size() < validMinimumSize)
			results.add( new MsgArgsPair("list.TooShort", gatherArgs(validMinimumSize)) );

		if (validMaximumSize != null && map.size() > validMaximumSize)
			results.add( new MsgArgsPair("list.TooLong", gatherArgs(validMaximumSize)) );
		
		if (results != null && results.size() > 0)
			allResults.put(name, results);

		if (checkIndividualElements)
		{
			int i = 0;
			for (Object o : map.keySet())
			{
				results = null;
				if (o == null)
				{
					results = new Vector<MsgArgsPair>();
					results.add( new MsgArgsPair("map.NullKeys") );
				}
				if (!allowNullElement && map.get(o) == null)
				{
					if (results == null) results = new Vector<MsgArgsPair>();
					results.add( new MsgArgsPair("map.NullValues") );
				}
				if (results != null)
					allResults.put(name+"["+o+"]", results);
				i++;
			}
		}

		if (allResults.size() > 0)
			return allResults;
		return null;
	}
}

/*
 * Wrong: this should be checked within each action against a fixed set of
 * parameter names. Delete this later.

public Map<String, String> checkNames(Locale locale, String[] names)
{
	if (names == null || names.length == 0)
		return null;
	
	ResourceBundle actionResources = 
		ResourceBundle.getBundle(Configurator.getDefaultActionResources(), locale); 
	Map<String, String> results =
		new LinkedHashMap<String, String>();
	int i=0;
	for (String name : names)
	{
		if (!actionResources.containsKey(name))
			results.put(name+"["+i+"]", "name.NotRecognized");
		i++;
	}
	
	if (results.size() > 0)
		return results;
	return null;
}
*/