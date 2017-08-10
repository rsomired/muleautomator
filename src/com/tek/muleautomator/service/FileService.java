package com.tek.muleautomator.service;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import com.tek.muleautomator.util.MuleConfigConnection;

public class FileService {

	private static String directory = System.getProperty("user.dir");

	private static String seperator = File.separator;

	public void fileCopy(String muleProjectLocation) {
		try {
			MuleConfigConnection dom=MuleConfigConnection.getDomObj();
			Document doc=dom.getDomConfig(muleProjectLocation);
			Element Mule = (Element) doc.getFirstChild();

			Element fileFlow = doc.createElement("flow");
			// add attributes
			fileFlow.setAttribute("name", "copyFile");
			
			Element fileInBound=doc.createElement("file:inbound-endpoint");
			fileInBound.setAttribute("responseTimeout", "10000");
			fileInBound.setAttribute("doc:name", "File");
			fileInBound.setAttribute("path", "C:/Users/nshaik/Desktop/source");
			fileFlow.appendChild(fileInBound);

			Element loggerElement=doc.createElement("logger");
			loggerElement.setAttribute("message", "#[payload]");
			loggerElement.setAttribute("level", "INFO");
			loggerElement.setAttribute("doc:name", "Logger");
			fileFlow.appendChild(loggerElement);

			Element fileOutBound=doc.createElement("file:outbound-endpoint");
			fileOutBound.setAttribute("responseTimeout", "10000");
			fileOutBound.setAttribute("doc:name", "File");
			fileOutBound.setAttribute("path", "C:/Users/nshaik/Desktop/Destination");
			fileFlow.appendChild(fileOutBound);
			Mule.appendChild(fileFlow);
			dom.trasfromData(doc,muleProjectLocation);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	public void fileCreate(String muleProjectLocation) {
		try {
			MuleConfigConnection dom=MuleConfigConnection.getDomObj();
			Document doc=dom.getDomConfig(muleProjectLocation);
			
			Element Mule = (Element) doc.getFirstChild();
			Element fileCreateFlow = doc.createElement("flow");
			// add attributes
			fileCreateFlow.setAttribute("name", "createFile");
			Element filesetPayload=doc.createElement("set-payload");
			filesetPayload.setAttribute("value", "abc");
			filesetPayload.setAttribute("doc:name", "Set Payload");
			fileCreateFlow.appendChild(filesetPayload);

			Element setVariableFileName=doc.createElement("set-variable");
			setVariableFileName.setAttribute("variableName", "fileName");
			setVariableFileName.setAttribute("value", "abc.txt");
			setVariableFileName.setAttribute("doc:name", "Variable");
			fileCreateFlow.appendChild(setVariableFileName);

			Element setVariableFolderName=doc.createElement("set-variable");
			setVariableFolderName.setAttribute("variableName", "folderName");
			setVariableFolderName.setAttribute("value", "sample");
			setVariableFolderName.setAttribute("doc:name", "Variable");
			fileCreateFlow.appendChild(setVariableFolderName);

			Element fileOutBound=doc.createElement("file:outbound-endpoint");
			fileOutBound.setAttribute("responseTimeout", "10000");
			fileOutBound.setAttribute("doc:name", "File");
			fileOutBound.setAttribute("outputPattern", "#[flowVars.fileName]");
			fileOutBound.setAttribute("path", "D:/mulFileCreation/#[flowVars.folderName]");
			fileCreateFlow.appendChild(fileOutBound);

			Mule.appendChild(fileCreateFlow);
			dom.trasfromData(doc,muleProjectLocation);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	public void fileDelete(String muleProjectLocation) {
		try {
			MuleConfigConnection dom=MuleConfigConnection.getDomObj();
			Document doc=dom.getDomConfig(muleProjectLocation);
			Element Mule = (Element) doc.getFirstChild();


			Element fileConnector = doc.createElement("file:connector");
			fileConnector.setAttribute("name", "File");
			fileConnector.setAttribute("autoDelete", "true");
			fileConnector.setAttribute("streaming", "false");
			fileConnector.setAttribute("validateConnections", "true");
			fileConnector.setAttribute("doc:name", "File");

			Element fileDelteFlow = doc.createElement("flow");
			// add attributes
			fileDelteFlow.setAttribute("name", "deleteFile");

			Element fileInBound=doc.createElement("file:inbound-endpoint");
			fileInBound.setAttribute("responseTimeout", "10000");
			fileInBound.setAttribute("moveToPattern", ".txt");
			fileInBound.setAttribute("connector-ref", "File");
			fileInBound.setAttribute("doc:name", "File");
			fileInBound.setAttribute("path", "D:/mulFileCreation/sample/");
			fileDelteFlow.appendChild(fileInBound);

			Element filesetPayload=doc.createElement("set-payload");
			filesetPayload.setAttribute("value", "fileDeleted");
			filesetPayload.setAttribute("doc:name", "Set Payload");
			fileDelteFlow.appendChild(filesetPayload);
			Mule.appendChild(fileDelteFlow);
			Mule.appendChild(fileConnector);
			dom.trasfromData(doc,muleProjectLocation);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

	}
	
}
