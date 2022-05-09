package com.cold.mvvm.base;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.cold.mvvm.R;
import com.cold.mvvm.databinding.ActivityBaseBinding;

/**
 * databinding使用步骤：
 * 1.在模块的.gradle文件添加：
 *  dataBinding{
 *      enabled = true
 *  }
 *  2.创建一个java bean，添加事件绑定方法
 *  3.xml根标签使用layout，在各控件中使用@{}指定属性和事件
 *  4.在Activity使用获取binding引用
 *  ActivityXxxBinding binding = DataBindingUtil.setContentView(this,R.layout.activity.xxx);s
 *
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityBaseBinding binding = DataBindingUtil.setContentView(this,R.layout.activity_base);

        User user = new User();
        user.setName("被坑惨了");
        user.setNickName("cold");
        user.setEmail("1812821235@qq.com");
        user.setVip(true);
        user.setLevel(5);
        binding.setUser(user);
    }
}
