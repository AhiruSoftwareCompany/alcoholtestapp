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
    private RadioButton female;
    private EditUser cu;
    private long created;
    private User currentUser;
    SharedPreferences sharedPref;
    JSONArray users;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_user);
        tvName = (TextView) findViewById(R.id.name);
        tvAge = (TextView) findViewById(R.id.age);
        tvWeight = (TextView) findViewById(R.id.weight);
        tvHeight = (TextView) findViewById(R.id.height);
        male = (RadioButton) findViewById(R.id.sex_male);
        female = (RadioButton) findViewById(R.id.sex_female);
        cu = this;

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
                    currentUser = new User(new JSONObject(users.get(i).toString()));
                    tvName.setText(currentUser.getName());
                    tvAge.setText(currentUser.getAge()+"");
                    tvWeight.setText(currentUser.getWeight()+"");
                    tvHeight.setText(currentUser.getHeight()+"");
                    if(!currentUser.isMale()){
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
                    Toast.makeText(cu, cu.getResources().getText(R.string.wronginput), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public boolean editUser() {
        String name = tvName.getText().toString();

        //"0" makes an empty field into a zero
        double age = Double.parseDouble("0" + tvAge.getText());
        double weight = Double.parseDouble("0" + tvWeight.getText());
        double height = Double.parseDouble("0" + tvHeight.getText());

        //TODO: Maybe split this up
        if (name.compareTo(cu.getResources().getText(R.string.add_user) + "") != 0 && name.length() > 2 && age > 10 && age < 100 && weight > 30 && weight < 200 && height > 100 && height < 230) {
            try {
                sharedPref = getSharedPreferences("data", 0);
                SharedPreferences.Editor editor = sharedPref.edit();

                Log.i("Added user", sharedPref.getString("users", "[]"));
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
