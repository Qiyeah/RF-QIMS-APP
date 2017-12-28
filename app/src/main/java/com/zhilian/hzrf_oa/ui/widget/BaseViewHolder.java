package com.zhilian.hzrf_oa.ui.widget;

import android.util.SparseArray;
import android.view.View;

/**
 * @Description:万能的viewHolder
 * @author http://blog.csdn.net/finddreams
 * Created by Administrator on 2016/8/13.
 */
public class BaseViewHolder {
    @SuppressWarnings("unchecked")
    public static <T extends View> T get(View view, int id) {
        SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
        if (viewHolder == null) {
            viewHolder = new SparseArray<View>();
            view.setTag(viewHolder);
        }
        View childView = viewHolder.get(id);
        if (childView == null) {
            childView = view.findViewById(id);
            viewHolder.put(id, childView);
        }
        return (T) childView;
    }
}
