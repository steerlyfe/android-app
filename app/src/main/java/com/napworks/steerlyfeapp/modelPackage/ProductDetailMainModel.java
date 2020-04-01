package com.napworks.steerlyfeapp.modelPackage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProductDetailMainModel implements Serializable {

    private String status;
    private String message;
    public ProductDetailModel productDetail = new ProductDetailModel();

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

    public ProductDetailModel getProductDetail() {
        return productDetail;
    }

    public void setProductDetail(ProductDetailModel productDetail) {
        this.productDetail = productDetail;
    }
}
