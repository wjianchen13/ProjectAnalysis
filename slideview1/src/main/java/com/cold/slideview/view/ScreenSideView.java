package com.cold.slideview.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;


public class ScreenSideView extends LinearLayout{

    private final int MIN_SCROLL_SIZE = 30;
    private final int LEFT_SIDE_X = 0;
    private final int RIGHT_SIDE_X = getResources().getDisplayMetrics().widthPixels;

    private int mDownX;
    private int mEndX;
    private ValueAnimator mEndAnimator;

    private boolean isCanSrcoll;

    @Constants.Orientation
    private int mOrientation;

    private IPositionCallBack mIPositionCallBack;
    private IClearEvent mIClearEvent;

    public void setIPositionCallBack(IPositionCallBack l) {
        mIPositionCallBack = l;
    }

    public void setIClearEvent(IClearEvent l) {
        mIClearEvent = l;
    }

    public ScreenSideView(Context context) {
        this(context, null);
    }

    public ScreenSideView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScreenSideView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mEndAnimator = ValueAnimator.ofFloat(0, 1.0f).setDuration(200);
        mEndAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float factor = (float) valueAnimator.getAnimatedValue();
                int diffX = mEndX - mDownX;
                mIPositionCallBack.onPositionChange((int) (mDownX + diffX * factor), 0);
            }
        });
        mEndAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (mOrientation == (Constants.RIGHT) && mEndX == RIGHT_SIDE_X) {
                    mIClearEvent.onClearEnd();
                    mOrientation = Constants.LEFT;
                } else if (mOrientation == (Constants.LEFT) && mEndX == LEFT_SIDE_X) {
                    mIClearEvent.onRecovery();
                    mOrientation = Constants.RIGHT;
                }
                mDownX = mEndX;
                isCanSrcoll = false;
            }
        });
    }


    public void setClearSide(@Constants.Orientation int orientation) {
        mOrientation = orientation;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int x = (int) event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (isScrollFromSide(x)) {
                    isCanSrcoll = true;
                    return true;
                }
            case MotionEvent.ACTION_MOVE:
                if (isGreaterThanMinSize(x) && isCanSrcoll) {
                    mIPositionCallBack.onPositionChange(getRealTimeX(x), 0);
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (isGreaterThanMinSize(x) && isCanSrcoll) {
                    mDownX = getRealTimeX(x);
                    fixPostion();
                    mEndAnimator.start();
                }
        }
        return super.onTouchEvent(event);
    }

    private int getRealTimeX(int x) {
        if (mOrientation == (Constants.RIGHT) && mDownX > RIGHT_SIDE_X / 3
                || mOrientation == (Constants.LEFT) && (mDownX > RIGHT_SIDE_X * 2 / 3)) {
            return x + MIN_SCROLL_SIZE;
        } else {
            return x - MIN_SCROLL_SIZE;
        }
    }

    private void fixPostion() {
        if (mOrientation == (Constants.RIGHT) && mDownX > RIGHT_SIDE_X / 3) {
            mEndX = RIGHT_SIDE_X;
        } else if (mOrientation == (Constants.LEFT) && (mDownX < RIGHT_SIDE_X * 2 / 3)) {
            mEndX = LEFT_SIDE_X;
        }
    }

    private boolean isGreaterThanMinSize(int x) {
        int absX = Math.abs(mDownX - x);
        return absX > MIN_SCROLL_SIZE;
    }

    private boolean isScrollFromSide(int x) {
        if (x <= LEFT_SIDE_X + MIN_SCROLL_SIZE && mOrientation == (Constants.RIGHT)
                || (x > RIGHT_SIDE_X - MIN_SCROLL_SIZE && mOrientation == (Constants.LEFT))) {
            return true;
        } else {
            return false;
        }
    }
}
