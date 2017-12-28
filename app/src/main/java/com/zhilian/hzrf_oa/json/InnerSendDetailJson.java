package com.zhilian.hzrf_oa.json;

import java.util.List;

/**
 * Created by Administrator on 2017-9-13.
 */
public class InnerSendDetailJson {
    private int d_id;
    private String degree;
    private String docno;
    private List<T_FJList> fjid;
    private int id;
    private String memo;
    private String receiver;
    private String security;
    private String senddate;
    private int status;
    private String title;
    private String u_id;
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

    public void setFjid(List<T_FJList> fjid) {
        this.fjid = fjid;
    }
    public List<T_FJList> getFjid() {
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

    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle() {
        return title;
    }

    public void setU_id(String u_id) {
        this.u_id = u_id;
    }
    public String getU_id() {
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
        return "InnerSendDetailJson{" +
            "d_id=" + d_id +
            ", docno='" + docno + '\'' +
            ", receiver='" + receiver + '\'' +
            ", senddate='" + senddate + '\'' +
            ", status=" + status +
            ", title='" + title + '\'' +
            ", unit='" + unit + '\'' +
            ", degree='" + degree + '\'' +
            ", fjlist=" + fjid +
            ", id=" + id +
            ", memo='" + memo + '\'' +
            ", security='" + security + '\'' +
            ", u_id=" + u_id +
            ", unames='" + unames + '\'' +
            '}';
    }
}
