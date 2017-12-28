package com.zhilian.hzrf_oa.json;

public class T_Achieve {

    private int id; //文档id
    private int status;//状态，0为未分发，1为已分发
	private String unit;// 来文单位
	private String receivedate;// 时间
	private String title;// 标题
	private String docno;// 来文文号/拟稿文号

	public T_Achieve() {
	}

    public T_Achieve(int id, int status, String unit, String receivedate, String title, String docno) {
        this.id = id;
        this.status = status;
        this.unit = unit;
        this.receivedate = receivedate;
        this.title = title;
        this.docno = docno;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDocno() {
        return docno;
    }

    public void setDocno(String docno) {
        this.docno = docno;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getReceivedate() {
        return receivedate;
    }

    public void setReceivedate(String receivedate) {
        this.receivedate = receivedate;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "T_Achieve{" +
                "id=" + id +
                ", status=" + status +
                ", unit='" + unit + '\'' +
                ", receivedate='" + receivedate + '\'' +
                ", title='" + title + '\'' +
                ", docno='" + docno + '\'' +
                '}';
    }
}
