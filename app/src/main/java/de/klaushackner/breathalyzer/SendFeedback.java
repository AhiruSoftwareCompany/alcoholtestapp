package de.klaushackner.breathalyzer;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

public class SendFeedback extends AppCompatActivity {
    private EditText name;
    private EditText message;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_feedback);

        name = (EditText) findViewById(R.id.name);
        message = (EditText) findViewById(R.id.text);
        button = (Button) findViewById(R.id.sendFeedback);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }

    private void sendMessage() {
        JSONObject toSend = new JSONObject();
        try {
            toSend.put("device", getDeviceInfo());
            toSend.put("sender", name.getText());
            toSend.put("message", message.getText());
            FeedbackSender fs = new FeedbackSender(toSend, getApplicationContext());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject getDeviceInfo() {
        JSONObject deviceInfo = new JSONObject();
        try {
            deviceInfo.put("os_ver", System.getProperty("os.version") + "(" + Build.VERSION.INCREMENTAL + ")");
            deviceInfo.put("os_api_lvl", android.os.Build.VERSION.SDK_INT);
            deviceInfo.put("device", android.os.Build.DEVICE + " (" + Build.MANUFACTURER + ", " + Build.MODEL + ")");
            deviceInfo.put("model", android.os.Build.MODEL + " (" + android.os.Build.PRODUCT + ")");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return deviceInfo;
    }


}
