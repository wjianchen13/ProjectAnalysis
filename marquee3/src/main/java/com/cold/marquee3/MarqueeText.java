package com.cold.marquee3;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * Created by Administrator on 2017/7/31.
 */
public class MarqueeText extends AppCompatTextView implements Runnable {

    private int currentScrollX = 0;// 初始滚动的位置

    private float textWidth;
    private int speed = 2;
    private int delayed = 50;

    /**
     * 暂停标志
     */
    private boolean mPaused = true;

    /**
     * 显示等级图标的宽度
     */
    private int imgWidth;

    public MarqueeText(Context context) {
        super(context);
        setSingleLine();
        setEllipsize(null);

    }

    public MarqueeText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setSingleLine();
        setEllipsize(null);
    }

    public MarqueeText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setSingleLine();
        setEllipsize(null);
    }

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
        if (!mPaused || textWidth <= getWidth()) {
            return;
        }
        this.removeCallbacks(this);
        postDelayed(this, 300);
        mPaused = false;
    }

    /**
     * 设置text和对齐方式
     * @param: text 文本内容
     * @return:void
     */
    public void setTextAndGravity(CharSequence text) {
        textWidth = getTextWidth(text);
        if(textWidth  > getWidth()) {
            setGravity(Gravity.CENTER_VERTICAL);
            setText(text);
        } else {
            setGravity(Gravity.CENTER);
            setText(text);
        }
    }

    /**
     * 获取文字的滚动周期
     * @param:
     * @return:滚动周期
     */
    public int getScrollCycle() {
        if(textWidth <=  getWidth()) { // 如果文字没有超出范围不轮播
            return 3000;
        }
        int distance = (int)(textWidth - getWidth());
        return (int)(distance * 1.0f / 5 * 15 * 1000) + 2000;
    }

    //每次滚动几点
    public void setSpeed(int sp){
        speed = sp;
    }

    //滚动间隔时间,毫秒
    public void setDelayed(int delay){
        delayed = delay;
    }

    /**
     * 获取文字宽度
     */
    private float getTextWidth(CharSequence text) {
        Paint paint = getPaint();
        String str = text.toString();
        return paint.measureText(str) + imgWidth;
    }

    @Override
    public void run() {
        if (Math.abs(currentScrollX) < (textWidth - getWidth())) {
            currentScrollX += speed;// 滚动速度,每次滚动几点
            scrollTo(currentScrollX, 0);
            postDelayed(this, delayed);
        } else {
            currentScrollX = 0;
            mPaused = true;
            removeCallbacks(this);
        }
    }

    // 开始滚动
    public void startScroll() {

    }

    // 停止滚动
    public void stopScroll() {

    }

    // 从头开始滚动
    public void startFor0() {
        currentScrollX = 0;
        startScroll();
    }
}