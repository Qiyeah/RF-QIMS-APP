package com.zhilian.api;

/**
 * 接收消息，以下是接收文本消息的例子
 * 
 * 1.接收查询消息
 * 
 * { createTime : '1348831860' ,msgType : 'query'
 * 
 * ,queryName : '查询方法名' ,queryPara ：{ name : 'abc' ,code : 'def' }
 * 
 * }
 * 
 * 2.接收保存消息
 * 
 * { createTime : '1348831860' ,msgType : 'save'
 * 
 * ,modelName : 'model名' ,modelProperty ：{ name : 'abc' ,code : 'def' } }
 * 
 * 3.接收删除消息
 * 
 * { createTime : '1348831860' ,msgType : 'delete'
 * 
 * ,modelName : 'model名' ,entityId ：'15' }
 * 
 */
public class InMsg {

    // 消息创建时间 （整型）
    protected Integer createTime;

    /**
     * 消息类型 1：query 查询消息 2：save 保存消息 3：delete 删除消息
     */
    protected String msgType;
    protected String key;

    public InMsg() {
    }

    public InMsg(Integer createTime, String msgType) {
	this.createTime = createTime;
	this.msgType = msgType;
    }

    public InMsg(Integer createTime, String msgType, String key) {
	this.createTime = createTime;
	this.msgType = msgType;
	this.key = key;
    }

    public Integer getCreateTime() {
	return createTime;
    }

    public void setCreateTime(Integer createTime) {
	this.createTime = createTime;
    }

    public String getMsgType() {
	return msgType;
    }

    public void setMsgType(String msgType) {
	this.msgType = msgType;
    }

    public void setKey(String key) {
	this.key = key;
    }

    public String getKey() {
	return key;
    }
}
