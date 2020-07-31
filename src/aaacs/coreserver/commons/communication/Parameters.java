package aaacs.coreserver.commons.communication;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import aaacs.coreserver.commons.validation.ParameterValidator;

/**
 * @author Ahmed A. Abd-Allah, Dec 2, 2006
 *
 * A class which wraps a Map with a set of "type-safe" methods for inserting and
 * retrieving parameters. This reduces the burden on users of ActionRequests and
 * ActionResponses, because they are guaranteed the expected type at least.
 * 
 * It also purposely restricts the allowable types for parameters:
 * - The supported types in ParameterValidator
 *   Object, Byte, Short, Integer, Long, Float, Double, String, Date, Timestamp, Boolean
 * - Arrays of the afore-mentioned supported types
 * - Lists of the afore-mentioned types
 * - Maps with keys & values of the AMT *PLUS* other Maps/Arrays/ for values
 */
public class Parameters extends ParameterValidator implements Serializable
{
	/**
	 * Serialization Version Number
	 */
	private static final long serialVersionUID = 1000L;

	/*
	 * Kinds of parameters to allow:
	 * - The supported types in ParameterValidator
	 *   Object, Byte, Short, Integer, Long, Float, Double, String, Date, Timestamp, Boolean
	 * - Arrays of the afore-mentioned supported types
	 * - Vectors of the afore-mentioned types
	 * - Maps with keys & values of the AMT *PLUS* other Maps/Arrays/ for values
	 */
	// ----- Instance variables -----------------------------------------------
	private LinkedHashMap<String, Object> parameters;

	// ----- Constructors -----------------------------------------------------
	public Parameters()
	{
		parameters = new LinkedHashMap<String, Object>();
	}

	// ----- Management methods -----------------------------------------------
	public int size() { return parameters.size(); }
	public void clearParameters() { parameters.clear(); }
	
	// ----- Methods to expose FieldValidator/ParameterValidator checks -------
	// We need to wrap the super class's setters to maintain proper chaining, otherwise
	// "return this" in the super class will only return a reference to an object with
	// that class obviously - not good in a chain of setters mixed with setters from
	// *this* (the derived) class.

	// from ParameterValidator
	public Parameters setParameterName(String parameterName)
	{
		super.setName(parameterName);
		return this;
	}

	public Parameters setAllowNullStructure(Boolean allowNullStructure)
	{
		super.setAllowNullStructure(allowNullStructure);
		return this;
	}

	public Parameters setCheckIndividualElements(Boolean checkIndividualElements)
	{
		super.setCheckIndividualElements(checkIndividualElements);
		return this;
	}

	public Parameters setLocale(Locale locale)
	{
		super.setLocale(locale);
		return this;
	}

	public Parameters setValidMaximumSize(Integer validMaximumSize)
	{
		super.setValidMaximumSize(validMaximumSize);
		return this;
	}

	public Parameters setValidMinimumSize(Integer validMinimumSize)
	{
		super.setValidMinimumSize(validMinimumSize);
		return this;
	}

	// from FieldValidator
	public Parameters setAllowNullElement(Boolean allowNull)
	{
		super.setAllowNullElement(allowNull);
		return this;
	}

	public Parameters setInvalidChoices(Object[] invalidChoices)
	{
		super.setInvalidChoices(invalidChoices);
		return this;
	}

	public Parameters setInvalidMaximum(Double invalidMaximum)
	{
		super.setInvalidMaximum(invalidMaximum);
		return this;
	}

	public Parameters setInvalidMinimum(Double invalidMinimum)
	{
		super.setInvalidMinimum(invalidMinimum);
		return this;
	}

	public Parameters setInvalidRegex(String invalidRegex)
	{
		super.setInvalidRegex(invalidRegex);
		return this;
	}

	public Parameters setUseInvalidChoicesAs(String useInvalidChoicesAs)
	{
		super.setUseInvalidChoicesAs(useInvalidChoicesAs);
		return this;
	}

	public Parameters setUseValidChoicesAs(String useValidChoicesAs)
	{
		super.setUseValidChoicesAs(useValidChoicesAs);
		return this;
	}

	public Parameters setValidChoices(Object[] validChoices)
	{
		super.setValidChoices(validChoices);
		return this;
	}

	public Parameters setMaxFracLength(Integer maxFracLength)
	{
		super.setMaxFracLength(maxFracLength);
		return this;
	}

	public Parameters setValidMaximum(Double validMaximum)
	{
		super.setValidMaximum(validMaximum);
		return this;
	}

	public Parameters setValidMinimum(Double validMinimum)
	{
		super.setValidMinimum(validMinimum);
		return this;
	}

	public Parameters setValidRegex(String validRegex)
	{
		super.setValidRegex(validRegex);
		return this;
	}

