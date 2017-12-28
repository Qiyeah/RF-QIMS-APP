package com.zhilian.hzrf_oa.json;

/**
 * 公告详情的实体类
 */
public class T_AnnounceDetails {
	private int id;// 公告id
	private String title;// 标题
	private String did;// 部门
	private String uid;// 发布人
	private int atype;// 公告类型（0 普通类型 1 紧急类型）
	private String content;// 内容
	private String sendtime;// 发布时间

	public T_AnnounceDetails() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDid() {
		return did;
	}

	public void setDid(String did) {
		this.did = did;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public int getAtype() {
		return atype;
	}

	public void setAtype(int atype) {
		this.atype = atype;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSendtime() {
		return sendtime;
	}

	public void setSendtime(String sendtime) {
		this.sendtime = sendtime;
	}
}
