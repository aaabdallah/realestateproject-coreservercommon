package aaacs.coreserver.commons.i18n;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * <p>This class is useful - or so it seems to me - to properly read in Arabic
 * resource bundles from property files written using the UTF-8 encoding, or any
 * multibyte encoding for that matter. The reason is the following snippet taken
 * from PropertyResourceBundle's description as of Java 1.6 at least:</p>
 * 
 * <p>"Note: PropertyResourceBundle can be constructed either from an InputStream or 
 * a Reader, which represents a property file. Constructing a PropertyResourceBundle 
 * instance from an InputStream requires that the input stream be encoded in 
 * ISO-8859-1. In that case, characters that cannot be represented in ISO-8859-1 
 * encoding must be represented by Unicode Escapes, whereas the other constructor 
 * which takes a Reader does not have that limitation."</p>
 * 
 * <p>As it turns out, the default behavior of ResourceBundle.Control is to use an
 * InputStream to represent the property file - a very questionable choice to use
 * as the default behavior. This means that property files for Arabic, Chinese,
 * Japanese and so on will be filled with Unicode Escapes, which is very tiresome.
 * True, Java does provide native2ascii to take care of that for you, but it is
 * tedious.</p>
 * 
 * <p>Therefore here is an implementation of ResourceBundle.Control that reads in
 * property files using a Reader, and can be configured with a particular character
 * encoding. The default encoding is in fact UTF-8.</p>
 * 
 * <p>Usage:</p>
 * <p><pre>
	ResourceBundle res = ResourceBundle.getBundle(
		"aaacs.simpleui.resources.ApplicationMessages",
		new Locale("ar"),
		SimpleClient.class.getClassLoader(),
		new CharsetAwareResourceBundleControl());
	</pre></p>
 * 
 * @author Abu Abd-Allah
 */
public class CharsetAwareResourceBundleControl extends ResourceBundle.Control
{
	Charset charset = null;
	
	public CharsetAwareResourceBundleControl()
	{
		this("UTF-8");
	}

	public CharsetAwareResourceBundleControl(String charsetName)
	{
		super();
		try { charset = Charset.forName(charsetName); }
		catch (Exception e) { charset = Charset.forName("UTF-8"); }
	}

	public Charset getCharset()
	{
		return charset;
	}

	public CharsetAwareResourceBundleControl setCharset(Charset charset)
	{
		this.charset = charset;
		return this;
	}

	public ResourceBundle newBundle(String baseName, Locale locale, String format, 
		ClassLoader loader, boolean reload)
		throws IllegalAccessException, InstantiationException, IOException
	{
		if (format == null || !format.equals("java.properties"))
			return super.newBundle(baseName, locale, format, loader, reload);
		if (baseName == null || locale == null || format == null || loader == null)
			throw new NullPointerException();
		ResourceBundle bundle = null;

		String bundleName = toBundleName(baseName, locale);
		String resourceName = toResourceName(bundleName, "properties");
		InputStream stream = null;
		if (reload)
		{
			URL url = loader.getResource(resourceName);
			if (url != null)
			{
				URLConnection connection = url.openConnection();
				if (connection != null)
				{
					// Disable caches to get fresh data for
					// reloading.
					connection.setUseCaches(false);
					stream = connection.getInputStream();
				}
			}
		}
		else
		{
			stream = loader.getResourceAsStream(resourceName);
		}
		if (stream != null)
		{
			BufferedInputStream bis = new BufferedInputStream(stream);
			bundle = new PropertyResourceBundle(new InputStreamReader(bis, charset));
			bis.close();
		}
		return bundle;
	}
}
