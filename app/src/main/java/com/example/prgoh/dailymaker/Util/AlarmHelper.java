package com.example.prgoh.dailymaker.Util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.prgoh.dailymaker.Adapter.AlarmAdapter;
import com.example.prgoh.dailymaker.DB.AlarmDBManager;
import com.example.prgoh.dailymaker.Item.Alarm;
import com.example.prgoh.dailymaker.Item.Code;
import com.example.prgoh.dailymaker.Item.CustomToast;
import com.example.prgoh.dailymaker.Receiver.AlarmReceiver;
import com.example.schedulelibrary.TimeDwEnn;

import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by prgoh on 2017-07-25.
 */

public class AlarmHelper {

    public static Calendar getTime(Alarm alarm) throws ParseException {
        String time = alarm.getTime();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh : mm a");
        calendar.setTime(dateFormat.parse(time));

        calendar = TimeDwEnn.getTime(calendar);

        Calendar now = Calendar.getInstance();
        long duration = now.getTimeInMillis() - calendar.getTimeInMillis();

        boolean[] weekDay = parseStrWeekDay(alarm.getWeekDay());

        if (checkBool(weekDay)) {
            calendar = getDayAfterCal(calendar, weekDay);
            return calendar;
        }

        if (duration < 0)
            return calendar;
        else{
            calendar.add(Calendar.DAY_OF_WEEK, 1);
            return calendar;
        }
    }

    public static boolean[] parseStrWeekDay(String data){
        boolean[] weekDay = new boolean[]{false, false, false, false, false, false, false, false};

        if (data == "반복안함"){
            Log.e("no repeat called", "weekdata");
            return weekDay;
        }else if (data.length() == 2){
            Arrays.fill(weekDay, true);
            Log.e("everyday called", "weekdata");
            return weekDay;
        }else if (data.length() == 3 && data.charAt(1) != ' '){
            switch (data.substring(0, 1)){
                case "일" :{
                    weekDay[Code.SUN_DAY] = true;
                    break;
                }
                case "월" : {
                    weekDay[Code.MON_DAY] = true;
                    break;
                }
                case "화" : {
                    weekDay[Code.THU_DAY] = true;
                    break;
                }
                case "수" : {
                    weekDay[Code.WEN_DAY] = true;
                    break;
                }
                case "목" : {
                    weekDay[Code.THUR_DAY] = true;
                    break;
                }
                case "금" : {
                    weekDay[Code.FRI_DAY] = true;
                    break;
                }
                case "토" : {
                    weekDay[Code.SAT_DAY] = true;
                    break;
                }
            }
            Log.e("everyday called1", "weekdata");
            return weekDay;
        } else {
            String[] week = data.split("\\s+");

            for (int i=0; i<week.length; i++){
                switch (week[i]){
                    case "일" :{
                        weekDay[Code.SUN_DAY] = true;
                        break;
                    }
                    case "월" : {
                        weekDay[Code.MON_DAY] = true;
                        break;
                    }
                    case "화" : {
                        weekDay[Code.THU_DAY] = true;
                        break;
                    }
                    case "수" : {
                        weekDay[Code.WEN_DAY] = true;
                        break;
                    }
                    case "목" : {
                        weekDay[Code.THUR_DAY] = true;
                        break;
                    }
                    case "금" : {
                        weekDay[Code.FRI_DAY] = true;
                        break;
                    }
                    case "토" : {
                        weekDay[Code.SAT_DAY] = true;
                        break;
                    }
                }
            }
            Log.e("everyday called2", "weekdata");
            return weekDay;
        }
    }

    public static void releaseAlarm(Context context, Alarm alarm){

        int requestCode = alarm.getAlarmCode();

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.cancel(pi);
        pi.cancel();

//        Toast.makeText(context, "알람 해제됨", Toast.LENGTH_SHORT).show();
    }

    public static void setAlarm(Context context, Alarm alarm) throws ParseException {
        Intent intent = new Intent(context, AlarmReceiver.class);

        int requestCode = alarm.getAlarmCode();
        boolean[] weekDay = parseStrWeekDay(alarm.getWeekDay());

        Bundle bundle = new Bundle();
        bundle.putSerializable("alarm", alarm);

        if (checkBool(weekDay)){
            intent.putExtra("bundle", bundle);

            PendingIntent pi = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            long oneday = 24 * 60 * 60 * 1000;
            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            am.setRepeating(AlarmManager.RTC_WAKEUP, getTime(alarm).getTimeInMillis(), oneday, pi);
            Log.e("setAlarm", "OK");

        }else{
            Calendar cal = Calendar.getInstance();
            int today = cal.get(Calendar.DAY_OF_WEEK);

            weekDay[today] = true;

            bundle.putBooleanArray("weekDay", weekDay);
            intent.putExtra("bundle", bundle);

            setCustomAlarm(context, getTime(alarm).getTimeInMillis(), intent, requestCode);
        }
    }

