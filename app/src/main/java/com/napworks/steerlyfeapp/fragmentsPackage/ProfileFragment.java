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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.napworks.steerlyfeapp.R;
import com.napworks.steerlyfeapp.activitiesPackage.AddressActivity;
import com.napworks.steerlyfeapp.activitiesPackage.FavoritesActivity;
import com.napworks.steerlyfeapp.activitiesPackage.OrdersActivity;
import com.napworks.steerlyfeapp.activitiesPackage.SavedPostsActivity;
import com.napworks.steerlyfeapp.activitiesPackage.UpdateDataActivity;
import com.napworks.steerlyfeapp.databasePackage.DatabaseMethods;
import com.napworks.steerlyfeapp.dialogPackage.CommonMessageDialog;
import com.napworks.steerlyfeapp.dialogPackage.ConfirmationDialog;
import com.napworks.steerlyfeapp.dialogPackage.LoadingDialog;
import com.napworks.steerlyfeapp.interfacePackage.ConfirmationDialogResponseInterface;
import com.napworks.steerlyfeapp.utilsPackage.CommonMethods;
import com.napworks.steerlyfeapp.utilsPackage.MyConstants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileFragment extends Fragment implements View.OnClickListener, ConfirmationDialogResponseInterface {
    private String TAG = getClass().getSimpleName();

    public ProfileFragment() {
    }

    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.email)
    TextView email;
    @BindView(R.id.logout)
    TextView logout;
    @BindView(R.id.nameRelative)
    RelativeLayout nameRelative;
    @BindView(R.id.viewAddressLayout)
    RelativeLayout viewAddressLayout;
    @BindView(R.id.viewFavoriteProduct)
    RelativeLayout viewFavoriteProduct;
    @BindView(R.id.viewSavedContent)
    RelativeLayout viewSavedContent;
    @BindView(R.id.viewOrdersLayout)
    RelativeLayout viewOrdersLayout;

    SharedPreferences sharedPreferences;
    LoadingDialog loadingDialog;
    CommonMessageDialog commonMessageDialog;
    DatabaseMethods databaseMethods;
    ConfirmationDialog confirmationDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);
        if (getActivity() != null) {
            sharedPreferences = getActivity().getSharedPreferences(MyConstants.SHARED_PREFERENCE, Context.MODE_PRIVATE);
            commonMessageDialog = new CommonMessageDialog(getActivity());
            loadingDialog = new LoadingDialog(getActivity());
            databaseMethods = new DatabaseMethods(getActivity());
            confirmationDialog = new ConfirmationDialog(getActivity(), this);
            name.setText(sharedPreferences.getString(MyConstants.FULL_NAME, ""));
            email.setText(sharedPreferences.getString(MyConstants.EMAIL, ""));

            nameRelative.setOnClickListener(this);
            logout.setOnClickListener(this);
            viewFavoriteProduct.setOnClickListener(this);
            viewAddressLayout.setOnClickListener(this);
            viewOrdersLayout.setOnClickListener(this);
            viewSavedContent.setOnClickListener(this);
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        if (getActivity() != null) {
            switch (v.getId()) {
                case R.id.nameRelative:
                    Intent intent = new Intent(getActivity(), UpdateDataActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivityForResult(intent, MyConstants.UPDATE_PROFILE_DATA);
                    break;
                case R.id.logout:
                    confirmationDialog.showDialog(getString(R.string.logoutText), MyConstants.LOGOUT, getString(R.string.cancel),
                            getString(R.string.logOut));
                    break;

                case R.id.viewFavoriteProduct:
                    intent = new Intent(getActivity(), FavoritesActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    getActivity().startActivity(intent);
                    break;
                case R.id.viewAddressLayout:
                    intent = new Intent(getActivity(), AddressActivity.class);
                    intent.putExtra(MyConstants.CALLED_FROM, MyConstants.PROFILE);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    getActivity().overridePendingTransition(0, 0);
                    getActivity().startActivity(intent);
                    break;
                case R.id.viewOrdersLayout:
                    intent = new Intent(getActivity(), OrdersActivity.class);
                    intent.putExtra(MyConstants.CALLED_FROM, MyConstants.PROFILE);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    getActivity().overridePendingTransition(0, 0);
                    getActivity().startActivity(intent);
                    break;
                case R.id.viewSavedContent:
                    intent = new Intent(getActivity(), SavedPostsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    getActivity().overridePendingTransition(0, 0);
                    getActivity().startActivity(intent);
                    break;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MyConstants.UPDATE_PROFILE_DATA) {
            if (resultCode == Activity.RESULT_OK) {
                CommonMethods.showLog(TAG, "DATA UPDATED");
                if (data != null) {
                    commonMessageDialog.showDialog(data.getStringExtra(MyConstants.MESSAGE));
                }
                name.setText(sharedPreferences.getString(MyConstants.FULL_NAME, ""));
                email.setText(sharedPreferences.getString(MyConstants.EMAIL, ""));
            }
        }
    }

    @Override
    public void getConfirmationDialogResponse(String type) {
        if (getActivity() != null) {
            if (type.equalsIgnoreCase(MyConstants.LOGOUT)) {
                databaseMethods.deleteAllProducts();
                databaseMethods.deleteAllFavoriteProducts();
                CommonMethods.logOut(getActivity());
            }
        }
    }
}
