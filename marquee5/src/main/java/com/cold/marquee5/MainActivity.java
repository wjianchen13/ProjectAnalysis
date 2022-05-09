package com.cold.marquee5;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.cold.marquee5.other.SpannableUtil2;
import com.cold.marquee5.other.WSBaseBroatcastBean;
import com.cold.marquee5.other.WSChater;

import java.util.List;

import static com.cold.marquee5.R.id.tv_scroll;

public class MainActivity extends AppCompatActivity {

    protected FrameLayout fl_slideParent;
    private LiveSlidePlayer2 mLiveSlidePlayer;
    private FrameLayout flyt_parent;
    private int i;

    private List<DataBean> mList;

    private HorizontalScrollView mSv;

    private HorizontalScrollView hsvScroll;

    private FrameLayout flytHit;

//    private LinearLayout mLl;
    private TextView mLl;
    private SpannableStringBuilder showString;
    private TextView tvScroll;
    private TextView tv_test;
    private  TextView tv2;

    private WSBaseBroatcastBean sysBSBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        showString = new SpannableStringBuilder("");
        Bitmap bm1 = ((BitmapDrawable)getResources().getDrawable(R.drawable.ic_test)).getBitmap();

        SpannableString spanStr = new SpannableString("我说dsnfonsadifsadnfasodfnasdlfnoasndfonasdfno");
        showString.append(spanStr);

        SpannableString str =  new SpannableString("img ");
        System.out.println("====================> str len: " + str.length());
        ImageSpan imageSpan = new ImageSpanC(MainActivity.this, bm1, DynamicDrawableSpan.ALIGN_BASELINE);
        str.setSpan(imageSpan, 0, str.length() - 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//        mLl.setImgWidth(this, R.drawable.ic_test, dip2px(15));
        showString.append(str);

    }

    private SpannableStringBuilder getContent() {

        int resId = 0;

        resId = R.drawable.ic_test;

        SpannableStringBuilder showString = new SpannableStringBuilder("");
        SpannableString spanStr = new SpannableString("恭喜 <b>“金大羽晴”</b> 升级到abcdefghijkLaaaaaaaa");
        showString.append(spanStr + " ");


        if(resId != 0) {
            showString.append(SpannableUtil2.getScaleImageSpan(this, resId, dip2px(15)));
        }
        return showString;
    }

