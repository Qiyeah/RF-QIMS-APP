package com.zhilian.hzrf_oa.entity;

import android.graphics.Bitmap;

/**
 * 联系人
 */
public class ContactModel extends SuperBean {

    private long contactId;     // 得到联系人ID
    private long photoId;       // 得到联系人头像ID
    private String name;        // 显示的名称
    private String number;      // 手机号码
    private Bitmap photo;       // 显示头像

    public long getContactId() {
        return contactId;
    }

    public void setContactId(long contactId) {
        this.contactId = contactId;
    }

    public long getPhotoId() {
        return photoId;
    }

    public void setPhotoId(long photoId) {
        this.photoId = photoId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }


}
