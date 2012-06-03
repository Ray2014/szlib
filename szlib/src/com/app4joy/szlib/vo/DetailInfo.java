package com.app4joy.szlib.vo;

public class DetailInfo {
	private String barCode;// 条码号
	private String getNum; // 索书号
	private String location;// 所在地
	private String state;// 馆藏状态
	private String type; // 流通类别

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public String getGetNum() {
		return getNum;
	}

	public void setGetNum(String getNum) {
		this.getNum = getNum;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
