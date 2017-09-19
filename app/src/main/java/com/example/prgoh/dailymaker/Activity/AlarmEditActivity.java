package com.example.prgoh.dailymaker.Activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import java.text.ParseException;

/**
 * Created by prgoh on 2017-07-29.
 */

public class AlarmEditActivity extends BaseActivity {

    private AlarmsetActivityBinding binding;

    private boolean isChecked[];
    private Alarm alarm;

    private Intent intent;
    private int position;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.alarmset_activity);

        intent = getIntent();
        position = intent.getIntExtra("position", 0);
        alarm = (Alarm) intent.getSerializableExtra("alarm");

        isChecked = AlarmHelper.parseStrWeekDay(alarm.getWeekDay());
        checkWeek(isChecked);

        binding.alarmSetDeleteBtn.setVisibility(View.VISIBLE);

        binding.alarmSetBellTv.setText(alarm.getBellTitle());
        binding.alarmSetTimeTv.setText(alarm.getTime());
        binding.alarmSetTodo.setText(alarm.getTodo());

        if (AlarmHelper.getBoolFromStr(alarm.getBoss()))
            binding.bossCheckBox.setChecked(true);

        if (!AlarmHelper.getBoolFromStr(alarm.getVibrate()))
            binding.vibrateCheckBox.setChecked(false);
    }

    public void onClick(View view) throws ParseException, JSONException {
        switch (view.getId()){
            case R.id.alarm_set_delete_btn : {
                Intent intent = new Intent();
                intent.putExtra("position", position);

                setResult(Code.DELETE_ALARM_RESULT, intent);
                finish();
                break;
            }
            case R.id.alarm_set_ok_btn : {

                alarm.setWeekDay(TimeDwEnn.getCheckedDayString(isChecked));
                alarm.setTodo(binding.alarmSetTodo.getText().toString());
                alarm.setChecked("1");

                int alarmCode = Code.generateRequestCode();
                alarm.setAlarmCode(alarmCode);
                AlarmHelper.setAlarm(this, alarm);
                AlarmHelper.toastTime(this, AlarmHelper.getTime(alarm));
//                alarm.setAlarmCode(requestCode);

                intent.putExtra("alarm", alarm);
                intent.putExtra("position", position);

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

    private void checkWeek(boolean[] weekDay){
        for (int i=0; i<weekDay.length; i++){
            if (weekDay[i] == true){
                switch (i){
                    case Code.SUN_DAY : {
                        binding.sun.setImageResource(R.drawable.sun_en);
                        break;
                    }
                    case Code.MON_DAY : {
                        binding.mon.setImageResource(R.drawable.mon_en);
                        break;
                    }
                    case Code.THU_DAY : {
                        binding.thu.setImageResource(R.drawable.thu_en);
                        break;
                    }
                    case Code.WEN_DAY : {
                        binding.wen.setImageResource(R.drawable.wen_en);
                        break;
                    }
                    case Code.THUR_DAY : {
                        binding.thur.setImageResource(R.drawable.thur_en);
                        break;
                    }
                    case Code.FRI_DAY : {
                        binding.fri.setImageResource(R.drawable.fri_en);
                        break;
                    }
                    case Code.SAT_DAY : {
                        binding.sat.setImageResource(R.drawable.sat_en);
                        break;
                    }
                }
            }
        }
    }
}
