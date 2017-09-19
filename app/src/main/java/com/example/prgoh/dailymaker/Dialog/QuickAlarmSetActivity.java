package com.example.prgoh.dailymaker.Dialog;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.example.prgoh.dailymaker.Item.Alarm;
import com.example.prgoh.dailymaker.Item.Code;
import com.example.prgoh.dailymaker.Item.CustomToast;
import com.example.prgoh.dailymaker.R;
import com.example.prgoh.dailymaker.Util.AlarmHelper;
import com.example.prgoh.dailymaker.Util.BaseActivity;
import com.example.prgoh.dailymaker.Util.ToggleImg;
import com.example.prgoh.dailymaker.databinding.QuickAlarmActivityBinding;
import com.example.schedulelibrary.TimeDwEnn;

import org.json.JSONException;

import java.text.ParseException;

/**
 * Created by prgoh on 2017-07-22.
 */

public class QuickAlarmSetActivity extends BaseActivity {

    private QuickAlarmActivityBinding binding;

    private int h, m;
    private int minute;
    private String time;

    private Alarm alarm;

    private static final int RING = 0;
    private static final int VIBRATE = 1;

    private boolean vibrateBool = true;
    private boolean ringBool = true;

    private boolean week[] = new boolean[8];

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.quick_alarm_activity);

        alarm = new Alarm();

        //alarm 기본 설정
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        Ringtone ring = RingtoneManager.getRingtone(this, uri);
        alarm.setBellTitle(ring.getTitle(this));
        alarm.setBellUri(uri.toString());

        alarm.setVibrate("1");
        alarm.setWeekDay(TimeDwEnn.getCheckedDayString(week));
        alarm.setBoss("0");
        alarm.setChecked("1");
        alarm.setIsQuick("TRUE");
    }

    public void onClick(View view) throws ParseException, JSONException {
        switch (view.getId()){
            case R.id.quick_ok : {

                if (minute == 0){
                    CustomToast.customToast(this, "시간을 설정해 주십시오");
                    break;
                }

                alarm.setTime(TimeDwEnn.getTimeString(h, m));
                alarm.setTodo(binding.quickTodo.getText().toString());

                if (!vibrateBool)
                    alarm.setVibrate("0");
                if (!ringBool) {
                    alarm.setBellUri(null);
                    alarm.setBellTitle("무음");
                }

                int alarmCode = Code.generateRequestCode();
                alarm.setAlarmCode(alarmCode);
                AlarmHelper.setAlarm(this, alarm);
                AlarmHelper.toastTime(this, AlarmHelper.getTime(alarm));

//                alarm.setAlarmCode(requestCode);

                Intent intent = getIntent();
                intent.putExtra("alarm", alarm);
                setResult(RESULT_OK, intent);
                finish();
                break;
            }
            case R.id.quick_ring : {
                toggle(binding.quickRing, ringBool, RING);
                break;
            }
            case R.id.quick_vibrate : {
                toggle(binding.quickVibrate, vibrateBool, VIBRATE);
                break;
            }
            case R.id.quick_reset_btn : {
                resetTime();
                break;
            }
            case R.id.quick_1m : {
                minute += 1;
                upDate(minute);
                break;
            }
            case R.id.quick_5m : {
                minute += 5;
                upDate(minute);
                break;
            }
            case R.id.quick_10m : {
                minute += 10;
                upDate(minute);
                break;
            }
            case R.id.quick_15m : {
                minute += 15;
                upDate(minute);
                break;
            }
            case R.id.quick_30m : {
                minute += 30;
                upDate(minute);
                break;
            }
            case R.id.quick_60m : {
                minute += 60;
                upDate(minute);
                break;
            }
        }
    }

    private void resetTime(){
        time = "+ 00 : 00";
        minute = 0;
        binding.timeSetTv.setText(time);
    }

    private void convertTime(int minute){

        if (minute >= 60){
            h = (int)(minute/60);
            m = (int)(minute%60);
        }else{
            h = 0;
            m = minute;
        }

        if (h != 0){
            if (h > 9){
                if (m <10) {
                    if (m == 0)
                        time = "+ "+h+" : 00";
                    else
                        time = "+ " + h + " : 0" + m;
                }else
                    time = "+ "+h+" : "+m;
            }else {
                if (m < 10){
                    if (m == 0)
                        time = "+ 0"+h+" : 00";
                    else
                        time = "+ 0" + h + " : 0" + m;
                }else
                    time = "+ 0"+h+" : "+m;
            }
        }else{
            if (m <10){
                time = "+ 00 : 0"+m;
            }else {
                time = "+ 00 : "+m;
            }
        }
    }

    public void upDate(int minute){
        convertTime(minute);
        binding.timeSetTv.setText(time);
    }

    private void toggle(ImageView imageView, boolean isChecked, int type){
        switch (type){
            case RING : {
                if (!isChecked){
                    imageView.setImageResource(R.drawable.ring_en);
                    ringBool = true;
                }else {
                    imageView.setImageResource(R.drawable.ring_icon);
                    ringBool = false;
                }
                break;
            }
            case VIBRATE : {
                if (!isChecked){
                    imageView.setImageResource(R.drawable.vibrate_en);
                    vibrateBool = true;
                }else{
                    imageView.setImageResource(R.drawable.vibrate_icon);
                    vibrateBool = false;
                }
                break;
            }
        }
    }
}
