package com.cold.popupwindow;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;


public class AppUtil {

    /******************************************************************************
     *
     * Animation
     *
     *****************************************************************************/
    /**
     * scale动画  小 - 大 - 小
     *
     * @return
     */
    public static AnimationSet getScaleAnimationSet(Context context) {
        AnimationSet animset = new AnimationSet(context, null);
        ScaleAnimation scaleAnim = new ScaleAnimation(1.0f, 2.0f, 1.0f, 2.0f, 0.5f, 0.5f);
        scaleAnim.setDuration(100);
        scaleAnim.setFillAfter(true);
        ScaleAnimation scaleAnim2 = new ScaleAnimation(2.0f, 0.6f, 2.0f, 0.6f, 0.5f, 0.5f);
        scaleAnim2.setDuration(100);
        scaleAnim2.setFillAfter(true);
        ScaleAnimation scaleAnim3 = new ScaleAnimation(0.6f, 1.0f, 0.6f, 1.0f, 0.5f, 0.5f);
        scaleAnim3.setDuration(100);
        scaleAnim3.setFillAfter(true);
        animset.addAnimation(scaleAnim);
        animset.addAnimation(scaleAnim2);
        animset.addAnimation(scaleAnim3);
        return animset;
    }

    /**
     * 砸蛋相关View出现动画
     *
     * @param view     播放动画的相关view
     * @param isAppear 是否是出现
     */
    public static ObjectAnimator getHitEggAnimator(final View view, final boolean isAppear, int drution) {
        PropertyValuesHolder translationYHolder = null;
        PropertyValuesHolder alphaHolder = null;
        if (isAppear) {
            translationYHolder = PropertyValuesHolder.ofFloat("translationY", 110, 0);
            alphaHolder = PropertyValuesHolder.ofFloat("alpha", 0, 1);
        } else {
            translationYHolder = PropertyValuesHolder.ofFloat("translationY", 0, 110);
            alphaHolder = PropertyValuesHolder.ofFloat("alpha", 1, 0);
        }
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(view, translationYHolder, alphaHolder).setDuration(drution);
        if (isAppear) {
            objectAnimator.setInterpolator(new DecelerateInterpolator());
        } else {
            objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        }
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                if (isAppear) {
                    view.setVisibility(View.VISIBLE);
                } else {
                    view.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (isAppear) {
                    view.setVisibility(View.VISIBLE);
                } else {
                    view.setVisibility(View.INVISIBLE);
                }
            }
        });
        return objectAnimator;
    }


