package com.example.prgoh.dailymaker.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prgoh.dailymaker.Activity.AlarmEditActivity;
import com.example.prgoh.dailymaker.Activity.AlarmSetActivity;
import com.example.prgoh.dailymaker.DB.AlarmDBManager;
import com.example.prgoh.dailymaker.Item.Alarm;
import com.example.prgoh.dailymaker.Item.Code;
import com.example.prgoh.dailymaker.Item.CustomToast;
import com.example.prgoh.dailymaker.R;
import com.example.prgoh.dailymaker.Util.AlarmHelper;
import com.example.prgoh.dailymaker.Util.Fonts;
import com.example.prgoh.dailymaker.Util.ItemTouchHelperListener;
import com.example.prgoh.dailymaker.databinding.AlarmLayoutBinding;

import org.json.JSONException;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

import static com.example.prgoh.dailymaker.R.id.view;

/**
 * Created by prgoh on 2017-07-21.
 */

public class AlarmAdapter extends RecyclerView.Adapter implements ItemTouchHelperListener {

    private Activity activity;
    private Context context;
    private Fragment fragment;

    public static ArrayList<Alarm> list;

    private PopupWindow todoWindow;
    private View        todoLayout;

    private TextView tv;

    private int lastPosition = -1;

    private Handler handler = new Handler();
    private Handler switchHandler = new Handler();


    public AlarmAdapter(Activity activity, Context context, ArrayList<Alarm> list, Fragment fragment){
        this.activity = activity;
        this.context = context;
        this.list = list;
        this.fragment = fragment;
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            todoWindow.showAsDropDown(view, 0, -60);
        }
    };

    private View.OnLongClickListener longClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            return true;
        }
    };


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alarm_layout, parent, false);

        todoLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.pop_up_layout, null);

        Fonts.setGlobalFont(parent.getContext(), view);
        Fonts.setGlobalFont(parent.getContext(), todoLayout);

        return new AlarmVh(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final AlarmVh item = (AlarmVh)holder;
        final Alarm alarm = list.get(position);

        setAnimation(item.itemView, position);

        item.binding.timeTv.setText(alarm.getTime());
        item.binding.weekday.setText(alarm.getWeekDay());

        // TODO: 2017-08-03 thread 로 돌리지 말아보고서 실행 ㄱㄱ
        // 더 자연스러워 졌음. 만족

//        changeview = new Thread(){
//            @Override
//            public void run() {
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//
//                    }
//                });
//            }
//        };
//
//        changeview.start();

        if (!AlarmHelper.getBoolFromStr(alarm.getChecked())) {
            item.binding.alarmSwitch.setChecked(false);
            item.binding.alarmCard.setCardBackgroundColor(activity.getResources().getColor(R.color.disabledCard));
        }else {
            item.binding.alarmSwitch.setChecked(true);
            item.binding.alarmCard.setCardBackgroundColor(activity.getResources().getColor(R.color.textColor));
        }

        if (alarm.getTodo().length() != 0) {
            item.binding.todoTv.setText(alarm.getTodo());
            item.binding.todoListIcon.setVisibility(View.VISIBLE);
            item.binding.todoTv.setVisibility(View.VISIBLE);

            tv = todoLayout.findViewById(R.id.popup_tv);
            tv.setText(alarm.getTodo());

            todoWindow = new PopupWindow(todoLayout, ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT, true);
            todoWindow.setAnimationStyle(-1);
            todoWindow.setBackgroundDrawable(new ColorDrawable());
        }

        item.binding.alarmCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, AlarmEditActivity.class);
                intent.putExtra("alarm", alarm);
                intent.putExtra("position", position);
                fragment.startActivityForResult(intent, Code.EDIT_ALARM_REQUEST);
            }
        });

        item.binding.alarmSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (item.binding.alarmSwitch.isPressed()) {
                    if (b){
                        alarm.setChecked("1");
                        Thread thread = new Thread(){
                            @Override
                            public void run() {
                                switchHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            AlarmHelper.re_setAlarm(context, alarm);
                                            AlarmHelper.toastTime(context, AlarmHelper.getTime(alarm));

                                            notifyItemChanged(position);
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }
                        };
                        thread.start();
                    }else{
                        alarm.setChecked("0");
                        Thread thread = new Thread(){
                            @Override
                            public void run() {
                                switchHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        AlarmHelper.releaseAlarm(context, alarm);
                                        CustomToast.customToast(context, "알람 해제됨");

                                        notifyItemChanged(position);
                                    }
                                });
                            }
                        };
                        thread.start();
                    }
                    Log.e("Called listener", "Pressed");
                }
                Log.e("Called listener", "else");
            }
        });

        item.binding.todoListIcon.setOnClickListener(listener);
        item.binding.todoTv.setOnClickListener(listener);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onItemRemove(int position) throws JSONException {
        if (AlarmHelper.getBoolFromStr(list.get(position).getChecked()))
            AlarmHelper.releaseAlarm(context, list.get(position));
        list.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if(fromPosition < 0 || fromPosition >= list.size() || toPosition < 0 || toPosition >= list.size()){
            return false;
        }

//        String fromItem = items.get(fromPosition);
//        items.remove(fromPosition);
//        items.add(toPosition, fromItem);

        notifyItemMoved(fromPosition, toPosition);
        return true;
    }


    public class AlarmVh extends RecyclerView.ViewHolder{

        private AlarmLayoutBinding binding;

        public AlarmVh(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }

    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) throws JSONException, ParseException {
        switch (requestCode){
            case Code.ALARM_REQUEST : {
                Alarm alarm = (Alarm) data.getSerializableExtra("alarm");

//                int alarmCode = AlarmHelper.setAlarm(context, alarm);
//                alarm.setAlarmCode(alarmCode);

                list = AlarmHelper.insertAlarm(list, alarm);
                notifyDataSetChanged();
                break;
            }
            case Code.EDIT_ALARM_REQUEST : {
                Alarm alarm = (Alarm) data.getSerializableExtra("alarm");
                int position = data.getIntExtra("position", 0);

                //변경된 알람 설정
//                AlarmHelper.re_setAlarm(context, alarm);

                //변경 전 알람 해제
                AlarmHelper.releaseAlarm(context, list.get(position));
                list.set(position, alarm);
                list = AlarmHelper.sortAlarm(list);
                notifyDataSetChanged();
                break;
            }
            case Code.QUICK_ALARM_REQUEST : {
                Alarm alarm = (Alarm) data.getSerializableExtra("alarm");
                list = AlarmHelper.insertAlarm(list, alarm);
                notifyDataSetChanged();
                break;
            }
        }
    }

    public void onActivityResult(Intent data) {
        int position = data.getIntExtra("position", 0);

        if (AlarmHelper.getBoolFromStr(list.get(position).getChecked()))
            AlarmHelper.releaseAlarm(context, list.get(position));
        list.remove(position);
        CustomToast.customToast(context, "알람 삭제됨");
        notifyItemRemoved(position);
    }

    public ArrayList<Alarm> returnData(){
        return list;
    }

}