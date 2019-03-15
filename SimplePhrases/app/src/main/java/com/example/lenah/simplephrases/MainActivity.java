package com.example.lenah.simplephrases;

import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    MediaPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void play(View view){
        Uri music = Uri.parse("android.resource://"+getPackageName()+"/raw/"+view.getTag());
        MediaPlayer player = (MediaPlayer) MediaPlayer.create(this, music);
        player.start();
    }
}
