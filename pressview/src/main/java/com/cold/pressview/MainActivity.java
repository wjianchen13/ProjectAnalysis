package com.cold.pressview;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<MyData> datas = new ArrayList<MyData>();

    //定义图标数组
    private int[] imageRes = {
            R.drawable.mofa_normal,
            R.drawable.mofa_normal,
            R.drawable.mofa_normal,
            R.drawable.mofa_normal,
            R.drawable.mofa_normal,
            R.drawable.mofa_normal,
            R.drawable.mofa_normal,
            R.drawable.mofa_normal,
            R.drawable.mofa_normal};

    //定义图标下方的名称数组
    private String[] name = {
            "O2O收款",
            "订单查询",
            "转账",
            "手机充值",
            "信用卡还款",
            "水电煤",
            "违章代缴",
            "快递查询",
            "更多"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GridView gridview = (GridView) findViewById(R.id.gridview);
        int length = imageRes.length;

        //生成动态数组，并且转入数据
//        ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();
//        for (int i = 0; i < length; i++) {
//            HashMap<String, Object> map = new HashMap<String, Object>();
//            map.put("ItemImage", imageRes[i]);//添加图像资源的ID
//            map.put("ItemText", name[i]);//按序号做ItemText
//            lstImageItem.add(map);
//        }

        for(int i = 0; i < length; i ++) {
            MyData data = new MyData();
            data.setImg(imageRes[i]);
            data.setText(name[i]);
            datas.add(data);
        }

        MyAdapter adapter = new MyAdapter(this, datas);

        //生成适配器的ImageItem 与动态数组的元素相对应
//        SimpleAdapter saImageItems = new SimpleAdapter(this,
//                lstImageItem,//数据来源
//                R.layout.gvitem,//item的XML实现
//
//                //动态数组与ImageItem对应的子项
//                new String[]{"ItemImage", "ItemText"},
//
//                //ImageItem的XML文件里面的一个ImageView,两个TextView ID
//                new int[]{R.id.img_shoukuan, R.id.txt_shoukuan});
        //添加并且显示
        gridview.setAdapter(adapter);
        //添加消息处理
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this,name[position],Toast.LENGTH_LONG).show();
            }
        });
    }
}
