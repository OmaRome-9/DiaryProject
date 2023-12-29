package com.example.diaryapp;

import java.util.Date;

public class DiaryEntry {

    private String mColour;
    private String mLogText;
    private String mLogTitle;
    private Date mLogDate;
    private String mLogImageId;

    public DiaryEntry(String colour, String diaryText, Date currentDate, String id){
        mColour = colour;
        mLogText = diaryText;
        mLogTitle = getTitle(mLogText);
        mLogDate = currentDate;
        mLogImageId=id;
    }
    private String getTitle(String input){
        if (input.length() > 20){
            return input.substring(0, 20) + "...";
        }
        else{
            return input;
        }
    }
    @Override
    public String toString(){
        return mLogTitle + " : " + mLogDate.toLocaleString();
    }

    public String getColour() {
        return mColour;
    }

    public String getLogImage() {
        return mLogImageId;
    }

    public String getLogText(){
        return mLogText;
    }
}
