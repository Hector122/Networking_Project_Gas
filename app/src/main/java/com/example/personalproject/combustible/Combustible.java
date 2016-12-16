package com.example.personalproject.combustible;

/***
 * Create by Hector Castillo on
 */
public class Combustible {

    private double price, lastPrice;
    private String description, code;

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