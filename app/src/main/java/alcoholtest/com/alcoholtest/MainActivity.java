package alcoholtest.com.alcoholtest;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import alcoholtest.com.alcoholtest.model.Person;

public class MainActivity extends AppCompatActivity {

    private boolean doubleBackToExitPressedOnce;
    //TODO: Get rid of json here
    private JSONObject currentPersonAsJSON = null;
    private Person currentPerson;
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

        if (switchUser(false) && currentPersonAsJSON != null  /* || currentPersonAsJSON.toString().compareTo("[]") != 0 */) {
            //TODO: find better name
            setGui();
        }

    }

    /**
     * Updates GUI to match the current selected user
     */
    public void setGui() {
        tvName = (TextView) findViewById(R.id.name);
        tvAge = (TextView) findViewById(R.id.age);
        tvWeight = (TextView) findViewById(R.id.weight);
        tvHeight = (TextView) findViewById(R.id.height);
        tvSex = (TextView) findViewById(R.id.sex);

        try {
            tvName.setText(currentPersonAsJSON.getString("name"));
            tvAge.setText(currentPersonAsJSON.getDouble("age") + " Jahre");
            tvWeight.setText(currentPersonAsJSON.getDouble("weight") + " kg");
            tvHeight.setText(currentPersonAsJSON.getDouble("height") + " cm");

            if (currentPersonAsJSON.getBoolean("isMale")) {
                tvSex.setText("männlich");
            } else {
                tvSex.setText("weiblich");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public boolean removeUser(Person personToRemove) throws JSONException {
        SharedPreferences sharedPref = getSharedPreferences("settings", 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        JSONArray persons = new JSONArray(sharedPref.getString("persons", "[]"));

        for (int i = 0; i < persons.length(); i++) {
            JSONObject person = new JSONObject(persons.get(i).toString());
            if (person.getString("name").compareTo(personToRemove.getName()) == 0) {
                persons.remove(i);
                editor.putString("persons", persons.toString());
                editor.commit();

                if (persons.length() == 0) {
                    switchUser(false);
                }
                if (persons.length() == 1) {
                    currentPersonAsJSON = new JSONObject(persons.get(0).toString());
                    currentPerson = new Person(currentPersonAsJSON);
                    setGui();
                } else {
                    switchUser(true);
                }

                return true;
            }
        }

        //Wenn nur noch ein Eintrag vorhanden ist, soll die MainActivity neu gezeichnet werden. Falls der einzige Eintrag gelöscht wurde, soll eine neue Person erstellt werden


        return false;

    }

    public void editPerson() {
        Intent i = new Intent(this, EditPerson.class);
        i.putExtra("created", currentPerson.getCreated());
        startActivity(i);
    }

    //TODO: Refactor person to user
    public boolean switchUser(boolean fromMenu) {
        SharedPreferences sharedPref = getSharedPreferences("settings", 0);

        //Ist die Personendatenbank leer
        if (sharedPref.getString("persons", null) == null || sharedPref.getString("persons", null).toString().compareTo("[]") == 0) {
            startActivity(new Intent(this, CreatePerson.class));
            finish();
        } else {
            String personsString[];

            try {
                final JSONArray persons = new JSONArray(sharedPref.getString("persons", "[]"));

                //Das tut so weh :c
                personsString = new String[persons.length() + 1];

                if (persons.length() == 1 && !fromMenu) {
                    currentPersonAsJSON = new JSONObject(persons.get(0).toString());
                    currentPerson = new Person(currentPersonAsJSON);
                    return true;
                }

                for (int i = 0; i < persons.length(); i++) {
                    JSONObject person = new JSONObject(persons.get(i).toString());
                    personsString[i] = person.toString();

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
                builder.setTitle("Pick a person");
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
                                    currentPersonAsJSON = selectedPerson;
                                    currentPerson = new Person(currentPersonAsJSON);
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
            case R.id.editPerson:
                editPerson();
                break;
            case R.id.removePerson:
                try {
                    System.out.println(currentPerson.toString());
                    removeUser(currentPerson);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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