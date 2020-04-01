package com.napworks.steerlyfeapp.modelPackage;

import java.io.Serializable;

public class LoginSignUpInnerModel implements Serializable {

    private String userId;
    private String accountId;
    private String fullName;
    private String phoneNumber;
    private String email;
    private String authToken;
    private String sessionToken;
    private int  questionSubmitted;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public int getQuestionSubmitted() {
        return questionSubmitted;
    }

    public void setQuestionSubmitted(int questionSubmitted) {
        this.questionSubmitted = questionSubmitted;
    }
}
