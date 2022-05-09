package com.cold.pkview.mine;

import org.json.JSONObject;

public class LivePkInfo {

	/**
	 * 这个类解析出来的pk主播信息左边还是右边不准
	 * 要在直播间内核主播id再次对吧确认
	 */
	private LivePkBean mAnchorLeft;//挑战者1
	private LivePkBean mAnchorRight;//挑战者2
	private long mTotalExp;//pk需要达到的火力值
	/**
	 * pk状态
	 * 1 正在进行, 2 已经结束 3 无资格参赛
	 */
	private int mStatus; 
	private int mWinerId;//pk获胜的主播id
	private int mLimitTime;//pk剩余时间  /秒

	public LivePkBean getAnchorLeft() {
		return mAnchorLeft;
	}

	public void setAnchorLeft(LivePkBean mAnchorLeft) {
		this.mAnchorLeft = mAnchorLeft;
	}

	public LivePkBean getAnchorRight() {
		return mAnchorRight;
	}

	public void setAnchorRight(LivePkBean mAnchorRight) {
		this.mAnchorRight = mAnchorRight;
	}

	public long getTotalExp() {
		return mTotalExp;
	}

	public void setTotalExp(long mTotalExp) {
		this.mTotalExp = mTotalExp;
	}

	public int getStatus() {
		return mStatus;
	}

	public void setStatus(int mStatus) {
		this.mStatus = mStatus;
	}

	public int getWinerId() {
		return mWinerId;
	}

	public void setmWinerId(int mWinerId) {
		this.mWinerId = mWinerId;
	}

	public int getLimitTime() {
		return mLimitTime;
	}

	public void setLimitTime(int mLimitTime) {
		this.mLimitTime = mLimitTime;
	}

	/**
	 * 交换主播信息
	 */
	public void swapAnchor(){
		if (mAnchorLeft != null && mAnchorRight != null) {
			LivePkBean livePkBeanLeft = mAnchorRight;
			mAnchorRight = mAnchorLeft;
			mAnchorLeft = livePkBeanLeft;
		}
	}

	public LivePkInfo() {

	}

	public LivePkInfo(JSONObject jsonObject){
		if (jsonObject != null) {
			mAnchorLeft = new LivePkBean(jsonObject.optJSONObject("mAnchor1"));
			mAnchorRight = new LivePkBean(jsonObject.optJSONObject("mAnchor2"));
			mWinerId = jsonObject.optInt("win_anchor_id");
			mTotalExp = jsonObject.optLong("totals_gold");
			mLimitTime = (int) ((jsonObject.optLong("endtime"))/60);
			mStatus = jsonObject.optInt("mStatus");
		}
	}

}
