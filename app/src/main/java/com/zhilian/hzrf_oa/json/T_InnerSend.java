package com.zhilian.hzrf_oa.json;

import java.util.Date;

/**
 * Created by Administrator on 2017-9-12.
 */
public class T_InnerSend {

    private String receiver;
    private String senddate;
    private int status;
    private String fjid;


    private int d_id;
    private String degree;
    private String docno;
    private int id;
    private String memo;
    private String security;
    private int tempcolumn;
    private int temprownumber;
    private String title;
    private int u_id;
    private String unames;
    private String unit;
    public void setD_id(int d_id) {
        this.d_id = d_id;
    }
    public int getD_id() {
        return d_id;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }
    public String getDegree() {
        return degree;
    }

    public void setDocno(String docno) {
        this.docno = docno;
    }
    public String getDocno() {
        return docno;
    }

    public void setFjid(String fjid) {
        this.fjid = fjid;
    }
    public String getFjid() {
        return fjid;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
    public String getMemo() {
        return memo;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
    public String getReceiver() {
        return receiver;
    }

    public void setSecurity(String security) {
        this.security = security;
    }
    public String getSecurity() {
        return security;
    }

    public void setSenddate(String senddate) {
        this.senddate = senddate;
    }
    public String getSenddate() {
        return senddate;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    public int getStatus() {
        return status;
    }

    public void setTempcolumn(int tempcolumn) {
        this.tempcolumn = tempcolumn;
    }
    public int getTempcolumn() {
        return tempcolumn;
    }

    public void setTemprownumber(int temprownumber) {
        this.temprownumber = temprownumber;
    }
    public int getTemprownumber() {
        return temprownumber;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle() {
        return title;
    }

    public void setU_id(int u_id) {
        this.u_id = u_id;
    }
    public int getU_id() {
        return u_id;
    }

    public void setUnames(String unames) {
        this.unames = unames;
    }
    public String getUnames() {
        return unames;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
    public String getUnit() {
        return unit;
    }

    @Override
    public String toString() {
        return "T_InnerSend{" +
            "receiver='" + receiver + '\'' +
            ", senddate='" + senddate + '\'' +
            ", status=" + status +
            ", fjid='" + fjid + '\'' +
            ", d_id=" + d_id +
            ", degree='" + degree + '\'' +
            ", docno='" + docno + '\'' +
            ", id=" + id +
            ", memo='" + memo + '\'' +
            ", security='" + security + '\'' +
            ", tempcolumn=" + tempcolumn +
            ", temprownumber=" + temprownumber +
            ", title='" + title + '\'' +
            ", u_id=" + u_id +
            ", unames='" + unames + '\'' +
            ", unit='" + unit + '\'' +
            '}';
    }
}
