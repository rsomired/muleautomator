package com.tek.muleautomator.service;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class JMSService {

	private static String seperator = File.separator;

	public void jmsConfiguration(String muleProjectLocation) {
		try {

			String filepath = muleProjectLocation + seperator + "src" + seperator + "main" + seperator
					+ "app" + seperator + "mule-config.xml";
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder;
			docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(filepath);
			// add doc namespace
			Element Mule = (Element) doc.getFirstChild();
			Mule.setAttribute("xmlns:doc", "http://www.mulesoft.org/schema/mule/documentation");
			// add jms config
			Element jmsConfig = doc.createElement("jms:activemq-connector");
			// add attributes
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

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(filepath));
			transformer.transform(source, result);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void jmsSubscribe(String muleProjectLocation) {
		try {
			
			if(isJmsConfigRequired(muleProjectLocation)){
				jmsConfiguration(muleProjectLocation);
			}
			
			
			String filepath = muleProjectLocation + seperator + "src" + seperator + "main" + seperator
					+ "app" + seperator + "mule-config.xml";
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder;

			docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(filepath);

			Element Mule = (Element) doc.getFirstChild();

			// add jms flowPublish
			Element jmsFlow = doc.createElement("flow");
			

			// add attributes
			jmsFlow.setAttribute("name", "jmsPoller");
			
			
		/*	Element jmsFrequency=doc.createElement("fixed-frequency-scheduler");
			jmsFrequency.setAttribute("frequency", "5000");
			jmsFlow.appendChild(jmsFrequency);*/
			
			
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

			// Node Flow = doc.createElement("flow");

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(filepath));
			transformer.transform(source, result);

		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public boolean isJmsConfigRequired(String muleProjectLocation) throws ParserConfigurationException, SAXException, IOException {
	    /*DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
	    Document doc = documentBuilderFactory.newDocumentBuilder().parse(new StringReader(content));*/
	    
	    String filepath = muleProjectLocation + seperator + "src" + seperator + "main" + seperator
				+ "app" + seperator + "mule-config.xml";
	    System.out.println("path-----"+filepath);
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;

		docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(filepath);
	    
	    NodeList nodeList = doc.getElementsByTagName("jms:activemq-connector");
	    return nodeList.getLength() == 0 ? true : false;
	}

	/*public static void main(String[] args) {
		AddMule addMule = new AddMule();
		System.out.println("In Main");
		addMule.jmsConfiguration();
		addMule.jmsSubscribe();

	}*/
}
