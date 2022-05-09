package com.cold.spantest;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 */
public class LiveMessageView extends TextView {


    private ArrayList drawables;

    public LiveMessageView(Context context) {
        super(context);
    }

    public LiveMessageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LiveMessageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setTopMargin(int topMargin) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) getLayoutParams();
        params.topMargin = topMargin;
        setLayoutParams(params);

    }

  /*  {
        drawables = mine ArrayList();
    }


    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        if (text instanceof Spannable) {
            ImageSpan[] is = ((Spannable) text).getSpans(0, text.length(), ImageSpan.class);
            if (is.length == 0) {
                for (int i = 0; i < drawables.size(); i++) {
                    WebpDrawable webpDrawable = (WebpDrawable) drawables.get(i);
                    webpDrawable.recycle();
                }
                drawables.clear();

            } else {

                for (int i = 0; i < is.length; i++) {
                    ImageSpan imageSpan = is[i];
                    if (imageSpan instanceof ImageSpanC) {
                        Drawable drawable = ((ImageSpanC) imageSpan).getCachedDrawable();
                        if (drawable instanceof WebpDrawable) {
                            drawable.setCallback(this);
                        }
                    }
                }

            }

        }

    }
*/
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

/*    @Override
    protected boolean verifyDrawable(@NonNull Drawable who) {
        super.verifyDrawable(who);
        return true;
    }

    @Override
    public void invalidateDrawable(@NonNull Drawable drawable) {
        super.invalidateDrawable(drawable);
    }*/
}
