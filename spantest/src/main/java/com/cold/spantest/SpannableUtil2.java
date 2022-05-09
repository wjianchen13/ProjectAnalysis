package com.cold.spantest;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;


public class SpannableUtil2 {

    public static SpannableString getColorSpanText(String txt, int colorId) {
        SpannableString spanStr = new SpannableString(txt);
        spanStr.setSpan(new ForegroundColorSpan(colorId), 0, spanStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanStr;
    }

    /**
     * 获取指定颜色，指定点击的字符串
     * @param
     * @return
     */
    public static SpannableString getColorClickSpanText(String txt, int colorId, int colorStart, int colorEnd, int clickColorId,
                                                        int clickStart, int clickEnd, final View.OnClickListener mOnClickListener, final Object clickTag) {
        SpannableString spanStr = new SpannableString(txt);
        spanStr.setSpan(new ForegroundColorSpan(colorId), colorStart, colorEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        NoLineClickSpan mNoLineClickSpan = new NoLineClickSpan(clickColorId, mOnClickListener) {
            @Override
            public void onClick(View widget) {
                widget.setTag(clickTag);
                super.onClick(widget);
            }
        };
        mNoLineClickSpan.setShowUnderLine(true);
        spanStr.setSpan(mNoLineClickSpan, clickStart, clickEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanStr;
    }
}
