package com.example.myapplication;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.util.Printer;
import android.widget.VideoView;

public  class Music{

    private MediaPlayer mPlayer;
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
        mPlayer=MediaPlayer.create(c, tracks[chosenTrack]);
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
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