package com.cold.mvvm.listbean;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.cold.mvvm.R;

/**
 * 用于图片加载
 * Created by 南尘 on 16-7-18.
 */
public class Utils {

    @BindingAdapter({"imageUrl"})
    public static void loadImage(ImageView imageView, String url) {
        if (url == null) {
            imageView.setImageResource(R.mipmap.ic_launcher);
        } else {
            Glide.with(imageView.getContext()).load(url).into(imageView);
        }
    }
}
