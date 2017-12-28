package com.zhilian.hzrf_oa.json;

/**
 * Created by Administrator on 2016/12/20.
 * 邮件列表的实体类
 */
//@JsonIgnoreProperties(ignoreUnknown = true)
public class ReceiveMailList {
	private int id;// 邮件id
	private String fromUserName;// 发件人
	private int isRead;// 是否阅读
	private String sendTime;// 发送时间
	private String title;// 邮件标题

	public ReceiveMailList() {
	}

	public ReceiveMailList(int id, String title, String fromUserName, String sendTime, int isRead) {
		this.id = id;
		this.title = title;
		this.fromUserName = fromUserName;
		this.sendTime = sendTime;
		this.isRead = isRead;
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

	public String getFromUserName() {
		return fromUserName;
	}

	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}

	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

	public int getIsRead() {
		return isRead;
	}

	public void setIsRead(int isRead) {
		this.isRead = isRead;
	}
}
