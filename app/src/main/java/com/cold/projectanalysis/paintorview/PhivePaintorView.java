package com.cold.projectanalysis.paintorview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;

import com.cold.projectanalysis.paintorview.bubble.PhiveBubbleEffectPaintor;
import com.cold.projectanalysis.paintorview.paintor.AddItem;
import com.cold.projectanalysis.paintorview.paintor.CandyEffectPaintor;
import com.cold.projectanalysis.paintorview.paintor.CandyPaintor;
import com.cold.projectanalysis.paintorview.paintor.DefineMessage;
import com.cold.projectanalysis.paintorview.paintor.DrawItem;
import com.cold.projectanalysis.paintorview.paintor.IPaint;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Administrator on 2016-09-12.
 */
public class PhivePaintorView extends TextureView implements View.OnTouchListener, TextureView.SurfaceTextureListener {
    private static final int MSG_MOVE = 101;
    private static final int MSG_ADD = 102;
    private static final int MSG_DEFINE = 103;
    private static final int MSG_DRAWING = 104;
    public static final int ALPHA_MAX = 255;
    public static final int ALPHA_MIN = 0;

    public static final int SLEEP_TIME = 32;              //刷新率

    private Context mContext;
    private CalculattingThread mCalculattingThread; //计算线程
    private DrawingThread mDrawingThread;           //画图线程
    private AtomicBoolean isPrepare;                //就绪

    private boolean isFirst;
    private boolean isWorking = false;              //是否处于工作状态

    private List<DrawItem> historyItems;
    private List<IPaint> allPaints;

//    private DanmuPaintor mDanmuPaintor;
    private CandyPaintor mCandyPaintor;
    private PhiveBubbleEffectPaintor mPhiveBubbleEffectPaintor;
    private CandyEffectPaintor mCandyEffectPaintor;
//    private PhiveBroadcastPaintor mPhiveBroadcastPaintor;


    public PhivePaintorView(Context context) {
        super(context);
        init();
    }

    public PhivePaintorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PhivePaintorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        this.mContext = getContext();
        this.setSurfaceTextureListener(this);
        this.setOpaque(false);
        this.setOnTouchListener(this);

