package cold.com.slideview5;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SlidingLayoutActivity extends AppCompatActivity {

    private Button btnTest;
    private SlidingLayout mLeftDrawerLayout ;
    private List<String> datas = new ArrayList<String>();
    int i = 0;
    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sliding_layout);

        btnTest = (Button) findViewById(R.id.btn_test);
        btnTest.setOnClickListener(clickListener);


        mLeftDrawerLayout = (SlidingLayout) findViewById(R.id.id_drawerlayout);
 //       initHorizaontal();
        initVertical();
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String str = "item" + i ++;
            datas.add(str);
            adapter.notifyDataSetChanged();
        }
    };

//    private void initHorizaontal() {
//        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview_horizontal);
//
//        // 创建一个线性布局管理器
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//        // 设置布局管理器
//        recyclerView.setLayoutManager(layoutManager);
//
//        // 创建数据集
//        String[] dataset = new String[100];
//        for (int i = 0; i < dataset.length; i++){
//            dataset[i] = "item" + i;
//        }
//        // 创建Adapter，并指定数据集
//        MyAdapter adapter = new MyAdapter(dataset);
//        // 设置Adapter
//        recyclerView.setAdapter(adapter);
//    }

    public void initVertical() {
        MyRecyclerView recyclerView = (MyRecyclerView) findViewById(R.id.recyclerview_vertical);
//        recyclerView.setFocusable(true);
//        recyclerView.setClickable(true);
        recyclerView.setOnTouchListener(new MyTouchListener());
        // 创建一个线性布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        // 默认是Vertical，可以不写
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        // 设置布局管理器
        recyclerView.setLayoutManager(layoutManager);

        // 创建数据集
        for (i = 0; i < 3; i++) {
            String str = "item" + i;
            datas.add(str);
        }
        // 创建Adapter，并指定数据集
        adapter = new MyAdapter(datas);
        // 设置Adapter
        recyclerView.setAdapter(adapter);
    }

    private class MyTouchListener implements View.OnTouchListener {

        private boolean flag = false;

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    flag = false;
                    System.out.println("=================================> onTouch ACTION_DOWN");
                    break;
                case MotionEvent.ACTION_MOVE:
                    flag = true;
                    System.out.println("=================================> onTouch ACTION_MOVE");
                    break;
                case MotionEvent.ACTION_UP:
                    if(!flag) {
                        Toast.makeText(SlidingLayoutActivity.this, "onTouch", Toast.LENGTH_SHORT).show();
                    }
                    System.out.println("=================================> onTouch ACTION_UP");
                    break;
            }
            return false;
        }
    }
}
