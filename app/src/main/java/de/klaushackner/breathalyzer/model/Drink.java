package de.klaushackner.breathalyzer.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A consumed mixture
 */

public class Drink {
    public String name;
    public Recipe r;
    public long takingTime;

    public Drink(Recipe r, User u) {
        this.name = r.name;
        this.r = r;
        this.takingTime = System.currentTimeMillis();
    }

    public Drink(Recipe r, User u, long takingTime) {
        this.name = r.name;
        this.r = r;
        this.takingTime = takingTime;
    }

    public Drink(JSONObject j) {
        try {
            this.name = j.getString("name");
            this.r = new Recipe(j.getJSONObject("recipe"));
            this.takingTime = j.getLong("takingTime");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject toJSON(){
        try {
            return new JSONObject().put("name", name).put("recipe", r.toJSON().put( "takingTime", takingTime));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param u Current User
     * @return expire time of this drink for this user
     */
    public long expireTime(User u){
        double bac = r.getBac(u); //alcohol content
        return takingTime + Math.round(bac * 3600000);
    }
}
