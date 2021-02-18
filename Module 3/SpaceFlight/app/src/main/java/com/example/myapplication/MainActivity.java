package com.example.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new SpaceFlight(this));
    }
}

class SpaceFlight extends SurfaceView implements SurfaceHolder.Callback {
    private boolean shot = false;
    private int pressed_button, canvas_width, canvas_height;
    private Ship ship;
    private final int asteroid_width=4, asteroid_height=4;
    private final ArrayList<Asteroid> asteroids = new ArrayList<>();
    private final ArrayList<Laser> laser_shots = new ArrayList<>();
    private final ArrayList<Explosion> explosions = new ArrayList<>();
    private final Bitmap[] textures = new Bitmap[64];

    private final Bitmap img = BitmapFactory.decodeResource(getResources(), R.drawable.spaceship),
            laser_img = Bitmap.createScaledBitmap(
                    BitmapFactory.decodeResource(getResources(), R.drawable.laser),
                    BitmapFactory.decodeResource(getResources(), R.drawable.laser).getWidth() / 50,
                    BitmapFactory.decodeResource(getResources(), R.drawable.laser).getHeight() / 50, false),
            asteroid_img = BitmapFactory.decodeResource(getResources(), R.drawable.asteroid),
            explosion_img = Bitmap.createScaledBitmap(
                    BitmapFactory.decodeResource(getResources(), R.drawable.explosion),
                    asteroid_img.getWidth() / asteroid_width,
                    asteroid_img.getHeight() / asteroid_height, false);
    private Canvas canvas;


