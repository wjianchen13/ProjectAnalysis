package com.cold.projectanalysis.paintorview.bubble;

/**
 * Created by Administrator on 2016-07-15.
 */
public class PhiveUtil extends LiveUtil{
    //直播类型
    public static final int LIVE_MODE_PC = 1;
    public static final int LIVE_MODE_PHONE = 2;

    //进场弹幕最低财富等级要求（包含）
    public static final int PHIVE_DANMU_GOROOM_LEVEL = 16;

    /******************************************************************************
     *
     * enum
     *
     *****************************************************************************/
    //手机直播间，事件发起者
    public  enum PhiveAnimEnum {
        PAE_INPUT,			//弹出输入框
        PAE_GIFT,			//弹出礼物
        PAE_INPUT_GIFT,   //输入中的礼物
        PAE_INPUT_KEYBOARD_HIDE,   //输入中的键盘隐藏
        PAE_AUDIENCE,		//观众列表
        PAE_RANGKING,		//排行榜
        PAE_MENU,			//菜单
        PAE_VIDEO,			//在非清屏状态下，点击视频区。
        LAE_AUD_INFO,		//观众信息
        LAE_CLEAR_IN,		//清屏隐藏
        LAE_CLEAR_VI,		//清屏显示
        LAE_CLEAR_VI_EXCLUDE_MENU,		//清屏显示，但是不包括底部菜单，只要用于输入聊天左边的礼物点击
        LAE_KEY_BACK,      //按下返回键
        LAE_NULL,
        LAE_OPEN_ACTIVITE, //活动版块
        LAE_GIFT_HIDE,     //礼物面板隐藏自己后发出，主要用于对聊天框的隐藏显示
        LAE_GIFT_SHOW,      //礼物面板显示出来，主要对于聊天框的隐藏显示
        LAE_OPEN_KNIGHT,   //开通骑士
        LAE_LOGIN,         //登录
        LAE_MESSAGE,         //触摸聊天区，主要用于隐藏键盘
        LAE_BEAUTY,          //美颜等级
        LAE_SHARE,          //分享
        LAE_HORIZONTAL_SCROLL //横向滚动
    }

}
