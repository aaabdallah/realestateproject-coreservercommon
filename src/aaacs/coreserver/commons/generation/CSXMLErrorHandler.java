package aaacs.coreserver.commons.generation;

import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author Ahmed A. Abd-Allah, Nov 7, 2006
 * 
 * A simple error handler that was taken originally from a file called
 * SimpleErrorHandler. Unsure of where I got that file though. Cleaned it
 * up, and added the error counter.
 */
public class CSXMLErrorHandler extends DefaultHandler
{
	private int errors = 0;
	
	public int getErrors()
	{
		return errors;
	}

	private String makeDescription(SAXParseException e)
	{
		return String.format(" [L%d, C%d]: %s\n   %s\n", e.getLineNumber(), 
			e.getColumnNumber(), e.getSystemId(), e.getMessage());
	}

	public void warning(SAXParseException e)
	{
		System.out.print("---> WARNING " + makeDescription(e));
	}

	public void error(SAXParseException e)
	{
		System.out.print("---> ERROR " + makeDescription(e));
		errors++;
	}

	public void fatalError(SAXParseException e)
	{
		System.out.print("---> FATAL " + makeDescription(e));
		errors++;
	}
}
