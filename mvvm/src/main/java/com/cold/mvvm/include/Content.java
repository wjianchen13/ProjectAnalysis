package com.cold.mvvm.include;

import android.view.View;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.cold.mvvm.BR;

/**
 * name:
 * desc:
 * author:
 * date: 2017-06-22 17:08
 * remark:
 */
public class Content extends BaseObservable {

    private String title;
    private String subTitle;

    public Content(String title, String subTitle) {
        this.title = title;
        this.subTitle = subTitle;
    }

    @Bindable
    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
        notifyPropertyChanged(BR.subTitle);
    }

    @Bindable public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(BR.title);
    }

    public void onClic(View v) {

    }
}
