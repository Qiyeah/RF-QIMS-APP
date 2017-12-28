package com.zhilian.hzrf_oa.entity;

/**
 * Created by YiFan
 * 通知公告的实体类
 */
public class NoticeAnnouncement {
    private String title;// 标题
    private String issuer;// 发布人
    private String time;// 时间

    public NoticeAnnouncement(String title, String issuer, String time) {
        this.title = title;
        this.issuer = issuer;
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
