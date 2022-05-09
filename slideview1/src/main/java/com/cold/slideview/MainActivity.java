package com.cold.slideview;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.LinearLayout;

import com.cold.slideview.view.IClearEvent;
import com.cold.slideview.view.MyAdapter;
import com.cold.slideview.view.RelativeRootView;

public class MainActivity extends AppCompatActivity  {

    private LinearLayout llyt_test1 = null;
    private LinearLayout tvTest2 = null;

    /**
     * 清屏父布局
     */
    private RelativeRootView rl_helper1;



    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_slide_layout);

        llyt_test1 = (LinearLayout)findViewById(R.id.llyt_test1);
        tvTest2 = (LinearLayout)findViewById(R.id.tv_test2);

        rl_helper1 = (RelativeRootView) findViewById(R.id.rl_helper1);

//        rl_helper1.bind(llyt_test1);
        rl_helper1.setIClearEvent(new IClearEvent() {
            @Override
            public void onClearEnd() {

            }

            @Override
            public void onRecovery() {

            }
        });
        initHorizaontal();
    }

    private void initHorizaontal() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview_horizontal);

        // 鍒涘缓涓€涓嚎鎬у竷灞€绠＄悊鍣?
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        // 璁剧疆甯冨眬绠＄悊鍣?
        recyclerView.setLayoutManager(layoutManager);

        // 鍒涘缓鏁版嵁闆?
        String[] dataset = new String[100];
        for (int i = 0; i < dataset.length; i++){
            dataset[i] = "item" + i;
        }
        // 鍒涘缓Adapter锛屽苟鎸囧畾鏁版嵁闆?
        MyAdapter adapter = new MyAdapter(dataset);
        // 璁剧疆Adapter
        recyclerView.setAdapter(adapter);
    }
}
