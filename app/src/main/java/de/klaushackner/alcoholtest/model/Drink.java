package de.klaushackner.alcoholtest.model;

public class Drink {
    private final User user;
    private final Mixture mixture;
    private final long takingTime;
    private double bac;
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

    public void setBac(double bac) {
        this.bac = bac;
    }

    public double getBac() {
        return bac;
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
