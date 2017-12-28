package com.zhilian.hzrf_oa.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by Administrator on 2016/12/19.
 * 联系人的实体类
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Test  extends Observable implements Observer {
	//private static final long serialVersionUID = -1L;// implements Serializable
	private int d_id;// 科室id
	private int id;// 联系人id
	private String name;// 联系人
	private boolean isSelected;

	public Test() {
	}

	public Test(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public int getD_id() {
		return d_id;
	}

	public void setD_id(int d_id) {
		this.d_id = d_id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "'" + name + '\'';
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public void changeChecked(){
		isSelected=!isSelected;
		setChanged();
		notifyObservers();
	}

	@Override
	public void update(Observable observable, Object data) {
		if(data instanceof Boolean){
			isSelected=(Boolean) data;
		}
	}
}
