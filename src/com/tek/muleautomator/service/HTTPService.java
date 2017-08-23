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
import com.tek.muleautomator.element.SOAPElement.SOAPEventSourceActivity;
import com.tek.muleautomator.element.SOAPElement.SOAPSendFaultActivity;
import com.tek.muleautomator.element.SOAPElement.SOAPSendReceiveActivity;
import com.tek.muleautomator.element.SOAPElement.SOAPSendReplyActivity;
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

			Element httpRequestConfig = doc.createElement("http:request-config");
			httpRequestConfig.setAttribute("name", "HTTP_Request_Configuration");
			httpRequestConfig.setAttribute("host", "localhost");
			httpRequestConfig.setAttribute("port", "8080");
			httpRequestConfig.setAttribute("doc:name", "HTTP Request Configuration");
			muleTag.appendChild(httpRequestConfig);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void httpReciver(String muleConfigPath, HTTPElement.HTTPReceiverActivity httpReceiverActivity,
			Element flow) {

		try {
			Document doc = MuleConfigConnection.getDomObj(muleConfigPath);

			if (isHTTPListenerConfigRequired(muleConfigPath)) {
				httpListnerConfiguration(muleConfigPath);
			}
			Element httpListnerRecvier = doc.createElement("http:listener");
			httpListnerRecvier.setAttribute("config-ref", "HTTP_Listener_Configuration");
			httpListnerRecvier.setAttribute("path", "/pathName");
			httpListnerRecvier.setAttribute("doc:name", "HTTP");

			flow.appendChild(httpListnerRecvier);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void SendHttpRequest(String muleConfigPath, HTTPElement.HTTPSendRequestActivity httpSendRequestActivity,
			Element flow) {
		try {
			Document doc = MuleConfigConnection.getDomObj(muleConfigPath);
			if (isHttpRequestConfigRequired(muleConfigPath)) {
				httpRequestConfiguration(muleConfigPath, flow);
			}
			Element httpRequest = doc.createElement("http:request");
			httpRequest.setAttribute("config-ref", "HTTP_Request_Configuration");
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

	public void sendHttpResopnse(String muleConfigPath, HTTPElement.HTTPSendResponseActivity httpSendResponseActivity,
			Element flow) {
		try {
			Document doc = MuleConfigConnection.getDomObj(muleConfigPath);
			/*if (isHttpRequestConfigRequired(muleConfigPath)) {
				httpRequestConfiguration(muleConfigPath, flow);
			}
			Element httpRequest = doc.createElement("http:request");
			httpRequest.setAttribute("config-ref", "HTTP_Request_Configuration");
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
*/
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
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;
		docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(muleConfigPath);
		NodeList nodeList = doc.getElementsByTagName("http:listener-config");
		return nodeList.getLength() == 0 ? true : false;
	}

	public boolean isHttpRequestConfigRequired(String muleConfigPath)
			throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;
		docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(muleConfigPath);
		NodeList nodeList = doc.getElementsByTagName("http:request-config");
		return nodeList.getLength() == 0 ? true : false;
	}

	// ****************************************SOAP to HTTP starts here***************************************************************

	public void webserviceConsumerConfiguration(String muleConfigPath) {
		try {
			Document doc = MuleConfigConnection.getDomObj(muleConfigPath);

			Element muleTag = (Element) doc.getFirstChild();

			Element webserviceConsumerConfig = doc.createElement("ws:consumer-config");
			webserviceConsumerConfig.setAttribute("name", "Web_Service_Consumer");
			webserviceConsumerConfig.setAttribute("service", "Calculator");
			webserviceConsumerConfig.setAttribute("port", "CalculatorSoap");
			webserviceConsumerConfig.setAttribute("serviceAddress", "http://www.dneonline.com/calculator.asmx");
			webserviceConsumerConfig.setAttribute("wsdlLocation", "http://www.dneonline.com/calculator.asmx?WSDL");
			webserviceConsumerConfig.setAttribute("doc:name", "Web Service Consumer");
			muleTag.appendChild(webserviceConsumerConfig);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void apiKitSoapRouterConfiguration(String muleConfigPath) {
		try {
			Document doc = MuleConfigConnection.getDomObj(muleConfigPath);

			Element muleTag = (Element) doc.getFirstChild();

			Element webserviceConsumerConfig = doc.createElement("ws:consumer-config");
			webserviceConsumerConfig.setAttribute("name", "Web_Service_Consumer");
			webserviceConsumerConfig.setAttribute("service", "Calculator");
			webserviceConsumerConfig.setAttribute("port", "CalculatorSoap");
			webserviceConsumerConfig.setAttribute("serviceAddress", "http://www.dneonline.com/calculator.asmx");
			webserviceConsumerConfig.setAttribute("wsdlLocation", "http://www.dneonline.com/calculator.asmx?WSDL");
			webserviceConsumerConfig.setAttribute("doc:name", "Web Service Consumer");
			muleTag.appendChild(webserviceConsumerConfig);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void SoapEventSourceToHttp(String muleConfigPath,
			SOAPEventSourceActivity soapEventSourceActivity, Element flow) {

		try {
			Document doc = MuleConfigConnection.getDomObj(muleConfigPath);

			if (isHTTPListenerConfigRequired(muleConfigPath)) {
				httpListnerConfiguration(muleConfigPath);
			}
			Element httpListnerRecvier = doc.createElement("http:listener");
			httpListnerRecvier.setAttribute("config-ref", "HTTP_Listener_Configuration");
			httpListnerRecvier.setAttribute("path", "/pathName");
			httpListnerRecvier.setAttribute("doc:name", "HTTP");

			flow.appendChild(httpListnerRecvier);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void SoapRequestReply(String muleConfigPath,
			SOAPSendReceiveActivity soapSendReceiveActivity, Element flow) {

		try {
			Document doc = MuleConfigConnection.getDomObj(muleConfigPath);

			if (isHTTPListenerConfigRequired(muleConfigPath)) {
				httpListnerConfiguration(muleConfigPath);
			}
			Element httpListnerRecvier = doc.createElement("http:listener");
			httpListnerRecvier.setAttribute("config-ref", "HTTP_Listener_Configuration");
			httpListnerRecvier.setAttribute("path", "/pathName");
			httpListnerRecvier.setAttribute("doc:name", "HTTP");
			flow.appendChild(httpListnerRecvier);

			webserviceConsumerConfiguration(muleConfigPath);

			Element webserviceConsumer = doc.createElement("ws:consumer");
			webserviceConsumer.setAttribute("config-ref", "Web_Service_Consumer");
			webserviceConsumer.setAttribute("operation", "Add");
			webserviceConsumer.setAttribute("doc:name", "Web Service Consumer");
			flow.appendChild(webserviceConsumer);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void SoapSendFault(String muleConfigPath,
			SOAPSendFaultActivity soapSendFaultActivity, Element flow) {

		try {
			Document doc = MuleConfigConnection.getDomObj(muleConfigPath);
			apiKitSoapRouterConfiguration(muleConfigPath);
			Element soapFault = doc.createElement("apikit-soap:fault");
			soapFault.setAttribute("config-ref", "APIkit_SOAP__Configuration");
			soapFault.setAttribute("faultType", "GenericSF||1.1");
			soapFault.setAttribute("doc:name", "SOAP Fault");
			flow.appendChild(soapFault);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void SoapSendReply(String muleConfigPath,
			SOAPSendReplyActivity soapSendReplyActivity, Element flow) {
		try {
			Document doc = MuleConfigConnection.getDomObj(muleConfigPath);
			Element xmlToJsonTransformer = doc.createElement("json:xml-to-json-transformer");
			xmlToJsonTransformer.setAttribute("doc:name", "XML to JSON");
			flow.appendChild(xmlToJsonTransformer);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
