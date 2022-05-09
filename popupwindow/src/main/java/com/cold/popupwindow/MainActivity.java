package com.cold.popupwindow;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;

import com.cold.popupwindow.pickerview.adapter.ArrayWheelAdapter;
import com.cold.popupwindow.pickerview.lib.WheelView;
import com.cold.popupwindow.pickerview.listener.OnItemSelectedListener;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    final ArrayList<String> chatLvList = new ArrayList<String>(Arrays.asList(new String[]{"不限制","1 一富","2 二富","3 三富","4 四富"}));

    private Button btnTest;

    private PopupWindow mCharPop;
    private View mPopuView;
    private WheelView wv_option;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnTest=(Button) this.findViewById(R.id.btn_test);
        btnTest.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(mCharPop == null) {
                    mPopuView = LayoutInflater.from(MainActivity.this).inflate(R.layout.view_phive_studio_cfg_chat_popu, null);
                    wv_option = (WheelView) mPopuView.findViewById(R.id.wv_option);

                    wv_option.setAdapter(new ArrayWheelAdapter(chatLvList));
                    wv_option.setCyclic(false);
                    wv_option.setCurrentItem(1);
                    wv_option.setOnItemSelectedListener(new OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(int index) {
                            btnTest.setText(chatLvList.get(index));
//                            AppUser.getInstance().setPhiveChatLV(index);
                        }
                    });

                    mCharPop = new PopupWindow(mPopuView,
                            btnTest.getWidth(),
                            WindowManager.LayoutParams.WRAP_CONTENT);
                    mCharPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        public void onDismiss() {
//                            selector(false , iv_up_down_arrow);
                        }
                    });
                    mCharPop.setFocusable(true);
                    mCharPop.setTouchable(true);
                    mCharPop.setOutsideTouchable(true);
                    mCharPop.setBackgroundDrawable(new BitmapDrawable());
                    mCharPop.setAnimationStyle(R.style.phive_studio_cfg_popu);
                    mCharPop.getContentView().setClickable(true);
                    mCharPop.getContentView().setOnKeyListener(new View.OnKeyListener() {

                        @Override
                        public boolean onKey(View v, int keyCode, KeyEvent event) {
                            if ((keyCode == KeyEvent.KEYCODE_BACK) && (mCharPop.isShowing())) {
                                mCharPop.dismiss();
                                mCharPop = null;
                                return true;
                            }
                            return false;
                        }
                    });
                }
                int offsetX = 0;
                int offsetY = 10;
//                selector(true , iv_up_down_arrow);
                mCharPop.showAsDropDown(btnTest, offsetX, offsetY);
            }
        });
    }

    private void selector(boolean flag , View view) {
        view.clearAnimation();
        if (flag) {
            AppUtil.rotateFrom0to180(view, true,null);
        } else {
            AppUtil.rotateFrom180to0(view, true,null);
        }
    }
}
