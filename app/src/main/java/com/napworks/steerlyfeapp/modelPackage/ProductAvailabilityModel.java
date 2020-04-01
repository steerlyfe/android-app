package com.napworks.steerlyfeapp.modelPackage;

import java.io.Serializable;

public class ProductAvailabilityModel implements Serializable {

    private String type;
    private boolean available;
    private double price;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
