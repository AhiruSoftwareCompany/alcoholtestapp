package de.ahirusoftware.breathalyzer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class CreateUser extends AppCompatActivity {
    private TextView tvName;
    private TextView tvAge;
    private TextView tvWeight;
    private TextView tvHeight;
    private RadioButton male;
    private CreateUser cp; //Used to display toast
    private boolean initialUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);
        tvName = findViewById(R.id.name);
        tvAge = findViewById(R.id.age);
        tvWeight = findViewById(R.id.weight);
        tvHeight = findViewById(R.id.height);
        male = findViewById(R.id.sex_male);
        cp = this;

        initialUser = getIntent().getBooleanExtra("initialUser", false);

        Button createUser = findViewById(R.id.createUser);

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

        //"0" makes an empty field into a zero -> prevents crashing
        int age = Integer.parseInt("0" + tvAge.getText());
        int weight = Integer.parseInt("0" + tvWeight.getText());
        int height = Integer.parseInt("0" + tvHeight.getText());

        SharedPreferences sharedPref = getSharedPreferences("data", 0);
        SharedPreferences.Editor editor = sharedPref.edit();

        if (User.isValidUser(name, age, height, weight)) {
            try {
                JSONArray users = new JSONArray(sharedPref.getString("users", "[]"));
                JSONObject user = new User(name, male.isChecked(), age, weight, height).toJSON();
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

    public void requestPermission() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, 1);
        } else {
            // do something for phones running an SDK before lollipop
        }
    }

    public void loadBackup() {
        requestPermission();

        if (FileHandler.loadFromFile(this)) {
            //Backup successfully loaded, starting MainActivity -> user picker is shown
            Intent intent = new Intent(CreateUser.this, MainActivity.class);
            startActivity(intent);
        }
    }

    /**
     * Menu stuff
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_user, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Remove load backup option if a user exists
        if (!initialUser) {
            menu.removeItem(R.id.loadBackup);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.loadBackup:
                loadBackup();
                break;
            case R.id.about:
                MainActivity.showAboutDialog(this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
