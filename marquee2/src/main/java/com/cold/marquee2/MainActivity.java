package com.cold.marquee2;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import com.cold.marquee2.other.WSBaseBroatcastBean;
import com.cold.marquee2.other.WSChater;


public class MainActivity extends Activity {
    private ScrollTextView test;
    private ScrollTextView test1;
    protected FrameLayout fl_slideParent;
    private LiveSlidePlayer2 mLiveSlidePlayer;
    private FrameLayout flyt_parent;
    private int i;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        test = (ScrollTextView) this.findViewById(R.id.test);
        test1 = (ScrollTextView) this.findViewById(R.id.test1);
        test1.setText("测试123456哈哈哈啊啊啊啊啊啊啊啊大是大非大沙发是的发送到饭店撒旦");

        fl_slideParent = (FrameLayout) this.findViewById(R.id.fl_slideParent);
        flyt_parent = (FrameLayout) this.findViewById(R.id.flyt_parent);
        SpannableStringBuilder showString = new SpannableStringBuilder("");
        Bitmap bm1 = ((BitmapDrawable)getResources().getDrawable(R.drawable.ic_test)).getBitmap();

        SpannableString spanStr = new SpannableString("我说dsnfonsadifsadnfasodfnasdlfnoasndfonasdfno");
        showString.append(spanStr);

        SpannableString str =  new SpannableString("img ");
        System.out.println("====================> str len: " + str.length());
        ImageSpan imageSpan = new ImageSpanC(MainActivity.this, bm1, DynamicDrawableSpan.ALIGN_BASELINE);
        str.setSpan(imageSpan, 0, str.length() - 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        showString.append(str);
        test.setText(showString);
    }

    private void test() {
        WSBaseBroatcastBean sysBSBean = WSBaseBroatcastBean.getInformalVO(); // 这里只是用来测试抢头条成功，因为没有系统广播
        WSChater mSender = new WSChater();
        sysBSBean.setSender(mSender);
        WSChater mReciever = new WSChater();
        sysBSBean.setReciever(mReciever);
        sysBSBean.setMsgContent("年后你懂哈手动阀哈师大华发商都好可怜");
        String test1 = "恭喜 <b>“金大羽晴”</b> 升级到14 探花11111";
//        String test1 = "恭喜 “꧁༺涛༒哥༻꧂” 成为 “骚骚莲骚骚” 的骑士";
        i = i % 5;
        if(i == 0) {
            sysBSBean.setBroatcastType(2);
            sysBSBean.setMsgContent(test1);
            i = i + 1;
        } else if(i == 1) {
            sysBSBean.setBroatcastType(8);
            sysBSBean.setMsgContent(test1);
            i = i + 1;
        } else if(i == 2) {
            sysBSBean.setBroatcastType(9);
            sysBSBean.setMsgContent(test1);
            i = i + 1;
        } else if(i == 3) {
            sysBSBean.setBroatcastType(10);
            sysBSBean.setMsgContent(test1);
            i = i + 1;
        } else if(i == 4) {
            sysBSBean.setBroatcastType(18);
            sysBSBean.setMsgContent(test1);
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
        if (sysBSBean != null && fl_slideParent != null) {
            if (mLiveSlidePlayer == null) {
                mLiveSlidePlayer = new LiveSlidePlayer2(MainActivity.this, fl_slideParent, flyt_parent.getWidth());
            }
            mLiveSlidePlayer.addContentSpan(sysBSBean);
        }
    }

    public void test(View v) {
        test();
    }

    public void test1(View v) {

    }

    public void test2(View v) {

    }

    public void test3(View v) {

    }

    public void stop(View v) {

    }

    public void startFor0(View v){
        test.setGravity(Gravity.CENTER_VERTICAL);
        SpannableStringBuilder showString = new SpannableStringBuilder("");
        Bitmap bm1 = ((BitmapDrawable)getResources().getDrawable(R.drawable.ic_test)).getBitmap();
        SpannableString spanStr = new SpannableString("我说dsnfonsadifsadnfasodfnasdlfnoasndfonasdfno");
        showString.append(spanStr);

        SpannableString str =  new SpannableString("img ");
        System.out.println("====================> str len: " + str.length());
        ImageSpan imageSpan = new ImageSpanC(MainActivity.this, bm1, DynamicDrawableSpan.ALIGN_BASELINE);
        str.setSpan(imageSpan, 0, str.length() - 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        showString.append(str);
        test.setText(showString);
        test.resumeScroll();
    }
}