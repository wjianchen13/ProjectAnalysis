package com.cold.projectanalysis.paintorview.paintor;

/**
 * Created by Administrator on 2016-09-12.
 */
public class AddItem {
    IPaint target;
    Object obj;

    public AddItem(IPaint target, Object obj) {
        this.target = target;
        this.obj = obj;
    }

    public IPaint getTarget() {
        return target;
    }

    public Object getObj() {
        return obj;
    }
}
