/*
 *  The MIT License (MIT)
 *
 *  Copyright (c) 2015 cpoopc
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */
package com.zhilian.hzrf_oa.ui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;
import android.widget.Scroller;

import com.zhilian.hzrf_oa.util.Constants;

/**
 * 个人日程星期栏以下（日期+事件列表）的自定义布局
 * 实现滑动到当天或者点击的那一天停止，下面布局显示事件列表
 */
public class ScrollableLayout extends LinearLayout {

    private final String tag = "cp:scrollableLayout";
    private float mDownX;               //记录上次点击屏幕的横坐标
    private float mDownY;               //记录上次点击屏幕的纵坐标
    private float mLastY;
    private float mDownYScrollY;        //记录手指按下时的Y坐标，用于区分上滑还是下滑
    private float mDownXScrollY;        //记录手指按下时的X坐标，用于区分是否为左右滑动
    private float mDownUpY;             //用于手指抬起时判断上滑还是下滑
    private String isOpenOrClose;       //用于手指按下时判断日历状态；close:为收缩状态，open：为展开状态
    private int minY = 0;
    private int maxY = 0;
    private int mHeadHeight;
    private int mExpandHeight;
    private int mTouchSlop;
    private int mMinimumVelocity;
    private int mMaximumVelocity;
    // 方向
    private DIRECTION mDirection;
    private int mCurY;
    private int mLastScrollerY;
    private boolean needCheckUpdown;
    private boolean updown;
    private boolean mDisallowIntercept;
    private boolean isClickHead;
    private boolean isClickHeadExpand;
    private View mHeadView;
    private ViewPager childViewPager;
    private Scroller mScroller;
    private VelocityTracker mVelocityTracker;
    private boolean isFirstMoveDown = true;
    private int slippageDirection = 0;// 实时获取listView滑动方向，0为像上滑动，1为向下滑动

    /**
     * 滑动方向 *
     */
    enum DIRECTION {
        UP,// 向上划
        DOWN// 向下划
    }

    public interface OnScrollListener {

        void onScroll(int currentY, int maxY);

    }

    private OnScrollListener onScrollListener;

    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    private ScrollableHelper mHelper;

    public ScrollableHelper getHelper() {
        return mHelper;
    }

    public ScrollableLayout(Context context) {
        super(context);
        init(context);
    }

    public ScrollableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public ScrollableLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /*@TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ScrollableLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }*/

    private void init(Context context) {
        mHelper = new ScrollableHelper();
        mScroller = new Scroller(context);
        final ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = configuration.getScaledTouchSlop();
        mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
    }

    public boolean isSticked() {
        return mCurY == maxY;
    }

    /**
     * 扩大头部点击滑动范围
     *
     * @param expandHeight
     */
    public void setClickHeadExpand(int expandHeight) {
        mExpandHeight = expandHeight;
    }

    public int getMaxY() {
        return maxY;
    }

    /**
     * 判断日历展开到最大
     * @return
     */
    public boolean isOpenMax() {
        return mCurY == minY;
    }
    /**
     * 判断日历收缩到最小
     * @return
     */
    public boolean isCloseMin() {
        return mCurY == maxY;
    }
    /**
     * 设置listView滑动方向
     * @return
     */
    public void setSlippageDirection(int direction){
        this.slippageDirection = direction;
    }
    /**
     * 设置下拉回弹效果判断条件
     * @return
     */
    public void setIsFirstMoveDown(){
        if(isCloseMin()){
            this.isFirstMoveDown = true;
        }
        if(isCloseMin() && slippageDirection == 1 && mHelper.isTop()){
            isFirstMoveDown = false;
        }
    }
    public boolean canPtr() {
        return updown && mCurY == minY && mHelper.isTop();
    }

