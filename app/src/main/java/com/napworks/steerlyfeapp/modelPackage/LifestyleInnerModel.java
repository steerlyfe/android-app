package com.napworks.steerlyfeapp.modelPackage;

public class LifestyleInnerModel {

    private String type;
    private String name;
    private int image;

    public LifestyleInnerModel(String type, String name,int image) {
        this.type = type;
        this.name = name;
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
