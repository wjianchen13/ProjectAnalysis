package com.cold.marquee3;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * name:ScrollTextView
 * desc:实现文字滚动功能
 * author:
 * date: 2017-07-14 19:47
 * remark:
 * 170731 17:20 使用另外一种方式实现文字滚动，原来的方式好像有坑
 */
public class ScrollTextView extends AppCompatTextView {

    private boolean mStopMarquee;
    private CharSequence mText;
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

    /**
     * 显示等级图标的宽度
     */
    private int imgWidth;


    public ScrollTextView(Context context, AttributeSet attrs) {
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
            canvas.drawText(mText, 0, mText.length() - 1, mCoordinateX, getTextSize() + getPaddingTop(), getPaint());
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (Math.abs(mCoordinateX) > (mTextWidth + imgWidth - getWidth())) {
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
     * 设置图片的宽度
     * @param imgWidth
     * @return
     */
    public void setImgWidth(int imgWidth) {
        this.imgWidth = imgWidth;
    }

    /**
     * 设置显示图片的宽度
     * @param: context 获取图片
     * @param: spanImgIds 图片id
     * @param: height 图片显示高度 px
     * @return:void
     */
    public void setImgWidth(Context context, int spanImgIds, int height) {
        if (spanImgIds != 0){
            try {
                Drawable img = context.getResources().getDrawable(spanImgIds);
                imgWidth = (int) (img.getIntrinsicWidth() * (float) height / img.getIntrinsicHeight());
            } catch (Resources.NotFoundException e) {

            }
        }
    }

    /**
     * 从暂停的地方继续移动
     * @param:
     * @return:void
     */
    public void resumeScroll() {
        if (!mPaused || mTextWidth + imgWidth <= getWidth()) {
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
        this.mText = text;
        mTextWidth = getPaint().measureText(mText.toString());
        if(mTextWidth + imgWidth > getWidth()) {
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
        if(mTextWidth + imgWidth<=  getWidth()) { // 如果文字没有超出范围不轮播
            return 3000;
        }
        int distance = (int)(mTextWidth + imgWidth - getWidth());
        return (int)(distance * 1.0f / 5 * 15 * 1000) + 2000;
    }

}