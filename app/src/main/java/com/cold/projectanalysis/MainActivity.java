package com.cold.projectanalysis;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cold.projectanalysis.comboview.ComboViewActivity;
import com.cold.projectanalysis.drawerlayout.DrawerLayoutActivity;
import com.cold.projectanalysis.paintorview.PaintorViewActivity;
import com.cold.projectanalysis.slidelayout.SlideLayoutActivity;
import com.cold.projectanalysis.spantext.SpanActivity;

public class MainActivity extends AppCompatActivity implements RecycleViewItemClickListener {

    private String[] dataset = new String[100];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.recyclerview_vertical);
        initVertical();
    }

    public void initVertical(){
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview_vertical);

        // 创建一个线性布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        // 默认是Vertical，可以不写
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        // 设置布局管理器
        recyclerView.setLayoutManager(layoutManager);

        initData();

        // 创建Adapter，并指定数据集
        MyAdapter adapter = new MyAdapter(dataset, this);
        // 设置Adapter
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View v, int position) {
        switch(position) {
            case 0:
                Intent it0 = new Intent();
                it0.setClass(MainActivity.this, PaintorViewActivity.class);
                startActivity(it0);
                break;
            case 1:
                Intent it1 = new Intent();
                it1.setClass(MainActivity.this, ComboViewActivity.class);
                startActivity(it1);
                break;
            case 2:
                Intent it2 = new Intent();
                it2.setClass(MainActivity.this, DrawerLayoutActivity.class);
                startActivity(it2);
                break;
            case 3:
                Intent it3 = new Intent();
                it3.setClass(MainActivity.this, SlideLayoutActivity.class);
                startActivity(it3);
                break;
            case 4:
                Intent it4 = new Intent();
                it4.setClass(MainActivity.this, SpanActivity.class);
                startActivity(it4);
                break;
            default:
                break;
        }
    }

    private void initData() {
        dataset[0] = "冒泡";
        dataset[1] = "连击";
        dataset[2] = "侧滑菜单";
        dataset[3] = "侧滑布局";
        dataset[4] = "SpanText";
    }
}
