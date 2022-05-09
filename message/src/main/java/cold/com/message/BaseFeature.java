package cold.com.message;

import android.os.Handler;
import android.os.Message;
import android.view.View;

import androidx.annotation.CallSuper;


/**
 * Created by Administrator on 2016-07-15.
 */
public abstract class BaseFeature implements Handler.Callback{

    private IChunk mChunk;
    protected View mParent;

    public BaseFeature() {

    }

    public boolean isOnShowing() {
        if (mParent == null) return false;
        return mParent.isShown();
    }

    public void setParent(View parent) {
        this.mParent = parent;
    }

    public View getParent() {
        return mParent;
    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    /**
     * 返回false = 事件继续传递
     *
     * @return
     */
    public boolean onBackPressed() {
//        if (isOnShowing()) {
//            setVisibleAnim(PhiveUtil.AnimEnum.AE_IN_VISIABLE, PhiveUtil.PhiveAnimEnum.LAE_KEY_BACK, 0);
//            return true;
//        }
        return false;
    }

    /**
     * 直播间加载完成
     */
    public void onLoadedFinish() {
    }

    /**
     * 资源回收，必须实现
     */
    @CallSuper
    public void clear(){
        if(mChunk != null) {
            mChunk.onDestroy();
            mChunk = null;
        }
    }

    public void onKeyboardChange(boolean isShown, int heigh){
    }

    public void onKeyboardChangeBefore(boolean isShown, int heigh){
    }

    // =========================chunk
    // 子线程执行
    public void run(Run run){
        getChunk().run(run);
    }

    public boolean post(Runnable runnable){
        return getChunk().post(runnable);
    }

    public boolean postAtTime(Runnable runnable, long time){
        return getChunk().postAtTime(runnable, time);
    }

    public boolean postDelayed(Runnable runnable, long time){
        return getChunk().postDelayed(runnable, time);
    }

    public boolean postAtFrontOfQueue(Runnable r){
        return getChunk().postAtFrontOfQueue(r);
    }

    public boolean sendEmptyMessage(int what){
        return getChunk().sendEmptyMessage(what);
    }

    public boolean sendMessage(Message message){
        return getChunk().sendMessage(message);
    }

    public boolean sendMessageAtTime(Message message, long time){
        return getChunk().sendMessageAtTime(message, time);
    }

    public boolean sendMessageDelayed(Message message, long time){
        return getChunk().sendMessageDelayed(message, time);
    }
    protected IChunk getChunk() {
        if(mChunk == null) {
            mChunk = new Chunk();
            mChunk.setCallback(this);
        }
        return mChunk;
    }
}
