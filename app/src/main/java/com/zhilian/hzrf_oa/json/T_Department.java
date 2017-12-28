package com.zhilian.hzrf_oa.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Administrator on 2016/12/8.
 * 通讯录
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class T_Department {
	private int id;// 科室id
	private String fname;// 科室

	public T_Department() {
	}

	public T_Department(int id, String fname) {
		this.id = id;
		this.fname = fname;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	@Override
	public String toString() {
		return "T_Department{" +
				"id=" + id +
				", name='" + fname + '\'' +
				'}';
	}
}
