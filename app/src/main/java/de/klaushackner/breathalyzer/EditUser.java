package de.klaushackner.breathalyzer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.klaushackner.breathalyzer.model.User;

public class EditUser extends AppCompatActivity {
    private TextView tvName;
    private TextView tvAge;
    private TextView tvWeight;
    private TextView tvHeight;
    private RadioButton male;
    private long created;
    private User userToEdit;


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
        final EditUser eu = this;

        created = getIntent().getLongExtra("created", 0);

        if (created == 0) {
            finish();
        }

        SharedPreferences sharedPref = getSharedPreferences("data", 0);
        JSONArray users;
        try {
            users = new JSONArray(sharedPref.getString("users", "[]"));

            for (int i = 0; i < users.length(); i++) {
                JSONObject j = new JSONObject(users.get(i).toString());
                if (j.getLong("created") == created) {
                    userToEdit = new User(new JSONObject(users.get(i).toString()));
                    tvName.setText(userToEdit.getName());
                    tvAge.setText(userToEdit.getAge() + "");
                    tvWeight.setText(userToEdit.getWeight() + "");
                    tvHeight.setText(userToEdit.getHeight() + "");
                    if (!userToEdit.isMale()) {
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


        if (User.isValidUser(name, age, height, weight)) {
            try {
                SharedPreferences sharedPref = getSharedPreferences("data", 0);
                JSONArray users = new JSONArray(sharedPref.getString("users", "[]"));
                JSONObject user = new User(name, male.isChecked(), age, weight, height, System.currentTimeMillis(), userToEdit.getDrinks()).toJSON();

                for (int i = 0; i < users.length(); i++) {
                    JSONObject j = new JSONObject(users.get(i).toString());
                    if (j.getLong("created") == created) {
                        users.put(i, user.toString());
                    }
                }

                userToEdit.saveUser(this);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }


}
