package com.example.diaryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

//TODO - add DiaryEntries to DiaryEntry list
//BitmapSaver doesn't work as intended
//Fix filter by day-colour, filter by date

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button diaryButton = findViewById(R.id.diaryButton);
        diaryButton.setOnClickListener(v -> {
            Intent intent  = new Intent(MainActivity.this, DiaryActivity.class);
            startActivity(intent);
        });

        TextView textView = findViewById(R.id.quoteDisplay);

        QuoteOfDay qod = new QuoteOfDay();
        qod.displayQuote(textView);

    }


}