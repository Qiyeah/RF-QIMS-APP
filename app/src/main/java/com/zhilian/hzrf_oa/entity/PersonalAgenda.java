package com.zhilian.hzrf_oa.entity;

/**
 * Created by Administrator on 2016/12/2.
 * 个人日程的实体类
 */
public class PersonalAgenda {
	private String title;// 标题
	private String remark;// 备注
	private String time;// 时间

	public PersonalAgenda(String title, String remark, String time) {
		this.title = title;
		this.remark = remark;
		this.time = time;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
}
