package com.cold.progressimageview;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements IProgressChangeListener{

    private ProgressImageView imgvTest;
    private Button btnTest = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imgvTest = (ProgressImageView)findViewById(R.id.test);
        imgvTest.setmRefreshTime(20);
        imgvTest.setChangeListener(this);
        btnTest = (Button)findViewById(R.id.btn_test);
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgvTest.startPlay();
            }
        });
    }

    @Override
    public void onLoadFinish() {
        Toast.makeText(MainActivity.this, "finish", Toast.LENGTH_SHORT).show();
    }
}
