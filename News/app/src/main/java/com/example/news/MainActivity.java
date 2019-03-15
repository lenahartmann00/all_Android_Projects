package com.example.news;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;


public class MainActivity extends AppCompatActivity {

    int numberOfArticles = 20; //Sets the number of news-articles shown in the app

    ListView listView;
    ArrayAdapter<String> adapter;
    ArrayList<String> titles;
    ArrayList<String> urls;
    SQLiteDatabase database;

    class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                //Reading out all indices of the TOP 500 articles
                String allArticleIndices = getHTTPSWebsiteContent(strings[0]);
                JSONArray jsonArray = new JSONArray(allArticleIndices);
                if (jsonArray.length() < numberOfArticles) numberOfArticles = jsonArray.length();

                //Inserts a certain amount (= numberOfArticles) of articles into database
                clearDatabase();
                String articleId;
                for (int i = 0; i < numberOfArticles; i++) {
                    articleId = jsonArray.getString(i);
                    insertArticleIntoDatabase(articleId);
                }
                return allArticleIndices;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            updateListView();
        }
    }

    private void updateListView(){
        Cursor c = database.rawQuery("SELECT * FROM articles", null);

        int titleIndex = c.getColumnIndex("title");
        int urlIndex = c.getColumnIndex("url");
        if(c.moveToFirst()){
            titles.clear();
            urls.clear();
            do{
                titles.add(c.getString(titleIndex));
                urls.add(c.getString(urlIndex));
            }while(c.moveToNext());
            if(titles != null && urls != null)  adapter.notifyDataSetChanged();
        }
    }

    //Loads old articles from database into listView and updates database in the background
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Load database articles
        database = this.openOrCreateDatabase("articles", MODE_PRIVATE, null);
        database.execSQL("CREATE TABLE IF NOT EXISTS articles (id INTEGER PRIMARY KEY, articleId INT(2), title VARCHAR, url VARCHAR)");
        titles = new ArrayList<>();
        urls = new ArrayList<>();
        listView = findViewById(R.id.listView);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, titles);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), articleViewActivity.class);
                intent.putExtra("url",urls.get(position));
                startActivity(intent);
            }
        });
        updateListView();

        //Update database
        DownloadTask task = new DownloadTask();
        task.execute("https://hacker-news.firebaseio.com/v0/topstories.json?print=pretty");

    }


    private String getHTTPSWebsiteContent(String link) {
        String result = "";
        try {
            URL url = new URL(link);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.connect();
            InputStream in = connection.getInputStream();
            InputStreamReader reader = new InputStreamReader(in);

            int data = reader.read();
            while (data != -1) {
                result += (char) data;
                data = reader.read();
            }
            connection.disconnect();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void clearDatabase() {
        database.execSQL("DELETE FROM articles ");
    }

    private void insertDataIntoArticleTable(String articleId, String title, String url){
        String sql = "INSERT INTO articles (articleId, title, url) VALUES (?,?,?)";
        SQLiteStatement statement = database.compileStatement(sql);

        statement.bindString(1, articleId);
        statement.bindString(2, title);
        statement.bindString(3, url);
        statement.execute();
    }

    private void insertArticleIntoDatabase(String articleId){
        String websiteContent = getHTTPSWebsiteContent("https://hacker-news.firebaseio.com/v0/item/" + articleId + ".json?print=pretty");
        String title;
        String url;
        try {
            JSONObject jsonObject = new JSONObject(websiteContent);
            if (!jsonObject.isNull("title") && !jsonObject.isNull("url")) {
                title = jsonObject.getString("title");
                url = jsonObject.getString("url");
                insertDataIntoArticleTable(articleId, title, url);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
