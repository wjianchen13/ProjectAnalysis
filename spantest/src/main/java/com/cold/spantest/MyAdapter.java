package com.cold.spantest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import androidx.annotation.ColorRes;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by baoyz on 2014/6/29.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{

    // 数据集
    private String[] mDataset;
    private Context context;
    
    public MyAdapter(Context context, String[] dataset) {
        super();
        this.context = context;
        mDataset = dataset;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        // 创建一个View，简单起见直接使用系统提供的布局，就是一个TextView
        View view = View.inflate(viewGroup.getContext(), R.layout.item_chat_msg_1, null);
        // 创建一个ViewHolder
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        SpannableStringBuilder showString = new SpannableStringBuilder("");
    	Bitmap bm1 = ((BitmapDrawable)context.getResources().getDrawable(R.drawable.ic_test)).getBitmap();

        SpannableString spanStr = new SpannableString("我说");
        showString.append(spanStr);

        SpannableString str =  new SpannableString("img ");
        ImageSpan imageSpan = new ImageSpanC(context, bm1, DynamicDrawableSpan.ALIGN_BASELINE);
        str.setSpan(imageSpan, 0, str.length() - 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        showString.append(str);

        Bitmap bm2 = ((BitmapDrawable)context.getResources().getDrawable(R.drawable.ic_test1)).getBitmap();
        SpannableString str2 =  new SpannableString("img ");
        ImageSpan imageSpan2 = new ImageSpanC(context, bm2, DynamicDrawableSpan.ALIGN_BASELINE);
        str2.setSpan(imageSpan2, 0, str2.length() - 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        showString.append(str2);
        
        SpannableString spanStr1 = new SpannableString("我说：Android是 ");
        showString.append(spanStr1);

        SpannableString str3 =  new SpannableString("img ");
//        ImageSpan imageSpan3 = ;
//        str3.setSpan(imageSpan3, 0, str2.length() - 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        showString.append(getWHImageSpan(context, R.drawable.ic_live_free_gift_1, 60, 60));

        Bitmap bm3 = ((BitmapDrawable)context.getResources().getDrawable(R.drawable.ic_system_symbol_left)).getBitmap();
        Bitmap bm4 = ((BitmapDrawable)context.getResources().getDrawable(R.drawable.ic_system_symbol_right)).getBitmap();

        SpannableStringBuilder spanBuilder = new SpannableStringBuilder();
//        spanBuilder.append(getColorSpanText(context, "HELLO", R.color.live_chat_system_info_2));
//        spanBuilder.append(getScaleImageSpan(context, R.drawable.ic_system_symbol_left, dip2px(context, 11)));
//        spanBuilder.append(getColorSpanText(context, "我的", R.color.live_chat_system_info_2));
        String str1 = "恭喜“小王子”在和“小狗子”的pk<s>活动中获得了胜利<\\s>，<c>速去围观>><\\c>";


        viewHolder.mTextView.setText(spanBuilder);
        viewHolder.mTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }


    @Override
    public int getItemCount() {
        return 1;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView mTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView)itemView.findViewById(R.id.tv_chat);
        }
    }

    /**
     * 获取设定大小的image span
     *
     * @param context
     * @param spanImgIds
     * @param width
     * @param height
     */

    public static SpannableString getWHImageSpan(Context context, int spanImgIds, int width, int height) {
        SpannableString spanStr = null;
        if (spanImgIds > 0 && context != null) {
            Drawable img = context.getResources().getDrawable(spanImgIds);
            img.setBounds(0, 0, width, height);
            ImageSpan imgSpan = new ImageSpanC(img, DynamicDrawableSpan.ALIGN_BASELINE);
            spanStr = new SpannableString("img ");
            spanStr.setSpan(imgSpan, 0, spanStr.length() - 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        return spanStr;
    }

    /**
     * 按照比例获取设定大小的image span
     *
     * @param context
     * @param
     * @param height 图片绘制高度 px
     * @param spanImgIds
     */
    public static SpannableString getScaleImageSpan(Context context, int spanImgIds, int height) {
        SpannableString spanStr = null;
        if (spanImgIds > 0 && context != null) {
            Drawable img = context.getResources().getDrawable(spanImgIds);
            int intrinsicWidth = img.getIntrinsicWidth(); // 获取图片的宽度 px
            int intrinsicHeight = img.getIntrinsicHeight(); // 获取图片的高度 px
            float factor = (float)height / intrinsicHeight; // 转换因子
            img.setBounds(0, 0, (int)(intrinsicWidth * factor), height);
            ImageSpan imgSpan = new ImageSpanC(img, DynamicDrawableSpan.ALIGN_BASELINE);
            spanStr = new SpannableString("img ");
            spanStr.setSpan(imgSpan, 0, spanStr.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        return spanStr;
    }

    /**
     * 给指定的内容设置颜色
     *
     * @param context
     * @param txt
     * @param colorId
     * @return
     */
    public static SpannableString getColorSpanText(Context context, String txt, @ColorRes int colorId) {
        SpannableString spanStr = new SpannableString(txt);
        if (context != null) {
            spanStr.setSpan(new ForegroundColorSpan(context.getResources().getColor(colorId)), 0, spanStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spanStr;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        return (int) (dpValue * getDensity(context) + 0.5f);
    }

    /**
     * 返回屏幕密度
     */
    public static float getDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

}
