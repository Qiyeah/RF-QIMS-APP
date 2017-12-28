package com.zhilian.api;

/**
 接收删除消息

 {
    createTime : '1348831860'
    ,msgType : 'delete'

    ,modelName : 'model名'
    ,entityId ：'15'
 }
 */
public class InDeleteMsg extends InMsg {

	private String modelName;   // Model名
	private String entityId;   // 实体ID

    public InDeleteMsg() {
    }

    public InDeleteMsg(Integer createTime, String msgType, String key) {
        super(createTime, msgType, key);
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }
}




