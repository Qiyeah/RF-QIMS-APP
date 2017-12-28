package com.zhilian.hzrf_oa.json;

/**
 * Created by Administrator on 2017-3-10.
 */
public class DocsendDetailJson {

    private String dname;//
    private String approvedate;//
    private String send1;//
    private String docno;//
    private String fjid;//
    private String id;//
    private String num;//
    private String opinion1;//
    private String opinion2;//
    private String title;//
    private String send2;//
    private String activename;//环节名称
    private String opinionfield;//意见域
    private String atype;//环节类型 3是结束
    private WorkflowJson wf;
    private String opinion;
    private String itemid;
    private String backlaststep;//退回
    private String uname;//用户名称
    private String[] opinions;//常用意见

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

    public String getApprovedate() {
        return approvedate;
    }

    public void setApprovedate(String approvedate) {
        this.approvedate = approvedate;
    }

    public String getSend1() {
        return send1;
    }

    public void setSend1(String send1) {
        this.send1 = send1;
    }

    public String getDocno() {
        return docno;
    }

    public void setDocno(String docno) {
        this.docno = docno;
    }

    public String getFjid() {
        return fjid;
    }

    public void setFjid(String fjid) {
        this.fjid = fjid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getOpinion1() {
        return opinion1;
    }

    public void setOpinion1(String opinion1) {
        this.opinion1 = opinion1;
    }

    public String getOpinion2() {
        return opinion2;
    }

    public void setOpinion2(String opinion2) {
        this.opinion2 = opinion2;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSend2() {
        return send2;
    }

    public void setSend2(String send2) {
        this.send2 = send2;
    }

    public String getActivename() {
        return activename;
    }

    public void setActivename(String activename) {
        this.activename = activename;
    }

    public String getOpinionfield() {
        return opinionfield;
    }

    public void setOpinionfield(String opinionfield) {
        this.opinionfield = opinionfield;
    }

    public String getAtype() {
        return atype;
    }

    public void setAtype(String atype) {
        this.atype = atype;
    }

    public WorkflowJson getWf() {
        return wf;
    }

    public void setWf(WorkflowJson wf) {
        this.wf = wf;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }

    public String getItemid() {
        return itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    public String getBacklaststep() {
        return backlaststep;
    }

    public void setBacklaststep(String backlaststep) {
        this.backlaststep = backlaststep;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String[] getOpinions() {
        return opinions;
    }

    public void setOpinions(String[] opinions) {
        this.opinions = opinions;
    }
}
