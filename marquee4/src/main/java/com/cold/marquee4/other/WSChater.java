package com.cold.marquee4.other;

import org.json.JSONObject;


public class WSChater {
    protected int uid = 0;        //如果是主播，这个就是房间ID
    protected String sessionId = "";
    protected String nickName = "";
    protected String avatar_url = "";
    protected int starLev = 0;
    protected int wealthLev = 0;
    protected long star_previous_integral = 0L;
    protected long star_integral = 0L;
    protected long star_next_integral = 0L;
    protected long wealth_integral = 0L;
    protected long wealth_next_integral = 0L;
    protected int carId = 0;
    protected int vipType = 0; //0 1 vip 2 s vip 3 d vip
    protected int uType = 0; // 0:普通用户；1：主播；2：巡管；3：销售;4：官方发言人
    protected int freeGiftCount = 0;
    protected int addFreeGiftNum = 0; // 当前用户增加或送出的阳光
    protected int coins = 0;
    protected boolean isGuarder = false;
    protected boolean isGrubSeat = false;
    protected String familyName;
    protected int familyLevel;
    public String contribution_val; // 贡献值
    protected int diamond;//秀钻
    protected JSONObject object;
    /**
     * 骑士类型
     * 1：爱的骑士 2：白马王子
     *
     * @return
     */
    protected int KnightType;

    // 帮会成员在帮会了的头衔
    protected int memberActor;

    //徽章id
    protected String badgeIds;
    //是否隐身
    protected boolean isHide;
    //是否被禁言
    protected boolean isGag;

    /*标注出跳转的类型，不同的跳转类型同时会附带其他参数，比如webviewurl，用来打开webview*/
    protected String extra_msg;            //表示跳转类型
    protected String extra_arg_url;       //跳转类型参数url

    //靓号
    protected int sid;
    //是否是靓号 0否，1是
    protected int liang;
    /**
     * 本地字段用于气氛判断数据是否来至礼物榜单，和观众列表数据区分
     */
    protected boolean isGiftRanking;


    public boolean isGiftRanking() {
        return isGiftRanking;
    }

    public void setGiftRanking(boolean giftRanking) {
        isGiftRanking = giftRanking;
    }

    public boolean isGag() {
        return isGag;
    }

    public void setGag(boolean isGag) {
        this.isGag = isGag;
    }

    public String getBadgeIds() {
        return badgeIds;
    }

    public void setBadgeIds(String badgeIds) {
        this.badgeIds = badgeIds;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getFreeGiftCount() {
        return freeGiftCount;
    }

    public int getKnightType() {
        return KnightType;
    }

    public void setKnightType(int knightType) {
        KnightType = knightType;
    }

    public void setFreeGiftCount(int freeGiftCount) {
        this.freeGiftCount = freeGiftCount;
    }

    public int getAddFreeGiftNum() {
        return addFreeGiftNum;
    }

    public void setAddFreeGiftNum(int addFreeGiftNum) {
        this.addFreeGiftNum = addFreeGiftNum;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getStarLev() {
        return starLev;
    }

    public void setStarLev(int starLev) {
        this.starLev = starLev;
    }

    public long getStar_previous_integral() {
        return star_previous_integral;
    }

    public void setStar_previous_integral(long star_previous_integral) {
        this.star_previous_integral = star_previous_integral;
    }

    public int getWealthLev() {
        return wealthLev;
    }

    public void setWealthLev(int wealthLev) {
        this.wealthLev = wealthLev;
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public int getVipType() {
        return vipType;
    }

    public void setVipType(int vipType) {
        this.vipType = vipType;
    }

    public boolean isGuarder() {
        return isGuarder;
    }

    public void setGuarder(boolean isGuarder) {
        this.isGuarder = isGuarder;
    }

    public boolean isGrubSeat() {
        return isGrubSeat;
    }

    public void setGrubSeat(boolean isGrubSeat) {
        this.isGrubSeat = isGrubSeat;
    }

    public String getExtra_msg() {
        return extra_msg;
    }

    public void setExtra_msg(String extra_msg) {
        this.extra_msg = extra_msg;
    }

    public int getuType() {
        return uType;
    }

    public void setuType(int uType) {
        this.uType = uType;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public String getContribution_val() {
        return contribution_val;
    }

    public void setContribution_val(String contribution_val) {
        this.contribution_val = contribution_val;
    }

    public long getStar_integral() {
        return star_integral;
    }

    public void setStar_integral(long star_integral) {
        this.star_integral = star_integral;
    }

    public long getStar_next_integral() {
        return star_next_integral;
    }

    public void setStar_next_integral(long star_next_integral) {
        this.star_next_integral = star_next_integral;
    }

    public long getWealth_integral() {
        return wealth_integral;
    }

    public void setWealth_integral(long wealth_integral) {
        this.wealth_integral = wealth_integral;
    }

    public long getWealth_next_integral() {
        return wealth_next_integral;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public void setWealth_next_integral(long wealth_next_integral) {
        this.wealth_next_integral = wealth_next_integral;
    }

    public int getDiamond() {
        return diamond;
    }

    public void setDiamond(int diamond) {
        this.diamond = diamond;
    }

    public int getFamilyLevel() {
        return familyLevel;
    }

    public void setFamilyLevel(int familyLevel) {
        this.familyLevel = familyLevel;
    }

    public boolean isHide() {
        return isHide;
    }

    public void setHide(boolean isHide) {
        this.isHide = isHide;
    }

    public String getExtra_arg_url() {
        return extra_arg_url;
    }

    public void setExtra_arg_url(String extra_arg_url) {
        this.extra_arg_url = extra_arg_url;
    }

    public WSChater() {

    }

    public int getMemberActor() {
        return memberActor;
    }

    public void setMemberActor(int memberActor) {
        this.memberActor = memberActor;
    }


    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public int getLiang() {
        return liang;
    }

    public void setLiang(int liang) {
        this.liang = liang;
    }




}
