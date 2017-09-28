package com.tek.muleautomator.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.tek.muleautomator.config.FTPConnection;
import com.tek.muleautomator.config.HTTPConnection;
import com.tek.muleautomator.config.JDBCConnection;
import com.tek.muleautomator.mvn.MuleProjectSetup;
import com.tek.muleautomator.util.MuleAutomatorConstants;
import com.tek.muleautomator.util.MuleAutomatorUtil;
import com.tek.muleautomator.util.MuleConfigConnection;
import com.tek.muleautomator.util.MuleFlowTools;

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
			
			String tibcoProjectLocationRootFolder = "D:/Tibco_To_Mule/javaMethod";
			String workspace = "D:/muleprojects/muleDemo";
			
			MuleAutomatorUtil.fileFinder(new File(tibcoProjectLocationRootFolder), MuleAutomatorConstants.tibcoProcessFiles, new String[]{"process"});
			// System.out.println("All: "+MuleAutomatorConstants.tibcoProcessFiles);
			// Loads all the Global Variables into
			// MuleAutomatorConstants.globalResolver Object
			MuleAutomatorConstants.TIBCO_PROJECT_ROOT_FOLDER = tibcoProjectLocationRootFolder;
			String projectName = getProjectName(tibcoProjectLocationRootFolder);
			
			createMuleProject(tibcoProjectLocationRootFolder, projectName, workspace);
			MuleAutomatorUtil.includeLibraries(tibcoProjectLocationRootFolder, workspace+"/"+projectName);
			
			String muleConfigPath = MuleAutomatorConstants.generateMuleConfigPath(workspace, projectName,"mule-config");

			int i=1;
			
			List<File> processFiles=MuleAutomatorConstants.tibcoProcessFiles;
			for(File currProcess: processFiles){
				String currFileName=currProcess.getName().substring(0,currProcess.getName().indexOf("."));
				System.out.println("\n***   Process "+i+": "+currFileName+"    ***");
				if(i==1){
					MuleAutomatorUtil.renameFile(muleConfigPath,MuleAutomatorConstants.generateMuleConfigPath(workspace, projectName,currFileName));
					muleConfigPath=MuleAutomatorConstants.generateMuleConfigPath(workspace, projectName,currFileName);
					MuleConfigConnection.updateConfigDom(muleConfigPath);
					MuleFlowTools.removeDefaultFlow(muleConfigPath, projectName);

				} else {
					muleConfigPath=MuleAutomatorConstants.generateMuleConfigPath(workspace, projectName,currFileName);
					File file=new File(muleConfigPath);
					file.createNewFile();
					MuleAutomatorUtil.writeToFile(file.getCanonicalPath(),MuleAutomatorConstants.muleConfigTemplate);
					MuleConfigConnection.updateConfigDom(muleConfigPath);
				}
				i++;
				
				if (new File(muleConfigPath).exists()) {
					flowElement = MuleFlowTools.createMuleFlow(muleConfigPath, currFileName);
				}
				
				
				flowElement=MuleFlowTools.generateMuleFlowFromTibcoProcessOrderByTransitionsWithChoice(currProcess.getCanonicalPath(), muleConfigPath,
						flowElement);
				Document doc = MuleConfigConnection.getDomObj(muleConfigPath);
				doc.getFirstChild().appendChild(flowElement);
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource source = new DOMSource(doc);
				StreamResult result = new StreamResult(muleConfigPath);
				transformer.transform(source, result);
				
			}
			
			
			//System.out.println("Vars: "+ MuleAutomatorConstants.tibcoVariables);
		} catch (Exception e) {
			e.printStackTrace();
		}
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
		if(MuleAutomatorConstants.removeExistingProject)
			renameExistingFiles(directory+"//"+projectName);
		MuleProjectSetup muleProjectSetup = new MuleProjectSetup();
		System.out.println("* * Creating Mule Project using Maven...");
		muleProjectSetup.createMuleProject(tibcoProjectLocationRootFolder, directory, projectName);
	}
	
	

	private static void renameExistingFiles(String directory) {
		File dir=new File(directory);
		if(!dir.exists())
			return;
		if(dir.listFiles().length>0){
			System.out.println("Removing existing project...");
			MuleAutomatorUtil.deleteDirectory(dir);
			System.out.println("Removed!");
		}
	}

	
	


	/**
	 * Method to get mule project name.
	 * 
	 * @param tibco
	 *            process path.
	 * @return mule project name.
	 */

	private static String getProjectName(String path) {
		return new File(path).getName();
		
	}
	
}
