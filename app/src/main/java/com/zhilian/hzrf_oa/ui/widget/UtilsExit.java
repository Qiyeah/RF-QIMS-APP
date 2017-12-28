package com.zhilian.hzrf_oa.ui.widget;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.io.Serializable;

/**
 * @author leeshenzhou on 2016/8/30.
 */
public class UtilsExit {

    public static final String CONTACTS_DETAILS_EXTRA = "contacts_details_extra";

    public static void startActivity(Context context, Class clazz, Serializable obj) {
        Intent intent = new Intent(context, clazz);
        Bundle bundle = new Bundle();
        bundle.putSerializable(CONTACTS_DETAILS_EXTRA, obj);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

}