	public Parameters setInvalidEarliest(Timestamp invalidEarliest)
	{
		super.setInvalidEarliest(invalidEarliest);
		return this;
	}

	public Parameters setInvalidLatest(Timestamp invalidLatest)
	{
		super.setInvalidLatest(invalidLatest);
		return this;
	}

	public Parameters setValidEarliest(Timestamp validEarliest)
	{
		super.setValidEarliest(validEarliest);
		return this;
	}

	public Parameters setValidLatest(Timestamp validLatest)
	{
		super.setValidLatest(validLatest);
		return this;
	}

	// ----- Get simple types from parameters ---------------------------------
	public Byte getByte(String name) { return (Byte) parameters.get(name); }
	public Short getShort(String name) { return (Short) parameters.get(name); }
	public Integer getInteger(String name) { return (Integer) parameters.get(name); }
	public Long getLong(String name) { return (Long) parameters.get(name); }
	public Float getFloat(String name) { return (Float) parameters.get(name); }
	public Double getDouble(String name) { return (Double) parameters.get(name); }
	public Boolean getBoolean(String name) { return (Boolean) parameters.get(name); }
	public String getString(String name) { return (String) parameters.get(name); }
	public Date getDate(String name) { return (Date) parameters.get(name); }
	public Timestamp getTimestamp(String name) { return (Timestamp) parameters.get(name); }

	// ----- Get array types from parameters ----------------------------------
	public Byte[] getByteArray(String name) { return (Byte[]) parameters.get(name); }
	public Short[] getShortArray(String name) { return (Short[]) parameters.get(name); }
	public Integer[] getIntegerArray(String name) { return (Integer[]) parameters.get(name); }
	public Long[] getLongArray(String name) { return (Long[]) parameters.get(name); }
	public Float[] getFloatArray(String name) { return (Float[]) parameters.get(name); }
	public Double[] getDoubleArray(String name) { return (Double[]) parameters.get(name); }
	public Boolean[] getBooleanArray(String name) { return (Boolean[]) parameters.get(name); }
	public String[] getStringArray(String name) { return (String[]) parameters.get(name); }
	public Date[] getDateArray(String name) { return (Date[]) parameters.get(name); }
	public Timestamp[] getTimestampArray(String name) { return (Timestamp[]) parameters.get(name); }

	// ----- Get collection types from parameters -----------------------------
	// see comment after class
	public Object[] getArray(String name) { return (Object[]) parameters.get(name); }
	public List getList(String name) { return (List) parameters.get(name); }
	public Map getMap(String name) { return (Map) parameters.get(name); }

	// ----- Mini helper method for checking name/value nulls -----------------
	protected void checkNulls(String name, Object value, String msgNameNull, String msgValueNull)
	{
		if (name == null) throw new IllegalArgumentException(msgNameNull);
		checkNullStructure(value, msgValueNull);
	}

	// ----- Set simple types into parameters ---------------------------------
	// Note: it is easy to see that generics would work well here. But the whole
	// point is to *prevent* unexpected types from creeping into the parameters.
	// Hence, the tedious - but hopefully safer - list of following methods.

	public void setByte(String name, Byte value)
	{
		checkNulls(name, value, "parameterName.Null", "number.Null");
		parameters.put(name, value);
	}
	public void setShort(String name, Short value)
	{
		checkNulls(name, value, "parameterName.Null", "number.Null");
		parameters.put(name, value);
	}
	public void setInteger(String name, Integer value)
	{
		checkNulls(name, value, "parameterName.Null", "number.Null");
		parameters.put(name, value);
	}
	public void setLong(String name, Long value)
	{
		checkNulls(name, value, "parameterName.Null", "number.Null");
		parameters.put(name, value);
	}
	public void setFloat(String name, Float value)
	{
		checkNulls(name, value, "parameterName.Null", "number.Null");
		parameters.put(name, value);
	}
	public void setDouble(String name, Double value)
	{
		checkNulls(name, value, "parameterName.Null", "number.Null");
		parameters.put(name, value);
	}
	public void setBoolean(String name, Boolean value)
	{
		checkNulls(name, value, "parameterName.Null", "boolean.Null");
		parameters.put(name, value);
	}
	public void setString(String name, String value)
	{
		checkNulls(name, value, "parameterName.Null", "string.Null");
		parameters.put(name, value);
	}
	public void setDate(String name, Date value)
	{
		checkNulls(name, value, "parameterName.Null", "date.Null");
		parameters.put(name, value);
	}
	public void setTimestamp(String name, Timestamp value)
	{
		checkNulls(name, value, "parameterName.Null", "timestamp.Null");
		parameters.put(name, value);
	}

