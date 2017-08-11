package com.tek.muleautomator.handler;

import com.tek.muleautomator.element.ActivityElement;
import com.tek.muleautomator.service.JMSService;

public class JMSHandler {

	
	public static void generateMuleFlow(ActivityElement activityElement, String muleProjectLocation) {
		String activityType=activityElement.getActivityType();
		switch(activityType){
		case "com.tibco.plugin.jms.JMSQueueEventSource":
			System.out.println("com.tibco.plugin.jms.JMSQueueEventSource-----This actvity will be used to receive jms messages from jms/tibco ems servers");
			JMSService jmsService = new JMSService();
			jmsService.jmsSubscribe(muleProjectLocation);
			break;
		case "com.tibco.plugin.jms.JMSQueueSendActivity":
			System.out.println("This is used to send jms messages to jms/tibco ems servers this is not a blocking activity i.e it will not wait for response from consumer");
			break;
		case "com.tibco.plugin.jms.JMSQueueRequestReplyActivity":
			System.out.println("This activity is used to send request and wait for response, in request reply scenarios we use this activity");
			break;
		case "com.tibco.plugin.jms.JMSTopicPublishActivity":
			System.out.println("This is used to send jms messages to jms/tibco ems servers this is not a blocking activity i.e it will not wait for response from consumer");
			break;
		case "com.tibco.plugin.jms.JMSTopicRequestReplyActivity":
			System.out.println("This activity is used to send request and wait for response, In request reply scenarios we use this activity");
			break;
		case "com.tibco.plugin.jms.JMSTopicSignalInActivity":
			System.out.println("This acitivity is used in scenarios where we have asyncronous pattern.This actvity will wait for messages on a topic");
			break;
		case "com.tibco.plugin.jms.JMSQueueSignalInActivity":
			System.out.println("This acitiviyt is used in scenarios where we have asyncronous pattern.This actvity will wait for messages on a queue");
			break;
		case "com.tibco.plugin.jms.JMSQueueGetMessageActivity":
			System.out.println("This activity is used in scenarios where we need to pull messages from a queue on demand");
			break;
		case "com.tibco.plugin.jms.JMSReplyActivity":
			System.out.println("com.tibco.plugin.jms.JMSReplyActivity-----This activity is used while impelementing synchronous pattern, inorder to send response back to requestor");
			break;
		
		}
		
		
	}
	
}
