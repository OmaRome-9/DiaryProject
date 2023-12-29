package com.example.diaryapp;

import static android.app.PendingIntent.getActivity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

//add all data from selected entry
public class WriteActivity extends AppCompatActivity {

    private String mColour;
    private String mEntryText;
    private String userChosenTask;
    private ImageView mDiaryEntryImage;
    private Bitmap mCurrentImage;
    private TextWatcher mTextWatcher = new TextWatcher(){
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s){
            mEntryText = s.toString();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        initialize();

    }
    private void initialize(){

        mDiaryEntryImage = findViewById(R.id.entryImage);
        mDiaryEntryImage.setOnClickListener(v ->{
            selectImage();
        });

        EditText inputTextField = findViewById(R.id.diaryTextWriter);
        inputTextField.addTextChangedListener(mTextWatcher);

        ImageButton goodButton = findViewById(R.id.goodDayButton);
        ImageButton neutralButton = findViewById(R.id.neutralDayButton);
        ImageButton badButton = findViewById(R.id.badDayButton);
        Button submitButton = findViewById(R.id.submitButton);

        goodButton.setOnClickListener(v ->{
            if(mColour == "red")
                badButton.setBackgroundColor(Color.RED);
            else if(mColour == "yellow")
                neutralButton.setBackgroundColor(Color.YELLOW);
            mColour = "green";
            goodButton.setBackgroundColor(Color.TRANSPARENT);
        });
        neutralButton.setOnClickListener(v ->{
            if(mColour == "red")
                badButton.setBackgroundColor(Color.RED);
            else if(mColour == "green")
                goodButton.setBackgroundColor(Color.GREEN);
            neutralButton.setBackgroundColor(Color.TRANSPARENT);
            mColour = "yellow";
        });
        badButton.setOnClickListener(v ->{
            if(mColour == "yellow")
                neutralButton.setBackgroundColor(Color.YELLOW);
            else if(mColour == "green")
                goodButton.setBackgroundColor(Color.GREEN);
            badButton.setBackgroundColor(Color.TRANSPARENT);
            mColour = "red";
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to submit your entry?");
        builder.setCancelable(true);
        builder.setPositiveButton(R.string.ok, (dialog, id) -> {
            if (mColour != null && mEntryText != null){
                Date currentDate = new Date();
                DiaryEntry diaryEntry;
                if(mCurrentImage != null) {
                    BitmapSaver saver = new BitmapSaver(getApplicationContext());
                    String uniqueID = UUID.randomUUID().toString();
                    diaryEntry = new DiaryEntry(mColour, mEntryText, currentDate, uniqueID);
                    saver.saveBitmap(mCurrentImage, uniqueID);
                }
                else{
                    diaryEntry = new DiaryEntry(mColour, mEntryText, currentDate, null);
                }
                Gson objectParser = new Gson();

                //Pass diaryEntry to DiaryActivity in JSON format
                String diaryEntryToString = objectParser.toJson(diaryEntry);

                Intent returnIntent = new Intent();
                returnIntent.putExtra("Diary", diaryEntryToString);
                setResult(Activity.RESULT_OK, returnIntent);
                killActivity();

            }
            else{
                Toast.makeText(getApplicationContext(), "Please select a colour to represent your day and write an entry", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton(R.string.cancel, (dialog, id) -> {
            dialog.dismiss();
        });
        AlertDialog submitAlert = builder.create();
        submitButton.setOnClickListener(v ->{
            submitAlert.show();
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 123:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChosenTask.equals("Select image"))
                        gallerySelected();
                } else {

                }
                break;
        }
    }
    private void selectImage() {
        final CharSequence[] items = { "Select image",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(WriteActivity.this);
        builder.setTitle("Add image to entry");
        builder.setItems(items, (dialog, item) -> {

            boolean result = (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED);

            if (items[item].equals("Select image")) {
                userChosenTask ="Select image";
                if(result)
                    gallerySelected();
                else
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 123);
            } else if (items[item].equals("Cancel")) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
    private void gallerySelected()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select image"), 1);
    }
    private void killActivity(){
        finish();
    }

    //Sets Diary Entry Image to selected Bitmap if a Bitmap (data) was successfully selected
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                Bitmap bm=null;
                if (data != null) {
                    try {
                        bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                mDiaryEntryImage.setImageBitmap(bm);
                mCurrentImage = bm;
            }
        }
    }

}