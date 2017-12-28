package com.zhilian.hzrf_oa.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.zhilian.hzrf_oa.entity.Opinion;

/**
 * Created by Administrator on 2017-12-18.
 */

public class OpinionUtil {
    private Context mContext;
    private SharedPreferences mSp;
    private SharedPreferences.Editor mEditor;
    public static OpinionUtil instance = null;
    private String key = null;


    public static synchronized OpinionUtil getInstance(Context context) {
        if (null == instance){
            synchronized (OpinionUtil.class){
                instance = new OpinionUtil(context);
            }
        }
        return instance;
    }

    private OpinionUtil(Context context) {
        mContext = context;
        mSp = mContext.getSharedPreferences("opinion",Context.MODE_PRIVATE);
    }

    public void saveOpinion(Opinion opinion){
        mEditor = mSp.edit();
        mEditor.putString(opinion.getKey(),opinion.getContent());
        mEditor.commit();
    }
    public void clearOpinion(String docno,String itemId){
        mEditor = mSp.edit();
        mEditor.remove(docno+"-"+itemId);
        mEditor.commit();
    }
    public String getOpinion(String docno,String itemId){
        return mSp.getString(docno+"-"+itemId,"");
    }
}
