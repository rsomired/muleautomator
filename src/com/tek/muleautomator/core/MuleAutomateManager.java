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

import com.tek.muleautomator.handler.FileHandler;
import com.tek.muleautomator.handler.JMSHandler;
import com.tek.muleautomator.mvn.MuleProjectSetup;
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
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document docIn = documentBuilder.parse(new File(tibcoProcessPath));
			
			List<String> starterNodeTypesList=getActivityTypes(docIn, "pd:starter");
			/*for(String activityType:starterNodeTypesList){
				if (activityType.equals("com.tibco.plugin.jms.JMSQueueEventSource")) {
					System.out.println("com.tibco.plugin.jms.JMSQueueEventSource-----This actvity will be used to receive jms messages from jms/tibco ems servers");
					jmsService.jmsSubscribe(muleProjectLocation);
				}
			}*/
			
			List<String> activityNodeTypesList=getActivityTypes(docIn, "pd:activity");
			
			// Add Starter Nodes
			activityNodeTypesList.addAll(starterNodeTypesList);
			for(String activityType: activityNodeTypesList){

				pluginType = getPluginType(activityType);
				switch(pluginType){
					case "jms": JMSHandler.generateMuleFlow(activityType, muleProjectLocation);
						break;
					case "file": FileHandler.generateMuleFlow(activityType, muleProjectLocation);
					     break;
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
