package com.cold.projectanalysis.paintorview.bubble;

import android.os.Handler;

import com.cold.projectanalysis.R;
import com.cold.projectanalysis.paintorview.ApplicationUtilV2;
import com.cold.projectanalysis.paintorview.PaintorViewActivity;
import com.cold.projectanalysis.paintorview.PhivePaintorView;
import com.cold.projectanalysis.paintorview.PhivePraiseManager;
import com.cold.projectanalysis.paintorview.paintor.AddItem;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016-08-11.
 */
public class PhiveBubbleEffect extends PhiveBaseModuel implements PhivePraiseManager.OnPraiseListener {
    private PhivePaintorView mPhivePaintorView;
    private PhiveBubbleEffectPaintor mPhiveBubbleEffectPaintor;

    private static final int MIN_LV = 1;
    private static final int MAX_LV = 3;

    private static final int BUBBLE_EFFECT_ADD = 0;
    private static final int BUBBLE_EFFECT_GETSPEED = 1;
    private static final int BUBBLE_ADD_DELAY = 200;    //默认冒泡等待时间

    private static final int BUBBLE_GET_SPEED = 5 * 60 * 1000;  //间隔多久获取一次真实人数，直接返回档位
    private static final int RESET_SPEED_TIME = 3 * 1000;      //30秒没赞就回复真实速度

    //一共三个档位，1，2,3，默认1当
//    *1档时，冒泡速度为1-5个/秒，每0.5秒改变一次速度；
//            *2档时，冒泡速度为3-7个/秒，每0.5秒改变一次速度；
//            *3档时，冒泡速度为5-9个/秒，每0.5秒改变一次速度。
    private long prePraiseTime = 0;     //上一次赞的时间
    private int bubbleLv = MIN_LV;      //当前档位
    private int wsBubbleLv = MIN_LV;    //服务器返回的真实档位
    private static final int speedExTime = 1000;     //速度切换时间，多长时间切换一次当前档位的一个随机的速度

    private Map<Integer, LvParam> lvParamMap ;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case BUBBLE_EFFECT_ADD:
                    if(mPhivePaintorView != null && mPhiveBubbleEffectPaintor != null) {
                        mPhivePaintorView.add(new AddItem(mPhiveBubbleEffectPaintor, null));
                        sendEmptyMessageDelayed(BUBBLE_EFFECT_ADD, getDelay(checkLv()));
                    }
                    break;
                case BUBBLE_EFFECT_GETSPEED:
//                    ISession socket = mActivity.getPhiveWebSocket();
//                    if(socket != null) {
//                        socket.getBubbleLv();
//                    }
//                    sendEmptyMessageDelayed(BUBBLE_EFFECT_GETSPEED, BUBBLE_GET_SPEED);
                    break;
            }
        }
    };

    //检查档位，检查是否赞的时间过期，需要恢复到真实速度，还是真实人数改变导致档位改变等
    //没获取一次冒泡延迟计算一次，保证数据及时更新
    private int checkLv(){
        //赞是否还在有效期内
        boolean praised = System.currentTimeMillis() - prePraiseTime < RESET_SPEED_TIME;
        bubbleLv = praised ? wsBubbleLv + 1 : wsBubbleLv;

        if (bubbleLv < MIN_LV) bubbleLv = MIN_LV;
        if (bubbleLv > MAX_LV) bubbleLv = MAX_LV;

        return bubbleLv;
    }

    //根据目前的档位获得延迟时间
    private int getDelay(int lv){
        if(lvParamMap != null) {
            LvParam lvParam = lvParamMap.get(lv);
            if(lvParam != null) {
                return lvParam.getDelay();
            }
        }
        return BUBBLE_ADD_DELAY;
    }

    public PhiveBubbleEffect(PaintorViewActivity activity){
        super(activity, R.id.ll_paintorParent);

        //socket连接上后会获取一次，默认这里是间隔后获取
        if(mHandler != null) mHandler.sendEmptyMessageDelayed(BUBBLE_EFFECT_GETSPEED, BUBBLE_GET_SPEED);

        initViews();
    }

    private void initViews(){
        mPhivePaintorView = (PhivePaintorView) mActivity.findViewById(R.id.trv_paintor);
        mPhiveBubbleEffectPaintor = mPhivePaintorView.getPhiveBubbleEffectPaintor();

        //初始化档位参数
        lvParamMap = new HashMap<Integer, LvParam>();
        lvParamMap.put(1, new LvParam(1,2,speedExTime));
        lvParamMap.put(2, new LvParam(3,4,speedExTime));
        lvParamMap.put(3, new LvParam(5,6,speedExTime));
    }

    //点赞回调，如果是首次点赞，档位+1，录制端没有点赞
    @Override
    public void onPraise(int praiseType, long praiseTime, int comboCount) {
        prePraiseTime = System.currentTimeMillis();
    }

