package com.zhilian.hzrf_oa.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Administrator on 2016/11/29.
 * 记事本的实体类
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpinionBeen {
	private String opinion;// 意见
	private String sign;// 签名
	private String date;// 时间
	private String signimg; //签名图片url
	private int id;

	public OpinionBeen(String opinion, String signimg, String date, int id) {
		this.opinion = opinion;
		this.signimg = signimg;
		this.date = date;
		this.id=id;
	}

	public OpinionBeen(String opinion, String sign, String date) {
		this.date = date;
		this.sign = sign;
		this.opinion = opinion;
	}

	@Override
	public String toString() {
		return "OpinionBeen{" +
				"opinion='" + opinion + '\'' +
				", sign='" + sign + '\'' +
				", date='" + date + '\'' +
				", id=" + id +
				'}';
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getOpinion() {
		return opinion;
	}

	public void setOpinion(String opinion) {
		this.opinion = opinion;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getSignimg() {
		return signimg;
	}

	public void setSignimg(String signimg) {
		this.signimg = signimg;
	}
}
