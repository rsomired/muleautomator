package com.tek.muleautomator.service;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.tek.muleautomator.element.JDBCElement;
import com.tek.muleautomator.util.MuleAutomatorUtil;
import com.tek.muleautomator.util.MuleConfigConnection;

public class JDBCService {

	public void jdbcConfiguration(String muleConfigPath) {

		try {
			Document doc = MuleConfigConnection.getDomObj(muleConfigPath);

			Element muleTag=(Element)doc.getFirstChild();

			Element jdbcConfig = doc.createElement("db:oracle-config");
			jdbcConfig.setAttribute("name", "Oracle_Configuration");
			jdbcConfig.setAttribute("host", "localhost");
			jdbcConfig.setAttribute("port", "1521");
			jdbcConfig.setAttribute("instance", "xe");
			jdbcConfig.setAttribute("password", "1234");
			jdbcConfig.setAttribute("doc:name", "Oracle Configuration");
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
			dbParamQuery.setTextContent("{call remove_emp(:employee_id)}");
			stordProc.appendChild(dbParamQuery);

			Element dbParam=doc.createElement("db:in-param");
			dbParam.setAttribute("name", "employee_id");
			dbParam.setAttribute("value", "1");
			stordProc.appendChild(dbParam);

			MuleAutomatorUtil.loggerElement(muleConfigPath,flow);

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
			dbSelect.setAttribute("config-ref", "Oracle_Configuration");
			dbSelect.setAttribute("doc:name", "Database");			

			Element dbParamQuery=doc.createElement("db:parameterized-query");
			dbParamQuery.setTextContent("select * from customer");
			dbSelect.appendChild(dbParamQuery);

			MuleAutomatorUtil.loggerElement(muleConfigPath,flow);

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
			dbInsert.setAttribute("config-ref", "Oracle_Configuration");
			dbInsert.setAttribute("doc:name", "Database");			

			Element dbParamQuery=doc.createElement("db:parameterized-query");
			dbParamQuery.setTextContent("insert into customer values(:name,:address,:age,155656556,:account_type)");
			dbInsert.appendChild(dbParamQuery);

			Element dbParamAccountType=doc.createElement("db:in-param");
			dbParamAccountType.setAttribute("name", "account_type");
			dbParamAccountType.setAttribute("value", "Current");
			dbInsert.appendChild(dbParamAccountType);

			Element dbParamAge=doc.createElement("db:in-param");
			dbParamAge.setAttribute("name", "age");
			dbParamAge.setAttribute("value", "22");
			dbInsert.appendChild(dbParamAge);

			Element dbParamAddr=doc.createElement("db:in-param");
			dbParamAddr.setAttribute("name", "address");
			dbParamAddr.setAttribute("value", "HYD");
			dbInsert.appendChild(dbParamAddr);


			Element dbParamName=doc.createElement("db:in-param");
			dbParamName.setAttribute("name", "name");
			dbParamName.setAttribute("value", "Raj");
			dbInsert.appendChild(dbParamName);


			MuleAutomatorUtil.loggerElement(muleConfigPath,flow);

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
			dbUpdate.setAttribute("config-ref", "Oracle_Configuration");
			dbUpdate.setAttribute("doc:name", "Database");			

			Element dbParamQuery=doc.createElement("db:parameterized-query");
			dbParamQuery.setTextContent("UPDATE customer SET address = :address WHERE account_no=:accountNO");
			dbUpdate.appendChild(dbParamQuery);

			Element dbParamAddr=doc.createElement("db:in-param");
			dbParamAddr.setAttribute("name", "address");
			dbParamAddr.setAttribute("value", "HYDERABAD");
			dbUpdate.appendChild(dbParamAddr);

			Element dbParamAge=doc.createElement("db:in-param");
			dbParamAge.setAttribute("name", "accountNO");
			dbParamAge.setAttribute("value", "22");
			dbUpdate.appendChild(dbParamAge);

			MuleAutomatorUtil.loggerElement(muleConfigPath,flow);

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
			dbParamQuery.setTextContent("Delete customer where account_no=#[payload.accountNo]");
			dbDelete.appendChild(dbParamQuery);

			MuleAutomatorUtil.loggerElement(muleConfigPath,flow);

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
