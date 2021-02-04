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
import android.widget.Toast;

public class BlackCatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.black_cat_activity);
        RadioButton r3=(RadioButton)findViewById(R.id.cat_rb);
        r3.setChecked(true);
        Button next=(Button)findViewById(R.id.next_app);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BlackCatActivity.this, CellularAutomatonActivity.class));
            }
        });
        Button previous=(Button)findViewById(R.id.previous_app);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BlackCatActivity.this, LampsActivity.class));
            }
        });
    }
}

class CatDraw extends SurfaceView implements SurfaceHolder.Callback{
    private Draw draw;
    private Paint paint=new Paint();
    private boolean hovered =false;
    private Canvas canvas;
    private SurfaceHolder h;
    private float x=(float)Math.random()*700, y=(float)Math.random()*700, xhovered, yhovered;

    public CatDraw(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        h=holder;
        draw=new Draw(h);
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
        if(event.getAction()==event.ACTION_DOWN){
            xhovered=event.getX();
            yhovered=event.getY();
            float xtouch=x-xhovered, ytouch=y-yhovered;
            if (xtouch*xtouch+ytouch*ytouch<=50*50){
                hovered =true;
                Toast.makeText(getContext(), "Congratulations, you've found a cat!", Toast.LENGTH_LONG).show();
            }
        }
        else if (event.getAction()==event.ACTION_MOVE){
            xhovered=event.getX();
            yhovered=event.getY();
            float xtouch=x-xhovered, ytouch=y-yhovered;
            if (xtouch*xtouch+ytouch*ytouch<=50*50){
                hovered =true;
                Toast.makeText(getContext(), "Congratulations, you've found a cat!", Toast.LENGTH_LONG).show();
            }
        }
        return super.onTouchEvent(event);
    }

    class Draw extends Thread{
        private volatile boolean drawing=true;
        private SurfaceHolder surfaceHolder;

        public Draw(SurfaceHolder surfaceHolder){
            this.surfaceHolder=surfaceHolder;
        }

        @Override
        public void run() {
            while (drawing){
                canvas=surfaceHolder.lockCanvas();
                if (canvas!=null){
                    canvas.drawColor(Color.BLACK);
                    paint.setColor(Color.YELLOW);
                    paint.setAlpha(127);
                    canvas.drawCircle(xhovered, yhovered, 100, paint);
                    paint.setColor(Color.argb(255, 20, 20, 20));
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



