package de.klaushackner.breathalyzer;

import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class FeedbackSender extends AsyncTask<String, Void, String> {
    private int rc;
    private JSONObject toSend;
    private SendFeedback sf;

    public FeedbackSender(JSONObject toSend, SendFeedback sf) {
        this.toSend = toSend;
        this.sf = sf;
        execute();
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            URL url = new URL("https://dieechtenilente.duckdns.org:9444");
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setConnectTimeout(5000);
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setRequestProperty("User-Agent", sf.getResources().getString(R.string.app_name));

            OutputStream os = conn.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
            osw.write(toSend.toString());
            osw.flush();
            osw.close();
            os.close();
            conn.connect();
            rc = conn.getResponseCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        sf.onResult();

        switch (rc) {
            case 400:
                //irony: you can't report a error in the error report system, lol
                Toast.makeText(sf, sf.getResources().getString(R.string.wronginput), Toast.LENGTH_SHORT).show(); //TODO: Extra string for that
                break;
            case 200:
                Toast.makeText(sf, sf.getResources().getString(R.string.feedback_sent), Toast.LENGTH_SHORT).show();
                sf.finish();
                break;
            case 0:
                Toast.makeText(sf, "Fehler", Toast.LENGTH_SHORT).show(); //TODO: Extra string for that
                break;
            default:
                Toast.makeText(sf, rc + " - Fehler", Toast.LENGTH_SHORT).show(); //TODO: Extra string for that
                break;
        }
    }
}
