package de.klaushackner.breathalyzer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.klaushackner.breathalyzer.model.Content;

/**
 * A consumed mixture bound to user
 */

public class Drink {
    protected String name;
    protected String description;
    protected long consumePoint;
    protected long depletionPoint; //calculated by MainActivity.updateDrinkList()
    protected Content[] content;

    protected long depletingDuration;
    private User consumer;
    public static long depletingFactor = 1; //0.1 per unit

    public Drink(String name, String description, long consumePoint, Content[] content, User consumer) {
        this.name = name;
        this.description = description;
        this.consumePoint = consumePoint;
        this.content = content;
        this.consumer = consumer;
        depletingDuration = getDepletingDuration();
    }

    public Drink(JSONObject drink, User consumer) {
        try {
            this.name = drink.getString("name");
            this.description = drink.getString("description");
            this.consumePoint = drink.getLong("consumePoint");

            JSONArray contentJSON = drink.getJSONArray("content");
            Content[] content = new Content[contentJSON.length()];

            for (int i = 0; i < contentJSON.length(); i++) {
                content[i] = new Content(contentJSON.getJSONObject(i));
            }

            this.content = content;
            this.consumer = consumer;

            depletingDuration = getDepletingDuration();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setDepletionPoint(long depletionPoint) {
        this.depletionPoint = depletionPoint;
    }

    public JSONObject toJSON() {
        try {
            JSONObject drink = new JSONObject();
            drink.put("name", name);
            drink.put("description", description);
            drink.put("consumePoint", consumePoint);

            JSONArray content = new JSONArray();

            for (Content c : this.content) {
                content.put(c.toJSON());
            }

            drink.put("content", content);
            return drink;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public long getDepletingDuration() {
        return Math.round(getBac() * Drink.depletingFactor * 36000000); //deplete 0.1 per-mill alcohol per hour
    }

    public double getAlcContent() {
        double totalAlcAmount = 0;

        for (int i = 0; i < content.length; i++) {
            totalAlcAmount = totalAlcAmount + content[i].alcContent * content[i].amount;
        }

        return totalAlcAmount / getAmount();
    }

    public double getAmount() {
        double totalAmount = 0;

        for (int i = 0; i < content.length; i++) {
            totalAmount = totalAmount + content[i].amount;
        }

        return totalAmount;
    }

    public double getBac() {
        double R;

        if (consumer.isMale) {
            R = 2.447 - 0.09516 * consumer.age + 0.1074 * consumer.height + 0.3362 * consumer.weight;
        } else {
            R = -2.097 + 0.1069 * consumer.height + 0.2466 * consumer.weight;
        }

        double r = (1.055 * R) / (0.8 * consumer.weight);
        System.out.println((getAmount() * getAlcContent() * 0.8) / (consumer.weight * r));
        return (getAmount() * getAlcContent() * 0.8) / (consumer.weight * r);
    }


}
