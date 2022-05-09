package com.cold.projectanalysis.drawerlayout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;

import com.cold.projectanalysis.R;


public class TestFragment extends Fragment {
    private View view;
    public ImageButton menuBtn,mesBtn;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.test_fragment, null);
		initView();
		return view;
	}
    public void initView(){
//    	menuBtn=(ImageButton)view.findViewById(R.id.menu_btn);
//    	mesBtn=(ImageButton)view.findViewById(R.id.xiaoxi_btn);
//    	//点击打开左边菜单
//    	menuBtn.setOnClickListener(mine View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				((DrawerLayoutActivity)getActivity()).openLeftLayout();
//			}
//		});
//    	//点击打开右边菜单
//    	mesBtn.setOnClickListener(mine View.OnClickListener() {
//
//    		@Override
//    		public void onClick(View v) {
//    			((DrawerLayoutActivity)getActivity()).openRightLayout();
//    		}
//    	});
    	
    }
}
