package com.napworks.steerlyfeapp.modelPackage;

public class PostDetailMainModel {
    private String status;
    private String message;
    private PostDetailInnerModel postDetail = new PostDetailInnerModel();


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

    public PostDetailInnerModel getPostDetail() {
        return postDetail;
    }

    public void setPostDetail(PostDetailInnerModel postDetail) {
        this.postDetail = postDetail;
    }
}
