package com.tek.muleautomator.element;

import org.w3c.dom.Node;

public class ActivityElement {
	String activityType;
	String activityName;
	Node targetNode;
	
	
	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public ActivityElement(String activityType, String activityName, Node targetNode) {
		this.activityType = activityType;
		this.activityName = activityName;
		this.targetNode = targetNode;
	}
	
	public String getActivityType() {
		return activityType;
	}
	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}
	public Node getTargetNode() {
		return targetNode;
	}
	public void setTargetNode(Node targetNode) {
		this.targetNode = targetNode;
	}
	
	
}
