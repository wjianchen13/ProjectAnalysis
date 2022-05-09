package com.cold.marquee2;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * name:HorizonScrollTextView
 * desc:实现文字滚动功能
 * author:
 * date: 2017-07-31 16:05
 * remark:
 */
public class HorizonScrollTextView extends AppCompatTextView {
	private boolean mStopMarquee;  
    private String mText;  
    private float mCoordinateX;

    /**
     * 是否需要滚动
     */
    private boolean needScroll;

    /**
     * 文字宽度
     */
    private float mTextWidth;

    /**
     * 暂停标志
     */
    private boolean mPaused = true;
   
   
    public HorizonScrollTextView(Context context, AttributeSet attrs) {  
        super(context, attrs);  
    }

    @Override
    protected void onAttachedToWindow() {
        mStopMarquee = false;
        super.onAttachedToWindow();
    }


    @Override
    protected void onDetachedFromWindow() {
        mStopMarquee = true;
        if (mHandler.hasMessages(0))
            mHandler.removeMessages(0);
        super.onDetachedFromWindow();
    }

    @Override 
    protected void onDraw(Canvas canvas) {  
        super.onDraw(canvas);
        if (needScroll)
            canvas.drawText(mText, mCoordinateX, getTextSize() + getPaddingTop(), getPaint());
    }  
   
    private Handler mHandler = new Handler() {  
        @Override 
        public void handleMessage(Message msg) {  
            switch (msg.what) {
            case 0:
                if (Math.abs(mCoordinateX) > (mTextWidth - getWidth())) {
                    mCoordinateX = 0;
                    mPaused = true;
                    if (mHandler.hasMessages(0))
                        mHandler.removeMessages(0);
                } else {
                    mCoordinateX -= 5;
                    invalidate();
                    if (!mStopMarquee) {
                        sendEmptyMessageDelayed(0, 15);
                    }
                }
                break;
            }
            super.handleMessage(msg);  
        }  
    };

    /**
     * 从暂停的地方继续移动
     * @param:
     * @return:void
     */
    public void resumeScroll() {
        if (!mPaused || mTextWidth <= getWidth()) {
            return;
        }
        if(mHandler != null) {
            mHandler.sendEmptyMessageDelayed(0, 100);
        }
        mPaused = false;
    }

    /**
     * 设置text和对齐方式
     * @param: text 文本内容
     * @return:void
     */
    public void setTextProperty(CharSequence text) {
        this.mText = text.toString();
        mTextWidth = getPaint().measureText(mText);
        if(mTextWidth > getWidth()) {
            needScroll = true;
            setGravity(Gravity.CENTER_VERTICAL);
            setText("");
        } else {
            needScroll = false;
            setGravity(Gravity.CENTER);
            setText(mText);
        }
    }

    /**
     * 获取文字的滚动周期
     * @param:
     * @return:滚动周期
     */
    public int getScrollCycle() {
        if(mTextWidth <=  getWidth()) { // 如果文字没有超出范围不轮播
            return 3000;
        }
        int distance = (int)(mTextWidth - getWidth());
        return (int)(distance * 1.0f / 5 * 15 * 1000) + 2000;
    }

}
