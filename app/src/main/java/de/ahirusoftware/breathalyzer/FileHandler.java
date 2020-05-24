package de.ahirusoftware.breathalyzer;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.util.Date;

public class FileHandler {
    public static void writeToFile(String data, Context c) {
        // Build file name
        //String date = DateFormat.getDateInstance().format(new Date(System.currentTimeMillis()));
        String appName = c.getApplicationInfo().packageName;
        String filename = String.format("%s", appName);

        // Is external storage available?
        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            return;
        }

        // Create file in downloads dir
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS), filename);

        FileOutputStream outputStream = null;
        try {
            file.createNewFile();
            // Don't append to file
            outputStream = new FileOutputStream(file, false);

            outputStream.write(data.getBytes());
            outputStream.flush();
            outputStream.close();
            Log.i("FileHandler", "Wrote File: " + file.getAbsolutePath());
            Toast.makeText(c, "Wrote File: " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }





}
