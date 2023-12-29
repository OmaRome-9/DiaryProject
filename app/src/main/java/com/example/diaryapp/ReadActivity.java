package com.example.diaryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

public class ReadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_read);
        initialize();
    }
    private void initialize(){
        DiaryEntry de = getEntryFromIntent();

        TextView reader = findViewById(R.id.TextReader);
        reader.setMovementMethod(new ScrollingMovementMethod());
        reader.setText(de.getLogText());

        ImageView iv = findViewById(R.id.entryImage);

        BitmapSaver loader = new BitmapSaver(getApplicationContext());

        String logImageId = de.getLogImage();
        if(logImageId != null) {
            Bitmap image = loader.loadBitmap(logImageId);
            iv.setImageBitmap(image);
        }

        String colour = de.getColour();
        if(colour.equals("green")){
            findViewById(R.id.goodDayButton).setBackgroundColor(Color.TRANSPARENT);
        }
        else if(colour.equals("yellow")){
            findViewById(R.id.neutralDayButton).setBackgroundColor(Color.TRANSPARENT);
        }
        else if(colour.equals("red")){
            findViewById(R.id.badDayButton).setBackgroundColor(Color.TRANSPARENT);
        }
    }
    private DiaryEntry getEntryFromIntent(){
        Gson objectParser = new Gson();
        String entryStringJson = getIntent().getStringExtra("Diary");
        DiaryEntry de = objectParser.fromJson(entryStringJson, DiaryEntry.class);

        return de;
    }
}