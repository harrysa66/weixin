package com.product.travelAttraction.po;

import java.util.List;

public class TravelAttraction {
	
	private String name;
	private String url;
	private String lng;//经度
	private String lat;//纬度
	private String telephone;
	private String abstracts;//景点印象摘要
	private String description;//景点介绍 
	private String price;//票价
	private String openTime;//景点开放时间
	private List<Attention> attentionList;//更多事项 
	private String status;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getLng() {
		return lng;
	}
	public void setLng(String lng) {
		this.lng = lng;
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getAbstracts() {
		return abstracts;
	}
	public void setAbstracts(String abstracts) {
		this.abstracts = abstracts;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getOpenTime() {
		return openTime;
	}
	public void setOpenTime(String openTime) {
		this.openTime = openTime;
	}
	public List<Attention> getAttentionList() {
		return attentionList;
	}
	public void setAttentionList(List<Attention> attentionList) {
		this.attentionList = attentionList;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

}
