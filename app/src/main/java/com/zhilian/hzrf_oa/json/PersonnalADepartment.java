package com.zhilian.hzrf_oa.json;

import com.zhilian.hzrf_oa.entity.Test;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Administrator on 2016/12/17.
 * 选择公开人员
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PersonnalADepartment extends Observable implements Observer {
	//private static final long serialVersionUID = -1L;// implements Serializable
	private int d_id;// 科室id
	private String sname;// 科室
	private String name;
	//private List<Map<String,String>> fname = new ArrayList<>();
	private List<Test> fname = new ArrayList<Test>();
	private boolean isSelected;

	public PersonnalADepartment() {
	}

	public PersonnalADepartment(int d_id, String sname) {
		this.d_id = d_id;
		this.sname = sname;
	}

	public int getD_id() {
		return d_id;
	}

	public void setD_id(int d_id) {
		this.d_id = d_id;
	}

	public String getSname() {
		return sname;
	}

	public void setSname(String sname) {
		this.sname = sname;
	}

	public List getFname() {
		return fname;
	}

	public void setFname(List fname) {
		this.fname = fname;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	@Override
	public String toString() {
		return "'" + sname + '\'';
	}

	public void changeChecked(){
		isSelected=!isSelected;
		setChanged();
		notifyObservers(isSelected);
	}

	@Override
	public void update(Observable observable, Object data) {
		boolean flag=true;
		for (Test child: fname) {
			if(!child.isSelected()){
				flag=false;
			}
		}
		isSelected=flag;
	}
}
