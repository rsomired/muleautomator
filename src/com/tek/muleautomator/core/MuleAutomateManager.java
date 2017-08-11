package com.tek.muleautomator.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

import com.tek.muleautomator.element.ActivityElement;
import com.tek.muleautomator.handler.FileHandler;
import com.tek.muleautomator.handler.JMSHandler;
import com.tek.muleautomator.mvn.MuleProjectSetup;
import com.tek.muleautomator.util.MuleAutomatorConstants;
import com.tek.muleautomator.util.MuleConfigConnection;

public class MuleAutomateManager {

	/**
	 * Method to execute mule automator application.
	 * @param tibco project root folder.
	 * @param tibco process location.
	 */

	public static void main(String args[]) {
		Element flowElement = null;
		try {

			String seperator = File.separator; 
			String tibcoProjectLocationRootFolder = "C:/Users/asgupta/Desktop/Sample";
			String tibcoProcessLocation = "C:/Users/asgupta/Desktop/Sample/FileProject/ProcessDefinition.process";
			String workspace = "D://mule4";
/*
			/*String tibcoProjectLocationRootFolder = "D:/Migration/Sample";
			String tibcoProcessLocation = "D:/Migration/Sample/FileProject/ProcessDefinition.process";
			String workspace = "D://mule";
			String tibcoProjectLocationRootFolder = "D:/Migration/Tibcocode";
			String tibcoProcessLocation = "D:/Migration/Tibcocode/Services/JMS/AdditionSvc.process";
			String workspace = "D://mule"
					;*/

			String projectName = getProjectName(tibcoProcessLocation);
			createMuleProject(tibcoProjectLocationRootFolder, projectName, workspace);
			String muleConfigPath = MuleAutomatorConstants.generateMuleConfigPath(workspace, projectName);
			if (new File(muleConfigPath).exists()) {
				flowElement = createMuleFlow(muleConfigPath, projectName);
			}
			generateMuleFlowFromTibcoProcess(tibcoProcessLocation, muleConfigPath, flowElement);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static Element createMuleFlow(String filepath, String flowName) throws Exception {
		Document docIn;
		docIn = MuleConfigConnection.getDomObj(filepath);
		NodeList flowTags = docIn.getElementsByTagName("flow");
		Node[] flowTagNodes = new Node[flowTags.getLength()];
		for (int i = 0; i < flowTags.getLength(); ++i) {
			flowTagNodes[i] = flowTags.item(i);
		}
		Node muleTag = docIn.getDocumentElement();
		for (Node X : flowTagNodes) {
			muleTag.removeChild(X);
		}
		Element muleRootElement = (Element) docIn.getFirstChild();
		muleRootElement.setAttribute("xmlns:doc", "http://www.mulesoft.org/schema/mule/documentation");
		Element flowElement = docIn.createElement("flow");
		flowElement.setAttribute("name", flowName);
		return flowElement;
	}

	/**
	 * Method to create mule project.
	 * @param tibco project root folder.
	 * @param mule project name.
	 * @param mule project workspace.
	 */

	private static void createMuleProject(String tibcoProjectLocationRootFolder, String projectName, String directory) throws IOException {
		MuleProjectSetup muleProjectSetup = new MuleProjectSetup();
		muleProjectSetup.createMuleProject(tibcoProjectLocationRootFolder, directory, projectName);
	}


	/**
	 * Method to get a List of All ActivityElements Containing  Type and a Reference to Node in Document.
	 * @param doc Parsed DOM Document.
	 * @param tag Tag inside which we expect "pd:type".
	 * @return List type ActivityElement.
	 */

	public static List<ActivityElement> getActivityTypes(Document doc, String tag) {
		NodeList allNodes = doc.getElementsByTagName(tag);
		List<ActivityElement> activityTypes = new ArrayList<>();
		for (int count = 0; count < allNodes.getLength(); count++) {
			Node tempNode = allNodes.item(count);
			Element tempNodeElement = (Element) tempNode;
			String activityType = tempNodeElement.getElementsByTagName("pd:type").item(0).getTextContent();
			activityTypes.add(new ActivityElement(activityType, tempNode));
		}
		return activityTypes;
	}


	/**
	 * Method to generate mule flow by reading tibco process.
	 * @param filePath 
	 * @param flowElement 
	 * @param tibco process path.
	 * @param mule project location.
	 */

	public static void generateMuleFlowFromTibcoProcess(String tibcoProcessPath, String muleConfigPath, Element flowElement) {
		String pluginType=null;
		try {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document docIn = documentBuilder.parse(new File(tibcoProcessPath));
			List<ActivityElement> activityElements = new ArrayList<>();
			activityElements.addAll(getActivityTypes(docIn, "pd:starter"));
			activityElements.addAll(getActivityTypes(docIn, "pd:activity"));
			for(ActivityElement activityElement: activityElements){
				pluginType = getPluginType(activityElement.getActivityType());
				switch(pluginType){
				case "jms": JMSHandler.generateMuleFlow(activityElement, muleConfigPath, flowElement);
				break;
				case "file": FileHandler.generateMuleFlow(activityElement, muleConfigPath, flowElement);
				break;
				}
			}
			Document doc = MuleConfigConnection.getDomObj(muleConfigPath);
			doc.getFirstChild().appendChild(flowElement);
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(muleConfigPath);
			transformer.transform(source, result);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Method to get plugin type of activityType.
	 * @param activity type.
	 * @return mule plugin type.
	 */

	private static String getPluginType(String activityType){
		return activityType.substring(activityType.indexOf("plugin.")+"plugin.".length(),activityType.lastIndexOf("."));
	}

	/**
	 * Method to get mule project name.
	 * @param tibco process path.
	 * @return mule project name.
	 */

	private static String getProjectName(String path) {
		return path.substring(path.lastIndexOf('/') + 1).split("\\.")[0];
	}

}
