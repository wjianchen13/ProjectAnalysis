package com.cold.marquee4.other;

import androidx.annotation.IntDef;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 涉及旧版本的兼容问题，旧版的样式删除，但是消息类型最好继续使用旧的属性名称，
 * 并且判断版本，赋值到旧的版本属性名称，用旧的属性名称统一处理，不要使用新的属性名称
 *
 * @author brice rong
 */
public class WSBaseBroatcastBean {


    public static final int URL_NONE = 0;//	默认为 0
    public static final int URL_CHANGER = 1; //1.充值
    public static final int URL_PK = 2; //2 pk
    public static final int URL_LUCK_INDIANA = 3; // 3.幸运夺宝
    public static final int URL_LUCK_JUMP_LIVE_ROOM = 4;// 4 跳转到直播间
    public static final int URL_LUCK_JUMP_WEB = 5;//5 跳转到webview

    @IntDef({URL_NONE, URL_CHANGER, URL_LUCK_INDIANA, URL_LUCK_JUMP_LIVE_ROOM, URL_PK, URL_LUCK_JUMP_WEB})
    @Retention(RetentionPolicy.SOURCE)
    public @interface URLType {
    }
    /**
     * 0,无
     * 1,礼物广播
     * 2 娱乐场广播
     * 3.系统信息 /集结消息
     * 4.普通广播
     * 5.文字广播
     * 6.主播送专属礼（mSender为主播）
     * 7.红包广播
     * 11,用户喊话广播
     * 10，用户升级系统消息
     * 12.红包消息
     * 13,活动消息
     * 14 pk胜利消息
     * 15文字图片广播（新的tab分类）
     * 新的类型json属性，赋值到旧字段
     */

    public final static int BROADCAST_TYPE_NONE = 0;
    public final static int BROADCAST_TYPE_GIFT = 1;
    public final static int BROADCAST_TYPE_ENTERTAINING = 2;
    public final static int BROADCAST_TYPE_SYS = 3;
    public final static int BROADCAST_TYPE_LOUDSPEAKER = 4;
    public final static int BROADCAST_TYPE_TEXT = 5;
    public final static int BROADCAST_TYPE_ANCHOR_SEND_GIFT = 6;
    public final static int BROADCAST_TYPE_RED_PACKET = 7;
    public final static int BROADCAST_TYPE_USER_TAKING = 11;
    public final static int BROADCAST_TYPE_USER_UPGRADE_SYS = 10;
    public final static int BROADCAST_TYPE_RED_PACKET_MESSAGE = 12;
    public final static int BROADCAST_TYPE_ACTION_MESSAGE = 13;
    public final static int BROADCAST_TYPE_PK_WIN_MESSAGE = 14;
    public final static int BROADCAST_TYPE_TEXT_IMG_TAG = 15;

    @IntDef({BROADCAST_TYPE_NONE, BROADCAST_TYPE_GIFT, BROADCAST_TYPE_ENTERTAINING,
            BROADCAST_TYPE_SYS, BROADCAST_TYPE_LOUDSPEAKER, BROADCAST_TYPE_TEXT,
            BROADCAST_TYPE_ANCHOR_SEND_GIFT, BROADCAST_TYPE_RED_PACKET, BROADCAST_TYPE_USER_TAKING,
            BROADCAST_TYPE_USER_UPGRADE_SYS, BROADCAST_TYPE_RED_PACKET_MESSAGE, BROADCAST_TYPE_ACTION_MESSAGE,
            BROADCAST_TYPE_PK_WIN_MESSAGE, BROADCAST_TYPE_TEXT_IMG_TAG})
    @Retention(RetentionPolicy.SOURCE)
    public @interface BroadcastType {
    }


    /**
     * 发送者消息
     */
    protected WSChater mSender;

    /**
     * 接受者消息
     */
    protected WSChater mReciever;

    /**
     * 消息内容
     */
    protected String mMsgContent;

    /**
     * 广播发送状态
     */
    protected int mStatus;

    /**
     * 0,无
     * 1,礼物广播
     * 2 娱乐场广播
     * 3.系统信息 /集结消息
     * 4.遮罩信息
     * 5.文字广播
     * 6.主播送专属礼（mSender为主播）
     * 7.红包广播
     * 11,用户喊话广播
     * 10，用户升级系统消息
     * 12.红包消息
     * 13,活动消息
     * 14 pk胜利消息
     * 15文字图片广播（新的tab分类）
     * 新的类型json属性，赋值到旧字段
     */
    protected int mBroatcastType;


    /**
     * 6.4版添加字段
     * 跳转类型，主要用于活动消息
     * 没有跳转的话，没有该字段，默认为 0
     * 1.充值 2 pk 3.幸运夺宝 4 跳转到直播间 5 跳转到webview
     */
    protected int urlType;

    /*跳转类型参数，跳转到webview时需要用到*/
    protected String webviewUrl;

    /**
     * 礼物名称（暂时只对红包）
     */
    protected String gift_name;

    /**
     * 礼物数量（暂时只对红包）
     */
    protected int acount;
    /**
     * 新的消息内容json属性，赋值到旧字段
     */
//	protected String newContent;
    /**
     * 6.4版新json属性
     * 输家名字
     */
    protected String nickname2;
    /**
     * 6.4版新json属性
     * 赢家共获得秀币
     */
    protected int gold;
    /**
     * 6.4版新json属性
     * 大小礼物区分字段
     * 1.小礼物  2.大礼物,3猜蛋，4水果机，5砸蛋,6夺宝
     */
    protected int show;
    /**
     * 额外非json字段，用于遮罩显示顺序
     * 显示优先级
     * 越大优先级越高
     */
    private int showPriority = 0;

