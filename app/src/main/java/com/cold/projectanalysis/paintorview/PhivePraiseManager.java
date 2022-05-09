package com.cold.projectanalysis.paintorview;

import java.util.LinkedList;

/**
 * 点赞管理类，只用于手机直播间
 * 1 进入房间首次点赞  2 连续点了99个赞 3 关注主播，4分享
 * Created by Administrator on 2016-11-15.
 */
public class PhivePraiseManager {

    public static final int PRAISE_NORMAL = -1;     //正常点赞
    public static final int PRAISE_FIRST = 1;
    public static final int PRAISE_99 = 2;
    public static final int PRAISE_FOCUS = 3;
    public static final int PRAISE_SHARE = 4;
    public static final int PRAISE_INVALID = 5;     //无效的赞，仅仅点击屏幕

    //用户连续点赞满99并触发推送消息条件后，30s内无法再次触发
    private int split99Time = 30 * 1000;
    private int resetTime = 5 * 1000;  //最后一次点赞结束后，5s内没有再次点赞，连续点赞次数重置；


    private long prePraiseTime = 0;     //上一次点赞时间
    private long pre99Time = 0;         //上一次99的时间
    private int comboCount = 0;     //连续点赞次数

    LinkedList<OnPraiseListener> mOnPraiseListeners ;


    public void addOnPraiseListener(OnPraiseListener listener){
        if(mOnPraiseListeners == null){
            mOnPraiseListeners = new LinkedList<OnPraiseListener>();
        }
        if(listener != null)
            mOnPraiseListeners.addLast(listener);
    }

    public void removeOnPraiseListener(OnPraiseListener listener){
        if(mOnPraiseListeners == null){
            return;
        }
        if(listener != null)
            mOnPraiseListeners.remove(listener);
    }

    public void clear(){
        if(mOnPraiseListeners != null) {
            mOnPraiseListeners.clear();
            mOnPraiseListeners = null;
        }
    }

    public static interface OnPraiseListener{
        void onPraise(int praiseType, long praiseTime, int comboCount);
    }
}
