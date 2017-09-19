package com.example.prgoh.dailymaker.Adapter;

import android.content.ContentResolver;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.prgoh.dailymaker.Item.MusicDto;
import com.example.prgoh.dailymaker.Item.RippleEffect;
import com.example.prgoh.dailymaker.R;
import com.example.prgoh.dailymaker.Util.Fonts;
import com.example.prgoh.dailymaker.databinding.MusicListLayoutBinding;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by prgoh on 2017-07-23.
 */

public class MusicListAdapter extends RecyclerView.Adapter {

    private Context context;
    private ArrayList<MusicDto> list;

    private int mSelectedRadio = -1;
    private RadioButton mLastSelectedRadio;

    private MediaPlayer mediaPlayer;

    public MusicListAdapter(Context context, ArrayList list){
        this.context = context;
        this.list = list;
        mediaPlayer = new MediaPlayer();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.music_list_layout, parent, false);
        Fonts.setGlobalFont(context, view);
        return new ListVh(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ListVh item = (ListVh)holder;
        final MusicDto musicDto = list.get(position);

        item.binding.musicTitleTv.setText(musicDto.getTitle());
        item.binding.musicArtistTv.setText(musicDto.getArtist());

        Uri uri = Uri.parse("content://media/external/audio/albumart/" +
                Integer.parseInt(list.get(position).getAlbumId()));
        if (uri!= null) {
            Glide.with(context).load(uri).crossFade().override(60, 60).error(R.drawable.unkown_icon)
                    .diskCacheStrategy(DiskCacheStrategy.ALL).thumbnail(0.5f).into(item.binding.musicImg);
        }

        if (mSelectedRadio == position)
            item.binding.musicRadio.setChecked(true);
        else
            item.binding.musicRadio.setChecked(false);

        item.binding.musicList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check(item.binding.musicRadio, position);
                play(musicDto.getStrUri());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private class ListVh extends RecyclerView.ViewHolder {

        private MusicListLayoutBinding binding;

        public ListVh(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }

    private void check(RadioButton rBtn, int position){

        if (mSelectedRadio == position){
            return;
        }

        mSelectedRadio = position;

        if (mLastSelectedRadio != null){
            mLastSelectedRadio.setChecked(false);
        }

        mLastSelectedRadio = rBtn;
        notifyDataSetChanged();
    }

    public MusicDto getMusic(){
        if (mSelectedRadio != -1)
            return list.get(mSelectedRadio);
        else
            return null;
    }

    private void play(String music){

        if (mediaPlayer.isPlaying()){
            mediaPlayer.release();
            mediaPlayer = new MediaPlayer();
        }

        Uri uri = Uri.parse(music);

        try {
            mediaPlayer.setDataSource(context, uri);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
            mediaPlayer.release();
        }
        mediaPlayer.start();
    }

    public void destroyPlayer(){
        if (mediaPlayer != null){
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
