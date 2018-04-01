package de.klaushackner.breathalyzer;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;


public class SendFeedback extends AppCompatActivity {
    private EditText name;
    private EditText email;
    private EditText message;
    private Button button;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_feedback);
        dialog = new Dialog(this);
        dialog.setCancelable(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //No title
        dialog.setContentView(R.layout.loading);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        message = findViewById(R.id.message);
        button = findViewById(R.id.sendFeedback);

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
            toSend.put("Device", getDeviceInfo());
            toSend.put("AppInfo", "Friendly name: " + BuildConfig.VERSION_NAME + ", version code: " + BuildConfig.VERSION_CODE);
            toSend.put("LogTrace", ""); //TODO
            toSend.put("Sender", name.getText());
            toSend.put("SenderMail", email.getText());
            toSend.put("Message", message.getText());

            if (isValidRequest()) {
                new FeedbackSender(toSend, this);
                dialog.show();
                button.setEnabled(false);
            } else {
                Toast.makeText(this, R.string.wronginput, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject getDeviceInfo() {
        JSONObject deviceInfo = new JSONObject();
        try {
            deviceInfo.put("OsVer", System.getProperty("os.version") + "(" + Build.VERSION.INCREMENTAL + ")");
            deviceInfo.put("OsApiLvl", android.os.Build.VERSION.SDK_INT);
            deviceInfo.put("Device", android.os.Build.DEVICE + " (" + Build.MANUFACTURER + ", " + Build.MODEL + ")");
            deviceInfo.put("Model", android.os.Build.MODEL + " (" + android.os.Build.PRODUCT + ")");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return deviceInfo;
    }

    public boolean isValidRequest() {
        if (message.getText().length() > 3) { //Should be enough for now
            if (email.getText().length() > 0) {
                if (email.getText().toString().contains("@") && email.getText().toString().contains(".")) {
                    return true;
                } else {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public void onResult(int code) {
        dialog.dismiss();
        button.setEnabled(true);
        switch (code) {
            case 0:
                finish();
                break;
        }
    }


}