    public SpaceFlight(Context context) {
        super(context);
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        DrawThread draw = new DrawThread(holder);
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
            if (event.getY() >= canvas_height / 2.0f) {
                if (event.getX() <= canvas_width / 3.0f) {
                    pressed_button = -1;
                } else if (event.getX() <= canvas_width / 3.0f * 2 && event.getX() > canvas_width / 3.0f) {
                    pressed_button = 0;
                } else {
                    pressed_button = 1;
                }
            } else {
                shot = true;
            }
        }
        return super.onTouchEvent(event);
    }

    class DrawThread extends Thread {
        private final Paint paint = new Paint();
        private final boolean running = true;
        private final SurfaceHolder surfaceHolder;
        private float rotation=0;
        private boolean neg=false;
        Matrix matrix=new Matrix();
        private final float a=2.66795f*25, b=10.47173f*25;
        private final Bitmap comet=BitmapFactory.decodeResource(getResources(), R.drawable.comet);
        private float cometX=602.5f+b-comet.getWidth()/3f, cometY=602.5f+comet.getHeight();

        public DrawThread(SurfaceHolder surfaceHolder) {
            this.surfaceHolder = surfaceHolder;
        }

        private void cometMove(){
           float x=cometX-602.5f-b+comet.getWidth()/3f, y;
            if (neg)
                x-=1;
            else
                x+=1;
            y= (float) Math.sqrt(1-x*x/(b*b))*a;
            if (x>=b)
                neg=true;
            else if (x<=-b)
                neg=false;
            if (!neg)
                y=-y;
            if (y<=0)
            {
                if (x<=b/2&&x>=-b/2)
                    rotation=0;
                else if (x>=b/2)
                    rotation=30;
                else if (x<=-b/2)
                    rotation=-30;
            }
            else {
                if (x<=b/2&&x>=-b/2)
                    rotation=180;
                else if (x>=b/2)
                    rotation=150;
                else if (x<=-b/2)
                    rotation=210;
            }
            matrix.setRotate(rotation);
            cometX=x+602.5f+b-comet.getWidth()/3f;
            cometY=y+602.5f+comet.getHeight();
        }

        @Override
        public void run() {
            Bitmap turned_comet=Bitmap.createBitmap(comet, 0, 0, comet.getWidth(), comet.getHeight(), matrix, false);
            long time = System.currentTimeMillis(), current_time;
            for (int i = 0; i < asteroid_width; i++) {
                for (int j = 0; j < asteroid_height; j++) {
                    textures[i * asteroid_width + j] =Bitmap.createScaledBitmap(Bitmap.createBitmap(asteroid_img, asteroid_img.getWidth() / asteroid_width * i, asteroid_img.getHeight() / asteroid_height * j, asteroid_img.getWidth() / asteroid_width, asteroid_img.getHeight() / asteroid_height), 128, 128, false) ;
                }
            }
            ship = new Ship(img, SpaceFlight.this);
            while (running) {
                canvas = surfaceHolder.lockCanvas();
                if (canvas != null) {
                    if (/*ship.dead*/false) {
                        canvas.drawColor(Color.RED);
                        paint.setColor(Color.BLACK);
                        paint.setTextSize(50);
                        canvas.drawText("YOU DIED", canvas_width / 2.0f, canvas_height / 2.0f, paint);
                    } else {
                        current_time = System.currentTimeMillis();
                        canvas_width = canvas.getWidth();
                        canvas_height = canvas.getHeight();
                        canvas.drawColor(Color.BLACK);
                        paint.setColor(Color.RED);
                        canvas.drawOval(600, 600, 600+b*2, 600+a*2, paint);
                        paint.setColor(Color.BLACK);
                        canvas.drawOval(600+5, 600+5, 600+b*2-5, 600+a*2-5, paint);
                        canvas.drawBitmap(Bitmap.createBitmap(comet, 0, 0, comet.getWidth(), comet.getHeight(), matrix, false), cometX, cometY, paint);
                        cometMove();
                        ship.move();
                        ship.check_walls();
                        if (current_time - time >= 2000) {
                            new Asteroid(textures[(int) (Math.random() * asteroid_width*asteroid_height - 1)], ship);
                            time = current_time;
                        }
                        if (laser_shots.size() != 0) {
                            for (int i = 0; i < laser_shots.size(); i++) {
                                if (!laser_shots.get(i).check_collision()) {
                                    canvas.drawBitmap(laser_shots.get(i).img, laser_shots.get(i).current_x, laser_shots.get(i).current_y, paint);
                                    laser_shots.get(i).go();
                                }
                            }
                        }
                        if (asteroids.size() != 0) {
                            for (int i = 0; i < asteroids.size(); i++) {
                                canvas.drawBitmap(asteroids.get(i).img, asteroids.get(i).current_x, asteroids.get(i).current_y, paint);
                                asteroids.get(i).move();
                            }
                        }
                        if (explosions.size() != 0) {
                            for (int i = 0; i < explosions.size(); i++) {
                                canvas.drawBitmap(explosions.get(i).img, explosions.get(i).current_x, explosions.get(i).current_y, paint);
                                explosions.get(i).current_time = System.currentTimeMillis();
                                if (explosions.get(i).current_time - explosions.get(i).time >= 500) {
                                    explosions.remove(explosions.get(i));
                                }
                            }
                        }
                        if (shot) {
                            ship.shoot();
                        }
                        canvas.drawBitmap(ship.current_frame, ship.current_x, ship.current_y, paint);
                        if (asteroids.size() != 0) {
                            for (int i = 0; i < asteroids.size(); i++) {
                                asteroids.get(i).check_ship();
                            }
                        }

                    }
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class Ship {
        private boolean dead = false;
        private final Bitmap img;
        private Bitmap current_frame;
        private int current_state = 0;
        private float current_x;
        private final float current_y;

        public Ship(Bitmap img, SpaceFlight flight) {
            this.img = img;
            ;
            current_x = flight.getWidth() / 2.0f - img.getWidth() / 6.0f;
            current_y = flight.getHeight() - img.getHeight() / 8.0f;
            current_frame = Bitmap.createBitmap(img, img.getWidth() / 3, img.getHeight() / 8 * current_state, img.getWidth() / 3, img.getHeight() / 8);
        }

        private void move() {
            switch (pressed_button) {
                case 0: {
                    current_state = 0;
                    current_frame = Bitmap.createBitmap(img, img.getWidth() / 3, img.getHeight() / 8 * current_state, img.getWidth() / 3, img.getHeight() / 8);
                    return;
                }
                case 1: {
                    current_state = 1;
                    current_x += 10;
                    current_frame = Bitmap.createBitmap(img, img.getWidth() / 3, img.getHeight() / 8 * current_state, img.getWidth() / 3, img.getHeight() / 8);
                    return;
                }
                case (-1): {
                    current_state = 4;
                    current_x -= 10;
                    current_frame = Bitmap.createBitmap(img, img.getWidth() / 3, img.getHeight() / 8 * current_state, img.getWidth() / 3, img.getHeight() / 8);
                }
            }
        }

        private void shoot() {
            if (shot) {
                laser_shots.add(new Laser(laser_img, ship));
                laser_shots.get(laser_shots.size() - 1).going = true;
                shot = false;
            }
        }

        private void check_walls() {
            if (current_x <= 5) {
                current_x = canvas_width - current_frame.getWidth() - 5;
            } else if (current_x >= canvas_width - current_frame.getWidth() - 5) {
                current_x = 5;
            }
        }
    }

    private class Asteroid {
        Ship ship;
        float current_x, current_y;
        Bitmap img;


         public Asteroid(Bitmap img, Ship ship) {
            this.img = img;
            this.ship = ship;
            current_x = (float) (Math.random() * canvas.getWidth());
            current_y = 0;
            asteroids.add(this);
        }

        private void move() {
            if (check_walls()) {
                current_y += 10;
            }
        }

        private void explode() {
            explosions.add(new Explosion(Bitmap.createScaledBitmap(explosion_img, 64 ,64, false)));
            explosions.get(explosions.size() - 1).current_x = current_x;
            explosions.get(explosions.size() - 1).current_y = current_y;
            asteroids.remove(this);
        }

        private boolean check_walls() {
            if (current_y < canvas.getHeight() - img.getHeight()) {
                return true;
            }
            asteroids.remove(this);
            return false;
        }

        private void check_ship() {
            if (Math.abs(current_x + img.getWidth() / 2.0f - ship.current_frame.getWidth() / 2.0f - ship.current_x) <= img.getWidth() / 2.0f + ship.current_frame.getWidth() / 2.0f && current_y + img.getHeight() >= ship.current_y) {
                ship.dead = true;
            }
        }
    }

    private class Laser {
        private boolean going = false;
        private float current_x, current_y;
        private final Bitmap img;

        public Laser(Bitmap img, Ship ship) {
            this.img = img;
            switch (ship.current_state) {
                case 1: {
                    current_x = ship.current_x + ship.img.getWidth() / 3.0f - img.getWidth() / 2.0f;
                    current_y = ship.current_y - img.getHeight();
                    return;
                }
                case 0: {
                    current_x = ship.current_x + ship.img.getWidth() / 6.0f - img.getWidth() / 2.0f;
                    current_y = ship.current_y - img.getHeight();
                    return;
                }
                case 4: {
                    current_x = ship.current_x + img.getWidth() / 2.0f;
                    current_y = ship.current_y - img.getHeight();
                }
            }
        }

        private void go() {
            if (going) {
                current_y -= 10;
            }
            if (check_collision()) {
                going = false;
            }
        }

        private boolean check_collision() {
            if (current_y <= 0) {
                laser_shots.remove(this);
                return true;
            }
            for (int i = 0; i < asteroids.size(); i++) {
                if (current_y <= asteroids.get(i).current_y + asteroids.get(i).img.getHeight() && Math.abs(asteroids.get(i).current_x + asteroids.get(i).img.getWidth() / 2.0f - current_x + img.getWidth() / 2.0f) <= img.getWidth() / 2.0f + asteroids.get(i).img.getWidth() / 2.0f) {
                    asteroids.get(i).explode();
                    laser_shots.remove(this);
                    return true;
                }
            }
            return false;
        }
    }

    private static class Explosion {
        long time, current_time;
        float current_x, current_y;
        Bitmap img;

        public Explosion(Bitmap img) {
            this.img = img;
            time = System.currentTimeMillis();
        }
    }
}