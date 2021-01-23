package com.example.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import androidx.annotation.NonNull;

public class CatDraw extends SurfaceView implements SurfaceHolder.Callback{
    private Draw draw;
    private Canvas canvas;
    private float x=(float)Math.random()*700, y=(float)Math.random()*700;

    public CatDraw(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        draw=new Draw(holder);
        draw.start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float xtouch=x-event.getX(), ytouch=y-event.getY();
        if (xtouch*xtouch+ytouch*ytouch<=50*50){
            draw.interrupt();
            Toast.makeText(getContext(), "Congratulations, you've found a cat!", Toast.LENGTH_LONG).show();
        }
        return super.onTouchEvent(event);
    }

    class Draw extends Thread{
        private volatile boolean drawing=true;
        private SurfaceHolder surfaceHolder;
        private Paint paint=new Paint();


        public Draw(SurfaceHolder surfaceHolder){
            this.surfaceHolder=surfaceHolder;
        }

        @Override
        public void run() {
            while (drawing){
                canvas=surfaceHolder.lockCanvas();
                if (canvas!=null){
                    canvas.drawColor(Color.BLACK);
                    paint.setColor(Color.argb(255, 40, 40, 40));
                    canvas.drawCircle(x, y, 50, paint);
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
            try{
                Thread.sleep(100);
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}
