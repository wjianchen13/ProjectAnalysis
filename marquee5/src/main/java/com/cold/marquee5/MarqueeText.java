package com.cold.marquee5;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * name:MarqueeText
 * desc:实现文字滚动功能
 * author:
 * date: 2017-08-01 10:30
 * remark:
 */
public class MarqueeText extends AppCompatTextView {

    /**
     * 显示等级图标的宽度
     */
    private int imgWidth;

    public MarqueeText(Context context) {
        super(context);
//        setSingleLine();
//        setEllipsize(null);

    }

    public MarqueeText(Context context, AttributeSet attrs) {
        super(context, attrs);
//        setSingleLine();
//        setEllipsize(null);
    }

    public MarqueeText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
//        setSingleLine();
//        setEllipsize(null);
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
     * 获取文字宽度
     * @param
     * @return
     */
    public float getTextWidth() {
        Paint paint = getPaint();
        String str = getText().toString();
        System.out.println("========================> getWidth(): " + getWidth());
        return paint.measureText(str) + imgWidth;
    }

    /**
     * 获取文字宽度
     * @param
     * @return
     */
    public float getTextWidth(CharSequence c) {
        Paint paint = getPaint();
        String str = c.toString();
        System.out.println("========================> getWidth(): " + getWidth());
        return paint.measureText(str) + imgWidth;
    }

}