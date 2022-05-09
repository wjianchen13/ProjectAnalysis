package com.cold.slideview11;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import androidx.customview.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import java.lang.reflect.Field;

/**
 * name: SlidingLayout
 * desc: 侧滑菜单
 * author:
 * date: 2017-06-16 15:10
 * remark:
 * 17-06-21 14:40 MyFrameLayout改为了独立的类，直接使用内部类没有效果，修改了onFinishInflate方法
 * 17-06-26 10:50 把子布局从3层改为2层，把清屏菜单当做侧滑手势捕获控件，修改了xml布局，onFinishInflate方法
 * 17-06-26 11:40 把SlidingLayout修改为最顶层，取消了对子view数量显示，通过tag获取2个控制布局的引用
 *                把RelativeLayout修改为FrameLayout
 * 17-06-26 15:30 通过给子控件设置tag，然后在这里通过 findViewWithTag()的方法获取控件引用失败，因为使用了
 *                databinding之后控件的tag改变了，具体原因需要查看源码
 * 17-06-26 15:30 在xml中视图通过app:imageUrl="@{@id/rl_control}"使用失败
 * 17-07-13 11:00 实现mDrawerview右半屏活动， 任意大小
 * 17-07-14 16:30 tryCaptureView 返回false
 * 17-07-17 15:30 1.实现侧滑不随手势移动 2.实现点击侧滑外空白处隐藏菜单 3.onTouch事件处理添加
 */
public class SlidingLayout extends FrameLayout implements View.OnTouchListener {

    /**
     * 滑动速率，每秒dip
     */
    private static final int SLIDING_VELOCITY = 600;

    /**
     * 抽屉视图
     */
    private View mDrawerView;

    /**
     * 侧滑视图
     */
    private View mSlideView;

    /**
     *
     */
    private Context mContext;

    /**
     * 页面状态 1:侧边栏可见，清屏可见 2:侧边栏不可见 清屏可见 3:侧边栏和清屏界面不可见，初始状态是都可见
     */
    private int state = 1;

    /**
     * ViewDragHelper
     */
    private ViewDragHelper mHelper1;

    /**
     * 内容id
     */
    private String contentViewId;

    /**
     * 侧滑id
     */
    private String slideViewId;

    /**
     * drawer显示出来的占自身的百分比
     */
    private float mLeftMenuOnScrren;

    /**
     * slideview 移动百分比
     */
    private float mSlideViewOnScreen;

    /**
     * 控制的view
     */
    private View controlView;

    /**
     * 是否计算完成
     */
    private boolean isCalculate = false;

    /**
     * 侧边栏滑动固定
     */
    private int newLeft;

