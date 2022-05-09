package com.cold.pkview.mine;

import org.json.JSONObject;

public class LivePkBean {
	private int anchorId; //主播id
	private long currentExp; //主播当前火力
	private String headUrl; //主播头像地址
	private String nickName;//主播昵称
	
	public int getAnchorId() {
		return anchorId;
	}
	
	public void setAnchorId(int anchorId) {
		this.anchorId = anchorId;
	}
	
	public long getCurrentExp() {
		return currentExp;
	}
	
	public void setCurrentExp(long currentExp) {
		this.currentExp = currentExp;
	}
	
	public String getHeadUrl() {
		return headUrl;
	}
	
	public void setHeadUrl(String headUrl) {
		this.headUrl = headUrl;
	}
	
	public String getNickName() {
		return nickName;
	}
	
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

     public LivePkBean(JSONObject jsonObject){
    	 if (jsonObject != null ) {
			anchorId = jsonObject.optInt("uid");
			currentExp = jsonObject.optLong("totals");
			headUrl = jsonObject.optString("avatar_url");
			nickName = jsonObject.optString("nickname");
		}
     }

	public LivePkBean(int anchorId, long currentExp, String headUrl, String nickName) {
		this.anchorId = anchorId;
		this.currentExp = currentExp;
		this.headUrl = headUrl;
		this.nickName = nickName;
	}
     
}
