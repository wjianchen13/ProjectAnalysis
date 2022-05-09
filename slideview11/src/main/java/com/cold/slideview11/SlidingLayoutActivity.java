package com.cold.slideview11;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

public class SlidingLayoutActivity extends AppCompatActivity {

    private Button btnTest;
    private SlidingLayout mLeftDrawerLayout ;
    private List<String> datas = new ArrayList<String>();
    int i = 0;
    private MyAdapter adapter;

    private Button btnTest2;
    private List<String> datas1 = new ArrayList<String>();
    private MyAdapter adapter1;

    private RelativeLayout rlytFirst;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sliding_layout);

        btnTest = (Button) findViewById(R.id.btn_test);
        btnTest.setOnClickListener(clickListener1);

        btnTest2 = (Button) findViewById(R.id.btn_test2);
        btnTest2.setOnClickListener(clickListener2);

        rlytFirst = (RelativeLayout) findViewById(R.id.rlyt_first);
        rlytFirst.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                System.out.println("-=================================> rlytFirst  onTouch");
                return false;
            }
        });

        mLeftDrawerLayout = (SlidingLayout) findViewById(R.id.id_drawerlayout);
//        mLeftDrawerLayout.setOnTouchListener(mLeftDrawerLayout);
 //       initHorizaontal();
        initVertical();
        initSlide();
    }

    private View.OnClickListener clickListener1 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            System.out.println("-------------------------> clickListener1 i: " +  i);
            String str = "item" + i ++;
            datas.add(str);
            adapter.notifyDataSetChanged();
        }
    };

    private View.OnClickListener clickListener2 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String str = "item" + i ++;
            datas1.add(str);
            adapter1.notifyDataSetChanged();
        }
    };

    private void initSlide() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview_slide);

        // 创建一个线性布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        // 设置布局管理器
        recyclerView.setLayoutManager(layoutManager);

        // 创建数据集
        for (i = 0; i < 3; i++) {
            String str = "item" + i;
            datas1.add(str);
        }
        // 创建Adapter，并指定数据集
        adapter1 = new MyAdapter(datas1);
        // 设置Adapter
        recyclerView.setAdapter(adapter1);
    }

    public void initVertical() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview_vertical);

        // 创建一个线性布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        // 默认是Vertical，可以不写
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        // 设置布局管理器
        recyclerView.setLayoutManager(layoutManager);

        // 创建数据集
        for (i = 0; i < 3; i++) {
            String str = "item" + i;
            datas.add(str);
        }
        // 创建Adapter，并指定数据集
        adapter = new MyAdapter(datas);
        // 设置Adapter
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            if(mLeftDrawerLayout != null && mLeftDrawerLayout.isSlideMenuOpen()) {
                mLeftDrawerLayout.closeSlideMenu();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
