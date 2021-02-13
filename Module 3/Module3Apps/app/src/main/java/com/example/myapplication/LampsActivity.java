package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import java.util.Random;

public class LampsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lamps_activity);
        RadioButton r2=(RadioButton)findViewById(R.id.lamps_rb);
        r2.setChecked(true);
        Button next=(Button)findViewById(R.id.next_app);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LampsActivity.this, BlackCatActivity.class));
            }
        });
        Button previous=(Button)findViewById(R.id.previous_app);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LampsActivity.this, MediaPlayerActivity.class));
            }
        });
    }
}


class LampDraw extends SurfaceView implements SurfaceHolder.Callback {
    private final int radius=25, distance=100, length=radius*2+distance/2;
    private DrawThread draw;
    private Canvas canvas;
    private int[][][] centers = new int[10][10][2];
    private int[][] colors = new int[10][10];
    private boolean[][] touched = new boolean[10][10];
    private float x;
    private float y;

    public LampDraw(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                colors[i][j] = Color.RED;
                touched[i][j] = false;
                if (i == 0 && j == 0) {
                    centers[i][j][0] = 75;
                    centers[i][j][1] = 75;
                } else if (i == 0) {
                    centers[i][j][0] = centers[i][j - 1][0] + distance;
                    centers[i][j][1] = 75;
                } else if (j == 0) {
                    centers[i][j][0] = 75;
                    centers[i][j][1] = centers[i - 1][j][1] + distance;
                } else {
                    centers[i][j][0] = centers[i][j - 1][0] + distance;
                    centers[i][j][1] = centers[i - 1][j][1] + distance;
                }

            }
        }
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        draw = new DrawThread(holder);
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
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            x = event.getX();
            y = event.getY();
            if (x>=50&&x<=950&&y>=50&&y<=950){
                int i=(int)(x-50)/length;
                int j=(int)(y-50)/length;
                float xtouch=x-centers[i][j][0], ytouch=y-centers[i][j][1];
                if (xtouch*xtouch+ytouch*ytouch==radius*radius){
                    colors[i][j]=Color.rgb(new Random().nextInt()%256,new Random().nextInt()%256, new Random().nextInt()%256);
                }
            }
        }
        return super.onTouchEvent(event);
    }

    class DrawThread extends Thread {
        private volatile boolean drawing = true;
        private SurfaceHolder surfaceHolder;
        private Paint paint = new Paint();

        public DrawThread(SurfaceHolder surfaceHolder) {
            this.surfaceHolder = surfaceHolder;
        }


        @Override
        public void run() {
            while (drawing) {
                canvas = surfaceHolder.lockCanvas();
                if (canvas != null) {
                    canvas.drawColor(Color.BLUE);
                    for (int i = 0; i < 10; i++) {
                        for (int j = 0; j < 10; j++) {
                            paint.setColor(colors[i][j]);
                            canvas.drawCircle(centers[i][j][0], centers[i][j][1], radius, paint);
                        }
                    }
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

