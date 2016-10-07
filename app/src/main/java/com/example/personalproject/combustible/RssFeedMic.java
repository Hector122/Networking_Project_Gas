package com.example.personalproject.combustible;

import java.util.ArrayList;

public class RssFeedMic {
    private String title, publicationDate;
    private ArrayList<Combustible> combustibles = new ArrayList<>();

//-- Getters and setters --

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
    }

    public ArrayList<Combustible> getCombustibles() {
        return combustibles;
    }

    public void setCombustibles(ArrayList<Combustible> combustibles) {
        this.combustibles = combustibles;
    }
}
