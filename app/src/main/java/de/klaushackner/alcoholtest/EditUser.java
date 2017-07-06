package de.klaushackner.alcoholtest;

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

import de.klaushackner.alcoholtest.model.User;

public class EditUser extends AppCompatActivity {
    private TextView tvName;
    private TextView tvAge;
    private TextView tvWeight;
    private TextView tvHeight;
    private RadioButton male;
    private EditUser eu;
    private long created;
    private SharedPreferences sharedPref;
    private JSONArray users;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_user);
        tvName = (TextView) findViewById(R.id.name);
        tvAge = (TextView) findViewById(R.id.age);
        tvWeight = (TextView) findViewById(R.id.weight);
        tvHeight = (TextView) findViewById(R.id.height);
        male = (RadioButton) findViewById(R.id.sex_male);
        RadioButton female = (RadioButton) findViewById(R.id.sex_female);
        eu = this;

        created = getIntent().getLongExtra("created", 0);

        if (created == 0) {
            finish();
        }

        sharedPref = getSharedPreferences("data", 0);
        users = null;
        try {
            users = new JSONArray(sharedPref.getString("users", "[]"));

            for (int i = 0; i < users.length(); i++) {
                JSONObject j = new JSONObject(users.get(i).toString());
                if (j.getLong("created") == created) {
                    User currentUser = new User(new JSONObject(users.get(i).toString()));
                    tvName.setText(currentUser.getName());
                    tvAge.setText(currentUser.getAge() + "");
                    tvWeight.setText(currentUser.getWeight() + "");
                    tvHeight.setText(currentUser.getHeight() + "");
                    if (!currentUser.isMale()) {
                        female.toggle();
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Button saveUser = (Button) findViewById(R.id.saveUser);
        saveUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editUser()) {
                    startActivity(new Intent(EditUser.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(eu, eu.getResources().getText(R.string.wronginput), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private boolean editUser() {
        String name = tvName.getText().toString();

        //"0" makes an empty field into a zero
        int age = Integer.parseInt("0" + tvAge.getText());
        int weight = Integer.parseInt("0" + tvWeight.getText());
        int height = Integer.parseInt("0" + tvHeight.getText());

        sharedPref = getSharedPreferences("data", 0);
        SharedPreferences.Editor editor = sharedPref.edit();

        if (User.isValidUser(name, age, height, weight)) {
            try {
                JSONObject user = new JSONObject();
                user.put("name", name);
                user.put("isMale", male.isChecked());
                user.put("age", age);
                user.put("weight", weight);
                user.put("height", height);
                user.put("created", System.currentTimeMillis() / 1000);

                for (int i = 0; i < users.length(); i++) {
                    JSONObject j = new JSONObject(users.get(i).toString());
                    if (j.getLong("created") == created) {
                        users.put(i, user.toString());
                    }
                }

                editor.putString("users", users.toString());
                editor.commit();
                Log.i("Updated user", user.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return true;
        } else {
            return false;
        }
    }


}
