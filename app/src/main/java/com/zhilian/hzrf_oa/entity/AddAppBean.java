package com.zhilian.hzrf_oa.entity;

/**
 * Created by Administrator on 2016/8/29.
 * 添加应用的实体类
 */
public class AddAppBean {
    private int PhotoDrawableId; //应用图标id
    private String AppName; //应用名称
    private String AppContent; //应用内容

    public AddAppBean(int photoDrawableId, String appName, String appContent) {
        PhotoDrawableId = photoDrawableId;
        AppName = appName;
        AppContent = appContent;
    }

    public int getPhotoDrawableId() {
        return PhotoDrawableId;
    }

    public String getAppName() {
        return AppName;
    }

    public String getAppContent() {
        return AppContent;
    }

}
