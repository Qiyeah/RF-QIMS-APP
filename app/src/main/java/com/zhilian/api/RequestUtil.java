package com.zhilian.api;

import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * RequestQueue工具类
 * @Author Andersen
 * mail: yawen199@163.com
 * Date: 2016-9-12 0012 11:21
 */
public class RequestUtil {

    private static RequestQueue requestQueue;   //请求队列
    private static String CHARSET = "UTF-8";
    /**
     * 获取请求队列
     * @return
     */
    public static RequestQueue getRequestQueue() {
        if (null != requestQueue){
            return requestQueue;
        }else{
            requestQueue = Volley.newRequestQueue(ContextUtil.getInstance());
            return requestQueue;
        }
    }

    /**
     * 用查询条件构建url
     * @param url
     * @param queryParas
     * @return
     */
    public static String buildUrlWithQueryString(String url, Map<String, String> queryParas)
    {
        if ((queryParas == null) || (queryParas.isEmpty())) {
            return url;
        }
        StringBuilder sb = new StringBuilder(url);
        boolean isFirst;
        if (url.indexOf("?") == -1)
        {
            isFirst = true;
            sb.append("?");
        }
        else
        {
            isFirst = false;
        }
        for (Map.Entry<String, String> entry : queryParas.entrySet())
        {
            if (isFirst) {
                isFirst = false;
            } else {
                sb.append("&");
            }
            String key = (String)entry.getKey();
            String value = (String)entry.getValue();
            if (StrKit.notBlank(value)) {
                try
                {
                    value = URLEncoder.encode(value, CHARSET);
                }
                catch (UnsupportedEncodingException e)
                {
                    throw new RuntimeException(e);
                }
            }
            sb.append(key).append("=").append(value);
        }
        Log.e("TAG", sb.toString());
        return sb.toString();
    }

}
