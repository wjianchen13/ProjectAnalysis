package com.cold.projectanalysis.slidelayout.view;

import android.app.Activity;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import java.util.LinkedList;


public class ClearScreenHelper implements View.OnTouchListener {

    private IClearRootView mScreenSideView;

    private LinkedList<View> mClearList;

    private IClearEvent mIClearEvent;
    private FrameLayout mImgV;

//    private PhiveVedio mPhiveVedio;

    @Deprecated
    public ClearScreenHelper(Context context) {
        this(context, null, null);
    }

    /**
     * Recomment
     *
     * @param context
     * @param rootView
     */
    public ClearScreenHelper(Context context, IClearRootView rootView, ISlideView v) {
        initView(context, rootView, v);
        initPara();
        initCallback();
    }

    private void initView(Context context, IClearRootView root, ISlideView v) {
        if (root == null) {
            ViewGroup decorView = (ViewGroup) ((Activity) context).getWindow().getDecorView();
            final ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mScreenSideView = new ScreenSideView(context);
            decorView.addView((View) mScreenSideView, params);
        } else {
            mScreenSideView = root;
            mImgV = new MyFrameLayout(context, v);
            mImgV.setOnTouchListener(this);
            mImgV.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            mImgV.setClickable(true);
            root.addView(mImgV, 0);

        }
    }

    public void setViewClick(boolean isClick) {
        mImgV.setClickable(isClick);
    }

    private void initPara() {
        mClearList = new LinkedList<>();
        setOrientation(Constants.RIGHT);
    }

    private void initCallback() {
        mScreenSideView.setIPositionCallBack(new IPositionCallBack() {
            @Override
            public void onPositionChange(int offsetX, int offsetY) {
                for (int i = 0; i < mClearList.size(); i++) {
                    mClearList.get(i).setTranslationX(offsetX);
                    mClearList.get(i).setTranslationY(offsetY);
                }
            }
        });

        mScreenSideView.setIClearEvent(new IClearEvent() {
            @Override
            public void onClearEnd() {
                if (mIClearEvent != null) {
                    mIClearEvent.onClearEnd();
                }
            }

            @Override
            public void onRecovery() {
                if (mIClearEvent != null) {
                    mIClearEvent.onRecovery();
                }
            }
        });
    }

    public void setIClearEvent(IClearEvent l) {
        mIClearEvent = l;
    }

    public void setOrientation(@Constants.Orientation int orientation) {
        mScreenSideView.setClearSide(orientation);
    }

    public void bind(@NonNull View... cellList) {
        for (View cell : cellList) {
            if (!mClearList.contains(cell)) {
                mClearList.add(cell);
            }
        }
    }

    public void unbind(@NonNull View... cellList) {
        for (View cell : cellList) {
            if (mClearList.contains(cell)) {
                mClearList.remove(cell);
            }
        }
    }

    public void unbindAllCell() {
        mClearList.clear();
    }

//    public void setPhiveVedio(PhiveVedio phiveVedio) {
//        mPhiveVedio = phiveVedio;
//    }


    private float firstDownX = 0;
    private float lastDownX = 0;
    private float firstDownY = 0;
    private float lastDownY = 0;
//    private float SIZERANGE = ApplicationUtilV2.dip2px(10);
    private float SIZERANGE = 10;
    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                firstDownX = event.getX();
                firstDownY = event.getY();

                lastDownX = event.getX();
                lastDownY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                lastDownX = event.getX();
                lastDownY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                float deltaX = Math.abs(lastDownX - firstDownX);
                float deltaY = Math.abs(lastDownY - firstDownY);
                if (deltaX < SIZERANGE && deltaY < SIZERANGE) {
                    //触发点击事件
//                    if (mPhiveVedio != null) {
//                        mPhiveVedio.onTouch((RelativeRootView) mScreenSideView, event);
//                    }
                }
                break;
        }


        return false;
    }
}