//	public static Object dismissEgg(final View view , final boolean isAppear , int drution){
//
//
//	}


    /**
     * 砸蛋相关View出现动画
     *
     * @param view 播放动画的View
     * @return
     */
    public static ObjectAnimator getHitEggDisappearAnimator(final View view) {
        PropertyValuesHolder translationYHolder = PropertyValuesHolder.ofFloat("translationY", -50, 0);
        PropertyValuesHolder alphaHolder = PropertyValuesHolder.ofFloat("alpha", 0, 1);
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(view, translationYHolder, alphaHolder);
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                view.setVisibility(View.VISIBLE);
            }
        });
        return objectAnimator;
    }

    /**
     * 位移
     *
     * @param fromx
     * @param tox
     * @param fromy
     * @param toy
     * @param duration
     * @param isFillAfter
     * @return
     */
    public static TranslateAnimation getTranslateAnim(float fromx, float tox, float fromy, float toy, int duration, boolean isFillAfter) {
        TranslateAnimation anim = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                fromx,
                Animation.RELATIVE_TO_SELF,
                tox,
                Animation.RELATIVE_TO_SELF,
                fromy,
                Animation.RELATIVE_TO_SELF,
                toy);
        anim.setDuration(duration);
        anim.setFillAfter(isFillAfter);
        anim.setInterpolator(new DecelerateInterpolator());
        anim.start();
        return anim;
    }

    /**
     * view translate Animation
     *
     * @param view
     * @param isShow
     */
    public static void showAnimView(View view, boolean isShow) {
        view.clearAnimation();
        if (isShow)
            view.setAnimation(AppUtil.getTranslateAnim(0.0f, 0.0f, 1.0f, 0.0f, 300, true));
        else
            view.setAnimation(AppUtil.getTranslateAnim(0.0f, 0.0f, 0.0f, 1.0f, 300, true));
    }

    /**
     * 位移动画 从下往上
     *
     * @param view
     */
    public static Animation BotomdownToupTransAnim(View view, AnimationListener animListener) {
        if (view == null)
            return null;
        view.clearAnimation();
        Animation anim = AppUtil.getTranslateAnim(0.0f, 0.0f, 1.00f, 0.0f, 400, true);
        anim.setAnimationListener(animListener);
        view.setAnimation(anim);
        return  anim;
    }

    /**
     * 位移动画 从下往上
     *
     * @param view
     */
    public static void BotomdownToupTransAnim(View view, AnimationListener animListener, long delay) {
        if (view == null)
            return;
        view.clearAnimation();
        Animation anim = AppUtil.getTranslateAnim(0.0f, 0.0f, 1.0f, 0.0f, 400, true);
        anim.setAnimationListener(animListener);
        view.setAnimation(anim);
    }

    /**
     * 位移动画 从上往下
     *
     * @param view
     */
    public static Animation BottomupToDownTransAnim(View view, AnimationListener animListener) {
        if (view != null) {
            view.clearAnimation();
            Animation anim = AppUtil.getTranslateAnim(0.0f, 0.0f, 0.0f, 1.0f, 400, true);
            anim.setAnimationListener(animListener);
            anim.setInterpolator(new AccelerateDecelerateInterpolator());
            view.setAnimation(anim);
            return anim;
        }

        return null;
    }

    /**
     * 位移动画 从下往上
     *
     * @param view
     */
    public static void AbovedownToupTransAnim(View view, AnimationListener animListener) {
        view.clearAnimation();
        Animation anim = AppUtil.getTranslateAnim(0.0f, 0.0f, 0.0f, -1.0f, 400, false);
        anim.setAnimationListener(animListener);
        view.setAnimation(anim);
    }

    /**
     * 位移动画 从上往下
     *
     * @param view
     */
    public static void AboveupToDownTransAnim(View view, AnimationListener animListener) {
        view.clearAnimation();
        Animation anim = AppUtil.getTranslateAnim(0.0f, 0.0f, -1.0f, 0.0f, 400, true);
        anim.setAnimationListener(animListener);
        view.setAnimation(anim);
    }

    /**
     * 从左到右
     *
     * @param view
     * @param animListener
     */
    public static void leftToRightTransAnim(View view, float fromx, float tox, AnimationListener animListener) {
        view.clearAnimation();
        Animation anim = AppUtil.getTranslateAnim(fromx, tox, 0.0f, 0.0f, 300, true);
        anim.setAnimationListener(animListener);
 //       view.setAnimation(anim);
        view.startAnimation(anim);
    }

    /**
     * 从右到左
     *
     * @param view
     * @param animListener
     */
    public static void rightToLeftTransAnim(View view, float fromx, float tox, AnimationListener animListener) {
        view.clearAnimation();
        Animation anim = AppUtil.getTranslateAnim(fromx, tox, 0.0f, 0.0f, 300, true);
        anim.setAnimationListener(animListener);
//        view.setAnimation(anim);
        view.startAnimation(anim);
    }

    /**
     * 0 to 180 rotate animation
     *
     * @param v
     */
    public static void rotateFrom0to180(View v, boolean isFillAfter, AnimationListener animListener) {
        RotateAnimation anim = new RotateAnimation(0, 180,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(300);
        anim.setFillAfter(isFillAfter);
        anim.setAnimationListener(animListener);
        v.startAnimation(anim);
    }

    /**
     * 180 to 0 rotate animation
     *
     * @param v
     */
    public static void rotateFrom180to0(View v, boolean isFillAfter, AnimationListener animListener) {
        RotateAnimation anim = new RotateAnimation(180, 0,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(300);
        anim.setFillAfter(isFillAfter);
        anim.setAnimationListener(animListener);
        v.startAnimation(anim);
    }

    /**
     * 透明度动画
     *
     * @param v
     * @param fromAlpha
     * @param toAlpha
     * @param isFillAfter
     * @param animListener
     */
    public static void alphaAnim(View v, float fromAlpha, float toAlpha, boolean isFillAfter, AnimationListener animListener) {
        AlphaAnimation alphaAnim = new AlphaAnimation(fromAlpha, toAlpha);
        alphaAnim.setFillAfter(isFillAfter);
        alphaAnim.setDuration(500);
        alphaAnim.setAnimationListener(animListener);
        v.startAnimation(alphaAnim);
    }


}
