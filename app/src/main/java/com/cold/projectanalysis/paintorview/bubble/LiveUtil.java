package com.cold.projectanalysis.paintorview.bubble;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

public class LiveUtil {
	/*****************************************************************************************
	 * 
	 * 直播间通讯端口
	 * 
	 ****************************************************************************************/


	/*****************************************************************************************
	 * 
	 * 直播间通信类型
	 * 
	 ****************************************************************************************/
	public static final int WEB_SOCK_DISCONNECT_DISABLE = -20;	//链接断开不再重连

	public static final int WEB_SOCK_STAR_REAL_COIN = -24;	// 获得真实的秀币和积分
	public static final int WEB_SOCK_FRUIT_GAME_BOUNDED = -23;	//超限额
	public static final int WEB_SOCK_FRUIT_GAME_EXCHARGE = -19;  // 水果积分兑换成秀币
	public static final int WEB_SOCK_FRUIT_GAME_TOP_FIVE = -18;  // 水果机前 5
	public static final int WEB_SOCK_FRUIT_GAME_AUDIENCE = -17;   // 水果机观众榜
	public static final int WEB_SOCK_FRUIT_GAME_WIN_CREDITS = -16; // 水果机赢积分
	public static final int WEB_SOCK_FRUIT_GAME_BET = -15; //  水果机下注
	public static final int WEB_SOCK_FRUIT_GAME_REPEAT_BET = -14; // 水果机重复上一次下注
	public static final int WEB_SOCK_FRUIT_GAME_INIT = -13;    // 水果机初始化
	public static final int WBE_SOCK_FRUIT_GAME_GAME_RESULT = -12;   // 水果机开奖结果
	public static final int WEB_SOCK_FRUIT_GAME_GAME_STATUS = -11;  // 水果机状态
	
	public static final int WEB_SOCK_GUESS_GAME_DISPLAY = -10;
	public static final int WEB_SOCK_GUESS_GAME_COINS = -9;
	public static final int WEB_SOCK_GUESS_GAME_RETURN = -8;
	public static final int WEB_SOCK_MSG_RESPOND = -7;
	public static final int WEB_SOCK_MSG_START = -6;
	public static final int WEB_SOCK_ANCHOR_PREVIOUS_LEVEXP = -5; 
	public static final int WEB_SOCK_OFFICER_DEFAULT = -4;
	public static final int WEB_SOCK_ANCHOR_FREE_GIFT_INIT = -3;
	public static final int WEB_SOCK_OPEN = -2;
	public static final int WEB_SOCK_DISCONNECT = -1;
	public static final int WEB_SOCK_USER_STATE = 0;
	public static final int WEB_SOCK_NOLOGIN_GO_ROOM = 1;
	public static final int WEB_SOCK_LOGIN_GO_ROOM = 2;
	public static final int WEB_SOCK_GIFT = 3;  // 送礼
	public static final int WEB_SOCK_CHAT = 4;
	public static final int WEB_SOCK_KICK = 5;
	public static final int WEB_SOCK_GAG = 6;  //禁言
	public static final int WEB_SOCK_CANCEL_GAG = 7; //解除禁言
	public static final int WEB_SOCK_GRAP_SEAT = 8;
	public static final int WEB_SOCK_SET_GUARD = 9;   //任命管理员
	public static final int WEB_SOCK_CANCEL_GUARD = 10; //	取消管理员
	public static final int WEB_SOCK_SYS_BROATCAST = 11;
	public static final int WEB_SOCK_NORMAL_BROATCAST = 12;
	public static final int WEB_SOCK_AUDIENCE = 13;
	public static final int WEB_SOCK_SUN_LIGHT = 14;
	public static final int WEB_SOCK_USER_ADD_SUN_LIGHT = 15;
	public static final int WEB_SOCK_GOODS_BUY_STATE = 16;
	public static final int WEB_SOCK_GIFT_RANK_USER_IDS = 17;    // 送礼前三名用户ids
	public static final int WEB_SOCK_ANCHOR_UPGRADE = 19;        // 主播升级通知
	public static final int WEB_SOCK_ANCHOR_LIVING_END = 21;        // 主播结束直播

