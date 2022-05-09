package com.cold.mvvm.listbean;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.cold.mvvm.R;
import com.cold.mvvm.databinding.ActivityListBeanBinding;

import java.util.ArrayList;
import java.util.List;

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
public class ListBeanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityListBeanBinding binding = DataBindingUtil.setContentView(this,R.layout.activity_list_bean);

        User user = new User();
        user.setName("刘世麟");
        user.setNickName("南尘");
        user.setEmail("liushilin@qq.com");
        user.setVip(true);
        user.setLevel(5);
        user.setIcon("http://qlogo1.store.qq.com/qzone/503233512/503233512/100?1311741184");
//        binding.setUser(user);

        User user1 = new User();
        user1.setName("春春儿");
        user1.setNickName(null);
        user1.setVip(false);
        user1.setEmail("nanchen@qq.com");
        user1.setLevel(1);
//        binding.setUser(user1);

        List<User> list = new ArrayList<>();
        list.add(user);
        list.add(user1);
        binding.setUsers(list);
    }
}
