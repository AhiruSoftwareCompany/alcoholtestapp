package de.ahirusoftware.breathalyzer;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.util.Date;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

// package-private class
class FileHandler {
    static void writeToFile(String data, Context c) {
        // Build file name
        //String date = DateFormat.getDateInstance().format(new Date(System.currentTimeMillis()));
        String appName = c.getApplicationInfo().packageName;
        //String filename = String.format("%s.json", appName);
        String filename = "alcoholtestapp.json";

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

    static boolean loadFromFile(Context c) {
        return loadFromFile("", c);
    }

    static boolean loadFromFile(String path, Context c) {
        File file;
        if (path.isEmpty()) {
            file = new File("/storage/emulated/0/Download/alcoholtestapp.json");
        } else {
            file = new File(path);
        }

        try {
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String fileContent = null;
            String backup = "";

            while ((fileContent = bufferedReader.readLine()) != null) {
                sb.append(fileContent);
                backup = fileContent;
            }

            //Save loaded file
            SharedPreferences sharedPref = c.getSharedPreferences("data", 0);
            SharedPreferences.Editor editor = sharedPref.edit();

            // Convert file content to JSONArray
            JSONArray users = new JSONArray(backup);

            for (int i = 0; i < users.length(); i++) {
                User user = new User(new JSONObject(users.get(i).toString()));
                users.put(i, user.toJSON());
                System.out.println("loadBackup: " + users.toString());
                editor.putString("users", users.toString());
                editor.commit();
            }

            //Success
            return true;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }
}
