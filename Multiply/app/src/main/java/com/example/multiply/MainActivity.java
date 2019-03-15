package com.example.multiply;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SeekBar numberRegulator = findViewById(R.id.seekBar);
        final TextView currentNumber = findViewById(R.id.currentNumber);
        updateListView(numberRegulator.getProgress());


        numberRegulator.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                currentNumber.setText("" + progress);
                updateListView(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void updateListView(int progressSeekbar) {
        ArrayList<Integer> array = new ArrayList<Integer>();
        ListView list = findViewById(R.id.listView);
        for (int i = 1; i <= 100; i++) {
            array.add(progressSeekbar * i);
        }
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, array);
        list.setAdapter(adapter);
    }
}
