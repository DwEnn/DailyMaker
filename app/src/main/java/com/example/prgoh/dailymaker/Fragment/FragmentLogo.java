package com.example.prgoh.dailymaker.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.prgoh.dailymaker.R;
import com.example.prgoh.dailymaker.Util.Fonts;

/**
 * Created by prgoh on 2017-07-21.
 */

public class FragmentLogo extends Fragment {

    private Context context;

    public FragmentLogo(){
//        context = getContext();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_logo, container, false);
        context = container.getContext();
        Fonts.setGlobalFont(context, view);
        return view;
    }
}
