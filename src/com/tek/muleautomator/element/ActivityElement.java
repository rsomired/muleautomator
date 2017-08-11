package com.tek.muleautomator.element;

import org.w3c.dom.Node;

public class ActivityElement {
	String activityType;
	Node targetNode;
	
	
	public ActivityElement(String activityType, Node targetNode) {
		this.activityType = activityType;
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
