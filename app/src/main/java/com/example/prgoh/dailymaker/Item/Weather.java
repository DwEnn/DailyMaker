package com.example.prgoh.dailymaker.Item;

/**
 * Created by prgoh on 2017-09-06.
 */

public class Weather {
    private String hour;  // 시간
    private String day;
    private String temp;  // 온도
    private String wfKor; // 상태
    private String pop; // 강수확률
    private String reh; // 습도
    private String sky;
    private String pty;

    public void setPty(String pty) {
        this.pty = pty;
    }

    public String getPty() {
        return pty;
    }

    public void setSky(String sky) {
        this.sky = sky;
    }

    public String getSky() {
        return sky;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getReh() {
        return reh;
    }

    public void setReh(String reh) {
        this.reh = reh;
    }

    public String getPop() {
        return pop;
    }

    public void setPop(String pop) {
        this.pop = pop;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public void setWfKor(String wfKor) {
        this.wfKor = wfKor;
    }

    public String getHour() {
        return hour;
    }

    public String getTemp() {
        return temp;
    }

    public String getWfKor() {
        return wfKor;
    }
}
