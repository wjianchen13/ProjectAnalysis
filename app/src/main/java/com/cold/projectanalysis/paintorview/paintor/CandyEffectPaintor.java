package com.cold.projectanalysis.paintorview.paintor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.View;

import com.cold.projectanalysis.R;
import com.cold.projectanalysis.paintorview.ApplicationUtilV2;
import com.cold.projectanalysis.paintorview.PhivePaintorView;

import java.util.ArrayList;
import java.util.List;

/**
 * 点击糖果的特效
 * Created by Administrator on 2016-11-29.
 */
public class CandyEffectPaintor implements IPaint, CandyPaintor.UpdateSizeListener, CandyPaintor.PhiveFreeGiftListener {

    private static final String TAG = CandyEffectPaintor.class.getName();

    private List<OnCandyEffectAnimListener> onAnimationListeners = new ArrayList<OnCandyEffectAnimListener>();
    private PhivePaintorView mPhivePaintorView;
    private Context mContext;

    private CandyPaintor mCandyPaintor;

    private PointF mCenterP;

    private Animation mAnimation;       //如果有值表示动画在运行中

//    private PhiveRoomActivity mActivity;

    public CandyEffectPaintor(PhivePaintorView phivePaintorView, Context context) {
        this.mPhivePaintorView = phivePaintorView;
        this.mContext = context;

//        if(mContext instanceof PhiveRoomActivity) {
//            mActivity = (PhiveRoomActivity) mContext;
//        }

        this.mCandyPaintor = phivePaintorView.getCandyPaintor();
        this.mCandyPaintor.addUpdateSizeListener(this);
        this.mCandyPaintor.addPhiveFreeGiftListener(this);
        addAnimationListener(mCandyPaintor);
    }

    /******************************************************************************
     *
     * override
     *
     ******************************************************************************/
    @Override
    public DrawItem getDrawItem() {

//        if(mAnimation != null && mAnimation.isOver) {
//            mAnimation.clear();
//            mAnimation = null;
//        }

        return mAnimation == null ? null : new DrawItem(this, mAnimation);
    }

    @Override
    public void draw(Canvas canvas, Paint paint, Object frame) {
        PointF center = mCenterP;
        if(center == null) return;

        Animation animation = (Animation) frame;

        if(animation == null || animation.isRealeased) return;

        if(animation.startTime == 0) {
            animation.startTime = System.currentTimeMillis();
//            _95L.i(TAG, "首次画图...");
            mPhivePaintorView.post(new Runnable() {
                @Override
                public void run() {
                    //回调动画开始
                    if(!onAnimationListeners.isEmpty()) {
                        for(OnCandyEffectAnimListener listener : onAnimationListeners) {
                            if(listener != null) {
                                listener.onAnimationStart();
                            }
                        }
                    }
                }
            });
        }

        //根据时间判断绘制的帧
        Frame f = getFrameByTime(animation);
//        _95L.i(TAG, "获得帧  --->" + f);
        if(animation.isOver) {
            mAnimation.clear();
            mAnimation = null;
//            _95L.i(TAG, "动画结束 isRealeased ---> " + mAnimation.isRealeased + " isOver --->" + mAnimation.isOver);
            mPhivePaintorView.post(new Runnable() {
                @Override
                public void run() {
                    //超过最后的时间，结束动画，回调动画结束
                    if(!onAnimationListeners.isEmpty()) {
                        for(OnCandyEffectAnimListener listener : onAnimationListeners) {
                            if(listener != null) {
                                listener.onAnimationEnd();
                            }
                        }
                    }
                }
            });
            return;
        }

        if(f == null) {
//            _95L.i(TAG, "没有可用的帧，下一帧  return--->" );
            return;
        }

        Bitmap candyBitmap = f.getCandyBitmap();
        if(candyBitmap != null) {        //先画糖果
//            _95L.i(TAG, "画糖果  --->" + candyBitmap);
            canvas.drawBitmap(candyBitmap
                    , center.x - candyBitmap.getWidth() / 2
                    , center.y - candyBitmap.getHeight() / 2
                    , paint);
        }

        if(f.bitmap != null) {          //再画特效
//            _95L.i(TAG, "画特效  --->" + f.bitmap);
            canvas.drawBitmap(f.bitmap
                    , center.x - f.bitmap.getWidth() / 2
                    , center.y - f.bitmap.getHeight() / 2
                    , paint);
        }
    }

    @Override
    public void add(Object obj) {

    }

