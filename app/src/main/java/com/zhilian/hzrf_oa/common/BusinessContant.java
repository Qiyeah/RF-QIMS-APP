package com.zhilian.hzrf_oa.common;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import com.zhilian.hzrf_oa.json.MailDetail;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Administrator on 2016-11-30.
 */
public class BusinessContant {
    public static final String SAVEPATH = "/mnt/sdcard/hzrf-oa/";
//    public final String Domain = "http://192.168.9.123:8083";
//    public final String Domain = "http://192.168.9.124:8080/hzrf-oa";
   public final String Domain = "http://hzrfoa.vicp.io:25246/hzrf-oa";

    public final String URL = Domain + "/Api";
    public final String DOWNLOADURL = Domain + "/File/File/";
    public final String FILEURL = Domain + "/File/Tempfile/";
    public final String SIGNIMG = Domain + "/";
    public static final String SA4hoVEPATH = "/mnt/sdcard/hzrf-oa/";
    private static String CONFIRM_ID = "";
    public final String ERROR = "用户登录超时！";
    public final String BACK_LAST_STEP = "此举表示您对上一个处理的处理意见有异议，\n将要将本事务退回给上一处理人重新拟定办理意见后，在此提交到您本人，\n是否继续？";
    private static int id;
    private static int doc_id;
    private static String item_id;
    private static String pid;
    private static String imaUrl;
    private static String nexttype;
    private static String checkid;
    private static String userid;
    private static ArrayList<Map<String, Object>> submData;
    private static String listSize;
    public  static String opinion = "";

    public static String APP_VERSION = null;

    public int getRefresh() {
        return refresh;
    }

    public void setRefresh(int refresh) {
        this.refresh = refresh;
    }

    private int refresh = 0;

    private static MailDetail model;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    };



    /**
     * Checks if the app has permission to write to device storage
     * <p/>
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                activity,
                PERMISSIONS_STORAGE,
                REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        BusinessContant.userid = userid;
    }

    //本地版本信息
    public static int localVersion = 7;

    public static MailDetail getModel() {
        return model;
    }

    public static void setModel(MailDetail model) {
        BusinessContant.model = model;
    }

    public static String getListSize() {
        return listSize;
    }

    public static void setListSize(String listSize) {
        BusinessContant.listSize = listSize;
    }

    public static ArrayList<Map<String, Object>> getSubmData() {
        return submData;
    }

    public static void setSubmData(ArrayList<Map<String, Object>> submData) {
        BusinessContant.submData = submData;
    }

    public static String getCheckid() {
        return checkid;
    }

    public static void setCheckid(String checkid) {
        BusinessContant.checkid = checkid;
    }

    public String getNexttype() {
        return nexttype;
    }

    public void setNexttype(String nexttype) {
        BusinessContant.nexttype = nexttype;
    }

    public String getImaUrl() {
        return imaUrl;
    }

    public void setImaUrl(String imaUrl) {
        BusinessContant.imaUrl = imaUrl;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        BusinessContant.pid = pid;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }


    public void setCONFIRM_ID(String key) {
        this.CONFIRM_ID = key;
    }

    public String getCONFIRM_ID() {
        return CONFIRM_ID;
    }

    public static int getId() {
        return id;
    }

    public static void setId(int id) {
        BusinessContant.id = id;
    }

    public int getDoc_id() {
        return doc_id;
    }

    public void setDoc_id(int doc_id) {
        this.doc_id = doc_id;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        BusinessContant.opinion = opinion;
    }
}
