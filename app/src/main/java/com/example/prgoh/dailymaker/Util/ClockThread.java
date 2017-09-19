package com.example.prgoh.dailymaker.Util;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by prgoh on 2017-08-15.
 */

public class ClockThread extends Thread {

    private final int MESSAGE_CLOCK = 200;
    private final int MESSAGE_BEEP = 201;

    private Calendar calendar;

    private boolean isBeep = true;

    private TextView am, hour, minute, sec;
    private ImageView beep;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MESSAGE_CLOCK : {
                    calendar = (Calendar) msg.obj;

                    am.setText((calendar.get(Calendar.AM_PM)) == 0 ? "오전" : "오후");
                    hour.setText(calendar.get(Calendar.HOUR) < 10 ? "0"+calendar.get(Calendar.HOUR) : ""+calendar.get(Calendar.HOUR));
                    minute.setText(calendar.get(Calendar.MINUTE) < 10 ? "0"+calendar.get(Calendar.MINUTE) : ""+calendar.get(Calendar.MINUTE));
                    sec.setText(calendar.get(Calendar.SECOND) < 10 ? "0"+calendar.get(Calendar.SECOND) : ""+calendar.get(Calendar.SECOND));

                    if (isBeep){
                        isBeep = false;
                        beep.setVisibility(View.INVISIBLE);
                    }else{
                        isBeep = true;
                        beep.setVisibility(View.VISIBLE);
                    }

                    break;
                }
                case MESSAGE_BEEP : {

                    break;
                }
            }
        }
    };

    public ClockThread(TextView hour, TextView am,
                       TextView minute, TextView sec, ImageView beep){
        this.am = am;
        this.hour = hour;
        this.beep = beep;
        this.minute = minute;
        this.sec = sec;
    }

    @Override
    public void run() {
        while(true){
            calendar = Calendar.getInstance();

            Message msg = Message.obtain(handler);
            msg.what = MESSAGE_CLOCK;
            msg.obj = calendar;

            handler.sendMessage(msg);

//            Message beepMsg = Message.obtain(handler);
//            msg.what = MESSAGE_BEEP;
//
//            handler.sendMessage(beepMsg);

            try {
                sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

