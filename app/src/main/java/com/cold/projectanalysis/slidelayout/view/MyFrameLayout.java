package com.cold.projectanalysis.slidelayout.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.Toast;

/**
 * Created by JY113 on 2016/7/1.
 */
public class MyFrameLayout extends FrameLayout implements  android.view.GestureDetector.OnGestureListener{

    //定义手势检测器实例
    GestureDetector detector;
    private Context context = null;
    private ISlideView v = null;

    public MyFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        detector = new GestureDetector(context,this);
        this.context = context;
    }

    public MyFrameLayout(Context context, ISlideView v) {
        super(context);
        detector = new GestureDetector(context,this);
        this.context = context;
        this.v = v;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch(ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                System.out.println("-----------------------> MyFrameLayout dispatchTouchEvent ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                System.out.println("-----------------------> MyFrameLayout dispatchTouchEvent ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                System.out.println("-----------------------> MyFrameLayout dispatchTouchEvent ACTION_UP");
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch(ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                System.out.println("-----------------------> MyFrameLayout onInterceptTouchEvent ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                System.out.println("-----------------------> MyFrameLayout onInterceptTouchEvent ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                System.out.println("-----------------------> MyFrameLayout onInterceptTouchEvent ACTION_UP");
                break;
        }

        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return detector.onTouchEvent(event);
//        switch(event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                System.out.println("-----------------------> MyFrameLayout onTouchEvent ACTION_DOWN");
//                break;
//            case MotionEvent.ACTION_MOVE:
//                System.out.println("-----------------------> MyFrameLayout onTouchEvent ACTION_MOVE");
//                break;
//            case MotionEvent.ACTION_UP:
//                System.out.println("-----------------------> MyFrameLayout onTouchEvent ACTION_UP");
//                break;
//        }
//
//        return super.onTouchEvent(event);
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
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        float minMove = 120;         //最小滑动距离
        float minVelocity = 0;      //最小滑动速度
        float beginX = e1.getX();
        float endX = e2.getX();
        float beginY = e1.getY();
        float endY = e2.getY();

        if(beginX-endX>minMove&&Math.abs(velocityX)>minVelocity){   //左滑
            v.onShow();
            Toast.makeText(context,velocityX+"左滑",Toast.LENGTH_SHORT).show();
        }else if(endX-beginX>minMove&&Math.abs(velocityX)>minVelocity){   //右滑
            v.onDissmiss();
            Toast.makeText(context,velocityX+"右滑",Toast.LENGTH_SHORT).show();
        }else if(beginY-endY>minMove&&Math.abs(velocityY)>minVelocity){   //上滑
            Toast.makeText(context,velocityX+"上滑",Toast.LENGTH_SHORT).show();
        }else if(endY-beginY>minMove&&Math.abs(velocityY)>minVelocity){   //下滑
            Toast.makeText(context,velocityX+"下滑",Toast.LENGTH_SHORT).show();
        }

        return true;
    }

    @Override
    public void onShowPress(MotionEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onSingleTapUp(MotionEvent arg0) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public void onLongPress(MotionEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float velocityX,
                            float velocityY) {

        return true;
    }


}
