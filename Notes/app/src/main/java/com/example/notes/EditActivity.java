package com.example.notes;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class EditActivity extends AppCompatActivity {
    EditText editNoteText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        editNoteText = findViewById(R.id.editNotePlainText);
        Intent intent = getIntent();
        String text = intent.getStringExtra("user_text");
        editNoteText.setText(text);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        String text = String.valueOf(editNoteText.getText());
        intent.putExtra("editTextValue", text);
        setResult(Activity.RESULT_OK, intent);
        super.onBackPressed();
    }
}
