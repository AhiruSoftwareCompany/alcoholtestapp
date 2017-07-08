package de.klaushackner.alcoholtest;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

import de.klaushackner.alcoholtest.adapter.DrinkAdapter;
import de.klaushackner.alcoholtest.adapter.MixtureAdapter;
import de.klaushackner.alcoholtest.adapter.UserAdapter;
import de.klaushackner.alcoholtest.model.Drink;
import de.klaushackner.alcoholtest.model.Mixture;
import de.klaushackner.alcoholtest.model.User;

public class MainActivity extends AppCompatActivity {

    private boolean doubleBackToExitPressedOnce;
    private MainActivity ma;
    private Context c;
    private User currentUser;
    private TextView tvName;
    private TextView tvAge;
    private TextView tvWeight;
    private TextView tvHeight;
    private TextView tvSex;
    private TextView tvBac;
    private DrinkAdapter dA;
    private final DecimalFormat format = new DecimalFormat();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        c = getApplicationContext();
        ma = this;

        tvName = (TextView) findViewById(R.id.name);
        tvAge = (TextView) findViewById(R.id.age);
        tvWeight = (TextView) findViewById(R.id.weight);
        tvHeight = (TextView) findViewById(R.id.height);
        tvSex = (TextView) findViewById(R.id.sex);
        Button btnAddDrink = (Button) findViewById(R.id.add_drink_button);
        ListView drinks = (ListView) findViewById(R.id.drinks);
        tvBac = (TextView) findViewById(R.id.bac);

