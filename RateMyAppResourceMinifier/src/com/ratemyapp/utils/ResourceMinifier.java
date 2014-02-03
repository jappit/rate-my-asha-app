package com.ratemyapp.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
 
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
 
public class ResourceMinifier
{
	static final ArrayList<String> VALID_RESOURCE_NAMES = new ArrayList<String>(Arrays.asList(new String[]{
		"FeedbackNo", "FeedbackMessage1", "FeedbackTitle", "FeedbackYes", "RatingNo", 
		"RatingMessage1", "RatingMessage2", "RatingTitle", "RatingYes", "FeedbackBody", 
		"FeedbackSubject"
	}));
	
 
	public static void main(String[] args) {
 
		if(args.length == 0)
		{
			System.out.println("Must specify the path of resource XML files");
			return;
		}
		String path = args[0];
		
		System.out.println("Resource path: " + path);
		
		try {
			
			File resourceFolder = new File(path);
			
			if(!resourceFolder.exists())
			{
				System.out.println("The specified resource path does not exist");
				return;
			}
			
			File[] resourceFiles = resourceFolder.listFiles();
			
			DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
 
			DocumentBuilder builder = documentFactory.newDocumentBuilder();
			
			for(int i = 0; i < resourceFiles.length; i++)
			{
				File file = resourceFiles[i];
				
				Document document = builder.parse(file);
				
				Node root = document.getChildNodes().item(0);
				
				NodeList dataElements = root.getChildNodes();
	 
				for(int j = dataElements.getLength() - 1; j >= 0; j--)
				{
					Node node = dataElements.item(j);
					
					boolean mustRemoveNode = true;
	 
					if(node.getNodeType() == Node.ELEMENT_NODE)
					{
						Element child = (Element)node;
						
						String nodeName = child.getNodeName();
						
						if(nodeName != null && nodeName.compareTo("data") == 0)
						{
							String resourceName = child.getAttribute("name");
							
							if(resourceName != null && VALID_RESOURCE_NAMES.contains(resourceName))
							{
								child.removeAttribute("xml:space");
								
								mustRemoveNode = false;
							}
						}
					}
					if(mustRemoveNode)
					{
						root.removeChild(node);
					}
				}
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource source = new DOMSource(document);
				
				StreamResult result =  new StreamResult(file);
				transformer.transform(source, result);
			}
			System.out.println("Done");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
 
	}
 
}