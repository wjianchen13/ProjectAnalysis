package com.cold.mvvm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.cold.mvvm.base.BaseActivity;
import com.cold.mvvm.gridview.GridViewActivity;
import com.cold.mvvm.include.IncludeActivity;
import com.cold.mvvm.listbean.ListBeanActivity;
import com.cold.mvvm.listview.ListViewActivity;

public class MainActivity extends AppCompatActivity {

    private Button btnBase = null;
    private Button btnListBean = null;
    private Button btnListView = null;
    private Button btnInclude = null;
    private Button btnGridview = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnBase = (Button)findViewById(R.id.btn_base);
        btnBase.setOnClickListener(clickListener);

        btnListBean = (Button)findViewById(R.id.btn_list_bean);
        btnListBean.setOnClickListener(clickListener);

        btnListView = (Button)findViewById(R.id.btn_list_view);
        btnListView.setOnClickListener(clickListener);

        btnInclude = (Button)findViewById(R.id.btn_include);
        btnInclude.setOnClickListener(clickListener);

        btnGridview = (Button)findViewById(R.id.btn_gridview);
        btnGridview.setOnClickListener(clickListener);

    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.btn_base:
                    Intent it = new Intent();
                    it.setClass(MainActivity.this, BaseActivity.class);
                    startActivity(it);
                    break;
                case R.id.btn_list_bean:
                    Intent it1 = new Intent();
                    it1.setClass(MainActivity.this, ListBeanActivity.class);
                    startActivity(it1);
                    break;
                case R.id.btn_list_view:
                    Intent it2 = new Intent();
                    it2.setClass(MainActivity.this, ListViewActivity.class);
                    startActivity(it2);
                    break;
                case R.id.btn_include:
                    Intent it3 = new Intent();
                    it3.setClass(MainActivity.this, IncludeActivity.class);
                    startActivity(it3);
                    break;
                case R.id.btn_gridview:
                    Intent it4 = new Intent();
                    it4.setClass(MainActivity.this, GridViewActivity.class);
                    startActivity(it4);
                    break;
            }

        }
    };
}
