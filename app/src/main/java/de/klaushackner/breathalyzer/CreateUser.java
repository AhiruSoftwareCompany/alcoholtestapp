package de.klaushackner.breathalyzer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.klaushackner.breathalyzer.model_old.User;

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

    private boolean addUser() {
        String name = tvName.getText().toString();

        //"0" makes an empty field into a zero
        int age = Integer.parseInt("0" + tvAge.getText());
        int weight = Integer.parseInt("0" + tvWeight.getText());
        int height = Integer.parseInt("0" + tvHeight.getText());

        SharedPreferences sharedPref = getSharedPreferences("data", 0);
        SharedPreferences.Editor editor = sharedPref.edit();

        if (User.isValidUser(name, age, height, weight)) {
            try {
                JSONArray users = new JSONArray(sharedPref.getString("users", "[]"));
                JSONObject user = new User(name, male.isChecked(), age, weight, height, System.currentTimeMillis(), new JSONArray()).toJSON();
                users.put(user);

                editor.putString("users", users.toString());
                editor.commit();
                Log.i("Created user", user.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return true;
        } else {
            return false;
        }
    }
}
