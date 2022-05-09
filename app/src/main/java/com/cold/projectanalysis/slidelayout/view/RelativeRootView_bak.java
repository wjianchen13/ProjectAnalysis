package com.cold.projectanalysis.slidelayout.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;

import com.cold.projectanalysis.slidelayout.SlideLayoutActivity;

public class RelativeRootView_bak extends RelativeLayout implements IClearRootView {

//    private final int MIN_SCROLL_SIZE = ApplicationUtilV2.dip2px(66);
    private final int MIN_SCROLL_SIZE = 132;
    private final int LEFT_SIDE_X = 0;
    private final int RIGHT_SIDE_X = getResources().getDisplayMetrics().widthPixels;

    private int mDownX;
    private int mEndX;
    private ValueAnimator mEndAnimator;

    private boolean isCanSrcoll;

    @Constants.Orientation
    private int mOrientation;

    private int page = 1;

    private IPositionCallBack mIPositionCallBack;
    private IClearEvent mIClearEvent;

    private SlideLayoutActivity mRoomActivity;

    private float startX;
    private float startY;

    private boolean isAnimating = false;

    public void setRoomActivity(SlideLayoutActivity roomActivity) {
        mRoomActivity = roomActivity;
    }

    @Override
    public void setIPositionCallBack(IPositionCallBack l) {
        mIPositionCallBack = l;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    public void setIClearEvent(IClearEvent l) {
        mIClearEvent = l;
    }

    public RelativeRootView_bak(Context context) {
        this(context, null);
    }

    public RelativeRootView_bak(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RelativeRootView_bak(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mEndAnimator = ValueAnimator.ofFloat(0, 1.0f).setDuration(400);
        mEndAnimator.setInterpolator(new DecelerateInterpolator());
        mEndAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                isAnimating = true;

                float factor = (float) valueAnimator.getAnimatedValue();
                int diffX = mEndX - mDownX;
                mIPositionCallBack.onPositionChange((int) (mDownX + diffX * factor), 0);

            }
        });
        mEndAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) { // 动画结束
                isAnimating = false;
                if (mOrientation == (Constants.RIGHT) && mEndX == RIGHT_SIDE_X) { // 向右移动，清除布局
                    mIClearEvent.onClearEnd();
                    mOrientation = Constants.LEFT;
                    page = 0;
                    mDownX = 0;
                } else if (mOrientation == (Constants.LEFT) && mEndX == LEFT_SIDE_X) { // 向左移动，恢复布局
                    mIClearEvent.onRecovery();
                    mOrientation = Constants.RIGHT;
                    mDownX = RIGHT_SIDE_X;
                    page = 1;
                } else if (mOrientation == (Constants.RIGHT) && mEndX != RIGHT_SIDE_X) { // 向左移动，恢复布局
                    mIClearEvent.onRecovery();
                    mDownX = RIGHT_SIDE_X;
                    page = 1;
                } else if (mOrientation == (Constants.LEFT) && mEndX != LEFT_SIDE_X) {  // 向右移动，清除布局
                    mIClearEvent.onClearEnd();
                    mDownX = 0;
                    page = 0;
                }
                //               mDownX = mEndX;
                isCanSrcoll = false;
            }

        });
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    @Override
    public void setClearSide(@Constants.Orientation int orientation) {
        mOrientation = orientation;
    }

    private int mLastX = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        System.out.println("RelativieRootView onTouchEvent");
        int index = event.getActionIndex();
        if (index != 0)
            return false;
        int pointerId = event.getPointerId(index);
        if (pointerId != 0)
            return false;
        int x = (int) event.getX();
        int offsetX = x - mDownX;
        System.out.println("RelativieRootView onTouchEvent 2");
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                if (mLastX > 0) {
                    x = mLastX;
                    offsetX = x - mDownX;
                }
                if (isGreaterThanMinSize(mDownX, x) && isCanSrcoll && !isAnimating) {
                    mIPositionCallBack.onPositionChange(getPositionChangeX(offsetX), 0);
                    return true;
                }
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                // 手指数量发生变化，更新lastXY为追踪手指
                // 根据手指ID生成流程图，在该事件到来时索引0对应的手指一定存在，且ID最小
                mLastX = (int) event.getX(0);
                break;

            case MotionEvent.ACTION_POINTER_UP:
                // 定义一个记录当前松开手指Index的变量
                int indexOfUpPointer = event.getActionIndex();

                // 如果松开的手指刚好是追踪手指
                if (indexOfUpPointer == 0) {
                    // !!!核心!!!：更新lastXY为接下来将被追踪的手指的当前坐标
                    mLastX = (int) event.getX(0);
                }
                startAnimation(offsetX, x);
                break;
            case MotionEvent.ACTION_UP:
                startAnimation(offsetX, x);
        }
        return super.onTouchEvent(event);
    }

    private void startAnimation(int offsetX, int x) {
        //isGreaterThanMinSize(mDownX, x) &&
        if (isCanSrcoll && !isAnimating) {
            if (mLastX > 0) {
                offsetX = mLastX - mDownX;
            }
            mDownX = getPositionChangeX(offsetX);
            fixPostion(offsetX);

            isAnimating = true;
            mEndAnimator.cancel();
            mEndAnimator.start();
            mLastX = 0;
        }
        postDelayed(mRunnable, 800);
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (mRoomActivity == null)
                return;

        }

    };


    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        System.out.println("-----------------------> RelativeRootView onInterceptTouchEvent");
        final int x = (int) event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!isAnimating) {
                    mDownX = x;
                }
                return false;

            case MotionEvent.ACTION_MOVE:
                if (isGreaterThanMinSize(mDownX, x)) {
                    isCanSrcoll = true;
                    return true;
                } 
        }
        return super.onInterceptTouchEvent(event);
    }

    private int getPositionChangeX(int offsetX) {
        int absOffsetX = Math.abs(offsetX);
        if (mOrientation == (Constants.RIGHT)) {
            return absOffsetX - MIN_SCROLL_SIZE > 0 ? absOffsetX - MIN_SCROLL_SIZE : 0;
        } else {
            return RIGHT_SIDE_X - (absOffsetX - MIN_SCROLL_SIZE);
        }
    }

    private void fixPostion(int offsetX) {
        int absOffsetX = Math.abs(offsetX);
        if (mOrientation == (Constants.RIGHT) && absOffsetX > RIGHT_SIDE_X / 3) {  // 显示，并且滑动距离大于1/3屏幕宽
            mEndX = RIGHT_SIDE_X;
        } else if (mOrientation == (Constants.LEFT) && (absOffsetX > RIGHT_SIDE_X / 3)) { // 隐藏，并且滑动距离大于1/3屏幕宽
            mEndX = LEFT_SIDE_X;

        } else if (mOrientation == (Constants.RIGHT) && absOffsetX <= RIGHT_SIDE_X / 3) { // 显示，并且滑动距离大于1/3屏幕宽
            mEndX = LEFT_SIDE_X;

        } else if (mOrientation == (Constants.LEFT) && (absOffsetX <= RIGHT_SIDE_X / 3)) { // 隐藏，并且滑动距离大于1/3屏幕宽
            mEndX = RIGHT_SIDE_X;
        }
    }

    public boolean isGreaterThanMinSize(int x1, int x2) {
        if (mOrientation == (Constants.RIGHT)) {
            return x2 - x1 > MIN_SCROLL_SIZE;
        } else {
            return x1 - x2 > MIN_SCROLL_SIZE;
        }
    }

    public void removeCallBackAndListener() {
        removeCallbacks(mRunnable);
    }
}