	public static final int WEB_SOCK_AUDIENCE_CUR = 22;//现场观众
	public static final int WEB_SOCK_FANS_CHART = 23 ; // 粉丝榜单
	public static final int FIRST_COMELIVINGROOM_MSG = 24;//首次进入直播间显示10条聊天信息和送礼信息
	public static final int CHAT_USER_LOWEST_LEVEL = 25;//主播设置用户最低发言等级；
	public static final int WEB_SOCK_ANCHOR_FREE_GIFT_RECEIVE = 26;//免费礼物
	public static final int WEB_SOCK_FREE_GIFT_CHECK = 27;//查询免费礼物接口
	public static final int WEB_SOCK_BACKPKG_GIFT = 28;//背包礼物
	//public static final int WEB_SOCK_RED_PACK_STATUS = 29;//最近在发红包的主播直播间
	//public static final int WEB_SOCK_RED_PACK_RESULT = 30;//抢红包结果
	public static final int WEB_SOCK_ALL_ROOM_GIFT_ANIM = 31;//全网礼物动画
	public static final int WEB_SOCK_GROUP_CHARTS = 32;//帮会冠名
	public static final int WEB_SOCK_UPDATE_COIN = 33;//直播间请求秀币更新
	public static final int WEB_SOCK_GROUP_COLLECT= 34;//帮会集结令
	public static final int WEB_SOCK_GROUP_COLLECT_ANIMATE= 35;//帮会集结成功动画
	//public static final int WEB_SOCK_ROD_RED_PACK_STATUS= 36;//点击抢红包时间后返回服务器接收结果
	public static final int WEB_SOCK_HIT_EGG = 37;//砸蛋结果
	public static final int WEB_SOCK_HIT_EGG_SWITCH = 38;//砸蛋开关
	public static final int WEB_SOCK_UPDATE_INFO = 39;//通知用户更新用户信息
	public static final int WEB_SOCK_UPDATE_BACK_PACK = 40;//更新背包
	public static final int WEB_SOCK_RECOMMEND_ANCHORS = 41;//推荐主播
	public static final int WEB_SOCK_HEADLINES_GIFT = 42;     // 头条礼物信息
	public static final int WEB_SOCK_LIVE_ANCHOR_PK = 43;     // 直播间PK

	public static final int WEB_SOCK_RED_PACK_STATUS_NEW = 44;//最近在发红包的主播直播间
	public static final int WEB_SOCK_RED_PACK_RESULT_NEW = 45;//抢红包结果
	public static final int WEB_SOCK_ROD_RED_PACK_STATUS_NEW = 46;//点击抢红包时间后返回服务器接收结果
	public static final int WEB_SOCK_LIVE_BUY_GUARD = 47;//直播间购买守护
	public static final int WEB_SOCK_LIVE_KNIGHT_LIST = 48;//直播骑士列表

	public static final int WEB_SOCK_ANCHOR_CDN_CHANGE = 50;//主播cdn线路改变
	public static final int WEB_SOCK_CANDY_STATE = 51;
	public static final int WEB_SOCK_CANDY_RESULT = 52;
	public static final int WEB_SOCK_TOOLS_INFO = 53;//用户的道具信息
	public static final int WEB_SOCK_ANCHOR_LINE = 54;		//直播方式改变, live_mode
	public static final int WEB_SOCK_ANCHOR_LINE_STATUS = 55 ;  //主播直播状态
	public static final int WEB_SOCK_PRAISE = 56;		//点赞消息
	public static final int WEB_SOCK_BUBBLE_LV = 57;		//冒泡档位
	public static final int WEB_SOCK_RECEIVE_COIN = 58;		//本场收益

	public static final int WEB_SOCK_TEST = Integer.MAX_VALUE;		// 统一测试消息用于展示

	//跳转类型标记
	public static final String PARAM_SWITH_TYPE = "swith_type";
	// 守护身份
	public static final String IDENTITY_GUARD = "id_guard";
	// 抢座标示
	public static final String IDENTITY_GRAB_SEAT = "id_grab_seat";
	// 充值
	public static final String EXTRA_CHARGE = "95xiu_charge";
	// 登陆
	public static final String EXTRA_LOGIN = "95xiu_login";
	// 分享
	public static final String EXTRA_SHARE = "95xiu_share";
	// 购买座驾
	public static final String EXTRA_CAR = "95xiu_car";
	//直播间跳转
	public static final String EXTRA_JUMP = "95xiu_jum_to_room";
	//夺宝跳转
	public static final String EXTRA_TREASURE = "95xiu_jum_to_treasure";
	//跳转到webview
	public static final String EXTRA_WEBVIEW = "95xiu_jum_to_webview";


	/******************************************************************************
	 * 
	 * enum
	 * 
	 *****************************************************************************/
	public enum LiveAnimEnum{
		LAE_INIT,
		LAE_NULL,
		LAE_ANCHOR_INFO,
		LAE_ANCHOR_INFO_HIDE,
		LAE_TALK_OTHERS,
		LAE_GIFT,
		LAE_EXPRESSION,
		LAE_LOGIN,
		LAE_INPUT_TEXT,
		LAE_INPUT_TEXT_V2
	};

	public enum AnimEnum{
		AE_INIT, AE_VISIABLE, AE_IN_VISIABLE
	}

	public static class LiveAnimListener implements AnimationListener{
		private View mView;

		public LiveAnimListener(View v){
			mView = v;
		}

		@Override
		public void onAnimationEnd(Animation arg0) {
			mView.clearAnimation();
		}

		@Override
		public void onAnimationRepeat(Animation arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onAnimationStart(Animation arg0) {
			// TODO Auto-generated method stub

		}
	}
	
}

