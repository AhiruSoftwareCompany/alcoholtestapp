package alcoholtest.com.alcoholtest;

import android.app.AlertDialog;
import android.app.Dialog;
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
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import alcoholtest.com.alcoholtest.adapter.MixtureAdapter;
import alcoholtest.com.alcoholtest.model.Mixture;
import alcoholtest.com.alcoholtest.model.User;

public class MainActivity extends AppCompatActivity {

    private boolean doubleBackToExitPressedOnce;
    //TODO: Get rid of json here
    private JSONObject currentUserAsJSON = null;
    private User currentUser;
    Button btnAddDrink;
    TextView tvName;
    TextView tvAge;
    TextView tvWeight;
    TextView tvHeight;
    TextView tvSex;

    Mixture[] mixtureArray = {
            new Mixture("Bier", 500, 0.05, null),
            new Mixture("Bier", 1000, 0.05, null),
            new Mixture("Wodka", 20, 0.40, null)};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (switchUser() && currentUserAsJSON != null) {
            updateGui();
        }

        btnAddDrink = (Button) findViewById(R.id.add_drink_button);
        btnAddDrink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDrink(currentUser);
            }
        });

        //TODO: Put this in an about dialog
        Toast.makeText(this, "Emoji artwork provided by EmojiOne", Toast.LENGTH_SHORT).show();
    }

    /**
     * Updates GUI to match the current selected user
     */
    public void updateGui() {
        tvName = (TextView) findViewById(R.id.name);
        tvAge = (TextView) findViewById(R.id.age);
        tvWeight = (TextView) findViewById(R.id.weight);
        tvHeight = (TextView) findViewById(R.id.height);
        tvSex = (TextView) findViewById(R.id.sex);

        try {
            tvName.setText(currentUserAsJSON.getString("name"));
            tvAge.setText(currentUserAsJSON.getDouble("age") + " " + getString(R.string.years));
            tvWeight.setText(currentUserAsJSON.getDouble("weight") + " kg");
            tvHeight.setText(currentUserAsJSON.getDouble("height") + " cm");

            if (currentUserAsJSON.getBoolean("isMale")) {
                tvSex.setText(R.string.male);
            } else {
                tvSex.setText(R.string.female);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a user and opens select dialog afterwards
     */
    public void createUser() {
        startActivity(new Intent(this, CreateUser.class));
        finish();
    }

    /**
     * Removes given user. If that's the only user, the activity to create a now one will be launched.
     * If there were two users, the one left will be selected
     *
     * @param userToRemove user to remove
     * @throws JSONException if something bad happened (should not be the case)
     * @returns false if not successful
     * @returns true if successful
     */
    public boolean removeUser(User userToRemove) throws JSONException {
        SharedPreferences sharedPref = getSharedPreferences("settings", 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        JSONArray users = new JSONArray(sharedPref.getString("users", "[]"));

        for (int i = 0; i < users.length(); i++) {
            JSONObject user = new JSONObject(users.get(i).toString());
            if (user.getString("name").compareTo(userToRemove.getName()) == 0) {
                users.remove(i);
                editor.putString("users", users.toString());
                editor.commit();

                if (users.length() == 0) {
                    createUser();
                }
                if (users.length() == 1) {
                    currentUserAsJSON = new JSONObject(users.get(0).toString());
                    currentUser = new User(currentUserAsJSON);
                    updateGui();
                } else {
                    switchUser();
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Opens activity to edit the current user
     */
    public void editUser() {
        Intent i = new Intent(this, EditUser.class);
        i.putExtra("created", currentUser.getCreated());
        startActivity(i);
    }

    public boolean switchUser() {
        SharedPreferences sharedPref = getSharedPreferences("settings", 0);

        //Ist die Users-datenbank leer, wird ein neuer User erstellt
        if (sharedPref.getString("users", "[]").compareTo("[]") == 0) {
            createUser();
        } else {
            try {
                final JSONArray users = new JSONArray(sharedPref.getString("users", "[]"));
                final String usersAsString[] = new String[users.length()];

                //Wenn nur ein User vorhanden, wird dieser ausgewählt
                if (users.length() == 1) {
                    currentUserAsJSON = new JSONObject(users.get(0).toString());
                    currentUser = new User(currentUserAsJSON);
                    Toast.makeText(this, R.string.only_one_user_there, Toast.LENGTH_SHORT).show();
                    return true;
                }

                for (int i = 0; i < users.length(); i++) {
                    JSONObject user = new JSONObject(users.get(i).toString());
                    usersAsString[i] = user.toString();
                }

                //TODO: custom view dialog
                ListAdapter adapter = new ArrayAdapter<String>(
                        getApplicationContext(), R.layout.dialog_switchuser_item, usersAsString) {

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
                            convertView = inflater.inflate(R.layout.dialog_switchuser_item, null);

                            holder = new ViewHolder();
                            holder.name = (TextView) convertView
                                    .findViewById(R.id.name);
                            convertView.setTag(holder);
                        } else {
                            holder = (ViewHolder) convertView.getTag();
                        }
                        holder.name.setText(usersAsString[position]);

                        return convertView;
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Pick a user");
                builder.setAdapter(adapter,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int item) {
                                try {
                                    JSONObject selectedUser = new JSONObject(users.get(item).toString());
                                    Toast.makeText(MainActivity.this, "You selected: " + selectedUser.getString("name"), Toast.LENGTH_LONG).show();
                                    currentUserAsJSON = selectedUser;
                                    currentUser = new User(currentUserAsJSON);
                                    updateGui();
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

    public void addDrink(User user) {
        // custom dialog
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_add_drink);

        //Add dialog title and center it
        dialog.setTitle(R.string.add_drink);
        final TextView title = (TextView) dialog.findViewById(android.R.id.title);
        if (title != null) {
            title.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            title.setPadding(0, 8, 0, 6);
        }

        // Add predefined mixtures
        MixtureAdapter mixtureAdapter = new MixtureAdapter(this, new ArrayList<Mixture>());
        GridView mixtureList = (GridView) dialog.findViewById(R.id.mixtureList);
        mixtureList.setAdapter(mixtureAdapter);

        for (Mixture aMixtureArray : mixtureArray) {
            mixtureAdapter.add(aMixtureArray);
        }


        Button addDrinkButton = (Button) dialog.findViewById(R.id.addDrink);

        addDrinkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
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
            case R.id.switchUser:
                switchUser();
                break;
            case R.id.editUser:
                editUser();
                break;
            case R.id.removeUser:
                try {
                    System.out.println(currentUser.toString());
                    removeUser(currentUser);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.newUser:
                createUser();
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
        Toast.makeText(this, R.string.press_back_again_to_leave, Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 1700);
    }
}