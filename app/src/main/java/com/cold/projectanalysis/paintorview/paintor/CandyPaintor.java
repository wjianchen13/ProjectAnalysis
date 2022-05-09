package com.cold.projectanalysis.paintorview.paintor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ThumbnailUtils;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

import androidx.core.content.ContextCompat;

import com.cold.projectanalysis.R;
import com.cold.projectanalysis.paintorview.ApplicationUtilV2;
import com.cold.projectanalysis.paintorview.PhivePaintorView;
import com.cold.projectanalysis.paintorview.bubble.LiveUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2016-09-12.
 */
public class CandyPaintor implements IPaint, OnCandyEffectAnimListener {
    private static final int MSG_CANDY_CLICK = 1;       //点击糖果后
    private static final int MSG_CANDY_PROCS = 2;      //推进进度
    private static final int MSG_CANDY_PRAISE = 3;        //如果礼物已就绪，就送出

    private PhivePaintorView mPhivePaintorView;
    private Context mContext;

    private final int CIRCLE_SPREAD = 16;               //扩散的圆每1秒增加的半径,dp
    private final int CICLE_ALPHA = 200;            //扩散的圆每1秒alpha变化
    private final int CIRCLE_GAP = 17;              //两个圆半径相差多少进入下一个圆,dp
    private final int BG_ANGLE = 50;                //中心背景1秒变化角度
    private final int BG_ANGLE_WAITTING = 20;     //等待时中心背景1秒变化角度
    private int CANDY_TIME = 1000 * 20;       //获得糖果的时间
    private final boolean CANDY_INIT = false;       //初始是否有糖果

    private List<PhiveFreeGiftListener> mPhiveFreeGiftListeners = new ArrayList<PhiveFreeGiftListener>();

    private Rect mSurfaceRect;  //画图区域
    private boolean isWorking = false;                      //是否工作中
    private LiveUtil.AnimEnum visib = LiveUtil.AnimEnum.AE_IN_VISIABLE;                         //初始不可见

    /**
     * 用于计算
     */
    private Candy mCandy;
    private float BG_ANGLE_SPEED;               //单位睡眠时间中心背景旋转角度
    private float BG_ANGLE_WAITTING_SPEED;   //等待时单位睡眠时间中心背景旋转角度
    private float WAITTING_ARC_ANGLE_SPEED;     //等待时圆弧角度速率
    private float CIRCLE_SPREAD_SPEED;          //圆的扩散速度,px
    private float CICLE_ALPHA_SPEED;            //圆的alpha速度
    private int mCriclGap ;

    private boolean isCountOut = true;         //是否已经送完糖果

//    private UpdateSizeListener mUpdateSizeListener;
    private List<UpdateSizeListener> mUpdateSizeListeners = new ArrayList<UpdateSizeListener>();

    private int candyBmSize ;

    public CandyPaintor(PhivePaintorView paintorView, Context mContext) {

        this.mPhivePaintorView = paintorView;
        this.mContext = mContext;
        mCriclGap = ApplicationUtilV2.dip2px(mContext, CIRCLE_GAP);
        init();
    }

