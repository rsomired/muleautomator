package com.tek.muleautomator.element;

public class TransitionElement {
	String from;
	String to;
	String condition;
	
	
	public TransitionElement(String from, String to, String condition) {
		this.from = from;
		this.to = to;
		this.condition = condition;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
	

}
