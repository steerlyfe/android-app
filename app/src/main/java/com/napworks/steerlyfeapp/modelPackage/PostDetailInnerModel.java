package com.napworks.steerlyfeapp.modelPackage;

import java.util.ArrayList;
import java.util.List;

public class PostDetailInnerModel {

    private String postId;
    private String postPublicId;
    private String storeName;
    private String storeLogo;
    private String categoryName;
    private String title;
    private int views;
    private long createdTime;
    private boolean postSaved;
    private String currentStatus;
    public List<String> postImages = new ArrayList<>();

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostPublicId() {
        return postPublicId;
    }

    public void setPostPublicId(String postPublicId) {
        this.postPublicId = postPublicId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreLogo() {
        return storeLogo;
    }

    public void setStoreLogo(String storeLogo) {
        this.storeLogo = storeLogo;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public boolean isPostSaved() {
        return postSaved;
    }

    public void setPostSaved(boolean postSaved) {
        this.postSaved = postSaved;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public List<String> getPostImages() {
        return postImages;
    }

    public void setPostImages(List<String> postImages) {
        this.postImages = postImages;
    }
}
