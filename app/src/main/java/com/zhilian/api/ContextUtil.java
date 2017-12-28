package com.zhilian.api;

import android.app.Application;

/**
 * @Author Andersen
 * mail: yawen199@163.com
 * Date: 2016-09-12 11:10
 */
public class ContextUtil extends Application {
    private static ContextUtil instance;

    public static ContextUtil getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        instance = this;
    }
}
