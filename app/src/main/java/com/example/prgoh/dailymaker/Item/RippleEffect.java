package com.example.prgoh.dailymaker.Item;

import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.RippleDrawable;

/**
 * Created by prgoh on 2017-08-02.
 */

public class RippleEffect {

    public static RippleDrawable getPressedColorRippleDrawable(int normalColor, int pressedColor)
    {
        return new RippleDrawable(getPressedColorSelector(normalColor, pressedColor), getColorDrawableFromColor(normalColor), null);
    }

    public static ColorStateList getPressedColorSelector(int normalColor, int pressedColor)
    {
        return new ColorStateList(
                new int[][]
                        {
                                new int[]{android.R.attr.state_pressed},
                                new int[]{android.R.attr.state_focused},
                                new int[]{android.R.attr.state_activated},
                                new int[]{}
                        },
                new int[]
                        {
                                pressedColor,
                                pressedColor,
                                pressedColor,
                                normalColor
                        }
        );
    }

    public static ColorDrawable getColorDrawableFromColor(int color)
    {
        return new ColorDrawable(color);
    }
}
