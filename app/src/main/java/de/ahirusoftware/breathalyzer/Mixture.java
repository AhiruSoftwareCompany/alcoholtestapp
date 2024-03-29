package de.ahirusoftware.breathalyzer;

import android.content.Context;
import android.content.res.Resources;

import java.util.ArrayList;
import java.util.Arrays;

import de.ahirusoftware.breathalyzer.model.Content;

public class Mixture {
    protected String name;
    protected String description;
    protected Content[] content;
    protected MixtureImage image;

    public Mixture(String name, String description, Content[] content, MixtureImage image) {
        this.name = name;
        this.description = description;
        this.content = content;
        this.image = image;
    }

    public Mixture(String name, String description, double amount, double alcContent, MixtureImage image) {
        this.name = name;
        this.description = description;
        this.content = new Content[]{new Content(name, alcContent, amount)};
        this.image = image;
    }

    public double getAlcContent() {
        double totalAlcAmount = 0;

        for (Content c : content) {
            totalAlcAmount = totalAlcAmount + c.alcContent * c.amount;
        }

        if (getAmount() != 0) {
            return totalAlcAmount / getAmount();
        } else {
            return 0;
        }
    }

    public double getAmount() {
        double totalAmount = 0;

        for (Content c : content) {
            totalAmount = totalAmount + c.amount;
        }

        return totalAmount;
    }

    public static ArrayList<Mixture> getMixtureArray(Context c, User u) {/*Context c,*/

        //Add new mixtures here!
        ArrayList<Mixture> mixtures = new ArrayList<>();
        Resources res = c.getResources();

        mixtures.add(new Mixture(res.getString(R.string.beer), c.getResources().getString(R.string.beer_desc), 500, 0.05, MixtureImage.beer));
        mixtures.add(new Mixture(res.getString(R.string.beer), c.getResources().getString(R.string.beer_desc), 1000, 0.05, MixtureImage.morebeer));

        mixtures.add(new Mixture(res.getString(R.string.goass), "", new Content[]{new Content(c.getResources().getString(R.string.beer), 0.05, 250),
                new Content(res.getString(R.string.cola), 0, 250), new Content(res.getString(R.string.cherryliqueur), 0.20, 20)}, MixtureImage.goass));
        mixtures.add(new Mixture(res.getString(R.string.goassmass), "", new Content[]{new Content(c.getResources().getString(R.string.beer), 0.05, 500),
                new Content(res.getString(R.string.cola), 0, 500), new Content(res.getString(R.string.cherryliqueur), 0.20, 40)}, MixtureImage.goass));

        mixtures.add(new Mixture(res.getString(R.string.pilsner), "", 330, 0.048, MixtureImage.pils));
        mixtures.add(new Mixture(res.getString(R.string.pilsner), "", 500, 0.048, MixtureImage.pils));
        mixtures.add(new Mixture(res.getString(R.string.redcider), "", 500, 0.04, MixtureImage.redcider));
        mixtures.add(new Mixture(res.getString(R.string.wine), "", 200, 0.10, MixtureImage.wine));
        mixtures.add(new Mixture(res.getString(R.string.vodka), "", 20, 0.30, MixtureImage.vodka));
        mixtures.add(new Mixture(res.getString(R.string.vodka), "", 20, 0.40, MixtureImage.vodka));
        mixtures.add(new Mixture(res.getString(R.string.irishflag), "",
                new Content[]{new Content(res.getString(R.string.baileysirishcreme), 0.17, 13.333),
                new Content(res.getString(R.string.cremedeMenthe), 0.24, 13.333),
                new Content(res.getString(R.string.irishwhisky), 0.40, 13.333)},
                MixtureImage.irishflag));
        mixtures.add(new Mixture(res.getString(R.string.whisky), "", 20, 0.40, MixtureImage.whisky));
        mixtures.add(new Mixture(res.getString(R.string.sparklingwine), "", 200, 0.12, MixtureImage.sparklingwine));
        mixtures.add(new Mixture(res.getString(R.string.hugo), "", 300, 0.069, MixtureImage.sparklingwine));

        mixtures.add(new Mixture("Sex On The Beach", "Zutaten mit Eiswürfeln shaken\n\nGlas: Longdrinkglas\nDekoration mit ½ Orangenscheibe, Ananas, Kirsche",
                new Content[]{new Content("Wodka Gorbatschow (37,5%)", 0.375, 40), new Content("Pfirsichlikör (17%)", 0.17, 20),
                        new Content("Orangensaft", 0, 40), new Content("Cranberrysaft", 0, 40)}, MixtureImage.cocktail2));
        mixtures.add(new Mixture("Mojito", "Ein Rum-Cocktail mit kubanischer Note.\n1.\tMinze, Limettensaft und Zucker in ein Glas geben\n" + "2.\tMit Stößel leicht andrücken.\n" + "3.\tGlas mit Eiswürfeln auffüllen und Rum hinzugeben.\n" + "4.\tGut verrühren und mit Soda auffüllen.\n\nGlas: Longdrinkglas\nDekoration mit Minzzweig, Limetten",
                new Content[]{new Content("Rum, weiß (37,5%)", 0.375, 40), new Content("Limettensaft", 0, 20), new Content("Rohrzucker (2 Löffel)", 0, 10),
                        new Content("Minzblätter, frisch", 0, 2), new Content("Soda/Wasser congas", 0, 330)}, MixtureImage.cocktail));
        mixtures.add(new Mixture("Island Mule", "Ein Rum-Cocktail mit original kubanischer Note.\nZutaten mit Eiswürfel in den Kupferbecher geben\n\nGlas: Kupfertasse\nDekoration mit Vanille, Orangenzeste",
                new Content[]{new Content("Pott Rum (54%)", 0.54, 50), new Content("Limette", 0, 10), new Content("Vanillesirup", 0, 20), new Content("Bitterorangen-Likör (Spritzer)", 0, 1),
                        new Content("Ginger Beer", 0, 100)}, MixtureImage.cocktail2));
        mixtures.add(new Mixture("Touch Down", "1.\tZutaten außer Grenadine mit Eis shaken und in das Glas geben.\n" + "2.\tGrenadine ins Glas geben und vorsichtig umrühren.\n\nGlas: Lang, dünn (Fancy)\nDekoration mit ½ Maracujascheibe",
                new Content[]{new Content("Wodka Gorbatschow (37,5%)", 0.375, 40), new Content("Apricot Brandy (24%)", 0.24, 20),
                        new Content("Zitronensaft", 0, 20), new Content("Grenadine", 0, 1), new Content("Maracujasaft", 0, 80)}, MixtureImage.cocktail2));
        mixtures.add(new Mixture("Pina Colada", "Zutaten erst ohne, nach 10 Sekunden mit Eiswürfeln shaken\n\nGlas: Lang, dünn (Fancy)\nDekoration mit Ananasstück, Trinkhalm",
                new Content[]{new Content("Eiswürfel", 0, 4), new Content("Rum, weiß (37,5%)", 0.375, 60), new Content("Sahne", 0, 20), new Content("Kokoslikör/Malibu (21%)", 0.21, 40),
                        new Content("Ananassaft", 0, 120), new Content("Crushed-Ice (2 EL)", 0, 2), new Content("frische Ananas", 0, 1)}, MixtureImage.cocktail));

/*
            for (int i = 0; i < customMixtures.length(); i++) {
                mixtures.put(customMixtures.get(i).toString());
            }
*/
        if (u.name.compareTo("Franzi") == 0) {
            mixtures.add(new Mixture("Eigenes\nGetränk", "", 0, 0, MixtureImage.custom_panda));
        } else {
            mixtures.add(new Mixture("Eigenes\nGetränk", "", 0, 0, MixtureImage.custom));
        }


        return mixtures;
    }

