package com.cold.activitytest;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class SecondActivity extends BaseActivity {

    private Button btnTest;
    private TextView tvTest1;
    private TextView tvTest2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        btnTest = (Button) this.findViewById(R.id.btn_test);
        tvTest1 = (TextView) this.findViewById(R.id.tv_test1);
        tvTest2 = (TextView) this.findViewById(R.id.tv_test2);
        tvTest1.setText("" + flag);
        tvTest2.setText(getResources().getString(R.string.test2));
        btnTest.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

            }
        });
    }

}
