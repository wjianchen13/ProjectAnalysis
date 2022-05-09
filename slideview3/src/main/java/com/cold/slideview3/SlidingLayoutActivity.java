package com.cold.slideview3;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import androidx.recyclerview.widget.RecyclerView;

public class SlidingLayoutActivity extends AppCompatActivity {

    private SlidingLayout mLeftDrawerLayout ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sliding_layout);

        mLeftDrawerLayout = (SlidingLayout) findViewById(R.id.id_drawerlayout);
 //       initHorizaontal();
        initVertical();
    }

//    private void initHorizaontal() {
//        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview_horizontal);
//
//        // 创建一个线性布局管理器
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//        // 设置布局管理器
//        recyclerView.setLayoutManager(layoutManager);
//
//        // 创建数据集
//        String[] dataset = new String[100];
//        for (int i = 0; i < dataset.length; i++){
//            dataset[i] = "item" + i;
//        }
//        // 创建Adapter，并指定数据集
//        MyAdapter adapter = new MyAdapter(dataset);
//        // 设置Adapter
//        recyclerView.setAdapter(adapter);
//    }

    public void initVertical() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview_vertical);

        // 创建一个线性布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        // 默认是Vertical，可以不写
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        // 设置布局管理器
        recyclerView.setLayoutManager(layoutManager);

        // 创建数据集
        String[] dataset = new String[100];
        for (int i = 0; i < dataset.length; i++) {
            dataset[i] = "item" + i;
        }
        // 创建Adapter，并指定数据集
        MyAdapter adapter = new MyAdapter(dataset);
        // 设置Adapter
        recyclerView.setAdapter(adapter);
    }
}
