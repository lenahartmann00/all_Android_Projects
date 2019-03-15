package com.example.weatherapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    String data;
    String celsius;
    String fahrenheit;
    String feelsLikeCelsius;
    String feelsLikeFahrenheit;
    String localtime;
    String weatherUrl;

    DownloadTask dataTask;
    TextView celsiusTextView;
    TextView fahrenheitTextView;
    TextView feelsCelsiusTextView;
    TextView feelsFahrenheitTextView;
    TextView localtimeTextView;



    class DownloadTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                connection.connect();
                InputStream in = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                String result = "";
                while(data > -1){
                    result += (char) data;
                    data = reader.read();
                }
                try {
                    JSONObject fullData = new JSONObject(result);
                    localtime = fullData.getJSONObject("location").getString("localtime");
                    celsius = fullData.getJSONObject("current").getString("temp_c");
                    fahrenheit = fullData.getJSONObject("current").getString("temp_f");
                    feelsLikeCelsius = fullData.getJSONObject("current").getString("feelslike_c");
                    feelsLikeFahrenheit = fullData.getJSONObject("current").getString("feelslike_f");
                    weatherUrl = fullData.getJSONObject("current").getJSONObject("condition").getString("icon");
                    Log.i("Full Data: ", "\n"+localtime+"\n"+celsius+"\n"+fahrenheit+"\n"+feelsLikeCelsius+"\n"+feelsLikeFahrenheit+"\n"+weatherUrl);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                celsiusTextView.setVisibility(View.INVISIBLE);
                fahrenheitTextView.setVisibility(View.INVISIBLE);
                feelsCelsiusTextView.setVisibility(View.INVISIBLE);
                feelsFahrenheitTextView.setVisibility(View.INVISIBLE);
                localtimeTextView.setVisibility(View.INVISIBLE);
                return "Failed!";
            } catch (IOException e) {
                e.printStackTrace();
                celsiusTextView.setVisibility(View.INVISIBLE);
                fahrenheitTextView.setVisibility(View.INVISIBLE);
                feelsCelsiusTextView.setVisibility(View.INVISIBLE);
                feelsFahrenheitTextView.setVisibility(View.INVISIBLE);
                localtimeTextView.setVisibility(View.INVISIBLE);
                return "Failed!";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }
    }

    public void getData(View view){
        EditText city = findViewById(R.id.editText);
        Log.i("City", ""+city.getText());
        dataTask = new DownloadTask();
        try {
            data = dataTask.execute("https://api.apixu.com/v1/current.json?key=eb211253429d42d0974122505192501&q="+city.getText()).get();
            celsiusTextView.setText(String.format("%s°C", celsius));
            fahrenheitTextView.setText(String.format("%s°F", fahrenheit));
            feelsCelsiusTextView.setText(String.format("Gefühlt: %s°C", feelsLikeCelsius));
            feelsFahrenheitTextView.setText(String.format("Gefühlt: %s°C", feelsLikeFahrenheit));
            String[] splitLocaltime = localtime.split(" ");
            localtimeTextView.setText(splitLocaltime[0]+"\n"+splitLocaltime[1]);
            celsiusTextView.setVisibility(View.VISIBLE);
            fahrenheitTextView.setVisibility(View.VISIBLE);
            feelsCelsiusTextView.setVisibility(View.VISIBLE);
            feelsFahrenheitTextView.setVisibility(View.VISIBLE);
            localtimeTextView.setVisibility(View.VISIBLE);
            Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
        } catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
            celsiusTextView.setVisibility(View.INVISIBLE);
            fahrenheitTextView.setVisibility(View.INVISIBLE);
            feelsCelsiusTextView.setVisibility(View.INVISIBLE);
            feelsFahrenheitTextView.setVisibility(View.INVISIBLE);
            localtimeTextView.setVisibility(View.INVISIBLE);
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        celsiusTextView = findViewById(R.id.celsiusTextView);
        fahrenheitTextView = findViewById(R.id.farenheitTextView);
        feelsCelsiusTextView = findViewById(R.id.feelsLikeCelsiusTextView);
        feelsFahrenheitTextView = findViewById(R.id.feelsLikeFarenheitTextView);
        localtimeTextView = findViewById(R.id.localTimeTextView);
        celsiusTextView.setVisibility(View.INVISIBLE);
        fahrenheitTextView.setVisibility(View.INVISIBLE);
        feelsCelsiusTextView.setVisibility(View.INVISIBLE);
        feelsFahrenheitTextView.setVisibility(View.INVISIBLE);
        localtimeTextView.setVisibility(View.INVISIBLE);
    }
}

