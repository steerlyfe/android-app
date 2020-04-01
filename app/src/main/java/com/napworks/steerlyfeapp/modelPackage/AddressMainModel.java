package com.napworks.steerlyfeapp.modelPackage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AddressMainModel implements Serializable {
    private String status;
    private String message;
    private List<AddressInnerModel> addressList = new ArrayList<>();

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<AddressInnerModel> getAddressList() {
        return addressList;
    }

    public void setAddressList(List<AddressInnerModel> addressList) {
        this.addressList = addressList;
    }
}
