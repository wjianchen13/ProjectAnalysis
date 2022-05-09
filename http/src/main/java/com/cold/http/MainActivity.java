package com.cold.http;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.cold.http.OkHttpUtil.mOkHttpClient;

public class MainActivity extends AppCompatActivity {

    String url1 = "http://image.95xiu.com/upload/down/gift_animationv3.zip?v=251";
    String url2 = "http://tapi.95xiu.com/upload/down/gift_animationv3.zip?v=293";

    private Button btnTest;
    private TextView tvshow;
    private Button btdownload;
    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnTest = (Button)findViewById(R.id.btn_test);
        btnTest.setOnClickListener(clicklistener);
        this.btdownload = (Button) findViewById(R.id.bt_download);
        this.tvshow = (TextView) findViewById(R.id.tv_show);
        btdownload.setOnClickListener(clicklistener);
    }


    private View.OnClickListener clicklistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.btn_test:
                    new MyThread().start();
                    break;
                case R.id.bt_download:
                    downloadPic();
                    break;
            }

        }
    };


    /**
     * 图片下载
     */
    private void downloadPic() {

        DownloadProgressListener uiProgressResponseListener = new DownloadProgressListener() {
            @Override
            public void onUIResponseProgress(long bytesRead, long contentLength, boolean done) {
                System.out.println("------------------->bytesRead: " + bytesRead + "  contentLength: " + contentLength +  "  done: " + done);
                tvshow.setText("下载进度 " + ((100 * bytesRead) / contentLength) + "%");
            }
        };

        //构造请求
        final Request request1 = new Request.Builder()
                .url("http://tapi.95xiu.com/upload/down/gift_animationv3.zip?v=293")
                .build();

        //包装Response使其支持进度回调
        ProgressHelper.addProgressResponseListener(mOkHttpClient, uiProgressResponseListener)
                .newCall(request1)
                .enqueue(new Callback() {
                             @Override
                             public void onFailure(Request request, IOException e) {

                             }

                             @Override
                             public void onResponse(Response response) throws IOException {
                                 InputStream inputStream = null;
                                 OutputStream output = null;
                                 try {
                                     inputStream = response.body().byteStream();
                                     file = new File("/mnt/sdcard/test/");
                                     if(!file.exists()) {
                                         file.mkdirs();
                                     }
                                     file = new File("/mnt/sdcard/test/", "gifts.zip");
                                     output = new FileOutputStream(file);
                                     byte[] buff = new byte[1024 * 4];
                                     while (true) {
                                         int readed = inputStream.read(buff);
                                         if (readed == -1) {
                                             break;
                                         }
                                         //write buff
                                         output.write(buff, 0, readed);
                                     }
                                     output.flush();
                                 } catch (IOException e) {
                                     file = null;
                                     e.printStackTrace();
                                 } finally {
                                     if (inputStream != null) {
                                         inputStream.close();
                                     }
                                     if (output != null) {
                                         output.close();
                                     }
                                     unGiftZip();
                                 }
                             }
                         }
                );
    }

    /**
     * 解压zip
     */
    private void unGiftZip() {
        System.out.println("---------------------> unGiftZip 1: ");
        try {
            try {
                System.out.println("---------------------> unGiftZip 2: ");
                ZipFileUtil.UnZipFolder("/mnt/sdcard/test/gifts.zip", "mnt/sdcard/test/giftUnzip");
                // 创建解压成功标记文件
                createGiftUnZipSuccessedTagFile();
                System.out.println("---------------------> unGiftZip 3: ");
            } catch (Exception e) {
                System.out.println("---------------------> unGiftZip 4: ");
                e.printStackTrace();
            }
        } catch (Exception e) {
            System.out.println("---------------------> unGiftZip 5: ");
            e.printStackTrace();
        }
    }

    public boolean createGiftUnZipSuccessedTagFile() {

        String pathDir = "mnt/sdcard/test/giftUnzip"+ "/gifts/";
        System.out.println("---------------------> createGiftUnZipSuccessedTagFile pathDir : " + pathDir);
        File file = new File(pathDir);
        if(!file.exists()) {
            file.mkdirs();
        }
        System.out.println("---------------------> createGiftUnZipSuccessedTagFile:(file != null) " + (file != null) + "    file.exists(): " +  file.exists() + "    file.isDirectory():" +  file.isDirectory() );
        if (file != null && file.exists() && file.isDirectory()) {
            System.out.println("---------------------> createGiftUnZipSuccessedTagFile 2 : ");
            File tagFile = new File(pathDir + "/mmTag.aa");
            if (!tagFile.exists()) {
                System.out.println("---------------------> createGiftUnZipSuccessedTagFile 3 : ");
                try {
                    System.out.println("---------------------> createGiftUnZipSuccessedTagFile 4 : ");
                    tagFile.createNewFile();
                    return true;
                } catch (IOException e) {
                    System.out.println("---------------------> createGiftUnZipSuccessedTagFile 5 : ");
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    return false;
                }
            }
        }
        return false;
    }

    class MyThread extends Thread {
        @Override
        public void run() {
            HttpURLConnection connection = null;
            try {
                URL url = new URL(url2);
                connection = (HttpURLConnection) url.openConnection();
                HttpURLConnection.setFollowRedirects(true);
                connection.setConnectTimeout(200000);
                connection.setReadTimeout(200000);
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Accept-Encoding", "identity");
                int code = connection.getResponseCode();
                System.out.println("---------------------> code: " + code);
                if (code == 200 || code == 301) {
                    long fileTotalSize = connection.getContentLength();
                    System.out.println("---------------------> fileTotalSize: " + fileTotalSize);
                }
            } catch(Exception e){
                e.printStackTrace();
            }finally{
                if(connection != null) connection.disconnect();
            }
        }
    }

}