    @Override
    public void updateSize(int width, int height) {

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void defineMessage(int what, Object obj) {

    }

    @Override
    public void onUpdateSize(PointF centerP, int offsetY, int width, int height) {
        this.mCenterP = centerP;
    }

    @Override
    public void onFreeGiftReady() {}

    @Override
    public void onFreeGiftStartCountting() {}

    @Override
    public void onCandyClick(boolean isCandyReady, boolean fromPraise) {
        if(mCandyPaintor != null && !mCandyPaintor.isShown()) return;

        if(!isCandyReady && fromPraise) return;

        boolean isWaitting = false;

//        if(mActivity != null && mActivity.getPhiveCandy() != null) {
//            isWaitting = mActivity.getPhiveCandy().isWaitting();
//        }

        if(mAnimation == null) {   //开始执行动画
            mAnimation = new Animation(isCandyReady && !isWaitting);
        }
    }

    @Override
    public void clear() {
        if(mAnimation != null) {
            mAnimation.clear();
        }

        if(mCandyPaintor != null) {
            mCandyPaintor.removePhiveFreeGiftListener(this);
            mCandyPaintor.removeUpdateSizeListener(this);
        }
    }

    /******************************************************************************
     *
     * methods
     *
     ******************************************************************************/
    public Frame getFrameByTime(Animation animation){
        if(animation != null) {

            Frame[] frames = animation.frames;
            long startTime = animation.startTime;
            long curTime = System.currentTimeMillis();

            long pastTime = curTime - startTime;
            if(pastTime > animation.totalTime) {
//                _95L.i(TAG, "动画超过总时间  --->" + animation.totalTime + " 动画结束");
                animation.isOver = true;
                return null;
            }

            for (int i = 0; i < frames.length; i++) {
                Frame f = frames[i];
                if (pastTime <= f.subTime) {
//                    _95L.i(TAG, "获得可用的帧  --->" + f);
                    return f;
                }
            }
        }
//        _95L.i(TAG, "没有可用的帧，返回null  --->");
        return null;
    }

    public void addAnimationListener(OnCandyEffectAnimListener listener) {
        if(listener == null) return;

        if(onAnimationListeners != null) {
            onAnimationListeners.add(listener);
        }
    }

    public void removeAnimationListener(OnCandyEffectAnimListener listener) {
        if(listener == null) return;

        if(onAnimationListeners != null) {
            onAnimationListeners.remove(listener);
        }
    }

    /******************************************************************************
     *
     * interface
     *
     ******************************************************************************/
    public class Animation{
        public boolean isOver;
        public boolean isRealeased;
        public long startTime;
        public Bitmap hugCandyBm;
        public int totalTime;
        public boolean isReady;     //糖果是否就绪
        public Frame[] frames ;

        public Animation(boolean isReady){
            this.isReady = isReady;
            hugCandyBm = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.candy_effect_hugcandy);

            frames = isReady ? new Frame[]{
                    new Frame(this, R.drawable.candy_effect_fram1, 0, 80),
                    new Frame(this, R.drawable.candy_effect_fram2, 45, 80),
                    new Frame(this, R.drawable.candy_effect_fram3, 69, 80),
                    new Frame(this, R.drawable.candy_effect_fram4, 67, 40),
                    new Frame(this, 0, 63, 60),
                    new Frame(this, 0, 50, 50),
                    new Frame(this, 0, 28, 50)
            } : new Frame[]{
                    new Frame(this, R.drawable.candy_effect_fram1, 0, 80),
                    new Frame(this, R.drawable.candy_effect_fram2, 0, 80),
                    new Frame(this, R.drawable.candy_effect_fram3, 0, 80),
                    new Frame(this, R.drawable.candy_effect_fram4, 0, 40)
            };

            for (int i = 0; i < frames.length; i++ ){
                totalTime += frames[i].duration;
                frames[i].subTime = totalTime;
                frames[i].index = i;
            }
        }

        public void clear(){
            isRealeased = true;

            if(hugCandyBm != null && !hugCandyBm.isRecycled()) {
                hugCandyBm.recycle();
            }

            if (frames != null && frames.length > 0) {
                for (int i = 0; i < frames.length; i++) {
                    Frame f = frames[i];
                    if (f != null) f.clear();
                }
            }
        }
    }

    private class Frame{
        public Animation animation;
        public int bitmapId;
        public Bitmap bitmap;
        public Bitmap candyBitmap;
        public int candySizeDP;
        public int subTime;         //连同自身消耗时间，到这个项为止，之前全部项累积时间的和
        public int duration;        //持续时间
        public int index;           //序号

        public Frame(Animation animation, int bitmapId, int candySizeDP, int duration) {
            this.animation = animation;
            this.bitmapId = bitmapId;
            this.candySizeDP = candySizeDP;
            this.duration = duration;

            init();
        }

        private void init(){
            if(this.bitmapId != 0) {
                bitmap = BitmapFactory.decodeResource(mContext.getResources(), this.bitmapId);
            }
        }

        public Bitmap getCandyBitmap(){
            if(candyBitmap == null && this.candySizeDP != 0) {
                int size = ApplicationUtilV2.dip2px(mContext, candySizeDP);
                candyBitmap = Bitmap.createScaledBitmap(animation.hugCandyBm, size, size, true);
            }

            return candyBitmap;
        }

        public void clear(){
            if(bitmap != null && !bitmap.isRecycled()){
                bitmap.recycle();
            }

            if(candyBitmap != null && !candyBitmap.isRecycled()) {
                candyBitmap.recycle();
            }
        }
    }
}
