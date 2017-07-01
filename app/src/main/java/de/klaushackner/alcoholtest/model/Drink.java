package de.klaushackner.alcoholtest.model;

public class Drink {
    private User user;
    private Mixture mixture;
    private long takingTime;
    private double promille;
    private long expireTime;

    public Drink(User user, Mixture mixture, long takingTime, long expireTime) {
        this.user = user;
        this.mixture = mixture;
        this.takingTime = takingTime;
        this.expireTime = expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
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

    public long getTakingTime() {
        return takingTime;
    }

    public long getExpireTime() {
        return expireTime;
    }

}
