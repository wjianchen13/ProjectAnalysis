package com.cold.recyclerview;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView tvTest1;
    private TextView tvTest2;
    private List<String> datas = new ArrayList<>();
    private MyAdapter adapter;
    private List<String> datas1 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        tvTest1 = (TextView) findViewById(R.id.tv_test1);
        setData();
        tvTest1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("==============> onClick");
                if(datas != null && datas.size() > 0) {
                    datas.remove(0);
                }
            }
        });
        tvTest2 = (TextView) findViewById(R.id.tv_test2);
        tvTest2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                datas.clear();
//                datas.addAll(poiItemList);
//                adapter.notifyItemRangeChanged(0, poiItemList.size());
                String add = "add";
                datas.add(add);
                adapter.notifyItemInserted(datas.size() - 1);
            }
        });

        initVertical();
    }

    private void setData() {
        String s1 = "add1";
        datas1.add(s1);
        String s2 = "add2";
        datas1.add(s2);
        String s3 = "add3";
        datas1.add(s3);
        String s4 = "add4";
        datas1.add(s4);
        String s5 = "add5";
        datas1.add(s5);
    }

    public void initVertical(){
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview_vertical);

        // ?????????????????????????????????
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        // ?????????Vertical???????????????
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        // ?????????????????????
        recyclerView.setLayoutManager(layoutManager);

        String s1 = "test1";
        datas.add(s1);
        String s2 = "test2";
        datas.add(s2);
        String s3 = "test3";
        datas.add(s3);

        // ??????Adapter?????????????????????
        adapter = new MyAdapter(datas);
        // ??????Adapter
        recyclerView.setAdapter(adapter);
    }

//    private void initHorizaontal() {
//        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview_horizontal);
//
//        // ?????????????????????????????????
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//        // ?????????????????????
//        recyclerView.setLayoutManager(layoutManager);
//
//        // ???????????????
//        String[] dataset = new String[100];
//        for (int i = 0; i < dataset.length; i++){
//            dataset[i] = "item" + i;
//        }
//        // ??????Adapter?????????????????????
//        MyAdapter adapter = new MyAdapter(dataset);
//        // ??????Adapter
//        recyclerView.setAdapter(adapter);
//    }




}
