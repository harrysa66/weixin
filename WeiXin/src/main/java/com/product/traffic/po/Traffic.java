package com.product.traffic.po;

public class Traffic {
	
	private String error;
	private String status;
	private String currentCity;
	private String dateTime;
	private TrafficEvent[] results;
	
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCurrentCity() {
		return currentCity;
	}
	public void setCurrentCity(String currentCity) {
		this.currentCity = currentCity;
	}
	public String getDateTime() {
		return dateTime;
	}
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	public TrafficEvent[] getResults() {
		return results;
	}
	public void setResults(TrafficEvent[] results) {
		this.results = results;
	}

}
