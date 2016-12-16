package com.example.personalproject.adapters;

import com.example.personalproject.combustible.Combustible;

/**
 * Created by hector castillo on 10/7/16.
 */
public class CustomFuelAdapterItem {
    /**
     * Enum with the type or row to show.
     */
    public enum AdapterItemType{
        HEADER_VIEW, COMBUSTIBLE_INFO;
    }

    private AdapterItemType adapterItemType;
    private Combustible combustible;
    private String title;

    /***
     * Constructor to set the variables of the class.
     * @param combustible
     * @param adapterItemType
     */
    public CustomFuelAdapterItem(AdapterItemType adapterItemType, Combustible combustible){
        this.combustible = combustible;
        this.adapterItemType = adapterItemType;
    }

    public CustomFuelAdapterItem(AdapterItemType adapterItemType, String title){
        this.title = title;
        this.adapterItemType = adapterItemType;
    }

    // Getters
    public Combustible getCombustible(){
        return combustible;
    }

    public AdapterItemType getAdapterItemType(){
        return adapterItemType;
    }

    public String getTitle(){
        return title;
    }
}
