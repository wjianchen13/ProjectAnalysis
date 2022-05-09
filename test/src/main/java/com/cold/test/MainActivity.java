package com.cold.test;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    /**
     * 游戏
     */
    public static final int ID_GAME_EGG = 1;
    public static final int ID_GAME_BUY = 2;
    public static final int ID_GAME_MAGIC = 3;
    public static final int ID_GAME_ANIMAL = 4;

    private int gameItemState = 0;

    private XiuWeakHandler mHandler = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("-----------------> ID_GAME_EGG state: " + getGameItemState(ID_GAME_EGG));
        System.out.println("-----------------> ID_GAME_BUY state: " + getGameItemState(ID_GAME_BUY));
        System.out.println("-----------------> ID_GAME_MAGIC state: " + getGameItemState(ID_GAME_MAGIC));
        System.out.println("-----------------> ID_GAME_ANIMAL state: " + getGameItemState(ID_GAME_ANIMAL));
        setGameItemState(ID_GAME_EGG, true);
        setGameItemState(ID_GAME_BUY, true);
        setGameItemState(ID_GAME_MAGIC, true);
        setGameItemState(ID_GAME_ANIMAL, true);
        System.out.println("-----------------> ID_GAME_EGG state2: " + getGameItemState(ID_GAME_EGG));
        System.out.println("-----------------> ID_GAME_BUY state2: " + getGameItemState(ID_GAME_BUY));
        System.out.println("-----------------> ID_GAME_MAGIC state2: " + getGameItemState(ID_GAME_MAGIC));
        System.out.println("-----------------> ID_GAME_ANIMAL state2: " + getGameItemState(ID_GAME_ANIMAL));
        setGameItemState(ID_GAME_EGG, false);
        setGameItemState(ID_GAME_BUY, false);
        setGameItemState(ID_GAME_MAGIC, false);
        setGameItemState(ID_GAME_ANIMAL, false);
        System.out.println("-----------------> ID_GAME_EGG state3: " + getGameItemState(ID_GAME_EGG));
        System.out.println("-----------------> ID_GAME_BUY state3: " + getGameItemState(ID_GAME_BUY));
        System.out.println("-----------------> ID_GAME_MAGIC state3: " + getGameItemState(ID_GAME_MAGIC));
        System.out.println("-----------------> ID_GAME_ANIMAL state3: " + getGameItemState(ID_GAME_ANIMAL));
//        mHandler = new XiuWeakHandler();
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        getResponseCode("http://blog.csdn.net/qq282330332/article/details/77763110");
//                    }
//                }).start();
//
//                mHandler.postDelayed(this, 2000);
//            }
//        }, 2000);
    }

    private int getResponseCode(String url) {
        try {
            URL getUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) getUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(5000);
            connection.setConnectTimeout(5000);
            return connection.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 获取游戏菜单的显示状态
     * @param resId
     * ID_GAME_EGG：砸蛋
     * ID_GAME_BUY：幸运夺宝
     * ID_GAME_MAGIC：魔法学院
     * ID_GAME_ANIMAL：四大神兽
     * @return 显示状态
     */
    private int getGameItemState(int resId) {
        if(resId == ID_GAME_EGG) {
            return (gameItemState & 1) >> 0;
        } else if(resId == ID_GAME_BUY) {
            return (gameItemState & 2) >> 1;
        } else if(resId == ID_GAME_MAGIC) {
            return (gameItemState & 4) >> 2;
        } else if(resId == ID_GAME_ANIMAL) {
            return (gameItemState & 8) >> 3;
        }
        return 0;
    }

    /**
     * 设置游戏菜单的显示状态
     * @param resId
     * @param show 显示状态 0：不显示  1：显示
     * @return
     */
    private void setGameItemState(int resId, boolean show) {
        if(resId == ID_GAME_EGG) {
            gameItemState = (show ? (gameItemState |= (1 << 0)) : (gameItemState &= ~(1 << 0)));
        } else if(resId == ID_GAME_BUY) {
            gameItemState = (show ? (gameItemState |= (1 << 1)) : (gameItemState &= ~(1 << 1)));
        } else if(resId == ID_GAME_MAGIC) {
            gameItemState = (show ? (gameItemState |= (1 << 2)) : (gameItemState &= ~(1 << 2)));
        } else if(resId == ID_GAME_ANIMAL) {
            gameItemState = (show ? (gameItemState |= (1 << 3)) : (gameItemState &= ~(1 << 3)));
        }
    }

}