    /**
     * ViewDragHelper回调接口
     */
    ViewDragHelper.Callback cb = new ViewDragHelper.Callback() {
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            int drawerLeft = Math.min( Math.max(getWidth() - child.getWidth(), left), getWidth());
            if(child == mSlideView) {
                Log.e("Tag", "===================================> " + "newLeft: " + newLeft);
                return newLeft;
            }
            return drawerLeft;
        }

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return false;
        }

        @Override
        public void onEdgeDragStarted(int edgeFlags, int pointerId) {
            mHelper1.captureChildView(controlView, pointerId);
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            final int childWidth = changedView.getWidth();
            float offset = (left - (getWidth() - childWidth))* 1.0f / childWidth;
            if(changedView == mDrawerView) {
                mLeftMenuOnScrren = offset;
            } else if(changedView == mSlideView){
                mSlideViewOnScreen = offset;
            }
            changedView.setVisibility(offset == 1 ? View.INVISIBLE : View.VISIBLE);
            invalidate();
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            final int childWidth = releasedChild.getWidth();
            float offset = (float)(releasedChild.getLeft() - (getWidth() - childWidth)) / (float)childWidth;
            if(releasedChild == mDrawerView) {
                mLeftMenuOnScrren = offset;
            } else if(releasedChild == mSlideView){
                mSlideViewOnScreen = offset;
            }
            if(xvel < 0 || xvel == 0 && offset < 0.5f ) { // 目标向左移动
                if(releasedChild == mDrawerView) {
                    state = 2;
                } else if(releasedChild == mSlideView){
                    newLeft = getWidth() - childWidth;
                    state = 1;
                }
                mHelper1.settleCapturedViewAt(getWidth() - childWidth, releasedChild.getTop());
            } else { // 目标向右移动
                if(releasedChild == mDrawerView) {
                    state = 3;
                } else if(releasedChild == mSlideView){
                    newLeft = getWidth();
                    state = 2;
                }
                mHelper1.settleCapturedViewAt(getWidth(), releasedChild.getTop());
            }
            invalidate();
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return controlView == child ? child.getWidth() : 0;
        }
    };

    public SlidingLayout(Context context, AttributeSet attrs)  {
        super(context, attrs);
        this.mContext = context;

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.viewid);
        contentViewId = ta.getString(R.styleable.viewid_contentview);
        slideViewId = ta.getString(R.styleable.viewid_slideview);
        ta.recycle();

        final float density = getResources().getDisplayMetrics().density;
        final float minVel = SLIDING_VELOCITY * density;

        mHelper1 = ViewDragHelper.create(this, 1.0f, cb);
        mHelper1.setEdgeTrackingEnabled(ViewDragHelper.EDGE_RIGHT);
        mHelper1.setMinVelocity(minVel);

        setDrawerLeftEdgeSize(context, mHelper1, 1.0f);
        setOnTouchListener(this);
    }

    /**
     * 设置滑动范围
     * @param: activity
     * @param: dragHelper 设置范围的ViewDragHelper
     * @param: displayWidthPercentage 滑动因子，为 1 全屏滑动
     * @return: void
     */
    public void setDrawerLeftEdgeSize(Context activity, ViewDragHelper dragHelper, float displayWidthPercentage) {
        Field edgeSizeField;
        try {
            edgeSizeField = dragHelper.getClass().getDeclaredField("mEdgeSize");
            edgeSizeField.setAccessible(true);
            DisplayMetrics dm = new DisplayMetrics();
            ((Activity)activity).getWindowManager().getDefaultDisplay().getMetrics(dm);
            edgeSizeField.setInt(dragHelper, (int) (dm.widthPixels * displayWidthPercentage));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed,l, t, r,b);
        MarginLayoutParams lp = (MarginLayoutParams) mDrawerView.getLayoutParams();
        final int menuWidth = mDrawerView.getMeasuredWidth();
        int childLeft = (getWidth() - menuWidth) + (int)(menuWidth * mLeftMenuOnScrren);
        mDrawerView.layout(childLeft, lp.topMargin, childLeft + menuWidth, lp.topMargin + mDrawerView.getMeasuredHeight());

        MarginLayoutParams lp1 = (MarginLayoutParams) mSlideView.getLayoutParams();
        final int menuWidth1 = mSlideView.getMeasuredWidth();
        int childLeft1 = (getWidth() - menuWidth1) + (int)(menuWidth1 * mSlideViewOnScreen);
        mSlideView.layout(childLeft1, lp1.topMargin, childLeft1 + menuWidth1, lp1.topMargin + mSlideView.getMeasuredHeight());

        newLeft = mSlideView.getLeft();
        System.out.println("------------------------> onLayout");
    }

    /**
     * 获取容器内的控件引用
     * @param:
     * @return:
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mDrawerView = findViewById(getResources().getIdentifier(contentViewId, "id", mContext.getPackageName()));
        if(mDrawerView == null) {
            throw new IllegalArgumentException("DrawView is null ");
        }

        mSlideView = findViewById(getResources().getIdentifier(slideViewId, "id", mContext.getPackageName()));
        if(mSlideView == null) {
            throw new IllegalArgumentException("SlideView is null ");
        }
        controlView = mSlideView;
    }

    /**
     * 按下点X坐标
     */
    private float startX;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                startX = ev.getX();
                mHelper1.shouldInterceptTouchEvent(ev);
                return false;
            case MotionEvent.ACTION_MOVE:
