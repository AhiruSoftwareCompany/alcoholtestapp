package de.klaushackner.breathalyzer.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Ingredient {
    private String name;
    private double percentage;
    private double amount;

    public Ingredient(String name, double percentage, double amount) {
        this.name = name;
        this.percentage = percentage;
        this.amount = amount;
    }

    public Ingredient(JSONObject ingredientAsJSON) {
        try {
            this.name = ingredientAsJSON.getString("name");
            this.percentage = ingredientAsJSON.getDouble("percentage");
            this.amount = ingredientAsJSON.getDouble("amount");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public double getPercentage() {
        return percentage;
    }

    public double getAmount() {
        return amount;
    }

    public JSONObject toJSON() {
        JSONObject j = new JSONObject();
        try {
            j.put("name", name);
            j.put("percentage", percentage);
            j.put("amount", amount);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return j;
    }

    @Override
    public String toString() {
        return toJSON().toString();
    }

    public boolean compareTo(Ingredient compare) {
        return this.toString().compareTo(compare.toString()) == 0;
    }
}
