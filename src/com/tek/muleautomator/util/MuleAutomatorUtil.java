package com.tek.muleautomator.util;

import java.io.File;
import java.util.List;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class MuleAutomatorUtil {

	public static void trasfromData(Document doc,String filepath){
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
	
	public static void loggerElement(String muleConfigPath, Element flow){
		
		try {
			Document doc = MuleConfigConnection.getDomObj(muleConfigPath);
			Element loggerElement=doc.createElement("logger");
			loggerElement.setAttribute("message", "#[payload]");
			loggerElement.setAttribute("level", "INFO");
			loggerElement.setAttribute("doc:name", "Logger");
			flow.appendChild(loggerElement);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void fileFinder(File rootFile, List<File> fileList, String[] fileTypes) {
		if (rootFile == null)
			return;
		if (rootFile.isDirectory()) {
			for (int i = 0; i < rootFile.listFiles().length; ++i) {
				fileFinder(rootFile.listFiles()[i], fileList, fileTypes);
			}
		} else {
			for (String fileType : fileTypes) {
				if (!fileType.startsWith("."))
					fileType = "." + fileType;
				if (rootFile.getName().toLowerCase().endsWith(fileType)) {
					fileList.add(rootFile);
				}
			}
		}
	}

}
