package com.cold.projectanalysis.paintorview.paintor;

/**
 * Created by Administrator on 2016-09-12.
 */
public class DrawItem {
    IPaint target;
    Object frame;
   int flag;
    public DrawItem(IPaint target, Object frame) {
        this.target = target;
        this.frame = frame;
    }

    public IPaint getTarget() {
        return target;
    }

    public Object getFrame() {
        return frame;
    }
}
