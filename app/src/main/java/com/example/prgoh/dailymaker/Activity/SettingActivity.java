package com.example.prgoh.dailymaker.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.prgoh.dailymaker.Dialog.AutoCancelDialog;
import com.example.prgoh.dailymaker.Dialog.LouderDialog;
import com.example.prgoh.dailymaker.Item.Code;
import com.example.prgoh.dailymaker.Item.CustomToast;
import com.example.prgoh.dailymaker.R;
import com.example.prgoh.dailymaker.Receiver.AlarmReceiver;
import com.example.prgoh.dailymaker.Util.BaseActivity;
import com.example.prgoh.dailymaker.databinding.SettingActivityBinding;

/**
 * Created by prgoh on 2017-07-21.
 */

public class SettingActivity extends BaseActivity {

    private SettingActivityBinding binding;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private int minute;
    private int louder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.setting_activity);

        preferences = getSharedPreferences("Setting", Context.MODE_PRIVATE);
        editor = preferences.edit();

        //알람 자동해제
        minute = preferences.getInt("autoCancel", 0);
        if (minute == 0)
            binding.autoCancelTv.setText("사용안함");
        else
            binding.autoCancelTv.setText(String.valueOf(minute)+"분");

        //알람 볼륨
        setVolumn();

        //휴가 모드
        boolean vacation = preferences.getBoolean("vacation", false);
        binding.vacationCheckBox.setChecked(vacation);

        //볼륨 점점 크게
        louder = preferences.getInt("louder", 0);

        if (louder == 0)
            binding.settingLouderTv.setText("끄기");
        else {
            if (louder > 60)
                binding.settingLouderTv.setText(String.valueOf(louder/60)+"분");
            else
                binding.settingLouderTv.setText(String.valueOf(louder)+"초");
        }
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.back_btn_setting : {
                finish();
                break;
            }
            case R.id.setting_louder_linear : {
                Intent intent = new Intent(this, LouderDialog.class);
                intent.putExtra("louder", louder);
                startActivityForResult(intent, Code.VOLUME_MAKE_LOUDER_REQUEST);
                break;
            }
            case R.id.auto_cancel_linear : {
                Intent intent = new Intent(this, AutoCancelDialog.class);
                intent.putExtra("minute", minute);
                startActivityForResult(intent, Code.ALARM_AUTO_CANCEL_REQUEST);
                break;
            }
            case R.id.vacation_mode_linear : {

                if (binding.vacationCheckBox.isChecked()){
                    binding.vacationCheckBox.setChecked(false);
                    CustomToast.customToast(this, "모든 알람 활성화");
                    editor.putBoolean("vacation", false).apply();
                }else {
                    binding.vacationCheckBox.setChecked(true);
                    CustomToast.customToast(this, "모든 알람 해제");
                    editor.putBoolean("vacation", true).apply();
                }
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK){
            switch (requestCode){
                case Code.ALARM_AUTO_CANCEL_REQUEST : {
                    minute = data.getIntExtra("autoCancel", 0);
                    editor.putInt("autoCancel", minute).apply();

                    if (minute == 0)
                        binding.autoCancelTv.setText("사용안함");
                    else
                        binding.autoCancelTv.setText(String.valueOf(minute)+"분");

                    break;
                }
                case Code.VOLUME_MAKE_LOUDER_REQUEST : {
                    louder = data.getIntExtra("louder", 0);
                    editor.putInt("louder", louder).apply();

                    if (louder == 0)
                        binding.settingLouderTv.setText("끄기");
                    else {
                        if (louder > 60)
                            binding.settingLouderTv.setText(String.valueOf(louder/60)+"분");
                        else
                            binding.settingLouderTv.setText(String.valueOf(louder)+"초");
                    }

                    break;
                }
            }
        }
    }

    private void setVolumn(){
        AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        int max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int previousVolumn = preferences.getInt("volumn", 0);

        binding.settingVolumeSeekbar.setMax(max);
        binding.settingVolumeSeekbar.setProgress(previousVolumn);

        binding.settingVolumeSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                editor.putInt("volumn", i).apply();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }
}
