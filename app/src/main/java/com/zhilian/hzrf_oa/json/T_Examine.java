package com.zhilian.hzrf_oa.json;

/**
 * Created by Administrator on 2017-9-15.
 */
public class T_Examine {
    private String degree;
    private String docno;
    private int id;
    private String receivedate;
    private String title;
    private String unit;
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

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }

    public void setReceivedate(String receivedate) {
        this.receivedate = receivedate;
    }
    public String getReceivedate() {
        return receivedate;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle() {
        return title;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
    public String getUnit() {
        return unit;
    }

    @Override
    public String toString() {
        return "T_Examine{" +
            "degree='" + degree + '\'' +
            ", docno='" + docno + '\'' +
            ", id=" + id +
            ", receivedate='" + receivedate + '\'' +
            ", title='" + title + '\'' +
            ", unit='" + unit + '\'' +
            '}';
    }
}
