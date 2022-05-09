package com.cold.projectanalysis.paintorview;


import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by MinF on 2017/1/22.
 * <p>
 * 懒加载框架
 */

public abstract class LazyHolder {

    private boolean isInit;
    private BlockingQueue<Action> queue;
    public LazyHolder() {
        queue = new LinkedBlockingQueue<>();
    }

    public void init() {
        if (isInit)
            return;
        lazyInitView();
        isInit = true;

        for (Action action : queue) {
            action.initCompleted(this);
        }
        queue.clear();
    }

    protected abstract void lazyInitView();

    public void add(Action action) {
        if (isInit)
            action.initCompleted(this);
        else
            queue.add(action);
    }

    public boolean isInit() {
        return isInit;
    }

    public interface Action {
        void initCompleted(LazyHolder holder);
    }

    public void destroy() {
        queue.clear();
        queue = null;
    }

}
