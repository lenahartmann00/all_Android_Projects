package com.example.notes;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private ArrayList<String> noteList;
    private int currentPosition;
    private ArrayAdapter adapter;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);

        preferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);

        //Creates ListView for notes in preferences and loads from it with every new start
        if(!preferences.contains("noteList")){
            noteList= new ArrayList<>();
            noteList.add("Geben Sie ihre erste Notiz ein");
            Set<String> noteListPreferences = new HashSet<String>(noteList);
            preferences.edit().putStringSet("noteList", noteListPreferences).apply();
        }
        else{
            Set <String> noteListPreferences = preferences.getStringSet("noteList", null);
            noteList = new ArrayList<String>(noteListPreferences);
        }

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, noteList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentPosition = position;
                Intent intent = new Intent(getApplication(), EditActivity.class);
                intent.putExtra("user_text", noteList.get(position));
                startActivityForResult(intent, 1);
            }
        });
        //Deletes note by longClick
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                createAlertDialog(position);
                return true;
            }
        });
    }

    private void createAlertDialog(final int position){
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Warnung!")
                .setMessage("Bist du dir sicher, dass du die Notiz löschen willst? Das kann nicht rückgängig gemacht werden!")
                .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        noteList.remove(position);
                        updateData();
                        Toast.makeText(MainActivity.this, "Notiz gelöscht", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Abbrechen", null) //Do nothing
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Creates new default Note by clicking "Add Notes" in menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.addNote:
                noteList.add("New Note");
                updateData();
                Toast.makeText(this, "Notiz hinzugefügt", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return false;
        }
    }

    //Sets current note to edited/new note text of user
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String strEditText =  data.getStringExtra("editTextValue");
                noteList.set(currentPosition, strEditText);
                updateData();
                Toast.makeText(this,"Notiz bearbeitet", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //Updates Adapter and Preferences to new noteList
    private void updateData(){
        adapter.notifyDataSetChanged();
        Set<String> noteListPreferences = new HashSet<String>(noteList);
        preferences.edit().putStringSet("noteList", noteListPreferences).apply();
    }

}