//    //设置ws返回的档位结果
//    public void setBubbleLv(WSBubbleLv lvObj){
//        if(lvObj != null ) {
//            this.wsBubbleLv = lvObj.getSpeed();
//        }
//
//        if(this.wsBubbleLv > MAX_LV - 1) this.wsBubbleLv = MAX_LV - 1;
//        if(this.wsBubbleLv < MIN_LV) this.wsBubbleLv = MIN_LV;
////        resetLv(bubbleLv);
//    }

//    private void resetLv(int lv){
//        if(lvParamMap != null) {
//            LvParam lvParam = lvParamMap.get(lv);
//            if(lvParam != null) {
//                lvParam.reset();
//            }
//        }
//    }

    @Override
    public boolean isOnShowing(){
        if(mPhiveBubbleEffectPaintor != null) {
            return mPhiveBubbleEffectPaintor.isShown();
        }
        return false;
    }

    /************************************************************************
     *
     * Methods
     *
     ***********************************************************************/
    @Override
    public void setVisibleAnim(PhiveUtil.AnimEnum animEnum, PhiveUtil.PhiveAnimEnum promoter, long delay){
        if((animEnum == PhiveUtil.AnimEnum.AE_VISIABLE && isOnShowing())
                || (animEnum == PhiveUtil.AnimEnum.AE_IN_VISIABLE && !isOnShowing())){
            return ;
        }

        super.setVisibleAnim(animEnum, promoter, delay);

        mParent.clearAnimation();
        switch(animEnum) {
            case AE_VISIABLE:       //显示
                if(mPhiveBubbleEffectPaintor != null) mPhiveBubbleEffectPaintor.setVisib(PhiveUtil.AnimEnum.AE_VISIABLE);
                break;
            case AE_IN_VISIABLE:    //隐藏
                if(mPhiveBubbleEffectPaintor != null) mPhiveBubbleEffectPaintor.setVisib(PhiveUtil.AnimEnum.AE_IN_VISIABLE);
                break;
        }
    }

    @Override
    public void onPhiveRoomLoadedFinish(){
        start();
        mHandler.sendEmptyMessage(BUBBLE_EFFECT_ADD);
    }

    public void start(){
        if(mPhiveBubbleEffectPaintor != null && !mPhiveBubbleEffectPaintor.isRunning()) mPhiveBubbleEffectPaintor.start();
        mHandler.sendEmptyMessage(BUBBLE_EFFECT_ADD);
    }

    public void stop(){
        if(mPhiveBubbleEffectPaintor != null && mPhiveBubbleEffectPaintor.isRunning()) mPhiveBubbleEffectPaintor.stop();
    }

//    public void resetBirthland(){
//        if(mPhiveBubbleEffectPaintor != null) mPhiveBubbleEffectPaintor.resetBirthland();
//    }

    @Override
    public boolean onBackPressed(){
        return false;
    }

    @Override
    public void clear() {
        if(mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }

//        if(mPhivePaintorView != null) {
//            mPhivePaintorView.clear();
//        }
//
//        if(mActivity instanceof PhiveRoomActivity) {
//            PhiveRoomActivity a = (PhiveRoomActivity) mActivity;
//            if(a.getPhivePraiseManager() != null) {
//                a.getPhivePraiseManager().removeOnPraiseListener(this);
//            }
//        }
    }



    private class LvParam{
        public int min;        //最少速度，1秒冒出数量
        public int max;        //最大速度，1秒冒出数量
        public int exMillions;//切换时间
        public long preExTime;//上一次切换速度时间
        public int curSpeed;  //当前速度，1秒冒出数量

        public LvParam(int min, int max, int exMillions) {
            this.min = min;
            this.max = max;
            this.exMillions = exMillions;
        }

        public int getDelay(){
            long curTime = System.currentTimeMillis();
            if(curTime - preExTime > exMillions) {    //重新算一个随机速度
                curSpeed = ApplicationUtilV2.rand(min, max);
            }

            //1000毫秒产生的气泡
            return Float.valueOf(1000f / curSpeed).intValue();
        }

        //重置记录，用来切档时重置
        public void reset(){
            preExTime = 0;
        }
    }
}
