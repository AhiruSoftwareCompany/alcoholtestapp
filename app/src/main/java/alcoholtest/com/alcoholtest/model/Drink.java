package alcoholtest.com.alcoholtest.model;

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
}
