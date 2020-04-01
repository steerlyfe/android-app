package com.napworks.steerlyfeapp.modelPackage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StoreDetailMainModel implements Serializable {

    private String status;
    private String message;
    private StoreDetailModel storeDetail = new StoreDetailModel();

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

    public StoreDetailModel getStoreDetail() {
        return storeDetail;
    }

    public void setStoreDetail(StoreDetailModel storeDetail) {
        this.storeDetail = storeDetail;
    }


}
