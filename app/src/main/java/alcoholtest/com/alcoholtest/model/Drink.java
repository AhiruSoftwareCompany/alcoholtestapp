package alcoholtest.com.alcoholtest.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Drink {
    String name;
    String person;
    double amount;
    double percent;
    long time;

    public Drink(String name, String person, double amount, double percent, long time) {
        this.name = name;
        this.person = person;
        this.amount = amount;
        this.percent = percent;
        this.time = time;
    }

    public Drink(JSONObject drinkAsJSON){
        try {
            this.name = drinkAsJSON.getString("name");
            this.person = drinkAsJSON.getString("person");
            this.amount = drinkAsJSON.getDouble("amount");
            this.percent = drinkAsJSON.getDouble("percent");
            this.time = drinkAsJSON.getLong("time");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public String getName() {
        return name;
    }

    public String getPerson() {
        return person;
    }

    public double getAmount() {
        return amount;
    }

    public double getPercent() {
        return percent;
    }

    public long getTime() {
        return time;
    }

}
