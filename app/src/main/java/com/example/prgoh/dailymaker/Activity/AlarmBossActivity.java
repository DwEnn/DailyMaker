package com.example.prgoh.dailymaker.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.prgoh.dailymaker.Item.Code;
import com.example.prgoh.dailymaker.Item.CustomToast;
import com.example.prgoh.dailymaker.Item.GonSurfaceView;
import com.example.prgoh.dailymaker.R;
import com.example.prgoh.dailymaker.Util.BaseActivity;
import com.example.prgoh.dailymaker.Util.ClockThread;
import com.example.prgoh.dailymaker.databinding.AlarmbossActivityBinding;

import java.io.IOException;
import java.util.Calendar;

/**
 * Created by prgoh on 2017-07-31.
 */

public class AlarmBossActivity extends BaseActivity {

    private AlarmbossActivityBinding binding;

    private GonSurfaceView surfaceView;

    private Vibrator vibrator;
    private MediaPlayer mediaPlayer;

    private SharedPreferences preferences;
    private AudioManager am;
    private int volumn;
    private int currentVolumn;

    private int clicked = 0;
    private int count = 30;

    private boolean isClicked = true;

    private boolean threadRun = true;

    private ClockThread clockThread;
    private Thread countThread;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.alarmboss_activity);

        surfaceView = new GonSurfaceView(this, 25);
        surfaceView.setZOrderOnTop(true);
        surfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        surfaceView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        preferences = getSharedPreferences("Setting", Context.MODE_PRIVATE);

        //자동종료
        int minute = preferences.getInt("minute", 0);

        if (minute != 0){
            long duration = minute*60000;

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, duration);
        }

        binding.bossCountTv.setText(""+count);
        binding.relative.addView(surfaceView);

        Intent intent = getIntent();

        Uri uri = Uri.parse(intent.getStringExtra("uri"));
        boolean vibrate = intent.getBooleanExtra("vibrate", false);

        Log.e("uri", uri.toString());

        am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        int result = am.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        if (result != AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {

        }

        if (uri != null){
            playMusic(uri);
        }

        if (vibrate){
            playVibrate();
        }


        countThread = new Thread(){
            @Override
            public void run() {
                while(threadRun){

                    if (clicked < surfaceView.getIsClicked()) {
                        clicked = surfaceView.getIsClicked();
                        isClicked = true;
                    }

                    if (isClicked) {
                        Runnable runnable = new Runnable() {
                            @Override
                            public void run() {
                                count --;
                                binding.bossCountTv.setText(""+count);
                            }
                        };

                        isClicked = false;

                        Message message = Message.obtain(handler, runnable);
                        handler.sendMessage(message);

                        try {
                            Thread.sleep(30);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }else if (count == 0){
                        finish();
                    }
                }
            }
        };

        clockThread = new ClockThread(binding.bossHour, binding.bossAm, binding.bossMinute,
                binding.bossSec, binding.bossBeep);

        clockThread.start();
        countThread.start();
    }

    private void playVibrate(){

        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(
                new long[]{100,1000,100,500,100,500,100,1000}
                , 0);
    }

    private void playMusic(Uri uri){
        volumn = preferences.getInt("volumn", 0);
        currentVolumn = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        am.setStreamVolume(AudioManager.STREAM_MUSIC, volumn, 0);
        mediaPlayer = new MediaPlayer();

        try {
            mediaPlayer.setDataSource(this, uri);
//            mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaPlayer.start();
        mediaPlayer.setLooping(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (vibrator != null){
            vibrator.cancel();
        }

        if (mediaPlayer != null){
            mediaPlayer.release();
            mediaPlayer = null;
        }

        am.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolumn, 0);
        boolean retry = true;

        threadRun = false;

        while(retry) {
            try {
                countThread.join();
                clockThread.interrupt();
                retry = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        CustomToast.customToast(this, "불가능");
        return;
    }
}