    //0,秀币  1,喇叭卷
    private int mTool;
    //道具id
    private int tool_id;
    //道具剩余数量
    private int tool_acount;


    public static final int sp_treasure = 5;    //夺宝
    public static final int sp_guessEgg = 4;    //猜蛋
    public static final int sp_fruit = 3;        //水果机
    public static final int sp_smallgift = 2;    //小礼物显示优先级
    public static final int sp_hitEgg = 1;        //砸蛋

    public static int showPriority(int showType) {
        switch (showType) {
            case 6:        //夺宝
                return sp_treasure;
            case 3:        //猜蛋
                return sp_guessEgg;
            case 4:        //水果机
                return sp_fruit;
            case 1:        //小礼物
                return sp_smallgift;
            case 5:        //砸蛋
                return sp_hitEgg;
            default:    //大礼物和其他
                return 0;
        }
    }

    @BroadcastType
    public int getBroatcastType() {
        return mBroatcastType;
    }

    public void setBroatcastType(int mBroatcastType) {
        this.mBroatcastType = mBroatcastType;
    }

    public WSChater getSender() {
        return mSender;
    }

    public void setSender(WSChater mSender) {
        this.mSender = mSender;
    }

    public WSChater getReciever() {
        return mReciever;
    }

    public void setReciever(WSChater mReciever) {
        this.mReciever = mReciever;
    }

    public String getMsgContent() {
        return mMsgContent;
    }

    public void setMsgContent(String mMsgContent) {
        this.mMsgContent = mMsgContent;
    }

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(int mStatus) {
        this.mStatus = mStatus;
    }

    public String getNickname2() {
        return nickname2;
    }

    public void setNickname2(String nickname2) {
        this.nickname2 = nickname2;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public int getShow() {
        return show;
    }

    public void setShow(int show) {
        this.show = show;
    }

    private WSBaseBroatcastBean() {

    }

    /*获得一个传递用的bean 没有实际数据，用于测试*/
    public static WSBaseBroatcastBean getInformalVO() {
        return new WSBaseBroatcastBean();
    }

    @URLType
    public int getUrlType() {
        return urlType;
    }

    public void setUrlType(int urlType) {
        this.urlType = urlType;
    }

    public String getGift_name() {
        return gift_name;
    }

    public void setGift_name(String gift_name) {
        this.gift_name = gift_name;
    }

    public int getAcount() {
        return acount;
    }

    public void setAcount(int acount) {
        this.acount = acount;
    }

    public int getShowPriority() {
        return showPriority;
    }

    public void setShowPriority(int showPriority) {
        this.showPriority = showPriority;
    }

    public String getWebviewUrl() {
        return webviewUrl;
    }

    public void setWebviewUrl(String webviewUrl) {
        this.webviewUrl = webviewUrl;
    }

    public int getTool() {
        return mTool;
    }

    public void setTool(int tool) {
        mTool = tool;
    }

    public int getTool_id() {
        return tool_id;
    }

    public void setTool_id(int tool_id) {
        this.tool_id = tool_id;
    }

    public int getTool_acount() {
        return tool_acount;
    }

    public void setTool_acount(int tool_acount) {
        this.tool_acount = tool_acount;
    }

    //返回礼物信息结构
    public static class GiftInfo {
        public int gCount;
        public int gId;

        public GiftInfo(int gCount, int gId) {
            super();
            this.gCount = gCount;
            this.gId = gId;
        }

        //有效礼物信息
        public boolean isValid() {
            return gCount != 0 && gId != 0;
        }
    }

    /**
     * 获得礼物信息
     */
    public GiftInfo getGiftInfo() {
        int gCount = 0, gid = 0;
        try {
            JSONObject jsonObj = new JSONObject(getMsgContent());
            if (jsonObj != null && !jsonObj.equals("") && !jsonObj.equals("{}")) {
                gCount = jsonObj.optInt("acount", 0);
                gid = jsonObj.optInt("gift_id", 0);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new GiftInfo(gCount, gid);
    }




    /**
     * 解析系统或送礼广播数据
     *
     * @return
     */
    public static WSBaseBroatcastBean parseSysBaseBrocast2(JSONObject obj) {
        if (obj == null) {
            return null;
        } else {
            WSBaseBroatcastBean wsBaseBroatcastBean = new WSBaseBroatcastBean();
            JSONObject msgJSON = obj.optJSONObject("msg");
            wsBaseBroatcastBean.mMsgContent = msgJSON.optString("content", "");
            wsBaseBroatcastBean.webviewUrl = msgJSON.optString("webviewUrl", "");

            return wsBaseBroatcastBean;
        }
    }

    @Override
    public String toString() {
        return "WSBaseBroatcastBean{" +
                ", mMsgContent='" + mMsgContent + '\'' +
                ", mStatus=" + mStatus +
                ", mBroatcastType=" + mBroatcastType +
                ", gift_name='" + gift_name + '\'' +
                ", acount=" + acount +
                ", nickname2='" + nickname2 + '\'' +
                ", gold=" + gold +
                '}';
    }
}
