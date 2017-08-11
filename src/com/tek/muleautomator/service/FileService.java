package com.tek.muleautomator.service;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.tek.muleautomator.util.MuleConfigConnection;
import com.tek.muleautomator.element.FileElement.FileCreateActivity;
import com.tek.muleautomator.element.FileElement.FileRemoveActivity;
import com.tek.muleautomator.util.MuleAutomatorConstants;

public class FileService {

	public void fileCopy(String muleConfigPath, Element flow) {
		try {
			Document doc = MuleConfigConnection.getDomObj(muleConfigPath);
			
			Element fileInBound=doc.createElement("file:inbound-endpoint");
			fileInBound.setAttribute("responseTimeout", "10000");
			fileInBound.setAttribute("doc:name", "File");
			fileInBound.setAttribute("path", "C:/Users/nshaik/Desktop/source");

			flow.appendChild(fileInBound);

			Element loggerElement=doc.createElement("logger");
			loggerElement.setAttribute("message", "#[payload]");
			loggerElement.setAttribute("level", "INFO");
			loggerElement.setAttribute("doc:name", "Logger");
			flow.appendChild(loggerElement);

			Element fileOutBound=doc.createElement("file:outbound-endpoint");
			fileOutBound.setAttribute("responseTimeout", "10000");
			fileOutBound.setAttribute("doc:name", "File");
			fileOutBound.setAttribute("path", "C:/Users/nshaik/Desktop/Destination");
			flow.appendChild(fileOutBound);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}


	public void fileCreate(String muleConfigPath, FileCreateActivity fileCreateActivity, Element flow) {

		try {
			Document doc = MuleConfigConnection.getDomObj(muleConfigPath);
			

			Element filesetPayload=doc.createElement("set-payload");
			filesetPayload.setAttribute("value", "abc");
			filesetPayload.setAttribute("fileName", fileCreateActivity.getFileName());
			filesetPayload.setAttribute("doc:name", "Set Payload");

			flow.appendChild(filesetPayload);

			Element setVariableFileName=doc.createElement("set-variable");
			setVariableFileName.setAttribute("variableName", "fileName");
			setVariableFileName.setAttribute("value", "abc.txt");
			setVariableFileName.setAttribute("doc:name", "Variable");
			flow.appendChild(setVariableFileName);

			Element setVariableFolderName=doc.createElement("set-variable");
			setVariableFolderName.setAttribute("variableName", "folderName");
			setVariableFolderName.setAttribute("value", "sample");
			setVariableFolderName.setAttribute("doc:name", "Variable");
			flow.appendChild(setVariableFolderName);

			Element fileOutBound=doc.createElement("file:outbound-endpoint");
			fileOutBound.setAttribute("responseTimeout", "10000");
			fileOutBound.setAttribute("doc:name", "File");
			fileOutBound.setAttribute("outputPattern", "#[flowVars.fileName]");
			fileOutBound.setAttribute("path", "D:/mulFileCreation/#[flowVars.folderName]");
			flow.appendChild(fileOutBound);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}


	public void fileDelete(String muleConfigPath,FileRemoveActivity fileDeleteActivity, Element flow) {

		try {
			Document doc = MuleConfigConnection.getDomObj(muleConfigPath);
			
			Element fileConnector = doc.createElement("file:connector");
			fileConnector.setAttribute("name", "File");
			fileConnector.setAttribute("autoDelete", "true");
			fileConnector.setAttribute("streaming", "false");
			fileConnector.setAttribute("validateConnections", "true");
			fileConnector.setAttribute("doc:name", "File");
			flow.appendChild(fileConnector);

			Element fileInBound=doc.createElement("file:inbound-endpoint");
			fileInBound.setAttribute("responseTimeout", "10000");
			fileInBound.setAttribute("moveToPattern", ".txt");
			fileInBound.setAttribute("connector-ref", "File");
			fileInBound.setAttribute("doc:name", "File");
			fileInBound.setAttribute("path", "D:/mulFileCreation/sample/");
			flow.appendChild(fileInBound);

			Element filesetPayload=doc.createElement("set-payload");
			filesetPayload.setAttribute("value", "fileDeleted");
			filesetPayload.setAttribute("doc:name", "Set Payload");
			flow.appendChild(filesetPayload);
		} catch (Exception e) {
			e.printStackTrace();
		} 

	}
	
}
