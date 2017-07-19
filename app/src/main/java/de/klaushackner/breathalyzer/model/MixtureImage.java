package de.klaushackner.breathalyzer.model;


public enum MixtureImage {
    beer("beer"),
    bluebeer("bluebeer"),
    champagne("champagne"),
    cocktail("cocktail"),
    cocktail2("cocktail2"),
    coconut("coconut"),
    cup_straw("cup_straw"),
    custom("custom"),
    custom_fabulous("custom_fabulous"),
    custom_fox("custom_fox"),
    custom_panda("custom_panda"),
    goass("goass"),
    morebeer("morebeer"),
    pils("pils"),
    save("save"),
    sparklingwine("sparklingwine"),
    vodka("vodka"),
    whisky("whisky"),
    wine("wine");

    String title;

    MixtureImage(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }

    public static MixtureImage fromString(String text) {
        for (MixtureImage m : MixtureImage.values()) {
            if (m.title.equalsIgnoreCase(text)) {
                return m;
            }
        }
        return null;
    }
}