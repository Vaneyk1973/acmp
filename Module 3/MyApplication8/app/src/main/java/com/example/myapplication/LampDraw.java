package com.example.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

public class LampDraw extends SurfaceView implements SurfaceHolder.Callback {
    private DrawThread draw;
    private Canvas canvas;
    private int[][][] centers=new int[10][10][2];
    private int[][] colors=new int[10][10];
    private boolean[][] touched=new boolean[10][10];
    private float x;
    private float y;

    public LampDraw(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
        for (int i=0;i<10;i++){
            for (int j=0;j<10;j++)
            {
                colors[i][j]=Color.RED;
                touched[i][j]=false;
                if (i==0&&j==0){
                    centers[i][j][0]=75;
                    centers[i][j][1]=75;
                }
                else if (i==0)
                {
                    centers[i][j][0]=centers[i][j-1][0]+100;
                    centers[i][j][1]=75;
                }
                else if(j==0)
                {
                    centers[i][j][0]=75;
                    centers[i][j][1]=centers[i-1][j][1]+100;
                }
                else {
                    centers[i][j][0]=centers[i][j-1][0]+100;
                    centers[i][j][1]=centers[i-1][j][1]+100;
                }

            }
        }
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        draw=new DrawThread(holder);
        draw.start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        x=event.getX();
        y=event.getY();
        float xtouch;
        float ytouch;
        for (int i=0;i<10;i++){
            for (int j=0;j<10;j++){
                xtouch=x-centers[i][j][0];
                ytouch=y-centers[i][j][1];
                if (xtouch*xtouch+ytouch*ytouch<=25*25){
                    if (!touched[i][j])
                    {
                        colors[i][j]=Color.YELLOW;
                        touched[i][j]=true;
                    }
                    else {
                        colors[i][j]=Color.RED;
                        touched[i][j]=false;
                    }
                }
            }
        }
        return super.onTouchEvent(event);
    }

    class DrawThread extends Thread{
        private volatile boolean drawing=true;
        private SurfaceHolder surfaceHolder;
        private Paint paint= new Paint();

        public DrawThread(SurfaceHolder surfaceHolder){
            this.surfaceHolder=surfaceHolder;
        }


        @Override
        public void run(){
            while (drawing){
                canvas=surfaceHolder.lockCanvas();
                if(canvas!=null)
                {
                    canvas.drawColor(Color.BLUE);
                    for (int i=0;i<10;i++){
                        for (int j=0;j<10;j++){
                            paint.setColor(colors[i][j]);
                            canvas.drawCircle(centers[i][j][0], centers[i][j][1], 25, paint);
                        }
                    }
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
                try {
                    Thread.sleep(100);
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
    }
}




