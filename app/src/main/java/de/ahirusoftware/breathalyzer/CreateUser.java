package de.ahirusoftware.breathalyzer;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class CreateUser extends AppCompatActivity {
    private TextView tvName;
    private TextView tvAge;
    private TextView tvWeight;
    private TextView tvHeight;
    private RadioButton male;
    private CreateUser cp; //Used to display toast

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

    /**
     * Menu stuff
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_user, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        if (User.getUserCount(c) == 1) {
            menu.removeItem(R.id.switchUser);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.switchUser:
                switchUser(false);
                break;
            case R.id.editUser:
                editUser();
                break;
            case R.id.removeUser:
                removeUser(currentUser);
                break;
            case R.id.newUser:
                createUser(false);
                break;
            case R.id.recipes:
                Intent i = new Intent(this, ShowRecipes.class);
                i.putExtra("currentUser", currentUser.created);
                startActivity(i);
                break;
            case R.id.sendFeedback:
                //startActivity(new Intent(this, SendFeedback.class));
                Toast.makeText(this, "currently unavailable", Toast.LENGTH_SHORT);
                break;
            case R.id.createBackup:
                requestPermission();
                // Creates backup of all current users
                SharedPreferences sharedPref = getSharedPreferences("data", 0);
                final SharedPreferences.Editor editor = sharedPref.edit();
                System.out.println("SharedPrefs: " + sharedPref.getAll());

                //Ist die Users-datenbank leer, wird ein neuer User erstellt
                if (sharedPref.getString("users", "[]").compareTo("[]") != 0) {
                    final JSONArray users;
                    try {
                        users = new JSONArray(sharedPref.getString("users", "[]"));
                        FileHandler.writeToFile(users.toString(), c);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(this, "No users available!", Toast.LENGTH_SHORT);
                }
                break;
            case R.id.loadBackup:
                // Opens File Picker Intent and calls onActivityResult afterwards
                // Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                //  intent.addCategory(Intent.CATEGORY_OPENABLE);
                //  intent.setType("*/*");
                //  startActivityForResult(intent, PICK_BACKUP_FILE);
                // just load the backup from default path
                loadBackup("");
                break;
            case R.id.about:
                final Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.dialog_about);
                dialog.show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
