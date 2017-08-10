package com.tek.muleautomator.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.tek.muleautomator.handler.JMSHandler;
import com.tek.muleautomator.mvn.MuleProjectSetup;
import com.tek.muleautomator.service.FileService;
import com.tek.muleautomator.service.JMSService;

public class MuleAutomateManager {

	public static void main(String args[]) {
		try {
			String seperator = File.separator; 
			String tibcoProcessLocation = "D://Migration//Sample//FileProject//ProcessDefinition.process";
			String tibcoProjectLocationRootFolder = "D://Migration//Sample";
			String workspace = "D://mule";
			String projectName = getProjectName(tibcoProcessLocation);
			String muleProjectLocation = workspace+seperator+projectName;
			createMuleProject(tibcoProjectLocationRootFolder, projectName, workspace);
			generateMuleFlowFromTibcoProcess(tibcoProcessLocation, muleProjectLocation);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Method to get a List of All textContent inside "pd:type" tag.
	 * @param doc Parsed DOM Document
	 * @param tag Tag inside which we expect "pd:type"
	 * @return List of type String containing the type
	 */

	public static List<String> getActivityTypes(Document doc, String tag) {
		NodeList starterNodeList = doc.getElementsByTagName(tag);
		List<String> activityTypes = new ArrayList<>();
		for (int count = 0; count < starterNodeList.getLength(); count++) {
			Node tempNode = starterNodeList.item(count);
			Element tempNodeElement = (Element) tempNode;
			String activityType = tempNodeElement.getElementsByTagName("pd:type").item(0).getTextContent();
			activityTypes.add(activityType);
		}
		return activityTypes;
	}

	private static String getPluginType(String a){
        return a.substring(a.indexOf("plugin.")+"plugin.".length(),a.lastIndexOf("."));
    }
	
	public static void generateMuleFlowFromTibcoProcess(String tibcoProcessPath, String muleProjectLocation) {
		String pluginType=null;
		try {
			JMSService jmsService = new JMSService();
			FileService fileService = new FileService();
			//generateJMSMuleFlowFromTibco.jmsConfiguration(muleProjectLocation);
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document docIn = documentBuilder.parse(new File(tibcoProcessPath));
			NodeList starterNodeList = docIn.getElementsByTagName("pd:starter");

			// Dealing with the Starter Tags
			List<String> starterNodeTypesList=getActivityTypes(docIn, "pd:starter");
			for(String activityType:starterNodeTypesList){
				if (activityType.equals("com.tibco.plugin.jms.JMSQueueEventSource")) {
					System.out.println("com.tibco.plugin.jms.JMSQueueEventSource-----This actvity will be used to receive jms messages from jms/tibco ems servers");
					jmsService.jmsSubscribe(muleProjectLocation);
				}
			}
			
			// Dealing with the Activity Tags
			List<String> activityNodeTypesList=getActivityTypes(docIn, "pd:activity");
			for(String activityType: activityNodeTypesList){

				pluginType = getPluginType(activityType);
				switch(pluginType){
					case "jms": JMSHandler.generateMuleFlow(activityType);
						break;
					// case "file": FileHandler.generateMuleFlow(activityType);
					// break;
				}
				

				//**********************************File Activities*************************************


				if(activityType.equals("com.tibco.plugin.file.FileCreateActivity")) {
					System.out.println("com.tibco.plugin.file.FileCreateActivity-----The Create File activity creates a new file or directory with the specified name. When creating a file, you can also provide the file contents. ");
					fileService.fileCreate(muleProjectLocation);
				}

				if(activityType.equals("com.tibco.plugin.file.ListFilesActivity")) {
					System.out.println("com.tibco.plugin.file.ListFilesActivity-----The List Files activity returns information about files or directories, or a listing of all the files in the specified directory. ");
				}

				if(activityType.equals("com.tibco.plugin.file.FileReadActivity")) {
					System.out.println("com.tibco.plugin.file.FileReadActivity-----The Read File activity is used to read a file and place its contents into the activity�s output. ");
				}

				if(activityType.equals("com.tibco.plugin.file.FileRemoveActivity")) {
					System.out.println("com.tibco.plugin.file.FileRemoveActivity-----The Remove File activity removes the specified file. This activity can also remove empty directories. If a directory that is not empty is specified, an exception is thrown. ");
					fileService.fileDelete(muleProjectLocation);
				}

				if(activityType.equals("com.tibco.plugin.file.FileRenameActivity")) {
					System.out.println("com.tibco.plugin.file.FileRenameActivity-----The Rename File activity is used to rename or move files. This activity can also rename directories, but you cannot use this activity to move a directory to a new location. ");
				}

				if(activityType.equals("com.tibco.plugin.file.FileSignalIn")) {
					System.out.println("com.tibco.plugin.file.FileSignalIn-----The Wait for File Change activity waits for a file creation, modification, or deletion event to occur during process execution. When this activity is executed, the process instance suspends and waits for the specified change to occur before resuming.");
				}

				if(activityType.equals("com.tibco.plugin.file.FileWriteActivity")) {
					System.out.println("com.tibco.plugin.file.FileWriteActivity-----The Write File activity writes content to the specified file.");
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static String getProjectName(String path) {
		return path.substring(path.lastIndexOf('/') + 1).split("\\.")[0];
	}

	private static void createMuleProject(String tibcoProjectLocationRootFolder, String projectName, String directory) throws IOException {
		MuleProjectSetup muleProjectSetup = new MuleProjectSetup();
		muleProjectSetup.createMuleProject(tibcoProjectLocationRootFolder, directory, projectName);
	}


}
