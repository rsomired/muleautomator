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

import com.tek.muleautomator.element.JDBCElement;
import com.tek.muleautomator.util.MuleAutomatorConstants;
import com.tek.muleautomator.util.MuleAutomatorUtil;
import com.tek.muleautomator.util.MuleConfigConnection;

public class JDBCService {

	private static String currentConnectionConfiguration;
	
	public void jdbcConfiguration(String muleConfigPath) {

		try {
			Document doc = MuleConfigConnection.getDomObj(muleConfigPath);
			Element muleTag=(Element)doc.getFirstChild();
			List<File> sharedJdbcFiles=new ArrayList<>();
			MuleAutomatorUtil.fileFinder(new File(MuleAutomatorConstants.TIBCO_PROJECT_ROOT_FOLDER), sharedJdbcFiles, new String[]{"sharedjdbc"});
			Element jdbcConfig = doc.createElement("db:oracle-config");
			if(sharedJdbcFiles.size()==0){
				System.err.println("NO JDBC CONFIGURATION FILE FOUND, SETTING DEFAULT CONFIG");
				JDBCService.currentConnectionConfiguration="Oracle_Configuration";
				// Hardcoded values if configuration file not found
				jdbcConfig.setAttribute("name", "Oracle_Configuration");
				jdbcConfig.setAttribute("host", "localhost");
				jdbcConfig.setAttribute("port", "1521");
				jdbcConfig.setAttribute("instance", "xe");
				jdbcConfig.setAttribute("user", "ashish");
				jdbcConfig.setAttribute("password", "1234");
				jdbcConfig.setAttribute("doc:name", "Oracle Configuration");
				
			} else {
				File currFile=sharedJdbcFiles.get(0);
				
				DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		        Document jdbcDoc = documentBuilder.parse(currFile);
		        
		        String driver=jdbcDoc.getElementsByTagName("driver").item(0).getTextContent();
		        String user=null,pwd=null,config=null, docname=null, host=null, port=null, instance=null;
		        
		        driver=driver.toLowerCase();
		        if(driver.contains("oracle")){
		        	config="Oracle_Configuration";
		        	docname="Oracle Configuration";
		        } else if(driver.contains("mysql")){
		        	config="MySQL_Configuration";
		        	docname="MySQL Configuration";
		        }
		        
		        JDBCService.currentConnectionConfiguration=config;
		        String t=jdbcDoc.getElementsByTagName("location").item(0).getTextContent();
		        t=t.split("//")[1];
		        host=t.split(":")[0];
		        port=t.split(":")[1].split(";")[0];
		        user=jdbcDoc.getElementsByTagName("user").item(0).getTextContent();
		        pwd=jdbcDoc.getElementsByTagName("password").item(0).getTextContent();
		        instance=t.split("=")[1];        
		        
		        jdbcConfig.setAttribute("name", config);
				jdbcConfig.setAttribute("host", host);
				jdbcConfig.setAttribute("port", port);
				jdbcConfig.setAttribute("instance", instance);
				jdbcConfig.setAttribute("user", user);
				jdbcConfig.setAttribute("password", pwd);
				jdbcConfig.setAttribute("doc:name", docname);
		        
			}
			muleTag.appendChild(jdbcConfig);
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	public void jdbcCallProcedure(String muleConfigPath,JDBCElement.JDBCCallActivity jdbcCallActivity , Element flow) {
		try{

			Document doc = MuleConfigConnection.getDomObj(muleConfigPath);

			if(isJDBCConfigRequired(muleConfigPath)){
				jdbcConfiguration(muleConfigPath);
			}

			Element stordProc=doc.createElement("db:stored-procedure");
			stordProc.setAttribute("config-ref", "Oracle_Configuration");
			stordProc.setAttribute("doc:name", "Database");			

			Element dbParamQuery=doc.createElement("db:parameterized-query");
			dbParamQuery.setTextContent(jdbcCallActivity.getNamedParamQuery());
			stordProc.appendChild(dbParamQuery);
			
			for(int i=0;i<jdbcCallActivity.getParams().size();++i){
				Element dbParam=doc.createElement("db:in-param");
				dbParam.setAttribute("name", jdbcCallActivity.getParams().get(i));
				dbParam.setAttribute("value", "#[payload."+jdbcCallActivity.getParams().get(i)+"]");
				stordProc.appendChild(dbParam);
			}

			flow.appendChild(stordProc);

		}
		catch (Exception e) {
			e.printStackTrace();
		} 
	}

	public void jdbcSelect(String muleConfigPath,JDBCElement.JDBCQueryActivity jdbcQueryActivity  , Element flow) {

		try{
			Document doc = MuleConfigConnection.getDomObj(muleConfigPath);

			if(isJDBCConfigRequired(muleConfigPath)){
				jdbcConfiguration(muleConfigPath);
			}

			Element dbSelect=doc.createElement("db:select");
			dbSelect.setAttribute("config-ref", JDBCService.currentConnectionConfiguration);
			dbSelect.setAttribute("doc:name", "Database");			
			
			Element dbParamQuery=doc.createElement("db:parameterized-query");
			dbParamQuery.setTextContent(jdbcQueryActivity.getNamedParamsQuery());
			dbSelect.appendChild(dbParamQuery);
			
			for(String param: jdbcQueryActivity.getQueryParams()){
				Element dbParamAccountType=doc.createElement("db:in-param");
				dbParamAccountType.setAttribute("name", param);
				dbParamAccountType.setAttribute("value", "#[payload."+param+"]");
				dbSelect.appendChild(dbParamAccountType);
			}
			
			flow.appendChild(dbSelect);


		}
		catch (Exception e) {
			e.printStackTrace();
		} 
	}

	public void jdbcInsert(String muleConfigPath,JDBCElement.JDBCUpdateActivity jdbcUpdateActivity , Element flow) {

		try{
			Document doc = MuleConfigConnection.getDomObj(muleConfigPath);

			if(isJDBCConfigRequired(muleConfigPath)){
				jdbcConfiguration(muleConfigPath);
			}

			Element dbInsert=doc.createElement("db:insert");
			dbInsert.setAttribute("config-ref", JDBCService.currentConnectionConfiguration);
			dbInsert.setAttribute("doc:name", "Database");			

			Element dbParamQuery=doc.createElement("db:parameterized-query");
			dbParamQuery.setTextContent(jdbcUpdateActivity.getNamedParamsQuery());
			dbInsert.appendChild(dbParamQuery);

			for(String param: jdbcUpdateActivity.getQueryParams()){
				
				Element dbParam=doc.createElement("db:in-param");
				dbParam.setAttribute("name", param);
				dbParam.setAttribute("value", "#[payload."+param+"]");
				dbInsert.appendChild(dbParam);
			
			}
			

			flow.appendChild(dbInsert);	


		}
		catch (Exception e) {
			e.printStackTrace();
		} 
	}

	public void jdbcUpdate(String muleConfigPath,JDBCElement.JDBCUpdateActivity jdbcUpdateActivity , Element flow) {

		try{
			Document doc = MuleConfigConnection.getDomObj(muleConfigPath);

			if(isJDBCConfigRequired(muleConfigPath)){
				jdbcConfiguration(muleConfigPath);
			}

			Element dbUpdate=doc.createElement("db:update");
			dbUpdate.setAttribute("config-ref", JDBCService.currentConnectionConfiguration);
			dbUpdate.setAttribute("doc:name", "Database");			

			Element dbParamQuery=doc.createElement("db:parameterized-query");
			dbParamQuery.setTextContent(jdbcUpdateActivity.getNamedParamsQuery());
			dbUpdate.appendChild(dbParamQuery);

			for(String param: jdbcUpdateActivity.getQueryParams()){
				
				Element dbParam=doc.createElement("db:in-param");
				dbParam.setAttribute("name", param);
				dbParam.setAttribute("value", "#[payload."+param+"]");
				dbUpdate.appendChild(dbParam);
			
			}

			flow.appendChild(dbUpdate);	


		}
		catch (Exception e) {
			e.printStackTrace();
		} 
	}



	public void jdbcDelete(String muleConfigPath,JDBCElement.JDBCUpdateActivity jdbcUpdateActivity , Element flow) {

		try{
			Document doc = MuleConfigConnection.getDomObj(muleConfigPath);

			if(isJDBCConfigRequired(muleConfigPath)){
				jdbcConfiguration(muleConfigPath);
			}

			Element dbDelete=doc.createElement("db:delete");
			dbDelete.setAttribute("config-ref", "Oracle_Configuration");
			dbDelete.setAttribute("doc:name", "Database");			

			Element dbParamQuery=doc.createElement("db:parameterized-query");
			dbParamQuery.setTextContent(jdbcUpdateActivity.getNamedParamsQuery());
			dbDelete.appendChild(dbParamQuery);

			for(String param: jdbcUpdateActivity.getQueryParams()){
				Element dbParam=doc.createElement("db:in-param");
				dbParam.setAttribute("name", param);
				dbParam.setAttribute("value", "#[payload."+param+"]");
				dbDelete.appendChild(dbParam);
			}
			
			flow.appendChild(dbDelete);	

		}
		catch (Exception e) {
			e.printStackTrace();
		} 
	}



	public boolean isJDBCConfigRequired(String muleConfigPath) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;
		docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(muleConfigPath);
		NodeList nodeList = doc.getElementsByTagName("db:oracle-config");
		return nodeList.getLength() == 0 ? true : false;
	}
	
	
}
