package com.cold.slideview6;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import androidx.customview.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
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
 * 移动对象改为了mDrawerView
 */
public class SlidingLayout_bak1 extends FrameLayout implements ISlide {

    /**
     * 滑动速率，每秒dip
     */
    private static final int SLIDING_VELOCITY = 600;

    /**
     * 抽屉视图
     */
    private Button mControlView;

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
     * 抽屉视图是否可见
     */
    private boolean flag = true;

    /**
     * ViewDragHelper
     */
    private ViewDragHelper mHelper;

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
     * ViewDragHelper回调接口
     */
    ViewDragHelper.Callback cb = new ViewDragHelper.Callback() {
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {

//            System.out.println("=====================> clampViewPositionHorizontal left: " + left);
            int newLeft = Math.min( Math.max(getWidth() - child.getWidth(), left), getWidth());
            return newLeft;
        }

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            System.out.println("==========================> tryCaptureView");
            return child == mDrawerView;
        }

        @Override
        public void onEdgeDragStarted(int edgeFlags, int pointerId) {
            mHelper.captureChildView(mDrawerView, pointerId);
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy)
        {
            final int childWidth = changedView.getWidth();
            float offset = (left - (getWidth() - childWidth)) / childWidth;
//            float offset = (float) (childWidth + left) / childWidth; //
            mLeftMenuOnScrren = offset;
//            System.out.println("====================> mLeftMenuOnScrren: " + mLeftMenuOnScrren);
            //offset can callback here
            changedView.setVisibility(offset == 1 ? View.INVISIBLE : View.VISIBLE);
            invalidate();
        }


        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            final int childWidth = releasedChild.getWidth();
//            float offset = (childWidth - releasedChild.getLeft()) * 1.0f / childWidth;
            float offset = (float)(releasedChild.getLeft() - (getWidth() - childWidth)) / (float)childWidth;
            mLeftMenuOnScrren = offset;
            System.out.println("===========================> offset: " + offset);
            System.out.println("===========================> xvel: " + xvel);
            if(xvel < 0 || xvel == 0 && offset < 0.5f ) {
                flag = true;
            } else {
                flag = false;
            }

            mHelper.settleCapturedViewAt(flag ? getWidth() - childWidth : getWidth(), releasedChild.getTop());
            invalidate();
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return mDrawerView == child ? child.getWidth() : 0;
        }
    };

    public SlidingLayout_bak1(Context context, AttributeSet attrs)  {
        super(context, attrs);
        this.mContext = context;

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.viewid);
        contentViewId = ta.getString(R.styleable.viewid_contentview);
        slideViewId = ta.getString(R.styleable.viewid_slideview);
        ta.recycle();

        final float density = getResources().getDisplayMetrics().density;
        final float minVel = SLIDING_VELOCITY * density;

        mHelper = ViewDragHelper.create(this, 1.0f, cb);
        mHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_RIGHT);
        mHelper.setMinVelocity(minVel);


        setDrawerLeftEdgeSize(context, mHelper, 1.0f);
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
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {
        super.onLayout(changed,l, t, r,b);
        MarginLayoutParams lp = (MarginLayoutParams) mDrawerView.getLayoutParams();
        final int menuWidth = mDrawerView.getMeasuredWidth();
//        int childLeft = 0;
//        if(flag) {
//            childLeft = menuWidth - (int) (menuWidth * 1);
//        } else {
//            childLeft = menuWidth - (int) (menuWidth * 0);
//        }
        int childLeft = (getWidth() - menuWidth) + (int)(menuWidth * mLeftMenuOnScrren);
        mDrawerView.layout(childLeft, lp.topMargin, childLeft + menuWidth, lp.topMargin + mDrawerView.getMeasuredHeight());

//
//        int childLeft = -menuWidth + (int) (menuWidth * mLeftMenuOnScrren);
//        menuView.layout(childLeft, lp.topMargin, childLeft + menuWidth,
//                lp.topMargin + menuView.getMeasuredHeight());

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
//        ((SlideContentLayout)mDrawerView).setIslide(this);
//        ((SlideContentLayout)mDrawerView).setControl(mControlView);
//        ((SlideContentLayout)mDrawerView).setParent(mDrawerView);
        mSlideView = findViewById(getResources().getIdentifier(slideViewId, "id", mContext.getPackageName()));
        if(mSlideView == null) {
            throw new IllegalArgumentException("SlideView is null ");
        }
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
                mHelper.shouldInterceptTouchEvent(ev);
                return false;
            case MotionEvent.ACTION_MOVE:
//                float endX = ev.getX();
//                float distanceX = endX - startX;
//                if(mSlideView.getLeft() == getWidth() - mSlideView.getWidth()) { // 侧边栏显示
//
//                } else if(mSlideView.getLeft() == getWidth() && mDrawerView.getLeft() == 0) { // 侧边栏隐藏，清屏界面显示
//
//                } else if(mDrawerView.getLeft() == getWidth()) { // 清屏界面隐藏
//
//                }
//                else if(mSlideView.getVisibility() == View.GONE) {
//                    if ((flag && distanceX > 0 && isGreaterThanMinSize(endX, startX)) || (!flag && distanceX < 0 && isGreaterThanMinSize(endX, startX))) {
//                        return mHelper.shouldInterceptTouchEvent(ev);
//                    } else if ((flag && distanceX < 0) || (!flag && distanceX > 0)) {
//                        return false;
//                    }
//                }

                float endX = ev.getX();
                float distanceX = endX - startX;
                if(mSlideView.getVisibility() == View.VISIBLE) {
                    return false;
                } else if(mSlideView.getVisibility() == View.GONE) {
                    if ((flag && distanceX > 0 && isGreaterThanMinSize(endX, startX)) || (!flag && distanceX < 0 && isGreaterThanMinSize(endX, startX))) {
                        return mHelper.shouldInterceptTouchEvent(ev);
                    } else if ((flag && distanceX < 0) || (!flag && distanceX > 0)) {
                        return false;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                return mHelper.shouldInterceptTouchEvent(ev);
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
    public boolean onTouchEvent(MotionEvent event) {
        mHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public void computeScroll() {
        if (mHelper.continueSettling(true)) {
            invalidate();
        }
    }

    /**
     * 关闭drawer，预留
     * @param:
     * @return: void
     */
    public void closeDrawer() {
        View menuView = mDrawerView;
        mHelper.smoothSlideViewTo(menuView, -menuView.getWidth(), menuView.getTop());
    }

    /**
     * 打开drawer，预留
     * @param:
     * @return: void
     */
    public void openDrawer() {
        View menuView = mDrawerView;
        mHelper.smoothSlideViewTo(menuView, 0, menuView.getTop());
    }

    /**
     * 显示侧滑View
     * @param:
     * @return: void
     */
    @Override
    public void showSlideView() {
        if(mSlideView.getVisibility() == View.GONE) {
            TranslateAnimation mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                    0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
            mShowAction.setDuration(300);
            mSlideView.startAnimation(mShowAction);
            mSlideView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 关闭侧滑View
     * @param:
     * @return: void
     */
    @Override
    public void closeSlideView() {
        if(mSlideView.getVisibility() == View.VISIBLE) {
            TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                    0.0f, Animation.RELATIVE_TO_SELF, 1.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                    0.0f);
            mHiddenAction.setDuration(300);
            mSlideView.startAnimation(mHiddenAction);
            mSlideView.setVisibility(View.GONE);
        }
    }


}