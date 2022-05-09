package cold.com.message;

import android.os.Handler;
import android.os.Message;

public interface IChunk {
    void run(Run run);

    boolean post(Runnable runnable);
    boolean postAtTime(Runnable runnable, long time);
    boolean postDelayed(Runnable runnable, long time);
    boolean postAtFrontOfQueue(Runnable r);

    boolean sendEmptyMessage(int what);
    boolean sendMessage(Message message);
    boolean sendMessageAtTime(Message message, long time);
    boolean sendMessageDelayed(Message message, long time);

    void onDestroy();
    void setCallback(Handler.Callback callback);

    WeakHandler getH();
}