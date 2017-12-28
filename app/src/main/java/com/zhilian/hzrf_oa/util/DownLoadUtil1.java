package com.zhilian.hzrf_oa.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.zhilian.hzrf_oa.R;
import com.zhilian.hzrf_oa.common.Common;
import com.zhilian.hzrf_oa.ui.widget.slidingmenu.DownLoadProgressbar;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Administrator on 2017-4-1.
 */
public class DownLoadUtil1 {
    private DownLoadProgressbar bar;

    public void setDownloadProgressBar(DownLoadProgressbar bar) {
        this.bar = bar;
    }



    //重写downoropen时需要给以下字段赋值
    public String downUrl;//下载url
    public String downPath;//文件放置路径
    public Context context;//环境
    public String name;//文件名称

    private int fileSize;//文件大小
    private int downLoadFileSize;//下载文件大小
    private NotificationManager mNotificationManager = null;
    private Notification mNotification;
    private int result;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            // 定义一个Handler，用于处理下载线程与UI间通讯
            switch (msg.what) {
                case 1:
                    result = downLoadFileSize * 100 / fileSize;
                    mNotification.contentView.setTextViewText(R.id.content_view_text1, name + " " + result + "%");
                    mNotification.contentView.setProgressBar(R.id.content_view_progress, fileSize, downLoadFileSize, false);
                    mNotificationManager.notify(0, mNotification);
                    bar.setCurrentValue(downLoadFileSize);
                    Log.e("size", "文件" + downLoadFileSize + ":" + fileSize + ":" + result);
                    break;
                case 2:
                    Toast.makeText(context, "文件下载完成", Toast.LENGTH_SHORT).show();
                    break;

                case -1:
                    String error = msg.getData().getString("error");
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                    break;

                default:
                    break;
            }
        }
    };

    /**
     * 点击打开或下载文件
     */
    public void DownOrOpen() {
        downPath = context.getExternalFilesDir(null).getPath() + name;
        /*File file = new File(BusinessContant.SAVEPATH);
        if(!file.exists()){
            file.mkdirs();
        }*/
        File targetfile = new File(downPath);
        //if (targetfile.exists()) {//如果文件在本地存在，则直接打开
        //    chooseappopenfile(downPath);
        //} else {//否则判断当前是否处于wifi网络下
            final ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
            final NetworkInfo mobNetInfoActivity = connectivityManager
                .getActiveNetworkInfo();
            if (mobNetInfoActivity == null || !mobNetInfoActivity.isAvailable()) {
                Toast.makeText(context, "当前没有连接网络！不能下载！", Toast.LENGTH_SHORT).show();
            } else {
                // NetworkInfo不为null开始判断是网络类型
                int netType = mobNetInfoActivity.getType();
                if (netType == ConnectivityManager.TYPE_WIFI) {//如果为wifi则直接下载
                    notificationInit(downPath);
                    new Thread() {
                        public void run() {
                            //startDownloadDialog(mContext);
                            try {

                                down_file_copy(downUrl, downPath);
                               // chooseappopenfile(downPath);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                } else {//移动网络下弹窗询问是否下载

                    //AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    //builder.setTitle("文件下载");
                    //builder.setMessage("现在处于非wifi网络状态，下载将消耗流量，是否继续？");
                    //builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    //    public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                    //    }
                    //});
                    //builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    //    public void onClick(DialogInterface dialog, int which) {
                            notificationInit(downPath);
                            new Thread() {
                                public void run() {
                                    try {
                                        down_file(downUrl, downPath);
                                        chooseappopenfile(downPath);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }.start();
                    //    }
                    //});
                    //builder.create().show();

                }
            }
        //}
    }

    public void chooseappopenfile(String path) {//选择应用程序打开文件
        Common commom = new Common();
        File file = new File(path);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        String type = commom.getMIMEType(file);
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, type);
        context.startActivity(intent);
    }

    private void notificationInit(String name) {
        Common commom = new Common();
        //通知栏内显示下载进度条
        File file = new File(name);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        String type = commom.getMIMEType(file);
        intent.setDataAndType(Uri.parse("file://" + file.getAbsolutePath()), type);//点击进度条，进入相应界面
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, 0);

        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotification = new Notification();
        mNotification.icon = R.drawable.fj_icon;
        mNotification.tickerText = "开始下载";
        mNotification.flags |= Notification.FLAG_AUTO_CANCEL; // 点击清除按钮或点击通知后会自动消失
        mNotification.contentView = new RemoteViews(context.getPackageName(), R.layout.download_pro);//通知栏中进度布局
        mNotification.contentIntent = pIntent;
    }

    public void down_file_copy(String url, String path) throws IOException {


        // 下载函数
        // 获取文件名
        URL myURL = new URL(url);
        URLConnection conn = myURL.openConnection();
        conn.connect();
        InputStream is = null;
        try {
            is = conn.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
            Looper.prepare();
            Toast.makeText(context, "没有找到文件！", Toast.LENGTH_SHORT).show();
            Looper.loop();
        }

        this.fileSize = conn.getContentLength();// 根据响应获取文件大小
       // Log.e("Response", "fileSize = "+fileSize);
        bar.setMaxValue(fileSize);
        if (this.fileSize <= 0)
            throw new RuntimeException("无法获知文件大小 ");
        if (is == null)
            throw new RuntimeException("stream is null");
        FileOutputStream fos = new FileOutputStream(path);
        // 把数据存入路径+文件名
        byte buf[] = new byte[1024];
        downLoadFileSize = 0;
        sendMsg(0);
        int numread = 0;
        BufferedInputStream bis = new BufferedInputStream(is);
        int times = 0;//设置更新频率，频繁操作ＵＩ线程会导致系统奔溃
        while ((numread = bis.read(buf)) != -1) {
           // fos.write(buf, 0, numread);
            downLoadFileSize += numread;
            //Log.i("num", rate+","+total+","+p);
            if (times >= 512 || times == 0 || downLoadFileSize == this.fileSize) {/*
                    这是防止频繁地更新通知，而导致系统变慢甚至崩溃。   非常重要。。。。。*/
                Log.i("time", "time");
                times = 0;
                sendMsg(1);
            }
            times++;

        }
        sendMsg(2);// 通知下载完成
        try {
            fos.close();
            is.close();
        } catch (Exception ex) {
            Log.e("tag", "error: " + ex.getMessage(), ex);
        }

    }
    public void down_file(String url, String path) throws IOException {


        // 下载函数
        // 获取文件名
        URL myURL = new URL(url);
        URLConnection conn = myURL.openConnection();
        conn.connect();
        InputStream is = null;
        try {
            is = conn.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
            Looper.prepare();
            Toast.makeText(context, "没有找到文件！", Toast.LENGTH_SHORT).show();
            Looper.loop();
        }

        this.fileSize = conn.getContentLength();// 根据响应获取文件大小
        bar.setMaxValue(fileSize);
        if (this.fileSize <= 0)
            throw new RuntimeException("无法获知文件大小 ");
        if (is == null)
            throw new RuntimeException("stream is null");
        FileOutputStream fos = new FileOutputStream(path);
        // 把数据存入路径+文件名
        byte buf[] = new byte[1024];
        downLoadFileSize = 0;
        sendMsg(0);
        int numread = 0;
        BufferedInputStream bis = new BufferedInputStream(is);
        int times = 0;//设置更新频率，频繁操作ＵＩ线程会导致系统奔溃
        while ((numread = bis.read(buf)) != -1) {
            fos.write(buf, 0, numread);
            downLoadFileSize += numread;

            //Log.i("num", rate+","+total+","+p);
            if (times >= 512 || times == 0 || downLoadFileSize == this.fileSize) {/*
                    这是防止频繁地更新通知，而导致系统变慢甚至崩溃。   非常重要。。。。。*/
                Log.i("time", "time");
                times = 0;
                sendMsg(1);
            }
            times++;

        }
        sendMsg(2);// 通知下载完成
        try {
            fos.close();
            is.close();
        } catch (Exception ex) {
            Log.e("tag", "error: " + ex.getMessage(), ex);
        }

    }

    private void sendMsg(int flag) {
        Message msg = new Message();
        msg.what = flag;
        handler.sendMessage(msg);
    }

}
