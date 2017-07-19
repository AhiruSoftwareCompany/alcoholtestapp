package de.klaushackner.breathalyzer;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONObject;

import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class FeedbackSender extends AsyncTask<String, Void, String> {
    private int rc;
    private JSONObject toSend;
    private Context c;

    public FeedbackSender(JSONObject toSend, Context c) {
        this.toSend = toSend;
        this.c = c;
        execute();
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            URL url = new URL("https://dieechtenilente.duckdns.org:9444");

            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            con.setConnectTimeout(5000);
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("feedback", toSend.toString());
            rc = con.getResponseCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        switch (rc) {
            case 400:
                //irony: you can't report a error in the error report system, lol
                Toast.makeText(c, c.getResources().getString(R.string.wronginput), Toast.LENGTH_SHORT).show();
                break;
            case 200:
                Toast.makeText(c, c.getResources().getString(R.string.feedback_sent), Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(c, rc + " - Fehler", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
