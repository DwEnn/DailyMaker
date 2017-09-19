package com.example.prgoh.dailymaker.Util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.prgoh.dailymaker.R;

/**
 * Created by prgoh on 2017-07-24.
 */


public class Divider extends RecyclerView.ItemDecoration {

    private Drawable mDivider;

    public Divider(Context context){
        mDivider = context.getResources().getDrawable(R.drawable.line_divider);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        float dp = parent.getContext().getResources().getDisplayMetrics().density;
        int left = parent.getPaddingLeft() + (int)(25*dp);
        int right = parent.getWidth() - parent.getPaddingRight() - (int)(25*dp);

        int childeCount = parent.getChildCount();
        for (int i=0; i<childeCount; i++){
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)child.getLayoutParams();

            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + mDivider.getIntrinsicHeight();

            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }
}