package com.cold.slideview2;

import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by baoyz on 2014/6/29.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{

    // 数据集
    private String[] mDataset;

    public MyAdapter(String[] dataset) {
        super();
        mDataset = dataset;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        // 创建一个View，简单起见直接使用系统提供的布局，就是一个TextView
        View view = View.inflate(viewGroup.getContext(), android.R.layout.simple_list_item_1, null);
        // 创建一个ViewHolder
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        // 绑定数据到ViewHolder上
        viewHolder.mTextView.setTextColor(Color.rgb(255, 255, 255));
        viewHolder.mTextView.setText(mDataset[i]);
    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView mTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView;
        }
    }
}
