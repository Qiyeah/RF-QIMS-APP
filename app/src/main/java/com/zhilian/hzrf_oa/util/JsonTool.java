package com.zhilian.hzrf_oa.util;

import com.alibaba.fastjson.JSON;
import com.zhilian.hzrf_oa.json.T_Examine;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017-9-18.
 */
public class JsonTool {
    public static <T> List<T> getObjectList(String jsonStr,Class<T> clazz){
        List<T> list = null;
        try {
            list = new ArrayList<T>();
            list = JSON.parseArray(jsonStr,clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            return list;
        }
    }
}
