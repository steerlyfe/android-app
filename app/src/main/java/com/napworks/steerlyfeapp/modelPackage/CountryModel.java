package com.napworks.steerlyfeapp.modelPackage;

public class CountryModel {

    private String countryId;
    private String countryName;
    private String shortCode;
    private String longCode;
    private String callingCode;
    private String timeZone;

    public CountryModel(String countryId, String countryName, String shortCode,
                        String longCode, String callingCode, String timeZone) {
        this.countryId = countryId;
        this.countryName = countryName;
        this.shortCode = shortCode;
        this.longCode = longCode;
        this.callingCode = callingCode;
        this.timeZone = timeZone;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public String getLongCode() {
        return longCode;
    }

    public void setLongCode(String longCode) {
        this.longCode = longCode;
    }

    public String getCallingCode() {
        return callingCode;
    }

    public void setCallingCode(String callingCode) {
        this.callingCode = callingCode;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }
}
