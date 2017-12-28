package com.zhilian.hzrf_oa.json;

import java.io.Serializable;

/**
 * @Author Andersen
 * mail: yawen199@163.com
 * Date: 2017-1-4 0004 14:43
 */
public class WorkflowJson implements Serializable {

	private static final long serialVersionUID = -1L;

	private int id;
	private String starttime;
	private String flowname;
	private String workpath;
	private String flowform;
	private String formname;
	private String title;
	private String starter;
	private String startdept;
	private String reader;
	private String todoman;
	private String todoManName;
	private String doneuser;
	private String todousers;
	private String isopen;
	private String editor;
	private String mainflowid;
	private String subflowid;
	private String subflowname;
	private String isend;
	private String isnormalend;
	private String ishold;
	private String islock;
	private Integer itemid;
	private String hastemplate;
	private String templatename;
	private String worddocname;
	private String bodyiscreaded;
	private String bodyauthor;
	private String bodyversion;
	private String isreceive;
	private String iscanreceive;

	public String getIscanreceive() {
		return iscanreceive;
	}

	public void setIscanreceive(String iscanreceive) {
		this.iscanreceive = iscanreceive;
	}

	public String getIsreceive() {
		return isreceive;
	}

	public void setIsreceive(String isreceive) {
		this.isreceive = isreceive;
	}

	public WorkflowJson() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getStarttime() {
		return starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public String getFlowname() {
		return flowname;
	}

	public void setFlowname(String flowname) {
		this.flowname = flowname;
	}

	public String getWorkpath() {
		return workpath;
	}

	public void setWorkpath(String workpath) {
		this.workpath = workpath;
	}

	public String getFlowform() {
		return flowform;
	}

	public void setFlowform(String flowform) {
		this.flowform = flowform;
	}

	public String getFormname() {
		return formname;
	}

	public void setFormname(String formname) {
		this.formname = formname;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getStarter() {
		return starter;
	}

	public void setStarter(String starter) {
		this.starter = starter;
	}

	public String getStartdept() {
		return startdept;
	}

	public void setStartdept(String startdept) {
		this.startdept = startdept;
	}

	public String getReader() {
		return reader;
	}

	public void setReader(String reader) {
		this.reader = reader;
	}

	public String getTodoman() {
		return todoman;
	}

	public void setTodoman(String todoman) {
		this.todoman = todoman;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getTodoManName() {
		return todoManName;
	}

	public void setTodoManName(String todoManName) {
		this.todoManName = todoManName;
	}

	public String getDoneuser() {
		return doneuser;
	}

	public void setDoneuser(String doneuser) {
		this.doneuser = doneuser;
	}

	public String getTodousers() {
		return todousers;
	}

	public void setTodousers(String todousers) {
		this.todousers = todousers;
	}

	public String getIsopen() {
		return isopen;
	}

	public void setIsopen(String isopen) {
		this.isopen = isopen;
	}

	public String getEditor() {
		return editor;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}

	public String getMainflowid() {
		return mainflowid;
	}

	public void setMainflowid(String mainflowid) {
		this.mainflowid = mainflowid;
	}

	public String getSubflowid() {
		return subflowid;
	}

	public void setSubflowid(String subflowid) {
		this.subflowid = subflowid;
	}

	public String getSubflowname() {
		return subflowname;
	}

	public void setSubflowname(String subflowname) {
		this.subflowname = subflowname;
	}

	public String getIsend() {
		return isend;
	}

	public void setIsend(String isend) {
		this.isend = isend;
	}

	public String getIsnormalend() {
		return isnormalend;
	}

	public void setIsnormalend(String isnormalend) {
		this.isnormalend = isnormalend;
	}

	public String getIshold() {
		return ishold;
	}

	public void setIshold(String ishold) {
		this.ishold = ishold;
	}

	public String getIslock() {
		return islock;
	}

	public void setIslock(String islock) {
		this.islock = islock;
	}

	public Integer getItemid() {
		return itemid;
	}

	public void setItemid(Integer itemid) {
		this.itemid = itemid;
	}

	public String getHastemplate() {
		return hastemplate;
	}

	public void setHastemplate(String hastemplate) {
		this.hastemplate = hastemplate;
	}

	public String getTemplatename() {
		return templatename;
	}

	public void setTemplatename(String templatename) {
		this.templatename = templatename;
	}

	public String getWorddocname() {
		return worddocname;
	}

	public void setWorddocname(String worddocname) {
		this.worddocname = worddocname;
	}

	public String getBodyiscreaded() {
		return bodyiscreaded;
	}

	public void setBodyiscreaded(String bodyiscreaded) {
		this.bodyiscreaded = bodyiscreaded;
	}

	public String getBodyauthor() {
		return bodyauthor;
	}

	public void setBodyauthor(String bodyauthor) {
		this.bodyauthor = bodyauthor;
	}

	public String getBodyversion() {
		return bodyversion;
	}

	public void setBodyversion(String bodyversion) {
		this.bodyversion = bodyversion;
	}

	@Override
	public String toString() {
		return "WorkflowJson{" +
			"pid=" + id +
			", starttime='" + starttime + '\'' +
			", flowname='" + flowname + '\'' +
			", workpath='" + workpath + '\'' +
			", flowform='" + flowform + '\'' +
			", formname='" + formname + '\'' +
			", title='" + title + '\'' +
			", starter='" + starter + '\'' +
			", startdept='" + startdept + '\'' +
			", reader='" + reader + '\'' +
			", todoman='" + todoman + '\'' +
			", todoManName='" + todoManName + '\'' +
			", doneuser='" + doneuser + '\'' +
			", todousers='" + todousers + '\'' +
			", isopen='" + isopen + '\'' +
			", editor='" + editor + '\'' +
			", mainflowid='" + mainflowid + '\'' +
			", subflowid='" + subflowid + '\'' +
			", subflowname='" + subflowname + '\'' +
			", isend='" + isend + '\'' +
			", isnormalend='" + isnormalend + '\'' +
			", ishold='" + ishold + '\'' +
			", islock='" + islock + '\'' +
			", itemid=" + itemid +
			", hastemplate='" + hastemplate + '\'' +
			", templatename='" + templatename + '\'' +
			", worddocname='" + worddocname + '\'' +
			", bodyiscreaded='" + bodyiscreaded + '\'' +
			", bodyauthor='" + bodyauthor + '\'' +
			", bodyversion='" + bodyversion + '\'' +
			", isreceive='" + isreceive + '\'' +
			", iscanreceive='" + iscanreceive + '\'' +
			'}';
	}
}