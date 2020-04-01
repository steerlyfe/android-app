package com.napworks.steerlyfeapp.fragmentsPackage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.napworks.steerlyfeapp.R;
import com.napworks.steerlyfeapp.activitiesPackage.AddressActivity;
import com.napworks.steerlyfeapp.activitiesPackage.MainActivity;
import com.napworks.steerlyfeapp.activitiesPackage.CouponsActivity;
import com.napworks.steerlyfeapp.adapterPackage.CartRecyclerAdapter;
import com.napworks.steerlyfeapp.databasePackage.DatabaseMethods;
import com.napworks.steerlyfeapp.dialogPackage.CommonMessageDialog;
import com.napworks.steerlyfeapp.dialogPackage.ConfirmationDialog;
import com.napworks.steerlyfeapp.dialogPackage.LoadingDialog;
import com.napworks.steerlyfeapp.interfacePackage.AppliedCouponResponseInterface;
import com.napworks.steerlyfeapp.interfacePackage.CheckCouponAmountInterface;
import com.napworks.steerlyfeapp.interfacePackage.ConfirmationDialogResponseInterface;
import com.napworks.steerlyfeapp.interfacePackage.OnCartItemClickedInterface;
import com.napworks.steerlyfeapp.interfacePackage.OnRecyclerItemClickedInterFace;
import com.napworks.steerlyfeapp.interfacePackage.PlaceOrderResponseInterface;
import com.napworks.steerlyfeapp.modelPackage.AddressInnerModel;
import com.napworks.steerlyfeapp.modelPackage.CouponAppliedModel;
import com.napworks.steerlyfeapp.modelPackage.CouponListInnerModel;
import com.napworks.steerlyfeapp.modelPackage.PlaceOrderModel;
import com.napworks.steerlyfeapp.modelPackage.ProductDetailModel;
import com.napworks.steerlyfeapp.utilsPackage.CommonMethods;
import com.napworks.steerlyfeapp.utilsPackage.CommonWebServices;
import com.napworks.steerlyfeapp.utilsPackage.MyConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;

public class CartFragment extends Fragment implements OnRecyclerItemClickedInterFace, View.OnClickListener, ConfirmationDialogResponseInterface, PlaceOrderResponseInterface, OnCartItemClickedInterface, CheckCouponAmountInterface, AppliedCouponResponseInterface {
    private Activity activity;
    private String TAG = getClass().getSimpleName();

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.messageTextView)
    TextView messageTextView;
    @BindView(R.id.cartValue)
    TextView cartValue;
    @BindView(R.id.applyCouponCode)
    TextView applyCouponCode;
    //    @BindView(R.id.totalCost)
//    TextView totalCost;
    @BindView(R.id.checkOutLayout)
    RelativeLayout checkOutLayout;
