package de.klaushackner.breathalyzer;

import java.util.ArrayList;

import de.klaushackner.breathalyzer.model.Content;

public class Mixture {
    protected String name;
    protected String description;
    protected Content[] content;
    private MixtureImage image;

    public Mixture(String name, String description, Content[] content, MixtureImage image) {
        this.name = name;
        this.description = description;
        this.content = content;
        this.image = image;
    }

    public Mixture(String name, String description, double amount, double alcContent, MixtureImage image) {
        this.name = name;
        this.description = description;
        this.content = new Content[1];
        this.content[0] = new Content(name, alcContent, amount);
        this.image = image;
    }


    public double getAlcContent() {
        double totalAlcAmount = 0;

        for (int i = 0; i < content.length; i++) {
            totalAlcAmount = totalAlcAmount + content[i].alcContent * content[i].amount;
        }

        return totalAlcAmount / getAmount();
    }

    public double getAmount() {
        double totalAmount = 0;

        for (int i = 0; i < content.length; i++) {
            totalAmount = totalAmount + content[i].amount;
        }

        return totalAmount;
    }

    public static ArrayList<Mixture> getMixtureArray(User u) {/*Context c,*/

        //Add new mixtures here!
        ArrayList<Mixture> mixtures = new ArrayList<>();

        mixtures.add(new Mixture("Bier", "", 500, 5, MixtureImage.beer));
        mixtures.add(new Mixture("Bier", "", 1000, 0.05, MixtureImage.morebeer));
        mixtures.add(new Mixture("Goaß", "", 540, 0.025, MixtureImage.goass));
        mixtures.add(new Mixture("Goaßmaß", "", 1040, 0.025, MixtureImage.goass));
        mixtures.add(new Mixture("Pils", "", 330, 0.048, MixtureImage.pils));
        mixtures.add(new Mixture("Pils", "", 500, 0.048, MixtureImage.pils));
        mixtures.add(new Mixture("Wein", "", 200, 0.10, MixtureImage.wine));
        mixtures.add(new Mixture("Wodka", "", 20, 0.30, MixtureImage.vodka));
        mixtures.add(new Mixture("Wodka", "", 20, 0.40, MixtureImage.vodka));
        mixtures.add(new Mixture("Whisky", "", 20, 0.40, MixtureImage.whisky));
        mixtures.add(new Mixture("Sekt", "", 200, 0.12, MixtureImage.sparklingwine));
        mixtures.add(new Mixture("Hugo", "", 300, 0.069, MixtureImage.sparklingwine));
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

    public static boolean isValidMixture(String name, double amount, double percentage) {
        return name.length() > 2 && amount > 1 && amount < 3000 && percentage > 0.01 && percentage < 0.99;
    }
}
