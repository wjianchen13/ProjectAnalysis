package com.cold.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class MainActivity extends Activity implements OnClickListener{
	private Button ios_dialog_btn,android_dialog_btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		hideNavigationBar();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		ios_dialog_btn = (Button) findViewById(R.id.ios_dialog_btn);
		android_dialog_btn = (Button) findViewById(R.id.android_dialog_btn);
		
		ios_dialog_btn.setOnClickListener(this);
		android_dialog_btn.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ios_dialog_btn:
			CustomDialog.Builder builder = new CustomDialog.Builder(MainActivity.this);
			builder.setTitle(R.string.prompt);
			builder.setMessage(R.string.exit_app);
			builder.setPositiveButton(R.string.confirm, null);
			builder.setNegativeButton(R.string.cancel, null);
			builder.create().show();
			break;
		case R.id.android_dialog_btn:
			AlertDialog.Builder mbuilder = new AlertDialog.Builder(MainActivity.this);
			mbuilder.setTitle(R.string.prompt);
			mbuilder.setMessage(R.string.exit_app);
			mbuilder.setPositiveButton(R.string.confirm, null);
			mbuilder.setNegativeButton(R.string.cancel, null);
			mbuilder.create().show();
			break;

		default:
			break;
		}
	}

	//////////////////////////////////////////////////////////////////////////
	public void hideNavigationBar() {
		int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
				| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
				| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
				| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
				| View.SYSTEM_UI_FLAG_FULLSCREEN; // hide status bar

		if( android.os.Build.VERSION.SDK_INT >= 19 ){
			uiFlags |= 0x00001000;    //SYSTEM_UI_FLAG_IMMERSIVE_STICKY: hide navigation bars - compatibility: building API level is lower thatn 19, use magic number directly for higher API target level
		} else {
			uiFlags |= View.SYSTEM_UI_FLAG_LOW_PROFILE;
		}

		getWindow().getDecorView().setSystemUiVisibility(uiFlags);
	}



	//////////////////////////////////////////////////////////////////////////
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if( hasFocus ) {
			hideNavigationBar();
		}
	}

}
