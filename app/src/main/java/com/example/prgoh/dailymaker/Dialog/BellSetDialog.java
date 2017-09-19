package com.example.prgoh.dailymaker.Dialog;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.example.prgoh.dailymaker.Activity.MusicListActivity;
import com.example.prgoh.dailymaker.Activity.RingListActivity;
import com.example.prgoh.dailymaker.Item.Code;
import com.example.prgoh.dailymaker.Item.MusicDto;
import com.example.prgoh.dailymaker.Item.RingtoneDto;
import com.example.prgoh.dailymaker.R;
import com.example.prgoh.dailymaker.Util.BaseActivity;
import com.example.prgoh.dailymaker.databinding.BellSetDialogBinding;

/**
 * Created by prgoh on 2017-07-22.
 */

public class BellSetDialog extends BaseActivity {

    private BellSetDialogBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.bell_set_dialog);
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.no_bell_btn : {
                Intent intent = new Intent();
                intent.putExtra("nobell","무음");
                setResult(Code.NO_BELL_RESULT, intent);
                finish();
                break;
            }
            case R.id.bell_btn : {
                startActivityForResult(new Intent(this, RingListActivity.class), Code.RING_REQUEST);
                break;
            }
            case R.id.music_btn : {
                startActivityForResult(new Intent(this, MusicListActivity.class), Code.MUSIC_REQUEST);
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK){
            switch (requestCode){
                case Code.RING_REQUEST : {
                    Intent intent = new Intent();
                    RingtoneDto ringtoneDto = (RingtoneDto)data.getSerializableExtra("ring");
                    intent.putExtra("ring", ringtoneDto);
                    setResult(Code.RING_RESULT, intent);
                    finish();
                    break;
                }
                case Code.MUSIC_REQUEST : {
                    Intent intent = new Intent();
                    MusicDto musicDto = (MusicDto)data.getSerializableExtra("music");
                    intent.putExtra("music", musicDto);
                    setResult(Code.MUSIC_RESULT, intent);
                    finish();
                    break;
                }
            }
        }
    }
}
