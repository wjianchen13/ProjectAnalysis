package com.cold.marquee;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.animation.LinearInterpolator;
import android.widget.Scroller;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * name:ScrollTextView
 * desc:实现文字滚动功能
 * author:
 * date: 2017-07-14 19:47
 * remark:
 * 170724 18:30 如果发现图标边界不移动，或者部分item不显示，有可能是边界问题，设置了gravity就可能会出现不显示
 * 170725 14:00 有时候出现不显示文字的情况，由于在设置Textview的属性时，设置了尺寸，尺寸没有那么快获取的到，在接下来
 *              的根据尺寸相关的逻辑处理就会有问题。因为不一样的逻辑设置了Gravity和reset都会导致不显示，下列字符串不
 *              显示：需要全部替换，代码已经更改过，在动画启动时用post，如果还有问题，试一下添加一个延时
 *              test1 = "恭喜 <b>“金大羽晴”</b> 升级到14 探花"
 * 170726 14:30 在LiveSlidePlayer2动画消失的时候不要把控件的状态设置成GONE，需设置成INVISIBLE，这样在post拿的尺寸是正常的
 */
public class ScrollTextView extends AppCompatTextView {

    /**
    * scroller
    */
    private Scroller mScroller = new Scroller(this.getContext(), new LinearInterpolator()); // 线性移动;

    /**
     * 停止时的x坐标偏移
     */
    private int mPausedX = 0;

    /**
     * 暂停标志
     */
    private boolean mPaused = true;

    /**
     * 滚动速率，px/ms
     */
    private float scrollSpeed = 0.10f;

    /**
     * 显示等级图标的宽度
     */
    private int imgWidth;

    public ScrollTextView(Context context) {
        this(context, null);
        setSingleLine();
        setEllipsize(null);
        setVisibility(VISIBLE);
    }

    public ScrollTextView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.textViewStyle);
        setSingleLine();
        setEllipsize(null);
        setVisibility(VISIBLE);
    }

    public ScrollTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setSingleLine();
        setEllipsize(null);
        setVisibility(VISIBLE);

    }

     /**
      * 从最右侧开始移动文字
      * @param:
      * @return: void
      */
    public void startScroll() {
        mPausedX = -1 * getWidth(); // 从最右侧开始移动
        mPaused = true;
        resumeScroll();
    }

    /**
     * 暂停移动
     * @param:
     * @return: void
     */
    public void pauseScroll() {
        if (null == mScroller)
            return;
        if (mPaused)
            return;
        mPaused = true;
        mPausedX = mScroller.getCurrX(); // 记录暂停位置
        mScroller.abortAnimation(); // 停止动画
    }

    public void finishScroller() {
        if(mScroller != null && !mScroller.isFinished()) {
            mScroller.abortAnimation();
        }
    }

    public boolean getScrollerState() {
        if(mScroller != null) {
            return mScroller.isFinished();
        }
        return false;
    }

    /**
     * 从暂停的地方继续移动
     * @param:
     * @return:void
     */
    public void resumeScroll() {
        if (!mPaused) {
            return;
        }
        int scrollingLen = calculateScrollingLen();
        if((scrollingLen - getWidth()) <=  getWidth()) { // 如果文字没有超出范围不轮播
            return;
        }
        setHorizontallyScrolling(true); // 如果放在构造函数中有时会不移动
        setScroller(mScroller);
        int distance = scrollingLen - (2 * getWidth() + mPausedX); // 计算移动的记录，富文本
        int time = (int)(distance * 1.0f / scrollSpeed);
        mScroller.startScroll(mPausedX, 0, distance, 0, time);
//        mScroller.
        invalidate();
        mPaused = false;
    }

    /**
     * 获取文字的滚动周期
     * @param:
     * @return:滚动周期
     */
    public int getScrollCycle() {
        int scrollingLen = calculateScrollingLen();
        if((scrollingLen - getWidth()) <=  getWidth()) { // 如果文字没有超出范围不轮播
            return 3000;
        }
        int distance = scrollingLen - (2 * getWidth() + mPausedX);
        return (int)(distance * 1.0f / scrollSpeed) + 2000;
    }

    /**
     * 判断是否需要滚动
     * @param:
     * @return:true 需要滚动，否则不需要
     */
    public boolean isNeedScroll() {
        int scrollingLen = calculateScrollingLen();
        return (scrollingLen - getWidth()) > getWidth();
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
     * 设置text和对齐方式
     * @param: text 文本内容
     * @return:void
     */
    public void setTextAndGravity(CharSequence text) {
        TextPaint tp = getPaint();
        Rect rect = new Rect();
        String strTxt = text.toString();
        tp.getTextBounds(strTxt, 0, strTxt.length(), rect);
        int scrollingLen = rect.width();
        if(scrollingLen > getWidth()) {
            setGravity(Gravity.CENTER_VERTICAL);
        } else {
            setGravity(Gravity.CENTER);
        }
        setText(text);
    }

    /**
     * 设置text和对齐方式
     * @param: text 文本内容
     * @return:void
     */
    public void setTextProperty(CharSequence text) {
        TextPaint tp = getPaint();
        Rect rect = new Rect();
        String strTxt = text.toString();
        tp.getTextBounds(strTxt, 0, strTxt.length(), rect);
        int scrollingLen = rect.width() + imgWidth;
        System.out.println("---------------------------> strTxt: " + strTxt);
        System.out.println("---------------------------> rect.width(): " + rect.width());
        System.out.println("---------------------------> scrollingLen: " + scrollingLen);
        System.out.println("---------------------------> getWidth 1: " + getMeasuredWidth());
        if(scrollingLen > getWidth()) {
            setGravity(Gravity.CENTER_VERTICAL);
//            reset();
        } else {
            setGravity(Gravity.CENTER);
        }
        System.out.println("-------------------------> getWidth 2: " + getMeasuredWidth());
        setText(text);
    }

    /**
     * 计算需要移动的文字长度 px
     * @param:
     * @return: 文字长度
     */
    private int calculateScrollingLen() {
        TextPaint tp = getPaint();
        Rect rect = new Rect();
        String strTxt = getText().toString();
        tp.getTextBounds(strTxt, 0, strTxt.length(), rect);
        int scrollingLen = rect.width() + getWidth() + imgWidth;
        rect = null;
        return scrollingLen;
    }

    /**
     * 监听结束状态
     * @param:
     * @return:
     */
    @Override
    public void computeScroll() {
        super.computeScroll();
        if (null == mScroller){
            return;
        }
        if (mScroller.isFinished() && (!mPaused)) {
            mPaused = true;
        }
    }

    /**
     * 初始化字体显示位置
     * @param:
     * @return: void
     */
    public void reset() {
        if(mScroller != null) {
            mScroller.setFinalX(0);
            invalidate();
        }
    }

    public int getMyX() {
        if(mScroller != null) {
            return mScroller.getFinalX();
        }
        return 0;
    }

    public void setX() {
        mScroller.setFinalX(-100);
        invalidate();
    }

}