	// ----- Set arrays into parameters ---------------------------------------
	public void setByteArray(String name, Byte[] value)
	{
		checkNulls(name, value, "parameterName.Null", "array.Null");
		checkArrayNullElements(value);
		parameters.put(name, value);
	}

	public void setShortArray(String name, Short[] value)
	{
		checkNulls(name, value, "parameterName.Null", "array.Null");
		checkArrayNullElements(value);
		parameters.put(name, value);
	}

	public void setIntegerArray(String name, Integer[] value)
	{
		checkNulls(name, value, "parameterName.Null", "array.Null");
		checkArrayNullElements(value);
		parameters.put(name, value);
	}

	public void setLongArray(String name, Long[] value)
	{
		checkNulls(name, value, "parameterName.Null", "array.Null");
		checkArrayNullElements(value);
		parameters.put(name, value);
	}

	public void setFloatArray(String name, Float[] value)
	{
		checkNulls(name, value, "parameterName.Null", "array.Null");
		checkArrayNullElements(value);
		parameters.put(name, value);
	}

	public void setDoubleArray(String name, Double[] value)
	{
		checkNulls(name, value, "parameterName.Null", "array.Null");
		checkArrayNullElements(value);
		parameters.put(name, value);
	}

	public void setBooleanArray(String name, Boolean[] value)
	{
		checkNulls(name, value, "parameterName.Null", "array.Null");
		checkArrayNullElements(value);
		parameters.put(name, value);
	}

	public void setStringArray(String name, String[] value)
	{
		checkNulls(name, value, "parameterName.Null", "array.Null");
		checkArrayNullElements(value);
		parameters.put(name, value);
	}

	public void setDateArray(String name, Date[] value)
	{
		checkNulls(name, value, "parameterName.Null", "array.Null");
		checkArrayNullElements(value);
		parameters.put(name, value);
	}

	public void setTimestampArray(String name, Timestamp[] value)
	{
		checkNulls(name, value, "parameterName.Null", "array.Null");
		checkArrayNullElements(value);
		parameters.put(name, value);
	}

	// ----- Set lists into parameters ----------------------------------------
	public void setByteList(String name, List<Byte> value)
	{
		checkNulls(name, value, "parameterName.Null", "list.Null");
		checkListNullElements(value);
		parameters.put(name, value);
	}

	public void setShortList(String name, List<Short> value)
	{
		checkNulls(name, value, "parameterName.Null", "list.Null");
		checkListNullElements(value);
		parameters.put(name, value);
	}

	public void setIntegerList(String name, List<Integer> value)
	{
		checkNulls(name, value, "parameterName.Null", "list.Null");
		checkListNullElements(value);
		parameters.put(name, value);
	}

	public void setLongList(String name, List<Long> value)
	{
		checkNulls(name, value, "parameterName.Null", "list.Null");
		checkListNullElements(value);
		parameters.put(name, value);
	}

	public void setFloatList(String name, List<Float> value)
	{
		checkNulls(name, value, "parameterName.Null", "list.Null");
		checkListNullElements(value);
		parameters.put(name, value);
	}

	public void setDoubleList(String name, List<Double> value)
	{
		checkNulls(name, value, "parameterName.Null", "list.Null");
		checkListNullElements(value);
		parameters.put(name, value);
	}

	public void setBooleanList(String name, List<Boolean> value)
	{
		checkNulls(name, value, "parameterName.Null", "list.Null");
		checkListNullElements(value);
		parameters.put(name, value);
	}

	public void setStringList(String name, List<String> value)
	{
		checkNulls(name, value, "parameterName.Null", "list.Null");
		checkListNullElements(value);
		parameters.put(name, value);
	}

	public void setDateList(String name, List<Date> value)
	{
		checkNulls(name, value, "parameterName.Null", "list.Null");
		checkListNullElements(value);
		parameters.put(name, value);
	}

	public void setTimestampList(String name, List<Timestamp> value)
	{
		checkNulls(name, value, "parameterName.Null", "list.Null");
		checkListNullElements(value);
		parameters.put(name, value);
	}

	// ----- Complicated methods for complicated structures -------------------
	// Complex arrays that have subArrays, subLists, and subMaps
	public void setArray(String name, Object[] value,
		Class<?>[] keyClasses, Class<?>[] valueClasses)
	{
		checkNulls(name, value, "parameterName.Null", "array.Null");
		if (value != null)
		{
			if (keyClasses.length != valueClasses.length)
				throw new IllegalArgumentException("map.KeyValueClassDisparity");
			checkArrayNullElements(value);
			checkArrayTypesRecursively(value, keyClasses, valueClasses, 0);
		}
		parameters.put(name, value);
	}

