package de.klaushackner.breathalyzer.model;

/**
 * A recipe ready to be consumed
 * an array of Ingredients
 */

public class Recipe {
    protected String name;
    protected String desc;
    protected Ingredient[] ingr;

    public Recipe(String name, String desc, Ingredient[] ingr) {
        this.name = name;
        this.desc = desc;
        this.ingr = ingr;
    }

    public double alc_mass() {
        double am = 0;
        for (Ingredient i : ingr) {
            am += i.alc_mass();
        }
        return am;
    }

}
