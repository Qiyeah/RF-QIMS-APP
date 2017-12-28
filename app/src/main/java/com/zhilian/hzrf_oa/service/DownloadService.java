package com.zhilian.hzrf_oa.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.zhilian.hzrf_oa.R;
import com.zhilian.hzrf_oa.update.UpdateDownloadListener;
import com.zhilian.hzrf_oa.update.UpdateManager;

import java.io.File;

/**
 * app更新下载后台服务
 */
public class DownloadService extends Service {
    private static final String TAG = "TAG";

    private String url;// 下载地址
    private String filePath;// 下载保存路径
    private String filename;//文件名称
    private NotificationManager notificationManager;// 通知管理类
    public final String[][] MIME_MapTable={
            //{后缀名，MIME类型}
            {".3gp",    "video/3gpp"},
            {".apk",    "application/vnd.android.package-archive"},
            {".asf",    "video/x-ms-asf"},
            {".avi",    "video/x-msvideo"},
            {".bin",    "application/octet-stream"},
            {".bmp",    "image/bmp"},
            {".c",  "text/plain"},
            {".class",  "application/octet-stream"},
            {".conf",   "text/plain"},
            {".cpp",    "text/plain"},
            {".doc",    "application/msword"},
            {".docx",   "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
            {".xls",    "application/vnd.ms-excel"},
            {".xlsx",   "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
            {".exe",    "application/octet-stream"},
            {".gif",    "image/gif"},
            {".gtar",   "application/x-gtar"},
            {".gz", "application/x-gzip"},
            {".h",  "text/plain"},
            {".htm",    "text/html"},
            {".html",   "text/html"},
            {".jar",    "application/java-archive"},
            {".java",   "text/plain"},
            {".jpeg",   "image/jpeg"},
            {".jpg",    "image/jpeg"},
            {".js", "application/x-javascript"},
            {".log",    "text/plain"},
            {".m3u",    "audio/x-mpegurl"},
            {".m4a",    "audio/mp4a-latm"},
            {".m4b",    "audio/mp4a-latm"},
            {".m4p",    "audio/mp4a-latm"},
            {".m4u",    "video/vnd.mpegurl"},
            {".m4v",    "video/x-m4v"},
            {".mov",    "video/quicktime"},
            {".mp2",    "audio/x-mpeg"},
            {".mp3",    "audio/x-mpeg"},
            {".mp4",    "video/mp4"},
            {".mpc",    "application/vnd.mpohun.certificate"},
            {".mpe",    "video/mpeg"},
            {".mpeg",   "video/mpeg"},
            {".mpg",    "video/mpeg"},
            {".mpg4",   "video/mp4"},
            {".mpga",   "audio/mpeg"},
            {".msg",    "application/vnd.ms-outlook"},
            {".ogg",    "audio/ogg"},
            {".pdf",    "application/pdf"},
            {".png",    "image/png"},
            {".pps",    "application/vnd.ms-powerpoint"},
            {".ppt",    "application/vnd.ms-powerpoint"},
            {".pptx",   "application/vnd.openxmlformats-officedocument.presentationml.presentation"},
            {".prop",   "text/plain"},
            {".rc", "text/plain"},
            {".rmvb",   "audio/x-pn-realaudio"},
            {".rtf",    "application/rtf"},
            {".sh", "text/plain"},
            {".tar",    "application/x-tar"},
            {".tgz",    "application/x-compressed"},
            {".txt",    "text/plain"},
            {".wav",    "audio/x-wav"},
            {".wma",    "audio/x-ms-wma"},
            {".wmv",    "audio/x-ms-wmv"},
            {".wps",    "application/vnd.ms-works"},
            {".xml",    "text/plain"},
            {".z",  "application/x-compress"},
            {".zip",    "application/x-zip-compressed"},
            {"",        "*/*"}
    };
    @Override
    public void onCreate() {
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //filePath = Environment.getExternalStorageDirectory() + "/hzrf-oa/hzrf-oa.apk";
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) {
            notifyUser(getString(R.string.update_download_failed), getString(R.string.update_download_failed_msg), 0);
            stopSelf();
        }
        url = intent.getStringExtra("url");
        filename=intent.getStringExtra("filename");
        filePath=Environment.getExternalStorageDirectory()+"/hzrf-oa/"+filename;
        Log.i(TAG, "下载地址: " + url);
        notifyUser(getString(R.string.update_download_start), getString(R.string.update_download_start), 0);
        startDownload();
        return super.onStartCommand(intent, flags, startId);
    }

    private void startDownload() {
        UpdateManager.getInstance().startDownloads(url, filePath, new UpdateDownloadListener() {

            @Override
            public void onStarted() {
            }

            @Override
            public void onProgressChanged(int progress, String downloadUrl) {
                notifyUser(getString(R.string.update_download_processing), getString(R.string.update_download_processing), progress);
            }

            @Override
            public void onFinished(int completeSize, String downloadUrl) {
                notifyUser(getString(R.string.update_download_finish), getString(R.string.update_download_finish), 100);
                stopSelf();
            }

            @Override
            public void onFailure() {
                notifyUser(getString(R.string.update_download_failed), getString(R.string.update_download_failed_msg), 0);
                stopSelf();
            }
        });
    }

    /**
     * 更新notification来告知用户下载进度
     * @param result
     * @param reason
     * @param progress
     */
    private void notifyUser(String result, String reason, int progress) {
        Notification mNotification;
        NotificationCompat.Builder build = new NotificationCompat.Builder(this);
        build.setSmallIcon(R.drawable.logo)// R.mipmap.ic_launcher
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo))
                .setContentTitle(filename);
        if (progress > 0 && progress < 100) {
            build.setProgress(100, progress, false);
        } else {
            build.setProgress(0, 0, false);
        }

        build.setAutoCancel(false);
        build.setWhen(System.currentTimeMillis());
        build.setTicker(result);
        build.setContentIntent(progress >= 100 ? getContentIntent() : PendingIntent.getActivity(this, 0, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT));
        mNotification = build.build();
        notificationManager.notify(0, mNotification);
    }

    public PendingIntent getContentIntent() {
        File apkFile = new File(filePath);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        String type=getMIMEType(apkFile);
        intent.setDataAndType(Uri.parse("file://" + apkFile.getAbsolutePath()), type);
        return PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private String getMIMEType(File file) {

        String type="*/*";
        String fName = file.getName();
        //获取后缀名前的分隔符"."在fName中的位置。
        int dotIndex = fName.lastIndexOf(".");
        if(dotIndex < 0){
            return type;
        }
    /* 获取文件的后缀名*/
        String end=fName.substring(dotIndex,fName.length()).toLowerCase();
        if(end=="")return type;
        //在MIME和文件类型的匹配表中找到对应的MIME类型。
        for(int i=0;i<MIME_MapTable.length;i++){ //MIME_MapTable??在这里你一定有疑问，这个MIME_MapTable是什么？
            if(end.equals(MIME_MapTable[i][0]))
                type = MIME_MapTable[i][1];
        }
        return type;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}


