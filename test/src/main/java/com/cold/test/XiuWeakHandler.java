package com.cold.test;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by Administrator on 2017/9/4 0004.
 */

public class XiuWeakHandler extends WeakHandler {
    public XiuWeakHandler() {
        super();
    }

    public XiuWeakHandler(@Nullable Handler.Callback callback) {
        super(callback);
    }

    public XiuWeakHandler(@NonNull Looper looper) {
        super(looper);
    }

    public XiuWeakHandler(@NonNull Looper looper, @NonNull Handler.Callback callback) {
        super(looper, callback);
    }
}
