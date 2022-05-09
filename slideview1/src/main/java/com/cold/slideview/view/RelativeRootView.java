package com.cold.slideview.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

import java.util.LinkedList;

public class RelativeRootView extends RelativeLayout implements View.OnTouchListener, ISlideView {

//    private IClearRootView mScreenSideView;
    private Context context;
    private LinkedList<View> mClearList;

//    private IClearEvent mIClearEvent;
    private MyFrameLayout mImgV;

//    private final int MIN_SCROLL_SIZE = ApplicationUtilV2.dip2px(66);
    private final int MIN_SCROLL_SIZE = 66;
    private final int LEFT_SIDE_X = 0;
    private final int RIGHT_SIDE_X = getResources().getDisplayMetrics().widthPixels;

    private int mDownX;
    private int mEndX;
    private ValueAnimator mEndAnimator;

    private boolean isCanSrcoll;

    @Constants.Orientation
    private int mOrientation;

    private boolean isCanIntercept = true;

    private IPositionCallBack mIPositionCallBack;
    private IClearEvent mIClearEvent;

    private boolean isAnimating = false;
    private View mainView = null;
    private MyLinearLayout slideView = null;


    public void setIPositionCallBack(IPositionCallBack l) {
        mIPositionCallBack = l;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }


    public void setIClearEvent(IClearEvent l) {
        mIClearEvent = l;
    }

    public RelativeRootView(Context context) {
        this(context, null);
    }

    public RelativeRootView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RelativeRootView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        initView(context);
        initPara();
//        initCallback();
        mEndAnimator = ValueAnimator.ofFloat(0, 1.0f).setDuration(400);
        mEndAnimator.setInterpolator(new DecelerateInterpolator());
        mEndAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                isAnimating = true;

