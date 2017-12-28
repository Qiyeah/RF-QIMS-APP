package com.zhilian.hzrf_oa.common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhilian.api.InQueryMsg;
import com.zhilian.api.JsonStringRequest;
import com.zhilian.api.ParaMap;
import com.zhilian.api.RequestUtil;
import com.zhilian.api.Sign;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017-2-9.
 */
public class Common {
    private String msg = "heng";
    static BusinessContant bc = new BusinessContant();
    public final String[][] MIME_MapTable = {
        //{后缀名，MIME类型}
        {".3gp", "video/3gpp"},
        {".apk", "application/vnd.android.package-archive"},
        {".asf", "video/x-ms-asf"},
        {".avi", "video/x-msvideo"},
        {".bin", "application/octet-stream"},
        {".bmp", "image/bmp"},
        {".c", "text/plain"},
        {".class", "application/octet-stream"},
        {".conf", "text/plain"},
        {".cpp", "text/plain"},
        {".doc", "application/msword"},
        {".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
        {".xls", "application/vnd.ms-excel"},
        {".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
        {".exe", "application/octet-stream"},
        {".gif", "image/gif"},
        {".gtar", "application/x-gtar"},
        {".gz", "application/x-gzip"},
        {".h", "text/plain"},
        {".htm", "text/html"},
        {".html", "text/html"},
        {".jar", "application/java-archive"},
        {".java", "text/plain"},
        {".jpeg", "image/jpeg"},
        {".jpg", "image/jpeg"},
        {".js", "application/x-javascript"},
        {".log", "text/plain"},
        {".m3u", "audio/x-mpegurl"},
        {".m4a", "audio/mp4a-latm"},
        {".m4b", "audio/mp4a-latm"},
        {".m4p", "audio/mp4a-latm"},
        {".m4u", "video/vnd.mpegurl"},
        {".m4v", "video/x-m4v"},
        {".mov", "video/quicktime"},
        {".mp2", "audio/x-mpeg"},
        {".mp3", "audio/x-mpeg"},
        {".mp4", "video/mp4"},
        {".mpc", "application/vnd.mpohun.certificate"},
        {".mpe", "video/mpeg"},
        {".mpeg", "video/mpeg"},
        {".mpg", "video/mpeg"},
        {".mpg4", "video/mp4"},
        {".mpga", "audio/mpeg"},
        {".msg", "application/vnd.ms-outlook"},
        {".ogg", "audio/ogg"},
        {".pdf", "application/pdf"},
        {".png", "image/png"},
        {".pps", "application/vnd.ms-powerpoint"},
        {".ppt", "application/vnd.ms-powerpoint"},
        {".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation"},
        {".prop", "text/plain"},
        {".rc", "text/plain"},
        {".rmvb", "audio/x-pn-realaudio"},
        {".rtf", "application/rtf"},
        {".sh", "text/plain"},
        {".tar", "application/x-tar"},
        {".tgz", "application/x-compressed"},
        {".txt", "text/plain"},
        {".wav", "audio/x-wav"},
        {".wma", "audio/x-ms-wma"},
        {".wmv", "audio/x-ms-wmv"},
        {".wps", "application/vnd.ms-works"},
        {".xml", "text/plain"},
        {".z", "application/x-compress"},
        {"", "*/*"}
    };

    public String getMIMEType(File file) {

        String type = "*/*";
        String fName = file.getName();
        //获取后缀名前的分隔符"."在fName中的位置。
        int dotIndex = fName.lastIndexOf(".");
        if (dotIndex < 0) {
            return type;
        }
    /* 获取文件的后缀名*/
        String end = fName.substring(dotIndex, fName.length()).toLowerCase();
        if (end == "") return type;
        //在MIME和文件类型的匹配表中找到对应的MIME类型。
        for (int i = 0; i < MIME_MapTable.length; i++) { //MIME_MapTable??在这里你一定有疑问，这个MIME_MapTable是什么？
            if (end.equals(MIME_MapTable[i][0]))
                type = MIME_MapTable[i][1];
        }
        return type;
    }

    public static boolean judgeNet(Context mContext) {//判断是否有网络
        boolean flag = true;
        final ConnectivityManager connectivityManager = (ConnectivityManager) mContext
            .getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo mobNetInfoActivity = connectivityManager
            .getActiveNetworkInfo();
        if (mobNetInfoActivity == null || !mobNetInfoActivity.isAvailable()) {
            Toast.makeText(mContext, "当前没有连接网络！", Toast.LENGTH_SHORT).show();
            flag = false;
        }

        return flag;
    }

    public static Map<String, String> getQueryParas() {
        String url = bc.URL;
        String token = "rqw3huav8zwr1q8zas3iatc67";
        final String encodingAesKey = "InVjlo7czsOWrCSmTPgEUXBzlFnmqpNMQU3ZfilULHyHZiRjVUhxxWpexhYH6f4i";
        Map<String, String> ret = Sign.sign(url, token, encodingAesKey);
        String signature = ret.get("signature");
        String nonceStr = ret.get("nonceStr");
        String timestamp = ret.get("timestamp");
        Map<String, String> queryParas = ParaMap.create("accessToken", token)
            .put("nonce", nonceStr)
            .put("timestamp", timestamp)
            .put("signature", signature)
            .getData();
        return queryParas;
    }

    public static String getPostData(InQueryMsg inQueryMsg) {
        String postData = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            postData = mapper.writeValueAsString(inQueryMsg);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        System.out.println("发送前的明文：" + postData);
        return postData;
    }

    public String sendRequest(final Context context, String name, HashMap<String, String> map) {

        BusinessContant bc = new BusinessContant();
        String key = bc.getCONFIRM_ID();
        String url = bc.URL;
        String token = "1lj4hbato30kl1ppytwa1ueqdn";
        final String encodingAesKey = "InVjlo7czsOWrCSmTPgEUXBzlFnmqpNMQU3ZfilULHyHZiRjVUhxxWpexhYH6f4i";
        Map<String, String> ret = Sign.sign(url, token, encodingAesKey);
        String signature = ret.get("signature");
        String nonceStr = ret.get("nonceStr");
        String timestamp = ret.get("timestamp");
        Map<String, String> queryParas = ParaMap.create("accessToken", token)
            .put("nonce", nonceStr)
            .put("timestamp", timestamp)
            .put("signature", signature)
            .getData();
        url = RequestUtil.buildUrlWithQueryString(url, queryParas);
        InQueryMsg inQueryMsg = new InQueryMsg(1348831860, "query", key);
        inQueryMsg.setQueryName(name);
        inQueryMsg.setQueryPara(map);
        String postData = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            postData = mapper.writeValueAsString(inQueryMsg);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        System.out.println("发送前的明文：" + postData);
        RequestQueue requestQueue = RequestUtil.getRequestQueue();

        JsonRequest jsonRequest = new JsonStringRequest(Request.Method.POST, url, postData,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    msg = response;
                }
            }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
                Toast.makeText(context, "出错了!", Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(jsonRequest);
        return msg;
    }
}
