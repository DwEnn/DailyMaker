package com.example.prgoh.dailymaker.Dialog;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RadioButton;

import com.example.prgoh.dailymaker.R;
import com.example.prgoh.dailymaker.Util.BaseActivity;
import com.example.prgoh.dailymaker.databinding.AutoCancelDialogBinding;

/**
 * Created by prgoh on 2017-07-23.
 */

public class AutoCancelDialog extends BaseActivity {

    private AutoCancelDialogBinding binding;

    private int minute;
    private RadioButton checkedRadio;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.auto_cancel_dialog);

        Intent intent = getIntent();
        minute = intent.getIntExtra("minute", 0);

        checkedRadio = checkRadio(minute);
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.auto_cancel_disable : {
                check(binding.autoCancelDisableRadio);
                Intent intent = getIntent();
                intent.putExtra("autoCancel", 0);
                setResult(RESULT_OK, intent);
                finish();
                break;
            }
            case R.id.auto_cancel_1min : {
                check(binding.autoCancel1minRadio);
                Intent intent = getIntent();
                intent.putExtra("autoCancel", 1);
                setResult(RESULT_OK, intent);
                finish();
                break;
            }
            case R.id.auto_cancel_5min : {
                check(binding.autoCancel5minRadio);
                Intent intent = getIntent();
                intent.putExtra("autoCancel", 5);
                setResult(RESULT_OK, intent);
                finish();
                break;
            }
            case R.id.auto_cancel_10min : {
                check(binding.autoCancel10minRadio);
                Intent intent = getIntent();
                intent.putExtra("autoCancel", 10);
                setResult(RESULT_OK, intent);
                finish();
                break;
            }
            case R.id.auto_cancel_15min : {
                check(binding.autoCancel15minRadio);
                Intent intent = getIntent();
                intent.putExtra("autoCancel", 15);
                setResult(RESULT_OK, intent);
                finish();
                break;
            }
            case R.id.auto_cancel_20min : {
                check(binding.autoCancel20minRadio);
                Intent intent = getIntent();
                intent.putExtra("autoCancel", 20);
                setResult(RESULT_OK, intent);
                finish();
                break;
            }
            case R.id.auto_cancel_25min : {
                check(binding.autoCancel25minRadio);
                Intent intent = getIntent();
                intent.putExtra("autoCancel", 25);
                setResult(RESULT_OK, intent);
                finish();
                break;
            }
            case R.id.auto_cancel_30min : {
                check(binding.autoCancel30minRadio);
                Intent intent = getIntent();
                intent.putExtra("autoCancel", 30);
                setResult(RESULT_OK, intent);
                finish();
                break;
            }
        }
    }

    private void check(RadioButton rBtn){
        rBtn.setChecked(true);
        checkedRadio.setChecked(false);
    }

    private RadioButton checkRadio(int minute){
        switch (minute){
            case 0 :{
                binding.autoCancelDisableRadio.setChecked(true);
                return binding.autoCancelDisableRadio;
            }
            case 1 : {
                binding.autoCancel1minRadio.setChecked(true);
                return binding.autoCancel1minRadio;
            }
            case 5 : {
                binding.autoCancel5minRadio.setChecked(true);
                return binding.autoCancel5minRadio;
            }
            case 10 : {
                binding.autoCancel10minRadio.setChecked(true);
                return binding.autoCancel10minRadio;
            }
            case 15 : {
                binding.autoCancel15minRadio.setChecked(true);
                return binding.autoCancel15minRadio;
            }
            case 20 : {
                binding.autoCancel20minRadio.setChecked(true);
                return binding.autoCancel20minRadio;
            }
            case 25 : {
                binding.autoCancel25minRadio.setChecked(true);
                return binding.autoCancel25minRadio;
            }
            case 30 : {
                binding.autoCancel30minRadio.setChecked(true);
                return binding.autoCancel30minRadio;
            }
        }
        return null;
    }
}
