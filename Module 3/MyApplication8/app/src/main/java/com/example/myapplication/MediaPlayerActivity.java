package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;

public class MediaPlayerActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.media_player_activity);
        SwitchCompat sw=(SwitchCompat) findViewById(R.id.switch1);
        SharedPreferences s=getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed= s.edit();
        RadioButton r1=(RadioButton)findViewById(R.id.media_player_rb);
        r1.setChecked(true);
        View background=findViewById(R.id.back);
        sw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!sw.isChecked())
                {
                    background.setVisibility(View.INVISIBLE);
                    sw.setTextColor(Color.BLACK);
                    ed.putBoolean("Dark", true);
                    ed.apply();
                }
                else
                {
                    background.setVisibility(View.VISIBLE);
                    sw.setTextColor(Color.WHITE);
                    ed.putBoolean("Dark", false);
                    ed.apply();
                }
            }
        });
        ed.apply();
        if(s.getBoolean("Dark", true)){
            sw.setChecked(false);
            sw.callOnClick();
        }
        else {
            sw.setChecked(true);
            sw.callOnClick();
        }
        Button next=(Button)findViewById(R.id.next_app);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MediaPlayerActivity.this, LampsActivity.class));
            }
        });
        Button previous=(Button)findViewById(R.id.previous_app);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MediaPlayerActivity.this, CellularAutomatonActivity.class));
            }
        });
    }
}


class Music{

    private android.media.MediaPlayer mPlayer;
    private boolean paused=false;
    private int[] tracks=new int[3];
    private int time=0;
    int chosenTrack=0;

    Music(){
        tracks[0]=R.raw.m1;
        tracks[1]=R.raw.m2;
        tracks[2]=R.raw.m3;
    }

    public void stop(){
        if(mPlayer!=null){
            mPlayer.release();
            mPlayer=null;
        }
        paused=false;
        time=0;
    }

    private void start(Context c){
        stop();
        mPlayer= android.media.MediaPlayer.create(c, tracks[chosenTrack]);
        mPlayer.setOnCompletionListener(new android.media.MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(android.media.MediaPlayer mp) {
                stop();
            }
        });
        paused=false;
        mPlayer.start();
    }

    public void play (Context c){
        if(paused)
        {
            mPlayer.start();
            mPlayer.seekTo(time);
            paused=false;
        }
        else {
            start(c);
        }
    }

    public void pause(){
        mPlayer.pause();
        time=mPlayer.getCurrentPosition();
        paused=true;
    }

    public void next(Context c){
        chosenTrack=(chosenTrack+1)%3;
        start(c);
        Log.i("Next","");
    }

    public void previous(Context c){
        if (chosenTrack!=0)
            chosenTrack=chosenTrack-1;
        else
            chosenTrack=2;
        start(c);
        Log.i("Prev","");
    }
}

