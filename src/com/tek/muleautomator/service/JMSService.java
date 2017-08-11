package com.tek.muleautomator.service;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.tek.muleautomator.util.MuleConfigConnection;

public class JMSService {

	public void jmsConfiguration(String muleConfigPath, Element flow) {
		try {
			Document doc = MuleConfigConnection.getDomObj(muleConfigPath);
			Element jmsConfig = doc.createElement("jms:activemq-connector");
			jmsConfig.setAttribute("name", "Active_MQ");
			jmsConfig.setAttribute("brokerURL", "tcp://localhost:61616");
			jmsConfig.setAttribute("validateConnections", "true");
			jmsConfig.setAttribute("doc:name", "Active MQ");
			if (flow.hasChildNodes()) {
				Node existingFlow = flow.getFirstChild();
				flow.insertBefore(jmsConfig, existingFlow);
			} else
				flow.appendChild(jmsConfig);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	public void jmsSubscribe(String muleConfigPath, Element flow) {
		try {
			Document doc = MuleConfigConnection.getDomObj(muleConfigPath);
			if(isJmsConfigRequired(muleConfigPath)){
				jmsConfiguration(muleConfigPath, flow);
			}
			Element jmsOutBound=doc.createElement("jms:inbound-endpoint");
			jmsOutBound.setAttribute("queue", "sample");
			jmsOutBound.setAttribute("connector-ref", "Active_MQ");
			jmsOutBound.setAttribute("doc:name", "JMSSample");
			jmsOutBound.setAttribute("exchange-pattern", "request-response");
			flow.appendChild(jmsOutBound);

			Element loggerElement=doc.createElement("logger");
			loggerElement.setAttribute("message", "#[payload]");
			loggerElement.setAttribute("level", "INFO");
			loggerElement.setAttribute("doc:name", "Logger");
			flow.appendChild(loggerElement);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	public boolean isJmsConfigRequired(String muleConfigPath) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;
		docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(muleConfigPath);
		NodeList nodeList = doc.getElementsByTagName("jms:activemq-connector");
		return nodeList.getLength() == 0 ? true : false;
	}
}
