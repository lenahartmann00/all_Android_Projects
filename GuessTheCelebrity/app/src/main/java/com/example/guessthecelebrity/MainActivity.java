package com.example.guessthecelebrity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    //Count 0: names count 1: imageURLS
    String[][] celebrities;
    int[] lastTenCelebrities = new int[10];
    int positionForNewTenCelebrity;
    Button[] answers;
    int correctAnswer;
    int currentCelebrity;

    public class DownloadImage extends  AsyncTask<String, Void, Bitmap>{

        @Override
        protected Bitmap doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection connector = (HttpURLConnection) url.openConnection();
                connector.connect();
                InputStream in = connector.getInputStream();
                Bitmap image = BitmapFactory.decodeStream(in);
                return image;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            } catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }
    }

    public class DownloadWebsite extends AsyncTask<String, Void, String[][]>{

        @Override
        protected String[][] doInBackground(String... strings) {
            try {
                //Gets HTTP-page code
                String websiteContent = "";
                String[][] array = new String[66][2];
                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream in = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while (data!=-1){
                    char current = (char) data;
                    websiteContent += current;
                    data = reader.read();
                }
                //Cutting page code
                websiteContent.substring(websiteContent.indexOf("<a href=\"/kandisar/a_till_o\" class=\"link\">Kändisar A-Ö</a>"),websiteContent.indexOf("<div class=\"col-xs-12 col-sm-6 col-md-4\">"));
                //Saves celebrity-names and -imageUrls in Matrix
                Pattern namePattern = Pattern.compile("alt=\"(.*?)\"");
                Pattern imagePattern = Pattern.compile("<img src=\"(.*?)\"");
                Matcher matcherImage = imagePattern.matcher(websiteContent);
                Matcher matcherName = namePattern.matcher(websiteContent);
                int i = 0;
                while(matcherImage.find()&&matcherName.find()){
                    array[i][0] = matcherName.group(1);
                    array[i][1] = matcherImage.group(1);
                    i++;
                }
                return array;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public void checkAnswer(View view){

        if(Integer.parseInt(String.valueOf(view.getTag()))==correctAnswer){
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "Wrong. It is "+celebrities[currentCelebrity][0], Toast.LENGTH_SHORT).show();
        }
        nextCelebrity();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        answers = new Button[4];
        answers[0] = findViewById(R.id.button1);
        answers[1] = findViewById(R.id.button2);
        answers[2] = findViewById(R.id.button3);
        answers[3] = findViewById(R.id.button4);
        for(int i = 0; i<4; i++){
            answers[i].setVisibility(View.INVISIBLE);
        }
        positionForNewTenCelebrity = 0;
    }

    private void nextCelebrity(){
       Random random = new Random();
        currentCelebrity = random.nextInt(66);
       while(wasOneOfTheLastTenCelebrities()){
           currentCelebrity = random.nextInt(66);
       }
       addToTheLastTenCelebrities();
       correctAnswer = random.nextInt(4);
       answers[correctAnswer].setText(celebrities[currentCelebrity][0]);
       ImageView currentImage = findViewById(R.id.imageView);

       DownloadImage downloadImage = new DownloadImage();
        try {
            Bitmap image = downloadImage.execute(celebrities[currentCelebrity][1]).get();
            currentImage.setImageBitmap(image);
        } catch (Exception e){
            e.printStackTrace();
        }
        ArrayList<Integer> selectedCelebrities = new ArrayList<Integer>();
        selectedCelebrities.add(currentCelebrity);
        for(int i = 0; i<4; i++){
            if(correctAnswer!=i){
                int wrongCelebrity = random.nextInt(66);
                while (wrongCelebrity==correctAnswer||selectedCelebrities.contains(wrongCelebrity)){
                    wrongCelebrity = random.nextInt(66);
                }
                selectedCelebrities.add(wrongCelebrity);
                answers[i].setText(celebrities[wrongCelebrity][0]);
            }
        }
    }

    private boolean wasOneOfTheLastTenCelebrities(){
        boolean wasOne = false;
        int i = 0;
        while (i<10&&!wasOne){
            if(currentCelebrity== lastTenCelebrities[i]){
                wasOne = true;
            }
            i++;
        }
        return wasOne;
    }

    private void addToTheLastTenCelebrities(){
        lastTenCelebrities[positionForNewTenCelebrity] = currentCelebrity;
        if(positionForNewTenCelebrity==9){
            positionForNewTenCelebrity = 0;
        }
        else{
            positionForNewTenCelebrity++;
        }
    }

    public void loadWebsite(View view){
        DownloadWebsite task = new DownloadWebsite();
        try {
            celebrities = task.execute("http://www.posh24.se/kandisar").get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch(Exception e){
            e.printStackTrace();
        }
        nextCelebrity();
        view.setVisibility(View.INVISIBLE);
        for(int i = 0; i<4; i++){
            answers[i].setVisibility(View.VISIBLE);
        }
    }
}
