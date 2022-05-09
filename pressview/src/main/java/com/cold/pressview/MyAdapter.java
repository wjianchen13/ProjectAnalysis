package com.cold.pressview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * name:
 * desc:
 * author:
 * date: 2017-06-21 20:28
 * remark:
 */

public class MyAdapter  extends BaseAdapter {

    private Context context=null;
    private List<MyData> datas = null;
    private LayoutInflater inflater;

    //构造方法
    public MyAdapter(Context context, List<MyData> datas) {
        this.context = context;
        this.datas = datas;
        inflater= LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return datas.size();

    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        Holder holder;
        if(view==null){
            view=inflater.inflate(R.layout.gvitem,null);
            holder=new Holder();
            holder.item_img=(ImageView)view.findViewById(R.id.img_shoukuan);
            holder.item_tex=(TextView)view.findViewById(R.id.txt_shoukuan);
            view.setTag(holder);
        }else{
            holder=(Holder) view.getTag();
        }
        holder.item_tex.setText(datas.get(position).getText());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "hello", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    private class Holder{

        ImageView item_img;
        TextView item_tex;

        public ImageView getItem_img() {
            return item_img;
        }

        public void setItem_img(ImageView item_img) {
            this.item_img = item_img;
        }

        public TextView getItem_tex() {
            return item_tex;
        }

        public void setItem_tex(TextView item_tex) {
            this.item_tex = item_tex;
        }
    }
}
