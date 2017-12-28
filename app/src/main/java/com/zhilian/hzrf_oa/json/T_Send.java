package com.zhilian.hzrf_oa.json;

/**
 * Created by Administrator on 2017-9-12.
 */
public class T_Send {
    private int pid;// 相当于wf.id
    private int docid; //文档id
    private String unit;// 发文单位
    private String senddate;// 时间
    private String doctitle;// 标题
    private String docno;// 来文文号/拟稿文号
    private Integer status;//文件属性，0为已读，1为未读
    private Integer security;//秘密等级
    private Integer degree;//紧急程度
    private String unames;//备注
    private String memo;//接 收 人

    public T_Send() {
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getDocid() {
        return docid;
    }

    public void setDocid(int docid) {
        this.docid = docid;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getSenddate() {
        return senddate;
    }

    public void setSenddate(String senddate) {
        this.senddate = senddate;
    }

    public String getDoctitle() {
        return doctitle;
    }

    public void setDoctitle(String doctitle) {
        this.doctitle = doctitle;
    }

    public String getDocno() {
        return docno;
    }

    public void setDocno(String docno) {
        this.docno = docno;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getSecurity() {
        return security;
    }

    public void setSecurity(Integer security) {
        this.security = security;
    }

    public Integer getDegree() {
        return degree;
    }

    public void setDegree(Integer degree) {
        this.degree = degree;
    }

    public String getUnames() {
        return unames;
    }

    public void setUnames(String unames) {
        this.unames = unames;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
