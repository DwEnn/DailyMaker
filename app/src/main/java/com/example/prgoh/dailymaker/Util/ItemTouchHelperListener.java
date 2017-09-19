package com.example.prgoh.dailymaker.Util;

import org.json.JSONException;

/**
 * Created by prgoh on 2017-07-23.
 */

public interface ItemTouchHelperListener {

    void onItemRemove(int position) throws JSONException;
    boolean onItemMove(int fromPosition, int toPosition);

}
