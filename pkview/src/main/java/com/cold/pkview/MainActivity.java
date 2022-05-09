package com.cold.pkview;

import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.cold.pkview.old.LivePkBean;
import com.cold.pkview.old.LivePkInfo;
import com.cold.pkview.old.LivePkView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private Button btnTest = null;
    private LivePkView pkView1 = null;
    private Button btnTest1 = null;
    private Button btnTest2 = null;
    private com.cold.pkview.mine.LivePkView pkView2 = null;
    int i = 1;
    private Handler mHandler;
    private Runnable mTimeRunnable; //倒计时器
    private int totalExp = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pkView1 = (LivePkView)findViewById(R.id.live_pk);
        btnTest = (Button) findViewById(R.id.btn_test);
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pkView1.addPkFire(getInfo());
            }
        });

        pkView2 = (com.cold.pkview.mine.LivePkView)findViewById(R.id.pk_mine);
        btnTest1 = (Button) findViewById(R.id.btn_test1);
        btnTest1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
            }
        });
//        initData();
        btnTest2 = (Button) findViewById(R.id.btn_test2);
        btnTest2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pkView2.addPkFire(getInfo2());
            }
        });
    }

    private LivePkInfo getInfo() {
        LivePkInfo livePkInfo = new LivePkInfo();
        LivePkBean mAnchorLeft = new LivePkBean(1, 100 * i, "", "hello");
        LivePkBean mAnchorRight = new LivePkBean(2, 100 * i, "", "right");
        livePkInfo.setAnchorLeft(mAnchorLeft);
        livePkInfo.setAnchorRight(mAnchorRight);
        livePkInfo.setTotalExp(10000);
        livePkInfo.setLimitTime(5000);
        livePkInfo.setmWinerId(1);
        i ++;
        return livePkInfo;
    }

    private com.cold.pkview.mine.LivePkInfo getInfo1() {

        com.cold.pkview.mine.LivePkInfo livePkInfo = new com.cold.pkview.mine.LivePkInfo();
        com.cold.pkview.mine.LivePkBean mAnchorLeft = new com.cold.pkview.mine.LivePkBean(1, 100 * getInt() + pkView2.getTopExp(), "", "left");
        livePkInfo.setStatus(1);
        if(mAnchorLeft.getCurrentExp() >= totalExp) {
            livePkInfo.setStatus(2);
            livePkInfo.setmWinerId(1);
            mAnchorLeft.setCurrentExp(totalExp);
        }
        com.cold.pkview.mine.LivePkBean mAnchorRight = new com.cold.pkview.mine.LivePkBean(2, 100 * getInt() +  pkView2.getBottomExp(), "", "right");
        if(mAnchorRight.getCurrentExp() >= totalExp) {
            livePkInfo.setStatus(2);
            livePkInfo.setmWinerId(2);
            mAnchorRight.setCurrentExp(totalExp);
        }
        livePkInfo.setAnchorLeft(mAnchorLeft);
        livePkInfo.setAnchorRight(mAnchorRight);
        livePkInfo.setTotalExp(totalExp);
        livePkInfo.setLimitTime(500);
//        livePkInfo.setStatus(1);
        return livePkInfo;
    }

    private com.cold.pkview.mine.LivePkInfo getInfo2() {

        com.cold.pkview.mine.LivePkInfo livePkInfo = new com.cold.pkview.mine.LivePkInfo();
        com.cold.pkview.mine.LivePkBean mAnchorLeft = new com.cold.pkview.mine.LivePkBean(1, 100 * getInt() + pkView2.getTopExp(), "", "left");
        livePkInfo.setStatus(3);
        livePkInfo.setmWinerId(1);
        mAnchorLeft.setCurrentExp(totalExp);

        com.cold.pkview.mine.LivePkBean mAnchorRight = new com.cold.pkview.mine.LivePkBean(2, 100 * getInt() +  pkView2.getBottomExp(), "", "right");
        livePkInfo.setStatus(3);
        livePkInfo.setmWinerId(2);
        mAnchorRight.setCurrentExp(totalExp);
        livePkInfo.setAnchorLeft(mAnchorLeft);
        livePkInfo.setAnchorRight(mAnchorRight);
        livePkInfo.setTotalExp(totalExp);
        livePkInfo.setLimitTime(500);
//        livePkInfo.setStatus(1);
        return livePkInfo;
    }

    private void initData() {
        if((pkView2.getTopExp() < totalExp) && (pkView2.getBottomExp() < totalExp)) {
            pkView2.addPkFire(getInfo1());
        }
    }

    private Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {

        }
    });

    private int getInt() {
        Random rand=new Random();
        int i = rand.nextInt(3) + 1;
        return i;
    }
}
