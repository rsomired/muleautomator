package com.tek.muleautomator.handler;

import org.w3c.dom.Element;

import com.tek.muleautomator.element.ActivityElement;
import com.tek.muleautomator.element.JDBCElement;
import com.tek.muleautomator.service.JDBCService;

public class JDBCHandler {
	
	JDBCService jdbcService = new JDBCService();
	
	public void generateMuleFlow(ActivityElement activityElement, String muleProjectLocation, Element flowElement) {
		String activityType=activityElement.getActivityType();
		switch(activityType) {
		case "com.tibco.plugin.jdbc.JDBCCallActivity":
			System.out.println("com.tibco.plugin.jdbc.JDBCCallActivity-----The JDBC Call Procedure activity calls a database procedure using the specified JDBC connection");
			JDBCElement.JDBCCallActivity jdbcCallActivity = new JDBCElement.JDBCCallActivity(activityElement.getTargetNode());
			jdbcService.jdbcCallProcedure(muleProjectLocation, jdbcCallActivity, flowElement);
			break;
		}
	}

}
