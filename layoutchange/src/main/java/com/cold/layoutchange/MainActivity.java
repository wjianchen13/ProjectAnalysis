package com.cold.layoutchange;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText edtvTest = null;
    private TextView tvTest = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtvTest = (EditText)findViewById(R.id.edtv_test);
        tvTest = (TextView)findViewById(R.id.tv_test);
    }
}
