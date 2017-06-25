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

import alcoholtest.com.alcoholtest.model.User;

public class MainActivity extends AppCompatActivity {

    private boolean doubleBackToExitPressedOnce;
    //TODO: Get rid of json here
    private JSONObject currentUserAsJSON = null;
    private User currentUser;
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

        if (switchUser(false) && currentUserAsJSON != null  /* || currentUserAsJSON.toString().compareTo("[]") != 0 */) {
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
            tvName.setText(currentUserAsJSON.getString("name"));
            tvAge.setText(currentUserAsJSON.getDouble("age") + " Jahre");
            tvWeight.setText(currentUserAsJSON.getDouble("weight") + " kg");
            tvHeight.setText(currentUserAsJSON.getDouble("height") + " cm");

            if (currentUserAsJSON.getBoolean("isMale")) {
                tvSex.setText("männlich");
            } else {
                tvSex.setText("weiblich");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

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
                    switchUser(false);
                }
                if (users.length() == 1) {
                    currentUserAsJSON = new JSONObject(users.get(0).toString());
                    currentUser = new User(currentUserAsJSON);
                    setGui();
                } else {
                    switchUser(true);
                }

                return true;
            }
        }

        //Wenn nur noch ein Eintrag vorhanden ist, soll die MainActivity neu gezeichnet werden. Falls der einzige Eintrag gelöscht wurde, soll eine neue user erstellt werden


        return false;

    }

    public void edituser() {
        Intent i = new Intent(this, EditUser.class);
        i.putExtra("created", currentUser.getCreated());
        startActivity(i);
    }

    public void createUser() {
        startActivity(new Intent(this, CreateUser.class));
        finish();
    }

    public boolean switchUser(boolean fromMenu) {
        SharedPreferences sharedPref = getSharedPreferences("settings", 0);

        //Ist die Users-datenbank leer
        if (sharedPref.getString("users", null) == null || sharedPref.getString("users", null).toString().compareTo("[]") == 0) {
            createUser();
        } else {
            String usersString[];

            try {
                final JSONArray users = new JSONArray(sharedPref.getString("users", "[]"));

                //Das tut so weh :c
                usersString = new String[users.length() + 1];

                if (users.length() == 1 && !fromMenu) {
                    currentUserAsJSON = new JSONObject(users.get(0).toString());
                    currentUser = new User(currentUserAsJSON);
                    return true;
                }

                for (int i = 0; i < users.length(); i++) {
                    JSONObject user = new JSONObject(users.get(i).toString());
                    usersString[i] = user.toString();

                }

                usersString[usersString.length - 1] = ma.getResources().getText(R.string.add_user) + "";

                final String[] pS = usersString;

                ListAdapter adapter = new ArrayAdapter<String>(
                        getApplicationContext(), R.layout.dialog_switchuser_item, usersString) {

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
                            // view already defined, retrieve view holder
                            holder = (ViewHolder) convertView.getTag();
                        }
                        holder.name.setText(pS[position]);

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
                                if (pS[item].compareTo(ma.getResources().getText(R.string.add_user) + "") == 0) {
                                    startActivity(new Intent(MainActivity.this, CreateUser.class));
                                    finish();
                                }

                                try {
                                    JSONObject selecteduser = new JSONObject(users.get(item).toString());
                                    Toast.makeText(MainActivity.this, "You selected: " + selecteduser.getString("name"), Toast.LENGTH_LONG).show();
                                    currentUserAsJSON = selecteduser;
                                    currentUser = new User(currentUserAsJSON);
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
            case R.id.switchUser:
                switchUser(true);
                break;
            case R.id.editUser:
                edituser();
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
                switchUser(false);
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