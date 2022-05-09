package com.cold.projectanalysis.slidelayout;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.cold.projectanalysis.R;
import com.cold.projectanalysis.slidelayout.view.ClearScreenHelper;
import com.cold.projectanalysis.slidelayout.view.IClearEvent;
import com.cold.projectanalysis.slidelayout.view.ISlideView;
import com.cold.projectanalysis.slidelayout.view.RelativeRootView;

public class SlideLayoutActivity extends FragmentActivity implements ISlideView {

    private TextView tvTest = null;
    private LinearLayout llyt_test1 = null;
    private TextView tvTest2 = null;

    private Button btnTest = null;

    /**
     * 清屏父布局
     */
    private RelativeRootView rl_helper1;
    private ClearScreenHelper mClearScreenHelper1;

    //定义手势检测器实例
    GestureDetector detector;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_slide_layout);

//        detector = new GestureDetector(this,this);
        llyt_test1 = (LinearLayout)findViewById(R.id.llyt_test1);
        tvTest2 = (TextView)findViewById(R.id.tv_test2);
        btnTest = (Button) findViewById(R.id.btn_test);
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tvTest2.getVisibility() == View.VISIBLE) {
                    TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                            0.0f, Animation.RELATIVE_TO_SELF, 1.0f,
                            Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                            0.0f);
                    mHiddenAction.setDuration(500);
                    tvTest2.startAnimation(mHiddenAction);
                    tvTest2.setVisibility(View.GONE);
                } else {
                    TranslateAnimation mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f,
                            Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                            0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
                    mShowAction.setDuration(500);
                    tvTest2.startAnimation(mShowAction);
                    tvTest2.setVisibility(View.VISIBLE);
                }
            }
        });


        rl_helper1 = (RelativeRootView) findViewById(R.id.rl_helper1);

        rl_helper1.setRoomActivity(this);
        mClearScreenHelper1 = new ClearScreenHelper(this, rl_helper1, this);
        mClearScreenHelper1.bind(llyt_test1);
        mClearScreenHelper1.setIClearEvent(new IClearEvent() {
            @Override
            public void onClearEnd() {

            }

            @Override
            public void onRecovery() {

            }
        });
    }

    @Override
    public void onShow() {
        TranslateAnimation mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mShowAction.setDuration(500);
        tvTest2.startAnimation(mShowAction);
        tvTest2.setVisibility(View.VISIBLE);
        rl_helper1.setCanIntercept(false);
    }

    @Override
    public void onDissmiss() {
        if(tvTest2.getVisibility() == View.VISIBLE) {
            TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                    0.0f, Animation.RELATIVE_TO_SELF, 1.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                    0.0f);
            mHiddenAction.setDuration(500);
            tvTest2.startAnimation(mHiddenAction);
            tvTest2.setVisibility(View.GONE);
            rl_helper1.setCanIntercept(true);
        }
    }

    //    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        System.out.println("-----------------------> slideLayoutActivity dispatchTouchEvent");
//        return super.dispatchTouchEvent(ev);
//    }
//
//    //将该activity上的触碰事件交给GestureDetector处理
//    public boolean onTouchEvent(MotionEvent me){
//        System.out.println("-----------------------> slideLayoutActivity onTouchEvent");
////        return true;
//        return detector.onTouchEvent(me);
//    }
//
//    @Override
//    public boolean onDown(MotionEvent arg0) {
//        return false;
//    }
//
//    /**
//     * 滑屏监测
//     *
//     */
//    @Override
//    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
//                           float velocityY) {
//        float minMove = 120;         //最小滑动距离
//        float minVelocity = 0;      //最小滑动速度
//        float beginX = e1.getX();
//        float endX = e2.getX();
//        float beginY = e1.getY();
//        float endY = e2.getY();
//
//        if(beginX-endX>minMove&&Math.abs(velocityX)>minVelocity){   //左滑

//        }else if(endX-beginX>minMove&&Math.abs(velocityX)>minVelocity){   //右滑

//        }else if(beginY-endY>minMove&&Math.abs(velocityY)>minVelocity){   //上滑
//            Toast.makeText(this,velocityX+"上滑",Toast.LENGTH_SHORT).show();
//        }else if(endY-beginY>minMove&&Math.abs(velocityY)>minVelocity){   //下滑
//            Toast.makeText(this,velocityX+"下滑",Toast.LENGTH_SHORT).show();
//        }
//
//        return false;
//    }
//
//    @Override
//    public void onShowPress(MotionEvent arg0) {
//        // TODO Auto-generated method stub
//
//    }
//
//    @Override
//    public boolean onSingleTapUp(MotionEvent arg0) {
//        // TODO Auto-generated method stub
//        return false;
//    }
//
//    @Override
//    public void onLongPress(MotionEvent arg0) {
//        // TODO Auto-generated method stub
//
//    }
//
//    @Override
//    public boolean onScroll(MotionEvent e1, MotionEvent e2, float velocityX,
//                            float velocityY) {
//
//        return false;
//    }
}
