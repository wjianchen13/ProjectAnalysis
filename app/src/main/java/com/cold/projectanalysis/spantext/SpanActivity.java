package com.cold.projectanalysis.spantext;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.widget.TextView;

import com.cold.projectanalysis.R;

import static android.text.style.DynamicDrawableSpan.ALIGN_BOTTOM;

public class SpanActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView textView=new TextView(this);
        setContentView(textView);
        SpannableStringBuilder showString = new SpannableStringBuilder("1我们533333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333336");
        ImageSpan imageSpan=new ImageSpan(this, R.drawable.ic_test,ALIGN_BOTTOM);
        ImageSpan imageSpan2=new ImageSpan(this, R.drawable.ic_test1,ALIGN_BOTTOM);
        ImageSpan imageSpan21=new ImageSpan(this, R.drawable.ic_test2,ALIGN_BOTTOM);
        showString.setSpan(imageSpan, 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        showString.setSpan(imageSpan2, 27, 30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        showString.setSpan(imageSpan21, 45, 46, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

//        DynamicDrawableSpan drawableSpan = mine DynamicDrawableSpan(DynamicDrawableSpan.ALIGN_BOTTOM) {
//            @Override
//            public Drawable getDrawable() {
//                Drawable d = getResources().getDrawable(R.drawable.ic_test);
//                d.setBounds(0, 0, 300, 50);
//                return d;
//            }
//        };
//        DynamicDrawableSpan drawableSpan2 = mine DynamicDrawableSpan(DynamicDrawableSpan.ALIGN_BOTTOM) {
//            @Override
//            public Drawable getDrawable() {
//                Drawable d = getResources().getDrawable(R.drawable.ic_test);
//                d.setBounds(0, 0, 300, 50);
//                return d;
//            }
//        };
//        showString.setSpan(drawableSpan, 3, 4, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//        showString.setSpan(drawableSpan2, 17, 18, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

        textView.setText(showString);


    }

    public class MyIm extends ImageSpan
    {
        public MyIm(Context arg0,int arg1) {
            super(arg0, arg1);
        }
//        public int getSize(Paint paint, CharSequence text, int start, int end,
//                           FontMetricsInt fm) {
//            Drawable d = getDrawable();
//            Rect rect = d.getBounds();
//            if (fm != null) {
//                FontMetricsInt fmPaint=paint.getFontMetricsInt();
//                int fontHeight = fmPaint.bottom - fmPaint.top;
//                int drHeight=rect.bottom-rect.top;
//
//                int top= drHeight/2 - fontHeight/4;
//                int bottom=drHeight/2 + fontHeight/4;
//
//                fm.ascent=-bottom;
//                fm.top=-bottom;
//                fm.bottom=top;
//                fm.descent=top;
//            }
//            return rect.right;
//        }
//
//        @Override
//        public void draw(Canvas canvas, CharSequence text, int start, int end,
//                         float x, int top, int y, int bottom, Paint paint) {
//            Drawable b = getDrawable();
//            canvas.save();
//            int transY = 0;
//            transY = ((bottom-top) - b.getBounds().bottom)/2+top;
//            canvas.translate(x, transY);
//            b.draw(canvas);
//            canvas.restore();
//        }
    }
}