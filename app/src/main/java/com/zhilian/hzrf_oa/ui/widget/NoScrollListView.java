package com.zhilian.hzrf_oa.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 自定义ListView，解决和ScrollView的冲突
 */
public class NoScrollListView extends ListView{
    public NoScrollListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public NoScrollListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoScrollListView(Context context) {
        super(context);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 设置为Integer.MAX_VALUE>>2 是listview全部展开
        int measureSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE>>2, MeasureSpec.AT_MOST);
        // 设置为400是设置listview的高度只能有400 不全部展开   实现可以滑动的效果
        int measureSpec1 = MeasureSpec.makeMeasureSpec(400, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, measureSpec);
    }


}
