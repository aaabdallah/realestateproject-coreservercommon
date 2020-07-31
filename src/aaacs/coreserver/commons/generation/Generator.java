package aaacs.coreserver.commons.generation;

import java.io.File;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.validation.SchemaFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * @author Ahmed A. Abd-Allah, Nov 7, 2006
 * 
 * Generic code generator from XML files.
 */
public class Generator
{
	public static Document validateFile(File inputFile)
		throws GenerationException
	{
		try
		{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setIgnoringElementContentWhitespace(true);
			factory.setNamespaceAware(true);
			//factory.setValidating(true); // this is for validation against a DTD
			factory.setSchema(
				SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema());
		
			DocumentBuilder builder = factory.newDocumentBuilder();
			CSXMLErrorHandler errorHandler = new CSXMLErrorHandler();
			builder.setErrorHandler(errorHandler);
		
			Document document = builder.parse(inputFile);
			if (errorHandler.getErrors() > 0)
				return null;
		
			document.getDomConfig().setParameter("comments", false);
			//domConfig.setParameter("element-content-whitespace", false);
			
			document.normalizeDocument();
			return document;
		}
		catch (Exception e)
		{
			throw new GenerationException(e.getMessage());
		}
	}
		
	public static void stripEmptyTextNodes(Node rootNode)
	{
		//System.out.printf("Node Type: %s, Node Name: %s, Node Children: %d\n", 
		//	rootNode.getNodeType(), rootNode.getNodeName(), rootNode.getChildNodes().getLength());

		NodeList children = rootNode.getChildNodes();
		for (int i=0; i<children.getLength(); i++ ) 
		{
			Node child = children.item(i);
			// System.out.println("Examining node type: " + child.getNodeType());
			if (child.getNodeType() == Node.TEXT_NODE &&
				child.getTextContent().trim().length() == 0 &&
				child.getChildNodes().getLength() == 0)
				rootNode.removeChild(child);
		}
		children = rootNode.getChildNodes();
		for (int i=0; i<children.getLength(); i++ )
		{
			stripEmptyTextNodes(children.item(i));
		}
	}

	public static void traversePreOrder(Node rootNode)
	{

		NodeList children = rootNode.getChildNodes();
		for (int i=0; i<children.getLength(); i++ ) 
		{
			Node child = children.item(i);
			System.out.printf("NodeName: %s || NodeType: %s || " +
					"NodeTextContent: %s || ParentNodeName: %s || NodeChildren: %d\n", 
				child.getNodeName(), 
				child.getNodeType(),
				child.getTextContent(),
				child.getParentNode() != null ? child.getParentNode().getNodeName() : "<none>",
				child.getChildNodes().getLength());
			traversePreOrder(child);
		}
	}
}
