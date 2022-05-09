package com.cold.slideview.view;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class Constants {

    public final static int LANDSCAPE = 0;
    public final static int PORTRAIT = 1;

    public final static int LEFT = 2; // 布局隐藏的状态
    public final static int RIGHT = 3; // 布局显示的状态
    public final static int TOP = 4;
    public final static int BOTTOM = 5;

    @IntDef({LANDSCAPE,PORTRAIT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ClearMode{

    }

    @IntDef({LEFT, RIGHT, TOP, BOTTOM})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Orientation {

    }

}
