package de.klaushackner.alcoholtest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CreateUser extends AppCompatActivity {
    private TextView tvName;
    private TextView tvAge;
    private TextView tvWeight;
    private TextView tvHeight;
    private RadioButton male;
    private CreateUser cp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);
        tvName = (TextView) findViewById(R.id.name);
        tvAge = (TextView) findViewById(R.id.age);
        tvWeight = (TextView) findViewById(R.id.weight);
        tvHeight = (TextView) findViewById(R.id.height);
        male = (RadioButton) findViewById(R.id.sex_male);
        cp = this;

        Button createUser = (Button) findViewById(R.id.createUser);
        createUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addUser()) {
                    startActivity(new Intent(CreateUser.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(cp, cp.getResources().getText(R.string.wronginput), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public boolean addUser() {
        String name = tvName.getText().toString();

        //"0" makes an empty field into a zero
        double age = Double.parseDouble("0" + tvAge.getText());
        double weight = Double.parseDouble("0" + tvWeight.getText());
        double height = Double.parseDouble("0" + tvHeight.getText());

        //TODO: Maybe split this up
        if (name.compareTo(cp.getResources().getText(R.string.add_user) + "") != 0 && name.length() > 2 && age > 10 && age < 100 && weight > 30 && weight < 200 && height > 100 && height < 230) {
            try {
                SharedPreferences sharedPref = getSharedPreferences("data", 0);
                SharedPreferences.Editor editor = sharedPref.edit();

                Log.i("Added user", sharedPref.getString("users", "[]"));
                JSONArray users = new JSONArray(sharedPref.getString("users", "[]"));
                JSONObject user = new JSONObject();
                user.put("name", name);
                user.put("isMale", male.isChecked());
                user.put("age", age);
                user.put("weight", weight);
                user.put("height", height);
                user.put("created", System.currentTimeMillis());
                users.put(user);
                Log.i("Current users object", users.toString());

                editor.putString("users", users.toString());
                editor.commit();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return true;
        } else {
            return false;
        }
    }
}
