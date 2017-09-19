package com.example.prgoh.dailymaker.Item;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prgoh.dailymaker.R;
import com.example.prgoh.dailymaker.Util.Fonts;

/**
 * Created by prgoh on 2017-08-10.
 */

public class CustomToast extends AppCompatActivity{

    public static void customToast(Context context, String msg){
        View view = LayoutInflater.from(context).inflate(R.layout.toast_layout, null);
        TextView textView = (TextView) view.findViewById(R.id.toast_tv);
        textView.setText(msg);

        Fonts.setGlobalFont(context, view);

        Toast toast = new Toast(context.getApplicationContext());
        toast.setGravity(Gravity.BOTTOM, 0, 250);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(view);
        toast.show();
    }
}