    public void requestScrollableLayoutDisallowInterceptTouchEvent(boolean disallowIntercept) {
        super.requestDisallowInterceptTouchEvent(disallowIntercept);
        mDisallowIntercept = disallowIntercept;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        float currentX = ev.getX();
        float currentY = ev.getY();
        float deltaY;
        int shiftX = (int) Math.abs(currentX - mDownX);
        int shiftY = (int) Math.abs(currentY - mDownY);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDisallowIntercept = false;
                needCheckUpdown = true;
                updown = true;
                mDownX = currentX;
                mDownY = currentY;
                mLastY = currentY;
                mDownYScrollY = currentY;
                mDownXScrollY = currentX;
                mDownUpY = currentY;
                if(isOpenMax()){
                    isOpenOrClose = "open";
                }
                if(isCloseMin()){
                    isOpenOrClose = "close";
                }
                checkIsClickHead((int) currentY, mHeadHeight, getScrollY());
                checkIsClickHeadExpand((int) currentY, mHeadHeight, getScrollY());
                initOrResetVelocityTracker();
                mVelocityTracker.addMovement(ev);
                mScroller.forceFinished(true);
                break;
            case MotionEvent.ACTION_MOVE:
                if (mDisallowIntercept) {
                    break;
                }
                if(slippageDirection == 1 && mHelper.isTop() && isFirstMoveDown){
//                    Log.i("zxListView","第一次下拉滑动");
                    break;
                }

                initVelocityTrackerIfNotExists();
//                mVelocityTracker.addMovement(ev);
                deltaY = mLastY - currentY;
                if (needCheckUpdown) {
                    if (shiftX > mTouchSlop && shiftX > shiftY) {
                        needCheckUpdown = false;
                        updown = false;
                    } else if (shiftY > mTouchSlop && shiftY > shiftX) {
                        needCheckUpdown = false;
                        updown = true;
                    }
                }

                if (updown && shiftY > mTouchSlop && shiftY > shiftX &&
                        (!isSticked() || mHelper.isTop() || isClickHeadExpand)) {

                    if (childViewPager != null) {
                        childViewPager.requestDisallowInterceptTouchEvent(true);
                    }
                    scrollBy(0, (int) (deltaY + 0.5));
                }
                mLastY = currentY;
                break;
            case MotionEvent.ACTION_UP:
                if(mDownYScrollY > currentY){
                    mDirection = DIRECTION.UP;
                } else if(mDownYScrollY < currentY) {
                    mDirection = DIRECTION.DOWN;
                }
                if(mDirection != null){
                    if( mDirection == DIRECTION.UP){
                        if(mDownXScrollY - currentX < 120 && mDownXScrollY - currentX > -120){
                            post(ScrollRunnable);
                            mDownUpY = 0;
                        }
                    } else if(mHelper.isTop() && !isCloseMin()){
                        if(mDownXScrollY - currentX < 120 && mDownXScrollY - currentX > -120){
                            post(ScrollRunnable);
                            mDownUpY = 0;
                        }
                    }
                }
                if(isCloseMin() && mDirection == DIRECTION.DOWN && mHelper.isTop()){
                    isFirstMoveDown = false;
                }
                break;
            default:
                break;
        }
        super.dispatchTouchEvent(ev);
        return true;
    }
    private Handler mHandler = new Handler();
    private int scrollHight = 0;
    private Runnable ScrollRunnable = new Runnable() {
        @Override
        public void run() {
            if (mDirection == DIRECTION.UP){
                scrollBy(0,50);
            } else {
                scrollBy(0, -50);
            }
            if (scrollHight >= maxY) {
                Thread.currentThread().interrupt();
                scrollHight = 0;
            } else {
                mHandler.postDelayed(this, 10);
            }
            scrollHight += 50;
        }
    };
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private int getScrollerVelocity(int distance, int duration) {
        if (mScroller == null) {
            return 0;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            return (int) mScroller.getCurrVelocity();
        } else {
            return distance / duration;
        }
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            final int currY = mScroller.getCurrY();
            if (mDirection == DIRECTION.UP) {
                // 手势向上划
                if (isSticked()) {
                    int distance = mScroller.getFinalY() - currY;
                    int duration = calcDuration(mScroller.getDuration(), mScroller.timePassed());
                    mHelper.smoothScrollBy(getScrollerVelocity(distance, duration), distance, duration);
                    mScroller.forceFinished(true);
                    return;
                } else {
                    scrollTo(0, currY);
                }
            } else {
                // 手势向下划
                if (mHelper.isTop() || isClickHeadExpand) {
                    int deltaY = (currY - mLastScrollerY);
                    int toY = getScrollY() + deltaY;
                    scrollTo(0, toY);
                    if (mCurY <= minY) {
                        mScroller.forceFinished(true);
                        return;
                    }
                }
                invalidate();
            }
            mLastScrollerY = currY;
        }
    }

    @Override
    public void scrollBy(int x, int y) {
        int scrollY = getScrollY();
        int toY = scrollY + y;
        if (toY >= maxY) {
            toY = maxY;
        } else if (toY <= minY) {
            toY = minY;
        }
        y = toY - scrollY;
        super.scrollBy(x, y);
    }

    @Override
    public void scrollTo(int x, int y) {
        if (y >= maxY) {
            y = maxY;
        } else if (y <= minY) {
            y = minY;
        }
        mCurY = y;
        if (onScrollListener != null) {
            onScrollListener.onScroll(y, maxY);
        }
        super.scrollTo(x, y);
    }

    private void initOrResetVelocityTracker() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        } else {
            mVelocityTracker.clear();
        }
    }

    private void initVelocityTrackerIfNotExists() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
    }

    private void recycleVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    private void checkIsClickHead(int downY, int headHeight, int scrollY) {
        isClickHead = downY + scrollY <= headHeight;
    }

    private void checkIsClickHeadExpand(int downY, int headHeight, int scrollY) {
        if (mExpandHeight <= 0) {
            isClickHeadExpand = false;
        }
        isClickHeadExpand = downY + scrollY <= headHeight + mExpandHeight;
    }

    private int calcDuration(int duration, int timepass) {
        return duration - timepass;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mHeadView = getChildAt(0);
        measureChildWithMargins(mHeadView, widthMeasureSpec, 0, MeasureSpec.UNSPECIFIED, 0);
        // 此处修改 最后显示日历的高度
        maxY = mHeadView.getMeasuredHeight() - Constants.dip2px(getContext(), 40f);
        mHeadHeight = mHeadView.getMeasuredHeight();
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(heightMeasureSpec) + maxY, MeasureSpec.EXACTLY));
    }

    @Override
    protected void onFinishInflate() {
        if (mHeadView != null && !mHeadView.isClickable()) {
            mHeadView.setClickable(true);
        }
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            if (childAt != null && childAt instanceof ViewPager) {
                childViewPager = (ViewPager) childAt;
            }
        }
        super.onFinishInflate();
    }

    public void stopSliding(){
        mScroller.forceFinished(true);
    }
}