                float factor = (float) valueAnimator.getAnimatedValue();
                int diffX = mEndX - mDownX;
                mIPositionCallBack.onPositionChange((int) (mDownX + diffX * factor), 0);

            }
        });
        mEndAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) { // 动画结束
                isAnimating = false;
                if (mOrientation == (Constants.RIGHT) && mEndX == RIGHT_SIDE_X) { // 向右移动，清除布局
                    if(mIClearEvent != null) {
                        mIClearEvent.onClearEnd();
                    }
                    mOrientation = Constants.LEFT;
                    mDownX = 0;
                } else if (mOrientation == (Constants.LEFT) && mEndX == LEFT_SIDE_X) { // 向左移动，恢复布局
                    if(mIClearEvent != null) {
                        mIClearEvent.onRecovery();
                    }
                    mOrientation = Constants.RIGHT;
                    mDownX = RIGHT_SIDE_X;
                } else if (mOrientation == (Constants.RIGHT) && mEndX != RIGHT_SIDE_X) { // 向左移动，恢复布局
                    if(mIClearEvent != null) {
                        mIClearEvent.onRecovery();
                    }
                    mDownX = RIGHT_SIDE_X;
                } else if (mOrientation == (Constants.LEFT) && mEndX != LEFT_SIDE_X) {  // 向右移动，清除布局
                    if(mIClearEvent != null) {
                        mIClearEvent.onClearEnd();
                    }
                    mDownX = 0;
                }
                //               mDownX = mEndX;
                isCanSrcoll = false;
            }

        });
    }

    @Override
    public void onShow() {
        if(slideView.getVisibility() == View.GONE) {
            TranslateAnimation mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                    0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
            mShowAction.setDuration(300);
            slideView.startAnimation(mShowAction);
            slideView.setVisibility(View.VISIBLE);
            setCanIntercept(false);
        }
    }

    @Override
    public void onDissmiss() {
        if(slideView.getVisibility() == View.VISIBLE) {
            TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                    0.0f, Animation.RELATIVE_TO_SELF, 1.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                    0.0f);
            mHiddenAction.setDuration(300);
            slideView.startAnimation(mHiddenAction);
            slideView.setVisibility(View.GONE);
            setCanIntercept(true);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() != 2) {
            throw new IllegalArgumentException("this view only contains 2 children! ");
        } else {
            mainView = getChildAt(0);
            slideView = (MyLinearLayout)getChildAt(1);
            slideView.setV(this);
            if(mainView == null) {
                throw new IllegalArgumentException("main view is null ");
            }
            if(slideView == null) {
                throw new IllegalArgumentException("slide view is null ");
            }
//            slideView.setClickable(true);
            ISlideView v = null;
            mImgV = new MyFrameLayout(context,  v);
            mImgV.setOnTouchListener(this);
            mImgV.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            mImgV.setClickable(true);
            addView(mImgV, 0);
            setOrientation(Constants.RIGHT);
            setSlideListener(this);
            initCallback();
            bind(mainView);

        }
    }

    public void setSlideListener(ISlideView v) {
        if(mImgV != null) {
            mImgV.setV(v);
        }
    }

    private void initView(Context context) {
//        if (root == null) {
//            ViewGroup decorView = (ViewGroup) ((Activity) context).getWindow().getDecorView();
//            final ViewGroup.LayoutParams params = mine ViewGroup.LayoutParams(
//                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//            mScreenSideView = mine ScreenSideView(context);
//            decorView.addView((View) mScreenSideView, params);
//        } else {
//            mScreenSideView = root;
//            ISlideView v = null;
//            mImgV = mine MyFrameLayout(context,  v);
//            mImgV.setOnTouchListener(this);
//            mImgV.setLayoutParams(mine LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//            mImgV.setClickable(true);
//            root.addView(mImgV, 0);
//
//        }
    }

    private void initPara() {
        mClearList = new LinkedList<>();

    }

    public void setOrientation(@Constants.Orientation int orientation) {
        setClearSide(orientation);
    }


    private void initCallback() {
        setIPositionCallBack(new IPositionCallBack() {
            @Override
            public void onPositionChange(int offsetX, int offsetY) {
                for (int i = 0; i < mClearList.size(); i++) {
                    mClearList.get(i).setTranslationX(offsetX);
                    mClearList.get(i).setTranslationY(offsetY);
                }
            }
        });
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

    public boolean isCanIntercept() {
        return isCanIntercept;
    }

    public void setCanIntercept(boolean canIntercept) {
        isCanIntercept = canIntercept;
    }

    public void setClearSide(@Constants.Orientation int orientation) {
        mOrientation = orientation;
    }

    private int mLastX = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        System.out.println("RelativieRootView onTouchEvent");
        int index = event.getActionIndex();
        if (index != 0)
            return false;
        int pointerId = event.getPointerId(index);
        if (pointerId != 0)
            return false;
        int x = (int) event.getX();
        int offsetX = x - mDownX;
        System.out.println("RelativieRootView onTouchEvent 2");
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                if (mLastX > 0) {
                    x = mLastX;
                    offsetX = x - mDownX;

                }
                if (isGreaterThanMinSize(mDownX, x) && isCanSrcoll && !isAnimating) {
                    mIPositionCallBack.onPositionChange(getPositionChangeX(offsetX), 0);
                    return true;
                }
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                // 手指数量发生变化，更新lastXY为追踪手指
                // 根据手指ID生成流程图，在该事件到来时索引0对应的手指一定存在，且ID最小
                mLastX = (int) event.getX(0);
                break;

            case MotionEvent.ACTION_POINTER_UP:
                // 定义一个记录当前松开手指Index的变量
                int indexOfUpPointer = event.getActionIndex();
                // 如果松开的手指刚好是追踪手指
                if (indexOfUpPointer == 0) {
                    // !!!核心!!!：更新lastXY为接下来将被追踪的手指的当前坐标
                    mLastX = (int) event.getX(0);
                }
                startAnimation(offsetX, x);
                break;
            case MotionEvent.ACTION_UP:
                startAnimation(offsetX, x);
        }
        return super.onTouchEvent(event);
    }

    private void startAnimation(int offsetX, int x) {
        //isGreaterThanMinSize(mDownX, x) &&
        if (isCanSrcoll && !isAnimating) {
            if (mLastX > 0) {
                offsetX = mLastX - mDownX;
            }
            mDownX = getPositionChangeX(offsetX);
            fixPostion(offsetX);

            isAnimating = true;
            mEndAnimator.cancel();
            mEndAnimator.start();
            mLastX = 0;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        final int x = (int) event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!isAnimating) {
                    mDownX = x;
                }
                return false;

            case MotionEvent.ACTION_MOVE:
                if (isGreaterThanMinSize(mDownX, x) && isCanIntercept) {
                    isCanSrcoll = true;
                    return true;
                }
        }
        return super.onInterceptTouchEvent(event);
    }

    private int getPositionChangeX(int offsetX) {
        int absOffsetX = Math.abs(offsetX);
        if (mOrientation == (Constants.RIGHT)) {
            return absOffsetX - MIN_SCROLL_SIZE > 0 ? absOffsetX - MIN_SCROLL_SIZE : 0;
        } else {
            return RIGHT_SIDE_X - (absOffsetX - MIN_SCROLL_SIZE);
        }
    }

    private void fixPostion(int offsetX) {
        int absOffsetX = Math.abs(offsetX);
        if (mOrientation == (Constants.RIGHT) && absOffsetX > RIGHT_SIDE_X / 3) {  // 显示，并且滑动距离大于1/3屏幕宽
            mEndX = RIGHT_SIDE_X;
        } else if (mOrientation == (Constants.LEFT) && (absOffsetX > RIGHT_SIDE_X / 3)) { // 隐藏，并且滑动距离大于1/3屏幕宽
            mEndX = LEFT_SIDE_X;
        } else if (mOrientation == (Constants.RIGHT) && absOffsetX <= RIGHT_SIDE_X / 3) { // 显示，并且滑动距离大于1/3屏幕宽
            mEndX = LEFT_SIDE_X;
        } else if (mOrientation == (Constants.LEFT) && (absOffsetX <= RIGHT_SIDE_X / 3)) { // 隐藏，并且滑动距离大于1/3屏幕宽
            mEndX = RIGHT_SIDE_X;
        }
    }

    public boolean isGreaterThanMinSize(int x1, int x2) {
//        System.out.println("---------------------> x1: " + x1);
//        System.out.println("---------------------> x2: " + x2);
        if (mOrientation == (Constants.RIGHT)) {
            return x2 - x1 > MIN_SCROLL_SIZE;
        } else {
            return x1 - x2 > MIN_SCROLL_SIZE;
        }
    }

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

    class MyFrameLayout extends FrameLayout implements  GestureDetector.OnGestureListener{

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

        public ISlideView getV() {
            return v;
        }

        public void setV(ISlideView v) {
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
                if(v != null) {
                    v.onShow();
                }
//                Toast.makeText(context,velocityX+"左滑",Toast.LENGTH_SHORT).show();
            }else if(endX-beginX>minMove&&Math.abs(velocityX)>minVelocity){   //右滑
                if(v != null) {
                    v.onDissmiss();
                }
//                Toast.makeText(context,velocityX+"右滑",Toast.LENGTH_SHORT).show();
            }else if(beginY-endY>minMove&&Math.abs(velocityY)>minVelocity){   //上滑
//                Toast.makeText(context,velocityX+"上滑",Toast.LENGTH_SHORT).show();
            }else if(endY-beginY>minMove&&Math.abs(velocityY)>minVelocity){   //下滑
//                Toast.makeText(context,velocityX+"下滑",Toast.LENGTH_SHORT).show();
            }

            return true;
        }

        @Override
        public void onShowPress(MotionEvent arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public boolean onSingleTapUp(MotionEvent arg0) {
            if(v != null) {
                v.onDissmiss();
            }
//            Toast.makeText(context, "onSingleTapUp", Toast.LENGTH_SHORT).show();
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

}
