/*
User.java

class used to define a user. This will contain basic stats that we need to track deltas.


 */
package com.example.habiticalist;

public class User {
    private double health;
    private double mana;
    private double gold;
    private double xp;
    public User(){

    }
    public User(double health, double gold, double xp, double mana){
        this.health=health;
        this.gold=gold;
        this.xp = xp;
        this.mana = mana;

    }

    public double getGold() {
        return gold;
    }

    public double getHealth() {
        return health;
    }

    public double getMana() {
        return mana;
    }

    public double getXp() {
        return xp;
    }

    public double setGold(double gold) {
        double diff = gold - this.gold;
        this.gold = gold;
        return diff;
    }

    public double setHealth(double health) {
        double diff = health - this.health;
        this.health = health;
        return diff;
    }

    public double setMana(double mana) {
        double diff = mana - this.mana;
        this.mana = mana;
        return diff;
    }

    public double setXp(double xp) {
        double diff = xp - this.xp;
        this.xp = xp;
        return diff;
    }
}
