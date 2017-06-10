package alcoholtest.com.alcoholtest;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
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
    private static Context ma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ma = this;

        if (switchUser(false) && currentPerson != null) {
            //TODO: find better name
            setGui();
        }

    }

    public void setGui() {
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

    public boolean removeUser(String userAsString) throws JSONException {
        SharedPreferences sharedPref = getSharedPreferences("settings", 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        JSONArray persons = new JSONArray(sharedPref.getString("persons", "[]"));

        for (int i = 0; i < persons.length(); i++) {
            JSONObject person = new JSONObject(persons.get(i).toString());
            if (person.getString("user").compareTo(userAsString) == 0) {
                persons.remove(i);
                editor.putString("persons", persons.toString());
                editor.commit();
                return true;
            }
        }
        return false;
    }

    //TODO: Refactor person to user
    public boolean switchUser(boolean fromMenu) {
        SharedPreferences sharedPref = getSharedPreferences("settings", 0);

        //Ist die Personendatenbank leer
        if (sharedPref.getString("persons", null) == null) {
            startActivity(new Intent(this, CreatePerson.class));
            finish();
        } else {
            String personsString[];

            try {
                final JSONArray persons = new JSONArray(sharedPref.getString("persons", "[]"));

                //Das tut so weh :c
                personsString = new String[persons.length() + 1];

                if (persons.length() == 1 && !fromMenu) {
                    currentPerson = new JSONObject(persons.get(0).toString());
                    return true;
                }

                for (int i = 0; i < persons.length(); i++) {
                    JSONObject person = new JSONObject(persons.get(i).toString());
                    personsString[i] = person.getString("name");

                }

                personsString[personsString.length - 1] = ma.getResources().getText(R.string.add_person) + "";

                final String[] pS = personsString;

                ListAdapter adapter = new ArrayAdapter<String>(
                        getApplicationContext(), R.layout.dialog_switchperson_item, personsString) {

                    ViewHolder holder;

                    class ViewHolder {
                        TextView name;
                    }

                    public View getView(int position, View convertView,
                                        ViewGroup parent) {
                        final LayoutInflater inflater = (LayoutInflater) getApplicationContext()
                                .getSystemService(
                                        Context.LAYOUT_INFLATER_SERVICE);

                        if (convertView == null) {
                            convertView = inflater.inflate(R.layout.dialog_switchperson_item, null);

                            holder = new ViewHolder();
                            holder.name = (TextView) convertView
                                    .findViewById(R.id.name);
                            convertView.setTag(holder);
                        } else {
                            // view already defined, retrieve view holder
                            holder = (ViewHolder) convertView.getTag();
                        }
                        holder.name.setText(pS[position]);

                        return convertView;
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Pick a tile set");
                builder.setAdapter(adapter,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int item) {
                                if (pS[item].compareTo(ma.getResources().getText(R.string.add_person) + "") == 0) {
                                    startActivity(new Intent(MainActivity.this, CreatePerson.class));
                                    finish();
                                }

                                try {
                                    JSONObject selectedPerson = new JSONObject(persons.get(item).toString());
                                    Toast.makeText(MainActivity.this, "You selected: " + selectedPerson.getString("name"), Toast.LENGTH_LONG).show();
                                    currentPerson = selectedPerson;
                                    setGui();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                dialog.dismiss();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();

                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
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
                switchUser(true);
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