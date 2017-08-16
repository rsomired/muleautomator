package com.tek.muleautomator.service;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.tek.muleautomator.util.MuleAutomatorUtil;
import com.tek.muleautomator.util.MuleConfigConnection;
import com.tek.muleautomator.element.FileElement.FileCreateActivity;
import com.tek.muleautomator.element.FileElement.FileReadActivity;
import com.tek.muleautomator.element.FileElement.FileRemoveActivity;
import com.tek.muleautomator.element.FileElement.FileRenameActivity;
import com.tek.muleautomator.element.FileElement.FileWriteActivity;

public class FileService {

	public void fileCopy(String muleConfigPath, Element flow) {
		try {
			Document doc = MuleConfigConnection.getDomObj(muleConfigPath);

			Element fileInBound=doc.createElement("file:inbound-endpoint");
			fileInBound.setAttribute("responseTimeout", "10000");
			fileInBound.setAttribute("doc:name", "File");
			fileInBound.setAttribute("path", "C:/Users/nshaik/Desktop/source");
			flow.appendChild(fileInBound);

			MuleAutomatorUtil.loggerElement(muleConfigPath,flow);

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

			MuleAutomatorUtil.loggerElement(muleConfigPath,flow);

			Element fileOutBound=doc.createElement("file:outbound-endpoint");
			fileOutBound.setAttribute("responseTimeout", "10000");
			fileOutBound.setAttribute("doc:name", "File");
			fileOutBound.setAttribute("outputPattern", "abc.txt");
			fileOutBound.setAttribute("path", "C:/Users/nshaik/Desktop/File");
			flow.appendChild(fileOutBound);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	public void fileWrite(String muleConfigPath, FileWriteActivity fileWriteActivity, Element flow) {

		try {
			Document doc = MuleConfigConnection.getDomObj(muleConfigPath);


			Element filesetPayload=doc.createElement("set-payload");
			filesetPayload.setAttribute("value", "sample");
			filesetPayload.setAttribute("fileName", fileWriteActivity.getFileName());
			filesetPayload.setAttribute("doc:name", "Set Payload");
			flow.appendChild(filesetPayload);

			MuleAutomatorUtil.loggerElement(muleConfigPath,flow);

			Element fileOutBound=doc.createElement("file:outbound-endpoint");
			fileOutBound.setAttribute("responseTimeout", "10000");
			fileOutBound.setAttribute("doc:name", "File");
			fileOutBound.setAttribute("outputPattern", "abc.txt");
			fileOutBound.setAttribute("path", "C:/Users/nshaik/Desktop/File");
			flow.appendChild(fileOutBound);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}


	public void fileDelete(String muleConfigPath,FileRemoveActivity fileRemoveActivity, Element flow) {

		try {
			Document doc = MuleConfigConnection.getDomObj(muleConfigPath);

			Element muleTag=(Element)doc.getFirstChild();

			Element fileConnector = doc.createElement("file:connector");
			fileConnector.setAttribute("name", "File");
			fileConnector.setAttribute("autoDelete", "true");
			fileConnector.setAttribute("streaming", "false");
			fileConnector.setAttribute("validateConnections", "true");
			fileConnector.setAttribute("doc:name", "File");
			muleTag.appendChild(fileConnector);

			Element fileInBound=doc.createElement("file:inbound-endpoint");
			fileInBound.setAttribute("responseTimeout", "10000");
			fileInBound.setAttribute("moveToPattern", ".txt");
			fileInBound.setAttribute("connector-ref", "File");
			fileInBound.setAttribute("doc:name", "File");
			fileInBound.setAttribute("path", "C:/Users/nshaik/Desktop/File");
			flow.appendChild(fileInBound);

			MuleAutomatorUtil.loggerElement(muleConfigPath,flow);

		} catch (Exception e) {
			e.printStackTrace();
		} 

	}

	public void fileRead(String muleConfigPath, FileReadActivity fileReadActivity, Element flow) {

		try {
			Document doc = MuleConfigConnection.getDomObj(muleConfigPath);


			Element fileInBound=doc.createElement("file:inbound-endpoint");
			fileInBound.setAttribute("responseTimeout", "10000");
			fileInBound.setAttribute("doc:name", "File");
			fileInBound.setAttribute("path", "C:/Users/nshaik/Desktop/source");
			fileInBound.setAttribute("moveToDirectory", "C:/Users/nshaik/Desktop/Destination");
			flow.appendChild(fileInBound);

			MuleAutomatorUtil.loggerElement(muleConfigPath,flow);

		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	public void fileRename(String muleConfigPath, FileRenameActivity fileRenameActivity, Element flow) {

		try {
			Document doc = MuleConfigConnection.getDomObj(muleConfigPath);
			
			Element muleTag=(Element)doc.getFirstChild();

			Element fileConnector = doc.createElement("file:connector");
			fileConnector.setAttribute("name", "File1");
			fileConnector.setAttribute("autoDelete", "true");
			fileConnector.setAttribute("streaming", "true");
			fileConnector.setAttribute("validateConnections", "true");
			fileConnector.setAttribute("doc:name", "File");
			fileConnector.setAttribute("workFileNamePattern", "MyText.txt");
			
			muleTag.appendChild(fileConnector);

			Element fileInBound=doc.createElement("file:inbound-endpoint");
			fileInBound.setAttribute("responseTimeout", "10000");
			fileInBound.setAttribute("doc:name", "File");
			fileInBound.setAttribute("connector-ref", "File1");
			fileInBound.setAttribute("path", "C:/Users/nshaik/Desktop/source");
			fileInBound.setAttribute("moveToDirectory", "C:/Users/nshaik/Desktop/source");
			flow.appendChild(fileInBound);

			MuleAutomatorUtil.loggerElement(muleConfigPath,flow);

		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	public void filePolling(String muleConfigPath,Element flow) {

		try {
			Document doc = MuleConfigConnection.getDomObj(muleConfigPath);


			Element fileInBound=doc.createElement("file:inbound-endpoint");
			fileInBound.setAttribute("responseTimeout", "10000");
			fileInBound.setAttribute("doc:name", "File");
			fileInBound.setAttribute("pollingFrequency", "10000");
			fileInBound.setAttribute("path", "C:/Users/nshaik/Desktop/source");
			fileInBound.setAttribute("moveToDirectory", "C:/Users/nshaik/Desktop/Destination");
			flow.appendChild(fileInBound);

			MuleAutomatorUtil.loggerElement(muleConfigPath,flow);

		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

}
