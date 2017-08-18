package com.tek.muleautomator.service;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.tek.muleautomator.element.HTTPElement;
import com.tek.muleautomator.util.MuleConfigConnection;

public class HTTPService {

	public void httpListnerConfiguration(String muleConfigPath) {
		try {
			Document doc = MuleConfigConnection.getDomObj(muleConfigPath);

			Element muleTag = (Element) doc.getFirstChild();

			Element httpConfig = doc.createElement("http:listener-config");
			httpConfig.setAttribute("name", "HTTP_Listener_Configuration");
			httpConfig.setAttribute("host", "0.0.0.0");
			httpConfig.setAttribute("port", "8081");
			httpConfig.setAttribute("doc:name", "HTTP Listener Configuration");
			muleTag.appendChild(httpConfig);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void httpRequestConfiguration(String muleConfigPath, Element flow) {
		try {
			Document doc = MuleConfigConnection.getDomObj(muleConfigPath);

			Element muleTag = (Element) doc.getFirstChild();

			Element httpRequestConfig = doc
					.createElement("http:request-config");
			httpRequestConfig
					.setAttribute("name", "HTTP_Request_Configuration");
			httpRequestConfig.setAttribute("host", "localhost");
			httpRequestConfig.setAttribute("port", "8080");
			httpRequestConfig.setAttribute("doc:name",
					"HTTP Request Configuration");
			muleTag.appendChild(httpRequestConfig);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void httpReciver(String muleConfigPath,
			HTTPElement.HTTPReceiverActivity httpReceiverActivity, Element flow) {

		try {
			Document doc = MuleConfigConnection.getDomObj(muleConfigPath);

			if (isHTTPListenerConfigRequired(muleConfigPath)) {
				httpListnerConfiguration(muleConfigPath);
			}
			Element httpListnerRecvier = doc.createElement("http:listener");
			httpListnerRecvier.setAttribute("config-ref",
					"HTTP_Listener_Configuration");
			httpListnerRecvier.setAttribute("path", "/pathName");
			httpListnerRecvier.setAttribute("doc:name", "HTTP");

			flow.appendChild(httpListnerRecvier);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void httpRequest(String muleConfigPath,
			HTTPElement.HTTPSendRequestActivity httpSendRequestActivity,
			Element flow) {
		try {
			Document doc = MuleConfigConnection.getDomObj(muleConfigPath);
			if (isHttpRequestConfigRequired(muleConfigPath)) {
				httpRequestConfiguration(muleConfigPath, flow);
			}
			Element httpRequest = doc.createElement("http:request");
			httpRequest
					.setAttribute("config-ref", "HTTP_Request_Configuration");
			httpRequest.setAttribute("path", "/path");
			httpRequest.setAttribute("method", "GET");
			httpRequest.setAttribute("doc:name", "HTTP");

			Element requestBuilder = doc.createElement("http:request-builder");
			Element httpParams = doc.createElement("http:query-param");
			httpParams.setAttribute("paramName", "value1");
			requestBuilder.appendChild(httpParams);

			Element httpParams2 = doc.createElement("http:query-param");
			httpParams2.setAttribute("paramName", "value2");
			requestBuilder.appendChild(httpParams2);

			httpRequest.appendChild(requestBuilder);
			flow.appendChild(httpRequest);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void httpResopnse(String muleConfigPath,
			HTTPElement.HTTPSendResponseActivity httpSendResponseActivity,
			Element flow) {
		try {
			Document doc = MuleConfigConnection.getDomObj(muleConfigPath);
			if (isHttpRequestConfigRequired(muleConfigPath)) {
				httpRequestConfiguration(muleConfigPath, flow);
			}
			Element httpRequest = doc.createElement("http:request");
			httpRequest
					.setAttribute("config-ref", "HTTP_Request_Configuration");
			httpRequest.setAttribute("path", "/path");
			httpRequest.setAttribute("method", "GET");
			httpRequest.setAttribute("doc:name", "HTTP");

			Element requestBuilder = doc.createElement("http:request-builder");
			Element httpParams = doc.createElement("http:query-param");
			httpParams.setAttribute("paramName", "value1");
			requestBuilder.appendChild(httpParams);

			Element httpParams2 = doc.createElement("http:query-param");
			httpParams2.setAttribute("paramName", "value2");
			requestBuilder.appendChild(httpParams2);

			httpRequest.appendChild(requestBuilder);
			flow.appendChild(httpRequest);

			Element httpResponse = doc.createElement("set-payload");
			httpResponse.setAttribute("value", "#[payload]");
			httpResponse.setAttribute("doc:name", "Set Payload");

			flow.appendChild(httpResponse);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean isHTTPListenerConfigRequired(String muleConfigPath)
			throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder docBuilder;
		docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(muleConfigPath);
		NodeList nodeList = doc.getElementsByTagName("http:listener-config");
		return nodeList.getLength() == 0 ? true : false;
	}

	public boolean isHttpRequestConfigRequired(String muleConfigPath)
			throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder docBuilder;
		docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(muleConfigPath);
		NodeList nodeList = doc.getElementsByTagName("http:request-config");
		return nodeList.getLength() == 0 ? true : false;
	}
}
