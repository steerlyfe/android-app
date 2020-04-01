package com.napworks.steerlyfeapp.modelPackage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NearByStoresMainModel implements Serializable {

    private String status;
    private String message;
    private List<StoreDetailModel> nearbyStores = new ArrayList<>();

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

    public List<StoreDetailModel> getNearbyStores() {
        return nearbyStores;
    }

    public void setNearbyStores(List<StoreDetailModel> nearbyStores) {
        this.nearbyStores = nearbyStores;
    }
}
