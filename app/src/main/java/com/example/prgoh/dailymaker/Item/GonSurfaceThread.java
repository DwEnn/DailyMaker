package com.example.prgoh.dailymaker.Item;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.view.SurfaceHolder;

import com.example.prgoh.dailymaker.R;

import java.util.Random;

/**
 * Created by prgoh on 2017-07-31.
 */

public class GonSurfaceThread extends Thread {
    private SurfaceHolder surfaceHolder;

    private boolean myThreadRun = false;

    int cx, cy, offx, offy;
    private Context context;

    private Random random;
    private Bitmap bitmap;

    public GonSurfaceThread(SurfaceHolder surfaceHolder, GonSurfaceView surfaceView,
                            Context context, int off){
        this.surfaceHolder = surfaceHolder;
        this.context = context;
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.boss_alarm_icon);
        bitmap = Bitmap.createScaledBitmap(bitmap, 300, 300, true);

        random = new Random();

        cx = -1;
        cy = -1;

//        offx = Math.abs(random.nextInt(200));
//        offy = Math.abs(random.nextInt(200));

        offx = off;
        offy = -off;
    }

    public void setMyThreadRun(boolean myThreadRun) {
        this.myThreadRun = myThreadRun;
    }

    @Override
    public void run() {
        while (myThreadRun){
            Canvas c = null;
            try {
                c = surfaceHolder.lockCanvas(null);

                synchronized (surfaceHolder) {

                    c.drawColor(0, PorterDuff.Mode.CLEAR);
                    c.drawBitmap(bitmap, cx, cy, null);

                    if (cx < 0 && cy < 0) {
                        cx = c.getWidth() / 2;
                        cy = c.getHeight() / 2;
                    } else {
                        cx += offx;
                        cy += offy;
                        if ((cx > c.getWidth() - bitmap.getWidth()) || (cx < 0)) {
                            offx = offx * -1;
                        }
                        if ((cy > c.getHeight() - bitmap.getHeight()) || (cy < 0)) {
                            offy = offy * -1;
                        }
                    }
                }

                sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch(NullPointerException e){
                e.printStackTrace();
            } finally {
                if (c != null)
                    surfaceHolder.unlockCanvasAndPost(c);
            }
        }
    }

    public int[] getPosition(){
        return new int[]{cx, cy};
    }
}
