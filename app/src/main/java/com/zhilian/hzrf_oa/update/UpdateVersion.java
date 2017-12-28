package com.zhilian.hzrf_oa.update;

/**
 * 版本更新的实体类
 */
public class UpdateVersion {
	private int id;
	private int attachmentId;// 附件id,APP文件存放的附件id
	private String down_url;// 下载地址
	private int force_update;// 是否强制更新 1；是  0；否
	private String version_desc;// 版本说明
	private String publish_date;// 发布日期
	private int app_type;// APP类型
	private String version_code;// 版本编号
	private String app_version;// 版本号

	public UpdateVersion() {
	}

	public String getDown_url() {
		return down_url;
	}

	public void setDown_url(String down_url) {
		this.down_url = down_url;
	}

	public int getForce_update() {
		return force_update;
	}

	public void setForce_update(int force_update) {
		this.force_update = force_update;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAttachmentId() {
		return attachmentId;
	}

	public void setAttachmentId(int attachmentId) {
		this.attachmentId = attachmentId;
	}

	public String getVersion_desc() {
		return version_desc;
	}

	public void setVersion_desc(String version_desc) {
		this.version_desc = version_desc;
	}

	public String getPublish_date() {
		return publish_date;
	}

	public void setPublish_date(String publish_date) {
		this.publish_date = publish_date;
	}

	public int getApp_type() {
		return app_type;
	}

	public void setApp_type(int app_type) {
		this.app_type = app_type;
	}

	public String getVersion_code() {
		return version_code;
	}

	public void setVersion_code(String version_code) {
		this.version_code = version_code;
	}

	public String getApp_version() {
		return app_version;
	}

	public void setApp_version(String app_version) {
		this.app_version = app_version;
	}
}
