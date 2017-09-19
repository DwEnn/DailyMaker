package com.example.prgoh.dailymaker.Item;

/**
 * Created by prgoh on 2017-07-24.
 */

public class Code {

    public static final int MY_PERMISSIONS = 1001;

    public static final int MUSIC_RESULT = 99;
    public static final int RING_RESULT = 98;
    public static final int NO_BELL_RESULT = 97;
    public static final int DELETE_ALARM_RESULT = 96;

    public static final int MUSIC_REQUEST = 101;
    public static final int BELL_REQUEST = 102;
    public static final int RING_REQUEST = 103;
    public static final int TIME_REQUEST = 104;
    public static final int ALARM_REQUEST = 105;
    public static final int EDIT_ALARM_REQUEST = 106;
    public static final int QUICK_ALARM_REQUEST = 107;
    public static final int VOLUME_MAKE_LOUDER_REQUEST = 108;
    public static final int ALARM_AUTO_CANCEL_REQUEST = 109;

    public static final int SUN_DAY = 1;
    public static final int MON_DAY = 2;
    public static final int THU_DAY = 3;
    public static final int WEN_DAY = 4;
    public static final int THUR_DAY = 5;
    public static final int FRI_DAY = 6;
    public static final int SAT_DAY = 7;

    public static final int MESSAGE_CLOCK = 200;
    public static final int MESSAGE_BEEP = 201;

    public static int generateRequestCode(){

        int requestCode = (int)(Math.random() * 10000) + 1;

        return requestCode;
    }
}
