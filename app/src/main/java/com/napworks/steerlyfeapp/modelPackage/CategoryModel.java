package com.napworks.steerlyfeapp.modelPackage;

import java.io.Serializable;

public class CategoryModel implements Serializable {

    private String categoryId;
    private String categoryName;
    private String categoryUrl;
    private boolean isSelected;

    public CategoryModel(String categoryId, String categoryName, String categoryUrl) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.categoryUrl = categoryUrl;
    }

    public CategoryModel(String categoryId, String categoryName, String categoryUrl, boolean isSelected) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.categoryUrl = categoryUrl;
        this.isSelected = isSelected;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryUrl() {
        return categoryUrl;
    }

    public void setCategoryUrl(String categoryUrl) {
        this.categoryUrl = categoryUrl;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
