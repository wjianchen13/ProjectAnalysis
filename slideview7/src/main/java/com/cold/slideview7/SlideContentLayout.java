package com.cold.slideview7;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

/**
 * name: MyFrameLayout
 * desc: 底层布局，接收手势并处理
 * author:
 * date: 2017-06-16 15:10
 * remark:
 */
public class SlideContentLayout extends RelativeLayout implements  GestureDetector.OnGestureListener {

    private Button control;
    GestureDetector detector;
    ISlide islide;
    private Context context;

    private View myParent;

    public Button getControl() {
        return control;
    }

    public void setControl(Button control) {
        this.control = control;
    }

    public void setParent(View parent) {
        this.myParent = parent;
    }

    public SlideContentLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        detector = new GestureDetector(context,this);
    }

    public SlideContentLayout(Context context) {
        super(context);
        detector = new GestureDetector(context,this);
    }

    public void setIslide(ISlide islide) {
        this.islide = islide;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        control.onTouchEvent(event);
        return detector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent arg0) {
        return true;
    }

    /**
     * 滑屏监测
     *
     */
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        float minMove = 120;         //最小滑动距离
        float minVelocity = 0;      //最小滑动速度
        float beginX = e1.getX();
        float endX = e2.getX();

        if(beginX-endX>minMove && Math.abs(velocityX)>minVelocity){   //左滑
            if(islide != null) {
                islide.showSlideView();
            }
        }else if(endX-beginX>minMove&&Math.abs(velocityX) > minVelocity){   //右滑
            if(islide != null) {
                islide.closeSlideView();
            }
        }
        return false;
    }

    @Override
    public void onShowPress(MotionEvent arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean onSingleTapUp(MotionEvent arg0) {
        if(islide != null) {
            islide.closeSlideView();
        }
//        if(control != null) {
//            control.callOnClick();
//        }
//        super.onTouchEvent(arg0);
        return false;
    }

    @Override
    public void onLongPress(MotionEvent arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }
}