package aaacs.coreserver.commons.generation;

import java.io.File;
import java.util.LinkedHashMap;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Utilities
{
	static public String firstLetterToUpperCase(String word)
	{
		if (word == null || word.trim().equals(""))
			return word;
		
		return Character.toUpperCase(word.charAt(0)) + word.substring(1);
	}

	static public String firstLetterToLowerCase(String word)
	{
		if (word == null || word.trim().equals(""))
			return word;
		
		return Character.toLowerCase(word.charAt(0)) + word.substring(1);
	}

	static public String packStringList(String list)
	{
		if (list == null) return null;

		list = list.trim();
		if (list.equals("")) return "";
		
		String[] words = list.replaceAll("\\s+", " ").split(" ");
		String result = "";
		for (String word: words)
		{
			result += Character.toUpperCase(word.charAt(0));
			if (word.length() > 1)
				result += word.substring(1);
		}
		return result;
	}

	static public String whitespaceToCommas(String list)
	{
		if (list == null) return null;

		list = list.trim();
		if (list.equals("")) return "";
		
		String result = list.replaceAll("\\s+", ", ");
		return result;
	}
	
	static public String quoteSubStrings(String list)
	{
		if (list == null) return null;
		
		list = list.trim();
		if (list.equals("")) return "";
		
		return list.replaceAll("([^\\s]+)", "\"$1\"");
	}
	
	static public String sqlComment(String paragraph, int indent)
	{
		String tabs = "";
		for (int i=0; i<indent; i++)
			tabs += "\t";

		if (indent >= 0) // re-indent and comment
			paragraph = paragraph.trim().replaceAll("^[\t]*", tabs + "-- ").
				replaceAll("\n[\t]*", "\n" + tabs + "-- ");
		else // purely comment
			paragraph = ("-- " + paragraph).replaceAll("\n", "\n-- ");;
				
		return paragraph;
	}
	
	static public String javaComment(String paragraph, int indent)
	{
		String tabs = "";
		for (int i=0; i<indent; i++)
			tabs += "\t";

		if (indent >= 0) // re-indent and comment
			paragraph = paragraph.trim().replaceAll("^[\t]*", tabs + "// ").
				replaceAll("\n[\t]*", "\n" + tabs + "// ");
		else // purely comment
			paragraph = ("-- " + paragraph).replaceAll("\n", "\n// ");;
				
		return paragraph;
	}
	
	static public String javaDocComment(String paragraph, int indent)
	{
		String tabs = "";
		for (int i=0; i<indent; i++)
			tabs += "\t";

		if (indent >= 0) // re-indent and comment
			paragraph = tabs + "/**\n" + paragraph.trim().replaceAll("^[\t]*", tabs + " * ").
				replaceAll("\n[\t]*", "\n" + tabs + " * ") + "\n" + tabs + " */";
		else // purely comment
			paragraph = "/** " + paragraph.replaceAll("\n", "\n * ") + "\n */";
				
		return paragraph;
	}
	
	static public String javaCodeFragment(String paragraph, int baseIndent,
		boolean keepExtraIndents, String startComment, String endComment)
	{
		if (paragraph == null)
		{
			if (startComment == null && endComment == null)
				return null;
			paragraph = "";
		}

		String tabs = "";
		for (int i=0; i<baseIndent; i++)
			tabs += "\t";
		
		String startingTabsToReplace = "";

		// get rid of the very first return (but leave tabs)
		paragraph = paragraph.replaceFirst("^\n", "");
		// get rid of ALL ending whitespace
		paragraph = paragraph.replaceFirst("\\s*$", "");

		if (keepExtraIndents) // i.e. keep inner indents over and above 1st line's indents
		{
			// determine the starting number of tabs
			int j = 0;
			while (j<paragraph.length())
			{
				char c = paragraph.charAt(j++);
				if (c == '\t')
					startingTabsToReplace += "\t";
				else
					break;
			}
			if (startingTabsToReplace != "")
			{
				// re-indent the first line
				paragraph = paragraph.replaceFirst("^" + startingTabsToReplace, tabs);
				// re-indent the other lines
				paragraph = paragraph.replaceAll("\n" + startingTabsToReplace, "\n" + tabs);
			}
			else
			{
				// shift over the first line
				paragraph = tabs + paragraph;
				// shift all other lines
				paragraph = paragraph.replaceAll("\n([\t]*)", "\n" + tabs +"$1");
			}
		}
		else
		{
			startingTabsToReplace = "[\t]*";
			// strip all tabs from first line, re-indent
			paragraph = paragraph.replaceFirst("^" + startingTabsToReplace, tabs);
			// strip all tabs from inner line, re-indent
			paragraph = paragraph.replaceAll("\n" + startingTabsToReplace, "\n" + tabs);
		}

		if (startComment == null) startComment = "";
		else startComment = tabs + startComment + "\n";
		if (endComment == null) endComment = "";
		else endComment = "\n" + tabs + endComment;

		return startComment + paragraph + endComment;
	}
	
	static public LinkedHashMap<String, String> splitColumnValues(String columnValues)
	{
		if (columnValues == null) return null;
		columnValues = columnValues.trim();
		if (columnValues.equals("")) return null;

		try
		{
			LinkedHashMap<String, String> results = new LinkedHashMap<String, String>();
			int secondApostrophe = 0;
	
			while(columnValues.trim().length() != 0)
			{
				// get column
				String column = columnValues.substring(0, columnValues.trim().indexOf(' '));
				String value = null;
				columnValues = columnValues.substring(columnValues.indexOf(' ')).trim();
				
				// get value
				secondApostrophe = 1;
				while (true)
				{
					secondApostrophe = columnValues.indexOf('\'', secondApostrophe);
					if (secondApostrophe == columnValues.length() - 1 ||
						columnValues.charAt(secondApostrophe + 1) != '\'')
						break;
					secondApostrophe += 2;
				}
				value = columnValues.substring(0, secondApostrophe + 1);
				columnValues = columnValues.substring(secondApostrophe + 1).trim();
				results.put(column, value);
			}
			return results;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public static int getCommentIndent(Node commentNode)
		throws GenerationException
	{
		if (!commentNode.getNodeName().equals("comment"))
			throw new GenerationException("Expected a \"comment\" element! (Found " + 
				commentNode.getNodeName() + ")");

		try { return Integer.parseInt(getNodeAttribute(commentNode, "indent")); }
		catch (Exception e) { return -1; } // don't indent }
	}
	
	public static String getCommentName(Node commentNode)
		throws GenerationException
	{
		if (!commentNode.getNodeName().equals("comment"))
			throw new GenerationException("Expected a \"comment\" element! (Found " + 
				commentNode.getNodeName() + ")");

		return getNodeAttribute(commentNode, "name");
	}

	public static String getCommentType(Node commentNode)
		throws GenerationException
	{
		if (!commentNode.getNodeName().equals("comment"))
			throw new GenerationException("Expected a \"comment\" element! (Found " + 
				commentNode.getNodeName() + ")");
	
		return getNodeAttribute(commentNode, "type");
	}

	public static String getNodeAttribute(Node node, String attribute)
	{
		try
		{
			NamedNodeMap map = node.getAttributes();
		
			for (int j=0; j<map.getLength(); j++)
			{
				Node attr = map.item(j);
				if (attr.getNodeName().equals(attribute))
					return attr.getNodeValue(); 
			}
			return null;			
		}
		catch (Exception e)
		{
			return null;
		}
	}

	// Safe version - will not flame if node is null.
	public static String getNodeText(Node node)
	{
		try
		{
			return node.getTextContent().trim();			
		}
		catch (Exception e)
		{
			return null;
		}
	}
	
	public static String getNodeNumber(String cast, Node node)
	{
		if (cast == null)
			cast = "";
		cast = cast.trim();
		if (cast.equals("integer")) cast = "int";
		
		if (cast.length() != 0)
			cast = "(" + cast + ")";

		String text = getNodeText(node);
		if (text == null)
			return null;
		return cast + " " + text;
	}

	public static Node goToNode(Node startNode, String nodePath)
	{
		try
		{
			if (startNode == null)
				return null;
			if (nodePath == null)
				return startNode;
			nodePath = nodePath.trim();
			if (nodePath.equals(""))
				return startNode;

			int locator = nodePath.indexOf('.');
			String branchName = null;
			int branchNumber = -1;
			int branchCounter = 0;
			if (locator < 0)
			{
				branchName = nodePath;
				nodePath = null;
			}
			else
			{
				branchName = nodePath.substring(0, locator);
				nodePath = nodePath.substring(locator + 1);
			}
			
			locator = branchName.indexOf('[');
			if (locator > 0)
			{
				branchNumber = 
					Integer.parseInt(branchName.substring(locator+1, branchName.length()-2));
			}

			NodeList children = startNode.getChildNodes();
			for (int i=0; i<children.getLength(); i++)
			{
				Node childNode = children.item(i);
				if (childNode.getNodeName().equals(branchName))
				{
					if (branchNumber == -1 ||
						branchNumber == branchCounter)
						return goToNode(childNode, nodePath);
					branchCounter++;
				}
			}
			return null;
		}
		catch (Exception e)
		{
			return null;
		}
	}
	
	/**
	 * <p><strong>PLEASE USE THIS VERY VERY VERY VERY CAREFULLY!!!</strong></p>
	 * <p>Always double check the arguments you send in...</p>
	 * <p>And back up your data...</p>
	 * <p>Cos there aint no comin back from dis one...</p>
	 */
	public static void deleteAllFiles(File root, boolean leaveRoot, 
		boolean leaveSubdirectories, String[] filesToIgnore)
	{
		if (root == null) return;
		
		if (filesToIgnore != null)
		{
			for (String fileName : filesToIgnore)
			{
				try 
				{
					if (root.getCanonicalPath().endsWith(fileName))
						return;
				} 
				catch (Exception e) 
				{}
			}
		}
		
		if (root.isDirectory())
		{
			File[] subFiles = root.listFiles(); 
			if (subFiles != null)
			{
				for (File subFile : subFiles)
					deleteAllFiles(subFile, leaveSubdirectories, leaveSubdirectories, filesToIgnore);
			}
			if (!leaveRoot)
			{
				System.out.println("Deleting directory: " + root.getName());
				root.delete();
			}
			else
				System.out.println("Leaving directory: " + root.getName());
		}
		else
		{
			System.out.println("Deleting file: " + root.getName());
			root.delete();
		}
	}
}
