package com.example.prgoh.dailymaker.Dialog;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
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
import android.view.WindowManager;

import com.example.prgoh.dailymaker.R;
import com.example.prgoh.dailymaker.Util.BaseActivity;
import com.example.prgoh.dailymaker.Util.ClockThread;
import com.example.prgoh.dailymaker.databinding.AlarmDialogBinding;

import java.io.IOException;

/**
 * Created by prgoh on 2017-07-25.
 */

public class AlarmDialog extends BaseActivity {

    private AlarmDialogBinding binding;

    private Vibrator vibrator;
    private MediaPlayer mediaPlayer;

    private SharedPreferences preferences;
    private AudioManager am;
    private int volumn;
    private int currentVolumn;

    private int louder;
    private double perLouder;

    private ClockThread clockThread;
    private Thread louderThread;

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.alarm_dialog);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                        | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                        | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        preferences = getSharedPreferences("Setting", Context.MODE_PRIVATE);

        // 자동종료 설정
        int minute = preferences.getInt("autoCancel", 0) * 60;

        if (minute != 0){
            long duration = minute*10000;

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, duration);
        }

        // 알람 볼륨 설정, 점점 크게
        volumn = preferences.getInt("volumn", 0);
        louder = preferences.getInt("louder", 0);

        am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        if (louder != 0){

            if (louder > minute && minute != 0)
                louder = minute;

            perLouder = (double)volumn/louder;
            final double per = perLouder;

            louderThread = new Thread(){
                @Override
                public void run() {
                    while((int)perLouder != volumn){

                        Runnable runnable = new Runnable() {
                            @Override
                            public void run() {
                                AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                                am.setStreamVolume(AudioManager.STREAM_MUSIC, (int)perLouder, 0);
                            }
                        };

                        Message msg = Message.obtain(mHandler, runnable);
                        mHandler.sendMessage(msg);

                        perLouder += per;

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };

            louderThread.start();
        }else{
            //louder 가 설정되지 않은경우 기본 설정된 볼륨으로 재생
            am.setStreamVolume(AudioManager.STREAM_MUSIC, volumn, 0);
        }

        currentVolumn = am.getStreamVolume(AudioManager.STREAM_MUSIC);

        Intent intent = getIntent();

        String strUri = intent.getStringExtra("uri");
        Uri uri = null;

        if (strUri != null)
            uri = Uri.parse(strUri);

        boolean vibrate = intent.getBooleanExtra("vibrate", false);


        int result = am.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        if (result != AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {

        }

        if (uri != null){
            playMusic(uri);
        }

        if (vibrate){
            playVibrate();
        }

        clockThread = new ClockThread(binding.hour, binding.am, binding.minute,
                binding.sec, binding.beep);
        clockThread.start();

    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.alarm_ok:{
                finish();
                break;
            }
        }
    }

    private void playVibrate(){

        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(
                new long[]{100,1000,100,500,100,500,100,1000}
                , 0);
    }

    private void playMusic(Uri uri){

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
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_OUTSIDE)
            return false;
        else
            return true;
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

        clockThread.interrupt();
        louderThread.interrupt();
    }
}
