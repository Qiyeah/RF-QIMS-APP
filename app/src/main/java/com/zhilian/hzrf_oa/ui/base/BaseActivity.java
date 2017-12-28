package com.zhilian.hzrf_oa.ui.base;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.zhilian.hzrf_oa.util.ThreadPoolManager;
import com.squareup.picasso.Picasso;

public abstract class BaseActivity extends FullTransparentActivity {

    private static final String TAG = "BaseActivity";

    // 状态栏高度
    protected int statusHeight;
    // View 绘制的高度
    protected int viewHeight;
    // 应用的高度
    protected int applicationHeight;

    protected ThreadPoolManager mThreadPoolManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mThreadPoolManager = ThreadPoolManager.getInstance();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            // getAreaScreen();
            // getAreaView();
            // getAreaApplication();
            // setPadding(getApplicationView(), 0, statusHeight, 0, (viewHeight - applicationHeight - statusHeight));
        }
    }

    /**
     * 设置View的Margin
     */
    protected void setMargins(View v, int left, int top, int right, int bottom) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            v.requestLayout();
        }
    }

    /**
     * 设置View的Padding
     */
    protected void setPadding(View v, int left, int top, int right, int bottom) {
        v.setPadding(left, top, right, bottom);
        v.requestLayout();
    }

    /**
     * 得到屏幕大小
     */
    protected void getAreaScreen() {
        Display display = getWindowManager().getDefaultDisplay();
        Point outP = new Point();
        display.getSize(outP);
        //LogUtil.d(TAG, "AreaScreen:" + " width=" + outP.x + " height=" + outP.y);
    }

    /**
     * 得到应用的大小
     */
    protected void getAreaApplication() {
        Rect outRect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect);
        statusHeight = outRect.top;
        applicationHeight = outRect.height();
        /*LogUtil.d(TAG, "AreaApplication:" + " width=" + outRect.width()
                + " height=" + outRect.height() + " top=" + outRect.top
                + " bottom=" + outRect.bottom);*/
    }

    /**
     * 得到View绘制的大小
     */
    protected void getAreaView() {
        // 用户绘制区域
        Rect outRect = new Rect();
        getWindow().findViewById(Window.ID_ANDROID_CONTENT).getDrawingRect(outRect);
        viewHeight = outRect.height();
        /*LogUtil.d(TAG, "AreaView:" + " width=" + outRect.width() + " height="
                + outRect.height());*/
    }

    /**
     * 运行在主线程中的Toast
     */
    public void showToastOnUI(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showToast(msg);
            }
        });
    }

    /**
     * 弹出Toast
     */
    public void showToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 子线程执行一个任务
     */
    public void executeTask(Runnable run) {
        mThreadPoolManager.executeTask(run);
    }

    /**
     * 隐藏输入法
     */
    public void hideSoftInput(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }


    /**
     * 加载图片
     */
    public void loadIMG(ImageView img, String url, int placeholder, int errorId) {
        Picasso.with(getApplicationContext()).load(url).error(errorId)
                .placeholder(placeholder).into(img);
    }

    protected void startActivity(Class<?> targetClass, int inAnimID, int outAnimID) {
        Intent intent = new Intent(this, targetClass);
        this.startActivity(intent);
        this.overridePendingTransition(inAnimID, outAnimID);
    }


}