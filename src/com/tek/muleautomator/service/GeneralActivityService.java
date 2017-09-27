package com.tek.muleautomator.service;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.tek.muleautomator.element.GeneralActivityElement.*;
import com.tek.muleautomator.util.MuleConfigConnection;

public class GeneralActivityService {
	public static void assign(String muleConfigPath, AssignActivity assignActivity, Element flow) {
		try {
			Document doc = MuleConfigConnection.getDomObj(muleConfigPath);
			Element assign = doc.createElement("set-variable");
			assign.setAttribute("variableName", assignActivity.getVariableName());
			assign.setAttribute("value", assignActivity.getVariableValue());
			assign.setAttribute("doc:name", "Variable");
			flow.appendChild(assign);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void catchService(String muleConfigPath, CatchActivity catchActivity, Element flow) {
		try {
			Document doc = MuleConfigConnection.getDomObj(muleConfigPath);
			Element catch_Exception_Strategy = doc.createElement("catch-exception-strategy");
			catch_Exception_Strategy.setAttribute("value", "!exception.causedBy(java.lang.ArithmeticException)");
			catch_Exception_Strategy.setAttribute("doc:name", "Catch_Exception_Strategy");
			flow.appendChild(catch_Exception_Strategy);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void inspector(String muleConfigPath, InspectorActivity inspectorActivity, Element flow) {
		try {
			Document doc = MuleConfigConnection.getDomObj(muleConfigPath);
			Element inspector = doc.createElement("logger");
			inspector.setAttribute("message", "Payload:   #[payload]");
			inspector.setAttribute("doc:name", "Logger");
			flow.appendChild(inspector);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void sleep(String muleConfigPath, SleepActivity sleepActivity, Element flow) {
		try {
			Document doc = MuleConfigConnection.getDomObj(muleConfigPath);
			Element sleep = doc.createElement("expression-component");
			sleep.setTextContent("Thread.sleep(10000)");
			flow.appendChild(sleep);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void timer(String muleConfigPath, TimerActivity timerActivity, Element flow) {
		try {
			Document doc = MuleConfigConnection.getDomObj(muleConfigPath);
			
			Element connecterRef=doc.createElement("quartz:connector");
			connecterRef.setAttribute("name", "Quartz");
			connecterRef.setAttribute("validateConnections", "true");
			connecterRef.setAttribute("doc:name", "Quartz");
			doc.getFirstChild().appendChild(connecterRef);
			Element el=(Element)doc.getFirstChild();
			el.setAttribute("xmlns:quartz", "http://www.mulesoft.org/schema/mule/quartz");
					
			Element timer = doc.createElement("quartz:inbound-endpoint");
			timer.setAttribute("xsi:schemaLocation", "http://www.mulesoft.org/schema/mule/quartz/current/mule-quartz.xsd");
            timer.setAttribute("jobName", "myJob");
			timer.setAttribute("repeatCount", "1");
			timer.setAttribute("responseTimeout", "10000");
			timer.setAttribute("connector-ref", "Quartz");
			timer.setAttribute("repeatInterval", timerActivity.CONFIG_timeInterval);
			Element eventgen=doc.createElement("quartz:event-generator-job");
			timer.appendChild(eventgen);
			timer.setAttribute("cronExpression", "0 30 7 1/1 * ?");
			timer.setAttribute("doc:name", "Quartz");
			flow.appendChild(timer);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void mapper(String muleConfigPath, MapperActivity mapperActivity, Element flow) {
		try {
			Document doc = MuleConfigConnection.getDomObj(muleConfigPath);
			
			Element el=(Element)doc.getFirstChild();
			el.setAttribute("xmlns:dw", "http://www.mulesoft.org/schema/mule/ee/dw");
					
			
			
			
			Element mapper = doc.createElement("dw:transform-message");
			mapper.setAttribute("xsi:schemaLocation", "http://www.mulesoft.org/schema/mule/ee/dw/current/dw.xsd");
			mapper.setAttribute("mimeType", "application/json");
			mapper.setAttribute("set-payload", "");
			mapper.setAttribute("doc:name", "Transform message");
			/*
			 * <dw:transform-message
			 * metadata:id="0b127fdc-6abd-40d6-9d9a-a118690ca244" doc:name=
			 * "Transform Message"> <dw:input-payload
			 * mimeType="application/json"/> <dw:set-payload><![CDATA[%dw 1.0
			 * %output application/java --- [{ Age__c: payload.Age, Phone__c:
			 * payload.Phone as :string, ID__c: payload.ID
			 * }]]]></dw:set-payload> </dw:transform-message>
			 */
			flow.appendChild(mapper);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void sharedVariable(String muleConfigPath, SharedVariableActivity sharedVariableActivity,
			   Element flow) {
			  try
			  {
			  Document doc = MuleConfigConnection.getDomObj(muleConfigPath);
			  Element sharedVariable = doc.createElement("set-session-variable");
			  sharedVariable.setAttribute("variableName", "Sesion_Variable");
			  sharedVariable.setAttribute("value","#[payload]");
			  sharedVariable.setAttribute("doc:name", "Session Variable");
			  flow.appendChild(sharedVariable);
			 }
			   catch (Exception e) {
			    e.printStackTrace();
			   }
	}
}
