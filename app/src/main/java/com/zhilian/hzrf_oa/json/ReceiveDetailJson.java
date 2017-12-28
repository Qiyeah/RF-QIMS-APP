package com.zhilian.hzrf_oa.json;

import java.util.List;

public class ReceiveDetailJson {

    private String docid;
    private String unit;//来文单位
    private String date;//收文日期
    private String auditor;//分办人
    private String receiver;//经办人
    private String type;//类型
    private String docno;//文号
    private String security;//密级
    private String count;//份数
    private String superman;//督办员
    private String title;//标题
    private List<T_FJList> fjlist;//附件列表
    private String opinion1;//批办及领导意见
    private String opinion2;//传阅意见
    private String opinion3;//办理情况
    private String activename;//环节名称
    private String opinionfield;//意见域
    private String atype;//环节类型 3是结束
    private WorkflowJson wf;
    private String opinion;
    private String itemid;
    private String uname;//用户名称
    private String[] opinions;//常用意见
    private String mustsubmit;//只能提交
    private String mustreceive;//只能签收
    private String backlaststep;//退回
    private String tempopinion1;
    private String tempopinion2;
    private String tempopinion3;
    private String positionId;//职位id
    private List<ReceiverJson> receiverList;



    public List<ReceiverJson> getReceiverList() {
        return receiverList;
    }

    public void setReceiverList(List<ReceiverJson> receiverList) {
        this.receiverList = receiverList;
    }

    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

    public String getBacklaststep() {
        return backlaststep;
    }

    public void setBacklaststep(String backlaststep) {
        this.backlaststep = backlaststep;
    }

    public String getDocid() {
        return docid;
    }

    public void setDocid(String docid) {
        this.docid = docid;
    }

    public String getTempopinion1() {
        return tempopinion1;
    }

    public void setTempopinion1(String tempopinion1) {
        this.tempopinion1 = tempopinion1;
    }

    public String getTempopinion2() {
        return tempopinion2;
    }

    public void setTempopinion2(String tempopinion2) {
        this.tempopinion2 = tempopinion2;
    }

    public String getTempopinion3() {
        return tempopinion3;
    }

    public void setTempopinion3(String tempopinion3) {
        this.tempopinion3 = tempopinion3;
    }

    public String getMustreceive() {
        return mustreceive;
    }

    public void setMustreceive(String mustreceive) {
        this.mustreceive = mustreceive;
    }

    public String getMustsubmit() {
        return mustsubmit;
    }

    public void setMustsubmit(String mustsubmit) {
        this.mustsubmit = mustsubmit;
    }

    public String[] getOpinions() {
        return opinions;
    }

    public void setOpinions(String[] opinions) {
        this.opinions = opinions;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getItemid() {
        return itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAuditor() {
        return auditor;
    }

    public void setAuditor(String auditor) {
        this.auditor = auditor;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDocno() {
        return docno;
    }

    public void setDocno(String docno) {
        this.docno = docno;
    }

    public String getSecurity() {
        return security;
    }

    public void setSecurity(String security) {
        this.security = security;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getSuperman() {
        return superman;
    }

    public void setSuperman(String superman) {
        this.superman = superman;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<T_FJList> getFjlist() {
        return fjlist;
    }

    public void setFjlist(List<T_FJList> fjlist) {
        this.fjlist = fjlist;
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

    public String getOpinion3() {
        return opinion3;
    }

    public void setOpinion3(String opinion3) {
        this.opinion3 = opinion3;
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
}
