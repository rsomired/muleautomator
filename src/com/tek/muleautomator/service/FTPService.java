package com.tek.muleautomator.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.tek.muleautomator.element.FTPElement.FTPGetActivity;
import com.tek.muleautomator.element.FTPElement.FTPPutActivity;
import com.tek.muleautomator.element.FTPElement.FTPRenameFileActivity;
import com.tek.muleautomator.util.MuleAutomatorConstants;
import com.tek.muleautomator.util.MuleAutomatorUtil;
import com.tek.muleautomator.util.MuleConfigConnection;

public class FTPService {

	public static String CONFIG_PORT, CONFIG_USERNAME, CONFIG_PASSWORD, CONFIG_HOST;

	public void ftpConfiguration(String muleConfigPath) throws ParserConfigurationException, SAXException, IOException {
		Document doc = null;
		try {
			doc = MuleConfigConnection.getDomObj(muleConfigPath);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<File> sharedFtpFiles = new ArrayList<>();
		Element muleTag = (Element) doc.getFirstChild();
		Element ftpConfigElement = doc.createElement("ftp:connector");
		MuleAutomatorUtil.fileFinder(new File(MuleAutomatorConstants.TIBCO_PROJECT_ROOT_FOLDER), sharedFtpFiles,
				new String[] { "sharedftp" });
		if (sharedFtpFiles.size() == 0) {
			System.err.println("Couldn't find Shared FTP configuration. Using Default FTP Config..");
			ftpConfigElement.setAttribute("doc:name", "FTP");
			ftpConfigElement.setAttribute("name", "FTP");
			ftpConfigElement.setAttribute("validateConnections", "true");
			ftpConfigElement.setAttribute("pollingFrequency", "1000");
			CONFIG_PORT = "21";
			CONFIG_USERNAME = "DEFAULT_USER";
			CONFIG_PASSWORD = "DEFAULT_PASSWORD";
			CONFIG_HOST = "localhost";
			
		} else {
			try {
				File currFile = sharedFtpFiles.get(0);
				DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
				Document ftpConfigDoc = documentBuilder.parse(currFile);

				CONFIG_PORT = ftpConfigDoc.getElementsByTagName("Port").item(0).getTextContent();
				CONFIG_USERNAME = ftpConfigDoc.getElementsByTagName("UserName").item(0).getTextContent();
				CONFIG_PASSWORD = ftpConfigDoc.getElementsByTagName("Password").item(0).getTextContent();
				CONFIG_HOST = ftpConfigDoc.getElementsByTagName("Host").item(0).getTextContent();
				
				ftpConfigElement.setAttribute("doc:name", "FTP");
				ftpConfigElement.setAttribute("name",ftpConfigDoc.getElementsByTagName("name").item(0).getTextContent());
				ftpConfigElement.setAttribute("validateConnections", "true");
				ftpConfigElement.setAttribute("pollingFrequency", "1000");
				
			} catch (Exception E) {
			}
		}
		muleTag.appendChild(ftpConfigElement);
	}

	public void ftpGet(String muleConfigPath, FTPGetActivity ftpGetActivity, Element flowElement) {
		/*
		 * <ftp:inbound-endpoint host="localhost" port="21" path="in"
		 * user="user2" password="1234" responseTimeout="10000" doc:name="FTP"/>
		 * <file:outbound-endpoint path="D:\Newfolder\out"
		 * outputPattern="#[message.inboundProperties.originalFilename]"
		 * responseTimeout="10000" doc:name="File"/>
		 * 
		
		 */

		try {
			if(isFTPConfigRequired(muleConfigPath)){
				ftpConfiguration(muleConfigPath);
			}
			
			
			Document doc = null;
			try {
				doc = MuleConfigConnection.getDomObj(muleConfigPath);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Element muleTag = (Element) doc.getFirstChild();
			Element ftpEl = doc.createElement("ftp:inbound-endbound");
			ftpEl.setAttribute("host", ftpGetActivity.getIN_host());
			ftpEl.setAttribute("port", "" + ftpGetActivity.getIN_port());
			ftpEl.setAttribute("user", ftpGetActivity.getIN_userName());
			ftpEl.setAttribute("path", ftpGetActivity.getIN_remoteFilename());
			ftpEl.setAttribute("password", ftpGetActivity.getIN_password());
			
		} catch (ParserConfigurationException | SAXException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		

	}

	public void ftpPut(String muleConfigPath, FTPPutActivity ftpPutActivity, Element flowElement) {

	}

	public void ftpSysType(String muleConfigPath, FTPRenameFileActivity ftpRenameFileActivity, Element flowElement) {

	}

	public boolean isFTPConfigRequired(String muleConfigPath)
			throws ParserConfigurationException, SAXException, IOException {
		Document doc;
		try {
			doc = MuleConfigConnection.getDomObj(muleConfigPath);
			NodeList nodeList = doc.getElementsByTagName("ftp:connector");
			return nodeList.getLength() == 0;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

}
