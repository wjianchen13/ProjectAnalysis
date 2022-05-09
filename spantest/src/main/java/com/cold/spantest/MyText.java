package com.cold.spantest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import androidx.appcompat.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * name:
 * desc:
 * author:
 * date: 2017-06-19 18:34
 * remark:
 */
public class MyText extends AppCompatTextView {

    private Rect rect = new Rect();
    private float pam[] = new float[16];

    public MyText(Context context) {
        super(context);
    }

    public MyText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        String text = "我们的爱abcdefghijklmnopqrstu";
        Paint textPaint = new Paint( Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize( 55);
        textPaint.setColor( Color.BLACK);
        Paint paint = new Paint( );
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(55);
        paint.getTextBounds(text,0, text.length(), rect);
        paint.setColor(Color.RED);

        // 计算每一个坐标
        float baseX = 0;
        float baseY = 100;

        canvas.save();  //
        pam[0] = rect.left;
        pam[1] = rect.top+100;
        pam[2] = rect.right;
        pam[3] = rect.top+100;

        pam[4] = rect.right;
        pam[5] = rect.top+100;
        pam[6] = rect.right;
        pam[7] = rect.bottom +100;

        pam[8] = rect.right;
        pam[9] = rect.bottom +100;
        pam[10] = rect.left;
        pam[11] = rect.bottom + 100;

        pam[12] = rect.left;
        pam[13] = rect.bottom + 100;
        pam[14] = rect.left;
        pam[15] = rect.top+100;

        canvas.drawLines(pam,  paint);
        Paint p = new Paint();
        p.setColor(Color.RED);
        p.setStyle(Paint.Style.FILL);

        // 绘制文本
        canvas.drawText(text, baseX, baseY, textPaint);
        canvas.restore();

    }
}