        isPrepare = new AtomicBoolean(false);
        //初始化所有子项
        if(allPaints == null) {
            allPaints = new ArrayList<IPaint>();
            mCandyPaintor = new CandyPaintor(this, mContext);
            mPhiveBubbleEffectPaintor = new PhiveBubbleEffectPaintor(this, mContext);
            mCandyEffectPaintor = new CandyEffectPaintor(this, mContext);
            allPaints.add(mPhiveBubbleEffectPaintor);
            allPaints.add(mCandyPaintor);
//            allPaints.add(mCandyEffectPaintor);
        }

    }

    public CandyPaintor getCandyPaintor() {
        return mCandyPaintor;
    }


    public PhiveBubbleEffectPaintor getPhiveBubbleEffectPaintor(){
        return mPhiveBubbleEffectPaintor;
    }

    public CandyEffectPaintor getCandyEffectPaintor(){
        return  mCandyEffectPaintor;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        boolean result = false;
        if(allPaints != null && !allPaints.isEmpty()){
            for (int i = 0; i < allPaints.size(); i ++) {
                IPaint paint = allPaints.get(i);
                if(paint != null) {
                    if(paint.onTouch(view, motionEvent)){
                        result = true;
                    }
                }
            }
        }
        return result;
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        System.out.println("----------------------> onSurfaceTextureAvailable width: " + width + "   height: " + height);
        if (mCalculattingThread == null) {
            mCalculattingThread = new CalculattingThread();
            mCalculattingThread.updateSize(width, height);
            mCalculattingThread.start();
        }

        mDrawingThread = new DrawingThread(new Surface(surface));
        mDrawingThread.start();
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        System.out.println("----------------------> onSurfaceTextureSizeChanged width: " + width + "   height: " + height);
//        showToast("onSurfaceTextureSizeChanged");
        if (mCalculattingThread != null && width != 0 && height != 0) {
            mCalculattingThread.updateSize(width, height);
        }
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        isPrepare.set(false);
//        showToast("onSurfaceTextureDestroyed");
        if (mDrawingThread != null) {
            mDrawingThread.quit();
            Surface sf = mDrawingThread.getDrawingSurface();
            if(sf != null && sf.isValid()) {
                sf.release();
            }
            mDrawingThread = null;

            if(surfaceTexture != null) {
                surfaceTexture.release();
            }
        }

        // 返回 true 并允许框架释放 Surface
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

    }

    public void start() {
        if (!isWorking) {
            isWorking = true;
            if (mCalculattingThread != null
                    && mCalculattingThread.getReceiver() != null
                    && !mCalculattingThread.getReceiver().hasMessages(MSG_MOVE))
                mCalculattingThread.send(MSG_MOVE);
        }
    }

    public void stop() {
        isWorking = false;
        if (mCalculattingThread != null) {
            Handler handler = mCalculattingThread.getReceiver();
            if (handler != null) {
                handler.removeCallbacksAndMessages(null);
            }
        }
    }

    public boolean isRunning() {
        return isWorking;
    }


    /**
     * 计算线程
     *
     * @author Administrator
     */
    private class CalculattingThread extends HandlerThread implements Handler.Callback {
        private Rect mSurfaceRect;
        private Handler mReceiver;

        public CalculattingThread() {
            super("CalculattingThread");
            mSurfaceRect = new Rect();
        }

        @Override
        protected void onLooperPrepared() {
            mReceiver = new Handler(getLooper(), this);
            mReceiver.sendEmptyMessage(MSG_MOVE);
        }

        @Override
        public boolean quit() {
            if (mReceiver != null) {
                mReceiver.removeCallbacksAndMessages(null);
                mReceiver = null;
            }
            return super.quit();
        }

        @Override
        public boolean handleMessage(Message msg) {
            if (isWorking) {
                switch (msg.what) {
                    case MSG_DEFINE:    //自定义事件，需要计算线程处理
                        DefineMessage dm = (DefineMessage) msg.obj;
                        if(dm != null) {
                            if(dm.getTarget() != null){
                                dm.getTarget().defineMessage(dm.getWhat(), dm.getObj());
                            }
                        }
                        break;
                    case MSG_ADD:
                        AddItem addItem = (AddItem)msg.obj;
                        if(addItem != null) {
                            addItem.getTarget().add(addItem.getObj());
                        }
                        break;
                    case MSG_MOVE:
                        System.out.println("--------------------> MSG_MOVE " + MSG_MOVE);
                        if (mSurfaceRect.isEmpty()) {
                            if(mReceiver != null) mReceiver.sendEmptyMessageDelayed(MSG_MOVE, SLEEP_TIME);
                            break;
                        }

                        List<DrawItem> items = null;
                        if(allPaints != null && !allPaints.isEmpty()) {
                            for (int i = 0; i < allPaints.size(); i++){
                                IPaint paint = allPaints.get(i);
                                if(paint != null) {
                                    DrawItem item = paint.getDrawItem();
                                    System.out.println("--------------------> (item != null): " + (item != null));
                                    if(item != null) {
                                        if(items == null) items = new ArrayList<DrawItem>();
                                        items.add(item);
                                    }
                                }
                            }
                        }

                        //请求画图
                        if (isPrepare.get() && mDrawingThread != null) {
                            try {
                                if (mDrawingThread.isAlive()) {
                                    Handler handler = mDrawingThread.getReceiver();
                                    if(handler != null) handler.obtainMessage(MSG_DRAWING, items).sendToTarget();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            //记录最后一帧
                            if (!isFirst) {
                                synchronized (PhivePaintorView.this) {
                                    historyItems = items;
                                }
                            }
                        }
                        // 发送下一帧
                        if(mReceiver != null) mReceiver.sendEmptyMessageDelayed(MSG_MOVE, SLEEP_TIME);
                        break;
                }
            }
            return true;
        }

        public void updateSize(int width, int height) {
            System.out.println("----------------------> updateSize width: " + width + "   height: " + height);
            mSurfaceRect.set(0, 0, width, height);
            if(allPaints != null && !allPaints.isEmpty()){
                for (int i = 0; i < allPaints.size(); i ++){
                    IPaint paint = allPaints.get(i);
                    if(paint != null) {
                        paint.updateSize(width, height);
                    }
                }
            }
        }

        public void send(int what) {
            if (mReceiver != null) mReceiver.obtainMessage(what).sendToTarget();
        }

        public Handler getReceiver() {
            return mReceiver;
        }
    }

    /**
     * 画图线程
     *
     * @author Administrator
     */
    private class DrawingThread extends HandlerThread implements Handler.Callback {

        private Surface mDrawingSurface;
        private Paint mPaint;
        private Handler mReceiver;

        public DrawingThread(Surface surface) {
            super("DrawingThread");
            this.mDrawingSurface = surface;
            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setStyle(Paint.Style.STROKE); //绘制空心圆

            synchronized (PhivePaintorView.this) {
                drawItem(historyItems);
            }
        }

        @Override
        protected void onLooperPrepared() {
            mReceiver = new Handler(getLooper(), this);
            isPrepare.set(true);
        }

        @Override
        public boolean quit() {

            if (mReceiver != null) {
                mReceiver.removeCallbacksAndMessages(null);
                mReceiver = null;
            }

            return super.quit();
        }

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_DRAWING:
                    drawItem((List<DrawItem>) msg.obj);
                    break;
            }
            return true;
        }

        private void drawItem(List<DrawItem> items) {
            Canvas canvas = null;
            // 渲染一帧
            try {
                // 锁定 SurfaceView，并返回到要绘图的 Canvas
                canvas = mDrawingSurface.lockCanvas(null);
                // 首先清空 Canvas
                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

                if(items != null && !items.isEmpty()) {
                    for (int i = 0; i < items.size(); i++){
                        DrawItem item = items.get(i);
                        if(item != null && item.getTarget() != null && item.getFrame() != null) {
                            item.getTarget().draw(canvas, mPaint, item.getFrame());
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (isFirst)
                    isFirst = false;
                // 解锁 Canvas，并渲染当前的图像
                if (canvas != null) {
                    try {
                        mDrawingSurface.unlockCanvasAndPost(canvas);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        public void release() {
            mDrawingSurface.release();
        }

        public Handler getReceiver() {
            return mReceiver;
        }

        public Surface getDrawingSurface(){
            return mDrawingSurface;
        }
    }


    public void add(AddItem item){
        if(mCalculattingThread != null) {
            Handler handler = mCalculattingThread.getReceiver();
            if(handler != null) handler.obtainMessage(MSG_ADD, item).sendToTarget();
        }
    }

    /**
     * 发送自定义的同步消息，目的跟计算线程同步处理
     * @param dm
     */
    public void sendDefineMessage(DefineMessage dm){
        if(mCalculattingThread != null) {
            Handler handler = mCalculattingThread.getReceiver();
            if(handler != null) handler.obtainMessage(MSG_DEFINE, dm).sendToTarget();
        }
    }

    //获得信息区的消息区域
    public int[] getPhiveMessageScreenLocation(){
//        if(mContext instanceof PhiveBaseActivity) {
//            PhiveBaseActivity activity = (PhiveBaseActivity) mContext;
            int[] location = new int[2];
            location[0] = 36;
            location[1] = 1110;
//             activity.getPhiveMessage().getParent().getLocationOnScreen(location);
            return location;
//        }
//        return null;
    }

    public View getPhiveMessageView(){
//        if(mContext instanceof PhiveBaseActivity) {
//            PhiveBaseActivity activity = (PhiveBaseActivity) mContext;
//            return activity.getPhiveMessage().getParent();
//        }
        return null;
    }

    //获得快捷送礼的区域
    public int[] getGiftShortcutLocation(){
//        if(mContext instanceof PhiveBaseActivity) {
//            PhiveBaseActivity activity = (PhiveBaseActivity) mContext;
            int[] location = new int[2];
//            activity.getIvShortcutGift().getLocationOnScreen(location);
//
        location[0] = 888;
        location[1] = 1635;
            return location;
//        }
//        return null;
    }

    public View getGiftShortcutView(){
//        if(mContext instanceof PhiveBaseActivity) {
//            PhiveBaseActivity activity = (PhiveBaseActivity) mContext;
//            return activity.getIvShortcutGift();
//        }
        return null;
    }

    //用户是否已登录
    public boolean isLogin(){
//        if(mContext instanceof PhiveBaseActivity){
//            PhiveBaseActivity activity = (PhiveBaseActivity) mContext;
//            return activity.isLogin();
//        }
        return true;
    }

//    public PhiveBroadcastPaintor getPhiveBroadcastPaintor() {
//        return mPhiveBroadcastPaintor;
//    }

    public DrawingThread getDrawingThread() {
        return mDrawingThread;
    }

    public CalculattingThread getCalculattingThread() {
        return mCalculattingThread;
    }

    public boolean isPrepare(){
        return isPrepare != null && isPrepare.get();
    }

//    public void clear() {
//        if(mContext instanceof PhiveBaseActivity) {
//            ((PhiveBaseActivity)mContext).unRegisterKeyboardListener(this);
//        }
//
//        if (mCalculattingThread != null) {
//            mCalculattingThread.quit();
//            mCalculattingThread = null;
//        }
//
//        if (mDrawingThread != null) {
//            mDrawingThread.quit();
//            Surface sf = mDrawingThread.getDrawingSurface();
//            if(sf != null && sf.isValid()) {
//                sf.release();
//            }
//            mDrawingThread = null;
//        }
//
//        if(allPaints != null && !allPaints.isEmpty()) {
//            for (int i = 0; i < allPaints.size(); i++) {
//                IPaint paint = allPaints.get(i);
//                if(paint != null) paint.clear();
//            }
//            allPaints.clear();
//        }
//    }
}
