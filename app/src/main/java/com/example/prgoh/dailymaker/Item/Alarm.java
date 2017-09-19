package com.example.prgoh.dailymaker.Item;

import java.io.Serializable;

/**
 * Created by prgoh on 2017-07-24.
 */

public class Alarm implements Serializable {

    private String time;
    private String weekDay;
    private String vibrate;
    private String bellUri;
    private String bellTitle;
    private String todo;
    private int alarmCode;
    private String isChecked;
    private String boss;
    private String isQuick;

    public void setBellUri(String bellUri) {
        this.bellUri = bellUri;
    }

    public void setBellTitle(String bellTitle) {
        this.bellTitle = bellTitle;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setTodo(String todo) {
        this.todo = todo;
    }

    public void setVibrate(String vibrate) {
        this.vibrate = vibrate;
    }

    public void setWeekDay(String weekDay) {
        this.weekDay = weekDay;
    }

    public String getBellTitle() {
        return bellTitle;
    }

    public String getBellUri() {
        return bellUri;
    }

    public String getTime() {
        return time;
    }

    public String getTodo() {
        return todo;
    }

    public String getVibrate() {
        return vibrate;
    }

    public String getWeekDay() {
        return weekDay;
    }

    public void setAlarmCode(int alarmCode) {
        this.alarmCode = alarmCode;
    }

    public int getAlarmCode() {
        return alarmCode;
    }

    public void setChecked(String checked) {
        isChecked = checked;
    }

    public String getChecked(){
        return isChecked;
    }

    public void setBoss(String boss) {
        this.boss = boss;
    }

    public String getBoss() {
        return boss;
    }

    public void setIsQuick(String isQuick) {
        this.isQuick = isQuick;
    }

    public String getIsQuick() {
        return isQuick;
    }
}
