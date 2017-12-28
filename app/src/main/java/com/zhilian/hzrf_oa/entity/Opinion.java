package com.zhilian.hzrf_oa.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017-12-18.
 */

public class Opinion implements Serializable{
    private static final long serialVersionUID = 8870533139723160469L;
    String content;
    String key;

    public Opinion(String docno,String itemId, String content) {
        this.content = content;
        key = docno+"-"+itemId;
    }

    public Opinion() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
