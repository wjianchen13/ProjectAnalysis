package com.cold.projectanalysis.slidelayout.view;

import android.view.View;

public interface IClearRootView {

    void setClearSide(@Constants.Orientation int orientation);

    void setIPositionCallBack(IPositionCallBack l);

    void setIClearEvent(IClearEvent l);

    void addView(View child, int index);
}
