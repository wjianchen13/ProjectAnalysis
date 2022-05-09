package com.cold.dialogfragment;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * 显示底部Dialog
     *
     * @param view 视图
     */
    public void showBottomDialog(View view) {
        FragmentManager fm = getSupportFragmentManager();
        BottomDialogFragment editNameDialog = new BottomDialogFragment();
        editNameDialog.show(fm, "fragment_bottom_dialog");
    }
}
