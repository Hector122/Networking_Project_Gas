package com.example.personalproject.combustible;

import java.io.Serializable;

public class Combustible implements Serializable {
    private static final long serialVersionUID = 1L;

    private double price;
    private double lastPrice;

    private String description;
    private String code;

    // -- Getters and Setters -- //

    public double getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(double lastPrice) {
        this.lastPrice = lastPrice;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}