    public static ArrayList<Mixture> getRecipeArray(Context c, User u) {
        ArrayList<Mixture> recipes = new ArrayList<>();

        recipes.add(new Mixture("Sex On The Beach", "Zutaten mit Eiswürfeln shaken\n\nGlas: Longdrinkglas\nDekoration mit ½ Orangenscheibe, Ananas, Kirsche",
                new Content[]{new Content("Wodka Gorbatschow (37,5%)", 0.375, 40), new Content("Pfirsichlikör (17%)", 0.17, 20),
                        new Content("Orangensaft", 0, 40), new Content("Cranberrysaft", 0, 40)}, MixtureImage.cocktail2));
        recipes.add(new Mixture("Mojito", "Ein Rum-Cocktail mit kubanischer Note.\n1.\tMinze, Limettensaft und Zucker in ein Glas geben\n" + "2.\tMit Stößel leicht andrücken.\n" + "3.\tGlas mit Eiswürfeln auffüllen und Rum hinzugeben.\n" + "4.\tGut verrühren und mit Soda auffüllen.\n\nGlas: Longdrinkglas\nDekoration mit Minzzweig, Limetten",
                new Content[]{new Content("Rum, weiß (37,5%)", 0.375, 40), new Content("Limettensaft", 0, 20), new Content("Rohrzucker (2 Löffel)", 0, 10),
                        new Content("Minzblätter, frisch", 0, 2), new Content("Soda/Wasser congas", 0, 330)}, MixtureImage.cocktail));
        recipes.add(new Mixture("Island Mule", "Ein Rum-Cocktail mit original kubanischer Note.\nZutaten mit Eiswürfel in den Kupferbecher geben\n\nGlas: Kupfertasse\nDekoration mit Vanille, Orangenzeste",
                new Content[]{new Content("Pott Rum (54%)", 0.54, 50), new Content("Limette", 0, 10), new Content("Vanillesirup", 0, 20), new Content("Bitterorangen-Likör (Spritzer)", 0, 1),
                        new Content("Ginger Beer", 0, 100)}, MixtureImage.cocktail2));
        recipes.add(new Mixture("Touch Down", "1.\tZutaten außer Grenadine mit Eis shaken und in das Glas geben.\n" + "2.\tGrenadine ins Glas geben und vorsichtig umrühren.\n\nGlas: Lang, dünn (Fancy)\nDekoration mit ½ Maracujascheibe",
                new Content[]{new Content("Wodka Gorbatschow (37,5%)", 0.375, 40), new Content("Apricot Brandy (24%)", 0.24, 20),
                        new Content("Zitronensaft", 0, 20), new Content("Grenadine", 0, 1), new Content("Maracujasaft", 0, 80)}, MixtureImage.cocktail2));
        recipes.add(new Mixture("Pina Colada", "Zutaten erst ohne, nach 10 Sekunden mit Eiswürfeln shaken\n\nGlas: Lang, dünn (Fancy)\nDekoration mit Ananasstück, Trinkhalm",
                new Content[]{new Content("Eiswürfel", 0, 4), new Content("Rum, weiß (37,5%)", 0.375, 60), new Content("Sahne", 0, 20), new Content("Kokoslikör/Malibu (21%)", 0.21, 40),
                        new Content("Ananassaft", 0, 120), new Content("Crushed-Ice (2 EL)", 0, 2), new Content("frische Ananas", 0, 1)}, MixtureImage.cocktail));

        /*
               add custom Recipes
         */

        return recipes;

    }

    public static boolean isValidMixture(String name, double amount, double percentage) {
        return name.length() > 2 && amount > 1 && amount < 3000 && percentage > 0.01 && percentage < 0.99;
    }
}
