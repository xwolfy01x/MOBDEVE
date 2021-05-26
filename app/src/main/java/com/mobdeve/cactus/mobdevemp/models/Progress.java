package com.mobdeve.cactus.mobdevemp.models;

import java.io.Serializable;

public class Progress implements Serializable {
    //basic currency
    private String username;
    private double gold;

    //equipment stuff
    private int caplvl;
    private int shirtlvl;
    private int shortlvl;
    private int shoelvl;

    //shards
    private int capshard;
    private int shirtshard;
    private int shortshard;
    private int shoeshard;

    public Progress(String username) {
        this.username = username;
        this.gold = 500.0;
        this.caplvl = 1;
        this.shirtlvl = 0;
        this.shortlvl = 0;
        this.shoelvl = 0;
        this.capshard = 0;
        this.shirtshard = 0;
        this.shoeshard = 0;
        this.shortshard = 0;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public double getGold() {
        return gold;
    }

    public void setGold(double gold) {
        this.gold = gold;
    }

    public int getCaplvl() {
        return caplvl;
    }

    public void setCaplvl(int caplvl) {
        this.caplvl = caplvl;
    }

    public int getShirtlvl() {
        return shirtlvl;
    }

    public void setShirtlvl(int shirtlvl) {
        this.shirtlvl = shirtlvl;
    }

    public int getShortlvl() {
        return shortlvl;
    }

    public void setShortlvl(int shortlvl) {
        this.shortlvl = shortlvl;
    }

    public int getShoelvl() {
        return shoelvl;
    }

    public void setShoelvl(int shoelvl) {
        this.shoelvl = shoelvl;
    }

    public int getCapshard() {
        return capshard;
    }

    public void setCapshard(int capshard) {
        this.capshard = capshard;
    }

    public int getShirtshard() {
        return shirtshard;
    }

    public void setShirtshard(int shirtshard) {
        this.shirtshard = shirtshard;
    }

    public int getShortshard() {
        return shortshard;
    }

    public void setShortshard(int shortshard) {
        this.shortshard = shortshard;
    }

    public int getShoeshard() {
        return shoeshard;
    }

    public void setShoeshard(int shoeshard) {
        this.shoeshard = shoeshard;
    }

}
