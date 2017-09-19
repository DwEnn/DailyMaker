package com.example.prgoh.dailymaker.Activity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.example.prgoh.dailymaker.Adapter.MusicListAdapter;
import com.example.prgoh.dailymaker.Item.MusicDto;
import com.example.prgoh.dailymaker.R;
import com.example.prgoh.dailymaker.Util.BaseActivity;
import com.example.prgoh.dailymaker.Util.Divider;
import com.example.prgoh.dailymaker.databinding.MusicListActivityBinding;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by prgoh on 2017-07-23.
 */

public class MusicListActivity extends BaseActivity {

    private MusicListActivityBinding binding;

    private ArrayList<MusicDto> list;
    private MusicListAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.music_list_activity);

        getMusicList();
        adapter = new MusicListAdapter(this, list);

        binding.musicRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        binding.musicRecyclerview.setHasFixedSize(true);
        binding.musicRecyclerview.addItemDecoration(new Divider(this));
        binding.musicRecyclerview.setAdapter(adapter);
    }

    public void getMusicList(){
        list = new ArrayList<>();

        String[] projection = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST
        };

        Cursor c = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection, null, null, null);

        while (c.moveToNext()){
            MusicDto musicDto = new MusicDto();
            musicDto.setId(c.getString(c.getColumnIndex(MediaStore.Audio.Media._ID)));
            musicDto.setTitle(c.getString(c.getColumnIndex(MediaStore.Audio.Media.TITLE)));
            musicDto.setArtist(c.getString(c.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
            musicDto.setAlbumId(c.getString(c.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)));
            musicDto.setStrUri(Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, ""+
                musicDto.getId()).toString());
            list.add(musicDto);
//            Log.e("music URI", ""+musicDto.getStrUri());
        }
        c.close();
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.music_ok_btn : {
                Intent intent = getIntent();
                MusicDto musicDto = adapter.getMusic();
                intent.putExtra("music", musicDto);
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter.destroyPlayer();
    }
}
