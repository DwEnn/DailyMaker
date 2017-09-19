package com.example.prgoh.dailymaker.Receiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.example.prgoh.dailymaker.Activity.AlarmBossActivity;
import com.example.prgoh.dailymaker.Activity.MainActivity;
import com.example.prgoh.dailymaker.Adapter.AlarmAdapter;
import com.example.prgoh.dailymaker.DB.AlarmDBManager;
import com.example.prgoh.dailymaker.Dialog.AlarmDialog;
import com.example.prgoh.dailymaker.Fragment.FragmentAlarm;
import com.example.prgoh.dailymaker.Item.Alarm;
import com.example.prgoh.dailymaker.Util.AlarmHelper;

import org.json.JSONException;

import java.util.Calendar;

/**
 * Created by prgoh on 2017-07-25.
 */

public class AlarmReceiver extends BroadcastReceiver {

    private SharedPreferences preferences;

    @Override
    public void onReceive(Context context, Intent intent)
    {
//        Alarm alarm = (Alarm) intent.getSerializableExtra("alarm");
//        Log.e("todaybool", ""+intent.getBooleanArrayExtra("today").length); // 이것도 죽음, 아예 인텐트 못받는듯
//        Log.e("alarmData", ""+alarm.toString()); // 근데 여기서 죽어 nullpointerexception

        preferences = context.getSharedPreferences("Setting", Context.MODE_PRIVATE);
        boolean vacation = preferences.getBoolean("vacation", false);

        if (vacation)
            return;

        Bundle bundle = intent.getBundleExtra("bundle");
        Alarm alarm = (Alarm) bundle.getSerializable("alarm");

        boolean[] week = bundle.getBooleanArray("weekDay");
        boolean isNoRepeat = true;

        if (week == null) {
            week = AlarmHelper.parseStrWeekDay(alarm.getWeekDay());
            isNoRepeat = false;
        }

        boolean alarmBoss = AlarmHelper.getBoolFromStr(alarm.getBoss());
        boolean vibrate = AlarmHelper.getBoolFromStr(alarm.getVibrate());
        String uri = alarm.getBellUri();

        int index = AlarmHelper.compareWithAlarm(alarm);

        if (index != -1){
            if (alarm.getIsQuick() != null)
                AlarmAdapter.list.remove(index);
            else if (isNoRepeat){
                alarm.setChecked("0");
                AlarmAdapter.list.set(index, alarm);
            }
        }else{
            AlarmDBManager dbManager = AlarmDBManager.getInstance(context);
            dbManager.receivedAlarm(context, alarm, isNoRepeat);
        }

        Calendar cal = Calendar.getInstance();
        // 오늘 요일의 알람 재생이 true이면 사운드 재생
        // false 면 return
        if (!week[cal.get(Calendar.DAY_OF_WEEK)]) {
            Log.e("alarm returned", "yes");
            return;
        }

        Intent intent1 = null;

        if (alarmBoss){
            intent1 = new Intent(context, AlarmBossActivity.class);
            Log.e("call boss", "yes");
        }else {
            intent1 = new Intent(context, AlarmDialog.class);
            Log.e("call bass", ""+alarmBoss);
        }

        intent1.putExtra("uri", uri);
        intent1.putExtra("vibrate", vibrate);
        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent1);

        Log.e("received", "OK");
    }
}
