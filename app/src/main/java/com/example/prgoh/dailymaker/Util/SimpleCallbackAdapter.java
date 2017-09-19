package com.example.prgoh.dailymaker.Util;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import android.util.Log;
import android.widget.Toast;

import com.example.prgoh.dailymaker.Adapter.AlarmAdapter;
import com.example.prgoh.dailymaker.DB.AlarmDBManager;
import com.example.prgoh.dailymaker.Item.CustomToast;

import org.json.JSONException;

/**
 * Created by prgoh on 2017-07-23.
 */

public class SimpleCallbackAdapter extends ItemTouchHelper.Callback{

    private Context context;
    private AlarmAdapter adapter;

    private ItemTouchHelperListener listener;

    public SimpleCallbackAdapter(ItemTouchHelperListener listener, Context context){
        this.listener = listener;
        this.context = context;
    }

    // 각 view에서 어떤 user action이 가능한지 정의
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {

        final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        final int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;

        return makeMovementFlags(dragFlags, swipeFlags);
    }


    // user가 item을 drag할 때, ItemTouchHelper가 onMove()를 호출
    @Override
    public boolean onMove(RecyclerView recyclerView,
                          RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {
        if (source.getItemViewType() != target.getItemViewType()) {
            return false;
        }

        return listener.onItemMove(source.getAdapterPosition(), target.getAdapterPosition());
    }

    // user가 item을 swipe할 때, ItemTouchHelper가 onSwiped()를 호출
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        Log.i("TAG", "onSwiped");
        CustomToast.customToast(context, "알람 삭제됨");

        try {
            listener.onItemRemove(viewHolder.getAdapterPosition());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE){
            final float alpha = 1.0f - Math.abs(dX)/viewHolder.itemView.getWidth();
            viewHolder.itemView.setAlpha(alpha);
            viewHolder.itemView.setTranslationX(dX);
        }
    }

//    @Override
//    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
//        // We only want the active item to change
//        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
//            if (viewHolder instanceof ItemTouchHelperViewHolder) {
//                // Let the view holder know that this item is being moved or dragged
//                ItemTouchHelperViewHolder itemViewHolder = (ItemTouchHelperViewHolder) viewHolder;
//                itemViewHolder.onItemSelected();
//            }
//        }
//
//        super.onSelectedChanged(viewHolder, actionState);
//    }
}
