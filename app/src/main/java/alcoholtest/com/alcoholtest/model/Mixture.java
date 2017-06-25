package alcoholtest.com.alcoholtest.model;

import android.media.Image;

public class Mixture {
    private double amount;
    private double percentage;
    private Image image;
    private String name;

    public Mixture(String name, double amount, double percentage, Image image) {
        this.amount = amount;
        this.name = name;
        this.percentage = percentage;
        this.image = image;
    }

    public double getAmount() {
        return amount;
    }

    public double getPercentage() {
        return percentage;
    }

    public Image getImage() {
        return image;
    }

    public String getName() {
        return name;
    }
}
