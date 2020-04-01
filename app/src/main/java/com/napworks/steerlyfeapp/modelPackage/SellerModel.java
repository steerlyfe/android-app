package com.napworks.steerlyfeapp.modelPackage;

import java.io.Serializable;

public class SellerModel implements Serializable {

    private String sub_store_id;
    private String store_id;
    private String name;
    private String address;
    private int rating;
    private String selectedUnit;
    private double selectedUnitPrice;

    public SellerModel(String sub_store_id, String store_id, String name, String address, int rating, String selectedUnit) {
        this.sub_store_id = sub_store_id;
        this.store_id = store_id;
        this.name = name;
        this.address = address;
        this.rating = rating;
        this.selectedUnit = selectedUnit;
    }

    public String getSub_store_id() {
        return sub_store_id;
    }

    public void setSub_store_id(String sub_store_id) {
        this.sub_store_id = sub_store_id;
    }

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getSelectedUnit() {
        return selectedUnit;
    }

    public void setSelectedUnit(String selectedUnit) {
        this.selectedUnit = selectedUnit;
    }

    public double getSelectedUnitPrice() {
        return selectedUnitPrice;
    }

    public void setSelectedUnitPrice(double selectedUnitPrice) {
        this.selectedUnitPrice = selectedUnitPrice;
    }
}
