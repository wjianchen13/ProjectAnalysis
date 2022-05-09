package com.cold.surfaceview;

import android.app.Activity;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ImageView;


/**
 * 登录注册页面
 */
public class MainActivity1 extends Activity
{
    private SurfaceView surfaceView;
    private MediaPlayer mediaPlayer;
    private SurfaceHolder surfaceHolder;
    private boolean isSurfaceCreated =false;        //surface是否已经创建好
    private Uri mUri;
    private int curIndex = 0;
    private ImageView imgvTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imgvTest = (ImageView) findViewById(R.id.imgv_test);
        initPlayerObj();
        InitControl();
        try {
            getLast();
        } catch (Exception e) {

        }
    }

    private void initPlayerObj()
    {

//        mUri = Uri.parse("android.resource://" + getPackageName() + "/"+ R.raw.guidvideo);
        surfaceView = (SurfaceView) findViewById(R.id.sv);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);      //设置视频流类型

        CreateSurface();
    }

    private void InitControl()
    {
//        LinearLayout LoginRegLayout = (LinearLayout)findViewById(R.id.log_reg_btn);
//        LinearLayout LookRoundLayout = (LinearLayout)findViewById(R.id.look_round_btn);
//        ImageView SkipImg = (ImageView)findViewById(R.id.skip_img);                //右上角跳过文本

//        ClickEventDelegate ClickListener = new ClickEventDelegate();
//        LoginRegLayout.setOnClickListener(ClickListener);
//        LookRoundLayout.setOnClickListener(ClickListener);
//
//        SkipImg.setOnClickListener(ClickListener);

    }

//    private class ClickEventDelegate implements View.OnClickListener
//    {
//        @Override
//        public void onClick(View v)
//        {
//            switch(v.getId())
//            {
//                case R.id.log_reg_btn:
//                    String[] key = {"Register_Way"};
//                    String[] values = {"GuiVideoActivity"};
//                    PageSwitch.GoActivityByParams(GuidVideoActivity.this,LoginRegistActivity.class,key,values);
//                    finish();
//                    break;
//
//                case R.id.look_round_btn:
//                    PageSwitch.GoActivityNoParams(GuidVideoActivity.this,MainActivity.class);
//                    finish();
//                    break;
//
//                case R.id.skip_img:
//                    PageSwitch.GoActivityNoParams(GuidVideoActivity.this,MainActivity.class);
//                    finish();
//                    break;
//            }
//        }
//    }

    /**
     * 创建视频展示页面
     */
    private void CreateSurface()
    {
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS); //兼容4.0以下的版本
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

                isSurfaceCreated = false;
                if(mediaPlayer.isPlaying()){ //此处需要注意
                    curIndex = mediaPlayer.getCurrentPosition();
                    mediaPlayer.stop();
                }
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                isSurfaceCreated = true;
                mediaPlayer.setDisplay(surfaceHolder);//页面创建好了以后再展示
                Play(6037,mUri);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format,
                                       int width, int height) {

            }
        });

    }

    /**
     * 释放播放器资源
     */
    private void ReleasePlayer()
    {
        if(mediaPlayer!=null)
        {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }

    }

    /**
     * 暂停
     */
    private void Pause()
    {
        if (mediaPlayer != null && mediaPlayer.isPlaying())
        {
            curIndex = mediaPlayer.getCurrentPosition();
            mediaPlayer.pause();

        }

    }


    private void Play(final int currentPosition,Uri mUri)
    {
        try
        {
            mediaPlayer.reset();
            mediaPlayer.setDataSource("/sdcard/ykzzldx.mp4");
            mediaPlayer.setDisplay(surfaceView.getHolder());
//            mediaPlayer.setLooping(true);
            mediaPlayer.prepareAsync();
            int duration = mediaPlayer.getDuration();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener()
            {

                @Override
                public void onPrepared(MediaPlayer mp)
                {
                    mediaPlayer.seekTo(currentPosition);
                    mediaPlayer.start();
                }
            });

            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {

                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {

                    return false;
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    // 在播放完毕被回调
//                    btn_play.setEnabled(true);
//                    last = mediaPlayer.getCurrentPosition();
//                    mp.get
                }
            });
        }
        catch (Exception e)
        {

        }
    }

    /**
     * 创建完毕页面后需要将播放操作延迟10ms防止因surface创建不及时导致播放失败
     */
//    @Override
//    protected void onResume()
//    {
//        super.onResume();
//        new Handler().postDelayed(new Runnable()
//        {
//            public void run()
//            {
//                if(isSurfaceCreated)
//                {
//                    Play(curIndex,mUri);
//                }
//            }
//        }, 1000);
//
//    }

    /**
     * 页面从前台到后台会执行 onPause ->onStop 此时Surface会被销毁，
     * 再一次从后台 到前台时需要 重新创建Surface
     */
    @Override
    protected void onRestart()
    {
        super.onRestart();
//        if(!isSurfaceCreated)
//        {
//            CreateSurface();
//        }
    }


    @Override
    protected void onPause()
    {
        super.onPause();
        Pause();
    }

    @Override
    protected void onStop()
    {
        super.onStop();

    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        ReleasePlayer();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN)
        {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void getLast() throws Exception{
        //创建MediaMetadataRetriever对象

        MediaMetadataRetriever mmr=new MediaMetadataRetriever();
        //设置资源位置
        String path="/sdcard/ykzzld1x.mp4";//绑定资源
        mmr.setDataSource(path);

        String time = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);

        int seconds = Integer.valueOf(time);
        //获取第一帧图像的bitmap对象
//        Bitmap bitmap=mmr.getFrameAtTime();
//        Bitmap bitmap = retriever.getFrameAtTime(i*1000*1000,MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
        Bitmap bitmap = mmr.getFrameAtTime(seconds * 1000,MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
        //加载到ImageView控件上
        imgvTest.setImageBitmap(bitmap);
    }
}