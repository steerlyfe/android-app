package com.napworks.steerlyfeapp.modelPackage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OrderStatusModel implements Serializable {


    private String currentStatus;
    private List<OrderLogModel> logsList = new ArrayList<>();


    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public List<OrderLogModel> getLogsList() {
        return logsList;
    }

    public void setLogsList(List<OrderLogModel> logsList) {
        this.logsList = logsList;
    }
}
