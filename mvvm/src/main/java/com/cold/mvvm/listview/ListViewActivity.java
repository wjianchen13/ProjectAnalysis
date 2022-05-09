package com.cold.mvvm.listview;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.cold.mvvm.R;
import com.cold.mvvm.databinding.ActivityListviewBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * name:
 * desc:
 * author:
 * date: 2017-06-22 10:41
 * remark:
 */
public class ListViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityListviewBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_listview);

        List<User> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            User user = new User();
            user.setName("用户 " + i );
            user.setIcon("http://qlogo1.store.qq.com/qzone/503233512/503233512/100?1311741184");
            list.add(user);
        }
        CustomAdapter<User> adapter = new CustomAdapter<>(
                this, list, R.layout.item_lv, com.cold.mvvm.BR.user);
        binding.setAdapter(adapter);

    }


}