	// Complex lists that have subArrays, subLists, and subMaps
	public void setList(String name, List<?> value,
		Class<?>[] keyClasses, Class<?>[] valueClasses)
	{
		checkNulls(name, value, "parameterName.Null", "list.Null");
		if (value != null)
		{
			if (keyClasses.length != valueClasses.length)
				throw new IllegalArgumentException("map.KeyValueClassDisparity");
			checkListNullElements(value);
			checkListTypesRecursively(value, keyClasses, valueClasses, 0);
		}
		parameters.put(name, value);
	}
	
	// Complex maps that have subArrays, subLists, and subMaps
	public void setMap(String name, Map<?, ?> value,
		Class<?>[] keyClasses, Class<?>[] valueClasses)
	{
		checkNulls(name, value, "parameterName.Null", "map.Null");
		if (value != null)
		{
			if (keyClasses.length != valueClasses.length)
				throw new IllegalArgumentException("map.KeyValueClassDisparity");
			checkMapNullKeysValues(value);
			checkMapTypesRecursively(value, keyClasses, valueClasses, 0);
		}
		parameters.put(name, value);
	}

	/*

	private void test2()
	{
		Parameters ps = new Parameters();
		ps.setParameterName("array").setValidMinimum(3.5).setCheckIndividualElements(true);
		Integer array[] = { 2, 3, 5 };
		Map<String, List<String>> results = ps.checkIntegerArray(array);
		if (false && results != null && results.size() > 0)
		{
			for (String key : results.keySet())
			{
				System.out.println("Key: " + key);
				List<String> list = results.get(key);
				for (String s : list)
					System.out.println("   Result: " + s);
			}
		}
		else 
			System.out.println("Everything A-OK");
		
		ps.clearChecks().setParameterName("map").setCheckIndividualElements(true);
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("a", "b");
		map.put("b", null);
		Class<?> keyClasses[] = { String.class };
		Class<?> valueClasses[] = { String.class };
		results = ps.checkMap(map, keyClasses, valueClasses);
		if (results != null && results.size() > 0)
		{
			for (String key : results.keySet())
			{
				System.out.println("Key: " + key);
				List<String> list = results.get(key);
				for (String s : list)
					System.out.println("   Result: " + s);
			}
		}
		else 
			System.out.println("Everything A-OK");
	}
	private void test()
	{
		Parameters parameters = new Parameters();
		parameters.setByte("byte1", new Byte((byte)3));
		System.out.println(parameters.getByte("byte1"));
		
		Map<Long, Map<String, Vector<Long>>> lhm =
			new LinkedHashMap<Long, Map<String, Vector<Long>>>();
		Map<String, Vector<Long>> entity = new LinkedHashMap<String, Vector<Long>>();
		Vector<Long> v1 = new Vector<Long>(), v2 = new Vector<Long>();
		v1.add(100L);
		v1.add(200L);
		v2.add(5000L);
		v2.add(6000L);
		//entity.put("idk", new Long(33));
		entity.put("username", v1);
		entity.put("password", v2);
		//entity.put("age", 38);

		lhm.put(new Long(33), entity);
		Class<?>[] keyClasses = { Long.class, String.class, null };
		Class<?>[] valueClasses = { LinkedHashMap.class, Vector.class, Long.class }; 
		parameters.setMap("entityMaps", lhm, keyClasses, valueClasses);
		Map<Long, Map<String, Object>> lhm2 = parameters.getMap("entityMaps");
		for (Long k : lhm2.keySet())
			System.out.println("key: " + k + "<-->" + lhm2.get(k));
		
		if (LinkedHashMap.class.isAssignableFrom(Map.class))
			System.out.println("YES");
		else
			System.out.println("NO");
	}
	*/
}
/* Since checking the type safety from Object to List<T> can't occur (it can 
 * only do a check for Object to List), scrap the below for the above.
public List<Byte> getByteList(String name) { return (List<Byte>) parameters.get(name); }
public List<Short> getShortList(String name) { return (List<Short>) parameters.get(name); }
public List<Integer> getIntegerList(String name) { return (List<Integer>) parameters.get(name); }
public List<Long> getLongList(String name) { return (List<Long>) parameters.get(name); }
public List<Float> getFloatList(String name) { return (List<Float>) parameters.get(name); }
public List<Double> getDoubleList(String name) { return (List<Double>) parameters.get(name); }
public List<Boolean> getBooleanList(String name) { return (List<Boolean>) parameters.get(name); }
public List<String> getStringList(String name) { return (List<String>) parameters.get(name); }
public List<Date> getDateList(String name) { return (List<Date>) parameters.get(name); }
public List<Timestamp> getTimestampList(String name) { return (List<Timestamp>) parameters.get(name); }
*/
