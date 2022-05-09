package com.cold.projectanalysis.paintorview.bubble;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.media.ThumbnailUtils;
import android.os.Handler;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;

import com.cold.projectanalysis.R;
import com.cold.projectanalysis.paintorview.ApplicationUtilV2;
import com.cold.projectanalysis.paintorview.PhivePaintorView;
import com.cold.projectanalysis.paintorview.paintor.CandyPaintor;
import com.cold.projectanalysis.paintorview.paintor.DefineMessage;
import com.cold.projectanalysis.paintorview.paintor.IPaint;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * 冒泡效果，手机直播间暖场
 * Created by Administrator on 2016-11-08.
 */
public class PhiveBubbleEffectPaintor implements IPaint, CandyPaintor.UpdateSizeListener {

    private static final String TAG =  PhiveBubbleEffectPaintor.class.getName();

    private static final int MSG_VISIB = 1;     //延时设置可见
    private static final int MSG_CLEAR = 2;     //清空所有泡泡

    /**
     * 速度透明度调节
     */
    private static final float BUBBLE_INIT_RATE = 0.2f;
    private static final float BUBBLE_INIT_ALPHA = 0.8f;
    private static final float BUBBLE_PATH_SPEED = 90f;    //1秒移动的基准速度，dp
    private static final float BUBBLE_PATH_SPEED_SCOPE = 45f;  //路径速度上下浮动，每秒,dp
    private static final float BUBBLE_SIZE_TIME_RATE = 0.26f;     //在总时间开始多久比例内缩放完成
    private static final float BUBBLE_ALPHA_TIME_RATE = 0.3333f; //在经过多长的总时间比例后开始对alpha做处理

    /**
     * 随机路径控制
     */
//    private static final int CTR_WIDTH = 3;     //控制点落在宽度的多少分之1的正方形内
//    private static final float VER_LEN = 3f;    //竖直方向随机线的长度比例占总宽度
    private static final int MIDDLE_BUBBLE = 2; //出现中间的气泡分数，至少保留比例，剩下的比例随机
    private static final int TOTAL_BUBBLE = 3;  //总分数

    /**
     * 状态
     */
    private boolean isWorking = false;     //是否运行, 不接收数据，无工作状态，不计算，不绘图

    private PhivePaintorView mPhivePaintorView;
    private Context mContext;

    private Random random = new Random();
    private static  int vir_width ;         //虚拟的宽度，宽度以冒泡点为中点展开
    private static  int vir_heigh;         //虚拟的高度
    private static  int verticalPathLen ;   //垂直方向的路径长度
    private static  int ctr_len;           //控制点正方形宽度
    //如果在糖果上方的y轴额外偏移量，主要防止冒泡跟糖果边际有重叠
    private static  int org_offsetY ;
    private static  int control_offsetY;

    private BubblePath mBubblePath;                 //随机路径管理
    private LinkedList<Bubble> allBubbles = new LinkedList<Bubble>();   //所有泡泡集合
    private List<Bubble> avaliables = new ArrayList<Bubble>();          //需要清除掉的泡泡

    private static  int bubbleSize ;

    //泡泡图片，随机一张
    private Bitmap[] bitmaps;
    //如果是当前用户采用的图片
    private Bitmap mineBitmap ;

    //首次初始化时默认的出生点锚点view
    private View defaultAnchorView;
    private Point defaultPoint;     //默认出生点

