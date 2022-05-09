package com.cold.marquee5;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import com.cold.marquee5.other.SpannableUtil2;
import com.cold.marquee5.other.WSBaseBroatcastBean;

import java.lang.ref.SoftReference;
import java.util.LinkedList;
import java.util.Queue;

/**
 * name: LiveSlidePlayer2
 * desc: 右侧进入，左侧弹出，提示对话框
 * author:
 * date: 2017-07-24 10:00
 * remark:
 * 2017-08-03 12:04 当使用String test1 = "恭喜 <b>“金大羽晴”</b> 升级到16 探花1111112 ";后面的是11112没有空格的时候，会导致后面的数字不显示，包括图片
 * 产生问题原因，因为布局和代码对HorizontalScrollView设置了padding，所以导致textview显示宽度不对，在布局增加一个frameLayout的父布局解决
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
     * 滚动速率，px/ms
     */
    private float scrollSpeed = 0.10f;

    /**
     * 5种不同的显示界面集合
     */
    private SparseArray<View> layouts;

    /**
     * 初始化消息队列等
     * @param: parent 父容器
     * @return
     */
    public LiveSlidePlayer2(MainActivity activity, ViewGroup parent, int width,TextView tvTest, TextView tvTest1) {
        this.mActivity = new SoftReference<>(activity);
        this.mParent = parent;
        mContentQueue = new LinkedList<>();
        parentWidth = width;
        layouts = new SparseArray<>();

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

    public void getView2(Context context, FrameLayout flyt_parent1, WSBaseBroatcastBean data) {
        View v = null;
        v = LayoutInflater.from(context).inflate(R.layout.view_live_chat_slide_item_test, null);
        FrameLayout.LayoutParams lp2 = new FrameLayout.LayoutParams(dip2px(260), dip2px(70));
        lp2.gravity = Gravity.CENTER | Gravity.BOTTOM;
        lp2.bottomMargin = dip2px(100);
        v.setLayoutParams(lp2);
        TextView tv2 = (TextView)v.findViewById(R.id.tv_scroll);
//                HorizontalScrollView hsv2 = (HorizontalScrollView)v.findViewById(R.id.hsv_scroll);
//                hsv2.setBackgroundResource(R.drawable.bg_hint_anchor_upgrade);
//                hsv2.setPadding(dip2px(18), 0, dip2px(18), 0);
        tv2.setMaxLines(1);
        flyt_parent1.addView(v);
        v.setVisibility(View.VISIBLE);
        tv2.setText(getContent(data));
    }

    /**
     * 获取对应类型的View
     * @param
     * @return
     *
     */
    public View getView(int mBroatcastType, WSBaseBroatcastBean data) {
        Activity activity = mActivity.get();
        View v = null;
        switch(mBroatcastType) {
            case 2: // 主播升级
                v = LayoutInflater.from(activity).inflate(R.layout.view_live_chat_slide_item, null);
                FrameLayout.LayoutParams lp2 = new FrameLayout.LayoutParams(dip2px(260), dip2px(70));
                lp2.gravity = Gravity.CENTER | Gravity.BOTTOM;
                lp2.bottomMargin = dip2px(100);
                v.setLayoutParams(lp2);
                TextView tv2 = (TextView)v.findViewById(R.id.tv_scroll);
                FrameLayout hsv2 = (FrameLayout)v.findViewById(R.id.flyt_parent);
                hsv2.setBackgroundResource(R.drawable.bg_hint_anchor_upgrade);
                hsv2.setPadding(dip2px(18), 0, dip2px(18), 0);
                tv2.setMaxLines(1);
                tv2.setText(getContent(data));
                ViewHolder holder2 = new ViewHolder();
                holder2.flytParent = hsv2;
                holder2.tvContent = tv2;
                v.setTag(holder2);
                mParent.addView(v);
                v.setVisibility(View.INVISIBLE);
                break;
            case 8: // 8.购买骑士广播
                v = LayoutInflater.from(activity).inflate(R.layout.view_live_chat_slide_item, null);
                FrameLayout.LayoutParams lp8 = new FrameLayout.LayoutParams(dip2px(260), dip2px(70));
                lp8.gravity = Gravity.CENTER | Gravity.BOTTOM;
                lp8.bottomMargin = dip2px(100);
                v.setLayoutParams(lp8);
                v.setVisibility(View.INVISIBLE);
                TextView tv8 = (TextView)v.findViewById(R.id.tv_scroll);
                FrameLayout hsv8 = (FrameLayout)v.findViewById(R.id.flyt_parent);
                hsv8.setBackgroundResource(R.drawable.bg_hint_knight);
                hsv8.setPadding(dip2px(37), 0, dip2px(18), 0);
                tv8.setMaxLines(1);
                ViewHolder holder8 = new ViewHolder();
                holder8.flytParent = hsv8;
                holder8.tvContent = tv8;
                v.setTag(holder8);
                mParent.addView(v);
                break;
            case 9: // 9.购买dvip广播
                v = LayoutInflater.from(activity).inflate(R.layout.view_live_chat_slide_item, null);
                FrameLayout.LayoutParams lp9 = new FrameLayout.LayoutParams(dip2px(260), dip2px(70));
                lp9.gravity = Gravity.CENTER | Gravity.BOTTOM;
                lp9.bottomMargin = dip2px(100);
                v.setLayoutParams(lp9);
                v.setVisibility(View.INVISIBLE);
                TextView tv9 = (TextView)v.findViewById(R.id.tv_scroll);
                FrameLayout hsv9 = (FrameLayout)v.findViewById(R.id.flyt_parent);
                hsv9.setBackgroundResource(R.drawable.bg_hint_buy_diamond);
                hsv9.setPadding(dip2px(37), 0, dip2px(18), 0);
                tv9.setMaxLines(1);
                ViewHolder holder9 = new ViewHolder();
                holder9.flytParent = hsv9;
                holder9.tvContent = tv9;
                v.setTag(holder9);
                mParent.addView(v);
                break;
            case 10: // 10,用户升级
                v = LayoutInflater.from(activity).inflate(R.layout.view_live_chat_slide_item, null);
                FrameLayout.LayoutParams lp10 = new FrameLayout.LayoutParams(dip2px(260), dip2px(70));
                lp10.gravity = Gravity.CENTER | Gravity.BOTTOM;
                lp10.bottomMargin = dip2px(100);
                v.setLayoutParams(lp10);
                v.setVisibility(View.INVISIBLE);
                TextView tv10 = (TextView)v.findViewById(R.id.tv_scroll);
                FrameLayout hsv10 = (FrameLayout)v.findViewById(R.id.flyt_parent);
                hsv10.setBackgroundResource(R.drawable.bg_hint_user_upgrade);
                hsv10.setPadding(dip2px(18), 0, dip2px(18), 0);
                tv10.setMaxLines(1);
                ViewHolder holder10 = new ViewHolder();
                holder10.flytParent = hsv10;
                holder10.tvContent = tv10;
                v.setTag(holder10);
                mParent.addView(v);
                break;
            case 18: // 抢头条成功广播
                v = LayoutInflater.from(activity).inflate(R.layout.view_live_chat_slide_item, null);
                FrameLayout.LayoutParams lp18 = new FrameLayout.LayoutParams(dip2px(260), dip2px(70));
                lp18.gravity = Gravity.CENTER | Gravity.BOTTOM;
                lp18.bottomMargin = dip2px(100);
                v.setLayoutParams(lp18);
                v.setVisibility(View.INVISIBLE);
                TextView tv18 = (TextView)v.findViewById(R.id.tv_scroll);
                FrameLayout hsv18 = (FrameLayout)v.findViewById(R.id.flyt_parent);
                hsv18.setBackgroundResource(R.drawable.bg_hint_headlines);
                hsv18.setPadding(dip2px(18), 0, dip2px(18), 0);
                tv18.setMaxLines(1);
                ViewHolder holder18 = new ViewHolder();
                holder18.flytParent = hsv18;
                holder18.tvContent = tv18;
                v.setTag(holder18);
                mParent.addView(v);
                break;
        }
        return v;
    }

    /**
     * 播放广播
     * @param
     * @return
     */
    private void playBrocast() {
        if (isPlaying) return;
        if (mContentQueue == null) return;
        final WSBaseBroatcastBean data = mContentQueue.poll();
        if (null == data) {
            if (mHandler != null) mHandler.sendEmptyMessage(NEXT_BROCAST);
            return;
        }
        isPlaying = true;
        String content = data.getMsgContent();
        if (TextUtils.isEmpty(content)) {
            if (mHandler != null) mHandler.sendEmptyMessage(NEXT_BROCAST);
            return;
        }
        if(layouts != null) {
            View view = layouts.get(data.getBroatcastType()); // 获取对应类型的布局
            if (view == null) {
                view = getView(data.getBroatcastType(),data);
                layouts.put(data.getBroatcastType(), view);
            }
            final View v = view;
//            TextView tvScroll = (TextView)v.findViewById(R.id.tv_scroll);
//            System.out.println("==========================> getContent(data): " + getContent(data) + "/");
//            tvScroll.setText(getContent(data));
//            tvScroll.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
////            tvScroll.layout(0, 0, tvScroll.getMeasuredWidth(), tvScroll.getMeasuredHeight());
//            int measuredWidthTicketNum = tvScroll.getMeasuredWidth();
//            System.out.println("----------------------> measuredWidthTicketNum111: " + measuredWidthTicketNum);

            view.post(new Runnable() {
                @Override
                public void run() {
//                    final FrameLayout hsv = (FrameLayout)v.findViewById(R.id.flyt_parent);
                    final ViewHolder holder = (ViewHolder)v.getTag();
//                    TextView tvScroll = (TextView)v.findViewById(R.id.tv_scroll);
//
//                    int textViewWidth = getTextViewWidth(tvScroll); // 文本textview的总长度
//                    System.out.println("-------------------> hsv.getWidth111(): " + hsv.getWidth());
                    AnimatorSet animatorSet = getAnimator2(v, data, new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            holder.flytParent.setVisibility(View.INVISIBLE);
                            isPlaying = false;
                            if (mHandler != null) mHandler.sendEmptyMessage(NEXT_BROCAST);
                        }

                        @Override
                        public void onAnimationStart(Animator animation) {
                            super.onAnimationStart(animation);
                            if (holder.flytParent.getVisibility() != View.VISIBLE)
                                holder.flytParent.setVisibility(View.VISIBLE);

                        }
                    });

                    // 4.启动
                    animatorSet.start();
                }
            });

        }


