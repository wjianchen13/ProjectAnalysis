package com.cold.projectanalysis.livelayout;

import android.os.Bundle;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.cold.projectanalysis.R;


public class LiveLayoutActivity extends FragmentActivity {

    private TextView text;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.main_frame_activity);

    }

}
