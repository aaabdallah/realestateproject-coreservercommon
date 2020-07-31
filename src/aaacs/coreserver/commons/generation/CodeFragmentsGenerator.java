package aaacs.coreserver.commons.generation;

import static aaacs.coreserver.commons.generation.Utilities.javaCodeFragment;

import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class CodeFragmentsGenerator extends Generator
{
	public static Map<String, Map<String, String>> allCodeFragments =
		new LinkedHashMap<String, Map<String, String>>();
	public static String CIP_METHOD = "method";
	public static String CIP_ONMODIFY = "onModify";
	public static String CIP_STATICMETHOD = "staticMethod";
	public static String CIP_GENERIC = "generic";

	public static String getCodeFragment(String name, Integer overrideBaseIndent, 
		Boolean overrideKeepExtraIndents, String overrideStartComment, 
		String overrideEndComment)
		throws GenerationException
	{
		Map<String, String> codeFragment = allCodeFragments.get(name);
		
		if (codeFragment != null)
		{
			int baseIndent = Integer.parseInt(codeFragment.get("baseIndent"));
			boolean keepExtraIndents = Boolean.parseBoolean(codeFragment.get("keepExtraIndents"));
			String startComment = codeFragment.get("startComment");
			String endComment = codeFragment.get("endComment");
			return javaCodeFragment(codeFragment.get("__CODE__"), 
				(overrideBaseIndent != null ? overrideBaseIndent : baseIndent),
				(overrideKeepExtraIndents != null ? overrideKeepExtraIndents : keepExtraIndents),
				(overrideStartComment != null ? overrideStartComment : startComment),
				(overrideEndComment != null ? overrideEndComment : endComment) + "\n");
		}
		throw new GenerationException("Unable to find code fragment with name: " + name);
	}

	private Map<String, Map<String, String>> codeFragments =
		new LinkedHashMap<String, Map<String, String>>();

	public CodeFragmentsGenerator(Node rootNode)
		throws GenerationException
	{
		extract(rootNode);
		for (String codeFragmentName : codeFragments.keySet())
		{
			if (allCodeFragments.containsKey(codeFragmentName))
				throw new GenerationException("Code fragment has already been defined for name: " + codeFragmentName);
		}
		allCodeFragments.putAll(codeFragments);
	}
	
	private void extract(Node codeFragmentsNode)
		throws GenerationException
	{
		if (codeFragmentsNode == null) // no code fragments.
			return;
			
		if (!codeFragmentsNode.getNodeName().equals("codeFragments"))
			throw new GenerationException("Expected a \"codeFragments\" element!" +
					" (found: " + codeFragmentsNode.getNodeName() + ")");
	
		NodeList children = codeFragmentsNode.getChildNodes();
		for (int i=0; i<children.getLength(); i++)
		{
			Node codeFragmentNode = children.item(i);
			Map<String, String> codeFragment = new LinkedHashMap<String, String>();
	
			String codeFragmentName = null;
	
			NamedNodeMap attrs = codeFragmentNode.getAttributes();
			for (int k=0; k<attrs.getLength(); k++)
			{
				Node attr = attrs.item(k);
	
				if (attr.getNodeName().equals("name"))
					codeFragmentName = attr.getNodeValue();
				else if (attr.getNodeName().equals("visibility"))
					codeFragment.put("visibility", attr.getNodeValue());
				else if (attr.getNodeName().equals("location"))
					codeFragment.put("location", attr.getNodeValue());
				else if (attr.getNodeName().equals("baseIndent"))
					codeFragment.put("baseIndent", attr.getNodeValue());
				else if (attr.getNodeName().equals("keepExtraIndents"))
					codeFragment.put("keepExtraIndents", attr.getNodeValue());
				else if (attr.getNodeName().equals("startComment"))
					codeFragment.put("startComment", attr.getNodeValue());
				else if (attr.getNodeName().equals("endComment"))
					codeFragment.put("endComment", attr.getNodeValue());
			}
			codeFragment.put("__CODE__", codeFragmentNode.getTextContent()); 
			codeFragments.put(codeFragmentName, codeFragment);
		}
	}
	
	public void insertCodeFragments(PrintWriter file, String visibility, String location)
	{
		int baseIndent = 0;
		boolean keepExtraIndents = true;
		String startComment = "", endComment = "";

		for (String codeFragmentName : codeFragments.keySet())
		{
			Map<String, String> codeFragment = codeFragments.get(codeFragmentName);
	
			if (codeFragment.get("visibility").matches(visibility) &&
				codeFragment.get("location").equals(location))
			{
				baseIndent = Integer.parseInt(codeFragment.get("baseIndent"));
				keepExtraIndents = Boolean.parseBoolean(codeFragment.get("keepExtraIndents"));
				startComment = codeFragment.get("startComment");
				endComment = codeFragment.get("endComment");
				file.println(javaCodeFragment(codeFragment.get("__CODE__"), 
					baseIndent, keepExtraIndents, startComment, endComment));
				file.println();
			}
		}		
	}
}
