package com.cold.marquee4;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import com.cold.marquee4.other.SpannableUtil2;
import com.cold.marquee4.other.WSBaseBroatcastBean;

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
    private MarqueeText mtvScroll;

    /**
     * 显示内容,加宽
     */
    private MarqueeText mtvScrollW;

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
     * 滚动控件
     */
    private HorizontalScrollView hsvScroll;

    /**
     * 滚动控件，宽
     */
    private HorizontalScrollView hsvScrollW;

    /**
     * 不滚动
     */
    private TextView tvNormal;

    /**
     * 不滚动，宽
     */
    private TextView tvNormalW;

    /**
     * 正常滚动阈值
     */
    private float sWidth = 0;

    /**
     * 加宽滚动阈值
     */
    private float sWWidth = 0;

    /**
     * 保存轮播对象信息
     */
    private ControlView controlView = null;

    /**
     * 滚动速率，px/ms
     */
    private float scrollSpeed = 0.10f;

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
        controlView = new ControlView();

        mtvScroll = (MarqueeText) mActivity.get().findViewById(R.id.mtv_scroll);
        mtvScrollW = (MarqueeText) mActivity.get().findViewById(R.id.mtv_scroll_w);
        rlytHit = (FrameLayout)mActivity.get().findViewById(R.id.fl_slideParent);
        hsvScroll = (HorizontalScrollView)mActivity.get().findViewById(R.id.hsv_scroll);
        hsvScrollW = (HorizontalScrollView)mActivity.get().findViewById(R.id.hsv_scroll_w);
        sWidth = hsvScroll.getWidth();
        sWWidth = hsvScrollW.getWidth();
        tvNormal = (TextView) mActivity.get().findViewById(R.id.tv_normal);
        tvNormalW = (TextView) mActivity.get().findViewById(R.id.tv_normal_w);

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
        if(controlView != null) {
            controlView.reset();
        }
        // 1.初始化字体属性
        initContent(data);

        int width = 0;
        if(controlView != null)
            width = (int)controlView.getTextWidth();
        // 2.初始化显示控件属性
        initContentView(data, width);

        // 3.获取动画
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
                if (rlytHit.getVisibility() != View.VISIBLE)
                    rlytHit.setVisibility(View.VISIBLE);
                if(controlView != null) {
                    controlView.getTvText().setText(controlView.getContent());
                    if(controlView.isScroll()) {
                        animatorHide1.setStartDelay(controlView.getDuration() + 2000 + 1120);
                        resetScroll(controlView.getTvText());
                        startScroll(controlView.getTvText(), controlView.getTextWidth() - controlView.getShowWidth());
                    }
                }
            }
        });

        // 4.启动
        animatorSet.start();
    }

    /**
     * 初始化显示的内容，包括文字，图片宽度，显示内容等
     * @param
     * @return
     */
    private void initContent(WSBaseBroatcastBean data) {
        int type = data.getBroatcastType();
        int imgWidth = 0;
        int resId = 0;
        if(type == 2) { // 主播升级
            imgWidth = getImgWidth(mActivity.get(), R.drawable.star_level_22, dip2px(15));
            resId = R.drawable.star_level_22;
        } else if(type == 10) { // 用户升级
            imgWidth = getImgWidth(mActivity.get(), R.drawable.ic_test, dip2px(15));
            resId = R.drawable.ic_test;
        } else {
            resId = 0;
            imgWidth = 0;
        }

        String mString = data.getMsgContent();//恭喜 “美女主播” 成为 “小裕妮” 的骑士
        SpannableStringBuilder showString = new SpannableStringBuilder("");
        SpannableString spanStr = new SpannableString(parseContent(mString));
        showString.append(spanStr + " ");
        float textWidth = getTextWidth(showString);
        int totalWidth = (int)textWidth + imgWidth; // 显示总长度

        if(resId != 0) {
            showString.append(SpannableUtil2.getScaleImageSpan(mActivity.get(), resId, dip2px(15)));
        }
        if(controlView != null) {
            controlView.setTextWidth(totalWidth);
            controlView.setContent(showString);
        }
    }

    /**
     * 开始滚动
     */
    private void startScroll(TextView targert, float scrollWidth) {
        System.out.println("========================> scrollWidth: " + scrollWidth);
        if (scrollWidth != 0) {
            final long duration = (long) (scrollWidth * 5);//动画时长
            //属性动画位移，从屏幕右边向左移动到屏幕外scrollWidth长度的距离
            ObjectAnimator anim = ObjectAnimator.ofFloat(targert, "translationX", 0, -scrollWidth);
            anim.setStartDelay(1500);
            anim.setDuration(1000);
            if(controlView != null) {
                anim.setDuration(controlView.getDuration());
            }

            anim.setInterpolator(new LinearInterpolator());
            anim.setRepeatMode(ValueAnimator.RESTART);//无限重复
            anim.setRepeatCount(0);
            anim.start();
        }
    }

    /**
     * 初始化显示位置
     * @param targert 初始化的对象
     * @return
     */
    private void resetScroll(TextView targert) {
        ObjectAnimator anim = ObjectAnimator.ofFloat(targert, "translationX", 0);
        anim.setDuration(0);
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatMode(ValueAnimator.RESTART);//无限重复
        anim.setRepeatCount(0);
        anim.start();
    }

    /**
     * 获取字体宽度
     * @param
     * @return
     */
    private float getTextWidth(CharSequence c) {
        return mtvScroll.getTextWidth(c);
    }

    /**
     * 获得textview
     *
     * @param data 广播内容
     * @param totalWidth 显示内容宽度
     * @return
     */
    private void initContentView(WSBaseBroatcastBean data, int totalWidth) {
        int type = data.getBroatcastType();
        switch (type) {
            case 2: // 主播升级
                initAnchor(totalWidth);
                break;
            case 8: // 8.购买骑士广播
                initknight(totalWidth);
                break;
            case 9: // 9.购买dvip广播
                initDiamond(totalWidth);
                break;
            case 10: // 10,用户升级
                initUser(totalWidth);
                break;
            case 18: // 抢头条成功广播
                initHeadlines(totalWidth);
            break;
            default:
                break;
        }
    }

    /**
     * 初始化主播界面
     * @param width 字体宽度
     * @return
     */
    private void initAnchor(int width) {
        rlytHit.setBackgroundResource(R.drawable.bg_hint_anchor_upgrade);
        if(width <= sWWidth) { // 不滚动
            setContentHolder(3);
        } else { // 滚动
            setContentHolder(1);
        }
    }

    /**
     * 初始化骑士界面
     * @param width 字体宽度
     * @return
     */
    private void initknight(int width) {
        rlytHit.setBackgroundResource(R.drawable.bg_hint_knight);
        if(width <= sWidth) { // 不滚动
            setContentHolder(4);
        } else { // 滚动
            setContentHolder(2);
        }
    }

    /**
     * 初始化DVIP界面
     * @param width 字体宽度
     * @return
     */
    private void initDiamond(int width) {
        rlytHit.setBackgroundResource(R.drawable.bg_hint_buy_diamond);
        if(width <= sWidth) { // 不滚动
            setContentHolder(4);
        } else { // 滚动
            setContentHolder(2);
        }
    }

    /**
     * 初始化用户界面
     * @param width 字体宽度
     * @return
     */
    private void initUser(int width) {
        rlytHit.setBackgroundResource(R.drawable.bg_hint_user_upgrade);
        if(width <= sWWidth) { // 不滚动
            setContentHolder(3);
        } else { // 滚动
            setContentHolder(1);
        }
    }

    /**
     * 初始化抢头条界面
     * @param width 字体宽度
     * @return
     */
    private void initHeadlines(int width) {
        rlytHit.setBackgroundResource(R.drawable.bg_hint_headlines);
        if(width <= sWWidth) { // 不滚动
            setContentHolder(3);
        } else { // 滚动
            setContentHolder(1);
        }
    }

    /**
     * 获取图片宽度
     * @param
     * @return
     */
    private int getImgWidth(Context context, int spanImgIds, int height) {
        if (spanImgIds != 0){
            try {
                Drawable img = context.getResources().getDrawable(spanImgIds);
                int width = (int) (img.getIntrinsicWidth() * (float) height / img.getIntrinsicHeight());
                return width;
            } catch (Resources.NotFoundException e) {
                e.printStackTrace();
                return 0;
            }
        }
        return 0;
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
     * 设置显示内容对象
     * @param type 类型
     *        1: 滚动，加宽
     *        2：滚动，不加宽
     *        3：不滚动，加宽
     *        4：不滚动，不加宽
     * @return
     */
    private void setContentHolder(int type) {
        MarqueeText mtv = null;
        switch(type) {
            case 1:
                hsvScroll.setVisibility(View.INVISIBLE);
                hsvScrollW.setVisibility(View.VISIBLE);
                tvNormal.setVisibility(View.INVISIBLE);
                tvNormalW.setVisibility(View.INVISIBLE);
                if(controlView != null) {
                    controlView.setTvText(mtvScrollW);
                    controlView.setScroll(true);
                    controlView.setShowWidth(hsvScrollW.getWidth());
                    controlView.setDuration();
                }
                break;
            case 2:
                hsvScroll.setVisibility(View.VISIBLE);
                hsvScrollW.setVisibility(View.INVISIBLE);
                tvNormal.setVisibility(View.INVISIBLE);
                tvNormalW.setVisibility(View.INVISIBLE);
                if(controlView != null) {
                    controlView.setTvText(mtvScroll);
                    controlView.setScroll(true);
                    controlView.setShowWidth(hsvScroll.getWidth());
                    controlView.setDuration();
                }
                break;
            case 3:
                hsvScroll.setVisibility(View.INVISIBLE);
                hsvScrollW.setVisibility(View.INVISIBLE);
                tvNormal.setVisibility(View.INVISIBLE);
                tvNormalW.setVisibility(View.VISIBLE);
                if(controlView != null) {
                    controlView.setScroll(false);
                    controlView.setTvText(tvNormalW);
                }
                break;
            case 4:
                hsvScroll.setVisibility(View.INVISIBLE);
                hsvScrollW.setVisibility(View.INVISIBLE);
                tvNormal.setVisibility(View.VISIBLE);
                tvNormalW.setVisibility(View.INVISIBLE);
                if(controlView != null) {
                    controlView.setScroll(false);
                    controlView.setTvText(tvNormal);
                }
                break;

        }
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
    
    private class ControlView {

        private boolean isScroll;
        private float textWidth; // 内容显示长度，包括图片
        private TextView tvText; // 文本对象
        private int showWidth; // 字体可以显示的长度，设置为父布局的宽度
        private long duration; // 文字滚动周期
        private SpannableStringBuilder content; // 拼装好的显示内容

        public ControlView() {
            tvText = null;
        }

        public void setDuration() {
            float scrollWidth = textWidth - showWidth;
            duration = (long)(scrollWidth / scrollSpeed);
        }

        public void reset() {
            tvText = null;
            textWidth = 0;
            showWidth = 0;
            duration = 0;
            content = null;
        }

        public boolean isScroll() {
            return isScroll;
        }

        public void setScroll(boolean scroll) {
            isScroll = scroll;
        }

        public TextView getTvText() {
            return tvText;
        }

        public void setTvText(TextView tvText) {
            this.tvText = tvText;
        }

        public float getTextWidth() {
            return textWidth;
        }

        public void setTextWidth(float textWidth) {
            this.textWidth = textWidth;
        }

        public int getShowWidth() {
            return showWidth;
        }

        public void setShowWidth(int showWidth) {
            this.showWidth = showWidth;
        }

        public long getDuration() {
            return duration;
        }

        public void setDuration(long duration) {
            this.duration = duration;
        }

        public SpannableStringBuilder getContent() {
            return content;
        }

        public void setContent(SpannableStringBuilder content) {
            this.content = content;
        }
    }
}
