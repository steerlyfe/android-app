package com.napworks.steerlyfeapp.modelPackage;

import java.io.Serializable;

public class OrderLogModel implements Serializable {

    private String type;
    private boolean value;
    private long time;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
