package com.zhilian.hzrf_oa.net_exception;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.zhilian.api.StrKit;
import com.zhilian.hzrf_oa.ui.activity.LoginActivity;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by sl on 2017-12-6.
 */

public class TimeOutException implements ITimeOutException{
    Timer timer = new Timer();
    TimerTask task ;
    @Override
    public void reLogin(final Context context, final CallBack callBack) {
        SharedPreferences sp = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        final String name = sp.getString("name", "");
        final String password = sp.getString("password", "");
        if (StrKit.notBlank(name)&& StrKit.notBlank(password)){
            new Thread(new Runnable(){
                @Override
                public void run() {
                   task = new TimerTask(){
                        @Override
                        public void run() {
                            boolean  flag =  LoginUtil.confirmUser(context,name,password);
                            if (flag){
                                task.cancel();
                                timer.cancel();
                                callBack.onReloginSuccess();
                            }
                        }
                    };
                    timer.schedule(task,1000*5);
                }
            }).start();
        }
    }

    @Override
    public void onReLoginFailure(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }
}
