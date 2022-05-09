package com.cold.slideview2;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SlidingLayoutActivity extends AppCompatActivity {

    private SlidingLayout mLeftDrawerLayout ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sliding_layout);

        mLeftDrawerLayout = (SlidingLayout) findViewById(R.id.id_drawerlayout);
        initHorizaontal();
    }

    private void initHorizaontal() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview_horizontal);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);

        String[] dataset = new String[100];
        for (int i = 0; i < dataset.length; i++){
            dataset[i] = "item" + i;
        }

        MyAdapter adapter = new MyAdapter(dataset);
        recyclerView.setAdapter(adapter);
    }

}
