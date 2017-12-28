package com.zhilian.hzrf_oa.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class T_Selectman {

    private String ato;
    private int id;
    private String name;
    private String type;
    private String d_id;
    private String pid;
    private String sname;
    private String pname;
    private boolean state;
    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }



    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getD_id() {
        return d_id;
    }

    public void setD_id(String d_id) {
        this.d_id = d_id;
    }

    public String getType() {
        return type;
    }

    public T_Selectman(){

    }
    public T_Selectman(int id, String name) {
        this.id = id;
        this.name = name;
    }
    public T_Selectman(int id, String name, String d_id, String pid) {//选择下一环节执行人
        this.id = id;
        this.name = name;
        this.d_id=d_id;
        this.pid=pid;
    }

    public T_Selectman(String name, String sname, String pname, int id) {//选择下一环节执行人
        this.id = id;
        this.name = name;
        this.sname=sname;
        this.pname=pname;
    }



    public T_Selectman(int id, String name, String d_id, String pid, boolean state) {//选择指定人员（个人日程）
        this.id = id;
        this.name = name;
        this.d_id=d_id;
        this.pid=pid;
        this.state=state;
    }

    public T_Selectman(String ato, int id) {

        this.ato = ato;
        this.id = id;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAto() {
        return ato;
    }

    public void setAto(String ato) {
        this.ato = ato;
    }
}
