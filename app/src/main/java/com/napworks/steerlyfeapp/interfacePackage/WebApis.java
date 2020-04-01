package com.napworks.steerlyfeapp.interfacePackage;


import com.napworks.steerlyfeapp.modelPackage.AddressMainModel;
import com.napworks.steerlyfeapp.modelPackage.CouponAppliedModel;
import com.napworks.steerlyfeapp.modelPackage.HomeDataMainModel;
import com.napworks.steerlyfeapp.modelPackage.LoginSignUpMainModel;
import com.napworks.steerlyfeapp.modelPackage.CommonStringModel;
import com.napworks.steerlyfeapp.modelPackage.NearByStoresMainModel;
import com.napworks.steerlyfeapp.modelPackage.OrderHistoryMainModel;
import com.napworks.steerlyfeapp.modelPackage.PlaceOrderModel;
import com.napworks.steerlyfeapp.modelPackage.PostDetailMainModel;
import com.napworks.steerlyfeapp.modelPackage.PostsMainModel;
import com.napworks.steerlyfeapp.modelPackage.ProductCategoriesMainModel;
import com.napworks.steerlyfeapp.modelPackage.ProductDetailMainModel;
import com.napworks.steerlyfeapp.modelPackage.CouponsListMainModel;
import com.napworks.steerlyfeapp.modelPackage.RatingMainModel;
import com.napworks.steerlyfeapp.modelPackage.SearchMainModel;
import com.napworks.steerlyfeapp.modelPackage.StaticDataMainModel;
import com.napworks.steerlyfeapp.modelPackage.StoreDetailMainModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface WebApis {

    @GET("getStaticData.php")
    Call<StaticDataMainModel> getStaticData();

    @FormUrlEncoded
    @POST("signup.php")
    Call<LoginSignUpMainModel> signUp(@Field("phone_number") String phone_number,
                                      @Field("name") String name,
                                      @Field("email") String email,
                                      @Field("fcm_token") String fcm_token,
                                      @Field("api_token") String api_token,
                                      @Field("called_from") String called_from,
                                      @Field("calling_code") String calling_code);

    @FormUrlEncoded
    @POST("login.php")
    Call<LoginSignUpMainModel> login(@Field("phone_number") String phone_number,
                                     @Field("fcm_token") String fcm_token,
                                     @Field("api_token") String api_token,
                                     @Field("called_from") String called_from,
                                     @Field("calling_code") String calling_code);

    @FormUrlEncoded
    @POST("updateProfileInfo.php")
    Call<CommonStringModel> updateProfileInfo(@Header("Auth") String Auth,
                                              @Field("userId") String userId,
                                              @Field("sessionToken") String sessionToken,
                                              @Field("name") String name,
                                              @Field("email") String email);

    @FormUrlEncoded
    @POST("getHomeData.php")
    Call<HomeDataMainModel> getHomeData(@Header("Auth") String Auth,
                                        @Field("userId") String userId,
                                        @Field("sessionToken") String sessionToken);

    @FormUrlEncoded
    @POST("categoryProducts.php")
    Call<ProductCategoriesMainModel> getCategoryProducts(@Header("Auth") String Auth,
                                                         @Field("userId") String userId,
                                                         @Field("sessionToken") String sessionToken,
                                                         @Field("categoryId") String categoryId,
                                                         @Field("count") int count,
                                                         @Field("sortingType") String sortingType);


    @FormUrlEncoded
    @POST("getNearbyStores.php")
    Call<NearByStoresMainModel> getNearByStores(@Header("Auth") String Auth,
                                                @Field("userId") String userId,
                                                @Field("sessionToken") String sessionToken,
                                                @Field("lat") Double lat,
                                                @Field("lng") Double lng);

    @FormUrlEncoded
    @POST("storeDetail.php")
    Call<StoreDetailMainModel> getStoreDetail(@Header("Auth") String Auth,
                                              @Field("userId") String userId,
                                              @Field("sessionToken") String sessionToken,
                                              @Field("subStoreId") String subStoreId);

    @FormUrlEncoded
    @POST("searchProduct.php")
    Call<SearchMainModel> searchApi(@Header("Auth") String Auth,
                                    @Field("userId") String userId,
                                    @Field("sessionToken") String sessionToken,
                                    @Field("name") String name,
                                    @Field("count") int count,
                                    @Field("latLngExists") String latLngExists,
                                    @Field("lat") String lat,
                                    @Field("lng") String lng);

    @FormUrlEncoded
    @POST("submitStoreReview.php")
    Call<CommonStringModel> submitReview(@Header("Auth") String Auth,
                                         @Field("userId") String userId,
                                         @Field("sessionToken") String sessionToken,
                                         @Field("subStoreId") String subStoreId,
                                         @Field("rating") float rating,
                                         @Field("description") String description);

    @FormUrlEncoded
    @POST("getSubStoreProducts.php")
    Call<ProductCategoriesMainModel> getSubStoreProducts(@Header("Auth") String Auth,
                                                         @Field("userId") String userId,
                                                         @Field("sessionToken") String sessionToken,
                                                         @Field("storeId") String storeId,
                                                         @Field("subStoreId") String subStoreId,
                                                         @Field("count") int count);

    @FormUrlEncoded
    @POST("submitQuizQuestions.php")
    Call<CommonStringModel> submitQuizQuestions(@Header("Auth") String Auth,
                                                @Field("userId") String userId,
                                                @Field("sessionToken") String sessionToken,
                                                @Field("isSkipped") String isSkipped,
                                                @Field("quizResponse") String quizResponse);

    @FormUrlEncoded
    @POST("productDetail.php")
    Call<ProductDetailMainModel> getProductDetail(@Header("Auth") String Auth,
                                                  @Field("userId") String userId,
                                                  @Field("sessionToken") String sessionToken,
                                                  @Field("productId") String productId);


    @FormUrlEncoded
    @POST("allAddress.php")
    Call<AddressMainModel> getAddress(@Header("Auth") String Auth,
                                      @Field("userId") String userId,
                                      @Field("sessionToken") String sessionToken);

    @FormUrlEncoded
    @POST("changeDefaultAddress.php")
    Call<CommonStringModel> setDefaultAddress(@Header("Auth") String Auth,
                                              @Field("userId") String userId,
                                              @Field("sessionToken") String sessionToken,
                                              @Field("addressId") String addressId);

    @FormUrlEncoded
    @POST("addAddress.php")
    Call<CommonStringModel> addAddress(@Header("Auth") String Auth,
                                       @Field("userId") String userId,
                                       @Field("sessionToken") String sessionToken,
                                       @Field("address") String address,
                                       @Field("pincode") String pincode,
                                       @Field("city") String city,
                                       @Field("state") String state,
                                       @Field("calling_code") String calling_code,
                                       @Field("phone_number") String phone_number,
                                       @Field("country") String country,
                                       @Field("email") String email,
                                       @Field("name") String name,
                                       @Field("locality") String locality);

    @FormUrlEncoded
    @POST("deleteAddress.php")
    Call<CommonStringModel> deleteAddress(@Header("Auth") String Auth,
                                          @Field("userId") String userId,
                                          @Field("sessionToken") String sessionToken,
                                          @Field("addressId") String addressId);

    @FormUrlEncoded
    @POST("placeOrder.php")
    Call<PlaceOrderModel> placeOrder(@Header("Auth") String Auth,
                                     @Field("userId") String userId,
                                     @Field("sessionToken") String sessionToken,
                                     @Field("total_amount") double total_amount,
                                     @Field("discount_amount") double discount_amount,
                                     @Field("amount_paid") double amount_paid,
                                     @Field("coupon_used") String coupon_used,
                                     @Field("coupon_name") String coupon_name,
                                     @Field("coupon_discount") double coupon_discount,
                                     @Field("coupon_id") String coupon_id,
                                     @Field("coupon_count") String coupon_count,
                                     @Field("address_id") String address_id,
                                     @Field("payment_info") String payment_info,
                                     @Field("order_info") String order_info);

    @FormUrlEncoded
    @POST("orderHistory.php")
    Call<OrderHistoryMainModel> orderHistory(@Header("Auth") String Auth,
                                             @Field("userId") String userId,
                                             @Field("sessionToken") String sessionToken,
                                             @Field("count") int count);

    @FormUrlEncoded
    @POST("getPosts.php")
    Call<PostsMainModel> getPosts(@Header("Auth") String Auth,
                                  @Field("userId") String userId,
                                  @Field("sessionToken") String sessionToken,
                                  @Field("count") int count,
                                  @Field("categoryId") String categoryId);

    @FormUrlEncoded
    @POST("updatePostViews.php")
    Call<PostsMainModel> updatePostViews(@Header("Auth") String Auth,
                                         @Field("userId") String userId,
                                         @Field("sessionToken") String sessionToken,
                                         @Field("postIds") String postIds);

    @FormUrlEncoded
    @POST("savePost.php")
    Call<PostsMainModel> savePost(@Header("Auth") String Auth,
                                  @Field("userId") String userId,
                                  @Field("sessionToken") String sessionToken,
                                  @Field("post_id") String post_id,
                                  @Field("action") String action);


    @FormUrlEncoded
    @POST("getSavedPosts.php")
    Call<PostsMainModel> getSavedPosts(@Header("Auth") String Auth,
                                       @Field("userId") String userId,
                                       @Field("sessionToken") String sessionToken,
                                       @Field("count") int count);

    @FormUrlEncoded
    @POST("getAllPreviousOrderedProducts.php")
    Call<ProductCategoriesMainModel> getAllPreviousOrderedProducts(@Header("Auth") String Auth,
                                                                   @Field("userId") String userId,
                                                                   @Field("sessionToken") String sessionToken);

    @FormUrlEncoded
    @POST("getOrderFeedbackQuestions.php")
    Call<RatingMainModel> getOrderFeedbackQuestions(@Header("Auth") String Auth,
                                                    @Field("userId") String userId,
                                                    @Field("sessionToken") String sessionToken);

    @FormUrlEncoded
    @POST("submitOrderReview.php")
    Call<RatingMainModel> submitOrderReview(@Header("Auth") String Auth,
                                            @Field("userId") String userId,
                                            @Field("sessionToken") String sessionToken,
                                            @Field("order_id") String order_id,
                                            @Field("order_reviews") String order_reviews);

    @FormUrlEncoded
    @POST("getProductDetailWithBarcode.php")
    Call<ProductCategoriesMainModel> getProductDetailWithBarcode(@Header("Auth") String Auth,
                                                                 @Field("userId") String userId,
                                                                 @Field("sessionToken") String sessionToken,
                                                                 @Field("barcode") String barcode);


    @FormUrlEncoded
    @POST("getPostDetail.php")
    Call<PostDetailMainModel> getPostDetail(@Header("Auth") String Auth,
                                            @Field("userId") String userId,
                                            @Field("sessionToken") String sessionToken,
                                            @Field("post_id") String post_id);

    @FormUrlEncoded
    @POST("getCouponList.php")
    Call<CouponsListMainModel> getCouponList(@Header("Auth") String Auth,
                                             @Field("userId") String userId,
                                             @Field("sessionToken") String sessionToken,
                                             @Field("store_id_list") String store_id_list);

    @FormUrlEncoded
    @POST("checkCouponWithOrder.php")
    Call<CouponAppliedModel> checkCouponWithOrder(@Header("Auth") String Auth,
                                                  @Field("userId") String userId,
                                                  @Field("sessionToken") String sessionToken,
                                                  @Field("total_amount") String total_amount,
                                                  @Field("order_info") String order_info,
                                                  @Field("coupon_id") String coupon_id);

}