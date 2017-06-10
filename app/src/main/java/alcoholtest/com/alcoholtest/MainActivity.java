package alcoholtest.com.alcoholtest;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private boolean doubleBackToExitPressedOnce;
    private JSONObject currentPerson = null;
    TextView tvName;
    TextView tvAge;
    TextView tvWeight;
    TextView tvHeight;
    TextView tvSex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (switchUser() && currentPerson != null) {
            tvName = (TextView) findViewById(R.id.name);
            tvAge = (TextView) findViewById(R.id.age);
            tvWeight = (TextView) findViewById(R.id.weight);
            tvHeight = (TextView) findViewById(R.id.height);
            tvSex = (TextView) findViewById(R.id.sex);

            try {
                tvName.setText(currentPerson.getString("name"));
                tvAge.setText(currentPerson.getDouble("age") + " Jahre");
                tvWeight.setText(currentPerson.getDouble("weight") + " kg");
                tvHeight.setText(currentPerson.getDouble("height") + " cm");

                if (currentPerson.getBoolean("isMale")) {
                    tvSex.setText("männlich");
                } else {
                    tvSex.setText("weiblich");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    public boolean switchUser() {
        SharedPreferences sharedPref = getSharedPreferences("settings", 0);

        //Ist die Personendatenbank leer
        if (sharedPref.getString("persons", null) == null) {
            startActivity(new Intent(this, CreatePerson.class));
            return true;
        } else {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
            LayoutInflater inflater = getLayoutInflater();
            View convertView = (View) inflater.inflate(R.layout.dialog_switchperson, null);
            alertDialog.setView(convertView);
            alertDialog.setTitle("Wähle eine Person");
            ListView lv = (ListView) convertView.findViewById(R.id.listView1);
            String personsString[];

            try {
                JSONArray persons = new JSONArray(sharedPref.getString("persons", "[]"));
                personsString = new String[persons.length()];

                if (persons.length() == 1) {
                    currentPerson = new JSONObject(persons.get(0).toString());
                    return true;
                }

                for (int i = 0; i < persons.length(); i++) {
                    JSONObject person = new JSONObject(persons.get(i).toString());
                    personsString[i] = person.getString("name");
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, personsString);
                lv.setAdapter(adapter);
                alertDialog.show();

                //Handle click on person

                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return false;
        }
    }


    /**
     * Layout-Stuff
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.switchPerson:

                //TODO: Dialog, um zw. Benutzern zu wechseln
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {


        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Nochmal drücken, um zu verlassen", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 1700);
    }
}