package com.example.prgoh.dailymaker.Dialog;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.adapters.TimePickerBindingAdapter;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.prgoh.dailymaker.R;
import com.example.prgoh.dailymaker.Util.BaseActivity;
import com.example.prgoh.dailymaker.databinding.TimeDialogBinding;

import java.lang.reflect.Field;

/**
 * Created by prgoh on 2017-07-23.
 */

public class TimePickerDialog extends BaseActivity {

    private TimeDialogBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.time_dialog);

    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.time_set_ok_btn : {
                Intent intent = getIntent();
                String time = getTime();
                intent.putExtra("time", time);
                setResult(RESULT_OK, intent);
                finish();
                break;
            }
        }
    }

    private String getTime(){
        int h = binding.timePicker.getCurrentHour();
        int m = binding.timePicker.getCurrentMinute();

        String a;
        String time;

        if (h < 12){
            a = "오전";
        }else{
            h -= 12;
            a = "오후";
        }

        if (h == 0)
            h = 12;

        time = String.format("%02d : %02d %s", h,m,a);
        return time;
    }

    public void setTypeface(NumberPicker picker, Typeface typeface) {

        //주위의 숫자들을 보여주는 휠 뷰
        try {
            //클래스의 휠 Paint 객체를 꺼내 폰트를 적용
            Field field = NumberPicker.class.getDeclaredField("mSelectorWheelPaint");
            field.setAccessible(true);
            ((Paint)field.get(picker)).setTypeface(typeface);

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        //선택된 숫자를 보여주는 TextView
        int count = picker.getChildCount();
        for(int i = 0 ; i < count ; i++) {
            //자식뷰로 꺼내 폰트를 적용
            View view = picker.getChildAt(i);
            if (view instanceof TextView) {
                ((TextView) view).setTypeface(typeface);
            }
        }
    }
}
