package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AudioPlayer extends Fragment {
    private Music player=new Music();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.audio_player, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Button play=(Button) getView().findViewById(R.id.play);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.play(getActivity());
            }
        });
        Button pause=(Button) getView().findViewById(R.id.pause);
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.pause();
            }
        });
        Button stop=(Button) getView().findViewById(R.id.stop);
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.stop();
            }
        });
        Button next=(Button) getView().findViewById(R.id.nextMusic);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.next(getActivity());
            }
        });
        Button previous=(Button) getView().findViewById(R.id.previousMusic);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.previous(getActivity());
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }
}
