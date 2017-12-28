package com.zhilian.hzrf_oa.entity;

/**
 * 联系人相关信息,一个是 ListView 的 name,另一个就是显示的 name 拼音的首字母
 */
public class ContactsInfoModel extends SuperBean {

	private String name;		// 显示的名称
	private String number;		// 手机号码
	private String imgSrc; 		// 显示头像 (备用)
	private String sortLetters;	// 显示数据拼音的首字母

	public void setContactModel(ContactModel model) {
		this.name = model.getName();
		this.number = model.getNumber();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSortLetters() {
		return sortLetters;
	}

	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}

	public String getImgSrc() {
		return imgSrc;
	}

	public void setImgSrc(String imgSrc) {
		this.imgSrc = imgSrc;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}


}
