package com.napworks.steerlyfeapp.modelPackage;

import java.io.Serializable;

public class LoginSignUpMainModel implements Serializable {

    private String status;
    private String message;
    public LoginSignUpInnerModel userDetail = new LoginSignUpInnerModel();

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

    public LoginSignUpInnerModel getUserDetail() {
        return userDetail;
    }

    public void setUserDetail(LoginSignUpInnerModel userDetail) {
        this.userDetail = userDetail;
    }
}
