package com.example.timer;

import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    int max = 600; //10 minutes
    int defaultProgress = 30; //30 seconds

    CountDownTimer timer;

    SeekBar timeCalculator;
    MediaPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //SeekBar
        timeCalculator = findViewById(R.id.seekBar);
        player = MediaPlayer.create(this, R.raw.airhorn);


        timeCalculator.setMax(600);
        timeCalculator.setProgress(30);
        setClock(timeCalculator.getProgress());
        timeCalculator.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setClock(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }

    public void startTimer(View view){
        //Timer
        timer = new CountDownTimer(timeCalculator.getProgress()*1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                setClock((int) millisUntilFinished/1000);
            }

            @Override
            public void onFinish() {
                player.start();
            }
        }.start();
    }

    public void stopTimer(View view){
        timer.cancel();
        timeCalculator.setProgress(defaultProgress);
        setClock(defaultProgress);
    }


    private void setClock(int progress){
        final TextView clock = findViewById(R.id.textView);
        int seconds = progress%60;
        int minutes = (progress-seconds)/60;
        DecimalFormat df = new DecimalFormat("00");
        clock.setText(df.format(minutes)+":"+df.format(seconds));
    }
}
