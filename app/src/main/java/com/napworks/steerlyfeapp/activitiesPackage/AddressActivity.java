package com.napworks.steerlyfeapp.activitiesPackage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.napworks.steerlyfeapp.R;
import com.napworks.steerlyfeapp.adapterPackage.AddressAdapter;
import com.napworks.steerlyfeapp.dialogPackage.CommonMessageDialog;
import com.napworks.steerlyfeapp.dialogPackage.LoadingDialog;
import com.napworks.steerlyfeapp.interfacePackage.AddAddressDialogInterFace;
import com.napworks.steerlyfeapp.interfacePackage.AddAddressResponseInterface;
import com.napworks.steerlyfeapp.interfacePackage.AddressAdapterResponseInterface;
import com.napworks.steerlyfeapp.interfacePackage.AddressResponseInterface;
import com.napworks.steerlyfeapp.modelPackage.AddressInnerModel;
import com.napworks.steerlyfeapp.modelPackage.AddressMainModel;
import com.napworks.steerlyfeapp.modelPackage.CommonStringModel;
import com.napworks.steerlyfeapp.utilsPackage.CommonMethods;
import com.napworks.steerlyfeapp.utilsPackage.CommonWebServices;
import com.napworks.steerlyfeapp.utilsPackage.MyConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AddressActivity extends ParentActivity implements AddressResponseInterface, AddAddressDialogInterFace, AddAddressResponseInterface {

    private String TAG = getClass().getSimpleName();
    private SharedPreferences sharedPreferences;
    private LoadingDialog loadingDialog;
    private CommonMessageDialog commonMessageDialog;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbarTextView)
    TextView toolbarText;
    @BindView(R.id.messageTextView)
    TextView messageTextView;
    @BindView(R.id.mainFrame)
    FrameLayout mainFrame;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
//    @BindView(R.id.swipeContainer)
//    SwipeRefreshLayout swipeRefresh;

    LinearLayoutManager mLayoutManager;
    List<AddressInnerModel> addressList;
    AddressAdapter addressAdapter;
    private String calledFrom = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        ButterKnife.bind(this);
        sharedPreferences = getSharedPreferences(MyConstants.SHARED_PREFERENCE, MODE_PRIVATE);
        loadingDialog = new LoadingDialog(this);
        commonMessageDialog = new CommonMessageDialog(this);
        CommonMethods.toolbarInitialize(this, toolbar, getString(R.string.manageAddress), toolbarText);
        addressList = new ArrayList<>();
        mainFrame.setVisibility(View.GONE);

        if (getIntent() != null) {
            calledFrom = getIntent().getStringExtra(MyConstants.CALLED_FROM);
        }

        addressAdapter = new AddressAdapter(this, addressList, calledFrom, sharedPreferences, loadingDialog,
                commonMessageDialog);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(addressAdapter);

//        swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
//        swipeRefresh.setRefreshing(true);
//        swipeRefresh.setOnRefreshListener(this);
//        swipeRefresh.setRefreshing(true);

        hitApi();

//        onRefresh();


    }

    /*
     * Get All Address Api
     */
    private void hitApi() {
        loadingDialog.showDialog();
        CommonWebServices.getAddress(sharedPreferences, this);
    }

//    @Override
//    public void onRefresh() {
//        swipeRefresh.setRefreshing(false);
//        loadingDialog.showDialog();
//        CommonWebServices.getAddress(sharedPreferences, this);
//    }


    /*
     * Get All Address Api Response
     */
    @Override
    public void getAddressResponse(String status, AddressMainModel addressMainModel) {
//        swipeRefresh.setRefreshing(false);
        loadingDialog.hideDialog();
        mainFrame.setVisibility(View.VISIBLE);
        switch (status) {
            case MyConstants.GO_TO_RESULT:
                if (addressMainModel.getStatus() != null) {
                    CommonMethods.showLog(TAG, "getAddressResponse Api Status : " + addressMainModel.getStatus());
                    switch (addressMainModel.getStatus()) {
                        case "1":
                            CommonMethods.showLog(TAG, "getAddressResponse Api Success");
                            addressList.addAll(addressMainModel.getAddressList());
                            addressAdapter.notifyDataSetChanged();
                            if (addressList.size() > 0) {
                                messageTextView.setVisibility(View.GONE);
                            } else {
                                messageTextView.setVisibility(View.VISIBLE);
                                messageTextView.setText(getString(R.string.noAddressAvailable));
                            }
                            break;

                        case "2":
                            CommonMethods.showLog(TAG, "getAddressResponse Status 2 : " + addressMainModel.getMessage());
                            commonMessageDialog.showDialog(getString(R.string.some_errors_occurred_text));
                            break;

                        case "11":
                            commonMessageDialog.showDialog(getString(R.string.some_errors_occurred_text));
                            break;
                        case "10":
                            CommonMethods.sessionOut(this);
                            break;
                        case "0":
                            commonMessageDialog.showDialog(addressMainModel.getMessage());
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.add) {
            Intent intent = new Intent(this, AddAddressActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivityForResult(intent,MyConstants.ADD_ADDRESS);
//            addAddressDialog.showDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==MyConstants.ADD_ADDRESS)
        {
            if (resultCode==RESULT_OK)
            {
                addressAdapter.notifyItemRangeRemoved(0, addressList.size());
                addressList.clear();
                hitApi();
            }
        }
    }

    @Override
    public void onAddAddressDialogResponse(String address, String pincode) {
//        loadingDialog.showDialog();
//        CommonWebServices.addAddress(sharedPreferences,
//                address,
//                pincode,
//                this);
    }

    @Override
    public void getAddAddressResponse(String status, CommonStringModel commonStringModel) {
        switch (status) {
            case MyConstants.GO_TO_RESULT:
                if (commonStringModel.getStatus() != null) {
                    CommonMethods.showLog(TAG, "Add Address Api Status : " + commonStringModel.getStatus());
                    switch (commonStringModel.getStatus()) {
                        case "1":
                            CommonMethods.showLog(TAG, "Add Address Api Success");
                            addressAdapter.notifyItemRangeRemoved(0, addressList.size());
                            addressList.clear();
                            hitApi();
//                            onRefresh();
                            break;
                        case "2":
                            loadingDialog.hideDialog();
                            CommonMethods.showLog(TAG, "Add Address Status 2 : " + commonStringModel.getMessage());
                            commonMessageDialog.showDialog(getString(R.string.some_errors_occurred_text));
                            break;

                        case "11":
                            loadingDialog.hideDialog();
                            commonMessageDialog.showDialog(getString(R.string.some_errors_occurred_text));
                            break;
                        case "10":
                            loadingDialog.hideDialog();
                            CommonMethods.sessionOut(this);
                            break;
                        case "0":
                            loadingDialog.hideDialog();
                            commonMessageDialog.showDialog(commonStringModel.getMessage());
                            break;
                        default:
                            loadingDialog.hideDialog();
                            commonMessageDialog.showDialog(commonStringModel.getMessage());
                    }
                } else {
                    loadingDialog.hideDialog();
                    commonMessageDialog.showDialog(getString(R.string.some_errors_occurred_text));
                }
                break;
            case MyConstants.NULL_RESPONSE:
            case MyConstants.FAILURE_RESPONSE:
                loadingDialog.hideDialog();
                commonMessageDialog.showDialog(getString(R.string.some_errors_occurred_text));
                break;
        }

    }
}
