package com.zhilian.hzrf_oa.entity;

/**
 * Created by Administrator on 2016/12/2.
 * 收文管理的实体类
 */
public class IncomingManage {
	private String titleNumber;// 文号
	private String title;// 来文标题
	private String fromUnit;// 来文单位
	private String date;// 收文时间
	private int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public IncomingManage(String titleNumber, String title, String fromUnit, String date, int id) {
		this.titleNumber = titleNumber;
		this.title = title;
		this.fromUnit = fromUnit;
		this.date = date;
		this.id=id;

	}

	public String getTitleNumber() {
		return titleNumber;
	}

	public void setTitleNumber(String titleNumber) {
		this.titleNumber = titleNumber;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getFromUnit() {
		return fromUnit;
	}

	public void setFromUnit(String fromUnit) {
		this.fromUnit = fromUnit;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
}
