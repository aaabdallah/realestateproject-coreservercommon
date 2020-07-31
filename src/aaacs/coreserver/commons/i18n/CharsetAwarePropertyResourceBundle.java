package aaacs.coreserver.commons.i18n;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.PropertyResourceBundle;

/**
 * <p>This is another solution to the problem outlined in 
 * <code>CharsetAwareResourceBundleControl</code>. Here, we extend PropertyResourceBundle
 * itself - even though their documentation ironically says that is not necessary.
 * The reason behind this solution is that there are situations where you simply cannot
 * configure the ResourceBundle.Control to use - notably in JSF, where the default
 * behavior is unchangeable as far as I can tell, without resorting to JSP scriptlets
 * that is. Unfortunately, loading in messages using faces-config.xml or using the
 * loadbundle tag both seem to force you into using the default ResourceBundle.Control,
 * which is frustrating with non-European languages (again, see
 * <code>CharsetAwareResourceBundleControl</code>).</p>
 * 
 * <p>To use this solution, wrap the properties file with a class with the target name
 * derived from this class. Hence, if you have "ApplicationMessages.properties", then
 * create "ApplicationMessages.class" in the same directory. The default behavior
 * of ResourceBundle.Control is to favor the class over the file. And this class is
 * UTF-8 (or other charset) aware, so the properties are ...properly... loaded in.</p>
 * 
 * <p>One caveat: this class assumes that you can correctly refer to the same directory
 * as the properties file to load it in. This is a bit tricky for web applications, you
 * will have to use ServletContext's <code>getRealPath()</code> to help in that 
 * situation...</p>
 * 
 * <p>Example:
 * <pre>
public class ApplicationMessages_ar extends CharsetAwarePropertyResourceBundle
{
	public ApplicationMessages_ar()
		throws IOException
	{
		super("C:/Sun/AppSrvr/domains/domain1/applications/j2ee-modules/SimpleUI/" +
			"WEB-INF/classes/aaacs/simpleui/resources/ApplicationMessages_ar.properties");
	}
}
</pre>
</p>

 * @author Abu Abd-Allah
 */
public class CharsetAwarePropertyResourceBundle extends PropertyResourceBundle
{
	private static String defaultPathPrefix = "";
	
	public static String getDefaultPathPrefix()
	{
		return defaultPathPrefix;
	}

	public static void setDefaultPathPrefix(String defaultPathPrefix)
	{
		if (defaultPathPrefix != null && defaultPathPrefix.trim().length() > 0)
			CharsetAwarePropertyResourceBundle.defaultPathPrefix = 
				defaultPathPrefix.trim() + "/";
	}

	public CharsetAwarePropertyResourceBundle(String pathPrefix, String propertyFileName, 
		String charsetName)
		throws IOException
	{
		super(new InputStreamReader(
			new FileInputStream(
				(pathPrefix != null && !pathPrefix.trim().equals("") ? 
					pathPrefix.trim() + "/" :
					defaultPathPrefix) +
				propertyFileName),
			Charset.forName(charsetName) ) );
		/*
		System.out.println("\n\n\n Inside CharsetAware... \n\n\n\n");
		String keyVals = "";
		for (String key : this.keySet())
		{
			keyVals += ("key: " + key + "<--->val: " + this.getString(key));
			keyVals += "[";
			for (char c : this.getString(key).toCharArray())
				keyVals += "\\u" + (int) c;
			keyVals += "]\n";
		}
		System.out.println("\n\n\nKey Values ------\n" + keyVals + "\n\n\n");
		*/
	}
	
	public CharsetAwarePropertyResourceBundle(String propertyFileName)
		throws IOException
	{
		this(null, propertyFileName, "UTF-8");
		/*
		System.out.println("\n\n\n Inside CharsetAware default... \n\n\n\n");
		*/
	}
}