    //re-set alarm method
    public static void re_setAlarm(Context context, Alarm alarm) throws ParseException {
        Intent intent = new Intent(context, AlarmReceiver.class);

        int requestCode = alarm.getAlarmCode();
        boolean[] weekDay = parseStrWeekDay(alarm.getWeekDay());

        Bundle bundle = new Bundle();
        bundle.putSerializable("alarm", alarm);


        if (checkBool(weekDay)){
            bundle.putBooleanArray("weekDay", weekDay);
            intent.putExtra("bundle", bundle);

            PendingIntent pi = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            long oneday = 24 * 60 * 60 * 1000;
            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            am.setRepeating(AlarmManager.RTC_WAKEUP, getTime(alarm).getTimeInMillis(), oneday, pi);
            Log.e("setAlarm", "OK");

        }else{
            Calendar cal = Calendar.getInstance();
            int today = cal.get(Calendar.DAY_OF_WEEK);

            boolean[] todaybool = parseStrWeekDay(alarm.getWeekDay());
            todaybool[today] = true;

            bundle.putBooleanArray("weekDay", todaybool);
            intent.putExtra("bundle", bundle);

            setCustomAlarm(context, getTime(alarm).getTimeInMillis(), intent, requestCode);
        }
    }

    public static void setCustomAlarm(Context context, long time,
                                      Intent intent, int requestCode){
        PendingIntent pi = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, time, pi);
    }

    public static boolean checkBool(boolean[] week){
        for (int i=0; i<week.length; i++){
            if (week[i] == true){
                return true;
            }
        }
        return false;
    }

    public static boolean getBoolFromStr(String data){
        int bool = Integer.parseInt(data);

        if (bool == 1){
            return true;
        }else
            return false;
    }

    public static void toastTime(Context context, Calendar calendar){
        CustomToast.customToast(context, TimeDwEnn.getPerTime(calendar));
    }

    //설정한 날짜가 1개  이상
    public static Calendar getDayAfterCal(Calendar cal, boolean[] weekDay){
        Calendar now = Calendar.getInstance();

        int nowDay = now.get(Calendar.DAY_OF_WEEK);
        int[] week = new int[8];
        int count = 0;

        for (int i=0; i<weekDay.length; i++){
            if (weekDay[i] == true) {
                week[count] = i;
                count++;
            }
        }

        for (int data : week){
            Log.e("weekInt", ""+data);
        }

        //설정한 날짜가 오늘이 아니라면
        if (!weekDay[nowDay]){
            for (int data : week){
                if (nowDay < data){
                    cal.add(Calendar.DAY_OF_WEEK, data - nowDay);
                    Log.e("wrongdata", "wrong");
                    return cal;
                }
            }

            int dayAfter = 7 - (nowDay - week[0]);
            cal.add(Calendar.DATE, dayAfter);

            return cal;
        }else{
            // 설정한 날짜가 오늘이라면
            for (int data : week){
                if (nowDay < data){
                    cal.add(Calendar.DAY_OF_WEEK, data - nowDay);
                    Log.e("wrongdata", "wrongdata");
                    return cal;
                }else if (nowDay == data){
                    if (!(now.getTimeInMillis() - cal.getTimeInMillis() < 0)) {
                        cal.add(Calendar.DAY_OF_WEEK, 1);
                        return cal;
                    }else{
                        return cal;
                    }
                }
            }

            if (!(now.getTimeInMillis() - cal.getTimeInMillis() < 0))
                cal.add(Calendar.DATE, 7);

            return cal;
        }
    }

    public static ArrayList<Alarm> insertAlarm(ArrayList<Alarm> data, Alarm alarm){
         return sortAlarm(data, alarm);
    }

    //기본 sort
    public static ArrayList<Alarm> sortAlarm(ArrayList<Alarm> data){

        // 모든 알람 정렬
        Collections.sort(data, new Comparator<Alarm>() {
            @Override
            public int compare(Alarm alarm, Alarm t1) {
                try {
                    if (AlarmHelper.getTime(alarm).getTimeInMillis() > AlarmHelper.getTime(t1).getTimeInMillis())
                        return 1;
                    else if (AlarmHelper.getTime(alarm).getTimeInMillis() < AlarmHelper.getTime(t1).getTimeInMillis())
                        return -1;
                    else
                        return 0;
                } catch (ParseException e) {
                    e.printStackTrace();
                    return 0;
                }
            }
        });

        return data;
    }

    //새로운 데이터 추가시
    public static ArrayList<Alarm> sortAlarm(ArrayList<Alarm> data, Alarm alarm){

        data.add(alarm);

        // 모든 알람 정렬
        Collections.sort(data, new Comparator<Alarm>() {
            @Override
            public int compare(Alarm alarm, Alarm t1) {
                try {
                    if (AlarmHelper.getTime(alarm).getTimeInMillis() > AlarmHelper.getTime(t1).getTimeInMillis())
                        return 1;
                    else if (AlarmHelper.getTime(alarm).getTimeInMillis() < AlarmHelper.getTime(t1).getTimeInMillis())
                        return -1;
                    else
                        return 0;
                } catch (ParseException e) {
                    e.printStackTrace();
                    return 0;
                }
            }
        });

        return data;
    }

    public static ArrayList<Alarm> removeAlarm(ArrayList<Alarm> data, int position){
        data.remove(position);
        return sortAlarm(data);
    }

    public static int compareWithAlarm(Alarm alarmData){
        ArrayList<Alarm> list = AlarmAdapter.list;
        int position = -1;

        if (list == null)
            return position;

        for (int i=0; i<list.size(); i++){
            Alarm alarm = list.get(i);

             if (alarm.getAlarmCode() == alarmData.getAlarmCode())
                 return i;

        }

        return position;
    }
}
