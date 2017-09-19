package com.example.prgoh.dailymaker.Fragment;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.prgoh.dailymaker.Activity.AlarmSetActivity;
import com.example.prgoh.dailymaker.DB.AlarmDBManager;
import com.example.prgoh.dailymaker.Dialog.QuickAlarmSetActivity;
import com.example.prgoh.dailymaker.Activity.SettingActivity;
import com.example.prgoh.dailymaker.Adapter.AlarmAdapter;
import com.example.prgoh.dailymaker.Item.Alarm;
import com.example.prgoh.dailymaker.Item.Code;
import com.example.prgoh.dailymaker.R;
import com.example.prgoh.dailymaker.Util.Fonts;
import com.example.prgoh.dailymaker.Util.ListAnimator;
import com.example.prgoh.dailymaker.Util.SimpleCallbackAdapter;
import com.example.prgoh.dailymaker.databinding.FragmentAlarmBinding;

import org.json.JSONException;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;

/**
 * Created by prgoh on 2017-07-21.
 */

public class FragmentAlarm extends Fragment {

    private Context context;
    private FragmentAlarmBinding binding;
    private AlarmAdapter adapter;

    private ArrayList<Alarm> list;
    private AlarmDBManager dbManager;

    public FragmentAlarm(){
    }

    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e("Fragment CYCLE", "\tonCreateView");
        View view = inflater.inflate(R.layout.fragment_alarm, container, false);

        context = container.getContext();

        dbManager = AlarmDBManager.getInstance(context);

        try {
            list = dbManager.queryAlarm(context);
            Log.e("DBQUERY", "\tSUCCESS");
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("failed", "false");
        }

        adapter = new AlarmAdapter(getActivity(), context, list, this);

        final ItemTouchHelper touchHelper = new ItemTouchHelper(new SimpleCallbackAdapter(adapter, context));

        binding = DataBindingUtil.bind(view);
        binding.alarmRecyclerview.setHasFixedSize(true);
        binding.alarmRecyclerview.setLayoutManager(new LinearLayoutManager(context));
        binding.alarmRecyclerview.setAdapter(adapter);
        binding.alarmRecyclerview.setItemAnimator(new ListAnimator());

        touchHelper.attachToRecyclerView(binding.alarmRecyclerview);

        binding.settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().startActivity(new Intent(getActivity(), SettingActivity.class));
            }
        });

        binding.alarmIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getActivity(), AlarmSetActivity.class), Code.ALARM_REQUEST);
            }
        });

        binding.quickAlarmIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getActivity(), QuickAlarmSetActivity.class), Code.QUICK_ALARM_REQUEST);
            }
        });

//        binding.mannerIcon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(getContext(), "구현 중", Toast.LENGTH_SHORT).show();
//            }
//        });

        Fonts.setGlobalFont(context, view);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.e("Fragment CYCLE", "\tonCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode){
            case RESULT_OK : {
                try {
                    adapter.onActivityResult(requestCode, resultCode, data);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            }case Code.DELETE_ALARM_RESULT : {
                adapter.onActivityResult(data);
                break;
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("onPause", "cycle");
        Thread thread = new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    dbManager.refreshAlarm(adapter.returnData());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();

        try {
            thread.join();
            adapter.notifyDataSetChanged();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("onResume", "cycle");
    }
}
