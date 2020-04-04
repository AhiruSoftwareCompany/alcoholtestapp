package de.klaushackner.breathalyzer;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.klaushackner.breathalyzer.model.Content;

/**
 * A consumed mixture bound to user
 */

public class Drink implements Comparable<Drink> {
    private String name;
    private String description;
    private long consumePoint;
    private long depletionPoint; //calculated by MainActivity.updateDrinkList()
    private Content[] content;
    private MixtureImage mixtureImage;
    private long depletingDuration;
    private User consumer;
    public static long DEPLETINGFACTOR = 1; //0.1 per unit

    public Drink(String name, String description, long consumePoint, Content[] content, MixtureImage mixtureImage, User consumer) {
        this.name = name;
        this.description = description;
        this.consumePoint = consumePoint;
        this.content = content;
        this.mixtureImage = mixtureImage;
        this.consumer = consumer;
        depletingDuration = getDepletingDuration();
    }

    public Drink(JSONObject drink, User consumer) {
        try {
            this.name = drink.getString("name");
            this.description = drink.getString("description");
            this.consumePoint = drink.getLong("consumePoint");
            this.mixtureImage = MixtureImage.fromString(drink.getString("mixtureImage"));

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
            drink.put("mixtureImage", mixtureImage.toString());

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
        return Math.round(getBac() * Drink.DEPLETINGFACTOR * 36000000); //deplete 0.1 per-mill alcohol per hour
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
        return (getAmount() * getAlcContent() * 0.8) / (consumer.weight * r);
    }

    public double getRelativeBac() {
        double R;

        if (consumer.isMale) {
            R = 2.447 - 0.09516 * consumer.age + 0.1074 * consumer.height + 0.3362 * consumer.weight;
        } else {
            R = -2.097 + 0.1069 * consumer.height + 0.2466 * consumer.weight;
        }

        double r = (1.055 * R) / (0.8 * consumer.weight);
        double bac = (getAmount() * getAlcContent() * 0.8) / (consumer.weight * r);

        double relativeFactor = (depletionPoint - System.currentTimeMillis()) / (double) getDepletingDuration();
        return bac * relativeFactor;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public long getConsumePoint() {
        return consumePoint;
    }

    public long getDepletionPoint() {
        return depletionPoint;
    }

    public Content[] getContent() {
        return content;
    }

    public MixtureImage getMixtureImage() {
        return mixtureImage;
    }

    // From JavaDoc: a negative integer, zero, or a positive integer as this object
    // is less than, equal to, or greater than the specified object.
    @Override
    public int compareTo(@NonNull Drink o) {
        if (o.getConsumePoint() == this.getConsumePoint()) {
            return 0;
        }
        if (o.getConsumePoint() > this.getConsumePoint()) {
            return 1;
        }
        return -1;
    }
}
