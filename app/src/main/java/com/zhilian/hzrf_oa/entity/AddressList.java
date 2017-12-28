package com.zhilian.hzrf_oa.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Administrator on 2016/11/30.
 * 通讯录的实体类
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddressList {
	private String name;// 名字
	private String gradename;// 职务
	private String phone;// 固定电话
	private String mbphone;// 手机

	private int id;// 联系人id
	private String begindate;// 开始工作日期
	private String married;// 婚姻状态
	//private String gradename;// 职务名称
	private String gradelevel;// 职务级别
	//private String phone;// 固定电话
	//private String mbphone;// 手机

	public AddressList() {
	}

	public AddressList(String name) {
		this.name = name;
	}

	public AddressList(int id, String name, String phone, String mbphone, String gradename) {
		this.id = id;
		this.name = name;
		this.phone = phone;
		this.mbphone = mbphone;
		this.gradename = gradename;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMbphone() {
		return mbphone;
	}

	public void setMbphone(String mbphone) {
		this.mbphone = mbphone;
	}

	public String getGradename() {
		return gradename;
	}

	public void setGradename(String gradename) {
		this.gradename = gradename;
	}

	public String getBegindate() {
		return begindate;
	}

	public void setBegindate(String begindate) {
		this.begindate = begindate;
	}

	public String getMarried() {
		return married;
	}

	public void setMarried(String married) {
		this.married = married;
	}

	public String getGradelevel() {
		return gradelevel;
	}

	public void setGradelevel(String gradelevel) {
		this.gradelevel = gradelevel;
	}


}
