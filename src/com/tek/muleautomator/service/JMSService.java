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
import com.tek.muleautomator.util.MuleAutomatorConstants;

public class JMSService {

	public void jmsConfiguration(String muleProjectLocation) {
		try {
			MuleConfigConnection dom=MuleConfigConnection.getDomObj();
			Document doc=dom.getDomConfig(MuleAutomatorConstants.generateMuleConfigPath(muleProjectLocation, ""));
			Element Mule = (Element) doc.getFirstChild();
			Element jmsConfig = doc.createElement("jms:activemq-connector");
			jmsConfig.setAttribute("name", "Active_MQ");
			jmsConfig.setAttribute("brokerURL", "tcp://localhost:61616");
			jmsConfig.setAttribute("validateConnections", "true");
			jmsConfig.setAttribute("doc:name", "Active MQ");
			Element springBeansConfig = doc.createElement("spring:beans");
			Element beanConfig= doc.createElement("spring:bean");
			beanConfig.setAttribute("id", "amq");
			beanConfig.setAttribute("name", "amq");
			beanConfig.setAttribute("class", "org.apache.activemq.ActiveMQConnectionFactory");
			beanConfig.setAttribute("lazy-init", "true");
			springBeansConfig.appendChild(beanConfig);
			if (Mule.hasChildNodes()) {
				Node existingFlow = Mule.getFirstChild();
				Mule.insertBefore(jmsConfig, existingFlow);
			} else
				Mule.appendChild(jmsConfig);
			Mule.appendChild(springBeansConfig);
			dom.trasfromData(doc,MuleAutomatorConstants.generateMuleConfigPath(muleProjectLocation, ""));
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	public void jmsSubscribe(String muleProjectLocation) {
		try {
			if(isJmsConfigRequired(muleProjectLocation)){
				jmsConfiguration(muleProjectLocation);
			}
			MuleConfigConnection dom=MuleConfigConnection.getDomObj();
			Document doc=dom.getDomConfig(MuleAutomatorConstants.generateMuleConfigPath(muleProjectLocation, ""));
			Element Mule = (Element) doc.getFirstChild();

			Element jmsFlow = doc.createElement("flow");
			jmsFlow.setAttribute("name", "jmsPoller");

			Element jmsOutBound=doc.createElement("jms:inbound-endpoint");
			jmsOutBound.setAttribute("queue", "sample");
			jmsOutBound.setAttribute("connector-ref", "Active_MQ");
			jmsOutBound.setAttribute("doc:name", "JMSSample");
			jmsOutBound.setAttribute("exchange-pattern", "request-response");
			jmsFlow.appendChild(jmsOutBound);

			Element loggerElement=doc.createElement("logger");
			loggerElement.setAttribute("message", "#[payload]");
			loggerElement.setAttribute("level", "INFO");
			loggerElement.setAttribute("doc:name", "Logger");
			jmsFlow.appendChild(loggerElement);

			Mule.appendChild(jmsFlow);
			dom.trasfromData(doc,MuleAutomatorConstants.generateMuleConfigPath(muleProjectLocation, ""));

		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	public boolean isJmsConfigRequired(String muleProjectLocation) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;
		docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(MuleAutomatorConstants.generateMuleConfigPath(muleProjectLocation, ""));
		NodeList nodeList = doc.getElementsByTagName("jms:activemq-connector");
		return nodeList.getLength() == 0 ? true : false;
	}
}
