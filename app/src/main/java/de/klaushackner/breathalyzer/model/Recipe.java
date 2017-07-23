package de.klaushackner.breathalyzer.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Recipe {
    private MixtureImage image;
    private String name;
    private Ingredient[] ingredients;

    public Recipe(MixtureImage image, String name, Ingredient[] ingredients) {
        this.image = image;
        this.name = name;
        this.ingredients = ingredients;
    }

    public String getName() {
        return name;
    }

    public Ingredient[] getIngredients() {
        return ingredients;
    }

    public double getAmount() {
        return 0;
    }

    public double getPercentage() {
        return 0;
    }

    public MixtureImage getImage() {
        return image;
    }

    public void setImage(MixtureImage image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return this.toJSON().toString();
    }

    public JSONObject toJSON() {
        JSONObject j = new JSONObject();
        try {
            j.put("image", image.toString());
            j.put("name", name);
            j.put("ingredients", ingredients);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(j.toString());
        return j;
    }

}
