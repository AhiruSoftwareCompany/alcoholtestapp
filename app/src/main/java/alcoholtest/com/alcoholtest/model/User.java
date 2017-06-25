package alcoholtest.com.alcoholtest.model;

import org.json.JSONException;
import org.json.JSONObject;

public class User {
    private String name;
    private boolean isMale;
    private double age;
    private double weight;
    private double height;
    private long created;
    private JSONObject userAsJSON;

    public User(JSONObject userAsJSON){
        try {
            this.name = userAsJSON.getString("name");
            this.isMale = userAsJSON.getBoolean("isMale");
            this.age = userAsJSON.getDouble("age");
            this.weight = userAsJSON.getDouble("weight");
            this.height = userAsJSON.getDouble("height");
            this.created = userAsJSON.getLong("created");
            this.userAsJSON = userAsJSON;
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

    public JSONObject getuserAsJSON(){
        return userAsJSON;
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
