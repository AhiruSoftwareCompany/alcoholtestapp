package alcoholtest.com.alcoholtest.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Person {
    private String name;
    private boolean isMale;
    private double age;
    private double weight;
    private double height;
    private long created;
    private JSONObject personAsJSON;

    public Person(JSONObject personAsJSON){
        try {
            this.name = personAsJSON.getString("name");
            this.isMale = personAsJSON.getBoolean("isMale");
            this.age = personAsJSON.getDouble("age");
            this.weight = personAsJSON.getDouble("weight");
            this.height = personAsJSON.getDouble("height");
            this.created = personAsJSON.getLong("created");
            this.personAsJSON = personAsJSON;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Person(String name, boolean isMale, double age, double weight, double height, long created) {
        this.name = name;
        this.isMale = isMale;
        this.age = age;
        this.weight = weight;
        this.height = height;
        this.created = created;
    }

    public JSONObject getPersonAsJSON(){
        return personAsJSON;
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
