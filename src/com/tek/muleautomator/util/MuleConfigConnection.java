package com.tek.muleautomator.util;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;


public class MuleConfigConnection {

	private static String directory = System.getProperty("user.dir");
	private static String seperator = File.separator;
	public static MuleConfigConnection domConfig;
	/*public static String filepath=directory + seperator + "src" + seperator + "main" + seperator
			+ "app" + seperator + "mule-config.xml";
*/

	public static synchronized MuleConfigConnection getDomObj() {
		if ( domConfig == null ) {
			domConfig = new MuleConfigConnection();
		}
		return domConfig;

	}
	public Document getDomConfig(String filepath) throws Exception{
		
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;
		docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(filepath);
		return doc;
	}
	public void trasfromData(Document doc,String filepath){
		try{
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(filepath));
			transformer.transform(source, result);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

	}
}