//    @BindView(R.id.costLinear)
//    LinearLayout costLinear;

    private CommonMessageDialog commonMessageDialog;
    private CartRecyclerAdapter cartRecyclerAdapter;
    LoadingDialog loadingDialog;

    private List<ProductDetailModel> productList;
    DatabaseMethods databaseMethods;

    double costValue = 0.0;
    double discountAmount = 0.0, amountPaid = 0.0, couponDiscount = 0.0;
    String couponUsed = "";

    ConfirmationDialog confirmationDialog;
    SharedPreferences sharedPreferences;
    private String addressId = "";
    boolean isShipping = false;
    private String couponId = "0", couponCount = "no";
    private String couponCode = "";

    public CartFragment() {
    }

    public CartFragment(Activity activity) {
        this.activity = activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        ButterKnife.bind(this, view);
        if (getActivity() != null) {
            activity = getActivity();
            commonMessageDialog = new CommonMessageDialog(getActivity());
            loadingDialog = new LoadingDialog(getActivity());
            sharedPreferences = getActivity().getSharedPreferences(MyConstants.SHARED_PREFERENCE, Context.MODE_PRIVATE);
            confirmationDialog = new ConfirmationDialog(activity, this);
            databaseMethods = new DatabaseMethods(getActivity());
            productList = new ArrayList<>();
            addDataIntoList();
            checkAndShow();

        }
//        promoCode.setOnClickListener(this);
        checkOutLayout.setOnClickListener(this);
        applyCouponCode.setOnClickListener(this);
        return view;

    }

    private void addDataIntoList() {
        if (getActivity() != null) {
            List<ProductDetailModel> list = databaseMethods.getAllProductList();
            cartValue.setText(String.valueOf(list.size()).concat(" ").concat(getActivity().getString(R.string.items)));
            costValue = 0.0;
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getProduct_quantity() > 0) {
                    ProductDetailModel innerModel = list.get(i);
                    CommonMethods.showLog(TAG, "If");

                    if (innerModel.getProductAvailabilityType().equalsIgnoreCase(MyConstants.DELIVER_NOW) ||
                            innerModel.getProductAvailabilityType().equalsIgnoreCase(MyConstants.SHIPPING)) {
                        isShipping = true;
                    }

                    int productQuantity = databaseMethods.getProductQuantityUpdated(innerModel.getProduct_id(),
                            innerModel.getAdditionalFeaturesType(),
                            innerModel.getSub_store_id(), innerModel.getProductAvailabilityType());


                    CommonMethods.showLog(TAG, "Quantity : " + productQuantity);
                    CommonMethods.showLog(TAG, "getProduct_id : " + innerModel.getProduct_id());
                    CommonMethods.showLog(TAG, "getAdditionalFeaturesType : " + innerModel.getAdditionalFeaturesType());
                    CommonMethods.showLog(TAG, "getAdditionalFeaturesPrice : " + innerModel.getAdditionalFeaturesPrice());
                    CommonMethods.showLog(TAG, "getSub_store_id : " + innerModel.getSub_store_id());
                    CommonMethods.showLog(TAG, "getProductAvailabilityType : " + innerModel.getProductAvailabilityType());
                    CommonMethods.showLog(TAG, "getProductAvailabilityPrice : " + innerModel.getProductAvailabilityPrice());

//                    int productQuantity = databaseMethods.getProductQuantity(innerModel.getProduct_id());
                    double price = innerModel.getSale_price() + innerModel.getAdditionalFeaturesPrice();
                    costValue = costValue + (price * productQuantity) + innerModel.getProductAvailabilityPrice();
                    costValue = CommonMethods.roundOff(costValue);
                    productList.add(new ProductDetailModel(innerModel.getProduct_id(), innerModel.getDescription(), innerModel.getCategory_id(),
                            innerModel.getProduct_name(), innerModel.getStore_id(), innerModel.getActual_price(), innerModel.getSale_price(),
                            innerModel.getProduct_quantity(), innerModel.getImage_url(), innerModel.getAdditionalFeaturesType(),
                            innerModel.getAdditionalFeaturesPrice(), innerModel.getProductAvailabilityType(), innerModel.getProductAvailabilityPrice(),
                            innerModel.getSub_store_id(), innerModel.getSeller_name(), innerModel.getSeller_address(), false));
                    CommonMethods.showLog(TAG, "Product List Size : " + productList.size());

                } else {
                    CommonMethods.showLog(TAG, "Else");
                    databaseMethods.deleteParticularProduct(list.get(i).getProduct_id());
                }
            }

            //For add total cost view
            if (productList.size() > 0) {
                productList.add(new ProductDetailModel("", "", "", "", "", costValue, costValue,
                        0, "", "", 0.0, "", 0.0,
                        "", "", "", true));
            }

            CommonMethods.showLog(TAG, "CHECK AND SHOw " + productList.size()


            );
            checkAndShow();
            cartRecyclerAdapter = new CartRecyclerAdapter(activity, productList, commonMessageDialog, databaseMethods,
                    MyConstants.CART, costValue, this, this, this);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(cartRecyclerAdapter);
