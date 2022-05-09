package com.cold.homescreen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/8/4.
 */
public class ScreenBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {
        Toast.makeText(context, "receive", Toast.LENGTH_SHORT).show();
        final String action = intent.getAction();
        System.out.println("====================> 屏幕打开111");
        if(Intent.ACTION_SCREEN_ON.equals(action)) {
            System.out.println("====================> 屏幕打开");
        } else if(Intent.ACTION_SCREEN_OFF.equals(action)) {
            System.out.println("====================> 屏幕关闭");
        }
    }

}
