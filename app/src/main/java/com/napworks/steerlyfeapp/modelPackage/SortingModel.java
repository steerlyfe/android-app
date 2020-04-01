package com.napworks.steerlyfeapp.modelPackage;

public class SortingModel {

    public String title;
    public boolean isSelected;
    private String sortingValue;

    public SortingModel(String title, boolean isSelected, String sortingValue) {
        this.title = title;
        this.isSelected = isSelected;
        this.sortingValue = sortingValue;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getSortingValue() {
        return sortingValue;
    }

    public void setSortingValue(String sortingValue) {
        this.sortingValue = sortingValue;
    }
}
