package com.example.schedulelibrary;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeDwEnn {

    public static Calendar getZeroTime(){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Date date = cal.getTime();
        String today = new SimpleDateFormat("yyyyMMdd").format(date);
        try{
            cal.setTime(format.parse(today));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return cal;
    }

    public static String getPerTime(Calendar calendar){

        String perTime;
        Calendar now = Calendar.getInstance();

        long durationM = (calendar.getTimeInMillis() - now.getTimeInMillis())/60000 + 1;
        long durationH = (int)durationM/60;
        int h = (durationH < 24)? (int)(durationH) : (int)(durationH%24);
        int m = (int)(durationM%60);

        String perH = (h != 0)? h+"시간 " : "";
        String perM = (m != 0)? m+"분 " : "";
        String perD = (durationH >= 24)? (int)(durationH/24)+"일 " : "";

        return perTime = "지금부터 "+perD+perH+perM+"후";
    }

    public static String getTimeString(int h, int m){

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR, h);
        cal.add(Calendar.MINUTE, m);

        SimpleDateFormat format = new SimpleDateFormat("hh : mm a");

        return format.format(cal.getTime());
    }

    public static Calendar getTime(Calendar calendar){

        int h = calendar.get(Calendar.HOUR_OF_DAY);
        int m = calendar.get(Calendar.MINUTE);

        Calendar cal = getZeroTime();
        cal.add(Calendar.HOUR, h);
        cal.add(Calendar.MINUTE, m);

        return cal;
    }

    public static String getCheckedDayString(boolean[] data){
        String day="";
        int count = 0;

        for(int i=0; i<data.length; i++){
            if (data[i] == true){
                switch(i){
                    case 1 :
                        day+=" 일";
                        count++;
                        break;
                    case 2 :
                        day+=" 월";
                        count++;
                        break;
                    case 3 :
                        day+=" 화";
                        count++;
                        break;
                    case 4 :
                        day+=" 수";
                        count++;
                        break;
                    case 5 :
                        day+=" 목";
                        count++;
                        break;
                    case 6 :
                        day+=" 금";
                        count++;
                        break;
                    case 7 :
                        day+=" 토";
                        count++;
                        break;
                }
            }
        }

        if (count == 1)
            return (day+"요일").trim();
        else if (count == 0)
            return "반복안함";
        else if (count == 7)
            return "매일";
        else
            return day.trim();
    }

}
