package com.cold.activitytest;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private Button btnTest1;
    private Button btnTest2;
    private TextView tvTest1;
    private TextView tvTest2;
    private ArrayList<User> users =  new ArrayList<User>();
    private int watch = 0;
    private final static int GIFT_ACTION_COLOR = Color.parseColor("#bae05d");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnTest1 = (Button) this.findViewById(R.id.btn_test1);
        btnTest2 = (Button) this.findViewById(R.id.btn_test2);
        tvTest1 = (TextView) this.findViewById(R.id.tv_test1);
        tvTest2 = (TextView) this.findViewById(R.id.tv_test2);
        tvTest1.setText(getResources().getString(R.string.test1));
        tvTest2.setText(getResources().getString(R.string.test2));
        btnTest1.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent it = new Intent();
                it.setClass(MainActivity.this, FirstActivity.class);
                startActivity(it);
            }
        });

        btnTest2.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent it = new Intent();
                it.setClass(MainActivity.this, SecondActivity.class);
                startActivity(it);
            }
        });
    }

    private int max(int a, int b) {
        return a > b ? a : b;
    }

    private void test() {
        String test = "恭喜 “MR.DJ.Lxp” 成功登上本轮头条，在直播大厅置顶30分钟。速去围观>";
        test = test.replace("速去观>","");
        System.out.println("---------------> test: " + test);
        int index = test.lastIndexOf("。");
        int index1 = test.lastIndexOf("，");
        System.out.println("---------------> index: " + index);
        System.out.println("---------------> index1: " + index1);
    }

//    private void test() {
//        List<String> list = new ArrayList<>();
//        list.add("q");
//        list.add("1q");
//        list.add("2q");
//        list.add("3q");
//        for(String s : list) {
//            System.out.println(s);
//        }
//    }

//        final float img_scale_x = getResources().getInteger(R.integer.live_chat_level_scale_x) / 10.0f; //图标大小
//        for(int i = 0; i < 10; i ++) {
//            User user = new User("test" + i, i);
//            users.add(user);
//        }
//        ArrayList<User> removes = new ArrayList<User>();
//        for(User user : users) {
//            if(user.getAge() % 2 != 0) {
//                removes.add(user);
//            }
//        }
//        if(removes.size() != 0) {
//            users.removeAll(removes);
//        }
//        for(int z = 0; z < users.size(); z ++) {
//            System.out.println("=================> user age: " + users.get(z).getAge());
//        }

//        btnTest.setOnClickListener(new View.OnClickListener() {
//
//            public void onClick(View v) {
//                Drawable img = getResources().getDrawable(R.drawable.level_manager);
//                System.out.println("-----------------> width: " + img.getIntrinsicWidth() + "   height: " + img.getIntrinsicHeight());
//                int intrinsicWidth = img.getIntrinsicWidth(); // 获取图片的宽度 px
//                int intrinsicHeight = img.getIntrinsicHeight(); // 获取图片的高度 px
//                System.out.println("-----------------> dip2px(MainActivity.this, 15): " + dip2px(MainActivity.this, 15));
//                System.out.println("-----------------> intrinsicHeight: " + intrinsicHeight);
//                float factor = (float) dip2px(MainActivity.this, 15) / intrinsicHeight; // 转换因子
//                System.out.println("-----------------> factor: " + factor);
//                DisplayMetrics dm = new DisplayMetrics();
//                getWindowManager().getDefaultDisplay().getMetrics(dm);
//                int densityDpi = dm.densityDpi;
//
//
//                System.out.println("-----------------> dip2px(15): " + dip2px(MainActivity.this, 15));
//            }
//        });
//    }

    class User {
        private String name ;
        private int age;

        public User(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        return (int) (dpValue * getDensity(context) + 0.5f);
    }

    /**
     * 返回屏幕密度
     */
    public static float getDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }
}
