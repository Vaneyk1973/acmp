package com.example.myapplication;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;

import java.util.jar.Attributes;

public class CellularAutomatonActivity extends AppCompatActivity {
    public static int ruleN=54;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cellular_automaton_activity);
        RadioButton r4=(RadioButton)findViewById(R.id.cellular_rb);
        r4.setChecked(true);
        Button next=(Button)findViewById(R.id.next_app);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CellularAutomatonActivity.this, MediaPlayerActivity.class));
            }
        });
        Button previous=(Button)findViewById(R.id.previous_app);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CellularAutomatonActivity.this, BlackCatActivity.class));
            }
        });
        EditText rule=(EditText)findViewById(R.id.ruleN);
        Button confirm=(Button)findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ruleN=Integer.parseInt(rule.getText().toString());
            }
        });
    }
}

class CellularAutomatonSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    public CellularAutomatonSurfaceView(Context c, AttributeSet attrs) {
        super(c, attrs);
        getHolder().addCallback(this);
    }


    @Override
    public void surfaceCreated( SurfaceHolder holder) {
        SurfaceThread thread = new SurfaceThread(holder);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed( SurfaceHolder holder) {

    }


    class SurfaceThread extends Thread {
        private final SurfaceHolder surfaceHolder;
        private final Paint paint= new Paint();
        private final boolean running = true;
        private int previous_ruleN=54;

        public SurfaceThread(SurfaceHolder surfaceHolder) {
            this.surfaceHolder = surfaceHolder;
        }

        public void drawCells(Bitmap bitmap, int ruleN){
            bitmap.eraseColor(Color.WHITE);
            String rule =Integer.toBinaryString(ruleN);
            int a=rule.length();
            for (int i=0;i<8-a;i++){
                rule="0"+rule;
            }
            bitmap.setPixel(bitmap.getWidth()/2, 1, Color.BLACK);
            for (int j=1;j<bitmap.getHeight()-1;j++){
                for (int i=1;i<bitmap.getWidth()-1;i++){
                    if(bitmap.getPixel(i-1, j)==Color.BLACK&&bitmap.getPixel(i, j)==Color.BLACK&&bitmap.getPixel(i+1, j)==Color.BLACK){
                        if(rule.charAt(0)=='1'){
                            bitmap.setPixel(i, j+1, Color.BLACK);
                        }
                    } else if(bitmap.getPixel(i-1, j)==Color.BLACK&&bitmap.getPixel(i, j)==Color.BLACK&&bitmap.getPixel(i+1, j)==Color.WHITE){
                        if(rule.charAt(1)=='1'){
                            bitmap.setPixel(i, j+1, Color.BLACK);
                        }
                    }else if(bitmap.getPixel(i-1, j)==Color.BLACK&&bitmap.getPixel(i, j)==Color.WHITE&&bitmap.getPixel(i+1, j)==Color.BLACK){
                        if(rule.charAt(2)=='1'){
                            bitmap.setPixel(i, j+1, Color.BLACK);
                        }
                    }else if(bitmap.getPixel(i-1, j)==Color.BLACK&&bitmap.getPixel(i, j)==Color.WHITE&&bitmap.getPixel(i+1, j)==Color.WHITE){
                        if(rule.charAt(3)=='1'){
                            bitmap.setPixel(i, j+1, Color.BLACK);
                        }
                    }else if(bitmap.getPixel(i-1, j)==Color.WHITE&&bitmap.getPixel(i, j)==Color.BLACK&&bitmap.getPixel(i+1, j)==Color.BLACK){
                        if(rule.charAt(4)=='1'){
                            bitmap.setPixel(i, j+1, Color.BLACK);
                        }
                    }else if(bitmap.getPixel(i-1, j)==Color.WHITE&&bitmap.getPixel(i, j)==Color.BLACK&&bitmap.getPixel(i+1, j)==Color.WHITE){
                        if(rule.charAt(5)=='1'){
                            bitmap.setPixel(i, j+1, Color.BLACK);
                        }
                    }else if(bitmap.getPixel(i-1, j)==Color.WHITE&&bitmap.getPixel(i, j)==Color.WHITE&&bitmap.getPixel(i+1, j)==Color.BLACK){
                        if(rule.charAt(6)=='1'){
                            bitmap.setPixel(i, j+1, Color.BLACK);
                        }
                    }else if(bitmap.getPixel(i-1, j)==Color.WHITE&&bitmap.getPixel(i, j)==Color.WHITE&&bitmap.getPixel(i+1, j)==Color.WHITE){
                        if(rule.charAt(7)=='1'){
                            bitmap.setPixel(i, j+1, Color.BLACK);
                        }
                    }
                }
            }
        }
        @Override
        public void run() {
            Canvas c;
            Bitmap bitmap= Bitmap.createBitmap(100,100, Bitmap.Config.ARGB_8888);
            drawCells(bitmap, 54);
            Bitmap b=Bitmap.createScaledBitmap(bitmap, 900, 900, false);
            while (running) {
                c = surfaceHolder.lockCanvas();
                c.drawColor(Color.WHITE);
                if (previous_ruleN!=CellularAutomatonActivity.ruleN){
                    previous_ruleN=CellularAutomatonActivity.ruleN;
                    drawCells(bitmap, previous_ruleN);
                    b=Bitmap.createScaledBitmap(bitmap, 900, 900, false);
                }
                c.drawBitmap(b, 50, 50, paint);
                surfaceHolder.unlockCanvasAndPost(c);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

