package com.cold.marquee3;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.cold.marquee3.other.SpannableUtil2;
import com.cold.marquee3.other.WSBaseBroatcastBean;

import java.lang.ref.SoftReference;
import java.util.LinkedList;
import java.util.Queue;

/**
 * name: LiveSlidePlayer2
 * desc: 右侧进入，左侧弹出，提示对话框
 * author:
 * date: 2017-07-24 10:00
 * remark:
 */
public class LiveSlidePlayer2 {

    /**
     * 继续播放下一条广播
     */
    private static final int NEXT_BROCAST = 1;

    /**
     * context
     */
    private SoftReference<MainActivity> mActivity;

    /**
     * 消息队列
     */
    private Queue<WSBaseBroatcastBean> mContentQueue = null;

    /**
     * 提示对话框
     */
    private FrameLayout rlytHit;

    /**
     * 显示内容
     */
    private ScrollTextView tv_txt;

    /**
     * 播放动画集合
     */
    private AnimatorSet mAnimatorSet;

    /**
     * 父容器
     */
    private ViewGroup mParent;

    /**
     * 是否正在播放广播
     */
    private boolean isPlaying;

    /**
     * handler
     */
    private Handler mHandler;

    /**
     * 需要改变动画播放时事件的动画，实现设置文字轮播和不轮播的停止周期
     */
    private ObjectAnimator animatorHide1;

    private int parentWidth;

