package com.zhilian.hzrf_oa.json;

public class T_FJList {
	private int id;// 附件id
	private String name;// 附件名称
	private String url;  //附件路径
	private String size;  //附件大小


	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public T_FJList(int id, String name, String url , String size) {
		this.id = id;
		this.name = name;
		this.url=url;
		this.size=size;
	}

	public T_FJList(String name) {
		this.name = name;
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

	public T_FJList() {
	}

	@Override
	public String toString() {
		return "T_FJList{" +
			"fjid=" + id +
			", name='" + name + '\'' +
			", url='" + url + '\'' +
			", size='" + size + '\'' +
			'}';
	}
}
