package com.example.prgoh.dailymaker.Dialog;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.prgoh.dailymaker.R;
import com.example.prgoh.dailymaker.Util.BaseActivity;
import com.example.prgoh.dailymaker.databinding.VolumeLouderDialogBinding;

/**
 * Created by prgoh on 2017-07-23.
 */

public class LouderDialog extends BaseActivity {

    private VolumeLouderDialogBinding binding;
    private RadioButton checkedRbtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.volume_louder_dialog);

        Intent intent = getIntent();
        checkedRbtn = setCheck(intent.getIntExtra("louder", 0));
        checkedRbtn.setChecked(true);
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.louder_off : {
                check(binding.louderOffRadio);
                Intent intent = getIntent();
                intent.putExtra("louder", 0);
                setResult(RESULT_OK, intent);
                finish();
            }
            case R.id.louder_30sec : {
                check(binding.louder30secRadio);
                Intent intent = getIntent();
                intent.putExtra("louder", 30);
                setResult(RESULT_OK, intent);
                finish();
            }
            case R.id.louder_60sec : {
                check(binding.louder60secRadio);
                Intent intent = getIntent();
                intent.putExtra("louder", 60);
                setResult(RESULT_OK, intent);
                finish();
            }
            case R.id.louder_5min : {
                check(binding.louder5minRadio);
                Intent intent = getIntent();
                intent.putExtra("louder", 300);
                setResult(RESULT_OK, intent);
                finish();
            }
            case R.id.louder_10min : {
                check(binding.louder10minRadio);
                Intent intent = getIntent();
                intent.putExtra("louder", 600);
                setResult(RESULT_OK, intent);
                finish();
            }
            case R.id.louder_30min : {
                check(binding.louder30minRadio);
                Intent intent = getIntent();
                intent.putExtra("louder", 1800);
                setResult(RESULT_OK, intent);
                finish();
            }
            case R.id.louder_60min : {
                check(binding.louder60minRadio);
                Intent intent = getIntent();
                intent.putExtra("louder", 3600);
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }

    private void check(RadioButton rBtn){
        rBtn.setChecked(true);
        checkedRbtn.setChecked(false);
    }

    private RadioButton setCheck(int data){
        switch (data){
            case 0 : {
                binding.louderOffRadio.setChecked(true);
                return binding.louderOffRadio;
            }
            case 30 : {
                binding.louder30secRadio.setChecked(true);
                return binding.louder30secRadio;
            }
            case 60 : {
                binding.louder60secRadio.setChecked(true);
                return binding.louder60secRadio;
            }
            case 300 : {
                binding.louder5minRadio.setChecked(true);
                return binding.louder5minRadio;
            }
            case 600 : {
                binding.louder10minRadio.setChecked(true);
                return binding.louder10minRadio;
            }
            case 1800 : {
                binding.louder30minRadio.setChecked(true);
                return binding.louder30minRadio;
            }
            case 3600 : {
                binding.louder60minRadio.setChecked(true);
                return binding.louder60minRadio;
            }
        }
        return null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
