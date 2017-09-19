package com.example.prgoh.dailymaker.DB;

import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.prgoh.dailymaker.Item.Alarm;
import com.example.prgoh.dailymaker.Receiver.AlarmReceiver;
import com.example.prgoh.dailymaker.Util.AlarmHelper;
import com.example.prgoh.dailymaker.Util.JSONHelper;

import org.json.JSONException;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import static android.R.attr.data;

/**
 * Created by prgoh on 2017-07-24.
 */

public class AlarmDBManager extends SQLiteOpenHelper {

    private static final String DB_ALARMS = "Alarms.db";
    private static final String TABLE_ALARMS = "Alarms";
    private static final int DB_VERSION = 2;

    Context context;

    private static AlarmDBManager dbManager;
    private SQLiteDatabase database;

    public static AlarmDBManager getInstance(Context context){
        if (dbManager == null){
            dbManager = new AlarmDBManager(context, DB_ALARMS, null, DB_VERSION);
        }
        return dbManager;
    }

    private AlarmDBManager(Context context, String dbName, SQLiteDatabase.CursorFactory factory,
                           int version){
        super(context,dbName,factory,version);
        this.context = context;
    }

    public long insert(ContentValues addRowValue){
        return getWritableDatabase().insert(TABLE_ALARMS, null, addRowValue);
    }

    public Cursor query(String[] columns, String selection,
                        String[] selectionArgs, String groupBy,
                        String having, String orderBy){
        return getReadableDatabase().query(TABLE_ALARMS, columns, selection, selectionArgs, groupBy, having, orderBy);
    }

    public int update(ContentValues updateRowValue, String[] whereArgs){
        return getWritableDatabase().update(TABLE_ALARMS, updateRowValue, "alarmcode=?", whereArgs);
    }

    public int delete(String whereClause, String[] whereArgs){
        return getWritableDatabase().delete(TABLE_ALARMS, whereClause, whereArgs);
    }

    public void deleteAll(String whereClause, String[] whereArgs){
        getWritableDatabase().delete(TABLE_ALARMS, whereClause, whereArgs);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_ALARMS + "(" +
                "_id        INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "position   INTEGER DEFAULT 100, " +
                "alarm      TEXT, "+
                "alarmcode  INTEGER );");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    public ArrayList<Alarm> queryAlarm(Context context) throws JSONException {

        ArrayList<Alarm> alarmData = new ArrayList<>();

        String[] columns = new String[]{
                "alarm"
        };

        Cursor c = dbManager.query(columns, null, null, null, null, null);

        if (c != null){
            while(c.moveToNext()){
                Alarm alarm = JSONHelper.getAlarmFromJSON(c.getString(0));
                alarmData.add(alarm);
            }
            c.close();
        }

        return alarmData;
    }

    public void refreshAlarm(ArrayList<Alarm> data) throws JSONException {

        //DB알람 삭제
        dbManager.deleteAll(null, null);

        // 알람 db삽입
        for (int i=0; i<data.size(); i++){
            int alarmCode = data.get(i).getAlarmCode();

            ContentValues addRowValues = new ContentValues();
            addRowValues.put("position", i);
            addRowValues.put("alarm", JSONHelper.getJSONFromAlarm(data.get(i)));
            addRowValues.put("alarmcode", alarmCode);

            dbManager.insert(addRowValues);
        }
    }

    public HashMap<Integer, Alarm> updateAlarm(Alarm data) throws JSONException {
        ArrayList<Alarm> alarms = new ArrayList<>();

        //새로운 alarm 저장
        alarms.add(data);

        //DB의 알람 쿼리 후 alarms에 삽입
        String[] columns = new String[]{"alarm"};
        Cursor c = dbManager.query(columns, null, null, null, null, null);

        if (c != null){
            while (c.moveToNext()){
                alarms.add(JSONHelper.getAlarmFromJSON(c.getString(0)));
            }
            c.close();
        }


        //DB알람 삭제
        dbManager.deleteAll(null, null);

        // 모든 알람 정렬
        Collections.sort(alarms, new Comparator<Alarm>() {
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

        HashMap<Integer, Alarm> alarmHashMap = new HashMap<>();

        // 알람 db삽입
        for (int i=0; i<alarms.size(); i++){
            alarmHashMap.put(i, alarms.get(i));

            ContentValues addRowValues = new ContentValues();
            addRowValues.put("position", i);
            addRowValues.put("alarm", JSONHelper.getJSONFromAlarm(alarms.get(i)));

            dbManager.insert(addRowValues);
        }

        return alarmHashMap;
    }

    public HashMap<Integer, Alarm> updateAlarm() throws JSONException {

        ArrayList<Alarm> alarms = new ArrayList<>();

        //DB의 알람 쿼리 후 alarms에 삽입
        String[] columns = new String[]{"alarm"};
        Cursor c = dbManager.query(columns, null, null, null, null, null);

        if (c != null){
            while (c.moveToNext()){
                Alarm alarm = JSONHelper.getAlarmFromJSON(c.getString(0));
                alarms.add(alarm);
            }
            c.close();
        }

        //DB알람 삭제
        dbManager.deleteAll(null, null);

        // 모든 알람 정렬
        Collections.sort(alarms, new Comparator<Alarm>() {
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

        HashMap<Integer, Alarm> alarmHashMap = new HashMap<>();

        // 알람 db삽입 포지션 변경 때문에 있어야함
        for (int i=0; i<alarms.size(); i++){
            alarmHashMap.put(i, alarms.get(i));

            ContentValues addRowValues = new ContentValues();
            addRowValues.put("position", i);
            addRowValues.put("alarm", JSONHelper.getJSONFromAlarm(alarms.get(i)));

            dbManager.insert(addRowValues);
        }

        return alarmHashMap;
    }

    public void receivedAlarm(Context context, Alarm alarm, boolean isNoRepeat){

        ArrayList<Alarm> data = null;

        try {
            data = queryAlarm(context);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        int position = -1;

        for (int i=0; i<data.size(); i++){
            Alarm alarm1 = data.get(i);

            if (alarm.getAlarmCode() == alarm1.getAlarmCode())
                position = i;
        }

        if (alarm.getIsQuick() != null){
            data.remove(position);
            AlarmHelper.sortAlarm(data);
            try {
                refreshAlarm(data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else if (isNoRepeat){
            alarm.setChecked("0");
            data.set(position, alarm);
            AlarmHelper.sortAlarm(data);
            try {
                refreshAlarm(data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}

