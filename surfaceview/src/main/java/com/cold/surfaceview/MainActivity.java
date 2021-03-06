package com.cold.surfaceview;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends Activity {
    private final String TAG = "main";
    private EditText et_path;
    private SurfaceView sv;
    private Button btn_play, btn_pause, btn_replay, btn_stop;
    private MediaPlayer mediaPlayer;
    private SeekBar seekBar;
    private int currentPosition = 0;
    private boolean isPlaying;
    private LinearLayout llytParent;
    private LinearLayout llytTest;
    private int last;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        llytParent = (LinearLayout) findViewById(R.id.llyt_parent);
//        llytTest = (LinearLayout) findViewById(R.id.llyt_test);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        sv = (SurfaceView) findViewById(R.id.sv);
//        sv = new SurfaceView(this);
//        sv.setVisibility(View.GONE);
        sv.getHolder().addCallback(callback);
//        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
//        sv.setLayoutParams(lp2);
//        llytTest.addView(sv);
        et_path = (EditText) findViewById(R.id.et_path);

        btn_play = (Button) findViewById(R.id.btn_play);
        btn_pause = (Button) findViewById(R.id.btn_pause);
        btn_replay = (Button) findViewById(R.id.btn_replay);
        btn_stop = (Button) findViewById(R.id.btn_stop);

        btn_play.setOnClickListener(click);
        btn_pause.setOnClickListener(click);
        btn_replay.setOnClickListener(click);
        btn_stop.setOnClickListener(click);

        // ???SurfaceHolder????????????
//        sv.getHolder().addCallback(callback);

        // 4.0?????????????????????????????????
        // ??????Surface???????????????????????????????????????????????????????????????????????????????????????
        // sv.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        // ????????????????????????????????????
        seekBar.setOnSeekBarChangeListener(change);
    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    private Callback callback = new Callback() {
        // SurfaceHolder????????????????????????
        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            Log.i(TAG, "SurfaceHolder ?????????");
            // ??????SurfaceHolder???????????????????????????????????????????????????
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                currentPosition = mediaPlayer.getCurrentPosition();
                mediaPlayer.stop();
            }
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            Log.i(TAG, "SurfaceHolder ?????????");
            if (currentPosition > 0) {
                // ??????SurfaceHolder???????????????????????????????????????????????????????????????????????????????????????
                play(currentPosition);
                currentPosition = 0;
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                   int height) {
            Log.i(TAG, "SurfaceHolder ???????????????");
        }

    };

    private OnSeekBarChangeListener change = new OnSeekBarChangeListener() {

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // ???????????????????????????????????????
            // ??????????????????????????????
            int progress = seekBar.getProgress();
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                // ???????????????????????????
                mediaPlayer.seekTo(progress);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {

        }
    };

    private View.OnClickListener click = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.btn_play:
                    play(7037);
                    break;
                case R.id.btn_pause:
                    pause();
                    break;
                case R.id.btn_replay:
                    replay();
                    break;
                case R.id.btn_stop:
                    stop();
                    break;
                default:
                    break;
            }
        }
    };


    /*
     * ????????????
     */
    protected void stop() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            btn_play.setEnabled(true);
            isPlaying = false;
        }
    }

    /**
     * ????????????
     *
     * @param msec ??????????????????
     */
    protected void play(final int msec) {
        // ????????????????????????
        String path = et_path.getText().toString().trim();
        File file = new File(path);
        if (!file.exists()) {
            Toast.makeText(this, "????????????????????????", 0).show();
            return;
        }
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            // ????????????????????????
            mediaPlayer.setDataSource(file.getAbsolutePath());
            // ?????????????????????SurfaceHolder
            mediaPlayer.setDisplay(sv.getHolder());
            Log.i(TAG, "????????????");
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new OnPreparedListener() {

                @Override
                public void onPrepared(MediaPlayer mp) {
                    Log.i(TAG, "????????????");
//                    mediaPlayer.start();
                    // ????????????????????????
                    mediaPlayer.seekTo(msec);
                    // ???????????????????????????????????????????????????????????????
                    int duration = mediaPlayer.getDuration();
                    seekBar.setMax(mediaPlayer.getDuration());
                    // ???????????????????????????????????????
                    new Thread() {

                        @Override
                        public void run() {
                            try {
                                isPlaying = true;
                                while (isPlaying) {
                                    int current = mediaPlayer
                                            .getCurrentPosition();
                                    seekBar.setProgress(current);

                                    sleep(500);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();

                    btn_play.setEnabled(false);
                }
            });
            mediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
                public void onSeekComplete(MediaPlayer m) {
                    m.start();
                    isPlaying = true;
                }
            });
            mediaPlayer.setOnCompletionListener(new OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    // ????????????????????????
                    btn_play.setEnabled(true);
                    last = mediaPlayer.getCurrentPosition();
                }
            });

            mediaPlayer.setOnErrorListener(new OnErrorListener() {

                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    // ????????????????????????
                    play(0);
                    isPlaying = false;
                    return false;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * ??????????????????
     */
    protected void replay() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.seekTo(0);
            Toast.makeText(this, "????????????", 0).show();
            btn_pause.setText("??????");
            return;
        }
        isPlaying = false;
        play(0);


    }

    /**
     * ???????????????
     */
    protected void pause() {
        if (btn_pause.getText().toString().trim().equals("??????")) {
            btn_pause.setText("??????");
            mediaPlayer.start();
            Toast.makeText(this, "????????????", 0).show();
            return;
        }
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            btn_pause.setText("??????");
            Toast.makeText(this, "????????????", 0).show();
        }

    }

}
