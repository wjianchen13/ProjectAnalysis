package com.cold.homescreen;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

/**
 * name:
 * desc: home和屏幕打开关闭监听
 * author:
 * date: 2017-08-04 10:00
 * remark:
 * 170725 14:00 通过广播捕获系统home键和屏幕开关的事件
 */
public class MainActivity extends AppCompatActivity {

    private FrameLayout flytParent;
    private TextView tvTest;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.flytParent = (FrameLayout)findViewById(R.id.flyt_parent);
        tvTest = (TextView)findViewById(R.id.tv_test);
        //home键监听
        final IntentFilter homeFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        registerReceiver(homePressReceiver, homeFilter);

        //电源键监听
        final IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(mBatInfoReceiver, filter);

//        tv = new TextView(this);
//        System.out.println("-----------------------> attach window1: " + tv.isAttachedToWindow());
//
//        tv.setText("hello");
//        flytParent.addView(tv);


    }


    public void test(View v) {
//        System.out.println("-----------------------> attach window2: " + tv.isAttachedToWindow());
        tvTest.setText(getString(R.string.app_name));
    }

    private BroadcastReceiver homePressReceiver = new BroadcastReceiver() {
        final String SYSTEM_DIALOG_REASON_KEY = "reason";
        final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
                if(reason != null&& reason.equals(SYSTEM_DIALOG_REASON_HOME_KEY)) {
                    System.out.println("====================> home键监听");
                }
            }
        }
    };

    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            final String action = intent.getAction();
            if(Intent.ACTION_SCREEN_ON.equals(action)) {
                System.out.println("====================> 屏幕打开");
                System.out.println("====================> ACTION_SCREEN_ON background: " + isBackground(MainActivity.this));
            } else if(Intent.ACTION_SCREEN_OFF.equals(action)) {
                System.out.println("====================> ACTION_SCREEN_OFF background: " + isBackground(MainActivity.this));
                System.out.println("====================> 屏幕关闭");
            }
        }
    };

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("====================> onStop background: " + isBackground(this));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(homePressReceiver != null) {
            unregisterReceiver(homePressReceiver);
            homePressReceiver = null;
        }
//        if(mBatInfoReceiver != null) {
//            unregisterReceiver(mBatInfoReceiver);
//            mBatInfoReceiver = null;
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("====================> onPause ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("====================> onResume background: " + isBackground(this));
    }

    public boolean isBackground(Context context) {
        try {
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<RunningTaskInfo> taskInfos = activityManager.getRunningTasks(1);
            if (taskInfos != null && taskInfos.size() > 0 && !taskInfos.get(0).topActivity.getPackageName().equals(context.getPackageName()))
                return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
