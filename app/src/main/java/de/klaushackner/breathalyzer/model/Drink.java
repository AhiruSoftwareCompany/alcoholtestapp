package de.klaushackner.breathalyzer.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Drink {
    private User user;
    private Mixture mixture;
    private long takingTime;
    private double bac;
    private long expireTime;
    public static double depletingFactor = 1; //0.1 per unit
    public static double depletingFactorPerHour = (depletingFactor / 10) / 3600000; // e.g 0.1/3600000

    public Drink(User user, Mixture mixture, long takingTime, long expireTime) {
        this.user = user;
        this.mixture = mixture;
        this.takingTime = takingTime;
        this.expireTime = expireTime;
    }

    public Drink(JSONObject drinkAsJSON) {
        try {
            this.user = new User(new JSONObject(drinkAsJSON.get("user").toString()));
            this.expireTime = drinkAsJSON.getLong("expireTime");
            this.mixture = new Mixture(new JSONObject(drinkAsJSON.get("mixture").toString()));
            this.takingTime = drinkAsJSON.getLong("takingTime");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }

    public void setBac(double bac) {
        this.bac = bac;
    }

    public double getBac() {
        return bac;
    }

    public User getUser() {
        return user;
    }

    public Mixture getMixture() {
        return mixture;
    }

    public long getTakingTime() {
        return takingTime;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public double getRelativeBac() {
        if ((expireTime - System.currentTimeMillis()) > 0) {
            long elapsed = System.currentTimeMillis() - takingTime;
            return bac - (elapsed * depletingFactorPerHour);
        }
        return 0;
    }
}
