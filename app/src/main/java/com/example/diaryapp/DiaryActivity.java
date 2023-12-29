package com.example.diaryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;


public class DiaryActivity extends AppCompatActivity {
    private ArrayList<DiaryEntry> mDiary;
    private ArrayAdapter listAdapter;
    private static final int WRITE_ENTRY = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_diary);

        //Loads saved diary entries from memory
        mDiary = loadDiary();

        FloatingActionButton writeEntryButton = findViewById(R.id.newDiaryEntry);
        writeEntryButton.setOnClickListener(v -> {
            Intent writeIntent = new Intent(DiaryActivity.this, WriteActivity.class);
            startActivityForResult(writeIntent, WRITE_ENTRY);
        });

        ListView diaryListView = findViewById(R.id.diary);
        listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mDiary);

        diaryListView.setAdapter(listAdapter);
        diaryListView.setOnItemClickListener((parent, lv, position, id) -> {
            Intent readIntent = new Intent(DiaryActivity.this, ReadActivity.class);

            DiaryEntry de = (DiaryEntry) diaryListView.getItemAtPosition(position);
            Gson objectParser = new Gson();
            String selectedDiaryEntry = objectParser.toJson(de);
            readIntent.putExtra("Diary", selectedDiaryEntry);

            startActivity(readIntent);
        });
    }
        private void addEntry(DiaryEntry de){
        mDiary.add(de);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == WRITE_ENTRY){

                Gson objectParser = new Gson();
                String diaryInput = data.getStringExtra("Diary");
                DiaryEntry createdEntry = objectParser.fromJson(diaryInput, DiaryEntry.class);
                addEntry(createdEntry);
            }
        }


    }
    @Override
    public void onResume(){
        super.onResume();
        listAdapter.notifyDataSetChanged();
    }
    @Override
    public void onPause(){
        saveDiary();
        super.onPause();
    }
    private void saveDiary(){
        Collections.reverse(mDiary);
        Gson gson = new Gson();
        String listToString = gson.toJson(mDiary);
        SharedPreferences sprefs = getSharedPreferences("com.example.diaryapp", Context.MODE_PRIVATE);
        sprefs.edit().putString("diary", listToString).apply();
    }
    private ArrayList<DiaryEntry> loadDiary(){
        Gson gson = new Gson();
        SharedPreferences sprefs = getSharedPreferences("com.example.diaryapp", Context.MODE_PRIVATE);
        String list = sprefs.getString("diary", "");
        if(list != "") {
            Type type = new TypeToken<ArrayList<DiaryEntry>>() {}.getType();
            ArrayList<DiaryEntry> diary = gson.fromJson(list, type);

            return diary;
        }
        else{
            return new ArrayList<DiaryEntry>();
        }
    }
}