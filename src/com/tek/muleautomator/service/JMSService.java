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

import com.tek.muleautomator.element.JMSElement.JMSQueueEventSource;
import com.tek.muleautomator.element.JMSElement.JMSQueueRequestReplyActivity;
import com.tek.muleautomator.element.JMSElement.JMSQueueSendActivity;
import com.tek.muleautomator.element.JMSElement.JMSReplyActivity;
import com.tek.muleautomator.element.JMSElement.JMSTopicPublishActivity;
import com.tek.muleautomator.element.JMSElement.JMSTopicRequestReplyActivity;
import com.tek.muleautomator.util.MuleAutomatorUtil;
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
			MuleAutomatorUtil.loggerElement(muleConfigPath, flow);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	public void jmsQueueReceiver(String muleConfigPath, Element flow, JMSQueueEventSource jmsQueueEventSource) {
		try {
			Document doc = MuleConfigConnection.getDomObj(muleConfigPath);
			if(isJmsConfigRequired(muleConfigPath)){
				jmsConfiguration(muleConfigPath, flow);
			}
			Element jmsQueueReceiver =doc.createElement("jms:inbound-endpoint");
			jmsQueueReceiver.setAttribute("queue", "sample");
			jmsQueueReceiver.setAttribute("connector-ref", "Active_MQ");
			jmsQueueReceiver.setAttribute("doc:name", "JMSSample");
			flow.appendChild(jmsQueueReceiver);
			MuleAutomatorUtil.loggerElement(muleConfigPath, flow);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	public void jmsQueueSender(String muleConfigPath, Element flow, JMSQueueSendActivity jmsQueueSendActivity) {
		try {
			Document doc = MuleConfigConnection.getDomObj(muleConfigPath);
			if(isJmsConfigRequired(muleConfigPath)){
				jmsConfiguration(muleConfigPath, flow);
			}
			Element jmsQueueSender =doc.createElement("jms:outbound-endpoint");
			jmsQueueSender.setAttribute("queue", "sample");
			jmsQueueSender.setAttribute("connector-ref", "Active_MQ");
			jmsQueueSender.setAttribute("doc:name", "JMSSample");
			flow.appendChild(jmsQueueSender);
			MuleAutomatorUtil.loggerElement(muleConfigPath, flow);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	public void jmsQueueRequestor(String muleConfigPath, Element flow, JMSQueueRequestReplyActivity jmsQueueRequestReplyActivity) {
		try {
			Document doc = MuleConfigConnection.getDomObj(muleConfigPath);
			if(isJmsConfigRequired(muleConfigPath)){
				jmsConfiguration(muleConfigPath, flow);
			}
			Element jmsQueueReceiver =doc.createElement("jms:outbound-endpoint");
			jmsQueueReceiver.setAttribute("queue", "sample");
			jmsQueueReceiver.setAttribute("connector-ref", "Active_MQ");
			jmsQueueReceiver.setAttribute("doc:name", "JMSSample");
			jmsQueueReceiver.setAttribute("exchange-pattern", "request-response");
			flow.appendChild(jmsQueueReceiver);
			MuleAutomatorUtil.loggerElement(muleConfigPath, flow);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	public void jmsTopicPublisher(String muleConfigPath, Element flow, JMSTopicPublishActivity jmsTopicPublishActivity) {
		try {
			Document doc = MuleConfigConnection.getDomObj(muleConfigPath);
			if(isJmsConfigRequired(muleConfigPath)){
				jmsConfiguration(muleConfigPath, flow);
			}
			Element jmsTopicPublisher =doc.createElement("jms:outbound-endpoint");
			jmsTopicPublisher.setAttribute("connector-ref", "Active_MQ");
			jmsTopicPublisher.setAttribute("doc:name", "JMSSample");
			jmsTopicPublisher.setAttribute("topic", "ActiveMQ.Advisory.TempQueue");
			flow.appendChild(jmsTopicPublisher);
			MuleAutomatorUtil.loggerElement(muleConfigPath, flow);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	public void jmsTopicRequestor(String muleConfigPath, Element flow, JMSTopicRequestReplyActivity jmsTopicRequestReplyActivity) {
		try {
			Document doc = MuleConfigConnection.getDomObj(muleConfigPath);
			if(isJmsConfigRequired(muleConfigPath)){
				jmsConfiguration(muleConfigPath, flow);
			}
			Element jmsQueueReceiver =doc.createElement("jms:outbound-endpoint");
			jmsQueueReceiver.setAttribute("connector-ref", "Active_MQ");
			jmsQueueReceiver.setAttribute("doc:name", "JMSSample");
			jmsQueueReceiver.setAttribute("topic", "ActiveMQ.Advisory.TempQueue");
			jmsQueueReceiver.setAttribute("exchange-pattern", "request-response");
			flow.appendChild(jmsQueueReceiver);
			MuleAutomatorUtil.loggerElement(muleConfigPath, flow);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	public void replyToJmsMessage(String muleConfigPath, Element flow, JMSReplyActivity jmsReplyActivity) {
		try {
			Document doc = MuleConfigConnection.getDomObj(muleConfigPath);
			Element replyToJmsMessage =doc.createElement("set-payload");
			replyToJmsMessage.setAttribute("value", "#[payload]");
			replyToJmsMessage.setAttribute("doc:name", "Set Payload");
			flow.appendChild(replyToJmsMessage);
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
