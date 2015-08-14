package com.product.express.po;

import java.util.List;

public class Express {
	
	private String expTextName;
	private String expSpellName;
	private String mailNo;
	private String update;
	private String tel;
	private List<ExpressData> expressDataList;
	private String status;
	private String msg;
	
	public String getExpTextName() {
		return expTextName;
	}
	public void setExpTextName(String expTextName) {
		this.expTextName = expTextName;
	}
	public String getExpSpellName() {
		return expSpellName;
	}
	public void setExpSpellName(String expSpellName) {
		this.expSpellName = expSpellName;
	}
	public String getMailNo() {
		return mailNo;
	}
	public void setMailNo(String mailNo) {
		this.mailNo = mailNo;
	}
	public String getUpdate() {
		return update;
	}
	public void setUpdate(String update) {
		this.update = update;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public List<ExpressData> getExpressDataList() {
		return expressDataList;
	}
	public void setExpressDataList(List<ExpressData> expressDataList) {
		this.expressDataList = expressDataList;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}

}
