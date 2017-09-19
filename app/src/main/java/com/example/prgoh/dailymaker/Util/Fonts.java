package com.example.prgoh.dailymaker.Util;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by prgoh on 2017-07-21.
 */

public class Fonts {

    public static void setGlobalFont(Context context, View view) {
        if(view != null) {
            if(view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup)view;
                int vgCnt = viewGroup.getChildCount();
                for(int i = 0; i<vgCnt; i++) {
                    View v = viewGroup.getChildAt(i);
                    if(v instanceof TextView) {
                        ((TextView) v).setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/SDMiSaeng.ttf"));
                    }
                    setGlobalFont(context, v);
                }
            }
        }
    }

    public static void setFont(){

    }
}
