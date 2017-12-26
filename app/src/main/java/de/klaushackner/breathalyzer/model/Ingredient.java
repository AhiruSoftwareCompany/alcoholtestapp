package de.klaushackner.breathalyzer.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A ingredient of a recipe, e.g. Beer or Vodka
 */

public class Ingredient {

    protected String name;
    protected double alc_content;
    protected double quantity;

    /**
     * @param name        name of the ingredient
     * @param alc_content the alcohol content of the ingredient in Vol.-%/100 (5% -> 0.05)
     * @param quantity    the quantity of the ingredient in *ml*
     */
    public Ingredient(String name, double alc_content, double quantity) {
        this.name = name;
        this.alc_content = alc_content;
        this.quantity = quantity;
    }

    /**
     * Converts a Ingredient as JSONObject back to a Ingredient-Object
     *
     * @param ingredient
     */
    public Ingredient(JSONObject ingredient) {
        try {
            this.name = ingredient.getString("name");
            this.alc_content = ingredient.getDouble("alc_content");
            this.quantity = ingredient.getDouble("quantity");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return mass of the alcohol = quantity [ml] * alcohol content * density of alcohol (0.8 g/ml)
     */
    public double alc_mass(){
        return quantity * alc_content * 0.8;
    }

    public JSONObject json() {
        try {
            return new JSONObject().put("name", name).put("alc_content", alc_content).put("quantity", quantity);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
