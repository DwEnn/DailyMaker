package com.example.prgoh.dailymaker.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.example.prgoh.dailymaker.Adapter.RingListAdapter;
import com.example.prgoh.dailymaker.Item.RingtoneDto;
import com.example.prgoh.dailymaker.R;
import com.example.prgoh.dailymaker.Util.BaseActivity;
import com.example.prgoh.dailymaker.Util.Divider;
import com.example.prgoh.dailymaker.databinding.MusicListActivityBinding;

import java.util.ArrayList;

/**
 * Created by prgoh on 2017-07-24.
 */

public class RingListActivity extends BaseActivity {

    private MusicListActivityBinding binding;
    private RingListAdapter adapter;

    private ArrayList<RingtoneDto> list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.music_list_activity);

        getBellList();
        adapter = new RingListAdapter(this, list);

        binding.musicRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        binding.musicRecyclerview.addItemDecoration(new Divider(this));
        binding.musicRecyclerview.setHasFixedSize(true);
        binding.musicRecyclerview.setAdapter(adapter);
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.music_ok_btn : {
                Intent intent = getIntent();
                RingtoneDto ringtoneDto = adapter.getRing();
                intent.putExtra("ring", ringtoneDto);
                setResult(RESULT_OK, intent);
                finish();
                break;
            }
        }
    }


    private void getBellList(){
        list = new ArrayList<>();

        RingtoneManager rm = new RingtoneManager(this);
        Cursor c = rm.getCursor();

        while (c.moveToNext()) {
            RingtoneDto ring = new RingtoneDto();
            ring.setId(c.getLong(RingtoneManager.ID_COLUMN_INDEX));
            ring.setStringUri(Uri.parse(c.getString(RingtoneManager.URI_COLUMN_INDEX)).toString()+"/"+ring.getId());
            ring.setStr_id(c.getString(RingtoneManager.ID_COLUMN_INDEX));
            ring.setTitle(c.getString(RingtoneManager.TITLE_COLUMN_INDEX));
            list.add(ring);
        }
        c.close();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter.destroyPlayer();
    }
}
