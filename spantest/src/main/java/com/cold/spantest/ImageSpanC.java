package com.cold.spantest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.style.ImageSpan;

import java.lang.ref.SoftReference;

/**
 * name: ImageSpanC
 * desc: 实现文字图片底部对齐
 * author:
 * date: 2017-06-20 19:30
 * remark:
 */
public class ImageSpanC extends ImageSpan {

    private Rect rect = new Rect();

    public ImageSpanC(Context context, Bitmap b, int verticalAlignment) {
        super(context, b, verticalAlignment);
    }

    public ImageSpanC(Context context, Drawable b, int verticalAlignment) {
        super(b, verticalAlignment);
    }

    public ImageSpanC(Drawable d, int verticalAlignment) {
        super(d, verticalAlignment);
    }

    public Drawable getCachedDrawable() {
        SoftReference<Drawable> wr = mDrawableRef;
        Drawable d = null;
        if (wr != null)
            d = wr.get();
        if (d == null) {
            d = getDrawable();
            mDrawableRef = new SoftReference<Drawable>(d);
        }
        return d;
    }

    private SoftReference<Drawable> mDrawableRef;

    /**
     * 返回绘制图片的宽度
     * @param: text 绘制的文本
     * @param: start
     * @param: end
     * @param: fm 字体信息，可以用来调节行高度
     * @return: 需要替换绘制图片的宽度
     */
    public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        Drawable d = getDrawable();
        Rect rect = d.getBounds();
        if (fm != null) {
            Paint.FontMetricsInt fmPaint = paint.getFontMetricsInt();
            int fontHeight = fmPaint.bottom - fmPaint.top;  //字体高度
            int drHeight = rect.bottom - rect.top; //图片高度
            int top = drHeight / 2 - fontHeight / 4;
            int bottom = drHeight / 2 + fontHeight / 3;

            fm.ascent = -bottom; // 字体的高度 descent - leading
            fm.top = -bottom;
            fm.bottom = top;
            fm.descent = top;


        }
        return rect.right;
    }

    /**
     * 绘制image
     * @param canvas
     * @param text 当前绘制的文本
     * @param start span开始的字符下标
     * @param end span结束的字符下标
     * @param x 要绘制的image的左边到textview左边的距离
     * @param top 替换行的最顶部位置
     * @param y 替换文字的基线
     * @param bottom 替换行的最底部位置
     * @param paint 画笔引用
     */
    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        paint.getTextBounds("我说",0, 1, rect);

        Drawable b = getCachedDrawable();
        int transY = 0;

        Paint.FontMetrics fm = paint.getFontMetrics();

        transY = y - b.getBounds().bottom + rect.bottom -  b.getBounds().top;
        transY = (int) ((y + fm.descent + y + fm.ascent) / 2 - b.getBounds().bottom / 2);
        canvas.save();
        canvas.translate(x, transY);
        b.draw(canvas);
        canvas.restore();
    }
}



/**
 *
 //        transY = y - b.getBounds().bottom + rect.bottom -  b.getBounds().top; // 图片高度减去文字底部得到最后图片和文字底部的偏差距离，再平移这个距离
 //        int temp = ((int)fm.descent - b.getBounds().top) / 2; // 消除图片顶部和文字底部的偏差
 //        transY = (int) ((y + fm.descent - 4)- b.getBounds().bottom ); // 最开始代码，可以实现部分机型底部对齐，但是适配性不好
 //        transY = (int) ((y + fm.descent + y + fm.ascent) / 2 - b.getBounds().bottom / 2); // 居中对齐

 // 重新设置textview一行的高度，可以达到字体平移效果，如果不需要考虑最终效果的居中，则不需要设置这里
 //            fm.ascent = -bottom - fontHeight * 2 / 3; //字体的高度 descent - leading
 //            fm.top = -bottom - fontHeight * 2 / 3;  //
 //            fm.bottom = top - fontHeight * 3 / 4;
 //            fm.descent = top - fontHeight * 3 / 4;


 Paint p = new Paint();
 p.setColor(Color.RED);
 p.setStyle(Paint.Style.FILL);
 canvas.drawLine(0, fm.ascent, 200, fm.ascent, p);
 //        canvas.drawLine(0, y, 200, y, p);
 canvas.drawLine(0, fm.descent, 200, fm.descent, p);
 p.setColor(Color.BLUE);
 canvas.drawLine(0, fm.top, 200, fm.top, p);
 //        canvas.drawLine(0, fm.bottom, 200, fm.bottom, p);
 canvas.drawLine(0, fm.bottom, 200, fm.bottom, p);
 canvas.drawLine(100, rect.bottom, 300, rect.bottom, p);
 canvas.drawRect(b.getBounds(), p);
 *
 */