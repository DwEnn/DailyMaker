package com.example.prgoh.dailymaker.Activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.example.prgoh.dailymaker.Adapter.AlarmAdapter;
import com.example.prgoh.dailymaker.Adapter.ViewPagerAdatper;
import com.example.prgoh.dailymaker.Item.Code;
import com.example.prgoh.dailymaker.Item.CustomToast;
import com.example.prgoh.dailymaker.R;
import com.example.prgoh.dailymaker.Util.BaseActivity;
import com.example.prgoh.dailymaker.databinding.ActivityMainBinding;

public class MainActivity extends BaseActivity{

    private ActivityMainBinding binding;

    private ViewPagerAdatper adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        adapter = new ViewPagerAdatper(getSupportFragmentManager());
        binding.viewpager.setAdapter(adapter);

        SharedPreferences preferences = getSharedPreferences("Setting", Context.MODE_PRIVATE);
        boolean vacation =  preferences.getBoolean("vacation", false);

        if (vacation){
            CustomToast.customToast(this, "휴가모드 설정 중");
        }

        binding.viewpager.setCurrentItem(2);

        // TODO: 2017-08-09 notification 아이콘 만들기
//        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        Notification.Builder builder = new Notification.Builder(this);
//        builder.setSmallIcon(R.drawable.alarm_icon);
//        manager.notify(0, builder.build());
    }
}
