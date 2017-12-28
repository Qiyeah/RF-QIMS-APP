package com.zhilian.hzrf_oa.json;

import java.util.List;

/**
 * Created by Administrator on 2016/12/20.
 * 邮件详情的实体类
 */
public class MailDetail {
    private int id;// 邮件id
    private String title;// 主题
    private String fromname;// 发件人
    private String sendTime;// 发送时间
    private String receiver;// 收件人
    private String fromId;//发件人idid
    private String copyer;// 抄送人
    private List<T_FJList> fjid;// 附件id
    private String url;// 附件
    private String content;// 正文
    private int oboxId;// 删除之后用于恢复的标志
    private int boxId;// 处于什么箱的标志

    public MailDetail() {
    }

    public MailDetail(String title, String fromname, String sendTime, String receiver, String copyer, String url, String content) {
        this.title = title;
        this.fromname = fromname;
        this.sendTime = sendTime;
        this.receiver = receiver;
        this.copyer = copyer;
        this.url = url;
        this.content = content;
    }

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
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

    public String getFromname() {
        return fromname;
    }

    public void setFromname(String fromname) {
        this.fromname = fromname;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getCopyer() {
        return copyer;
    }

    public void setCopyer(String copyer) {
        this.copyer = copyer;
    }

    public List<T_FJList> getFjid() {
        return fjid;
    }

    public void setFjid(List<T_FJList> fjid) {
        this.fjid = fjid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getOboxId() {
        return oboxId;
    }

    public void setOboxId(int oboxId) {
        this.oboxId = oboxId;
    }

    public int getBoxId() {
        return boxId;
    }

    public void setBoxId(int boxId) {
        this.boxId = boxId;
    }
}