    /**
     * 初始化消息队列等
     * @param: parent 父容器
     * @return
     */
    public LiveSlidePlayer2(MainActivity activity, ViewGroup parent, int width) {

        this.mActivity = new SoftReference<>(activity);
        this.mParent = parent;
        mContentQueue = new LinkedList<>();
        parentWidth = width;

        tv_txt = (ScrollTextView) mActivity.get().findViewById(R.id.tv_txt);
        rlytHit = (FrameLayout)mActivity.get().findViewById(R.id.fl_slideParent);

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg != null) {
                    switch (msg.what) {
                        case NEXT_BROCAST:
                            if (mContentQueue != null && mContentQueue.size() > 0) {
                                if (!isPlaying) {
                                    playBrocast();
                                }
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
        };
    }

    /**
     * 播放广播
     * @param
     * @return
     */
    private void playBrocast() {
        if (isPlaying) return;
        if (mContentQueue == null) return;
        WSBaseBroatcastBean data = mContentQueue.poll();
        if (null == data) {
            if (mHandler != null) mHandler.sendEmptyMessage(NEXT_BROCAST);
            return;
        }
        String content = data.getMsgContent();
        if (TextUtils.isEmpty(content)) {
            if (mHandler != null) mHandler.sendEmptyMessage(NEXT_BROCAST);
            return;
        }
        isPlaying = true;

        final SpannableStringBuilder str = getContent(data);
        System.out.println("-------------------------> getWidth 3: " + tv_txt.getMeasuredWidth());

        AnimatorSet animatorSet = getAnimator(rlytHit, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                rlytHit.setVisibility(View.INVISIBLE);
                isPlaying = false;
                if (mHandler != null) mHandler.sendEmptyMessage(NEXT_BROCAST);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                System.out.println("-------------------------> getWidth 4: " + tv_txt.getMeasuredWidth());
                tv_txt.post(new Runnable() {
                    @Override
                    public void run() {
                        if (rlytHit.getVisibility() != View.VISIBLE)
                            rlytHit.setVisibility(View.VISIBLE);
                        if(tv_txt != null) {
                            tv_txt.setTextProperty(str);
                        }
                    }
                });
                tv_txt.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("-------------------------> getWidth 6: " + tv_txt.getMeasuredWidth());
                        animatorHide1.setStartDelay(tv_txt.getScrollCycle());
                        System.out.println("-------------------------> getWidth 7: " + tv_txt.getMeasuredWidth());
                        tv_txt.resumeScroll();
                        System.out.println("-------------------------> getWidth 8: " + tv_txt.getMeasuredWidth());

                    }
                }, 1500);

            }
        });
        animatorSet.start();
    }

    /**
     * 获得textview
     *
     * @param data
     * @return
     */
    private SpannableStringBuilder getContent(WSBaseBroatcastBean data) {
        int type = data.getBroatcastType();
        int resId = 0;
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) tv_txt.getLayoutParams();
        switch (type) {
            case 2: // 主播升级
                rlytHit.setBackgroundResource(R.drawable.bg_hint_anchor_upgrade);
                resId = R.drawable.star_level_22;
                lp.width = dip2px(224);
                lp.setMargins(dip2px(18), lp.topMargin, lp.rightMargin, lp.bottomMargin);
                tv_txt.setTextColor(Color.parseColor("#ffffff"));
                tv_txt.setLayoutParams(lp);
                tv_txt.invalidate();
                break;
            case 8: // 8.购买骑士广播
                rlytHit.setBackgroundResource(R.drawable.bg_hint_knight);
                lp.width = dip2px(205);
                lp.setMargins(dip2px(37), lp.topMargin, lp.rightMargin, lp.bottomMargin);
                tv_txt.setTextColor(Color.parseColor("#ffffff"));
                tv_txt.setLayoutParams(lp);
                break;
            case 9: // 9.购买dvip广播
                rlytHit.setBackgroundResource(R.drawable.bg_hint_buy_diamond);
                lp.width = dip2px(205);
                lp.setMargins(dip2px(37), lp.topMargin, lp.rightMargin, lp.bottomMargin);
                tv_txt.setTextColor(Color.parseColor("#ffffff"));
                tv_txt.setLayoutParams(lp);
                break;
            case 10: // 10,用户升级
                rlytHit.setBackgroundResource(R.drawable.bg_hint_user_upgrade);
                resId = R.drawable.ic_test;
                lp.width = dip2px(224);
                lp.setMargins(dip2px(18), lp.topMargin, lp.rightMargin, lp.bottomMargin);
                tv_txt.setTextColor(Color.parseColor("#ffffff"));
                tv_txt.setLayoutParams(lp);
                break;
            case 18: // 抢头条成功广播
                rlytHit.setBackgroundResource(R.drawable.bg_hint_headlines);
                lp.width = dip2px(224);
                lp.setMargins(dip2px(18), lp.topMargin, lp.rightMargin, lp.bottomMargin);
                tv_txt.setTextColor(Color.parseColor("#000000"));
                tv_txt.setLayoutParams(lp);
                break;

            default:
                break;
        }

        String mString = data.getMsgContent();//恭喜 “美女主播” 成为 “小裕妮” 的骑士
        SpannableStringBuilder showString = new SpannableStringBuilder("");
        SpannableString spanStr = new SpannableString(parseContent(mString));
        showString.append(spanStr + " ");
        if(resId != 0) {
            showString.append(SpannableUtil2.getScaleImageSpan(mActivity.get(), resId, dip2px(15)));
            if(tv_txt != null) {
                tv_txt.setImgWidth(mActivity.get(), resId, dip2px(15));
            }
        } else {
            if(tv_txt != null) {
                tv_txt.setImgWidth(0);
            }
        }
        return showString;
    }

    /**
     * 解析字符串
     * @param: mString
     * @return: void
     */
    private StringBuilder parseContent(String mString) {
        String c1 = "<b>";
        String c2 = "</b>";
        String c3 = "“";
        String c4 = "”";
        int indexC1 = mString.indexOf(c1);
        int indexC2 = mString.indexOf(c2);
        int indexC3 = mString.indexOf(c3);//中文【前双引号】
        int indexC4 = mString.indexOf(c4);//中文【后双引号】
        StringBuilder formatString = new StringBuilder(mString);
        if(mString.startsWith("恭喜") && indexC1 != -1 && indexC2 != -1) {
            formatString.replace(formatString.indexOf(c1),  formatString.indexOf(c1) + c1.length(), "");
            formatString.replace(formatString.indexOf(c2),  formatString.indexOf(c2) + c2.length(), "");
        }

        if (mString.startsWith("恭喜") && indexC3 != -1 && indexC4 != -1) {//要包含指定的字符才处理
            formatString.replace(formatString.indexOf(c3),  formatString.indexOf(c3) + c3.length(), "");
            formatString.replace(formatString.indexOf(c4),  formatString.indexOf(c4) + c4.length(), "");

            //处理主播名称
            indexC3 = formatString.indexOf(c3);
            indexC4 = formatString.indexOf(c4);
            if (indexC3 != -1 && indexC4 != -1) {
                formatString.delete(formatString.indexOf(c3), formatString.indexOf(c3) + 1);
                formatString.delete(formatString.indexOf(c4), formatString.indexOf(c4) + 1);
            }
        }
        return formatString;
    }


    /**
     * 获得动画
     */
    private AnimatorSet getAnimator(View view, AnimatorListenerAdapter animAdapter) {
        if (mAnimatorSet == null) {
            Activity activity = mActivity.get();
            if (activity != null) {
                ObjectAnimator animatorShow1 = ObjectAnimator.ofFloat(view, "translationX", parentWidth, -dip2px(10)).setDuration(200);
                ObjectAnimator animatorShow2 = ObjectAnimator.ofFloat(view, "translationX", -dip2px(10), 0).setDuration(180);

                animatorHide1 = ObjectAnimator.ofFloat(view, "translationX", 0, dip2px(10)).setDuration(180);
                ObjectAnimator animatorHide2 = ObjectAnimator.ofFloat(view, "translationX", dip2px(10), -parentWidth).setDuration(200);
                animatorHide1.setStartDelay(3000);

                mAnimatorSet = new AnimatorSet();
                mAnimatorSet.addListener(animAdapter);
                mAnimatorSet.play(animatorShow1);
                mAnimatorSet.play(animatorShow2).after(animatorShow1);
                mAnimatorSet.play(animatorHide1).after(animatorShow2);
                mAnimatorSet.play(animatorHide2).after(animatorHide1);
            }
        }
        mAnimatorSet.removeAllListeners();

        mAnimatorSet.addListener(animAdapter);
        return mAnimatorSet;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dip2px(float dpValue) {
        return (int) (dpValue * getDensity(mActivity.get()) + 0.5f);
    }

    /**
     * 返回屏幕密度
     */
    public float getDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    /**
     * 加入单个广播内容
     *
     * @param bean
     */
    public void addContentSpan(WSBaseBroatcastBean bean) {
        if (bean != null && mContentQueue != null) {
            mContentQueue.offer(bean);
        }
        if (mHandler != null) mHandler.sendEmptyMessage(NEXT_BROCAST);
    }


    /**
     * 释放资源
     */
    public void free() {
        if (mContentQueue != null) {
            mContentQueue.clear();
            mContentQueue = null;
        }
        if (mHandler != null) mHandler.removeCallbacksAndMessages(null);
        if (null != rlytHit) {
            rlytHit.setOnClickListener(null);
            rlytHit = null;
        }
        if (null != mAnimatorSet) {
            mAnimatorSet.removeAllListeners();
            mAnimatorSet.cancel();
            mAnimatorSet = null;
        }
        mHandler = null;
    }
}
