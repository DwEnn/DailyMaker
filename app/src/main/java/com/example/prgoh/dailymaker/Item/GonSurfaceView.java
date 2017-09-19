package com.example.prgoh.dailymaker.Item;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by prgoh on 2017-07-31.
 */

public class GonSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

        private GonSurfaceThread thread;
        private int count = 0;


        public GonSurfaceView(Context context, int off) {
            super(context);
            init(off);
        }

        public GonSurfaceView(Context context, AttributeSet attrs, int off){
            super(context, attrs);
            init(off);
        }

        public GonSurfaceView(Context context, AttributeSet attrs, int defStyle, int off){
            super(context, attrs, defStyle);
            init(off);
        }

        @Override
        public void surfaceCreated(SurfaceHolder surfaceHolder) {
            thread.setMyThreadRun(true);
            thread.start();
        }

        @Override
        public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
            boolean retry = true;
            thread.setMyThreadRun(false);
            while (retry){
                try {
                    thread.join();
                    retry = false;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            int x = (int) event.getX();
            int y = (int) event.getY();

            if (event.getAction() == MotionEvent.ACTION_DOWN){
                if (checkInImg(x, y)){
//                return false;
                }
            }
//        isClicked = false;
            return false;
        }

        private void init(int off){
            getHolder().addCallback(this);
            thread = new GonSurfaceThread(getHolder(), this, getContext(), off);
            setFocusable(true);
        }

        private boolean checkInImg(int x, int y){
//        int inWidth = 30;
//        int inHeight = 20;

            int cx = thread.getPosition()[0];
            int cy = thread.getPosition()[1];

            if ((x - 300) < cx && cx < (x + 300))
                if ((y - 300) < cy && cy < (y + 300)) {
                    count ++;
                    return true;
                }
            return false;
        }

        public int getIsClicked(){
            return count;
        }
}
