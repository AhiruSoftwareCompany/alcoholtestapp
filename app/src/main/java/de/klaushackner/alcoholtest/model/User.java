package de.klaushackner.alcoholtest.model;

import org.json.JSONException;
import org.json.JSONObject;

public class User {
    private String name;
    private boolean isMale;
    private int age;
    private int weight; //kg
    private int height; //cm
    private long created;

    public User(JSONObject userAsJSON) {
        try {
            this.name = userAsJSON.getString("name");
            this.isMale = userAsJSON.getBoolean("isMale");
            this.age = userAsJSON.getInt("age");
            this.weight = userAsJSON.getInt("weight");
            this.height = userAsJSON.getInt("height");
            this.created = userAsJSON.getLong("created");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public User(String name, boolean isMale, int age, int weight, int height, long created) {
        this.name = name;
        this.isMale = isMale;
        this.age = age;
        this.weight = weight;
        this.height = height;
        this.created = created;
    }

    public String getCreatedAsString() {
        return "" + created;
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
        if (toCompare.toString().compareTo(this.toString()) == 0) {
            return true;
        }
        return false;
    }

}
