package com.napworks.steerlyfeapp.modelPackage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProductDetailModel implements Serializable {

    private String product_id;
    private String description;
    private String category_id;
    private String product_name;
    private String store_id;
    private Double actual_price;
    private Double sale_price;
    private String image_url;
    private int product_quantity;
    private String additionalFeaturesType;
    private double additionalFeaturesPrice;
    private String productAvailabilityType;
    private double productAvailabilityPrice;
    private String sub_store_id;
    private String seller_name;
    private String seller_address;
    private long product_added_time;


    private boolean loadingType;


    private String product_public_id;
    private float rating;
    private int rating_count;
    private List<CategoryModel> product_categories = new ArrayList<>();
    private List<ProductInfoModel> product_info = new ArrayList<>();
    private List<AdditionalFeaturesModel> additional_features = new ArrayList<>();
    private List<ProductAvailabilityModel> product_availability = new ArrayList<>();

    public ProductDetailModel() {
    }

    public ProductDetailModel(String product_id, String description, String category_id, String product_name,
                              String store_id, double actual_price, Double sale_price, int product_quantity, String image_url,
                              String additionalFeaturesType, double additionalFeaturesPrice,
                              String productAvailabilityType, double productAvailabilityPrice,
                              String sub_store_id, String seller_name, String seller_address, boolean loadingType) {
        this.product_id = product_id;
        this.description = description;
        this.category_id = category_id;
        this.product_name = product_name;
        this.store_id = store_id;
        this.actual_price = actual_price;
        this.sale_price = sale_price;
        this.product_quantity = product_quantity;
        this.image_url = image_url;
        this.additionalFeaturesType = additionalFeaturesType;
        this.additionalFeaturesPrice = additionalFeaturesPrice;
        this.productAvailabilityType = productAvailabilityType;
        this.productAvailabilityPrice = productAvailabilityPrice;
        this.sub_store_id = sub_store_id;
        this.seller_name = seller_name;
        this.seller_address = seller_address;
        this.loadingType = loadingType;


    }

    public ProductDetailModel(String product_id, String description, String category_id, String product_name,
                              String store_id, double actual_price, Double sale_price, int product_quantity, String image_url, boolean loadingType) {
        this.product_id = product_id;
        this.description = description;
        this.category_id = category_id;
        this.product_name = product_name;
        this.store_id = store_id;
        this.actual_price = actual_price;
        this.sale_price = sale_price;
        this.product_quantity = product_quantity;
        this.image_url = image_url;
        this.loadingType = loadingType;


    }



    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public Double getActual_price() {
        return actual_price;
    }

    public void setActual_price(Double actual_price) {
        this.actual_price = actual_price;
    }

    public Double getSale_price() {
        return sale_price;
    }

    public void setSale_price(Double sale_price) {
        this.sale_price = sale_price;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public boolean isLoadingType() {
        return loadingType;
    }

    public void setLoadingType(boolean loadingType) {
        this.loadingType = loadingType;
    }

    public int getProduct_quantity() {
        return product_quantity;
    }

    public void setProduct_quantity(int product_quantity) {
        this.product_quantity = product_quantity;
    }

    public String getProduct_public_id() {
        return product_public_id;
    }

    public void setProduct_public_id(String product_public_id) {
        this.product_public_id = product_public_id;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getRating_count() {
        return rating_count;
    }

    public void setRating_count(int rating_count) {
        this.rating_count = rating_count;
    }

    public List<CategoryModel> getProduct_categories() {
        return product_categories;
    }

    public void setProduct_categories(List<CategoryModel> product_categories) {
        this.product_categories = product_categories;
    }

    public List<ProductInfoModel> getProduct_info() {
        return product_info;
    }

    public void setProduct_info(List<ProductInfoModel> product_info) {
        this.product_info = product_info;
    }

    public List<AdditionalFeaturesModel> getAdditional_features() {
        return additional_features;
    }

    public void setAdditional_features(List<AdditionalFeaturesModel> additional_features) {
        this.additional_features = additional_features;
    }

    public List<ProductAvailabilityModel> getProduct_availability() {
        return product_availability;
    }

    public void setProduct_availability(List<ProductAvailabilityModel> product_availability) {
        this.product_availability = product_availability;
    }

    public String getAdditionalFeaturesType() {
        return additionalFeaturesType;
    }

    public void setAdditionalFeaturesType(String additionalFeaturesType) {
        this.additionalFeaturesType = additionalFeaturesType;
    }

    public double getAdditionalFeaturesPrice() {
        return additionalFeaturesPrice;
    }

    public void setAdditionalFeaturesPrice(double additionalFeaturesPrice) {
        this.additionalFeaturesPrice = additionalFeaturesPrice;
    }

    public String getProductAvailabilityType() {
        return productAvailabilityType;
    }

    public void setProductAvailabilityType(String productAvailabilityType) {
        this.productAvailabilityType = productAvailabilityType;
    }

    public double getProductAvailabilityPrice() {
        return productAvailabilityPrice;
    }

    public void setProductAvailabilityPrice(double productAvailabilityPrice) {
        this.productAvailabilityPrice = productAvailabilityPrice;
    }

    public String getSub_store_id() {
        return sub_store_id;
    }

    public void setSub_store_id(String sub_store_id) {
        this.sub_store_id = sub_store_id;
    }


    public String getSeller_name() {
        return seller_name;
    }

    public void setSeller_name(String seller_name) {
        this.seller_name = seller_name;
    }


    public long getProduct_added_time() {
        return product_added_time;
    }

    public void setProduct_added_time(long product_added_time) {
        this.product_added_time = product_added_time;
    }

    public String getSeller_address() {
        return seller_address;
    }

    public void setSeller_address(String seller_address) {
        this.seller_address = seller_address;
    }


}
