package de.klaushackner.alcoholtest.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Mixture {
    private String name;
    private double amount;
    private double percentage;
    private String image;

    public Mixture(String name, double amount, double percentage, String image) {
        this.amount = amount;
        this.name = name;
        this.percentage = percentage;
        this.image = image;
    }

    public Mixture(JSONObject mixtureAsJSON) {
        try {
            this.name = mixtureAsJSON.getString("name");
            this.amount = mixtureAsJSON.getDouble("amount");
            this.percentage = mixtureAsJSON.getDouble("percentage");
            this.image = mixtureAsJSON.getString("image");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String toString() {
        JSONObject j = new JSONObject();
        try {
            j.put("name", name);
            j.put("amount", amount);
            j.put("percentage", percentage);
            j.put("image", image);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return j.toString();
    }

    public double getAmount() {
        return amount;
    }

    public double getPercentage() {
        return percentage;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }
}
