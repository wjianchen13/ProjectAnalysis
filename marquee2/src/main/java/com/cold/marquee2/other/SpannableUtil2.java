package com.cold.marquee2.other;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

import com.cold.marquee2.ImageSpanC;


public class SpannableUtil2 {

    /**
     * 按照比例获取设定大小的image span
     *
     * @param context
     * @param
     * @param height 图片绘制高度 px
     * @param spanImgIds
     */
    public static SpannableString getScaleImageSpan(Context context, int spanImgIds, int height) {
        SpannableString spanStr = null;
        if (spanImgIds > 0 && context != null) {
            Drawable img = context.getResources().getDrawable(spanImgIds);
            int intrinsicWidth = img.getIntrinsicWidth(); // 获取图片的宽度 px
            int intrinsicHeight = img.getIntrinsicHeight(); // 获取图片的高度 px
            float factor = (float)height / intrinsicHeight; // 转换因子
            img.setBounds(0, 0, (int)(intrinsicWidth * factor), height);
            ImageSpan imgSpan = createImageSpan(context, img);
            spanStr = new SpannableString("img ");
            spanStr.setSpan(imgSpan, 0, spanStr.length() - 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        return spanStr;
    }


    public static ImageSpan createImageSpan(Context context, Drawable drawable) {
        return createImageSpan(context, drawable, 1);
    }

    public static ImageSpan createImageSpan(Context context, Drawable drawable, int v) {
        return new ImageSpanC(context, drawable, v);
    }


}