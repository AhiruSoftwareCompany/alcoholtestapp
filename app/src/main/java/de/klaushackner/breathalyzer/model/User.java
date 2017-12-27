package de.klaushackner.breathalyzer.model;


public class User {
    public String name;
    public boolean isMale;
    public int age;
    public int weight; //kg
    public int height; //cm
    public long created;
    public Drink[] drinks;

    public User(String name, boolean isMale, int age, int weight, int height, long created, Drink[] drinks) {
        this.name = name;
        this.isMale = isMale;
        this.age = age;
        this.weight = weight;
        this.height = height;
        this.created = created;
        this.drinks = drinks;
    }

    public User(String name, boolean isMale, int age, int weight, int height, long created) {
        this.name = name;
        this.isMale = isMale;
        this.age = age;
        this.weight = weight;
        this.height = height;
        this.created = created;
        this.drinks = new Drink[0];
    }

    public static boolean isValidUser(String name, int age, int height, int weight) {
        return name.length() > 2 && age > 10 && age < 100 && weight > 30 && weight < 200 && height > 100 && height < 230;
    }

    public void addDrink(Drink d) {
        Drink[] newD = new Drink[drinks.length + 1];

        System.arraycopy(drinks, 0, newD, 0, drinks.length);
        newD[newD.length - 1] = d;

        drinks = newD;
    }

}
