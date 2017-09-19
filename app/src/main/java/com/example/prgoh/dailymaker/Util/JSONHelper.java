package com.example.prgoh.dailymaker.Util;

import com.example.prgoh.dailymaker.Item.Alarm;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by prgoh on 2017-07-24.
 */

public class JSONHelper {

    public static String getJSONFromAlarm(Alarm alarm) throws JSONException {
        JSONObject object = new JSONObject();

        object.put("time", alarm.getTime());
        object.put("weekDay", alarm.getWeekDay());
        object.put("bellTitle", alarm.getBellTitle());
        object.put("bellUri", alarm.getBellUri());
        object.put("vibrate", alarm.getVibrate());
        object.put("todo", alarm.getTodo());
        object.put("alarmCode", alarm.getAlarmCode());
        object.put("isChecked", alarm.getChecked());
        object.put("bossAlarm", alarm.getBoss());
        object.put("isQuick", alarm.getIsQuick());

        return object.toString();
    }

    public static Alarm getAlarmFromJSON(String data) throws JSONException {

        JSONObject jsonObject = new JSONObject(data);

        Alarm alarm = new Alarm();

        alarm.setTime(jsonObject.getString("time"));
        alarm.setBellTitle(jsonObject.getString("bellTitle"));
        alarm.setVibrate(jsonObject.getString("vibrate"));
        alarm.setWeekDay(jsonObject.getString("weekDay"));
        alarm.setTodo(jsonObject.getString("todo"));
        alarm.setAlarmCode(jsonObject.getInt("alarmCode"));
        alarm.setChecked(jsonObject.getString("isChecked"));
        alarm.setBoss(jsonObject.getString("bossAlarm"));

        if (jsonObject.has("bellUri"))
            alarm.setBellUri(jsonObject.getString("bellUri"));
        if (jsonObject.has("isQuick"))
            alarm.setIsQuick(jsonObject.getString("isQuick"));

        return alarm;
    }
}
