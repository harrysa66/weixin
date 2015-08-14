package com.product.robot.po;

public class Robot<T> {
	
	private String code;//状态码 
	private String text;//文字内容 
	private String url;//链接 
	private T[] list;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public T[] getList() {
		return list;
	}
	public void setList(T[] list) {
		this.list = list;
	}

}