    private void init() {
        candyBmSize = mContext.getResources().getDimensionPixelOffset(R.dimen.phive_candy_bmsize);
        mCandy = new Candy();
//        mCandy.candyColorful = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.phive_freegift_center_candy);
//        mCandy.candyGray = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.phive_freegift_center_candy_gray);
//        mCandy.centerBg = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.phive_freegift_center_bg);

        mCandy.candyColorful = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.phive_freegift_center_candy)
                , candyBmSize
                , candyBmSize);
        mCandy.candyGray = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.phive_freegift_center_candy_gray)
                , candyBmSize
                , candyBmSize);
        mCandy.centerBg = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.phive_freegift_center_bg)
                , candyBmSize
                , candyBmSize);

        mCandy.circleColor = ContextCompat.getColor(mContext, R.color.phive_freegift_circle);
        mCandy.waittingCircleColor = ContextCompat.getColor(mContext, R.color.phive_freegift_circle_waitting);
        setORC();       //设置部分初始化数据
    }

    /**
     * 糖果时间改变会被再次调用
     */
    public void setORC() {
        mCandy.radiu = mCandy.centerBg.getWidth() / 2f;
        mCandy.circleWidth = mContext.getResources().getDimensionPixelOffset(R.dimen.phive_freegift_circle_width);

        BG_ANGLE_SPEED = BG_ANGLE * (mPhivePaintorView.SLEEP_TIME / 1000f);
        BG_ANGLE_WAITTING_SPEED = BG_ANGLE_WAITTING * (mPhivePaintorView.SLEEP_TIME / 1000f);
        WAITTING_ARC_ANGLE_SPEED = ((float) mPhivePaintorView.SLEEP_TIME / CANDY_TIME) * 360f;

        CIRCLE_SPREAD_SPEED = ApplicationUtilV2.dip2px(mContext, CIRCLE_SPREAD) * (mPhivePaintorView.SLEEP_TIME / 1000f);
        CICLE_ALPHA_SPEED = CICLE_ALPHA * (mPhivePaintorView.SLEEP_TIME / 1000f);
        if (CICLE_ALPHA_SPEED <= 0) {
            CICLE_ALPHA_SPEED = 1;
        }
    }

    @Override
    public DrawItem getDrawItem() {
        if (!isWorking) return null;    //停止状态不计算，不绘图
        //不可见，只计算不绘图
        if (visib == LiveUtil.AnimEnum.AE_IN_VISIABLE) {
            return null;
        }
        Frame frame = new Frame(mCandy, null);
        //计算扇形和圆半径
        if (mCandy.ready) {
            mCandy.shapAngle += BG_ANGLE_SPEED;
            if (mCandy.shapAngle > 360f) {
                mCandy.shapAngle = 360f - mCandy.shapAngle;
            }

            //扩散
            if (!mCandy.candySubs.isEmpty()) {
                Iterator<CandySub> iterator = mCandy.candySubs.iterator();

                while (iterator.hasNext()) {
                    CandySub cs = iterator.next();
                    cs.currAlpha(mCandy);
                    if (cs.alpha <= PhivePaintorView.ALPHA_MIN) {
                        mCandy.candySubs.remove(cs);
                        iterator = mCandy.candySubs.iterator();
                        continue;
                    }
                    cs.currPosition(mCandy);
                    addCircl(frame, cs);
                }

                if (!mCandy.candySubs.isEmpty()) {
                    CandySub cs = mCandy.candySubs.getLast();
                    if (cs.radiu > mCandy.radiu + mCriclGap) {
                        mCandy.candySubs.addLast(new CandySub(mCandy.radiu, PhivePaintorView.ALPHA_MAX));
                    }
                } else {
                    mCandy.candySubs.addLast(new CandySub(mCandy.radiu, PhivePaintorView.ALPHA_MAX));
                }
            } else {
                mCandy.candySubs.addLast(new CandySub(mCandy.radiu, PhivePaintorView.ALPHA_MAX));
            }
        } else {
            mCandy.shapAngle += BG_ANGLE_WAITTING_SPEED;
            if (mCandy.shapAngle > 360f) {
                mCandy.shapAngle = 360f - mCandy.shapAngle;
            }

            mCandy.angle += WAITTING_ARC_ANGLE_SPEED;
            if (mCandy.angle >= 360f) {
                mCandy.angle = 0;
                mCandy.candySubs.clear();
                mCandy.ready = true;
                if (!mPhiveFreeGiftListeners.isEmpty()) {
                    mPhivePaintorView.post(new Runnable() {
                        @Override
                        public void run() {
                            for (PhiveFreeGiftListener listener : mPhiveFreeGiftListeners) {
                                if(listener != null) {
                                    listener.onFreeGiftReady();
                                }
                            }
                        }
                    });
                }
            }
        }

        return new DrawItem(this, frame);
    }

    private void addCircl(Frame frame, CandySub cs) {
        if (frame == null || cs == null) return;
        if (frame.candySubsDraw == null) frame.candySubsDraw = new LinkedList<CandySub>();
        frame.candySubsDraw.addLast(cs.copy());
    }

    private boolean isClickOnCandy(MotionEvent motionEvent) {
        Rect rect = new Rect(Float.valueOf(mCandy.centerP.x - mCandy.centerBg.getWidth() / 2f).intValue()
                , Float.valueOf(mCandy.centerP.y - mCandy.centerBg.getHeight() / 2f).intValue()
                , Float.valueOf(mCandy.centerP.x + mCandy.centerBg.getWidth() / 2f).intValue()
                , Float.valueOf(mCandy.centerP.y + mCandy.centerBg.getHeight() / 2f).intValue());
        return rect.contains((int) motionEvent.getX(), (int) motionEvent.getY());
    }

    public boolean isCandyReady() {
        return mCandy.ready;
    }

    @Override
    public void draw(Canvas canvas, Paint paint, Object f) {

        Frame frame = (Frame) f;
        if (canvas == null || paint == null || frame == null || frame.candy == null) return;
        Candy candy = frame.candy;

        float halfBgWidth = candy.centerBg.getWidth() / 2f;
        float halfCandyWidth = candy.candyGray.getWidth() / 2f;

        canvas.save();
        canvas.rotate(candy.shapAngle, candy.centerP.x, candy.centerP.y);
        canvas.drawBitmap(candy.centerBg
                , candy.centerP.x - halfBgWidth
                , candy.centerP.y - halfBgWidth
                , paint);
        canvas.restore();   //恢复继续画其他

        if (candy.ready) {        //就绪状态
            canvas.drawBitmap(candy.candyColorful
                    , candy.centerP.x - halfCandyWidth
                    , candy.centerP.y - halfCandyWidth
                    , paint);

            drawnCirle(canvas, paint, candy.circleColor, candy.circleWidth, halfBgWidth, candy.centerP.x, candy.centerP.y);

            //扩散
            if (frame.candySubsDraw != null && !frame.candySubsDraw.isEmpty()) {
               // _95L.e("Candy", frame.candySubsDraw.size()+"");
                Iterator<CandySub> iterator = frame.candySubsDraw.iterator();
                while (iterator.hasNext()) {
                    CandySub cs = iterator.next();
                    paint.setColor(candy.circleColor);
                    paint.setStrokeWidth(candy.circleWidth);
                    paint.setAlpha(cs.alpha);

                    canvas.drawCircle(candy.centerP.x, candy.centerP.y, cs.radiu, paint);
                }
                paint.setAlpha(PhivePaintorView.ALPHA_MAX);
            }
        } else {                  //等待状态
            canvas.drawBitmap(candy.candyGray
                    , candy.centerP.x - halfCandyWidth
                    , candy.centerP.y - halfCandyWidth
                    , paint);

            //固定圆环
            drawnCirle(canvas, paint, candy.waittingCircleColor, candy.circleWidth, halfBgWidth, candy.centerP.x, candy.centerP.y);

            //圆弧进度
            RectF oval = new RectF();                     //RectF对象
            oval.left = candy.centerP.x - halfBgWidth;                              //左边
            oval.top = candy.centerP.y - halfBgWidth;                                   //上边
            oval.right = candy.centerP.x + halfBgWidth;                             //右边
            oval.bottom = candy.centerP.y + halfBgWidth;                                //下边
            paint.setColor(candy.circleColor);
            canvas.drawArc(oval, -90, candy.angle, false, paint);    //绘制圆弧
        }
    }

    private void drawnCirle(Canvas canvas, Paint p, int color, int width, float radiu, float x, float y) {
        p.setColor(color);
        p.setStrokeWidth(width);
        canvas.drawCircle(x, y, radiu, p);
    }

    @Override
    public void add(Object obj) {

    }

    @Override
    public void onAnimationStart() {

    }

    @Override
    public void onAnimationEnd() {

    }

    /**
     * 糖果状态
     */
    private class Candy {
        public PointF centerP = new PointF();       //画图中心点

        public int circleColor;                    //圆颜色
        public int waittingCircleColor;            //等待时圆颜色

        public float radiu;     //圆的半径
        public int circleWidth; //圆宽度
        public float angle;     //如果在等待糖果，扇形的角度
        public float shapAngle; //中心角度，360度后清0
        public boolean ready = CANDY_INIT;

        public Bitmap candyColorful;    //有糖果
        public Bitmap candyGray;        //等待糖果
        public Bitmap centerBg;         //背景图

        LinkedList<CandySub> candySubs = new LinkedList<CandySub>();    //所有圆的集合，记录目前的状态

        public void clear() {
            if (candyColorful != null && !candyColorful.isRecycled()) {
                candyColorful.recycle();
            }

            if (candyGray != null && !candyGray.isRecycled()) {
                candyGray.recycle();
            }

            if (centerBg != null && !centerBg.isRecycled()) {
                centerBg.recycle();
            }

            if (candySubs != null && !candySubs.isEmpty()) {
                candySubs.clear();
            }
        }
    }

    /**
     * 绘制的圆形
     */
    private class CandySub {
        public long startTime;
        public long totalTime;
        public float radiu;     //圆的半径
        public int alpha;
        private Interpolator mInterpolator;

        public CandySub(float radiu, int alpha) {
            this.radiu = radiu;
            this.alpha = alpha;
            this.startTime = SystemClock.elapsedRealtime();

            totalTime = getAnimTotalTime();
        }

        public CandySub copy() {
            return new CandySub(this.radiu, this.alpha);
        }

        public void currPosition(Candy candy) {
            if (mPhivePaintorView.getCalculattingThread() == null) {
                radiu = candy == null ? 0 : candy.radiu;
                return;
            }
            radiu = getCurrPosition(candy);
        }

        private long getAnimTotalTime() {
            //alpha决定了时间，计算动画耗时，毫秒
            return mPhivePaintorView.getCalculattingThread() == null ? 0 :
                        (long) ((PhivePaintorView.ALPHA_MAX / CICLE_ALPHA_SPEED) * mPhivePaintorView.SLEEP_TIME);
        }

        private float getProgress() {
            float progress = (SystemClock.elapsedRealtime() - startTime) / (float) totalTime;
            return progress > 1 ? 1 : progress < 0 ? 0 : progress;
        }

        private Interpolator getInterpolator() {
            if (mInterpolator == null) {
                mInterpolator = new DecelerateInterpolator();
            }
            return mInterpolator;
        }

        private float getCurrPosition(Candy candy) {
            //先快后慢方式
            float progress = getProgress();
            float rate = getInterpolator().getInterpolation(progress);
            return candy.radiu + rate * totalTime / mPhivePaintorView.SLEEP_TIME * CIRCLE_SPREAD_SPEED;
        }

        public void currAlpha(Candy candy) {
            if (mPhivePaintorView.getCalculattingThread() == null) {
                alpha = PhivePaintorView.ALPHA_MIN;
                return;
            }
            alpha = getCurrAlpha(candy);
        }

        private int getCurrAlpha(Candy candy) {
            float progress = getProgress();
            float rate = getInterpolator().getInterpolation(progress);
            return new BigDecimal(PhivePaintorView.ALPHA_MAX * (1 - rate)).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
        }
    }

    /**
     * 绘制的帧
     */
    private class Frame {
        public Candy candy;
        public LinkedList<CandySub> candySubsDraw;

        public Frame(Candy candy, LinkedList<CandySub> candySubsDraw) {
            this.candy = candy;
            this.candySubsDraw = candySubsDraw;
        }
    }

    @Override
    public void defineMessage(int what, Object obj) {
        switch (what) {
            case MSG_CANDY_CLICK:
                if (mCandy.ready) {
                    mCandy.ready = false;
                    mCandy.candySubs.clear();
                    if (!mPhiveFreeGiftListeners.isEmpty()) {
                        mPhivePaintorView.post(new Runnable() {
                            @Override
                            public void run() {
                                for (PhiveFreeGiftListener listener : mPhiveFreeGiftListeners) {
                                    if(listener != null) {
                                        listener.onFreeGiftStartCountting();
                                    }
                                }
                            }
                        });
                    }
                }
                break;
            case MSG_CANDY_PROCS:
                int time = (int) obj;
                if(mCandy != null) {
                    mCandy.angle += ((float) time / CANDY_TIME) * 360f;;
                }
                break;
            case MSG_CANDY_PRAISE:
                performClickCandy(true);
                break;
        }
    }

    //这个方法只有在最开始初始化textureview的时候调用一次
    @Override
    public void updateSize(int width, int height) {
        System.out.println("----------------------> updateSize width: " + width + "   height: " + height);
        if(width > 0 && height > 0) {
            if (mSurfaceRect == null) mSurfaceRect = new Rect();
            int[] messageLocation = mPhivePaintorView.getPhiveMessageScreenLocation();
//            View shortcutView = mPhivePaintorView.getGiftShortcutView();
            int[] shortcutLocation = mPhivePaintorView.getGiftShortcutLocation();
            if (shortcutLocation != null && messageLocation != null) {
                int scWidth = 132;
                int scHeigh = 132;

                int scCenterX = shortcutLocation[0] + scWidth / 2;
//            int scCenterY = shortcutLocation[1] + scHeigh / 2;

                int[] location = new int[2];
                mPhivePaintorView.getLocationOnScreen(location);

                int candySize = mContext.getResources().getDimensionPixelOffset(R.dimen.phive_candy_size);
                //分有暖场和没暖场两个尺寸
                int shortcutOffsetY = mContext.getResources().getDimensionPixelOffset(mUpdateSizeListeners.isEmpty() ?
                        R.dimen.phive_bubble_shortcut_offsetY : R.dimen.phive_bubble_shortcut_offsetY2);

                int topSub = 0;
                if (mCandy != null && mCandy.centerBg != null) {
                    topSub = (candySize - mCandy.centerBg.getWidth()) / 2;
                }

                int left = scCenterX - location[0] - candySize / 2;
                int top = shortcutLocation[1] - location[1] - shortcutOffsetY - candySize + topSub;

                if ((top + candySize) > mPhivePaintorView.getHeight()) {
                    top = mPhivePaintorView.getHeight() - candySize;
                }

                if (top < 0) {
                    top = 0;
                }
                int right = left + candySize;
                int bottom = top + candySize;
                mSurfaceRect.set(left,
                        top,
                        right,
                        bottom);

//                mCandy.centerP.x = mSurfaceRect.left + candySize / 2f;
//              mCandy.centerP.y = mSurfaceRect.top + candySize / 2f;
                mCandy.centerP.x = 500;
                mCandy.centerP.y = 800;
                notifyUpdateListeners(width, height);
            }
        }
    }

    //通知冒泡位置改变，或者是否可见改变
    private void notifyUpdateListeners(int width, int height){
        System.out.println("----------------------> notifyUpdateListeners width: " + width + "   height: " + height);
        if(!mUpdateSizeListeners.isEmpty()) {
            for (UpdateSizeListener listener : mUpdateSizeListeners) {
                if(listener != null) {

//                    if(mContext instanceof PhiveStudioActivity && listener instanceof PhiveBubbleEffectPaintor) {
//                        continue;
//                    }
//
//                    if(mContext instanceof PhiveStudioActivity && listener instanceof CandyEffectPaintor) {
//                        continue;
//                    }

                    listener.onUpdateSize(isShown() ? mCandy.centerP : null,
                            Float.valueOf(mCandy.radiu).intValue(),
                            width,
                            height);
                }
            }
        }
    }

    private void notifyUpdateListeners(){
        if(mPhivePaintorView != null) {
            notifyUpdateListeners(mPhivePaintorView.getWidth(), mPhivePaintorView.getHeight());
        }
    }

    public void start() {
//        if(mContext instanceof PhiveStudioActivity){
//            return;
//        }
        isWorking = true;
        notifyUpdateListeners();
    }

    public void stop() {
        isWorking = false;
        notifyUpdateListeners();
    }

    //额外进度添加,time=毫秒
    public void advance(int time){
        if (isWorking && !isCountOut && mCandy != null && !mCandy.ready && mPhivePaintorView != null)
            mPhivePaintorView.sendDefineMessage(new DefineMessage(this, MSG_CANDY_PROCS, time));
    }

    //发出糖果，如果已经就绪
    public void sendIfReady(){
        if(isWorking && !isCountOut && mCandy != null && mCandy.ready){
            mPhivePaintorView.sendDefineMessage(new DefineMessage(this, MSG_CANDY_PRAISE, null));
        }
    }

    public void setVisib(LiveUtil.AnimEnum visib) {
        if(isCountOut && this.visib == LiveUtil.AnimEnum.AE_VISIABLE) {
            return;
        }
//        if(mContext instanceof PhiveStudioActivity && visib == LiveUtil.AnimEnum.AE_VISIABLE) {
//            return;
//        }
        this.visib = visib;
        notifyUpdateListeners();
    }

    public LiveUtil.AnimEnum getVisib(){
        return this.visib;
    }

    public boolean isShown(){
        return !(visib != LiveUtil.AnimEnum.AE_VISIABLE || !isWorking || isCountOut);
    }

    public void addPhiveFreeGiftListener(PhiveFreeGiftListener listener) {
        if(listener == null) return;

        if(mPhiveFreeGiftListeners != null) {
            mPhiveFreeGiftListeners.add(listener);
        }
    }

    public void removePhiveFreeGiftListener(PhiveFreeGiftListener listener) {
        if(listener == null) return;

        if(mPhiveFreeGiftListeners != null) {
            mPhiveFreeGiftListeners.remove(listener);
        }
    }

    //改变糖果恢复时间
    public void setCandyTime(int candyTime) {
        this.CANDY_TIME = candyTime;
        setORC();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        //判断是否点中糖果
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN
                && mPhivePaintorView != null) {
            if (isShown() && isClickOnCandy(motionEvent)) {
                performClickCandy(false);
                return true;
            }
        }
        return false;
    }

    private void performClickCandy(final boolean fromPraise){
//        if(fromPraise && !AppUser.getInstance().isLogin()) {
//            return;
//        }

        if(!mPhiveFreeGiftListeners.isEmpty() && mPhivePaintorView != null && mPhivePaintorView.isLogin() ) {
            mPhivePaintorView.post(new Runnable() {
                @Override
                public void run() {
                    for (PhiveFreeGiftListener listener : mPhiveFreeGiftListeners) {
                        if(listener != null) {
                            listener.onCandyClick(isCandyReady(), fromPraise);
                        }
                    }
                }
            });
        }
    }

    //开始倒计时进入等待状态
    public void startBackwards(){
        if (mPhivePaintorView != null)
            mPhivePaintorView.sendDefineMessage(new DefineMessage(this, MSG_CANDY_CLICK, null));
    }

    @Override
    public void clear() {
        if (mCandy != null) {
            mCandy.clear();
        }
        if(mUpdateSizeListeners != null
                && !mUpdateSizeListeners.isEmpty()){
            mUpdateSizeListeners.clear();
        }

        if(mPhiveFreeGiftListeners != null) {
            mPhiveFreeGiftListeners.clear();
        }

        CandyEffectPaintor candyEffectPaintor = mPhivePaintorView.getCandyEffectPaintor();
        if(candyEffectPaintor != null) {
            candyEffectPaintor.removeAnimationListener(this);
        }
    }

    public boolean isCountOut() {
        return isCountOut;
    }

    public void setCountOut(boolean countOut) {
        isCountOut = countOut;
        notifyUpdateListeners();
    }

    public void addUpdateSizeListener(UpdateSizeListener listener) {
        if(listener == null) return;

        if(mUpdateSizeListeners != null) {
            mUpdateSizeListeners.add(listener);
        }
    }

    public void removeUpdateSizeListener(UpdateSizeListener listener) {
        if(listener == null) return;

        if(mUpdateSizeListeners != null) {
            mUpdateSizeListeners.remove(listener);
        }
    }

    /**
     * interface
     */
    public static interface PhiveFreeGiftListener {
        void onFreeGiftReady();                 //免费礼物就绪

        void onFreeGiftStartCountting();        //开始倒数获得糖果

        void onCandyClick(boolean isCandyReady, boolean fromPraise);
    }

    //主要用于冒泡监听糖果位置做对齐
    public static interface UpdateSizeListener{
        void onUpdateSize(PointF centerP, int offsetY, int width, int height);
    }
}
