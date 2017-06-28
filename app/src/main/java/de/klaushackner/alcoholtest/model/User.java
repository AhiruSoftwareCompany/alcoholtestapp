package de.klaushackner.alcoholtest.model;

import org.json.JSONException;
import org.json.JSONObject;

public class User {
    private String name;
    private boolean isMale;
    private double age;
    private double weight;
    private double height;
    private long created;

    public User(JSONObject userAsJSON) {
        try {
            this.name = userAsJSON.getString("name");
            this.isMale = userAsJSON.getBoolean("isMale");
            this.age = userAsJSON.getDouble("age");
            this.weight = userAsJSON.getDouble("weight");
            this.height = userAsJSON.getDouble("height");
            this.created = userAsJSON.getLong("created");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public User(String name, boolean isMale, double age, double weight, double height, long created) {
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

    public double getAge() {
        return age;
    }

    public double getWeight() {
        return weight;
    }

    public double getHeight() {
        return height;
    }

    public long getCreated() {
        return created;
    }

}
