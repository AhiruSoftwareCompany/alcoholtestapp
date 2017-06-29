package de.klaushackner.alcoholtest.model;

public class Drink {
    private User user;
    private Mixture mixture;
    private long time;
    private double promille;

    public Drink(User user, Mixture mixture, long time) {
        this.user = user;
        this.mixture = mixture;
        this.time = time;
    }

    public void setPromille(double promille) {
        this.promille = promille;
    }

    public double getPromille() {
        return promille;
    }

    public User getUser() {
        return user;
    }

    public Mixture getMixture() {
        return mixture;
    }

    public long getTime() {
        return time;
    }

}
