package com.product.robot.po;

public class Train {
	
	private String trainnum;//车次
	private String start;//起始站
	private String terminal;//到达站
	private String starttime;//开车时间 
	private String endtime;//到达时间 
	private String detailurl;//详情地址
	private String icon;//图标地址
	
	public String getTrainnum() {
		return trainnum;
	}
	public void setTrainnum(String trainnum) {
		this.trainnum = trainnum;
	}
	public String getStart() {
		return start;
	}
	public void setStart(String start) {
		this.start = start;
	}
	public String getTerminal() {
		return terminal;
	}
	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}
	public String getStarttime() {
		return starttime;
	}
	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}
	public String getEndtime() {
		return endtime;
	}
	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}
	public String getDetailurl() {
		return detailurl;
	}
	public void setDetailurl(String detailurl) {
		this.detailurl = detailurl;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}

}
