package de.klaushackner.breathalyzer.model;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Recipe {
    private MixtureImage image;
    private String name;
    private String text;
    private Ingredient[] ingredients;

    public Recipe(MixtureImage image, String name, String text, Ingredient[] ingredients) {
        this.image = image;
        this.text = text;
        this.name = name;
        this.ingredients = ingredients;
    }

    public Recipe(JSONObject recipeAsJSON) {
        try {
            this.image = MixtureImage.fromString(recipeAsJSON.getString("image"));
            this.name = recipeAsJSON.getString("name");
            this.text = recipeAsJSON.getString("text");
            JSONArray ingr = recipeAsJSON.getJSONArray("ingredients");
            ingredients = new Ingredient[ingr.length()];
            for (int i = 0; i < ingr.length(); i++) {
                this.ingredients[i] = new Ingredient(new JSONObject(ingr.get(i).toString()));
            }

            for (int i = 0; i < ingr.length(); i++) {
                ingredients[i] = new Ingredient(new JSONObject(ingr.get(i).toString()));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
            j.put("text", text);
            JSONArray ingr = new JSONArray();
            for (Ingredient i : ingredients) {
                ingr.put(i.toString());
            }
            j.put("ingredients", ingr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return j;
    }

    public MixtureImage getImage() {
        return image;
    }

    public void setImage(MixtureImage image) {
        this.image = image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public double getAmount() {
        double amount = 0;
        for (Ingredient ingr : ingredients) {
            if (ingr.getPercentage() != 0) {
                amount += ingr.getAmount();
            }
        }
        return amount; //Menge der Bestandteile des Getränks mit Alkohol
    }

    public double getPercentage() {
        double a = 0;
        for (Ingredient ingr : ingredients) {
            a += (ingr.getAmount() * ingr.getPercentage());
        }
        if (getAmount() > 0) {
            return a / getAmount();
        } else {
            return 0;
        }
    }

    public static Recipe[] getRecipeArray(Context c) {
        try {
            JSONArray customRecipe = getCustomRecipes(c);

            //Add new recipes here!
            JSONArray recipes = new JSONArray("[]");
            recipes.put(new Recipe(
                    MixtureImage.cocktail2,
                    "Sex On The Beach",
                    "Zutaten mit Eiswürfeln shaken\n\nGlas: Longdrinkglas\nDekoration mit ½ Orangenscheibe, Ananas, Kirsche",
                    new Ingredient[]{
                            new Ingredient("Wodka Gorbatschow (37,5%)", 0.375, 40),
                            new Ingredient("Pfirsichlikör (17%)", 0.17, 20),
                            new Ingredient("Orangensaft", 0, 40),
                            new Ingredient("Cranberrysaft", 0, 40)}));
            recipes.put(new Recipe(
                    MixtureImage.cocktail,
                    "Mojito",
                    "Ein Rum-Cocktail mit kubanischer Note.\n1.\tMinze, Limettensaft und Zucker in ein Glas geben\n" +
                            "2.\tMit Stößel leicht andrücken.\n" +
                            "3.\tGlas mit Eiswürfeln auffüllen und Rum hinzugeben.\n" +
                            "4.\tGut verrühren und mit Soda auffüllen.\n\nGlas: Longdrinkglas\nDekoration mit Minzzweig, Limetten",
                    new Ingredient[]{
                            new Ingredient("Rum, weiß (37,5%)", 0.375, 40),
                            new Ingredient("Limettensaft", 0, 20),
                            new Ingredient("Rohrzucker (2 Löffel)", 0, 10),
                            new Ingredient("Minzblätter, frisch", 0, 2),
                            new Ingredient("Soda/Wasser congas", 0, 330)}));
            recipes.put(new Recipe(
                    MixtureImage.cocktail2,
                    "Island Mule",
                    "Ein Rum-Cocktail mit original kubanischer Note.\nZutaten mit Eiswürfel in den Kupferbecher geben\n\nGlas: Kupfertasse\nDekoration mit Vanille, Orangenzeste",
                    new Ingredient[]{
                            new Ingredient("Pott Rum (54%)", 0.54, 50),
                            new Ingredient("Limette", 0, 10),
                            new Ingredient("Vanillesirup", 0, 20),
                            new Ingredient("Bitterorangen-Likör (Spritzer)", 0, 1),
                            new Ingredient("Ginger Beer", 0, 100)}));
            recipes.put(new Recipe(
                    MixtureImage.cocktail2,
                    "Touch Down",
                    "1.\tZutaten außer Grenadine mit Eis shaken und in das Glas geben.\n" +
                            "2.\tGrenadine ins Glas geben und vorsichtig umrühren.\n\nGlas: Lang, dünn (Fancy)\nDekoration mit ½ Maracujascheibe",
                    new Ingredient[]{
                            new Ingredient("Wodka Gorbatschow (37,5%)", 0.375, 40),
                            new Ingredient("Apricot Brandy (24%)", 0.24, 20),
                            new Ingredient("Zitronensaft", 0, 20),
                            new Ingredient("Grenadine", 0, 1),
                            new Ingredient("Maracujasaft", 0, 80)}));

            recipes.put(new Recipe(
                    MixtureImage.cocktail,
                    "Pina Colada",
                    "Zutaten erst ohne, nach 10 Sekunden mit Eiswürfeln shaken\n\nGlas: Lang, dünn (Fancy)\nDekoration mit Ananasstück, Trinkhalm",
                    new Ingredient[]{
                            new Ingredient("Eiswürfel", 0, 4),
                            new Ingredient("Rum, weiß (37,5%)", 0.375, 60),
                            new Ingredient("Sahne", 0, 20),
                            new Ingredient("Kokoslikör/Malibu (21%)", 0.21, 40),
                            new Ingredient("Ananassaft", 0, 120),
                            new Ingredient("Crushed-Ice (2 EL)", 0, 2),
                            new Ingredient("frische Ananas", 0, 1)}));

            for (int i = 0; i < customRecipe.length(); i++) {
                recipes.put(customRecipe.get(i).toString());
            }

            final Recipe[] recipeArray = new Recipe[recipes.length()];

            for (int i = 0; i < recipes.length(); i++) {
                recipeArray[i] = new Recipe(new JSONObject(recipes.get(i).toString()));
            }

            return recipeArray;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new Recipe[0];
    }

    public static JSONArray getCustomRecipes(Context c) {
        try {
            SharedPreferences sharedPref = c.getSharedPreferences("data", 0);
            return new JSONArray(sharedPref.getString("customRecipes", "[]"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new JSONArray();
    }

    public static void addCustomRecipe(Context c, Recipe r) {
        try {
            SharedPreferences sharedPref = c.getSharedPreferences("data", 0);
            SharedPreferences.Editor editor = sharedPref.edit();

            JSONArray recipes = new JSONArray(sharedPref.getString("customRecipes", "[]"));
            recipes.put(r.toString());

            editor.putString("customRecipes", recipes.toString());
            editor.commit();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void removeCustomRecipe(Context c, Recipe r) {
        try {
            SharedPreferences sharedPref = c.getSharedPreferences("data", 0);
            SharedPreferences.Editor editor = sharedPref.edit();

            JSONArray recipes = new JSONArray(sharedPref.getString("customRecipes", "[]"));

            for (int i = 0; i < recipes.length(); i++) {
                if (recipes.get(i).toString().compareTo(r.toString()) == 0) {
                    recipes.remove(i);
                    break; //break prevents removing every recipe matching to the given recipe (helpful if you addCustomRecipe the same recipe two times and want to remove it)
                }
            }

            editor.putString("customRecipes", recipes.toString());
            editor.commit();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Ingredient[] getIngredients() {
        return ingredients;
    }
}
