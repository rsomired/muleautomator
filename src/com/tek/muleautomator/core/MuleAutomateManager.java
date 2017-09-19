package com.tek.muleautomator.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

import com.tek.muleautomator.config.FTPConnection;
import com.tek.muleautomator.config.HTTPConnection;
import com.tek.muleautomator.config.JDBCConnection;
import com.tek.muleautomator.element.ActivityElement;
import com.tek.muleautomator.element.ConditionalTransition;
import com.tek.muleautomator.element.TransitionElement;
import com.tek.muleautomator.handler.FTPHandler;
import com.tek.muleautomator.handler.FileHandler;
import com.tek.muleautomator.handler.GeneralActivityHandler;
import com.tek.muleautomator.handler.HTTPHandler;
import com.tek.muleautomator.handler.JDBCHandler;
import com.tek.muleautomator.handler.JMSHandler;
import com.tek.muleautomator.handler.SOAPHandler;
import com.tek.muleautomator.mvn.MuleProjectSetup;
import com.tek.muleautomator.util.MuleAutomatorConstants;
import com.tek.muleautomator.util.MuleAutomatorUtil;
import com.tek.muleautomator.util.MuleConfigConnection;

public class MuleAutomateManager {

	/**
	 * Method to execute mule automator application.
	 * 
	 * @param tibco
	 *            project root folder.
	 * @param tibco
	 *            process location.
	 */

