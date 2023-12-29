package com.example.diaryapp;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static java.security.AccessController.getContext;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class BitmapSaver {
    private Context context;
    public BitmapSaver(Context context){
        this.context = context;
    }
    public void saveBitmap(Bitmap bm, String bitmapId){
        File outputFile = getOutputMediaFile(bitmapId);
        if (outputFile == null){
            Log.d("BitmapSaver", "Error saving Bitmap");
        }
        else{
            try {
                FileOutputStream fos = new FileOutputStream(outputFile);
                bm.compress(Bitmap.CompressFormat.PNG, 90, fos);
                fos.close();
            } catch (FileNotFoundException e) {
                Log.d("BitmapSaver", "Error - not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d("BitmapSaver", "Error accessing file: " + e.getMessage());
            }
        }
    }
    public Bitmap loadBitmap(String bitmapId){
        File file = getOutputMediaFile(bitmapId);
        try {
            InputStream is = new FileInputStream(file);
            Bitmap loadedBitmap = BitmapFactory.decodeStream(is);
            return loadedBitmap;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    private File getOutputMediaFile(String bitmapId){
        File mediaStorageDir = new File(context.getExternalFilesDir(null)
                + "/Android/data/"
                + context.getPackageName()
                + "/Files");
        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                return null;
            }
        }
        String imageName = bitmapId + ".jpg";
        File media = new File(mediaStorageDir.getPath() + File.separator + imageName);
        return media;
    }
}
