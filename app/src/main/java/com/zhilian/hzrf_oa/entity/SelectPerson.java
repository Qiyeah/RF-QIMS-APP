package com.zhilian.hzrf_oa.entity;

/**
 * Created by Administrator on 2016/12/25.
 * 选择联系人的实体类
 */
public class SelectPerson {
	private int id;// 联系人id
	private String name;// 联系人
	private String sname;// 科室
	private String pname;// 职位


	public SelectPerson() {
	}

	public SelectPerson(int id, String name, String sname, String pname) {
		this.id = id;
		this.name = name;
		this.sname = sname;
		this.pname = pname;
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

	public String getSname() {
		return sname;
	}

	public void setSname(String sname) {
		this.sname = sname;
	}

	public String getPname() {
		return pname;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}
}
