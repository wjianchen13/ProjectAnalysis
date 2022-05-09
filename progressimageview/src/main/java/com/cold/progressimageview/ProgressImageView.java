package com.cold.progressimageview;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.TypedValue;

import androidx.appcompat.widget.AppCompatImageView;

/**
 * name: ProgressImageView
 * desc: 小苹果累加
 * author:
 * date: 2017-07-07 10:14
 * remark:
 */
public class ProgressImageView extends AppCompatImageView {

    /**
     * 默认圆弧的颜色
     */
    private final int mDefaultColor = Color.RED;

    /**
     * 默认宽度，dp
     */
    private final int mDefaultWidth = 2;

    /**
     * 默认刷新速率，ms
     */
    private final int mDefaultSpeed = 10;

    /**
     * 刷新界面消息
     */
    private final int MSG_INVALIDATE = 1;

    /**
     * 刷新周期，单位是秒
     */
    private int mRefreshTime = 1;

    /**
     * 单位进度
     */
    private float perPorgress;

    /**
     * 颜色
     */
    private int mColor;

    /**
     * 宽度
     */
    private float mWidth;

    /**
     * 当前进度
     */
    private float mProgress;

    /**
     * 刷新速率，/S
     */
    private int mSpeed;

    /**
     * 每次刷新时间
     */
    private int mSpeedTime;

    /**
     * 上下文
     */
    private Context mContext;

    /**
     * 画笔
     */
    private Paint mPaint;

    /**
     * 获取绘制圆弧的中心点x坐标
     */
    private float centerX;

    /**
     * 圆弧半径
     */
    private float mRadius;

    /**
     * 绘制区域
     */
    private RectF oval;

    private IProgressChangeListener changeListener;

    public ProgressImageView(Context context) {
        this(context, null);
        mContext = context;
    }

    public ProgressImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        mContext = context;
    }

    /**
     * 绘制圆弧
     */
    private Handler handler= new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what) {
                case MSG_INVALIDATE:
                    mProgress += perPorgress;
                    if (mProgress > 360) {
                        mProgress = 0;
                        invalidate();
                        handler.removeMessages(MSG_INVALIDATE);
                        if(changeListener != null) {
                            changeListener.onLoadFinish();
                        }
                        return ;
                    }
                    invalidate();
                    sendEmptyMessageDelayed(MSG_INVALIDATE, mSpeedTime);
                    break;
            }
        }
    };

    /**
     * 必要的初始化，获得一些自定义的值
     * @param context
     * @param attrs
     * @param defStyle
     * @return
     */
    public ProgressImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomProgressImageView, defStyle, 0);
        int n = a.getIndexCount();

        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.CustomProgressImageView_progressColor:
                    mColor = a.getColor(attr, mDefaultColor);
                    break;
                case R.styleable.CustomProgressImageView_progressWidth:
                    mWidth = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP, mDefaultWidth, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.CustomProgressImageView_progressSpeed:
                    mSpeed = a.getInt(attr, mDefaultSpeed);
                    break;
            }
        }
        a.recycle();

        perPorgress = (float)360 / (float)(mRefreshTime * mSpeed);
        mSpeedTime = 1000 / mSpeed;

        if(mPaint == null) {
            mPaint = new Paint();
            mPaint.setStrokeWidth(mWidth); // 设置圆环的宽度
            mPaint.setAntiAlias(true); // 消除锯齿
            mPaint.setStyle(Paint.Style.STROKE); // 设置空心
            mPaint.setColor(mColor);
        }

    }

    /**
     * 获取绘制圆弧的半径和宽
     * @param:
     * @return:
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        centerX = (float)getWidth() / 2;
        mRadius = centerX - mWidth / 2;
        oval = new RectF(centerX - mRadius, centerX - mRadius, centerX + mRadius, centerX + mRadius);
        super.onLayout(changed, left, top, right, bottom);
    }

    /**
     * 绘制圆弧
     * @param:
     * @return:
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawArc(oval, -90, mProgress, false, mPaint);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if(handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
    }

    /**
     * 设置刷新周期
     * @param: mRefreshTime 刷新时间
     * @return: void
     */
    public void setmRefreshTime(int mRefreshTime) {
        this.mRefreshTime = mRefreshTime;
        perPorgress = (float)360 / (float)(mRefreshTime * mSpeed);
    }

    /**
     * 设置坚挺
     * @param: changeListener
     * @return: void
     */
    public void setChangeListener(IProgressChangeListener changeListener) {
        this.changeListener = changeListener;
    }

    /**
     * 启动
     * @param:
     * @return: void
     */
    public void startPlay() {
        if(handler != null) {
            handler.sendEmptyMessage(MSG_INVALIDATE);
        }
    }

    /**
     * 停止
     * @param:
     * @return: void
     */
    public void stopPlay() {
        if(handler != null) {
            handler.removeMessages(MSG_INVALIDATE);
            mProgress = 0;
            invalidate();
        }
    }
}