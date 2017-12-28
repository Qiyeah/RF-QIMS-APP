package com.zhilian.hzrf_oa.json;

public class T_Announce{
	private int id;// 公告id
	private int atype;// 公告类型（0 普通类型 1 紧急类型）
	private String sendtime;// 发布时间
	private String content;// 公告内容
	private int status;// 公告状态(0不启用 1启用）
	private String title;// 公告标题
	private String name;// 发布人id（uid）
	private int did;// 发布科室id
	private int scope;// 范围id
	private String scope_uid;// 范围
	private String scope_uname;// 范围

	public T_Announce() {
	}

	public T_Announce(int id, String sendtime, String title, String name, int atype) {
		this.id = id;
		this.sendtime = sendtime;
		this.title = title;
		this.name = name;
		this.atype = atype;
	}

	public T_Announce(String title, String name, String sendtime, String content) {
		this.title = title;
		this.name = name;
		this.sendtime = sendtime;
		this.content = content;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAtype() {
		return atype;
	}

	public void setAtype(int atype) {
		this.atype = atype;
	}

	public String getSendtime() {
		return sendtime;
	}

	public void setSendtime(String sendtime) {
		this.sendtime = sendtime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getDid() {
		return did;
	}

	public void setDid(int did) {
		this.did = did;
	}

	public int getScope() {
		return scope;
	}

	public void setScope(int scope) {
		this.scope = scope;
	}

	public String getScope_uid() {
		return scope_uid;
	}

	public void setScope_uid(String scope_uid) {
		this.scope_uid = scope_uid;
	}

	public String getScope_uname() {
		return scope_uname;
	}

	public void setScope_uname(String scope_uname) {
		this.scope_uname = scope_uname;
	}

	@Override
	public String toString() {
		return "T_Announce{" +
				"id=" + id +
				", atype=" + atype +
				", sendtime='" + sendtime + '\'' +
				", content='" + content + '\'' +
				", status=" + status +
				", title='" + title + '\'' +
				", name=" + name +
				", did=" + did +
				", scope=" + scope +
				", scope_uid='" + scope_uid + '\'' +
				", scope_uname='" + scope_uname + '\'' +
				'}';
	}
}
