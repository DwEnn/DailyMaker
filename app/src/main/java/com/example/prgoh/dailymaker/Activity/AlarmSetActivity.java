package com.example.prgoh.dailymaker.Activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.icu.text.SimpleDateFormat;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.prgoh.dailymaker.Dialog.BellSetDialog;
import com.example.prgoh.dailymaker.Dialog.HowToDialog;
import com.example.prgoh.dailymaker.Dialog.TimePickerDialog;
import com.example.prgoh.dailymaker.Item.Alarm;
import com.example.prgoh.dailymaker.Item.Code;
import com.example.prgoh.dailymaker.Item.MusicDto;
import com.example.prgoh.dailymaker.Item.RingtoneDto;
import com.example.prgoh.dailymaker.R;
import com.example.prgoh.dailymaker.Util.AlarmHelper;
import com.example.prgoh.dailymaker.Util.BaseActivity;
import com.example.prgoh.dailymaker.Util.ToggleImg;
import com.example.prgoh.dailymaker.databinding.AlarmsetActivityBinding;
import com.example.schedulelibrary.TimeDwEnn;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Date;

/**
 * Created by prgoh on 2017-07-21.
 */

public class AlarmSetActivity extends BaseActivity {

    private AlarmsetActivityBinding binding;

    private Alarm alarm;

    private boolean isChecked[] = new boolean[]{false, false, false, false, false, false, false, false};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.alarmset_activity);

        alarm = new Alarm();

        //time_tv 현재 시간 설정
        Date today = new Date();
        java.text.SimpleDateFormat time = new java.text.SimpleDateFormat("hh : mm a");
        binding.alarmSetTimeTv.setText(time.format(today));
        alarm.setTime(time.format(today));

        //bell_tv 기본 벨소리 설정
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        Ringtone ring = RingtoneManager.getRingtone(this, uri);
        binding.alarmSetBellTv.setText(ring.getTitle(this));
        alarm.setBellTitle(ring.getTitle(this));
        alarm.setBellUri(uri.toString());

        //vibrate 기본 설정
        alarm.setVibrate("1");

        //weekday 기본 설정
        alarm.setWeekDay(TimeDwEnn.getCheckedDayString(isChecked));

        //checked 설정
        alarm.setChecked("1");

        //boss 기본설정
        alarm.setBoss("0");
    }

    public void onClick(View view) throws ParseException, JSONException {
        switch (view.getId()){
            case R.id.alarm_set_ok_btn : {
                Intent intent = getIntent();

                alarm.setWeekDay(TimeDwEnn.getCheckedDayString(isChecked));
                alarm.setTodo(binding.alarmSetTodo.getText().toString());

                Log.e("todo", "text : "+binding.alarmSetTodo.getText().toString());

                int alarmCode = Code.generateRequestCode();
                alarm.setAlarmCode(alarmCode);
                AlarmHelper.setAlarm(getApplicationContext(), alarm);
                AlarmHelper.toastTime(this, AlarmHelper.getTime(alarm));

//                alarm.setAlarmCode(requestCode);
                intent.putExtra("alarm", alarm);
                setResult(RESULT_OK, intent);
                finish();
                break;
            }
            case R.id.back_btn_alarmset : {
                finish();
                break;
            }
            case R.id.alarm_set_bell_linear : {
                startActivityForResult(new Intent(this, BellSetDialog.class), Code.BELL_REQUEST);
                break;
            }
            case R.id.alarm_set_vibrate_linear : {
                if (binding.vibrateCheckBox.isChecked()){
                    binding.vibrateCheckBox.setChecked(false);
                    alarm.setVibrate("0");
                }else {
                    binding.vibrateCheckBox.setChecked(true);
                    alarm.setVibrate("1");
                }
                break;
            }
            case R.id.alarm_set_boss_linear : {
                if (binding.bossCheckBox.isChecked()){
                    binding.bossCheckBox.setChecked(false);
                    alarm.setBoss("0");
                }else {
                    binding.bossCheckBox.setChecked(true);
                    alarm.setBoss("1");
                }
                break;
            }
            case R.id.alarm_set_time_linear : {
                startActivityForResult(new Intent(this, TimePickerDialog.class), Code.TIME_REQUEST);
                break;
            }
            case R.id.alarm_set_cancel_btn : {
                finish();
                break;
            }
            case R.id.sun : {
                setIsChecked(1);
                ToggleImg.toggleImg(binding.sun, 1, isChecked);
                break;
            }
            case R.id.mon : {
                setIsChecked(2);
                ToggleImg.toggleImg(binding.mon, 2, isChecked);
                break;
            }
            case R.id.thu : {
                setIsChecked(3);
                ToggleImg.toggleImg(binding.thu, 3, isChecked);
                break;
            }
            case R.id.wen : {
                setIsChecked(4);
                ToggleImg.toggleImg(binding.wen, 4, isChecked);
                break;
            }
            case R.id.thur : {
                setIsChecked(5);
                ToggleImg.toggleImg(binding.thur, 5, isChecked);
                break;
            }
            case R.id.fri : {
                setIsChecked(6);
                ToggleImg.toggleImg(binding.fri, 6, isChecked);
                break;
            }
            case R.id.sat : {
                setIsChecked(7);
                ToggleImg.toggleImg(binding.sat, 7, isChecked);
                break;
            }
        }
    }

    private void setIsChecked(int num){
        if (isChecked[num] == false){
            isChecked[num] = true;
        }else{
            isChecked[num] = false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode){
            case RESULT_OK : {
                switch (requestCode){
                    case Code.TIME_REQUEST : {
                        String time = data.getStringExtra("time");
                        binding.alarmSetTimeTv.setText(time);
                        alarm.setTime(time);
                        break;
                    }
                }
                break;
            }
            case Code.MUSIC_RESULT : {
                MusicDto musicDto = (MusicDto)data.getSerializableExtra("music");
                binding.alarmSetBellTv.setText(musicDto.getTitle());
                alarm.setBellTitle(musicDto.getTitle());
                alarm.setBellUri(musicDto.getStrUri());
                break;
            }
            case Code.RING_RESULT : {
                RingtoneDto ringtoneDto = (RingtoneDto)data.getSerializableExtra("ring");
                binding.alarmSetBellTv.setText(ringtoneDto.getTitle());
                alarm.setBellTitle(ringtoneDto.getTitle());
                alarm.setBellUri(ringtoneDto.getStringUri());
                break;
            }
            case Code.NO_BELL_RESULT : {
                binding.alarmSetBellTv.setText("무음");
                alarm.setBellTitle("무음");
                alarm.setBellUri(null);
                break;
            }
        }
    }
}