//                float endX = ev.getX();
//                float distanceX = endX - startX;
//                if(mSlideView.getVisibility() == View.VISIBLE) {
//                    return false;
//                } else if(mSlideView.getVisibility() == View.GONE) {
//                    if ((flag && distanceX > 0 && isGreaterThanMinSize(endX, startX)) || (!flag && distanceX < 0 && isGreaterThanMinSize(endX, startX))) {
//                        return mHelper1.shouldInterceptTouchEvent(ev);
//                    } else if ((flag && distanceX < 0) || (!flag && distanceX > 0)) {
//                        return false;
//                    }
//                }
                break;
            case MotionEvent.ACTION_UP:
                return mHelper1.shouldInterceptTouchEvent(ev);
            default:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    /**
     * 滑动范围阈值，可以修改布局上存在滑动控件时的移动范围
     * @param:
     * @return:
     */
    public boolean isGreaterThanMinSize(float x1, float x2) {
        return Math.abs((int)(x1 - x2)) > 66;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = ev.getX();
                mHelper1.processTouchEvent(ev);
                isCalculate = false;//计算开始
                break;
            case MotionEvent.ACTION_MOVE:
                float endX = ev.getX();
                float distanceX = endX - startX;
                if(!isCalculate) {
                    if ((mSlideView.getLeft() >= getWidth() - mSlideView.getWidth()) && (mSlideView.getLeft() <  getWidth() - mSlideView.getWidth() / 2)) { // 侧边栏显示
                        if(distanceX > 0) { // 向右滑动
                            controlView = mSlideView;
                            isCalculate = true;
                        } else { // 向左滑动，不叼他
                        }
                    } else if ((mSlideView.getLeft() >= getWidth() - mSlideView.getWidth() / 2) && (mDrawerView.getLeft() < getWidth() - mDrawerView.getWidth() / 2)) { // 侧边栏隐藏，清屏界面显示
                        if(distanceX > 0) { // 向右滑动，移动drawerview
                            controlView = mDrawerView;
                            isCalculate = true;
                        } else if(distanceX < 0) {// 向左滑动，移动slideview
                            controlView = mSlideView;
                            isCalculate = true;
                        }
                    } else if (mDrawerView.getLeft() >= getWidth() - mDrawerView.getWidth() / 2) { // 清屏界面隐藏
                        if(distanceX < 0) { // 移动移动drawerview
                            controlView = mDrawerView;
                            isCalculate = true;
                        } else { // 不管
                        }
                    }
                }
                if(isCalculate) {
                    mHelper1.processTouchEvent(ev);
                }

                break;
            case MotionEvent.ACTION_UP:
                mHelper1.processTouchEvent(ev);
                break;
            default:
                mHelper1.processTouchEvent(ev);
                break;
        }
        return true;
    }

    @Override
    public void computeScroll() {
        if (mHelper1.continueSettling(true)) {
            invalidate();
        }
    }

    /**
     * 关闭drawer，预留
     * @param:
     * @return: void
     */
    public void closeSlideMenu() {
        View menuView = mSlideView;
        mSlideViewOnScreen = 1.0f;
        newLeft = getWidth();
        mHelper1.smoothSlideViewTo(menuView, getWidth(), menuView.getTop());
        invalidate();
    }

    /**
     * 打开drawer，预留
     * @param:
     * @return: void
     */
    public void openSlideMenu() {
        View menuView = mSlideView;
        mSlideViewOnScreen = 0.0f;
        newLeft = getWidth() - mSlideView.getWidth();
        mHelper1.smoothSlideViewTo(menuView, getWidth() - mSlideView.getWidth(), menuView.getTop());
        invalidate();
    }

    /**
     * 判断侧滑菜单是否打开
     * @param:
     * @return:
     */
    public boolean isSlideMenuOpen() {
        return mSlideView.getLeft() >= getWidth() - mSlideView.getWidth() && mSlideView.getLeft() <  getWidth() - mSlideView.getWidth() / 2;
    }

    /**
     * 以下4个参数用来记录按下点的初始坐标和结束坐标
     */
    private float firstDownX = 0;
    private float lastDownX = 0;
    private float firstDownY = 0;
    private float lastDownY = 0;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                firstDownX = event.getX();
                firstDownY = event.getY();
                lastDownX = event.getX();
                lastDownY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                lastDownX = event.getX();
                lastDownY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                lastDownX = event.getX();
                lastDownY = event.getY();
                float deltaX = Math.abs(lastDownX - firstDownX);
                float deltaY = Math.abs(lastDownY - firstDownY);
                if (deltaX < 10 && deltaY < 10) {
                    if(isTouchCloseSlideArea(lastDownX, lastDownY)) {
                        if(isSlideMenuOpen()){
                            closeSlideMenu();
                            return true;
                        }
                    }
                }
                break;
        }
        return false;
    }

    /**
     * 判断是否点击在了关闭侧边栏的区域
     * @param: x 按下点x坐标
     * @param: y 按下点y坐标
     * @return: true：关闭 false：不关闭
     */
    private boolean isTouchCloseSlideArea(float x, float y) {
        int left = 0;
        int top = 0;
        int right = getWidth() - mSlideView.getWidth();
        int bottom = mSlideView.getHeight();
        if (x >= left && x <= right && y >= top && y <= bottom) {
            return true;
        }
        return false;
    }

}