package com.napworks.steerlyfeapp.utilsPackage;

import android.app.Activity;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Constraints;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.napworks.steerlyfeapp.interfacePackage.AddAddressResponseInterface;
import com.napworks.steerlyfeapp.interfacePackage.AddressResponseInterface;
import com.napworks.steerlyfeapp.interfacePackage.AppliedCouponResponseInterface;
import com.napworks.steerlyfeapp.interfacePackage.CommonStringResponseInterface;
import com.napworks.steerlyfeapp.interfacePackage.CouponListResponseInterface;
import com.napworks.steerlyfeapp.interfacePackage.HomeDataResponseInterface;
import com.napworks.steerlyfeapp.interfacePackage.LoginSignUpResponseInterface;
import com.napworks.steerlyfeapp.interfacePackage.NearByStoresResponseInterface;
import com.napworks.steerlyfeapp.interfacePackage.OrderHistoryResponseInterface;
import com.napworks.steerlyfeapp.interfacePackage.PlaceOrderResponseInterface;
import com.napworks.steerlyfeapp.interfacePackage.PostDetailResponseInterface;
import com.napworks.steerlyfeapp.interfacePackage.PostsResponseInterface;
import com.napworks.steerlyfeapp.interfacePackage.ProductCategoryResponseInterface;
import com.napworks.steerlyfeapp.interfacePackage.ProductDetailResponseInterface;
import com.napworks.steerlyfeapp.interfacePackage.QuizQuestionsResponseInterface;
import com.napworks.steerlyfeapp.interfacePackage.RatingProductResponseInterface;
import com.napworks.steerlyfeapp.interfacePackage.SearchResponseInterface;
import com.napworks.steerlyfeapp.interfacePackage.StaticDataResponseInterface;
import com.napworks.steerlyfeapp.interfacePackage.StoreDetailResponseInterface;
import com.napworks.steerlyfeapp.interfacePackage.WebApis;
import com.napworks.steerlyfeapp.modelPackage.AddressMainModel;
import com.napworks.steerlyfeapp.modelPackage.CommonStringModel;
import com.napworks.steerlyfeapp.modelPackage.CouponAppliedModel;
import com.napworks.steerlyfeapp.modelPackage.HomeDataMainModel;
import com.napworks.steerlyfeapp.modelPackage.LoginSignUpMainModel;
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
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class CommonWebServices {

    private static final String TAG = "CommonWebServices";

    private static WebApis getRetrofitObject() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MyConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(WebApis.class);
    }


    public static WebApis getScalarRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MyConstants.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        return retrofit.create(WebApis.class);
    }


    public static void getStaticData(StaticDataResponseInterface staticDataResponseInterface) {
        Call<StaticDataMainModel> call = getRetrofitObject().getStaticData();
        call.enqueue(new Callback<StaticDataMainModel>() {
            @Override
            public void onResponse(@NonNull Call<StaticDataMainModel> call, @NonNull Response<StaticDataMainModel> response) {
                StaticDataMainModel staticDataMainModel = response.body();
                if (staticDataMainModel != null) {
                    staticDataResponseInterface.getStaticDataResponse(MyConstants.GO_TO_RESULT, staticDataMainModel);
                } else {
                    CommonMethods.showLog(TAG, "Static Data Api Null Response ");
                    staticDataResponseInterface.getStaticDataResponse(MyConstants.NULL_RESPONSE, null);

                }
            }

            @Override
            public void onFailure(@NonNull Call<StaticDataMainModel> call, @NonNull Throwable t) {
                CommonMethods.showLog(Constraints.TAG, "Static Data Api Exception  " + t.getMessage());
                staticDataResponseInterface.getStaticDataResponse(MyConstants.FAILURE_RESPONSE, null);
            }
        });

    }

    public static void signUp(SharedPreferences sharedPreferences,
                              String phoneNumber,
                              String name,
                              String email,
                              String callingCode,
                              final LoginSignUpResponseInterface loginSignUpResponseInterface) {
        Call<LoginSignUpMainModel> call = getRetrofitObject().
                signUp(phoneNumber,
                        name,
                        email,
                        sharedPreferences.getString(MyConstants.NOTIFICATION_TOKEN, ""),
                        MyConstants.SIGN_UP_API_TOKEN,
                        MyConstants.ANDROID,
                        callingCode);
        call.enqueue(new Callback<LoginSignUpMainModel>() {
            @Override
            public void onResponse(@NonNull Call<LoginSignUpMainModel> call, @NonNull Response<LoginSignUpMainModel> response) {
                LoginSignUpMainModel loginSignUpMainModel = response.body();
                if (loginSignUpMainModel != null) {
                    loginSignUpResponseInterface.getLoginSignUpResponse(MyConstants.GO_TO_RESULT, loginSignUpMainModel);
                } else {
                    CommonMethods.showLog(TAG, "Sign Up Api Null Response ");
                    loginSignUpResponseInterface.getLoginSignUpResponse(MyConstants.NULL_RESPONSE, null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginSignUpMainModel> call, @NonNull Throwable t) {
                CommonMethods.showLog(TAG, "Sign Up Api Exception " + t.getMessage());
                loginSignUpResponseInterface.getLoginSignUpResponse(MyConstants.FAILURE_RESPONSE, null);
            }
        });
    }

    public static void login(String phoneNumber,
                             String fcmToken,
                             String callingCode,
                             final LoginSignUpResponseInterface loginSignUpResponseInterface) {
        Call<LoginSignUpMainModel> call = getRetrofitObject().
                login(phoneNumber,
                        fcmToken,
                        MyConstants.LOGIN_API_TOKEN,
                        MyConstants.ANDROID,
                        callingCode);
        call.enqueue(new Callback<LoginSignUpMainModel>() {
            @Override
            public void onResponse(@NonNull Call<LoginSignUpMainModel> call, @NonNull Response<LoginSignUpMainModel> response) {
                LoginSignUpMainModel loginSignUpMainModel = response.body();
                if (loginSignUpMainModel != null) {
                    loginSignUpResponseInterface.getLoginSignUpResponse(MyConstants.GO_TO_RESULT, loginSignUpMainModel);
                } else {
                    CommonMethods.showLog(TAG, "Login Api Null Response ");
                    loginSignUpResponseInterface.getLoginSignUpResponse(MyConstants.NULL_RESPONSE, null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginSignUpMainModel> call, @NonNull Throwable t) {
                CommonMethods.showLog(TAG, "Login Api Exception " + t.getMessage());
                loginSignUpResponseInterface.getLoginSignUpResponse(MyConstants.FAILURE_RESPONSE, null);
            }
        });
    }

    public static void updateProfileInfo(SharedPreferences sharedPreferences,
                                         String name,
                                         String email,
                                         CommonStringResponseInterface commonStringResponseInterface) {
        Call<CommonStringModel> call = getRetrofitObject().
                updateProfileInfo(sharedPreferences.getString(MyConstants.AUTHORIZATION, ""),
                        sharedPreferences.getString(MyConstants.USER_ID, ""),
                        sharedPreferences.getString(MyConstants.SESSION, ""),
                        name,
                        email);
        call.enqueue(new Callback<CommonStringModel>() {
            @Override
            public void onResponse(@NonNull Call<CommonStringModel> call, @NonNull Response<CommonStringModel> response) {
                CommonStringModel commonStringModel = response.body();
                if (commonStringModel != null) {
                    commonStringResponseInterface.getCommonStringResponse(MyConstants.GO_TO_RESULT, commonStringModel);
                } else {
                    CommonMethods.showLog(TAG, "Update Profile Api Null Response ");
                    commonStringResponseInterface.getCommonStringResponse(MyConstants.NULL_RESPONSE, null);

                }
            }

            @Override
            public void onFailure(@NonNull Call<CommonStringModel> call, @NonNull Throwable t) {
                CommonMethods.showLog(Constraints.TAG, "Update Profile Api Exception  " + t.getMessage());
                commonStringResponseInterface.getCommonStringResponse(MyConstants.FAILURE_RESPONSE, null);
            }
        });
    }


    public static void getHomeData(SharedPreferences sharedPreferences,
                                   HomeDataResponseInterface homeDataResponseInterface) {
        Call<HomeDataMainModel> call = getRetrofitObject().
                getHomeData(sharedPreferences.getString(MyConstants.AUTHORIZATION, ""),
                        sharedPreferences.getString(MyConstants.USER_ID, ""),
                        sharedPreferences.getString(MyConstants.SESSION, ""));
        call.enqueue(new Callback<HomeDataMainModel>() {
            @Override
            public void onResponse(@NonNull Call<HomeDataMainModel> call, @NonNull Response<HomeDataMainModel> response) {
                HomeDataMainModel homeDataMainModel = response.body();
                if (homeDataMainModel != null) {
                    homeDataResponseInterface.getHomeDataResponse(MyConstants.GO_TO_RESULT, homeDataMainModel);
                } else {
                    CommonMethods.showLog(TAG, "Home Data Api Null Response ");
                    homeDataResponseInterface.getHomeDataResponse(MyConstants.NULL_RESPONSE, null);

                }
            }

            @Override
            public void onFailure(@NonNull Call<HomeDataMainModel> call, @NonNull Throwable t) {
                CommonMethods.showLog(Constraints.TAG, "Home Data Api Exception  " + t.getMessage());
                homeDataResponseInterface.getHomeDataResponse(MyConstants.FAILURE_RESPONSE, null);
            }
        });

    }

    public static void getCategoryProducts(SharedPreferences sharedPreferences,
                                           String categoryId,
                                           int count,
                                           String sortingType,
                                           ProductCategoryResponseInterface productCategoryResponseInterface) {

        Call<ProductCategoriesMainModel> call = getRetrofitObject().
                getCategoryProducts(sharedPreferences.getString(MyConstants.AUTHORIZATION, ""),
                        sharedPreferences.getString(MyConstants.USER_ID, ""),
                        sharedPreferences.getString(MyConstants.SESSION, ""),
                        categoryId,
                        count,
                        sortingType);
        call.enqueue(new Callback<ProductCategoriesMainModel>() {
            @Override
            public void onResponse(@NonNull Call<ProductCategoriesMainModel> call, @NonNull Response<ProductCategoriesMainModel> response) {
                ProductCategoriesMainModel productCategoriesMainModel = response.body();
                if (productCategoriesMainModel != null) {
                    productCategoryResponseInterface.getProductCategoryResponse(MyConstants.GO_TO_RESULT, productCategoriesMainModel);
                } else {
                    CommonMethods.showLog(TAG, "Product Category Api Null Response ");
                    productCategoryResponseInterface.getProductCategoryResponse(MyConstants.NULL_RESPONSE, null);

                }
            }

            @Override
            public void onFailure(@NonNull Call<ProductCategoriesMainModel> call, @NonNull Throwable t) {
                CommonMethods.showLog(Constraints.TAG, "Product Category Api Exception  " + t.getMessage());
                productCategoryResponseInterface.getProductCategoryResponse(MyConstants.FAILURE_RESPONSE, null);
            }
        });
    }

    public static void getNearbyStores(SharedPreferences sharedPreferences,
                                       Double lat,
                                       Double lng,
                                       NearByStoresResponseInterface nearByStoresResponseInterface) {

        Call<NearByStoresMainModel> call = getRetrofitObject().
                getNearByStores(sharedPreferences.getString(MyConstants.AUTHORIZATION, ""),
                        sharedPreferences.getString(MyConstants.USER_ID, ""),
                        sharedPreferences.getString(MyConstants.SESSION, ""),
                        lat,
                        lng);
        call.enqueue(new Callback<NearByStoresMainModel>() {
            @Override
            public void onResponse(@NonNull Call<NearByStoresMainModel> call, @NonNull Response<NearByStoresMainModel> response) {
                NearByStoresMainModel nearByStoresMainModel = response.body();
                if (nearByStoresMainModel != null) {
                    nearByStoresResponseInterface.getNearByStoresResponse(MyConstants.GO_TO_RESULT, nearByStoresMainModel);
                } else {
                    CommonMethods.showLog(TAG, "NearBy Stores Api Null Response ");
                    nearByStoresResponseInterface.getNearByStoresResponse(MyConstants.NULL_RESPONSE, null);

                }
            }

            @Override
            public void onFailure(@NonNull Call<NearByStoresMainModel> call, @NonNull Throwable t) {
                CommonMethods.showLog(Constraints.TAG, "NearBy Stores Api Exception  " + t.getMessage());
                nearByStoresResponseInterface.getNearByStoresResponse(MyConstants.FAILURE_RESPONSE, null);
            }
        });
    }

    public static void getStoreDetail(SharedPreferences sharedPreferences,
                                      String subStoreId,
                                      StoreDetailResponseInterface storeDetailResponseInterface) {

        Call<StoreDetailMainModel> call = getRetrofitObject().
                getStoreDetail(sharedPreferences.getString(MyConstants.AUTHORIZATION, ""),
                        sharedPreferences.getString(MyConstants.USER_ID, ""),
                        sharedPreferences.getString(MyConstants.SESSION, ""),
                        subStoreId);
        call.enqueue(new Callback<StoreDetailMainModel>() {
            @Override
            public void onResponse(@NonNull Call<StoreDetailMainModel> call, @NonNull Response<StoreDetailMainModel> response) {
                StoreDetailMainModel storeDetailMainModel = response.body();
                if (storeDetailMainModel != null) {
                    storeDetailResponseInterface.getStoreDetailResponse(MyConstants.GO_TO_RESULT, storeDetailMainModel);
                } else {
                    CommonMethods.showLog(TAG, "NearBy Stores Api Null Response ");
                    storeDetailResponseInterface.getStoreDetailResponse(MyConstants.NULL_RESPONSE, null);

                }
            }

            @Override
            public void onFailure(@NonNull Call<StoreDetailMainModel> call, @NonNull Throwable t) {
                CommonMethods.showLog(Constraints.TAG, "NearBy Stores Api Exception  " + t.getMessage());
                storeDetailResponseInterface.getStoreDetailResponse(MyConstants.FAILURE_RESPONSE, null);
            }
        });

    }

    public static void searchApi(SharedPreferences sharedPreferences,
                                 String name,
                                 int count,
                                 String latLngExists,
                                 String lat,
                                 String lng,
                                 SearchResponseInterface searchResponseInterface) {

        CommonMethods.showLog(TAG, "lat: " + latLngExists);

        Call<SearchMainModel> call = getRetrofitObject().
                searchApi(sharedPreferences.getString(MyConstants.AUTHORIZATION, ""),
                        sharedPreferences.getString(MyConstants.USER_ID, ""),
                        sharedPreferences.getString(MyConstants.SESSION, ""),
                        name,
                        count,
                        latLngExists,
                        lat,
                        lng);
        call.enqueue(new Callback<SearchMainModel>() {
            @Override
            public void onResponse(@NonNull Call<SearchMainModel> call, @NonNull Response<SearchMainModel> response) {
                SearchMainModel productCategoriesMainModel = response.body();
                if (productCategoriesMainModel != null) {
                    searchResponseInterface.getSearchResponse(MyConstants.GO_TO_RESULT, productCategoriesMainModel);
                } else {
                    CommonMethods.showLog(TAG, "searchApi Null Response ");
                    searchResponseInterface.getSearchResponse(MyConstants.NULL_RESPONSE, null);

                }
            }

            @Override
            public void onFailure(@NonNull Call<SearchMainModel> call, @NonNull Throwable t) {
                CommonMethods.showLog(Constraints.TAG, "searchApi Exception  " + t.getMessage());
                searchResponseInterface.getSearchResponse(MyConstants.FAILURE_RESPONSE, null);
            }
        });
    }

    public static void submitReview(SharedPreferences sharedPreferences,
                                    String subStoreId,
                                    float rating,
                                    String description,
                                    CommonStringResponseInterface commonStringResponseInterface) {

        Call<CommonStringModel> call = getRetrofitObject().
                submitReview(sharedPreferences.getString(MyConstants.AUTHORIZATION, ""),
                        sharedPreferences.getString(MyConstants.USER_ID, ""),
                        sharedPreferences.getString(MyConstants.SESSION, ""),
                        subStoreId,
                        rating,
                        description);
        call.enqueue(new Callback<CommonStringModel>() {
            @Override
            public void onResponse(@NonNull Call<CommonStringModel> call, @NonNull Response<CommonStringModel> response) {
                CommonStringModel commonStringModel = response.body();
                if (commonStringModel != null) {
                    commonStringResponseInterface.getCommonStringResponse(MyConstants.GO_TO_RESULT, commonStringModel);
                } else {
                    CommonMethods.showLog(TAG, "Submit Review Api Null Response ");
                    commonStringResponseInterface.getCommonStringResponse(MyConstants.NULL_RESPONSE, null);

                }
            }

            @Override
            public void onFailure(@NonNull Call<CommonStringModel> call, @NonNull Throwable t) {
                CommonMethods.showLog(Constraints.TAG, "Submit Review Api Exception  " + t.getMessage());
                commonStringResponseInterface.getCommonStringResponse(MyConstants.FAILURE_RESPONSE, null);
            }
        });
    }

    public static void getSubStoreProducts(SharedPreferences sharedPreferences,
                                           String storeId,
                                           String subStoreId,
                                           int count,
                                           ProductCategoryResponseInterface productCategoryResponseInterface) {

        Call<ProductCategoriesMainModel> call = getRetrofitObject().
                getSubStoreProducts(sharedPreferences.getString(MyConstants.AUTHORIZATION, ""),
                        sharedPreferences.getString(MyConstants.USER_ID, ""),
                        sharedPreferences.getString(MyConstants.SESSION, ""),
                        storeId,
                        subStoreId,
                        count);
        call.enqueue(new Callback<ProductCategoriesMainModel>() {
            @Override
            public void onResponse(@NonNull Call<ProductCategoriesMainModel> call, @NonNull Response<ProductCategoriesMainModel> response) {
                ProductCategoriesMainModel productCategoriesMainModel = response.body();
                if (productCategoriesMainModel != null) {
                    productCategoryResponseInterface.getProductCategoryResponse(MyConstants.GO_TO_RESULT, productCategoriesMainModel);
                } else {
                    CommonMethods.showLog(TAG, "SubStore Product Api Null Response ");
                    productCategoryResponseInterface.getProductCategoryResponse(MyConstants.NULL_RESPONSE, null);

                }
            }

            @Override
            public void onFailure(@NonNull Call<ProductCategoriesMainModel> call, @NonNull Throwable t) {
                CommonMethods.showLog(Constraints.TAG, "SubStore Product Api Exception  " + t.getMessage());
                productCategoryResponseInterface.getProductCategoryResponse(MyConstants.FAILURE_RESPONSE, null);
            }
        });
    }

    public static void submitQuizQuestions(SharedPreferences sharedPreferences,
                                           String isSkipped,
                                           String jsonResponse,
                                           QuizQuestionsResponseInterface quizQuestionsResponseInterface) {

        Call<CommonStringModel> call = getRetrofitObject().
                submitQuizQuestions(sharedPreferences.getString(MyConstants.AUTHORIZATION, ""),
                        sharedPreferences.getString(MyConstants.USER_ID, ""),
                        sharedPreferences.getString(MyConstants.SESSION, ""),
                        isSkipped,
                        jsonResponse);
        call.enqueue(new Callback<CommonStringModel>() {
            @Override
            public void onResponse(@NonNull Call<CommonStringModel> call, @NonNull Response<CommonStringModel> response) {
                CommonStringModel commonStringModel = response.body();
                if (commonStringModel != null) {
                    quizQuestionsResponseInterface.getQuizQuestionsResponse(MyConstants.GO_TO_RESULT, commonStringModel, isSkipped);
                } else {
                    CommonMethods.showLog(TAG, "Submit Quiz Questions Api Null Response ");
                    quizQuestionsResponseInterface.getQuizQuestionsResponse(MyConstants.NULL_RESPONSE, null, "");

                }
            }

            @Override
            public void onFailure(@NonNull Call<CommonStringModel> call, @NonNull Throwable t) {
                CommonMethods.showLog(Constraints.TAG, "Submit Quiz Questions Api Exception  " + t.getMessage());
                quizQuestionsResponseInterface.getQuizQuestionsResponse(MyConstants.FAILURE_RESPONSE, null, "");
            }
        });
    }

    public static void getProductDetail(SharedPreferences sharedPreferences,
                                        String productId,
                                        ProductDetailResponseInterface productDetailResponseInterface) {

        Call<ProductDetailMainModel> call = getRetrofitObject().
                getProductDetail(sharedPreferences.getString(MyConstants.AUTHORIZATION, ""),
                        sharedPreferences.getString(MyConstants.USER_ID, ""),
                        sharedPreferences.getString(MyConstants.SESSION, ""),
                        productId);
        call.enqueue(new Callback<ProductDetailMainModel>() {
            @Override
            public void onResponse(@NonNull Call<ProductDetailMainModel> call, @NonNull Response<ProductDetailMainModel> response) {
                ProductDetailMainModel productDetailMainModel = response.body();
                if (productDetailMainModel != null) {
                    productDetailResponseInterface.getProductDetailResponse(MyConstants.GO_TO_RESULT, productDetailMainModel);
                } else {
                    CommonMethods.showLog(TAG, "getProductDetail Api Null Response ");
                    productDetailResponseInterface.getProductDetailResponse(MyConstants.NULL_RESPONSE, null);

                }
            }

            @Override
            public void onFailure(@NonNull Call<ProductDetailMainModel> call, @NonNull Throwable t) {
                CommonMethods.showLog(Constraints.TAG, "getProductDetail Api Exception  " + t.getMessage());
                productDetailResponseInterface.getProductDetailResponse(MyConstants.FAILURE_RESPONSE, null);
            }
        });

    }

    public static void getAddress(SharedPreferences sharedPreferences,
                                  AddressResponseInterface addressResponseInterface) {

        Call<AddressMainModel> call = getRetrofitObject().
                getAddress(sharedPreferences.getString(MyConstants.AUTHORIZATION, ""),
                        sharedPreferences.getString(MyConstants.USER_ID, ""),
                        sharedPreferences.getString(MyConstants.SESSION, ""));
        call.enqueue(new Callback<AddressMainModel>() {
            @Override
            public void onResponse(@NonNull Call<AddressMainModel> call, @NonNull Response<AddressMainModel> response) {
                AddressMainModel addressMainModel = response.body();
                if (addressMainModel != null) {
                    addressResponseInterface.getAddressResponse(MyConstants.GO_TO_RESULT, addressMainModel);
                } else {
                    CommonMethods.showLog(TAG, "getAddressResponse Api Null Response ");
                    addressResponseInterface.getAddressResponse(MyConstants.NULL_RESPONSE, null);

                }
            }

            @Override
            public void onFailure(@NonNull Call<AddressMainModel> call, @NonNull Throwable t) {
                CommonMethods.showLog(Constraints.TAG, "getAddressResponse Api Exception  " + t.getMessage());
                addressResponseInterface.getAddressResponse(MyConstants.FAILURE_RESPONSE, null);
            }
        });
    }

    public static void setDefaultAddress(SharedPreferences sharedPreferences,
                                         String addressId,
                                         final CommonStringResponseInterface commonStringResponseInterface) {

        Call<CommonStringModel> call = getRetrofitObject().
                setDefaultAddress(sharedPreferences.getString(MyConstants.AUTHORIZATION, ""),
                        sharedPreferences.getString(MyConstants.USER_ID, ""),
                        sharedPreferences.getString(MyConstants.SESSION, ""),
                        addressId);
        call.enqueue(new Callback<CommonStringModel>() {
            @Override
            public void onResponse(@NonNull Call<CommonStringModel> call, @NonNull Response<CommonStringModel> response) {
                CommonStringModel commonStringModel = response.body();
                if (commonStringModel != null) {
                    commonStringResponseInterface.getCommonStringResponse(MyConstants.GO_TO_RESULT, commonStringModel);
                } else {
                    commonStringResponseInterface.getCommonStringResponse(MyConstants.NULL_RESPONSE, null);
                    CommonMethods.showLog(TAG, "Set Default  Address No Response");
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommonStringModel> call, @NonNull Throwable t) {
                CommonMethods.showLog(TAG, "Set Default  Address  Exception " + t.getMessage());
                commonStringResponseInterface.getCommonStringResponse(MyConstants.FAILURE_RESPONSE, null);
            }
        });
    }

    public static void addAddress(SharedPreferences sharedPreferences,
                                  String address,
                                  String pincode,
                                  String city,
                                  String state,
                                  String callingCode,
                                  String phoneNumber,
                                  String country,
                                  String email,
                                  String name,
                                  String locality,
                                  AddAddressResponseInterface addAddressResponseInterface) {

        Call<CommonStringModel> call = getRetrofitObject().
                addAddress(sharedPreferences.getString(MyConstants.AUTHORIZATION, ""),
                        sharedPreferences.getString(MyConstants.USER_ID, ""),
                        sharedPreferences.getString(MyConstants.SESSION, ""),
                        address,
                        pincode,
                        city,
                        state,
                        callingCode,
                        phoneNumber,
                        country,
                        email,
                        name,
                        locality);
        call.enqueue(new Callback<CommonStringModel>() {
            @Override
            public void onResponse(@NonNull Call<CommonStringModel> call, @NonNull Response<CommonStringModel> response) {
                CommonStringModel commonStringModel = response.body();
                if (commonStringModel != null) {
                    addAddressResponseInterface.getAddAddressResponse(MyConstants.GO_TO_RESULT, commonStringModel);
                } else {
                    CommonMethods.showLog(TAG, "getAddressResponse Api Null Response ");
                    addAddressResponseInterface.getAddAddressResponse(MyConstants.NULL_RESPONSE, null);

                }
            }

            @Override
            public void onFailure(@NonNull Call<CommonStringModel> call, @NonNull Throwable t) {
                CommonMethods.showLog(Constraints.TAG, "getAddressResponse Api Exception  " + t.getMessage());
                addAddressResponseInterface.getAddAddressResponse(MyConstants.FAILURE_RESPONSE, null);
            }
        });
    }

    public static void deleteAddress(SharedPreferences sharedPreferences,
                                     String addressId,
                                     final CommonStringResponseInterface commonStringResponseInterface) {

        Call<CommonStringModel> call = getRetrofitObject().
                deleteAddress(sharedPreferences.getString(MyConstants.AUTHORIZATION, ""),
                        sharedPreferences.getString(MyConstants.USER_ID, ""),
                        sharedPreferences.getString(MyConstants.SESSION, ""),
                        addressId);
        call.enqueue(new Callback<CommonStringModel>() {
            @Override
            public void onResponse(@NonNull Call<CommonStringModel> call, @NonNull Response<CommonStringModel> response) {
                CommonStringModel commonStringModel = response.body();
                if (commonStringModel != null) {
                    commonStringResponseInterface.getCommonStringResponse(MyConstants.GO_TO_RESULT_DELETE_ADDRESS, commonStringModel);
                } else {
                    commonStringResponseInterface.getCommonStringResponse(MyConstants.NULL_RESPONSE, null);
                    CommonMethods.showLog(TAG, "Set Default  Address No Response");
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommonStringModel> call, @NonNull Throwable t) {
                CommonMethods.showLog(TAG, "Set Default  Address  Exception " + t.getMessage());
                commonStringResponseInterface.getCommonStringResponse(MyConstants.FAILURE_RESPONSE, null);
            }
        });
    }


    public static void placeOrder(SharedPreferences sharedPreferences,
                                  double totalAmount,
                                  double discountAmount,
                                  double amountPaid,
                                  String couponUsed,
                                  String couponName,
                                  double couponDiscount,
                                  String couponId,
                                  String couponCount,
                                  String addressId,
                                  String paymentInfo,
                                  String orderInfo,
                                  PlaceOrderResponseInterface placeOrderResponseInterface) {

        Call<PlaceOrderModel> call = getRetrofitObject().
                placeOrder(sharedPreferences.getString(MyConstants.AUTHORIZATION, ""),
                        sharedPreferences.getString(MyConstants.USER_ID, ""),
                        sharedPreferences.getString(MyConstants.SESSION, ""),
                        totalAmount,
                        discountAmount,
                        amountPaid,
                        couponUsed,
                        couponName,
                        couponDiscount,
                        couponId,
                        couponCount,
                        addressId,
                        paymentInfo,
                        orderInfo
                );
        call.enqueue(new Callback<PlaceOrderModel>() {
            @Override
            public void onResponse(@NonNull Call<PlaceOrderModel> call, @NonNull Response<PlaceOrderModel> response) {
                PlaceOrderModel placeOrderModel = response.body();
                if (placeOrderModel != null) {
                    placeOrderResponseInterface.getPlaceOrderResponse(MyConstants.GO_TO_RESULT, placeOrderModel);
                } else {
                    placeOrderResponseInterface.getPlaceOrderResponse(MyConstants.NULL_RESPONSE, null);
                    CommonMethods.showLog(TAG, "Set Default  Address No Response");
                }
            }

            @Override
            public void onFailure(@NonNull Call<PlaceOrderModel> call, @NonNull Throwable t) {
                CommonMethods.showLog(TAG, "placeOrder  Exception " + t.getMessage());
                placeOrderResponseInterface.getPlaceOrderResponse(MyConstants.FAILURE_RESPONSE, null);
            }

        });
    }

    public static void getOrderHistory(SharedPreferences sharedPreferences,
                                       int count,
                                       OrderHistoryResponseInterface orderHistoryResponseInterface) {

        Call<OrderHistoryMainModel> call = getRetrofitObject().
                orderHistory(sharedPreferences.getString(MyConstants.AUTHORIZATION, ""),
                        sharedPreferences.getString(MyConstants.USER_ID, ""),
                        sharedPreferences.getString(MyConstants.SESSION, ""),
                        count);
        call.enqueue(new Callback<OrderHistoryMainModel>() {
            @Override
            public void onResponse(@NonNull Call<OrderHistoryMainModel> call, @NonNull Response<OrderHistoryMainModel> response) {
                OrderHistoryMainModel productCategoriesMainModel = response.body();
                if (productCategoriesMainModel != null) {
                    orderHistoryResponseInterface.getOrderHistoryResponse(MyConstants.GO_TO_RESULT, productCategoriesMainModel);
                } else {
                    CommonMethods.showLog(TAG, "OrderHistory Api Null Response ");
                    orderHistoryResponseInterface.getOrderHistoryResponse(MyConstants.NULL_RESPONSE, null);

                }
            }

            @Override
            public void onFailure(@NonNull Call<OrderHistoryMainModel> call, @NonNull Throwable t) {
                CommonMethods.showLog(Constraints.TAG, "OrderHistory Api Exception  " + t.getMessage());
                orderHistoryResponseInterface.getOrderHistoryResponse(MyConstants.FAILURE_RESPONSE, null);
            }
        });
    }

    public static void getPosts(SharedPreferences sharedPreferences,
                                int count,
                                String categoryId,
                                PostsResponseInterface postsResponseInterface) {

        Call<PostsMainModel> call = getRetrofitObject().
                getPosts(sharedPreferences.getString(MyConstants.AUTHORIZATION, ""),
                        sharedPreferences.getString(MyConstants.USER_ID, ""),
                        sharedPreferences.getString(MyConstants.SESSION, ""),
                        count,
                        categoryId);
        call.enqueue(new Callback<PostsMainModel>() {
            @Override
            public void onResponse(@NonNull Call<PostsMainModel> call, @NonNull Response<PostsMainModel> response) {
                PostsMainModel postsMainModel = response.body();
                if (postsMainModel != null) {
                    postsResponseInterface.getPostsResponse(MyConstants.GO_TO_RESULT, postsMainModel);
                } else {
                    CommonMethods.showLog(TAG, "getPosts Api Null Response ");
                    postsResponseInterface.getPostsResponse(MyConstants.NULL_RESPONSE, null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<PostsMainModel> call, @NonNull Throwable t) {
                CommonMethods.showLog(Constraints.TAG, "getPosts Api Exception  " + t.getMessage());
                postsResponseInterface.getPostsResponse(MyConstants.FAILURE_RESPONSE, null);
            }
        });
    }

    public static void updatePostViews(SharedPreferences sharedPreferences,
                                       String postIds) {

        Call<PostsMainModel> call = getRetrofitObject().
                updatePostViews(sharedPreferences.getString(MyConstants.AUTHORIZATION, ""),
                        sharedPreferences.getString(MyConstants.USER_ID, ""),
                        sharedPreferences.getString(MyConstants.SESSION, ""),
                        postIds);
        call.enqueue(new Callback<PostsMainModel>() {
            @Override
            public void onResponse(@NonNull Call<PostsMainModel> call, @NonNull Response<PostsMainModel> response) {
                PostsMainModel postsMainModel = response.body();
                if (postsMainModel != null) {
                    CommonMethods.showLog(TAG, "updatePostViews Api Response " + postsMainModel.getStatus());
                } else {
                    CommonMethods.showLog(TAG, "updatePostViews Null Response ");
                }
            }

            @Override
            public void onFailure(@NonNull Call<PostsMainModel> call, @NonNull Throwable t) {
                CommonMethods.showLog(Constraints.TAG, "getPosts Api Exception  " + t.getMessage());
            }
        });
    }

    public static void savePost(SharedPreferences sharedPreferences,
                                String postId,
                                String action) {

        CommonMethods.showLog(TAG, "Action : " + action + " Post Id : " + postId);

        Call<PostsMainModel> call = getRetrofitObject().
                savePost(sharedPreferences.getString(MyConstants.AUTHORIZATION, ""),
                        sharedPreferences.getString(MyConstants.USER_ID, ""),
                        sharedPreferences.getString(MyConstants.SESSION, ""),
                        postId,
                        action);
        call.enqueue(new Callback<PostsMainModel>() {
            @Override
            public void onResponse(@NonNull Call<PostsMainModel> call, @NonNull Response<PostsMainModel> response) {
                PostsMainModel postsMainModel = response.body();
                if (postsMainModel != null) {
                    CommonMethods.showLog(TAG, "savePosts Api Response " + postsMainModel.getStatus());
                } else {
                    CommonMethods.showLog(TAG, "savePosts Null Response ");
                }
            }

            @Override
            public void onFailure(@NonNull Call<PostsMainModel> call, @NonNull Throwable t) {
                CommonMethods.showLog(Constraints.TAG, "getPosts Api Exception  " + t.getMessage());
            }
        });
    }

    public static void getSavedPosts(SharedPreferences sharedPreferences,
                                     int count,
                                     PostsResponseInterface postsResponseInterface) {

        Call<PostsMainModel> call = getRetrofitObject().
                getSavedPosts(sharedPreferences.getString(MyConstants.AUTHORIZATION, ""),
                        sharedPreferences.getString(MyConstants.USER_ID, ""),
                        sharedPreferences.getString(MyConstants.SESSION, ""),
                        count);
        call.enqueue(new Callback<PostsMainModel>() {
            @Override
            public void onResponse(@NonNull Call<PostsMainModel> call, @NonNull Response<PostsMainModel> response) {
                PostsMainModel postsMainModel = response.body();
                if (postsMainModel != null) {
                    postsResponseInterface.getPostsResponse(MyConstants.GO_TO_RESULT, postsMainModel);
                } else {
                    CommonMethods.showLog(TAG, "getPosts Api Null Response ");
                    postsResponseInterface.getPostsResponse(MyConstants.NULL_RESPONSE, null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<PostsMainModel> call, @NonNull Throwable t) {
                CommonMethods.showLog(Constraints.TAG, "getPosts Api Exception  " + t.getMessage());
                postsResponseInterface.getPostsResponse(MyConstants.FAILURE_RESPONSE, null);
            }
        });
    }

    public static void getAllPreviousOrderedProducts(SharedPreferences sharedPreferences,
                                                     ProductCategoryResponseInterface productCategoryResponseInterface) {

        Call<ProductCategoriesMainModel> call = getRetrofitObject().
                getAllPreviousOrderedProducts(sharedPreferences.getString(MyConstants.AUTHORIZATION, ""),
                        sharedPreferences.getString(MyConstants.USER_ID, ""),
                        sharedPreferences.getString(MyConstants.SESSION, ""));
        call.enqueue(new Callback<ProductCategoriesMainModel>() {
            @Override
            public void onResponse(@NonNull Call<ProductCategoriesMainModel> call, @NonNull Response<ProductCategoriesMainModel> response) {
                ProductCategoriesMainModel productCategoriesMainModel = response.body();
                if (productCategoriesMainModel != null) {
                    productCategoryResponseInterface.getProductCategoryResponse(MyConstants.GO_TO_RESULT, productCategoriesMainModel);
                } else {
                    CommonMethods.showLog(TAG, "getAllPreviousOrderedProducts Api Null Response ");
                    productCategoryResponseInterface.getProductCategoryResponse(MyConstants.NULL_RESPONSE, null);

                }
            }

            @Override
            public void onFailure(@NonNull Call<ProductCategoriesMainModel> call, @NonNull Throwable t) {
                CommonMethods.showLog(Constraints.TAG, "getAllPreviousOrderedProducts Api Exception  " + t.getMessage());
                productCategoryResponseInterface.getProductCategoryResponse(MyConstants.FAILURE_RESPONSE, null);
            }
        });

    }

    public static void getOrderFeedbackQuestions(SharedPreferences sharedPreferences,
                                                 RatingProductResponseInterface ratingProductResponseInterface) {

        Call<RatingMainModel> call = getRetrofitObject().
                getOrderFeedbackQuestions(sharedPreferences.getString(MyConstants.AUTHORIZATION, ""),
                        sharedPreferences.getString(MyConstants.USER_ID, ""),
                        sharedPreferences.getString(MyConstants.SESSION, ""));
        call.enqueue(new Callback<RatingMainModel>() {
            @Override
            public void onResponse(@NonNull Call<RatingMainModel> call, @NonNull Response<RatingMainModel> response) {
                RatingMainModel ratingMainModel = response.body();
                if (ratingMainModel != null) {
                    ratingProductResponseInterface.getRatingProductResponse(MyConstants.GO_TO_RESULT, ratingMainModel);
                } else {
                    CommonMethods.showLog(TAG, "getOrderFeedbackQuestions Api Null Response ");
                    ratingProductResponseInterface.getRatingProductResponse(MyConstants.NULL_RESPONSE, null);

                }
            }

            @Override
            public void onFailure(@NonNull Call<RatingMainModel> call, @NonNull Throwable t) {
                CommonMethods.showLog(Constraints.TAG, "getOrderFeedbackQuestions Api Exception  " + t.getMessage());
                ratingProductResponseInterface.getRatingProductResponse(MyConstants.FAILURE_RESPONSE, null);
            }
        });

    }


    public static void submitOrderReview(SharedPreferences sharedPreferences,
                                         String orderReviews,
                                         String orderId,
                                         Activity activity,
                                         SwipeRefreshLayout swipeContainer) {

        Call<RatingMainModel> call = getRetrofitObject().
                submitOrderReview(sharedPreferences.getString(MyConstants.AUTHORIZATION, ""),
                        sharedPreferences.getString(MyConstants.USER_ID, ""),
                        sharedPreferences.getString(MyConstants.SESSION, ""),
                        orderId,
                        orderReviews);
        call.enqueue(new Callback<RatingMainModel>() {
            @Override
            public void onResponse(@NonNull Call<RatingMainModel> call, @NonNull Response<RatingMainModel> response) {
                swipeContainer.setRefreshing(false);
                RatingMainModel postsMainModel = response.body();
                if (postsMainModel != null) {
                    CommonMethods.showLog(TAG, "addProductReviews Api Response " + postsMainModel.getStatus());
                    activity.setResult(Activity.RESULT_OK);
                    activity.finish();
                    activity.overridePendingTransition(0, 0);
                    Toast.makeText(activity, postsMainModel.getMessage(), Toast.LENGTH_SHORT).show();

                } else {
                    CommonMethods.showLog(TAG, "addProductReviews Null Response ");
                }
            }

            @Override
            public void onFailure(@NonNull Call<RatingMainModel> call, @NonNull Throwable t) {
                swipeContainer.setRefreshing(false);
                CommonMethods.showLog(Constraints.TAG, "addProductReviews Api Exception  " + t.getMessage());
            }
        });
    }

    public static void getProductDetailWithBarcode(SharedPreferences sharedPreferences, String barcode,
                                                   ProductCategoryResponseInterface productCategoryResponseInterface) {
        Call<ProductCategoriesMainModel> call = getRetrofitObject().
                getProductDetailWithBarcode(sharedPreferences.getString(MyConstants.AUTHORIZATION, ""),
                        sharedPreferences.getString(MyConstants.USER_ID, ""),
                        sharedPreferences.getString(MyConstants.SESSION, ""),
                        barcode);
        call.enqueue(new Callback<ProductCategoriesMainModel>() {
            @Override
            public void onResponse(@NonNull Call<ProductCategoriesMainModel> call, @NonNull Response<ProductCategoriesMainModel> response) {
                ProductCategoriesMainModel productDetailMainModel = response.body();
                if (productDetailMainModel != null) {
                    productCategoryResponseInterface.getProductCategoryResponse(MyConstants.GO_TO_RESULT, productDetailMainModel);
                } else {
                    CommonMethods.showLog(TAG, "getProductDetail Api Null Response ");
                    productCategoryResponseInterface.getProductCategoryResponse(MyConstants.NULL_RESPONSE, null);

                }
            }

            @Override
            public void onFailure(@NonNull Call<ProductCategoriesMainModel> call, @NonNull Throwable t) {
                CommonMethods.showLog(Constraints.TAG, "getProductDetail Api Exception  " + t.getMessage());
                productCategoryResponseInterface.getProductCategoryResponse(MyConstants.FAILURE_RESPONSE, null);
            }
        });

    }

    public static void getPostDetail(SharedPreferences sharedPreferences,
                                     String postId,
                                     PostDetailResponseInterface postDetailResponseInterface) {

        Call<PostDetailMainModel> call = getRetrofitObject().
                getPostDetail(sharedPreferences.getString(MyConstants.AUTHORIZATION, ""),
                        sharedPreferences.getString(MyConstants.USER_ID, ""),
                        sharedPreferences.getString(MyConstants.SESSION, ""),
                        postId);
        call.enqueue(new Callback<PostDetailMainModel>() {
            @Override
            public void onResponse(@NonNull Call<PostDetailMainModel> call, @NonNull Response<PostDetailMainModel> response) {
                PostDetailMainModel postDetailMainModel = response.body();
                if (postDetailMainModel != null) {
                    postDetailResponseInterface.getPostDetailResponse(MyConstants.GO_TO_RESULT, postDetailMainModel);
                } else {
                    CommonMethods.showLog(TAG, "getPostDetail Api Null Response ");
                    postDetailResponseInterface.getPostDetailResponse(MyConstants.NULL_RESPONSE, null);

                }
            }

            @Override
            public void onFailure(@NonNull Call<PostDetailMainModel> call, @NonNull Throwable t) {
                CommonMethods.showLog(Constraints.TAG, "getPostDetail Api Exception  " + t.getMessage());
                postDetailResponseInterface.getPostDetailResponse(MyConstants.FAILURE_RESPONSE, null);
            }
        });

    }

    public static void getCouponList(SharedPreferences sharedPreferences,
                                     String storeList,
                                     CouponListResponseInterface couponListResponseInterface) {

        Call<CouponsListMainModel> call = getRetrofitObject().
                getCouponList(sharedPreferences.getString(MyConstants.AUTHORIZATION, ""),
                        sharedPreferences.getString(MyConstants.USER_ID, ""),
                        sharedPreferences.getString(MyConstants.SESSION, ""),
                        storeList);
        call.enqueue(new Callback<CouponsListMainModel>() {
            @Override
            public void onResponse(@NonNull Call<CouponsListMainModel> call, @NonNull Response<CouponsListMainModel> response) {
                CouponsListMainModel couponsListMainModel = response.body();
                if (couponsListMainModel != null) {
                    couponListResponseInterface.getCouponListResponse(MyConstants.GO_TO_RESULT, couponsListMainModel);
                } else {
                    CommonMethods.showLog(TAG, "getCouponList Api Null Response ");
                    couponListResponseInterface.getCouponListResponse(MyConstants.NULL_RESPONSE, null);

                }
            }

            @Override
            public void onFailure(@NonNull Call<CouponsListMainModel> call, @NonNull Throwable t) {
                CommonMethods.showLog(Constraints.TAG, "getCouponList Api Exception  " + t.getMessage());
                couponListResponseInterface.getCouponListResponse(MyConstants.FAILURE_RESPONSE, null);
            }
        });

    }

    public static void checkCouponWithOrder(SharedPreferences sharedPreferences,
                                            String total_amount,
                                            String order_info,
                                            String coupon_id,
                                            AppliedCouponResponseInterface appliedCouponResponseInterface) {

        Call<CouponAppliedModel> call = getRetrofitObject().
                checkCouponWithOrder(sharedPreferences.getString(MyConstants.AUTHORIZATION, ""),
                        sharedPreferences.getString(MyConstants.USER_ID, ""),
                        sharedPreferences.getString(MyConstants.SESSION, ""),
                        total_amount,
                        order_info,
                        coupon_id);
        call.enqueue(new Callback<CouponAppliedModel>() {
            @Override
            public void onResponse(@NonNull Call<CouponAppliedModel> call, @NonNull Response<CouponAppliedModel> response) {
                CouponAppliedModel couponAppliedModel = response.body();
                if (couponAppliedModel != null) {
                    appliedCouponResponseInterface.getAppliedCouponResponse(MyConstants.GO_TO_RESULT, couponAppliedModel);
                } else {
                    CommonMethods.showLog(TAG, "checkCouponWithOrder Api Null Response ");
                    appliedCouponResponseInterface.getAppliedCouponResponse(MyConstants.NULL_RESPONSE, null);

                }
            }

            @Override
            public void onFailure(@NonNull Call<CouponAppliedModel> call, @NonNull Throwable t) {
                CommonMethods.showLog(Constraints.TAG, "checkCouponWithOrder Api Exception  " + t.getMessage());
                appliedCouponResponseInterface.getAppliedCouponResponse(MyConstants.FAILURE_RESPONSE, null);
            }
        });

    }
}
