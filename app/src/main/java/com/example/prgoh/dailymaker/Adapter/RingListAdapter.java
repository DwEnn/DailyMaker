package com.example.prgoh.dailymaker.Adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.example.prgoh.dailymaker.Item.MusicDto;
import com.example.prgoh.dailymaker.Item.RingtoneDto;
import com.example.prgoh.dailymaker.R;
import com.example.prgoh.dailymaker.Util.Fonts;
import com.example.prgoh.dailymaker.databinding.MusicListLayoutBinding;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by prgoh on 2017-07-24.
 */

public class RingListAdapter extends RecyclerView.Adapter {

    private Context context;
    private int mSelectedRadio = -1;
    private RadioButton mLastSelectedRadio;

    private MediaPlayer mediaPlayer;

    private ArrayList<RingtoneDto> list;

    public RingListAdapter(Context context, ArrayList list){
        this.list = list;
        this.context = context;
        mediaPlayer = new MediaPlayer();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.music_list_layout, parent, false);
        Fonts.setGlobalFont(context, view);
        return new RingVh(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final RingVh item = (RingVh)holder;
        final RingtoneDto ringtoneDto = list.get(position);

        item.binding.musicTitleTv.setText(ringtoneDto.getTitle());

        if (mSelectedRadio == position){
            item.binding.musicRadio.setChecked(true);
        }else{
            item.binding.musicRadio.setChecked(false);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check(item.binding.musicRadio, position);
                play(ringtoneDto.getStringUri());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private class RingVh extends RecyclerView.ViewHolder {

        private MusicListLayoutBinding binding;

        public RingVh(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
            binding.musicArtistTv.setVisibility(View.GONE);

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) binding.musicTitleTv.getLayoutParams();
            params.addRule(RelativeLayout.CENTER_VERTICAL);
            binding.musicTitleTv.setLayoutParams(params);
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

    public RingtoneDto getRing(){
        if (mSelectedRadio != -1)
            return list.get(mSelectedRadio);
        else
            return null;
    }

    private void play(String ring){
        Uri uri = Uri.parse(ring);

        if (mediaPlayer.isPlaying()) {
            mediaPlayer.release();
            mediaPlayer = new MediaPlayer();
        }

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
