package de.klaushackner.breathalyzer.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A recipe ready to be consumed
 * an array of Ingredients
 */

public class Recipe {
    public String name;
    public String desc;
    public Ingredient[] ingr;
    public MixtureImage imgString;

    public Recipe(String name, String desc, Ingredient[] ingr, MixtureImage imgString) {
        this.name = name;
        this.desc = desc;
        this.ingr = ingr;
        this.imgString = imgString;
    }

    public Recipe(JSONObject j) {
        try {
            this.name = j.getString("name");
            this.desc = j.getString("desc");
            this.imgString = MixtureImage.fromString(j.getString("imgString"));

            JSONArray i = j.getJSONArray("ingredients");
            Ingredient[] ingr = new Ingredient[j.getJSONArray("ingredients").length() - 1];
            for (int x = 0; x < i.length(); x++) {
                ingr[x] = new Ingredient(i.getJSONObject(x));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public double getBac(User user) {
        double R = 0;
        if (user.isMale) {
            R = 2.447 - 0.09516 * user.age + 0.1074 * user.height + 0.3362 * user.weight;
        } else {
            R = -2.097 + 0.1069 * user.height + 0.2466 * user.weight;
        }

        double r = (1.055 * R) / (0.8 * user.weight);

        if (ingr.length == 1) {
            return (ingr[0].quantity * ingr[0].alcContent * 0.8) / (user.weight * r);
        } else {
            return 0; //TODO: Adding case: more ingredients in a recipe
        }

    }

    public JSONObject toJSON() {
        try {
            JSONObject j = new JSONObject().put("name", name).put("desc", desc).put("imgString", imgString.toString());

            JSONArray i = new JSONArray();

            for (Ingredient in : ingr) {
                i.put(in.toJSON());
            }

            j.put("ingredients", i);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public double alcContent() {
        if (ingr.length == 1) {
            return ingr[0].alcContent;
        } else {
            //TODO: add this case aswell
            return 0;
        }
    }

    public double amount() {
        double d = 0;

        for (Ingredient i : ingr) {
            d += i.quantity;
        }

        return d;
    }

    public Recipe[] getRecipes() {
        Recipe[] r = new Recipe[5];
        r[0] = new Recipe("Sex On The Beach", "Zutaten mit Eiswürfeln shaken\n\nGlas: Longdrinkglas\nDekoration mit ½ Orangenscheibe, Ananas, Kirsche",
                new Ingredient[]{
                        new Ingredient("Wodka Gorbatschow (37,5%)", 0.375, 40),
                        new Ingredient("Pfirsichlikör (17%)", 0.17, 20),
                        new Ingredient("Orangensaft", 0, 40),
                        new Ingredient("Cranberrysaft", 0, 40)},
                MixtureImage.cocktail2);
        r[1] = new Recipe("Mojito", "Ein Rum-Cocktail mit kubanischer Note.\n1.\tMinze, Limettensaft und Zucker in ein Glas geben\n" +
                "2.\tMit Stößel leicht andrücken.\n" +
                "3.\tGlas mit Eiswürfeln auffüllen und Rum hinzugeben.\n" +
                "4.\tGut verrühren und mit Soda auffüllen.\n\nGlas: Longdrinkglas\nDekoration mit Minzzweig, Limetten",
                new Ingredient[]{
                        new Ingredient("Rum, weiß (37,5%)", 0.375, 40),
                        new Ingredient("Limettensaft", 0, 20),
                        new Ingredient("Rohrzucker (2 Löffel)", 0, 10),
                        new Ingredient("Minzblätter, frisch", 0, 2),
                        new Ingredient("Soda/Wasser congas", 0, 330)}, MixtureImage.cocktail);
        r[2] = new Recipe("Island Mule", "Ein Rum-Cocktail mit original kubanischer Note.\nZutaten mit Eiswürfel in den Kupferbecher geben\n\nGlas: Kupfertasse\nDekoration mit Vanille, Orangenzeste",
                new Ingredient[]{
                        new Ingredient("Pott Rum (54%)", 0.54, 50),
                        new Ingredient("Limette", 0, 10),
                        new Ingredient("Vanillesirup", 0, 20),
                        new Ingredient("Bitterorangen-Likör (Spritzer)", 0, 1),
                        new Ingredient("Ginger Beer", 0, 100)}, MixtureImage.cocktail2);
        r[3] = new Recipe("Touch Down", "1.\tZutaten außer Grenadine mit Eis shaken und in das Glas geben.\n" +
                "2.\tGrenadine ins Glas geben und vorsichtig umrühren.\n\nGlas: Lang, dünn (Fancy)\nDekoration mit ½ Maracujascheibe",
                new Ingredient[]{
                        new Ingredient("Wodka Gorbatschow (37,5%)", 0.375, 40),
                        new Ingredient("Apricot Brandy (24%)", 0.24, 20),
                        new Ingredient("Zitronensaft", 0, 20),
                        new Ingredient("Grenadine", 0, 1),
                        new Ingredient("Maracujasaft", 0, 80)}, MixtureImage.cocktail2);
        r[4] = new Recipe("Pina Colada", "Zutaten erst ohne, nach 10 Sekunden mit Eiswürfeln shaken\n\nGlas: Lang, dünn (Fancy)\nDekoration mit Ananasstück, Trinkhalm",
                new Ingredient[]{
                        new Ingredient("Eiswürfel", 0, 4),
                        new Ingredient("Rum, weiß (37,5%)", 0.375, 60),
                        new Ingredient("Sahne", 0, 20),
                        new Ingredient("Kokoslikör/Malibu (21%)", 0.21, 40),
                        new Ingredient("Ananassaft", 0, 120),
                        new Ingredient("Crushed-Ice (2 EL)", 0, 2),
                        new Ingredient("frische Ananas", 0, 1)}, MixtureImage.cocktail);
        return r;
    }

}
