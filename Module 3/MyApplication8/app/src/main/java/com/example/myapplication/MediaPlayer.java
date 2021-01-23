package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

public class MediaPlayer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.media_player);
        SwitchCompat sw=(SwitchCompat) findViewById(R.id.switch1);
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
                }
                else
                {
                    background.setVisibility(View.VISIBLE);
                    sw.setTextColor(Color.WHITE);
                }
            }
        });
        sw.callOnClick();
        sw.callOnClick();
        Button next=(Button)findViewById(R.id.next_app);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MediaPlayer.this, Lamps.class));
            }
        });
        Button previous=(Button)findViewById(R.id.previous_app);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MediaPlayer.this, BlackCat.class));
            }
        });
    }
}

