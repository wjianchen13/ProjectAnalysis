package com.cold.projectanalysis.paintorview.paintor;

/**
 * Created by Administrator on 2016-09-13.
 */
public class DefineMessage {
    IPaint target;
    int what;
    Object obj;

    public DefineMessage(IPaint target, int what, Object obj) {
        this.target = target;
        this.what = what;
        this.obj = obj;
    }

    public IPaint getTarget() {
        return target;
    }

    public int getWhat() {
        return what;
    }

    public Object getObj() {
        return obj;
    }
}
