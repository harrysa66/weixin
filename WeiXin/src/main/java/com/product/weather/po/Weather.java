package com.product.weather.po;

import java.util.List;

public class Weather {
	
	private String status;
	private String currentCity;//当前城市
	private String date;//当前时间
	private String pm25;//pm2.5
	private List<Index> indexList;//指数集合
	private List<WeatherData> weatherDataList;//天气信息
	
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
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getPm25() {
		return pm25;
	}
	public void setPm25(String pm25) {
		this.pm25 = pm25;
	}
	public List<Index> getIndexList() {
		return indexList;
	}
	public void setIndexList(List<Index> indexList) {
		this.indexList = indexList;
	}
	public List<WeatherData> getWeatherDataList() {
		return weatherDataList;
	}
	public void setWeatherDataList(List<WeatherData> weatherDataList) {
		this.weatherDataList = weatherDataList;
	}

}
