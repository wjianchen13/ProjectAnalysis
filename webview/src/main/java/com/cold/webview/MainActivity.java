package com.cold.webview;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private WebView contentWebView = null;
    private TextView msgView = null;

    private WebView contentWebView1 = null;
    private WebView contentWebView2 = null;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // 加载 原来的小WebView页面
        contentWebView = (WebView) findViewById(R.id.webview);
        msgView = (TextView) findViewById(R.id.msg);
        // 启用javascript
        contentWebView.getSettings().setJavaScriptEnabled(true);
        contentWebView.setBackgroundColor(Color.parseColor("#00000000"));
        contentWebView.getBackground().setAlpha(0); // 设置填充透明度 范围：0-255
        contentWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (contentWebView != null)
                    contentWebView.loadUrl(url);
                return true;
            }
        });
        contentWebView.setWebChromeClient(new WebChromeClient() {
        });
        // 从assets目录下面的加载html
        contentWebView.loadUrl("http://tapi.95xiu.com/web/active_web_view_match.php");

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(btnClickListener);
//        contentWebView.addJavascriptInterface(this, "wst");

        // 加载最新的小WebView页面
        contentWebView1 = (WebView) findViewById(R.id.wv_contest1);
        contentWebView1.getSettings().setJavaScriptEnabled(true);
        contentWebView1.setBackgroundColor(Color.parseColor("#00000000"));
        contentWebView1.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (contentWebView1 != null)
                    contentWebView1.loadUrl(url);
                return true;
            }
        });
        contentWebView1.setWebChromeClient(new WebChromeClient() {
        });
        // 从assets目录下面的加载html
        contentWebView1.loadUrl("http://tapi.95xiu.com/web/new_active_web_view_match.php");

        // 加载周三送好礼界面
        contentWebView2 = (WebView) findViewById(R.id.wv_contest2);
        contentWebView2.getSettings().setJavaScriptEnabled(true);
        contentWebView2.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (contentWebView2 != null)
                    contentWebView2.loadUrl(url);
                return true;
            }
        });
        contentWebView2.setWebChromeClient(new WebChromeClient() {
        });
        // 从assets目录下面的加载html
//        contentWebView2.loadUrl("http://api2.95xiu.com/web/active_phone.php?id=64");
        contentWebView2.loadUrl("http://image.33wan.com/fruit/fruit_app/index.html");
    }

    View.OnClickListener btnClickListener = new Button.OnClickListener() {
        public void onClick(View v) {
            // 无参数调用
            contentWebView.loadUrl("javascript:updateAnniversaryData()");
            // 传递参数调用
//			contentWebView.loadUrl("javascript:javacalljswithargs(" + "'hello world'" + ")");
        }
    };

    public void startFunction() {
        Toast.makeText(this, "js调用了java函数", Toast.LENGTH_SHORT).show();
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                msgView.setText(msgView.getText() + "\njs调用了java函数");

            }
        });
    }

    public void startFunction(final String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                msgView.setText(msgView.getText() + "\njs调用了java函数传递参数：" + str);

            }
        });
    }
}
