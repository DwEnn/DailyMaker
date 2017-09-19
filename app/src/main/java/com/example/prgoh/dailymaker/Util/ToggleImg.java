package com.example.prgoh.dailymaker.Util;

import android.util.Log;
import android.widget.ImageView;

import com.example.prgoh.dailymaker.R;

/**
 * Created by prgoh on 2017-07-21.
 */

public class ToggleImg {

    public static void toggleImg(ImageView imageView, int type, boolean[] isChecked){
        switch (type){
            case 1 : {
                if (isChecked[type] == false)
                    imageView.setImageResource(R.drawable.sun);
                else
                    imageView.setImageResource(R.drawable.sun_en);
                break;
            }
            case 2 : {
                if (isChecked[type] == false)
                    imageView.setImageResource(R.drawable.mon);
                else
                    imageView.setImageResource(R.drawable.mon_en);
                Log.e("Called toggleImg !!" , "true");
                break;
            }
            case 3 : {
                if (isChecked[type] == false)
                    imageView.setImageResource(R.drawable.thu);
                else
                    imageView.setImageResource(R.drawable.thu_en);
                break;
            }
            case 4 : {
                if (isChecked[type] == false)
                    imageView.setImageResource(R.drawable.wen);
                else
                    imageView.setImageResource(R.drawable.wen_en);
                break;
            }
            case 5 : {
                if (isChecked[type] == false)
                    imageView.setImageResource(R.drawable.thur);
                else
                    imageView.setImageResource(R.drawable.thur_en);
                break;
            }
            case 6 : {
                if (isChecked[type] == false)
                    imageView.setImageResource(R.drawable.fri);
                else
                    imageView.setImageResource(R.drawable.fri_en);
                break;
            }
            case 7 : {
                if (isChecked[type] == false)
                    imageView.setImageResource(R.drawable.sat);
                else
                    imageView.setImageResource(R.drawable.sat_en);
                break;
            }
        }
    }
}