//            cartRecyclerAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void checkAndShow() {
        if (productList.size() == 0) {
            CommonMethods.showLog(TAG, "If Works");
            messageTextView.setVisibility(View.VISIBLE);
            applyCouponCode.setVisibility(View.GONE);
            checkOutLayout.setVisibility(View.GONE);
            messageTextView.setText(getString(R.string.noItemInCart));

        } else {
            applyCouponCode.setVisibility(View.VISIBLE);
            checkOutLayout.setVisibility(View.VISIBLE);
            messageTextView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRecyclerItemClicked(int position, String callFrom) {
        if (callFrom.equalsIgnoreCase(MyConstants.CANCEL_COUPON)) {
            applyCouponCode.setVisibility(View.VISIBLE);
            couponId = "0";
            couponDiscount = 0.0;
            couponCode = "";
            couponCount = "no";
            costValue = productList.get(productList.size() - 1).getActual_price();
            productList.get(productList.size() - 1).setSale_price(costValue);
            amountPaid = productList.get(productList.size() - 1).getSale_price();
        } else {
            cartRecyclerAdapter.notifyItemRangeRemoved(0, productList.size());
            productList.clear();
            addDataIntoList();
        }
    }

    @Override
    public void onClick(View v) {
        if (getActivity() != null) {
            if (v.getId() == R.id.checkOutLayout) {
                if (isShipping) {
                    String selectedAddress = "";
                    if (selectedAddress.equalsIgnoreCase("")) {
                        Intent intent = new Intent(activity, AddressActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        intent.putExtra(MyConstants.CALLED_FROM, MyConstants.CHECKOUT);
                        startActivityForResult(intent, MyConstants.SELECTED_ADDRESS);
                        activity.overridePendingTransition(0, 0);
                    } else {
                        confirmationDialog.showDialog(getActivity().getString(R.string.yourShippingAddress)
                                        .concat("\n")
                                        .concat(selectedAddress)
                                        .concat("\n")
                                        .concat(getActivity().getString(R.string.pressContinueText)),
                                MyConstants.DEFAULT_ADDRESS, getString(R.string.changeAddress),
                                getString(R.string.continueText));
                    }
                } else {
                    addressId = "";
                    callConfirmationDialog();
                }
            } else if (v.getId() == R.id.applyCouponCode) {


                if (applyCouponCode.getText().equals(activity.getString(R.string.removeCoupon))) {
                    productList.get(productList.size() - 1).setAdditionalFeaturesPrice(0.0);
                    cartRecyclerAdapter.notifyItemChanged(productList.size() - 1);
                    applyCouponCode.setText(activity.getString(R.string.applyCoupon));
                    couponId = "0";
                    couponDiscount = 0.0;
                    couponCode = "";
                    couponCount = "no";
                    costValue = productList.get(productList.size() - 1).getActual_price();
                    productList.get(productList.size() - 1).setSale_price(costValue);
                    amountPaid = productList.get(productList.size() - 1).getSale_price();
                } else {
                    CommonMethods.showLog(TAG, "AMOUNT  : " + productList.get(productList.size() - 1).getSale_price());
                    Intent intent = new Intent(activity, CouponsActivity.class);
                    intent.putExtra(MyConstants.STORE_LIST, prepareStoreListJSON());
                    intent.putExtra(MyConstants.ORDER_INFO, prepareProductIdListJSON());
                    intent.putExtra(MyConstants.TOTAL_AMOUNT, productList.get(productList.size() - 1).getActual_price());
                    startActivityForResult(intent, MyConstants.ADD_PROMO_CODE);
                }

            }
        }
    }

    private void callConfirmationDialog() {
        if (getActivity() != null) {
            if (couponDiscount > 0.0) {
                couponUsed = "yes";
                if (applyCouponCode.getText().toString().trim().isEmpty()) {
                    commonMessageDialog.showDialog(getActivity().getString(R.string.enterCouponName));
                    applyCouponCode.requestFocus();
                }
            } else {
                couponUsed = "no";
            }

//            double totalDiscount = discountAmount + couponDiscount;
//            amountPaid = costValue - totalDiscount;
            prepareOrderInfoJSON();
            CommonMethods.showLog(TAG, "addressId : " + addressId);
            CommonMethods.showLog(TAG, "costValue : " + productList.get(productList.size() - 1).getActual_price());
            CommonMethods.showLog(TAG, "discountAmount : " + discountAmount);
            CommonMethods.showLog(TAG, "couponDiscount : " + couponDiscount);
            CommonMethods.showLog(TAG, "couponUsed : " + couponUsed);
            CommonMethods.showLog(TAG, "amountPaid : " + amountPaid);
            CommonMethods.showLog(TAG, "promoCode : " + applyCouponCode.getText().toString().trim());

            confirmationDialog.showDialog(getActivity().getString(R.string.placeOrderText), MyConstants.PLACE_ORDER, getString(R.string.cancel),
                    getString(R.string.placeOrder));
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (getActivity() != null) {
            if (requestCode == MyConstants.SELECTED_ADDRESS) {
                if (resultCode == RESULT_OK) {
                    CommonMethods.showLog(TAG, "Address Result in Cart Fragment, isShipping : " + isShipping);
                    if (data != null) {

                        AddressInnerModel addressInnerModel = (AddressInnerModel) data.getSerializableExtra(MyConstants.ADDRESS_DATA);
                        addressId = addressInnerModel.getAddress_id();
                        String selectedAddress = sharedPreferences.getString(MyConstants.DEFAULT_ADDRESS, "");
                        if (selectedAddress != null) {
                            confirmationDialog.showDialog(getActivity().getString(R.string.yourShippingAddress)
                                            .concat("\n")
                                            .concat(selectedAddress)
                                            .concat("\n")
                                            .concat(getActivity().getString(R.string.pressContinueText)),
                                    MyConstants.DEFAULT_ADDRESS, getString(R.string.changeAddress),
                                    getString(R.string.continueText));
                        }

//
//                        callConfirmationDialog();


//                        if (couponDiscount > 0.0) {
//                            couponUsed = 1;
//                            if (promoCode.getText().toString().trim().isEmpty()) {
//                                commonMessageDialog.showDialog(getActivity().getString(R.string.enterCouponName));
//                                promoCode.requestFocus();
//                            }
//                        } else {
//                            couponUsed = 0;
//                        }


//                        double totalDiscount = discountAmount + couponDiscount;
//                        amountPaid = costValue - totalDiscount;
//                        prepareOrderInfoJSON();
//                        CommonMethods.showLog(TAG, "shipping Address : " + address);
//                        CommonMethods.showLog(TAG, "Pincode : " + pincode);
//                        CommonMethods.showLog(TAG, "costValue : " + costValue);
//                        CommonMethods.showLog(TAG, "discountAmount : " + discountAmount);
//                        CommonMethods.showLog(TAG, "couponDiscount : " + couponDiscount);
//                        CommonMethods.showLog(TAG, "couponUsed : " + couponUsed);
//                        CommonMethods.showLog(TAG, "amountPaid : " + amountPaid);
//                        CommonMethods.showLog(TAG, "promoCode : " + promoCode.getText().toString().trim());
//
//                        confirmationDialog.showDialog(getString(R.string.placeOrderText), MyConstants.PLACE_ORDER, getString(R.string.cancel),
//                                getString(R.string.placeOrder));

                    } else {
                        CommonMethods.showLog(TAG, "Data is Null");
                    }
                }
            } else if (requestCode == MyConstants.ADD_PROMO_CODE) {
                if (data != null) {
                    CouponAppliedModel couponAppliedModel = (CouponAppliedModel) data.getSerializableExtra(MyConstants.COUPON_DATA);
                    CouponListInnerModel couponListInnerModel = (CouponListInnerModel) data.getSerializableExtra(MyConstants.COUPON_FULL_DATA);
                    CommonMethods.showLog(TAG, "Discounted Price : " + couponAppliedModel.getDiscountAmount());

                    couponId = couponListInnerModel.getCouponId();
                    couponCount = couponAppliedModel.getCouponCountCheck();
                    amountPaid = couponAppliedModel.getPayableAmountAfterDiscount();
                    couponDiscount = couponAppliedModel.getDiscountAmount();
                    couponCode = couponAppliedModel.getCouponCode();

                    applyCouponCode.setText(activity.getString(R.string.removeCoupon));

                    productList.get(productList.size() - 1).setSale_price(couponAppliedModel.getPayableAmountAfterDiscount());
                    productList.get(productList.size() - 1).setProduct_name(couponAppliedModel.getCouponCode());
                    productList.get(productList.size() - 1).setAdditionalFeaturesPrice(couponAppliedModel.getDiscountAmount());
                    cartRecyclerAdapter.notifyItemChanged(productList.size() - 1);
                }
            }
        }
    }

    private String prepareOrderInfoJSON() {
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < productList.size(); i++) {
            ProductDetailModel productDetailModel = productList.get(i);
            if (!productDetailModel.isLoadingType()) {
                CommonMethods.showLog(TAG, "Seller Address : " + productDetailModel.getSeller_address());
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("product_id", productDetailModel.getProduct_id());
                    jsonObject.put("product_name", productDetailModel.getProduct_name());
                    jsonObject.put("product_image", productDetailModel.getImage_url());
                    jsonObject.put("actual_price", productDetailModel.getActual_price());
                    jsonObject.put("sale_price", productDetailModel.getSale_price());
                    jsonObject.put("store_id", productDetailModel.getStore_id());
                    jsonObject.put("store_name", productDetailModel.getSeller_name());
                    jsonObject.put("sub_store_id", productDetailModel.getSub_store_id());
                    jsonObject.put("sub_store_address", productDetailModel.getSeller_address());
                    jsonObject.put("product_availability", productDetailModel.getProductAvailabilityType());
                    jsonObject.put("product_availability_price", productDetailModel.getProductAvailabilityPrice());
                    jsonObject.put("additional_feature", productDetailModel.getAdditionalFeaturesType());
                    jsonObject.put("additional_feature_price", productDetailModel.getAdditionalFeaturesPrice());
                    jsonObject.put("quantity", productDetailModel.getProduct_quantity());
                    jsonArray.put(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        CommonMethods.showLog(TAG, "JSON : " + jsonArray.toString());
        return jsonArray.toString();
    }


    private String prepareStoreListJSON() {
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < productList.size(); i++) {
            ProductDetailModel productDetailModel = productList.get(i);
            if (!productDetailModel.isLoadingType()) {
                jsonArray.put(productDetailModel.getStore_id());
            }
        }
        CommonMethods.showLog(TAG, "JSON : " + jsonArray.toString());
        return jsonArray.toString();
    }

    private String prepareProductIdListJSON() {
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < productList.size(); i++) {
            ProductDetailModel productDetailModel = productList.get(i);
            if (!productDetailModel.isLoadingType()) {
                jsonArray.put(productDetailModel.getProduct_id());
            }
        }
        CommonMethods.showLog(TAG, "JSON : " + jsonArray.toString());
        return jsonArray.toString();
    }

    private String preparePaymentInfoJSON() {
        JSONArray jsonArray = new JSONArray();
        CommonMethods.showLog(TAG, "JSON : " + jsonArray.toString());
        return jsonArray.toString();
    }

    @Override
    public void getConfirmationDialogResponse(String type) {
        if (getActivity() != null) {
            if (type.equalsIgnoreCase(MyConstants.PLACE_ORDER)) {
                costValue = productList.get(productList.size() - 1).getActual_price();
                amountPaid = productList.get(productList.size() - 1).getSale_price();

                CommonMethods.showLog(TAG, "Cost Value : " + costValue);
                CommonMethods.showLog(TAG, "couponId : " + couponId);
                CommonMethods.showLog(TAG, "couponCount : " + couponCount);
                CommonMethods.showLog(TAG, "couponDiscount : " + couponDiscount);
                CommonMethods.showLog(TAG, "couponCode : " + couponCode);
                CommonMethods.showLog(TAG, "couponCode : " + amountPaid);


                loadingDialog.showDialog();
                CommonWebServices.placeOrder(sharedPreferences,
                        costValue,
                        discountAmount,
                        amountPaid,
                        couponUsed,
                        couponCode,
                        couponDiscount,
                        couponId,
                        couponCount,
                        addressId,
                        preparePaymentInfoJSON(),
                        prepareOrderInfoJSON(),
                        this);
            } else if (type.equalsIgnoreCase(MyConstants.DEFAULT_ADDRESS)) {
                callConfirmationDialog();

            } else if (type.equalsIgnoreCase(MyConstants.OPEN_ADDRESS_ACTIVITY)) {
                Intent intent = new Intent(activity, AddressActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra(MyConstants.CALLED_FROM, MyConstants.CHECKOUT);
                startActivityForResult(intent, MyConstants.SELECTED_ADDRESS);
                activity.overridePendingTransition(0, 0);
            }
        }
    }

    @Override
    public void getPlaceOrderResponse(String status, PlaceOrderModel placeOrderModel) {
        loadingDialog.hideDialog();
        switch (status) {
            case MyConstants.GO_TO_RESULT:
                if (placeOrderModel.getStatus() != null) {
                    CommonMethods.showLog(TAG, "placeOrder Api Status : " + placeOrderModel.getStatus());
                    switch (placeOrderModel.getStatus()) {
                        case "1":
                            CommonMethods.showLog(TAG, "place Order Api Success");
                            String message = placeOrderModel.getMessage();
                            Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(activity, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            intent.putExtra(MyConstants.CALLED_FROM, "");
                            activity.startActivity(intent);
                            databaseMethods.deleteAllProducts();
                            activity.finishAffinity();
                            break;

                        case "2":
                            CommonMethods.showLog(TAG, "placeOrder Status 2 : " + placeOrderModel.getMessage());
                            commonMessageDialog.showDialog(activity.getString(R.string.some_errors_occurred_text));
                            break;

                        case "11":
                            commonMessageDialog.showDialog(activity.getString(R.string.some_errors_occurred_text));
                            break;
                        case "10":
                            CommonMethods.sessionOut(activity);
                            break;
                        case "0":
                            commonMessageDialog.showDialog(placeOrderModel.getMessage());
                            break;
                    }
                } else {
                    commonMessageDialog.showDialog(activity.getString(R.string.some_errors_occurred_text));
                }
                break;

            case MyConstants.NULL_RESPONSE:
            case MyConstants.FAILURE_RESPONSE:
                commonMessageDialog.showDialog(activity.getString(R.string.some_errors_occurred_text));
                break;
        }
    }

    @Override
    public void onCartItemClicked(List<ProductDetailModel> productList, int position) {
        if (getActivity() != null) {
            this.productList = productList;
            if (productList.size() == 1) {
                messageTextView.setVisibility(View.VISIBLE);
                applyCouponCode.setVisibility(View.GONE);
                checkOutLayout.setVisibility(View.GONE);
                messageTextView.setText(getString(R.string.noItemInCart));
                cartValue.setText(String.valueOf(productList.size() - 1).concat(" ").concat(getActivity().getString(R.string.items)));
            }
        }
    }

    @Override
    public void getCheckCouponAmountResponse(String callFrom, int position) {
        if (callFrom.equalsIgnoreCase(MyConstants.CHECK_COUPON_AMOUNT)) {
            CommonMethods.showLog(TAG, "couponId : " + couponId);
            CommonMethods.showLog(TAG, "Actual Price  : " + productList.get(productList.size() - 1).getActual_price());
            if (!couponId.equalsIgnoreCase("0"))
            {
                loadingDialog.showDialog();
                CommonWebServices.checkCouponWithOrder(sharedPreferences,
                        String.valueOf(CommonMethods.roundOff(productList.get(productList.size() - 1).getActual_price())),
                        prepareProductIdListJSON(),
                        couponId,
                        this);
            }
            else
            {
                CommonMethods.showLog(TAG,"No Coupon Applied Yet ");
            }
        }
    }

    @Override
    public void getAppliedCouponResponse(String status, CouponAppliedModel couponAppliedModel) {
        loadingDialog.hideDialog();
        switch (status) {
            case MyConstants.GO_TO_RESULT:
                if (couponAppliedModel.getStatus() != null) {
                    CommonMethods.showLog(TAG, "AppliedCoupon Api Status : " + couponAppliedModel.getStatus() + "Message : " + couponAppliedModel.getMessage());
                    switch (couponAppliedModel.getStatus()) {
                        case "1":
                            CommonMethods.showLog(TAG, "AppliedCoupon Api Success");
                            couponCount = couponAppliedModel.getCouponCountCheck();
                            amountPaid = couponAppliedModel.getPayableAmountAfterDiscount();
                            couponDiscount = couponAppliedModel.getDiscountAmount();
                            couponCode = couponAppliedModel.getCouponCode();

                            productList.get(productList.size() - 1).setSale_price(couponAppliedModel.getPayableAmountAfterDiscount());
                            productList.get(productList.size() - 1).setProduct_name(couponAppliedModel.getCouponCode());
                            productList.get(productList.size() - 1).setAdditionalFeaturesPrice(couponAppliedModel.getDiscountAmount());
                            cartRecyclerAdapter.notifyItemChanged(productList.size() - 1);
                            break;

                        case "2":
                            CommonMethods.showLog(TAG, "AppliedCoupon Status 2 : " + couponAppliedModel.getMessage());
                            commonMessageDialog.showDialog(getString(R.string.some_errors_occurred_text));
                            break;

                        case "11":
                            commonMessageDialog.showDialog(getString(R.string.some_errors_occurred_text));
                            break;
                        case "10":
                            CommonMethods.sessionOut(activity);
                            break;
                        case "0":
                            commonMessageDialog.showDialog(couponAppliedModel.getMessage());
                            productList.get(productList.size() - 1).setAdditionalFeaturesPrice(0.0);
                            cartRecyclerAdapter.notifyItemChanged(productList.size() - 1);
                            applyCouponCode.setText(activity.getString(R.string.applyCoupon));
                            couponId = "0";
                            couponDiscount = 0.0;
                            couponCode = "";
                            couponCount = "no";
                            costValue = productList.get(productList.size() - 1).getActual_price();
                            productList.get(productList.size() - 1).setSale_price(costValue);
                            amountPaid = productList.get(productList.size() - 1).getSale_price();
                            break;
                    }
                } else {
                    commonMessageDialog.showDialog(getString(R.string.some_errors_occurred_text));
                }
                break;
            case MyConstants.NULL_RESPONSE:
            case MyConstants.FAILURE_RESPONSE:
                commonMessageDialog.showDialog(getString(R.string.some_errors_occurred_text));
                break;
        }
    }
}
