
package com.cold.spantest;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import androidx.core.content.ContextCompat;

import java.lang.ref.WeakReference;

public class NoLineClickSpan extends ClickableSpan {
    private int mcolorId;
    private int mcolor;
    private Context mContext;
    private boolean isShowUnderLine = false;
    private WeakReference<View.OnClickListener> wrOnClickListener;

    public NoLineClickSpan(Context context, int colorId, View.OnClickListener listener) {
        super();
        wrOnClickListener = new WeakReference<>(listener);
        mContext = context.getApplicationContext();
        mcolorId = colorId;
    }

    public NoLineClickSpan(int color, View.OnClickListener listener) {
        super();
        wrOnClickListener = new WeakReference<>(listener);
        mcolor = color;
    }

    @Override
    public void onClick(View widget) {
        if(wrOnClickListener != null) {
            View.OnClickListener listener = wrOnClickListener.get();
            if (listener != null)
                listener.onClick(widget);
        }
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        if (mContext == null)
            ds.setColor(mcolor);
        else {
            try {
                ds.setColor(ContextCompat.getColor(mContext, mcolorId));
            } catch (Resources.NotFoundException e) {
                e.printStackTrace();
            }
        }

        ds.setUnderlineText(isShowUnderLine);

    }

    public boolean isShowUnderLine() {
        return isShowUnderLine;
    }

    public void setShowUnderLine(boolean isShowUnderLine) {
        this.isShowUnderLine = isShowUnderLine;
    }

}
