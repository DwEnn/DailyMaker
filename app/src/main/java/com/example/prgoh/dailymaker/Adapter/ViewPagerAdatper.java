package com.example.prgoh.dailymaker.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.prgoh.dailymaker.Fragment.FragmentWeather;
import com.example.prgoh.dailymaker.Fragment.FragmentLogo;
import com.example.prgoh.dailymaker.Fragment.FragmentAlarm;

/**
 * Created by prgoh on 2017-07-21.
 */

public class ViewPagerAdatper extends FragmentStatePagerAdapter {

    Fragment[] fragments = new Fragment[3];

    public ViewPagerAdatper(FragmentManager fm){
        super(fm);
        fragments[0] = new FragmentLogo();
        fragments[1] = new FragmentWeather();
        fragments[2] = new FragmentAlarm();
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }
}
