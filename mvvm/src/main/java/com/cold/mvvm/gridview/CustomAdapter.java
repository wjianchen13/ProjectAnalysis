package com.cold.mvvm.gridview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import java.util.List;

/**
 * name:
 * desc:
 * author:
 * date: 2017-06-22 10:41
 * remark:
 */
public class CustomAdapter<T> extends BaseAdapter {
    private Context context;//上下文环境
    private List<T> list;//通用的，不知道数据
    private int layoutId;//通用的，不知道布局
    private int variableId;//变量的id

    /**
     * 构造方法
     */
    public CustomAdapter(Context context, List<T> list, int layoutId, int variableId) {
        this.context = context;
        this.list = list;
        this.layoutId = layoutId;
        this.variableId = variableId;
    }

    @Override
    public int getCount() {
        if (list!=null)
            return list.size();
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewDataBinding binding = null;
        if (convertView == null){
            binding = DataBindingUtil.inflate(LayoutInflater.from(context),layoutId,parent,false);
        }else{
            binding = DataBindingUtil.getBinding(convertView);
        }
        System.out.println("---------------------> icon: " + ((User)(list.get(position))).getIcon());
        binding.setVariable(variableId,list.get(position));
        return binding.getRoot();
    }
}
