package com.example.lenah.button;

import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    SQLiteDatabase database;

    private class DownloadTask extends AsyncTask<String, Void ,String>{

        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            try {
                    URL url = new URL(strings[0]);
                    HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                    connection.connect();
                    InputStream inputStream = connection.getInputStream();
                    InputStreamReader reader = new InputStreamReader(inputStream);

                    int data = reader.read();
                    while (data != -1) {
                        result += (char) data;
                        data = reader.read();
                    }
                    connection.disconnect();
                    return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String jsonData) {
            super.onPostExecute(jsonData);
            try {
                JSONObject jsonObjectAllData = new JSONObject(jsonData);
                JSONObject jsonObjectResult = jsonObjectAllData.getJSONObject("result");


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private void clearDatabase() {
            database.execSQL("DELETE FROM articles ");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        database = this.openOrCreateDatabase("currencies", MODE_PRIVATE, null);
        database.execSQL("CREATE TABLE IF NOT EXISTS articles (id INTEGER PRIMARY KEY, currencyName VARCHAR, exchangeRate INTEGER)");

    }


    public void convert(View view) {
        EditText amount = findViewById(R.id.amountEditText);
        String text = amount.getText().toString();
        double euros = Double.valueOf(amount.getText().toString());
        double dollar = Math.round(euros * 1.15 * 100) / 100;
        Toast.makeText(this, euros + "€ is " + dollar + "$", Toast.LENGTH_LONG).show();
    }





}
