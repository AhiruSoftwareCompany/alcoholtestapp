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

    public String getCreatedAsString() {
        return "" + created;
    }

    @Override
    public String toString() {
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
        return user.toString();
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

}
