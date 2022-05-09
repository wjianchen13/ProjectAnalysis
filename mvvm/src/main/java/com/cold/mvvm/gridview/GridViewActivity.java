package com.cold.mvvm.gridview;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.cold.mvvm.R;
import com.cold.mvvm.databinding.ActivityGridviewBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * name:
 * desc:
 * author:
 * date: 2017-06-22 10:41
 * remark:
 */
public class GridViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityGridviewBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_gridview);

        List<User> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            User user = new User();
            user.setName("用户 " + i );
            user.setIcon("http://qlogo1.store.qq.com/qzone/503233512/503233512/100?1311741184");
            list.add(user);
        }
        CustomAdapter<User> adapter = new CustomAdapter<>(
                this, list, R.layout.item_gv, com.cold.mvvm.BR.user);
        binding.rlytSide.setAdapter(adapter);

    }


}
