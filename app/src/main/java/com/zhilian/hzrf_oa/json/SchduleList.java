package com.zhilian.hzrf_oa.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Administrator on 2016/12/13.
 * 日程列表
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SchduleList {
	private int id;// 日程id
	private String title;// 日程标题
	private String wdate;// 开始时间
	private String edate;// 结束时间
	private String event;// 全天事件（开始）
	private String event1;// 全天事件（结束）

	private int u_id;// 编辑人id
	private String name;// 编辑人
	private int remind;// 提醒
	private int scope;// 公开范围
	private String type;// 日程类型
	private String content;// 日程内容
	private String reason;// 日程备注
	private String receiver;// 接收人
	private String receiver_id;// 接收人id

	public SchduleList() {
	}

	public SchduleList(int id, String title, String wdate, String edate, String event, String event1) {
		this.id = id;
		this.title = title;
		this.wdate = wdate;
		this.edate = edate;
		this.event = event;
		this.event1 = event1;
	}

	public SchduleList(String name, String event, String event1, int remind, String wdate, String edate, int scope, String type, String title, String content, String reason) {
		this.name = name;
		this.event = event;
		this.event1 = event1;
		this.remind = remind;
		this.wdate = wdate;
		this.edate = edate;
		this.scope = scope;
		this.type = type;
		this.title = title;
		this.content = content;
		this.reason = reason;
	}

	public int getU_id() {
		return u_id;
	}

	public void setU_id(int u_id) {
		this.u_id = u_id;
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

	public String getWdate() {
		return wdate;
	}

	public void setWdate(String wdate) {
		this.wdate = wdate;
	}

	public String getEdate() {
		return edate;
	}

	public void setEdate(String edate) {
		this.edate = edate;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getEvent1() {
		return event1;
	}

	public void setEvent1(String event1) {
		this.event1 = event1;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getRemind() {
		return remind;
	}

	public void setRemind(int remind) {
		this.remind = remind;
	}

	public int getScope() {
		return scope;
	}

	public void setScope(int scope) {
		this.scope = scope;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getReceiver_id() {
		return receiver_id;
	}

	public void setReceiver_id(String receiver_id) {
		this.receiver_id = receiver_id;
	}

	@Override
	public String toString() {
		return "SchduleList{" +
				"id=" + id +
				", title='" + title + '\'' +
				", wdate='" + wdate + '\'' +
				", edate='" + edate + '\'' +
				", event='" + event + '\'' +
				", event1='" + event1 + '\'' +
				'}';
	}
}
