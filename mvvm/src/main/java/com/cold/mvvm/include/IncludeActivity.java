package com.cold.mvvm.include;

import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.cold.mvvm.R;
import com.cold.mvvm.databinding.ActivityIncludeBinding;

/**
 * name: IncludeActivity
 * desc: 布局含有<include></include>标签处理
 * author:
 * date: 2017-06-22 17:00
 * remark:
 */
public class IncludeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        ActivityIncludeBinding binding = DataBindingUtil.setContentView(IncludeActivity.this, R.layout.activity_include);

        Content con = new Content("Title","SubTitle");
        binding.setCon(con);

//        binding.toolbar.setContent(con);  //这个测试没有效果，不会显示toolbar的title/subTitle
//        binding.toolbar.toolbar.setTitle("");
//        binding.toolbar.toolbar.setSubtitle("");

        //下面的代码也可以通过DataBinding绑定数据
        binding.toolbar.toolbar.setNavigationIcon(R.mipmap.ic_launcher);
        setSupportActionBar(binding.toolbar.toolbar);
        binding.toolbar.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


}
