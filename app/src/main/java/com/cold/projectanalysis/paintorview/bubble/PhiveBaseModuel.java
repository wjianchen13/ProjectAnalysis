package com.cold.projectanalysis.paintorview.bubble;

import android.app.Activity;
import android.view.ViewGroup;

import com.cold.projectanalysis.paintorview.PaintorViewActivity;


/**
 * 基础模块类
 * Created by Administrator on 2016-07-15.
 */
public abstract class PhiveBaseModuel extends LazyHolder {
    public static final int MODUEL_TYPE_PIECE = 0;      // 碎片，不会对屏幕造成遮盖
    public static final int MODUEL_TYPE_COVER = 1;      // 遮挡屏幕底部

    protected int moduelType = MODUEL_TYPE_PIECE;
    protected PaintorViewActivity mActivity;
    protected ViewGroup mParent;

    public PhiveBaseModuel(Activity activity, Integer parentId) {
        super();
        if(activity instanceof PaintorViewActivity) {
            this.mActivity = (PaintorViewActivity) activity;
        }
//        if (mActivity != null) this.mActivity.getPhiveModuels().add(this);
        if (parentId != null && mActivity != null) {
            mParent = (ViewGroup) mActivity.findViewById(parentId);
//            if(mParent != null) {
//                mParent.setOnTouchListener(mActivity);
//            }
        }
    }

    @Override
    protected void lazyInitView() {

    }

    public boolean isOnShowing() {
        if (mParent == null) return false;
        return mParent.isShown();
    }

    public void setParent(ViewGroup parent) {
        this.mParent = parent;
    }

    public ViewGroup getParent() {
        return mParent;
    }

    public int getModuelType() {
        return moduelType;
    }

    public void setModuelType(int moduelType) {
        this.moduelType = moduelType;
    }

    public void setVisibleAnim(PhiveUtil.AnimEnum animEnum, PhiveUtil.PhiveAnimEnum promoter, long delay) {

        init();

        if (mParent != null) mParent.clearAnimation();
//        if (mActivity != null) mActivity.getBackPressObserver().remove(this);
//        switch (animEnum) {
//            case AE_VISIABLE:       //显示
//                if (mActivity != null) mActivity.getBackPressObserver().addFirst(this);
//                break;
//            case AE_IN_VISIABLE:    //隐藏
////                this.mActivity.getBackPressObserver().remove(this);
//                break;
//        }
    }

    /**
     * 返回false = 事件继续传递，否则被消费
     *
     * @return
     */
    public boolean onBackPressed() {
        if (isOnShowing()) {
            setVisibleAnim(PhiveUtil.AnimEnum.AE_IN_VISIABLE, PhiveUtil.PhiveAnimEnum.LAE_KEY_BACK, 0);
//            mActivity.setDisplayEnum(PhiveUtil.PhiveAnimEnum.LAE_KEY_BACK);
            return true;
        }
        return false;
    }

    /**
     * 直播间加载完成
     */
    public void onPhiveRoomLoadedFinish() {
    }

    /**
     * 资源回收，必须实现
     */
    public abstract void clear();

    //    public boolean dispatchTouchEvent(MotionEvent ev){return false;};
}
