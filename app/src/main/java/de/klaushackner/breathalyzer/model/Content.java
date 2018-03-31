package de.klaushackner.breathalyzer.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A single content of a Mixture/Drink (e.g. Water, Beer, Vodka, lime slice, ...)
 */

public class Content {

    public String name;
    public double alcContent; // %
    public double amount; // ml


    public Content(String name, double alcContent, double amount) {
        this.name = name;
        this.alcContent = alcContent;
        this.amount = amount;
    }

    public Content(JSONObject content) {
        try {
            this.name = content.getString("name");
            this.alcContent = content.getDouble("alcContent");
            this.amount = content.getDouble("amount");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject toJSON() {
        try {
            JSONObject content = new JSONObject();
            content.put("name", name);
            content.put("alcContent", alcContent);
            content.put("amount", amount);
            return content;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
