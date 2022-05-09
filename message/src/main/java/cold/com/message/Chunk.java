package cold.com.message;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

/**
 * Created by Administrator on 2017/3/17 0017.
 */

public class Chunk implements IChunk, Handler.Callback {
    private H h;
    private Handler.Callback callback;
    private WorkThreadFactory executor;
    private final int threadPoolSize;
    private final boolean isMainLoop;

    public Chunk() {
        this(false);
    }

    public Chunk(boolean isMainLoop) {
        this(1, isMainLoop);
    }

    public Chunk(int threadPoolSize) {
        this(threadPoolSize, false);
    }

    public Chunk(int threadPoolSize, boolean isMainLoop) {
        this.threadPoolSize = threadPoolSize;
        this.isMainLoop = isMainLoop;
    }

    @Override
    public boolean post(Runnable runnable) {
        return getH().post(runnable);
    }

    @Override
    public boolean postAtTime(Runnable runnable, long time) {
        return getH().postAtTime(runnable, time);
    }

    @Override
    public boolean postDelayed(Runnable runnable, long time) {
        return getH().postDelayed(runnable, time);
    }

    @Override
    public boolean sendEmptyMessage(int what) {
        return getH().sendEmptyMessage(what);
    }

    @Override
    public boolean sendMessage(Message message) {
        return getH().sendMessage(message);
    }

    @Override
    public boolean sendMessageAtTime(Message message, long time) {
        return getH().sendMessageAtTime(message, time);
    }

    @Override
    public boolean sendMessageDelayed(Message message, long time) {
        return getH().sendMessageDelayed(message, time);
    }

    @Override
    public H getH() {
        if (h == null)
            synchronized (H.class) {
                if (h == null) {
                    if (isMainLoop)
                        h = new H(Looper.getMainLooper(), this);
                    else
                        h = new H(this);
                }
            }
        return h;
    }

    @Override
    public boolean postAtFrontOfQueue(Runnable r) {
        return getH().postAtFrontOfQueue(r);
    }

    /**
     * 运行在非Ui线程
     *
     * @param run 执行体
     */
    @Override
    public final void run(@NonNull Run run) {
        if (executor == null) {
            synchronized (Chunk.class) {
                if (executor == null)
                    executor = WorkThreadFactory.createThreadPool(threadPoolSize);
            }
        }
        executor.execute(run);
    }

    public void setCallback(Handler.Callback callback) {
//        System.out.println("------------------------> setCallback  callback:" + callback);
        this.callback = callback;
        System.out.println("------------------------> setCallback  callback == null:" + (this.callback == null));
    }


    public void onDestroy() {
        if (executor != null)
            executor.shutdownNow();
        if (h != null) {
            h.removeCallbacksAndMessages(null);
            h = null;
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        System.out.println("------------------------> callback:" + (this.callback == null));
        return callback != null && callback.handleMessage(msg);

    }
    //=========================================inner static Class ================================================================///

    protected static class H extends WeakHandler {

        private H(Handler.Callback callback) {
            super(callback);
        }

        public H(@NonNull Looper looper, @NonNull Handler.Callback callback) {
            super(looper, callback);
        }
    }
}