//
//        int width = getTextViewWidth(layouts.get(data.getBroatcastType()), data);
//        System.out.println("==========================> width: " + width);
//        View v = layouts.get(data.getBroatcastType());
//        TextView tvScroll = (TextView)v.findViewById(R.id.tv_scroll);
//        startScroll(tvScroll, width - 672);
//        isPlaying = true;
//        if(controlView != null) {
//            controlView.reset();
//        }
//        // 1.初始化字体属性
//        initContent(data);
//
//        int width = 0;
//        if(controlView != null)
//            width = (int)controlView.getTextWidth();
//        // 2.初始化显示控件属性
//        initContentView(data, width);
//
//        // 3.获取动画

    }

    /**
     * 获取textview宽度
     * @param
     * @return
     */
    private int getTextViewWidth(View v, WSBaseBroatcastBean data) {
        TextView tvScroll = (TextView)v.findViewById(R.id.tv_scroll);
        tvScroll.setText(getContent(data));
        int spec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        tvScroll.measure(spec,spec);
//        tvScroll.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
//        tvScroll.layout(0, 0, tvScroll.getMeasuredWidth(), tvScroll.getMeasuredHeight());
        int measuredWidthTicketNum = tvScroll.getMeasuredWidth();


        System.out.println("----------------------> measuredWidthTicketNum: " + measuredWidthTicketNum);
        return measuredWidthTicketNum;
    }

    /**
     * 获取文本内容
     * @param
     * @return
     */
    public SpannableStringBuilder getContent(WSBaseBroatcastBean data) {
        int type = data.getBroatcastType();
        int resId = 0;
        if(type == 2) { // 主播升级
            resId = R.drawable.star_level_22;
        } else if(type == 10) { // 用户升级
            resId = R.drawable.ic_test;
        } else {
            resId = 0;

        }

        String mString = data.getMsgContent();//恭喜 “美女主播” 成为 “小裕妮” 的骑士
        SpannableStringBuilder showString = new SpannableStringBuilder("");
        SpannableString spanStr = new SpannableString(parseContent(mString));
        showString.append(spanStr + " ");


        if(resId != 0) {
            showString.append(SpannableUtil2.getScaleImageSpan(mActivity.get(), resId, dip2px(15)));
        }

//        tvTest.setText(showString);
//        tvTest1.setText(showString);
        return showString;
    }

    /**
     * 开始滚动
     */
    private void startScroll(TextView targert, float scrollWidth) {
        System.out.println("========================> scrollWidth: " + scrollWidth);
        if (scrollWidth > 0) {
            final long duration = (long) (scrollWidth * 5);//动画时长
            //属性动画位移，从屏幕右边向左移动到屏幕外scrollWidth长度的距离
            ObjectAnimator anim = ObjectAnimator.ofFloat(targert, "translationX", 0, -scrollWidth);
            anim.setStartDelay(1500);
            anim.setDuration(3000);
//            if(controlView != null) {
//                anim.setDuration(controlView.getDuration());
//            }

            anim.setInterpolator(new LinearInterpolator());
            anim.setRepeatMode(ValueAnimator.RESTART);//无限重复
            anim.setRepeatCount(0);
            anim.start();
        }
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
     * @param
     * @return 动画
     */
    private AnimatorSet getAnimator2(View view, WSBaseBroatcastBean data, AnimatorListenerAdapter animAdapter) {
        ViewHolder holder = (ViewHolder)view.getTag();
//        FrameLayout hsv = holder.flytParent;
//        TextView tvScroll = holder.tvContent;
        holder.tvContent.setText(getContent(data));
        int textViewWidth = getTextViewWidth(holder.tvContent); // 文本textview的总长度
        int showWidth = holder.flytParent.getWidth() - holder.flytParent.getPaddingLeft() - holder.flytParent.getPaddingRight(); // 显示区域的长度，需要考虑padding值
        int scrollWidth = textViewWidth - showWidth; // 滚动距离，如果为0或负数不需要滚动
        Activity activity = mActivity.get();
        if (activity != null) {
            if(holder.tvContent.getTranslationX() != 0) { // 恢复文本初始位置
                holder.tvContent.setTranslationX(0);
            }

            ObjectAnimator animatorShow1 = ObjectAnimator.ofFloat(holder.flytParent, "translationX", parentWidth, -dip2px(10)).setDuration(200);
            ObjectAnimator animatorShow2 = ObjectAnimator.ofFloat(holder.flytParent, "translationX", -dip2px(10), 0).setDuration(180);

            animatorHide1 = ObjectAnimator.ofFloat(holder.flytParent, "translationX", 0, dip2px(10)).setDuration(180);
            animatorHide1.setStartDelay(3000);

            ObjectAnimator animatorHide2 = ObjectAnimator.ofFloat(holder.flytParent, "translationX", dip2px(10), -parentWidth).setDuration(200);

            ObjectAnimator tvAnim = null;
            if(scrollWidth > 0) { // 移动距离大于0时才开始移动
                tvAnim = ObjectAnimator.ofFloat(holder.tvContent, "translationX", 0, -scrollWidth).setDuration((long)(scrollWidth / scrollSpeed)); // 滚动时间
                tvAnim.setStartDelay(1000); // 延时1000ms开始移动
            }

            mAnimatorSet = new AnimatorSet();
            mAnimatorSet.addListener(animAdapter);
            if(tvAnim == null) {
                mAnimatorSet.play(animatorShow1);
                mAnimatorSet.play(animatorShow2).after(animatorShow1);
                mAnimatorSet.play(animatorHide1).after(animatorShow2);
                mAnimatorSet.play(animatorHide2).after(animatorHide1);
            } else {
                mAnimatorSet.play(animatorShow1);
                mAnimatorSet.play(animatorShow2).after(animatorShow1);
                mAnimatorSet.play(tvAnim).after(animatorShow2);
                mAnimatorSet.play(animatorHide1).after(tvAnim);
                mAnimatorSet.play(animatorHide2).after(animatorHide1);
            }
        }

        mAnimatorSet.removeAllListeners();

        mAnimatorSet.addListener(animAdapter);
        return mAnimatorSet;
    }

    /**
     * 获取文本宽度
     * @param
     * @return
     */
    private int getTextViewWidth(TextView tvScroll) {
        int spec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        tvScroll.measure(spec,spec);
        tvScroll.layout(0, 0, tvScroll.getMeasuredWidth(), tvScroll.getMeasuredHeight());
        int measuredWidthTicketNum = tvScroll.getMeasuredWidth();
        System.out.println("----------------------> measuredWidthTicketNum: " + measuredWidthTicketNum);
        return measuredWidthTicketNum;
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
        if (null != mAnimatorSet) {
            mAnimatorSet.removeAllListeners();
            mAnimatorSet.cancel();
            mAnimatorSet = null;
        }
        mHandler = null;
    }

    private class ViewHolder{

        public TextView tvContent = null;
        public FrameLayout flytParent = null;

        public ViewHolder() {

        }

    }

}
