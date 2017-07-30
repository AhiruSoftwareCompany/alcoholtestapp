package de.klaushackner.breathalyzer.model;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Mixture {
    private String name;
    private double amount;
    private double percentage;
    private MixtureImage image;

    public Mixture(String name, double amount, double percentage, MixtureImage image) {
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
            this.image = MixtureImage.fromString(mixtureAsJSON.getString("image"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String toString() {
        return toJSON().toString();
    }

    public JSONObject toJSON() {
        JSONObject j = new JSONObject();
        try {
            j.put("name", name);
            j.put("amount", amount);
            j.put("percentage", percentage);
            j.put("image", image.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return j;
    }

    public double getAmount() {
        return amount;
    }

    public double getPercentage() {
        return percentage;
    }

    public MixtureImage getImage() {
        return image;
    }

    public String getImageString() {
        return image.toString();
    }

    public String getName() {
        return name;
    }

    public static double getBac(Mixture mixture, User user) {
        double r;

        if (user.isMale()) {
            double R = 2.447 - 0.09516 * user.getAge() + 0.1074 * user.getHeight() + 0.3362 * user.getWeight();
            r = (1.055 * R) / (0.8 * user.getWeight());
        } else {
            double R = -2.097 + 0.1069 * user.getHeight() + 0.2466 * user.getWeight();
            r = (1.055 * R) / (0.8 * user.getWeight());
        }

        return (mixture.getAmount() * mixture.getPercentage() * 0.8) / (user.getWeight() * r);
    }

    public static boolean isValidMixture(String name, double amount, double percentage) {
        return name.length() > 2 && amount > 1 && amount < 3000 && percentage > 0.01 && percentage < 0.99;
    }

    public static Mixture[] getMixtureArray(Context c, User u) {
        try {
            JSONArray customMixtures = getCustomMixtures(c);

            //Add new mixtures here!
            JSONArray mixtures = new JSONArray("[]");
            mixtures.put(new Mixture("Bier", 500, 0.05, MixtureImage.beer).toString());
            mixtures.put(new Mixture("Bier", 1000, 0.05, MixtureImage.morebeer).toString());
            mixtures.put(new Mixture("Goaß", 270, 0.06296296296, MixtureImage.goass).toString());
            mixtures.put(new Mixture("Goaßmaß", 540, 0.06296296296, MixtureImage.goass).toString());
            mixtures.put(new Mixture("Pils", 330, 0.048, MixtureImage.pils).toString());
            mixtures.put(new Mixture("Pils", 500, 0.048, MixtureImage.pils).toString());
            mixtures.put(new Mixture("Wein", 200, 0.10, MixtureImage.wine).toString());
            mixtures.put(new Mixture("Wodka", 20, 0.30, MixtureImage.vodka).toString());
            mixtures.put(new Mixture("Wodka", 20, 0.40, MixtureImage.vodka).toString());
            mixtures.put(new Mixture("Whisky", 20, 0.40, MixtureImage.whisky).toString());
            mixtures.put(new Mixture("Sekt", 200, 0.12, MixtureImage.sparklingwine).toString());

            for (int i = 0; i < customMixtures.length(); i++) {
                mixtures.put(customMixtures.get(i).toString());
            }

            if (u.getName().compareTo("Franzi") == 0) {
                mixtures.put(new Mixture("Eigenes\nGetränk", 0, 0, MixtureImage.custom_panda).toString());
            } else {
                mixtures.put(new Mixture("Eigenes\nGetränk", 0, 0, MixtureImage.custom).toString());
            }


            final Mixture[] mixtureArray = new Mixture[mixtures.length()];

            for (int i = 0; i < mixtures.length(); i++) {
                mixtureArray[i] = new Mixture(new JSONObject(mixtures.get(i).toString()));
            }

            return mixtureArray;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new Mixture[0];
    }

    public static JSONArray getCustomMixtures(Context c) {
        try {
            SharedPreferences sharedPref = c.getSharedPreferences("data", 0);
            return new JSONArray(sharedPref.getString("customMixtures", "[]"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new JSONArray();
    }

    public static void addCustomMixture(Context c, Mixture m) {
        try {
            SharedPreferences sharedPref = c.getSharedPreferences("data", 0);
            SharedPreferences.Editor editor = sharedPref.edit();

            JSONArray mixtures = new JSONArray(sharedPref.getString("customMixtures", "[]"));
            mixtures.put(m.toString());

            editor.putString("customMixtures", mixtures.toString());
            editor.commit();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public static void removeCustomMixture(Context c, Mixture m) {
        try {
            SharedPreferences sharedPref = c.getSharedPreferences("data", 0);
            SharedPreferences.Editor editor = sharedPref.edit();

            JSONArray mixtures = new JSONArray(sharedPref.getString("customMixtures", "[]"));

            for (int i = 0; i < mixtures.length(); i++) {
                if (mixtures.get(i).toString().compareTo(m.toString()) == 0) {
                    mixtures.remove(i);
                    break; //break prevents removing every mixture matching to the given mixture (helpful if you addCustomRecipe the same mixture two times and want to remove it)
                }
            }

            editor.putString("customMixtures", mixtures.toString());
            editor.commit();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

