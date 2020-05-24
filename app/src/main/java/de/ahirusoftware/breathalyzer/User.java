package de.ahirusoftware.breathalyzer;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

public class User {
    protected String name;
    protected boolean isMale;
    protected int age;
    protected int weight; //kg
    protected int height; //cm
    protected long created;
    protected ArrayList<Drink> drinks;
    protected ArrayList<Drink> depletedDrinks;

    public User(String name, boolean isMale, int age, int weight, int height, long created, ArrayList<Drink> drinks, ArrayList<Drink> depletedDrinks) {
        this.name = name;
        this.isMale = isMale;
        this.age = age;
        this.weight = weight;
        this.height = height;
        this.created = created;
        this.drinks = drinks;
        this.depletedDrinks = depletedDrinks;
    }

    //creating a new User
    public User(String name, boolean isMale, int age, int weight, int height) {
        this.name = name;
        this.isMale = isMale;
        this.age = age;
        this.weight = weight;
        this.height = height;
        this.created = System.currentTimeMillis();
        this.drinks = new ArrayList<>();
        this.depletedDrinks = new ArrayList<>();
    }

    public User(JSONObject user) {
        try {
            this.name = user.getString("name");
            this.isMale = user.getBoolean("isMale");
            this.age = user.getInt("age");
            this.weight = user.getInt("weight");
            this.height = user.getInt("height");
            this.created = user.getLong("created");

            JSONArray drinksJSON = user.getJSONArray("drinks");
            drinks = new ArrayList<>();

            for (int i = 0; i < drinksJSON.length(); i++) {
                drinks.add(new Drink(drinksJSON.getJSONObject(i), this));
            }

            System.out.println(user.getJSONArray("depletedDrinks"));
            JSONArray depletedDrinksJSON = user.getJSONArray("depletedDrinks");
            // Converting from Alkomat 3000 v1.9 and older
            if (depletedDrinksJSON == null) {
                depletedDrinksJSON = new JSONArray();
            } else {
                depletedDrinks = new ArrayList<>();
            }

            for (int i = 0; i < depletedDrinksJSON.length(); i++) {
                depletedDrinks.add(new Drink(depletedDrinksJSON.getJSONObject(i), this));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject toJSON() {
        try {
            JSONObject user = new JSONObject();
            user.put("name", name);
            user.put("isMale", isMale);
            user.put("age", age);
            user.put("weight", weight);
            user.put("height", height);
            user.put("created", created);

            JSONArray drinks = new JSONArray();

            for (Drink d : this.drinks) {
                drinks.put(d.toJSON());
            }

            user.put("drinks", drinks);


            JSONArray depletedDrinks = new JSONArray();
            // Converting from Alkomat 3000 v1.9 and older
            if (this.depletedDrinks != null) {
                for (Drink d : this.depletedDrinks) {
                    depletedDrinks.put(d.toJSON());
                }
            }

            user.put("depletedDrinks", depletedDrinks);

            return user;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String toString() {
        return toJSON().toString();
    }

    public static boolean isValidUser(String name, int age, int height, int weight) {
        Log.i("isValidUser", name + ", " + age + ", " + height + ", " + height);
        return name.length() >= 2 && age > 10 && age < 100 && weight > 30 && weight < 300 && height > 130 && height < 230;
    }

    public void consumeDrink(Mixture m) {
        drinks.add(new Drink(m.name, m.description, System.currentTimeMillis(), m.content, m.image, this));
    }

    public boolean removeDrink(long consumePoint) {
        for (Drink d : drinks) {
            if (d.getConsumePoint() == consumePoint) {
                drinks.remove(d);
                return true;
            }
        }
        for (Drink d : depletedDrinks) {
            if (d.getConsumePoint() == consumePoint) {
                depletedDrinks.remove(d);
                return true;
            }
        }
        return false;
    }

    public boolean removeDrink(Drink d) {
        return removeDrink(d.getConsumePoint());
    }

    /**
     * @param compareTo User to compare with
     * @return true if the compareTo and this user share the same creation date
     */
    public boolean compare(User compareTo) {
        return this.created == compareTo.created;
    }

    public void saveUser(Context c) {
        SharedPreferences sharedPref = c.getSharedPreferences("data", 0);
        SharedPreferences.Editor editor = sharedPref.edit();

        try {
            JSONArray users = new JSONArray(sharedPref.getString("users", "[]"));

            for (int i = 0; i < users.length(); i++) {
                User user = new User(new JSONObject(users.get(i).toString()));
                if (user.compare(this)) {
                    users.put(i, this.toJSON());
                    editor.putString("users", users.toString());
                    editor.commit();
                    return;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static int getUserCount(Context c) {
        SharedPreferences sharedPref = c.getSharedPreferences("data", 0);

        try {
            JSONArray users = new JSONArray(sharedPref.getString("users", "[]"));
            return users.length();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static User getUserByCreated(Context c, long created) {
        SharedPreferences sharedPref = c.getSharedPreferences("data", 0);

        try {
            JSONArray users = new JSONArray(sharedPref.getString("users", "[]"));

            for (int i = 0; i < users.length(); i++) {
                User u = new User(new JSONObject(users.get(i).toString()));
                if (u.created == created) {
                    return u;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}

