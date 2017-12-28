package com.zhilian.hzrf_oa.entity;

/**
 * Created by YiFan
 * 消息的实体类（测试）
 */
public class MessageBean {
    private int PhotoDrawableId;// 头像
    private String MessageName;// 用户名称
    private String MessageContent;// 提示消息
    private String MessageTime;// 消息时间

    public MessageBean(){

    }

    public MessageBean(int photoDrawableId, String messageName,
                       String messageContent, String messageTime) {
        super();
        PhotoDrawableId = photoDrawableId;
        MessageName = messageName;
        MessageContent = messageContent;
        MessageTime = messageTime;
    }

    public int getPhotoDrawableId() {
        return PhotoDrawableId;
    }

    public String getMessageName() {
        return MessageName;
    }

    public String getMessageContent() {
        return MessageContent;
    }

    public String getMessageTime() {
        return MessageTime;
    }

}
