package com.napworks.steerlyfeapp.modelPackage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class   PostsInnerModel implements Serializable {


    private String postId;
    private String postPublicId;
    private String storeName;
    private String postImage;
    private String storeLogo;
    private String categoryName;
    private String title;
    private int views;
    private long createdTime;
    private boolean postSaved;
    private String currentStatus;
    private boolean isViewStatusUpdated = false;

    List<String> postImages = new ArrayList<>();

    private boolean isLoadingType;


    public PostsInnerModel(String postId, String postPublicId, String title, int views,
                           long createdTime, String currentStatus, String storeName, String storeLogo,String postImage,
                           boolean postSaved,List<String> postImages, boolean isLoadingType) {
        this.postId = postId;
        this.postPublicId = postPublicId;
        this.postSaved = postSaved;
        this.title = title;
        this.views = views;
        this.createdTime = createdTime;
        this.currentStatus = currentStatus;
        this.postImages = postImages;
        this.storeName = storeName;
        this.postImage = postImage;
        this.storeLogo = storeLogo;
        this.isLoadingType = isLoadingType;
    }


    public boolean isPostSaved() {
        return postSaved;
    }

    public void setPostSaved(boolean postSaved) {
        this.postSaved = postSaved;
    }

    public boolean isViewStatusUpdated() {
        return isViewStatusUpdated;
    }

    public void setViewStatusUpdated(boolean viewStatusUpdated) {
        isViewStatusUpdated = viewStatusUpdated;
    }

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

    public boolean isLoadingType() {
        return isLoadingType;
    }

    public void setLoadingType(boolean loadingType) {
        isLoadingType = loadingType;
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


    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }
}
