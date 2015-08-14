package com.product.traffic.po;

public class TrafficEvent {
	
	private String startTime;
	private String endTime;
	private String title;
	private String description;
	private String type;
	private TrafficLocation location;
	
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public TrafficLocation getLocation() {
		return location;
	}
	public void setLocation(TrafficLocation location) {
		this.location = location;
	}

}