    private LiveUtil.AnimEnum visib = LiveUtil.AnimEnum.AE_VISIABLE;                         //是否可见

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case MSG_VISIB:
                    visib = LiveUtil.AnimEnum.AE_VISIABLE;
                    break;
            }
        }
    };

    public PhiveBubbleEffectPaintor(PhivePaintorView paintorView, Context mContext){
        this.mPhivePaintorView = paintorView;
        this.mContext = mContext;
        paintorView.getCandyPaintor().addUpdateSizeListener(this);
        init();
    }

    //初始以送礼快捷键/关闭按钮位置冒泡
    private void init(){
        vir_width = mContext.getResources().getDimensionPixelOffset(R.dimen.phive_bubble_vir_width);         //虚拟的宽度，宽度以冒泡点为中点展开
        vir_heigh = mContext.getResources().getDimensionPixelOffset(R.dimen.phive_bubble_vir_heigh);         //虚拟的高度
        verticalPathLen = mContext.getResources().getDimensionPixelOffset(R.dimen.phive_bubble_verticalPathLen);   //垂直方向的路径长度
        ctr_len = mContext.getResources().getDimensionPixelOffset(R.dimen.phive_bubble_ctr_len);           //控制点正方形宽度
        org_offsetY = mContext.getResources().getDimensionPixelOffset(R.dimen.phive_bubble_origin_offsetY);
        control_offsetY = mContext.getResources().getDimensionPixelOffset(R.dimen.phive_bubble_control_offsetY);
        bubbleSize = mContext.getResources().getDimensionPixelOffset(R.dimen.phive_bubble_size);
        bitmaps = new Bitmap[]{
                ThumbnailUtils.extractThumbnail(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.phive_heart0)
                        , bubbleSize
                        , bubbleSize),

                ThumbnailUtils.extractThumbnail(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.phive_heart1)
                        , bubbleSize
                        , bubbleSize),

                ThumbnailUtils.extractThumbnail(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.phive_heart2)
                        , bubbleSize
                        , bubbleSize),

                ThumbnailUtils.extractThumbnail(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.phive_heart3)
                        , bubbleSize
                        , bubbleSize),

                ThumbnailUtils.extractThumbnail(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.phive_heart4)
                        , bubbleSize
                        , bubbleSize),
        };
        mineBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.phive_heart3);
        getDefaultBirthLand();
    }

    //获得默认的出生地
    private void getDefaultBirthLand(){
//        if(mContext instanceof PhiveBaseActivity) {
//            final PhiveBaseActivity activity = (PhiveBaseActivity) mContext;
//            activity.addOnActivityLoaded(mine PhiveBaseActivity.OnActivityLoaded() {
//                @Override
//                public void activityLoaded() {
//                    if (mContext instanceof PhiveRoomActivity) {     //如果是手机直播间的观众端，以快捷送礼为锚点
//                        defaultAnchorView = ((PhiveRoomActivity) mContext).findViewById(R.id.iv_shortcut_gift);
//                    } else if (mContext instanceof PhiveStudioActivity) {     //如果是录制端，以右下角关闭按钮为锚点
//                        defaultAnchorView = ((PhiveStudioActivity) mContext).findViewById(R.id.iv_exitRoom);
//                    }
//
//                    if (defaultAnchorView != null) {
//                        //计算出生原点
//                        int[] sreenLocation = mine int[2];
//                        defaultAnchorView.getLocationOnScreen(sreenLocation);
//                        int anchorWidth = defaultAnchorView.getWidth();
//
//                        int anchorCenterX = sreenLocation[0] + anchorWidth / 2;
//                        int anchorCenterY = sreenLocation[1];
//
//                        int[] totalPaintorViewLocation = mine int[2];
//                        mPhivePaintorView.getLocationOnScreen(totalPaintorViewLocation);
//
//                        //这是没有糖果，默认出生地时，y的额外偏移，防止有一部分超出view边界没有绘制
//                        //也就是至少离view底部这个值的距离
//                        int offsetY = mContext.getResources().getDimensionPixelOffset(R.dimen.phive_bubble_origin_offsetY_default);
//                        int x = anchorCenterX - totalPaintorViewLocation[0];
//                        int y = anchorCenterY - totalPaintorViewLocation[1];
//
//                        if (x > mPhivePaintorView.getWidth()) x = mPhivePaintorView.getWidth();
//                        if (y > mPhivePaintorView.getHeight()) y = mPhivePaintorView.getHeight();
//
//                        y = y - offsetY;

                        defaultPoint = new Point(500, 800);

                        if (mBubblePath == null && defaultPoint != null) {   //如果不为空证明被糖果抢先初始化
                            mBubblePath = new BubblePath(defaultPoint);
                            isWorking = true;
                        }
//                    }

//                    activity.removeActivityLoaded(this);
//                }
//            });
//       }
    }

    /************************************************************************************************
     *
     * overrides
     *
     ***********************************************************************************************/
    @Override
    public com.cold.projectanalysis.paintorview.paintor.DrawItem getDrawItem() {
        System.out.println("--------------------> !isWorking: " + !isWorking);
        System.out.println("--------------------> visib != LiveUtil.AnimEnum.AE_VISIABLE: " + (visib != LiveUtil.AnimEnum.AE_VISIABLE));
        if(!isWorking || visib != LiveUtil.AnimEnum.AE_VISIABLE) return null;

        avaliables.clear();
        LinkedList<DrawItem> items = null;

        for (int i = 0; i < allBubbles.size(); i++) {
            Bubble bubble = allBubbles.get(i);
            if (bubble != null) {
                DrawItem item = bubble.getDrawItem();
                if (item == null) {
                    if (!avaliables.contains(bubble))
                        avaliables.add(bubble);
                    continue;
                }
                if (items == null) {
                    items = new LinkedList<DrawItem>();
                }
                items.addLast(item);
            }

        }
        allBubbles.removeAll(avaliables);

        return new com.cold.projectanalysis.paintorview.paintor.DrawItem(this, items);
    }

    @Override
    public void draw(Canvas canvas, Paint paint, Object frame) {

        if(canvas == null || paint == null || frame == null) return;

        if(visib != LiveUtil.AnimEnum.AE_VISIABLE) return ;     //如果不可见，不绘制，但是依然计算

        LinkedList<DrawItem> bubbles = (LinkedList<DrawItem>) frame;

        // 首先清空 Canvas
//        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        for (int i = 0; i < bubbles.size(); i++) {
            DrawItem item = bubbles.get(i);
            if (item != null) {
                paint.setAlpha(item.alpha);
//                        drawBitmap(Bitmap bitmap, Rect src, RectF dst, Paint paint)；
//                        Rect src: 是对图片进行裁截，若是空null则显示整个图片
//                        RectF dst：是图片在Canvas画布中显示的区域，
//                        大于src则把src的裁截区放大，
//                        小于src则把src的裁截区缩小。

//                        canvas.drawBitmap(item.bitmap
//                                , item.point.x
//                                , item.point.y
//                                , mPaint);
                if(item.bitmap != null && !item.bitmap.isRecycled()) {
                    canvas.drawBitmap(item.bitmap, null, item.getLoc(), paint);
                }
            }
        }

        paint.setAlpha(mPhivePaintorView.ALPHA_MAX);
    }

    @Override
    public void add(Object obj) {
        if (isWorking && visib == LiveUtil.AnimEnum.AE_VISIABLE && mBubblePath != null) {
            allBubbles.add(new Bubble());
        }
    }

    //改变为监听candy的改变
    @Override
    public void updateSize(int width, int height) {

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void defineMessage(int what, Object obj) {
        switch (what) {
            case MSG_CLEAR:
                allBubbles.clear();
                break;
        }
    }

    //糖果size改变时回调
    @Override
    public void onUpdateSize(PointF centerP, int offsetY, int width, int height) {
        Point p = null;
        if(width > 0 && height > 0){
//            if(mSurfaceRect == null) {
//                mSurfaceRect = mine Rect(0, 0, CODEC_WIDTH, CODEC_HEIGHT);
//            } else
//                mSurfaceRect.set(0, 0, CODEC_WIDTH, CODEC_HEIGHT);
            if(centerP != null) {
                p = new Point(Float.valueOf(centerP.x).intValue(),
                        Float.valueOf(centerP.y - offsetY - org_offsetY).intValue());
            }
        }

        if(p == null) {
            p = defaultPoint;
        }

        if(p != null) {
            isWorking = true;

            if (mBubblePath == null) {
                mBubblePath = new BubblePath(p);
            } else
                mBubblePath.update(p);
        } else {
            //设置为不可见，停止工作
            isWorking = false;
        }
    }
    /************************************************************************************************
     *
     * methods
     *
     ***********************************************************************************************/
//    public void resetBirthland() {
//        if (mBubblePath != null) {
//            mBubblePath.resetBirthland();
//            mBubblePath.resetDisPath();
//        }
//    }

    //生成一个随机的比例,0-1
    private float randRate() {
        return rand(0, 100) / 100f;
    }

    //生成随机数
    private int rand(int min, int max) {
        if(max <= 0) return 0;
        return random.nextInt(max) % (max - min + 1) + min;
    }

    //生成随机路径速度
    private float randPathSpeed(float max) {
        //伪随机数生成器的序列中均匀分布的0.0和1.0之间的float值
        float speed = random.nextFloat() * max;
        if (rand(0, 1) > 0)
            return speed;
        return -speed;
    }

    //随机抓取一张图片
    private Bitmap randBitmap() {
        return bitmaps[rand(0, bitmaps.length - 1)];
    }

    public void start() {    //开始接收数据
        isWorking = true;
    }

    public void stop() {     //不接收数据
        isWorking = false;
    }

    public boolean isRunning() {
        return isWorking;
    }

    public LiveUtil.AnimEnum getVisib() {
        return visib;
    }

    public void setVisib(LiveUtil.AnimEnum visib) {
        if(mHandler != null && mHandler.hasMessages(MSG_VISIB)) {
            mHandler.removeMessages(MSG_VISIB);
        }

        if(visib != LiveUtil.AnimEnum.AE_VISIABLE) {
            this.visib = visib;
        } else {
            if(mPhivePaintorView != null) {
                mPhivePaintorView.sendDefineMessage(new DefineMessage(this, MSG_CLEAR, null));
            }
            if(mHandler != null) mHandler.sendEmptyMessageDelayed(MSG_VISIB, 800);
//            boolean hasMsg = mHandler.hasMessages(MSG_VISIB);
//            _95L.i(TAG, "hasMsg --->" + hasMsg);
        }
    }

    //是否可见
    public boolean isShown(){
        return visib == LiveUtil.AnimEnum.AE_VISIABLE
                || (mHandler != null && mHandler.hasMessages(MSG_VISIB));
    }

    /************************************************************************************************
     *
     * classs
     *
     ***********************************************************************************************/
    /**
     * 描述气泡状态
     */
    private class Bubble {
        private Bitmap bitmap;

        private boolean isMine;     //是否当前用户
        private Path mMovePath;     //移动路线
        private float mSizeRate;    //初始大小比例
        private float mSizeSpeed;      //每单位时间增加的大小比例
        private int mSizeTime;          //改变大小完成的总时长
        private float mPathSpeed;   //每刷新时间的路径速度
        private int mPathTime;      //走完路径总耗时，毫秒
        private float mAlpha;        //当前半透明度
        private long mAlphaStart;   //半透明度时间点，1/3，经过多长时间后开始
        private float mAlphaSpeed;  //每单位睡眠时间改变的alpha值

        private long mStartTime;                //开始时间，开机时间为准
        private PathMeasure pathMeasure;

        private int preAlpha = PhivePaintorView.ALPHA_MAX;       //上一次的alpha值
        private Interpolator mInterpolator;

        private DrawItem item;


        public Bubble() {
            bitmap = randBitmap();
            mStartTime = SystemClock.elapsedRealtime();

            mMovePath = mBubblePath.getRandBubblePath();
            pathMeasure = new PathMeasure();
            pathMeasure.setPath(mMovePath, false);
            float pathLength = pathMeasure.getLength() - bitmap.getHeight() / 2f;

            mSizeRate = BUBBLE_INIT_RATE;
            mAlpha = BUBBLE_INIT_ALPHA * PhivePaintorView.ALPHA_MAX;
            //一秒移动的总长度
            float pathSpeedPerSecond = ApplicationUtilV2.dip2px(mContext, BUBBLE_PATH_SPEED) + ApplicationUtilV2.dip2px(mContext, randPathSpeed(BUBBLE_PATH_SPEED_SCOPE));
            mPathSpeed = pathSpeedPerSecond * (PhivePaintorView.SLEEP_TIME / 1000f);     //每刷新率的速度

            mPathTime = Float.valueOf(pathLength / (pathSpeedPerSecond / 1000f)).intValue();
            mSizeTime = Float.valueOf(mPathTime * BUBBLE_SIZE_TIME_RATE).intValue();       //大小改变总时长
            mSizeSpeed = ((1.0f - mSizeRate) / mSizeTime) * PhivePaintorView.SLEEP_TIME;              //每单位睡眠时间增加的比例

            mAlphaStart = Float.valueOf(mPathTime * BUBBLE_ALPHA_TIME_RATE).intValue();
            mAlphaSpeed = (mAlpha / (mPathTime - mAlphaStart)) * PhivePaintorView.SLEEP_TIME;
        }

        public void reUsed() {
            bitmap = randBitmap();
            mMovePath = mBubblePath.getRandBubblePath();
            pathMeasure.setPath(mMovePath, false);
            mStartTime = SystemClock.elapsedRealtime();
        }


        private Interpolator getInterpolator() {
            if (mInterpolator == null) {
                mInterpolator = new OvershootInterpolator();
            }
            return mInterpolator;
        }

        public DrawItem getDrawItem() {
            long pastTime = SystemClock.elapsedRealtime() - mStartTime;
            //经过的距离
            float len = ((float) pastTime / PhivePaintorView.SLEEP_TIME) * mPathSpeed;
            if (len > pathMeasure.getLength()) {
                return null;
            }
            float[] pos = new float[2];
            pathMeasure.getPosTan(len, pos, null);
            PointF p = new PointF(pos[0], pos[1]);

            float alpha = this.mAlpha;
            if (pastTime >= mAlphaStart) {
                long alphaTime = pastTime - mAlphaStart;
                alpha = this.mAlpha - ((float) alphaTime / PhivePaintorView.SLEEP_TIME) * mAlphaSpeed;
                if (alpha < PhivePaintorView.ALPHA_MIN) {
                    return null;
                }
            }

            float size = 1.0f;
            float sizeProgress = (float) pastTime / mSizeTime;
            if (sizeProgress <= 1) {
                sizeProgress = getInterpolator().getInterpolation(sizeProgress);
                size = mSizeRate + ((sizeProgress * mSizeTime) / PhivePaintorView.SLEEP_TIME) * mSizeSpeed;
            }

            if (item == null)
                item = new DrawItem(bitmap, p, Float.valueOf(alpha).intValue(), size);
            else
                //复用 重新初始化数据
                item.reset(bitmap, p, Float.valueOf(alpha).intValue(), size);

            return item;
        }
    }

    /**
     * 路径类
     * @return
     */
    private class BubblePath {
        private Point originP; //冒泡点
        private Path disPath;
        private PathMeasure pathMeasure;

        public BubblePath(Point originP) {
            this.originP = originP;
            this.pathMeasure = new PathMeasure();

            resetBirthland();
            resetDisPath();
        }

        public void update(Point originP) {
            this.originP = originP;
            resetBirthland();
            resetDisPath();
        }

        private void resetBirthland() {
//            if(this.originP == null || (this.originP.x == 0 && this.originP.y == 0)){
//            }
        }

        private void resetDisPath() {
            if (disPath == null) {
                disPath = new Path();
            } else {
                disPath.reset();
            }

            disPath.moveTo(originP.x - vir_width / 2, originP.y - vir_heigh + verticalPathLen);  //绘画基点
            disPath.lineTo(originP.x - vir_width / 2, originP.y - vir_heigh);
            disPath.lineTo(originP.x + vir_width / 2, originP.y - vir_heigh);
            disPath.lineTo(originP.x + vir_width / 2, originP.y - vir_heigh + verticalPathLen);
        }

        /**
         * 获得一条随机的路径
         *
         * @return
         */
        public Path getRandBubblePath() {
            Path quadPath = new Path();
            quadPath.moveTo(originP.x, originP.y);

            //控制点，大小为宽度的1/4的正方形，离底部1/5高度
//            int cStartX = (CODEC_WIDTH - high / 4) / 2;
//            int cEndX = cStartX + CODEC_WIDTH / 4;
//            int cEndY = (high - Float.valueOf(startY).intValue()) - high / 5;
//            int cStartY = cEndY - CODEC_WIDTH / 4;

            int cStartX = originP.x - ctr_len / 2;
            int cEndX = originP.x + ctr_len / 2;
            int cStartY = originP.y - control_offsetY - ctr_len ;
            int cEndY = cStartY + ctr_len;

//            if(cEndX <= 0) {
//                _95L.i(TAG, "cEndX is <= 0");
//                cEndX = 1;
//            }
//            if(cEndY <= 0) {
//                _95L.i(TAG, "cEndY is <= 0");
//                cEndY = 1;
//            }
            int controllX = rand(cStartX, cEndX);
            int controllY = rand(cStartY, cEndY);

            pathMeasure.setPath(disPath, false);

            //获取指定长度的位置坐标及该点切线值
            float[] pos = new float[2];
            int subLen = Float.valueOf(rand(1, TOTAL_BUBBLE) <= MIDDLE_BUBBLE ? verticalPathLen : 0).intValue();
            pathMeasure.getPosTan(rand(subLen, Float.valueOf(pathMeasure.getLength() - subLen).intValue()), pos, null);

            quadPath.quadTo(controllX, controllY, pos[0], pos[1]);

            return quadPath;
        }
    }

    /**
     * 绘制类
     */
    private class DrawItem {
        public Bitmap bitmap;
        public PointF point;
        public int alpha;
        public float size;
        private RectF selfLoc;
        public float width, height;

        public DrawItem(Bitmap bitmap, PointF point, int alpha, float size) {
            this.bitmap = bitmap;
            this.point = point;
            this.alpha = alpha;
            this.size = size;

            width = bitmap.getWidth() * size;
            height = bitmap.getHeight() * size;

        }

        public void reset(Bitmap bitmap, PointF point, int alpha, float size) {
            this.bitmap = bitmap;
            this.point.set(point);
            this.alpha = alpha;
            this.size = size;

            width = bitmap.getWidth() * size;
            height = bitmap.getHeight() * size;
        }

        //得到图形本身的位置
        public RectF getLoc() {
            if (selfLoc == null) {
                selfLoc = new RectF(point.x - width / 2f
                        , point.y - height / 2f
                        , point.x + width / 2f
                        , point.y + height / 2f);
                return selfLoc;
            }
            selfLoc.set(point.x - width / 2f
                    , point.y - height / 2f
                    , point.x + width / 2f
                    , point.y + height / 2f);
            return selfLoc;
        }
    }

    @Override
    public void clear() {
        if (bitmaps.length > 0) {
            for (int i = 0; i < bitmaps.length; i++) {
                if (bitmaps[i] != null && !bitmaps[i].isRecycled()) {
                    bitmaps[i].recycle();
                }
            }
        }

        if(mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }

        mineBitmap.recycle();

        CandyPaintor candyPaintor = this.mPhivePaintorView.getCandyPaintor();
        if(candyPaintor != null) {
            candyPaintor.removeUpdateSizeListener(this);
        }
    }
}
