package com.cold.projectanalysis.paintorview.paintor;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Administrator on 2016-09-12.
 */
public interface IPaint {
    DrawItem getDrawItem();
    void draw(Canvas canvas, Paint paint, Object frame);
    void add(Object obj);
    void updateSize(int width, int height);
    boolean onTouch(View view, MotionEvent motionEvent);
    void defineMessage(int what, Object obj);
    void clear();
}
