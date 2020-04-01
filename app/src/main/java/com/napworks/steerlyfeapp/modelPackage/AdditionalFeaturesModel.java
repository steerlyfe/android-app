package com.napworks.steerlyfeapp.modelPackage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AdditionalFeaturesModel implements Serializable {

    private String value;
    private double price;
    private String unitType;
    private boolean isSelected;
    private List<SellerModel> sellers = new ArrayList();


    public AdditionalFeaturesModel(String value, double price, String unitType, boolean isSelected, List<SellerModel> sellers) {
        this.value = value;
        this.price = price;
        this.unitType = unitType;
        this.isSelected = isSelected;
        this.sellers = sellers;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    public List<SellerModel> getSellers() {
        return sellers;
    }

    public void setSellers(List<SellerModel> sellers) {
        this.sellers = sellers;
    }
}
