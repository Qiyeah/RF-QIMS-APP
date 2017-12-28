package com.zhilian.hzrf_oa.entity;

/**
 * Created by Administrator on 2016/11/29.
 * 记事本列表的实体类
 */
//@JsonIgnoreProperties(ignoreUnknown = true)
public class NotepadBeen {
	private int id;// 事件id
	private String title;// 标题
	private String wdate;// 时间
	private String content;// 内容

	private int u_id;// 用户id
	private String type;// 类型
	private String _MASK_FROM_V2;// 创建时间

	public NotepadBeen() {
	}

	public NotepadBeen(int id, String title, String content, String wdate) {
		this.id = id;
		this.title = title;
		this.content = content;
		this.wdate = wdate;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getWdate() {
		return wdate;
	}

	public void setWdate(String wdate) {
		this.wdate = wdate;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getU_id() {
		return u_id;
	}

	public void setU_id(int u_id) {
		this.u_id = u_id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String get_MASK_FROM_V2() {
		return _MASK_FROM_V2;
	}

	public void set_MASK_FROM_V2(String _MASK_FROM_V2) {
		this._MASK_FROM_V2 = _MASK_FROM_V2;
	}
}