	public static void main(String args[]) {
		Element flowElement = null;
		try {
			String tibcoProjectLocationRootFolder = "D:/Tibco_To_Mule/FTP";
			String tibcoProcessLocation = "D:/Tibco_To_Mule/FTP/process/put.process";
			String workspace = "D:/muleFTP";

			// Loads all the Global Variables into
			// MuleAutomatorConstants.globalResolver Object
			MuleAutomatorConstants.TIBCO_PROJECT_ROOT_FOLDER = tibcoProjectLocationRootFolder;
			String projectName = getProjectName(tibcoProcessLocation);
			createMuleProject(tibcoProjectLocationRootFolder, projectName, workspace);
			String muleConfigPath = MuleAutomatorConstants.generateMuleConfigPath(workspace, projectName);
			if (new File(muleConfigPath).exists()) {
				flowElement = createMuleFlow(muleConfigPath, projectName);
			}
			generateMuleFlowFromTibcoProcessOrderByTransitionsWithChoice(tibcoProcessLocation, muleConfigPath,
					flowElement);
			//System.out.println("Vars: "+ MuleAutomatorConstants.tibcoVariables);
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
	
	
	private static void fetchAllConnections(String tibcoProjectLocationRootFolder){
		List<File> connFiles=new ArrayList<>();
        MuleAutomatorUtil.fileFinder(new File(tibcoProjectLocationRootFolder), connFiles, new String[]{"sharedjdbc","sharedhttp","sharedftp"});
        for(File file: connFiles){
        	if(file.getPath().contains("jdbc")){
        		JDBCConnection jdbcConnection=new JDBCConnection(file);
            	MuleAutomatorConstants.connectionConfigs.put(jdbcConnection.CONNECTION_NAME, jdbcConnection);

        	} else if(file.getPath().contains("http")){
        		HTTPConnection httpCon=new HTTPConnection(file);
        		String fileName="";
				try {
					fileName = file.getCanonicalPath().substring(file.getCanonicalPath().lastIndexOf(File.separator)+1);
					fileName=fileName.substring(0,fileName.indexOf("."));
				} catch (IOException e) {
					fileName="HTTP Connection";
					e.printStackTrace();
				}
                httpCon.CONNECTION_NAME=fileName;
        		MuleAutomatorConstants.connectionConfigs.put(httpCon.CONNECTION_NAME, httpCon);

        	} else if (file.getPath().contains("ftp")){
        		FTPConnection ftpCon=new FTPConnection(file);
        		String fileName="";
        		try {
					fileName = file.getCanonicalPath().substring(file.getCanonicalPath().lastIndexOf(File.separator)+1);
					fileName=fileName.substring(0,fileName.indexOf("."));
				} catch (IOException e) {
					fileName="HTTP Connection";
					e.printStackTrace();
				}
                ftpCon.CONNECTION_NAME=fileName;
        		MuleAutomatorConstants.connectionConfigs.put(ftpCon.CONNECTION_NAME, ftpCon);
        	}
       }
	}

	/**
	 * Method to create mule project.
	 * 
	 * @param tibco
	 *            project root folder.
	 * @param mule
	 *            project name.
	 * @param mule
	 *            project workspace.
	 */

	private static void createMuleProject(String tibcoProjectLocationRootFolder, String projectName, String directory)
			throws IOException {
		fetchAllConnections(tibcoProjectLocationRootFolder);
		MuleProjectSetup muleProjectSetup = new MuleProjectSetup();
		muleProjectSetup.createMuleProject(tibcoProjectLocationRootFolder, directory, projectName);
	}
	
	
	private static void loopGenerator(ActivityElement activityElement, String muleConfigPath, Element flowElement){
		Element loopElement=(Element)activityElement.getTargetNode();
		List<ActivityElement> activityElements=new ArrayList<>();
		List<TransitionElement> transitionElements=new ArrayList<>();
		
		NodeList loopActivities=loopElement.getElementsByTagName("pd:activity");
		NodeList loopTransitions=loopElement.getElementsByTagName("pd:transition");
		// Get all inner transitions
		for(int transIndex=0;transIndex<loopTransitions.getLength();++transIndex){
			Element currTrans=(Element)loopTransitions.item(transIndex);
			TransitionElement t=new TransitionElement(currTrans.getElementsByTagName("pd:from").item(0).getTextContent(), currTrans.getElementsByTagName("pd:to").item(0).getTextContent(), currTrans.getElementsByTagName("pd:conditionType").item(0).getTextContent());
			transitionElements.add(t);
		}
		// Get ordered activities corresponding to transition
		for(TransitionElement transitionElement: transitionElements){
			if(transitionElement.getTo().toLowerCase().equals("end"))
				break;
			for(int actIndex=0;actIndex<loopActivities.getLength();++actIndex){
				Element actElement=(Element)loopActivities.item(actIndex);
				String actName=actElement.getAttribute("name");
				if((transitionElement.getTo().equals(actName))){
					activityElements.add(new ActivityElement(actElement.getElementsByTagName("pd:type").item(0).getTextContent(), actName, actElement,true));
					break;
				}
			}
		}
		// Append components inside the Loop's forEach flow
		try {
			Document doc = MuleConfigConnection.getDomObj(muleConfigPath);
			
			Element forEachTag=doc.createElement("foreach");
			forEachTag.setAttribute("doc:name", "For Each");
			
			flowElement.appendChild(forEachTag);
			
			for(ActivityElement actEl: activityElements){
				generateFlowForActivity(actEl, muleConfigPath, forEachTag);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}

	private static void generateFlowForActivity(ActivityElement activityElement, String muleConfigPath,
			Element flowElement) {
		if (activityElement != null) {
			
			if(activityElement.getActivityType().contains("LoopGroup")){
				loopGenerator(activityElement, muleConfigPath, flowElement);
				return;
			}
			
			String pluginType = getPluginType(activityElement.getActivityType());
			switch (pluginType) {
			case "jms":
				JMSHandler.generateMuleFlow(activityElement, muleConfigPath, flowElement);
				break;
			case "file":
				FileHandler.generateMuleFlow(activityElement, muleConfigPath, flowElement);
				break;
			case "jdbc":
				JDBCHandler.generateMuleFlow(activityElement, muleConfigPath, flowElement);
				break;
			case "http":
				HTTPHandler.generateMuleFlow(activityElement, muleConfigPath, flowElement);
				break;
			case "soap":
				SOAPHandler.generateMuleFlow(activityElement, muleConfigPath, flowElement);
			case "ftp":
				FTPHandler.generateMuleFlow(activityElement, muleConfigPath, flowElement);
			case "core":
			case "mapper":
			case "timer":
				GeneralActivityHandler.generateMuleFlow(activityElement, muleConfigPath, flowElement);
			}
		}
	}

	/**
	 * Method to get a List of All ActivityElements Containing Type and a
	 * Reference to Node in Document.
	 * 
	 * @param doc
	 *            Parsed DOM Document.
	 * @param tag
	 *            Tag inside which we expect "pd:type".
	 * @return List type ActivityElement.
	 */

	public static List<ActivityElement> getActivityTypes(Document doc, String tag) {
		NodeList allNodes = doc.getElementsByTagName(tag);
		List<ActivityElement> activityTypes = new ArrayList<>();
		for (int count = 0; count < allNodes.getLength(); count++) {
			Node tempNode = allNodes.item(count);
			Element tempNodeElement = (Element) tempNode;
			String activityType = tempNodeElement.getElementsByTagName("pd:type").item(0).getTextContent();
			String activityName = tempNodeElement.getAttributes().getNamedItem("name").getNodeValue();
			activityTypes.add(new ActivityElement(activityType, activityName, tempNode));
		}
		return activityTypes;
	}

	/**
	 * Method to return a list of transitions, in non-sorted order from document
	 * 
	 * @param doc
	 * @return List of type TransitionElement
	 */

	public static List<TransitionElement> getTransitions(Document doc) {
		NodeList allNodes = doc.getElementsByTagName("pd:transition");
		List<TransitionElement> transitions = new ArrayList<>();
		for (int count = 0; count < allNodes.getLength(); count++) {
			Node tempNode = allNodes.item(count);
			Element tempNodeElement = (Element) tempNode;
			String from = tempNodeElement.getElementsByTagName("pd:from").item(0).getTextContent();
			String to = tempNodeElement.getElementsByTagName("pd:to").item(0).getTextContent();
			String condition = tempNodeElement.getElementsByTagName("pd:conditionType").item(0).getTextContent();
			TransitionElement temp = new TransitionElement(from, to, condition);
			if (condition.contains("xpath")) {
				temp.setXPath(tempNodeElement.getElementsByTagName("pd:xpath").item(0).getTextContent());
			} else if (condition.contains("otherwise")) {
				temp.setXPath("otherwise");
			}
			transitions.add(temp);
		}
		return transitions;
	}

	/**
	 * Method to know the count of occurrences of supplied activity name. Useful
	 * to deduce whether the given activity may have multiple sub childs
	 * 
	 * @param elements
	 * @param activityName
	 * @return count
	 */

	public static int getActivityStartCount(List<TransitionElement> elements, String activityName) {
		int c = 0;
		for (TransitionElement currTransElement : elements) {
			if (currTransElement.getFrom().equals(activityName)) {
				c++;
			}
		}
		return c;
	}

	/**
	 * Method to extract subchilds from given activity name. Also appends XPath
	 * Condition in the name seperated by " -- "
	 * 
	 * @param elements
	 * @param activityName
	 * @return List of Strings of form:
	 *         " <Sub-Activity-Name> -- <Corresponding-Xpath> "
	 */

	private static List<String> getAllSubActivitiesWithXPath(List<TransitionElement> elements, String activityName) {
		List<String> subAct = new ArrayList<>();
		for (TransitionElement currTransElement : elements) {
			if (currTransElement.getFrom().equals(activityName)) {
				subAct.add(currTransElement.getTo() + " -- " + currTransElement.getXPath());
			}
		}
		return subAct;
	}

	/**
	 * Method to generate mule flow by reading tibco processes, taking Sub
	 * Activities into Account
	 * 
	 * @param filePath
	 * @param flowElement
	 * @param tibco
	 *            process path.
	 * @param mule
	 *            project location.
	 */
	public static void generateMuleFlowFromTibcoProcessOrderByTransitionsWithChoice(String tibcoProcessPath,
			String muleConfigPath, Element flowElement) {
		String pluginType = null;
		try {
			List<ActivityElement> activityElements = new ArrayList<>();
			Map<String, ActivityElement> activityCache = new HashMap<>();
			Map<String, TransitionElement> transitionCache = new HashMap<>();
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document docIn = documentBuilder.parse(new File(tibcoProcessPath));
			List<TransitionElement> transitionElements = getTransitions(docIn);
			Document docOut = MuleConfigConnection.getDomObj(muleConfigPath);
			activityElements.addAll(getActivityTypes(docIn, "pd:starter"));
			activityElements.addAll(getActivityTypes(docIn, "pd:activity"));
			activityElements.addAll(getActivityTypes(docIn, "pd:group"));
			// Add all activity Elements to hashMap so that retrieval would be
			// in O(1) Constant time.
			for (ActivityElement currActElement : activityElements) {
				activityCache.put(currActElement.getActivityName(), currActElement);
			}

			for (TransitionElement currTransElement : transitionElements) {
				if (transitionCache.containsKey(currTransElement.getFrom()))
					continue;
				transitionCache.put(currTransElement.getFrom(), currTransElement);
			}

			String tempActivityName = getStartActivity(docIn);
			// System.out.println("Transiiton Cache: "+transitionCache);
			while (true) {
				TransitionElement currTrans = null;
				if (tempActivityName.equals("End"))
					break;
				int choiceCases = getActivityStartCount(transitionElements, tempActivityName);
				if (choiceCases == 1) {
					// System.out.println("Curr Trans: "+currTrans);
					currTrans = transitionCache.get(tempActivityName);
					if (currTrans.getCondition().contains("always")) {
						ActivityElement activityElement = activityCache.get(tempActivityName);
						generateFlowForActivity(activityElement, muleConfigPath, flowElement);
					}
					tempActivityName = currTrans.getTo();
					continue;
				}

				System.out.println("Working with the Choice in Mule...");

				List<ConditionalTransition> conditionalTransitions = new ArrayList<>();

				List<String> allSubActivities = getAllSubActivitiesWithXPath(transitionElements, tempActivityName);
				// System.err.println("All SUB: "+allSubActivities);
				for (String currSubActivity : allSubActivities) {
					String tempAct = currSubActivity.split(" -- ")[0];
					List<ActivityElement> choiceTransitionElement = new ArrayList<>();

					for (int j = 0; j < transitionElements.size(); ++j) {
						TransitionElement transEl = transitionElements.get(j);
						if (transEl.getFrom().equals(tempAct)) {
							tempAct = transEl.getTo();
							choiceTransitionElement.add(activityCache.get(transEl.getFrom()));
							if (tempAct.contains("End")) {
								break;
							}
							j = 0;
						}
					}
					String xpath = currSubActivity.split(" -- ")[1];
					ConditionalTransition conditionalTransition = new ConditionalTransition(xpath,
							choiceTransitionElement);
					conditionalTransitions.add(conditionalTransition);
				}
				// System.out.println(conditionalTransitions);

				Element choiceTag = docOut.createElement("choice");
				choiceTag.setAttribute("doc:name", "Choice");
				for (ConditionalTransition currCondTr : conditionalTransitions) {
					Element whenTag = docOut.createElement("when");
					whenTag.setAttribute("expression", currCondTr.getWhenConditionPath());
					if (currCondTr.getWhenConditionPath().contains("otherwise")) {
						Element otherwiseTag = docOut.createElement("otherwise");
						for (ActivityElement aE : currCondTr.getOrderedElements()) {
							generateFlowForActivity(aE, muleConfigPath, otherwiseTag);
						}
						choiceTag.appendChild(otherwiseTag);
						continue;
					}
					for (ActivityElement aE : currCondTr.getOrderedElements()) {
						generateFlowForActivity(aE, muleConfigPath, whenTag);
					}
					choiceTag.appendChild(whenTag);
				}

				flowElement.appendChild(choiceTag);
				break;

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
	 * Method to get a List of All transitions Containing from,to and condition
	 * type.
	 * 
	 * @param doc
	 *            Parsed DOM Document.
	 * @return List type TransitionElement.
	 */

	public static List<TransitionElement> getSortedTransitions(Document doc) {
		List<TransitionElement> transitions = getTransitions(doc);

		if (transitions.size() > 1) {
			List<TransitionElement> tranistionsByOrder = new ArrayList<>();
			String activity = getStartActivity(doc);
			while (transitions.size() != 0) {
				for (int count = 0; count < transitions.size(); count++) {
					TransitionElement transition = transitions.get(count);
					if (transition.getFrom().equals(activity)) {
						tranistionsByOrder.add(transition);
						activity = transition.getTo();
						transitions.remove(transition);
						break;
					}
				}
				if (activity.equals("End")) {
					break;
				}
			}
			return tranistionsByOrder;
		}
		return transitions;
	}

	/**
	 * Method to check whether Transition Elements has Choice or not
	 * 
	 * @param doc
	 * @return boolean, true if it has choices
	 */

	public static boolean hasConditionalTransitions(Document doc) {
		NodeList allNodes = doc.getElementsByTagName("pd:transition");
		Set<String> startPoints = new HashSet<>();
		for (int count = 0; count < allNodes.getLength(); count++) {
			Node tempNode = allNodes.item(count);
			Element tempNodeElement = (Element) tempNode;
			String from = tempNodeElement.getElementsByTagName("pd:from").item(0).getTextContent();
			if (startPoints.contains(from)) {
				return true;
			}
			startPoints.add(from);
		}
		return false;
	}


	/**
	 * Method to get plugin type of activityType.
	 * 
	 * @param activity
	 *            type.
	 * @return mule plugin type.
	 */

	private static String getPluginType(String activityType) {
		return activityType.split("\\.")[3];
	}

	/**
	 * Method to get mule project name.
	 * 
	 * @param tibco
	 *            process path.
	 * @return mule project name.
	 */

	private static String getProjectName(String path) {
		return path.substring(path.lastIndexOf('/') + 1).split("\\.")[0];
	}


	
	/**
	 * Checks TIBCO process file for start Activity, returns the name of the same
	 * @param doc
	 * @return start activity name
	 */

	private static String getStartActivity(Document doc) {

		String startActivity = doc.getElementsByTagName("pd:startName").item(0).getTextContent();
		return startActivity;
	}
	
	

	
}
