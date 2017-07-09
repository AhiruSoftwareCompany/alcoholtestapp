package de.klaushackner.alcoholtest.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static de.klaushackner.alcoholtest.R.string.male;

public class User {
    private String name;
    private boolean isMale;
    private int age;
    private int weight; //kg
    private int height; //cm
    private long created;
    private JSONArray drinks;

    public User(JSONObject userAsJSON) {
        try {
            this.name = userAsJSON.getString("name");
            this.isMale = userAsJSON.getBoolean("isMale");
            this.age = userAsJSON.getInt("age");
            this.weight = userAsJSON.getInt("weight");
            this.height = userAsJSON.getInt("height");
            this.created = userAsJSON.getLong("created");
            this.drinks = userAsJSON.getJSONArray("drinks");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public User(String name, boolean isMale, int age, int weight, int height, long created, JSONArray drinks) {
        this.name = name;
        this.isMale = isMale;
        this.age = age;
        this.weight = weight;
        this.height = height;
        this.created = created;
        this.drinks = drinks;
    }

    @Override
    public String toString() {
        return this.toJSON().toString();
    }

    public JSONObject toJSON() {
        JSONObject user = new JSONObject();
        try {
            user.put("name", name);
            user.put("isMale", isMale);
            user.put("age", age);
            user.put("weight", weight);
            user.put("height", height);
            user.put("created", created);
            user.put("drinks", drinks);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }

    public String getName() {
        return name;
    }

    public boolean isMale() {
        return isMale;
    }

    public int getAge() {
        return age;
    }

    /**
     * @return weight in kilogram
     */
    public int getWeight() {
        return weight;
    }

    /**
     * @return height in centimeter
     */
    public int getHeight() {
        return height;
    }

    public long getCreated() {
        return created;
    }

    public static boolean isValidUser(String name, int age, int height, int weight) {
        return name.length() > 2 && age > 10 && age < 100 && weight > 30 && weight < 200 && height > 100 && height < 230;
    }

    public boolean compareTo(User toCompare) {
        if (this.getCreated() == toCompare.getCreated()) {
            return true;
        }
        return false;
    }

    public void addDrink(long takingTime, Mixture m) {
        try {
            JSONArray toPut = new JSONArray();
            toPut.put(0, takingTime);
            toPut.put(1, m.toString());
            drinks.put(toPut);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONArray getDrinks() {
        if (drinks.length() > 0) {
            return drinks;
        } else {
            return new JSONArray();
        }
    }

    /**
     * taking time = System.currentTimeMillis();
     *
     * @param m
     */
    public void addDrink(Mixture m) {
        try {
            JSONArray toPut = new JSONArray();
            toPut.put(0, System.currentTimeMillis());
            toPut.put(1, m.toString());
            drinks.put(toPut);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void saveUser(Context c) {
        SharedPreferences sharedPref = c.getSharedPreferences("data", 0);
        SharedPreferences.Editor editor = sharedPref.edit();

        try {
            JSONArray users = new JSONArray(sharedPref.getString("users", "[]"));

            for (int i = 0; i < users.length(); i++) {
                User u = new User(new JSONObject(users.get(i).toString()));
                if (u.compareTo(this)) {
                    users.put(i, this.toString());
                    editor.putString("users", users.toString());
                    editor.commit();
                    return;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void removeDrink(long takingTime) {
        try {
            for (int i = 0; i < drinks.length(); i++) {
                JSONArray array = new JSONArray(drinks.get(i).toString());
                long t = array.getLong(0);
                if (t == takingTime) {
                    drinks.remove(i);
                    return;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