    /**
     * 初始化布局
     */
    private void initView() {
        fl_slideParent = (FrameLayout) this.findViewById(R.id.fl_slideParent);
        flyt_parent = (FrameLayout) this.findViewById(R.id.flyt_parent);

        hsvScroll =  (HorizontalScrollView) this.findViewById(R.id.hsv_scroll);
        flytHit =  (FrameLayout) this.findViewById(R.id.rlyt_hit);

        mSv = (HorizontalScrollView) findViewById(R.id.sv);
        mLl = (TextView) findViewById(R.id.ll);
        tvScroll = (TextView) findViewById(tv_scroll);
        tv_test = (TextView) findViewById(R.id.tv_test);

        mSv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                //设置ScrollView取消手动滑动
                return true;
            }
        });
    }

    /**
     * 开始滚动
     */
    private void startScroll(float scrollWidth) {
        System.out.println("========================> scrollWidth: " + scrollWidth);
        if (scrollWidth != 0) {
            final long duration = (long) (scrollWidth * 5);//动画时长
            //属性动画位移，从屏幕右边向左移动到屏幕外scrollWidth长度的距离
            ObjectAnimator anim = ObjectAnimator.ofFloat(tvScroll, "translationX", 0, -scrollWidth);
            anim.setStartDelay(1000);
            anim.setDuration(duration);
            anim.setInterpolator(new LinearInterpolator());
            anim.setRepeatMode(ValueAnimator.RESTART);//无限重复
            anim.setRepeatCount(0);
            anim.start();
        }
    }

    /**
     * 获得屏幕宽度
     */
    private int getScreenWidth() {
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    private void test() {
        sysBSBean = WSBaseBroatcastBean.getInformalVO(); // 这里只是用来测试抢头条成功，因为没有系统广播
        WSChater mSender = new WSChater();
        sysBSBean.setSender(mSender);
        WSChater mReciever = new WSChater();
        sysBSBean.setReciever(mReciever);
        sysBSBean.setMsgContent("年后你懂哈手动阀哈师大华发商都好可怜");
        String test1 = "恭喜 <b>“金大羽晴”</b> 升级到16 探花1111112";
        String text2 = "恭喜 <b>“金大羽晴”</b> 升级到16";
        String test3 = "恭喜 “꧁༺涛༒哥༻꧂” 成为 “骚骚莲骚骚” 的骑士";
//        i = 0;
        i = i % 10;
        if(i == 0) {
            sysBSBean.setBroatcastType(2);
            sysBSBean.setMsgContent(test1);
            i = i + 1;
        } else if(i == 1) {
            sysBSBean.setBroatcastType(8);
            sysBSBean.setMsgContent(text2);
            i = i + 1;
        } else if(i == 2) {
            sysBSBean.setBroatcastType(9);
            sysBSBean.setMsgContent(test3);
            i = i + 1;
        } else if(i == 3) {
            sysBSBean.setBroatcastType(10);
            sysBSBean.setMsgContent(text2);
            i = i + 1;
        } else if(i == 4) {
            sysBSBean.setBroatcastType(18);
            sysBSBean.setMsgContent(test1);
            i = i + 1;
        } else if(i == 5) {
            sysBSBean.setBroatcastType(2);
            sysBSBean.setMsgContent(test3);
            i = i + 1;
        } else if(i == 6) {
            sysBSBean.setBroatcastType(8);
            sysBSBean.setMsgContent(test1);
            i = i + 1;
        } else if(i == 7) {
            sysBSBean.setBroatcastType(9);
            sysBSBean.setMsgContent(text2);
            i = i + 1;
        } else if(i == 8) {
            sysBSBean.setBroatcastType(10);
            sysBSBean.setMsgContent(test3);
            i = i + 1;
        } else if(i == 9) {
            sysBSBean.setBroatcastType(18);
            sysBSBean.setMsgContent(text2);
            i = i + 1;
        }
        sysBSBean.setStatus(1);
        sysBSBean.setAcount(1);
        sysBSBean.setGift_name("现金");
        sysBSBean.setGold(100);
        sysBSBean.setNickname2("小红");
        sysBSBean.setShow(1);
        sysBSBean.setShowPriority(1);
        sysBSBean.setTool(1);
        sysBSBean.setTool_acount(2);
        sysBSBean.setTool_id(1001);
        sysBSBean.setUrlType(1);
        sysBSBean.setWebviewUrl("'");
        addBroadcast(sysBSBean);
    }

    private void addBroadcast(WSBaseBroatcastBean sysBSBean ) {
        if (sysBSBean != null && flyt_parent != null) {
            if (mLiveSlidePlayer == null) {
                mLiveSlidePlayer = new LiveSlidePlayer2(this, flyt_parent, flyt_parent.getWidth(), tv_test, tv2);
            }
//            getView2(this,flyt_parent,sysBSBean);
            mLiveSlidePlayer.addContentSpan(sysBSBean);
        }
    }

//    public void getView2(Context context, FrameLayout flyt_parent1, WSBaseBroatcastBean data) {
//        View v = null;
//        v = LayoutInflater.from(context).inflate(R.layout.view_live_chat_slide_item_test, null);
//        FrameLayout.LayoutParams lp2 = new FrameLayout.LayoutParams(dip2px(260), dip2px(70));
//        lp2.gravity = Gravity.CENTER | Gravity.BOTTOM;
//        lp2.bottomMargin = dip2px(100);
//        v.setLayoutParams(lp2);
//        TextView tv2 = (TextView)v.findViewById(R.id.tv_scroll);
//        tv2.setMaxLines(1);
//        flyt_parent1.addView(v);
//        v.setVisibility(View.VISIBLE);
//        tv2.setText(mLiveSlidePlayer.getContent(data));
//    }

    public void test(View v) {
        test();
    }

    public void test1(View v) {
        tv_test.setText(getContent());
    }

    public void test2(View v) {
        startScroll(100);
//        mLl.setText(getContent());
//        System.out.println("===============================> hsvScroll width: " + hsvScroll.getWidth());
//        System.out.println("===============================> flytHit width: " + flytHit.getWidth());
//        System.out.println("----------------------> paddingLeft: " + hsvScroll.getPaddingLeft());
//        System.out.println("----------------------> paddingRight: " + hsvScroll.getPaddingRight());
//
    }

    public void test3(View v) {
        tvScroll.setTranslationX(0);
    }

    private int getTextViewWidth() {
        tvScroll .setText(getContent());
        int spec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        tvScroll.measure(spec,spec);
        int measuredWidthTicketNum = tvScroll.getMeasuredWidth();
        return measuredWidthTicketNum;
    }

    public void test4(View v) {
        v = LayoutInflater.from(this).inflate(R.layout.view_live_chat_slide_item_test, null);
        FrameLayout.LayoutParams lp2 = new FrameLayout.LayoutParams(dip2px(260), dip2px(70));
        lp2.gravity = Gravity.CENTER | Gravity.BOTTOM;
        lp2.bottomMargin = dip2px(50);
        v.setLayoutParams(lp2);
        tv2 = (TextView)v.findViewById(tv_scroll);
        tv2.setMaxLines(1);
        flyt_parent.addView(v);
        v.setVisibility(View.VISIBLE);
        tv2.setText(mLiveSlidePlayer.getContent(sysBSBean));
//        tv2.setText(getContent());
    }

    public void test5(View v){
        mLl.setText("hello");
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dip2px(float dpValue) {
        return (int) (dpValue * getDensity(this) + 0.5f);
    }

    /**
     * 返回屏幕密度
     */
    public float getDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }
}
