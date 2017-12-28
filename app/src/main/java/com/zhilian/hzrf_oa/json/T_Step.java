package com.zhilian.hzrf_oa.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class T_Step {

    private String itemid1;  //环节名称

    private String name;// 处理人
    private String operation;// 处理操作
    private String begintime;// 开始时间
    private String endtime;// 结束时间
    private String opinion; //处理意见
    private String user2; //下一步处理人

    public T_Step() {
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public T_Step(String itemid1, String name, String begintime, String endtime, String operation, String user2, String opinion) {
        this.itemid1 = itemid1;
        this.name = name;
        this.begintime = begintime;
        this.endtime = endtime;
        this.operation = operation;
        this.user2 = user2;
        this.opinion = opinion;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getItemid1() {
        return itemid1;
    }

    public void setItemid1(String itemid1) {
        this.itemid1 = itemid1;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getBegintime() {
        return begintime;
    }

    public void setBegintime(String begintime) {
        this.begintime = begintime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }

    public String getUser2() {
        return user2;
    }

    public void setUser2(String user2) {
        this.user2 = user2;
    }
}
