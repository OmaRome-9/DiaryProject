package com.example.diaryapp;

import android.media.metrics.Event;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class QuoteOfDay {
    private String mQuote;
    private String mAuthor;
    private Thread mDataFetcher;
    private final String TAG = "QuoteOfDay";

    QuoteOfDay(){
        mDataFetcher = new Thread(this.new FetchRequest());
    }

    public String getQuote(){
        if(mQuote == null){
            mDataFetcher.start();
            try{
                mDataFetcher.join();
            }
            catch(InterruptedException ie){
                Log.e(TAG, "Failed joining thread", ie);
            }
        }
        return mQuote;
    }

    //Establishes HttpURLConnection to API in order to fetch quote of the day in JSON format
    private void generateQuote(){
        String generatedQuote = "";
        String quoteAuthor = "";
        try {
            URL url = new URL("https://official-joke-api.appspot.com/random_joke");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            int responseCode = connection.getResponseCode();
            if(responseCode != 200){
                throw new RuntimeException("Http Response Code: " + responseCode);
            }
            //Use scanner to iterate through the stream from the URL and parse JSON data to String inline
            else{
                String inline = "";
                Scanner urlScanner = new Scanner(url.openStream());
                while (urlScanner.hasNext()){
                    inline += urlScanner.nextLine();
                }
                urlScanner.close();
                //Convert String inline to JSON Object for easier access to data, assign values to quote and author
                JsonObject jsonObject = JsonParser.parseString(inline).getAsJsonObject();
                generatedQuote = jsonObject.get("setup").getAsString();
                quoteAuthor = jsonObject.get("punchline").getAsString();
            }
        }
        catch(IOException e){
            Log.e(TAG, "Error fetching data", e);
        }
        mQuote = generatedQuote;
        mAuthor = quoteAuthor;
    }
    private class FetchRequest implements Runnable{
        @Override
        public void run() {
            generateQuote();
        }
    }
    public void displayQuote(TextView textView){
        getQuote();
        textView.setText("> " + mQuote + "\n\n" + "--" + mAuthor);
    }
}
