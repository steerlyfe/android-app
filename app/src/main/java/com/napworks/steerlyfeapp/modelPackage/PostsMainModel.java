package com.napworks.steerlyfeapp.modelPackage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PostsMainModel implements Serializable {

    private String status;
    private String message;
    private int perPageItems;

    List<PostsInnerModel> postList = new ArrayList<>();


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

    public int getPerPageItems() {
        return perPageItems;
    }

    public void setPerPageItems(int perPageItems) {
        this.perPageItems = perPageItems;
    }

    public List<PostsInnerModel> getPostList() {
        return postList;
    }

    public void setPostList(List<PostsInnerModel> postList) {
        this.postList = postList;
    }
}
