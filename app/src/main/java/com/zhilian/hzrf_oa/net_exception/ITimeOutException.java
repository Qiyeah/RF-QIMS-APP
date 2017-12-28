package com.zhilian.hzrf_oa.net_exception;

import android.content.Context;

/**
 * Created by sl on 2017-12-6.
 */

public interface ITimeOutException{
    void reLogin(Context context,CallBack callBack);
    void onReLoginFailure(Context context);
    interface CallBack{
        void onReloginSuccess();
    }
}
