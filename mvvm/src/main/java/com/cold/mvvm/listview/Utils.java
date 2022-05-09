package com.cold.mvvm.listview;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.cold.mvvm.R;

/**
 * 用于图片加载
 */
public class Utils {

    @BindingAdapter({"imageUrl"})
    public static void loadImage(ImageView imageView, String url) {
        System.out.println("-------> loadImage");
        if (url == null) {
            imageView.setImageResource(R.mipmap.ic_launcher);
        } else {
            Glide.with(imageView.getContext()).load(url).into(imageView);
        }
    }

    @BindingAdapter({"tag"})
    public static void setTag(ImageView imageView, User user) {
        imageView.setTag(user);
    }
}
