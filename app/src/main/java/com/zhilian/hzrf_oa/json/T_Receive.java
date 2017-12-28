package com.zhilian.hzrf_oa.json;

public class T_Receive {
	private int pid;// 相当于wf.id
    private int docid; //文档id
    private String active;//环节名称
	private String unit;// 来文单位
	private String starttime;// 时间
	private String doctitle;// 标题
	private String docno;// 来文文号/拟稿文号
    private Integer status;//文件属性，0为待办，1为已办


	public T_Receive() {
	}


    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public int getDocid() {
        return docid;
    }

    public void setDocid(int docid) {
        this.docid = docid;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
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

    @Override
    public String toString() {
        return "T_Receive{" +
                "pid=" + pid +
                ", docid=" + docid +
                ", unit='" + unit + '\'' +
                ", starttime='" + starttime + '\'' +
                ", doctitle='" + doctitle + '\'' +
                ", docno='" + docno + '\'' +
                '}';
    }
}
