package com.cold.projectanalysis.comboview;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.cold.projectanalysis.R;
import com.cold.projectanalysis.paintorview.bubble.PhiveBubbleEffect;

public class ComboViewActivity extends AppCompatActivity {

    /**
     * 气泡
     */
    protected PhiveBubbleEffect mPhiveBubbleEffect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combo_view);

    }

}