        btnAddDrink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDrink();
            }
        });

        dA = new DrinkAdapter(this, new ArrayList<Drink>());
        drinks.setAdapter(dA);
        drinks.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    final int pos = position;
                    SharedPreferences sharedPref = getSharedPreferences("data", 0);
                    final SharedPreferences.Editor editor = sharedPref.edit();
                    final JSONArray mixtures;

                    mixtures = new JSONArray(sharedPref.getString("mixturesToUser", "[]"));
                    AlertDialog.Builder builder = new AlertDialog.Builder(ma);
                    builder.setTitle(R.string.remove_drink_question);
                    builder.setPositiveButton(R.string.remove_drink, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            mixtures.remove(pos);
                            editor.putString("mixturesToUser", mixtures.toString());
                            editor.commit();
                            updateGui();
                        }
                    });
                    builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                    return true;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return false;
            }
        });

        ImageButton refresh = (ImageButton) findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateGui();
            }
        });

        format.setDecimalSeparatorAlwaysShown(false);
        switchUser(isStartedByLauncher());
    }

    protected boolean isStartedByLauncher() {
        if (getIntent() == null) {
            return false;
        }
        boolean isActionMain = Intent.ACTION_MAIN.equals(getIntent().getAction());
        Set<String> categories = getIntent().getCategories();
        boolean isCategoryLauncher = categories != null && categories.contains(Intent.CATEGORY_LAUNCHER);
        return isActionMain && isCategoryLauncher;
    }

    /**
     * Updates gui after app is paused
     */
    @Override
    public void onResume() {
        super.onResume();
        updateGui();
    }

    /**
     * Updates gui on startup
     */
    @Override
    public void onStart() {
        super.onStart();
        updateGui();
    }

    /**
     * Updates GUI to match the current selected user, then calls updateDrinkList()
     */
    private void updateGui() {
        if (currentUser != null) {
            tvName.setText(currentUser.getName());
            tvAge.setText(format.format(currentUser.getAge()) + " " + getString(R.string.years));
            tvWeight.setText(format.format(currentUser.getWeight()) + " kg");
            tvHeight.setText(format.format(currentUser.getHeight()) + " cm");

            if (currentUser.isMale()) {
                tvSex.setText(R.string.male);
            } else {
                tvSex.setText(R.string.female);
            }
            updateDrinkList();
        }
    }

    /**
     * Adds drinks from the current person to the list view
     * Calculates current persons blood alcohol content (BAC or in German: BAK)
     * Only called by updateGui();
     */
    private void updateDrinkList() {
        SharedPreferences sharedPref = getSharedPreferences("data", 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        dA.clear();
        dA.notifyDataSetChanged();
        double currentBac = 0;
        long lastExpireDuration = 0;

        try {
            JSONArray mixtures = new JSONArray(sharedPref.getString("mixturesToUser", "[]"));
            if (mixtures.length() > 0) {
                for (int i = 0; i < mixtures.length(); i++) {
                    JSONArray j = new JSONArray(mixtures.get(i).toString());  //0 = timestamp, 1 = user, 2 = mixture
                    long takingTime = Long.valueOf(j.get(0).toString());
                    User user = new User(new JSONObject(j.get(1).toString()));
                    Mixture mixture = new Mixture(new JSONObject(j.get(2).toString()));

                    if (user.compareTo(currentUser)) {
                        double bac = Mixture.getBac(mixture, currentUser); //alcohol content

                        long expireTime = lastExpireDuration + takingTime + Math.round(bac * 36000000); // 0,1 promille pro Stunde wird abgebaut
                        lastExpireDuration = expireTime - takingTime;

                        final Drink d = new Drink(user, mixture, takingTime, expireTime);

                        if (new Date().getTime() < expireTime) {
                            d.setBac(bac);
                            currentBac += d.getBac();
                            dA.add(d);
                        } else {
                            mixtures.remove(i);
                        }
                    }
                }
            }

            editor.putString("mixturesToUser", mixtures.toString());
            editor.commit();
            tvBac.setText(format.format(currentBac));
            createNotification(currentBac);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a user and opens select dialog afterwards
     */
    private void createUser() {
        startActivity(new Intent(this, CreateUser.class));
    }

    /**
     * Remove given user. If that's the only user, the activity to create a new one will be launched.
     * If there were two users, the one left will be selected
     *
     * @param userToRemove user to remove
     */
    private void removeUser(User userToRemove) {
        SharedPreferences sharedPref = getSharedPreferences("data", 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        JSONArray users = null;
        try {
            users = new JSONArray(sharedPref.getString("users", "[]"));

            for (int i = 0; i < users.length(); i++) {
                JSONObject user = new JSONObject(users.get(i).toString());
                if (user.getString("name").compareTo(userToRemove.getName()) == 0) {
                    users.remove(i);
                    editor.putString("users", users.toString());
                    editor.commit();

                    if (users.length() == 0) {
                        createUser();
                        return;
                    }

                    if (users.length() == 1) {
                        currentUser = new User(new JSONObject(users.get(0).toString()));
                        updateGui();
                    } else {
                        switchUser(false);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens activity to edit the current user
     */
    private void editUser() {
        Intent i = new Intent(this, EditUser.class);
        i.putExtra("created", currentUser.getCreated());
        startActivity(i);
    }

    /**
     * Switches between users. If there's no user, a new one will be created. If only one user exists, a toast will be shown
     *
     * @param fromStart If this method is called in the onCreate() method, the "only one user existing" message will be suppressed.
     */
    private void switchUser(boolean fromStart) {
        try {
            SharedPreferences sharedPref = getSharedPreferences("data", 0);
            final SharedPreferences.Editor editor = sharedPref.edit();

            //Ist die Users-datenbank leer, wird ein neuer User erstellt
            if (sharedPref.getString("users", "[]").compareTo("[]") == 0) {
                createUser();
            } else {
                final JSONArray users = new JSONArray(sharedPref.getString("users", "[]"));

                //Select last user on startup
                if (fromStart) {
                    long lastUser = sharedPref.getLong("lastUser", 0);

                    if (lastUser != 0) {
                        for (int i = 0; i < users.length(); i++) {
                            User u = new User(new JSONObject(users.get(i).toString()));
                            if (u.getCreated() == lastUser) {
                                currentUser = u;
                                editor.putLong("lastUser", u.getCreated());
                                editor.commit();
                                return;
                            }
                        }
                    }
                }

                //Wenn nur ein User vorhanden, wird dieser ausgewählt
                if (users.length() == 1) {
                    currentUser = new User(new JSONObject(users.get(0).toString()));
                    if (!fromStart) {
                        Toast.makeText(this, R.string.only_one_user_there, Toast.LENGTH_SHORT).show();
                    }

                    editor.putLong("lastUser", currentUser.getCreated());
                    editor.commit();
                    return;
                }

                final ArrayList<User> usersList = new ArrayList<>();
                for (int i = 0; i < users.length(); i++) {
                    usersList.add(new User(new JSONObject(users.get(i).toString())));
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(R.string.pick_user);
                builder.setCancelable(false);
                builder.setNeutralButton(R.string.add_user, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        createUser();
                    }
                });

                builder.setAdapter(new UserAdapter(getApplicationContext(), usersList),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int item) {
                                currentUser = usersList.get(item);
                                //For some reason "R.string.you_selected" doesn't work anymore, maybe because I changed MainActivity.this to c
                                Toast.makeText(c, getResources().getString(R.string.you_selected) + " " + currentUser.getName(), Toast.LENGTH_LONG).show();
                                editor.putLong("lastUser", currentUser.getCreated());
                                editor.commit();

                                updateGui();
                                dialog.dismiss();
                            }
                        });

                Dialog dialog = builder.create();
                dialog.show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds mixtures to dialog and handles its events
     * Here you can add more mixtures
     */
    private void addDrink() {
        try {
            SharedPreferences sharedPref = getSharedPreferences("data", 0);
            SharedPreferences.Editor editor = sharedPref.edit();
            JSONArray mixtures = new JSONArray(sharedPref.getString("mixtures", "[]"));

            if (mixtures.length() < 8) {
                //Updating mixtures
                //Add new mixtures here!
                mixtures = new JSONArray("[]");
                mixtures.put(new Mixture("Bier", 500, 0.05, "beer").toString());
                mixtures.put(new Mixture("Bier", 1000, 0.05, "morebeer").toString());
                mixtures.put(new Mixture("Pils", 330, 0.048, "pils").toString());
                mixtures.put(new Mixture("Pils", 500, 0.048, "pils").toString());
                mixtures.put(new Mixture("Wein", 200, 0.10, "wine").toString());
                mixtures.put(new Mixture("Wodka", 20, 0.40, "vodka").toString());
                mixtures.put(new Mixture("Whisky", 20, 0.40, "whisky").toString());
                mixtures.put(new Mixture("Sekt", 200, 0.12, "sparklingwine").toString());

                if (currentUser.getName().compareTo("Franzi") == 0) {
                    mixtures.put(new Mixture("Eigenes\nGetränk", 0, 0, "custom_franzi").toString());
                } else {
                    mixtures.put(new Mixture("Eigenes\nGetränk", 0, 0, "custom").toString());
                }

                editor.putString("drinks", mixtures.toString());
                editor.commit();
            }

            final Mixture[] mixtureArray = new Mixture[mixtures.length()];

            for (int i = 0; i < mixtures.length(); i++) {
                mixtureArray[i] = new Mixture(new JSONObject(mixtures.get(i).toString()));
            }

            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.dialog_add_drink);

            dialog.setTitle(R.string.add_drink);
            final TextView title = (TextView) dialog.findViewById(android.R.id.title);
            if (title != null) {
                title.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                title.setPadding(0, 20, 0, 20);
            }

            final MixtureAdapter mixtureAdapter = new MixtureAdapter(this, new ArrayList<Mixture>());
            GridView mixtureList = (GridView) dialog.findViewById(R.id.mixtureList);
            mixtureList.setAdapter(mixtureAdapter);

            for (Mixture aMixtureArray : mixtureArray) {
                mixtureAdapter.add(aMixtureArray);
            }

            mixtureList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {
                    try {
                        SharedPreferences sharedPref = getSharedPreferences("data", 0);
                        SharedPreferences.Editor editor = sharedPref.edit();

                        if (mixtureArray[position].getPercentage() == 0) {
                            addCustomDrink(0, null);
                            dialog.dismiss();
                            return;
                        }

                        JSONArray mixtures = new JSONArray(sharedPref.getString("mixturesToUser", "[]"));

                        //0 = user, 1 = mixture
                        JSONArray toPut = new JSONArray();
                        toPut.put(System.currentTimeMillis());
                        toPut.put(currentUser.toString());
                        toPut.put(mixtureArray[position].toString()); //selected mixture
                        mixtures.put(toPut);

                        editor.putString("mixturesToUser", mixtures.toString());
                        editor.commit();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    dialog.dismiss();
                    updateDrinkList();
                }
            });

            dialog.show();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void createNotification(double currentBac) {
        if (currentBac > 0) {
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                    this).setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Obacht")
                    .setContentText(String.format(getString(R.string.current_bac), format.format(currentBac)));
            //TODO: replace $s in string resource with $d
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            // notificationID = ID, um die Benachrichtigung später nochmal zu bearbeiten
            mNotificationManager.notify(1, mBuilder.build());
        } else {
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.cancel(1);
        }
    }


    /**
     * @param stage         0 = from "add drinks" dialog; 1 = from "add custom drink" dialog
     * @param customMixture from "add custom drink" dialog
     */
    private void addCustomDrink(int stage, Mixture customMixture) {
        switch (stage) {
            case 0:
                //Open dialog and wait for result
                final Dialog d = new Dialog(this);
                d.setContentView(R.layout.dialog_add_custom_drink);

                ImageView image = (ImageView) d.findViewById(R.id.image);
                if (currentUser.getName().compareTo("Franzi") == 0) {
                    image.setImageResource(getResources().getIdentifier("custom_franzi", "drawable",
                            getApplicationContext().getPackageName()));
                }

                Button addDrink = (Button) d.findViewById(R.id.addDrink);
                addDrink.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TextView tvName = (TextView) d.findViewById(R.id.name);
                        TextView tvAmount = (TextView) d.findViewById(R.id.amount);
                        TextView tvPercentage = (TextView) d.findViewById(R.id.percentage);
                        String name = tvName.getText().toString();
                        double amount = Double.parseDouble("0" + tvAmount.getText());
                        double percentage = Double.parseDouble("0" + tvPercentage.getText()) / 100;

                        if (name.length() > 2 && amount > 1 && amount < 3000 && percentage > 0.01 && percentage < 0.99) {
                            if (currentUser.getName().compareTo("Franzi") == 0) {
                                addCustomDrink(1, new Mixture(name, amount, percentage, "custom_franzi"));
                            } else {
                                addCustomDrink(1, new Mixture(name, amount, percentage, "custom"));
                            }
                            d.dismiss();
                        } else {
                            Toast.makeText(c, c.getResources().getString(R.string.wronginput), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                d.show();
                break;
            case 1:
                try {
                    if (customMixture != null || currentUser != null) {
                        SharedPreferences sharedPref = getSharedPreferences("data", 0);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        JSONArray mixtures = new JSONArray(sharedPref.getString("mixturesToUser", "[]"));

                        //0 = user, 1 = mixture
                        JSONArray toPut = new JSONArray();
                        toPut.put(System.currentTimeMillis());
                        toPut.put(currentUser.toString());
                        toPut.put(customMixture != null ? customMixture.toString() : null); //selected mixture
                        mixtures.put(toPut);

                        editor.putString("mixturesToUser", mixtures.toString());
                        editor.commit();
                        updateDrinkList();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
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
    public boolean onPrepareOptionsMenu(Menu menu) {
        SharedPreferences sharedPref = getSharedPreferences("data", 0);
        final JSONArray users;
        try {
            users = new JSONArray(sharedPref.getString("users", "[]"));
            if (users.length() == 1) {
                menu.removeItem(R.id.switchUser);
            }
        } catch (JSONException e) {
            e.printStackTrace();
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
                createUser();
                break;
            case R.id.about:
                final Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.dialog_about);

                //Add dialog title and center it
                dialog.setTitle(R.string.about);
                final TextView title = (TextView) dialog.findViewById(android.R.id.title);
                title.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                title.setPadding(0, 32, 0, 4);

                dialog.show();
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

