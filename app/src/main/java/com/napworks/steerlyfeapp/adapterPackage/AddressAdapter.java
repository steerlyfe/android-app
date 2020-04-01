package com.napworks.steerlyfeapp.adapterPackage;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.napworks.steerlyfeapp.R;
import com.napworks.steerlyfeapp.dialogPackage.CommonMessageDialog;
import com.napworks.steerlyfeapp.dialogPackage.LoadingDialog;
import com.napworks.steerlyfeapp.interfacePackage.CommonStringResponseInterface;
import com.napworks.steerlyfeapp.modelPackage.AddressInnerModel;
import com.napworks.steerlyfeapp.modelPackage.CommonStringModel;
import com.napworks.steerlyfeapp.utilsPackage.CommonMethods;
import com.napworks.steerlyfeapp.utilsPackage.CommonWebServices;
import com.napworks.steerlyfeapp.utilsPackage.MyConstants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.MyViewHolder> {
    private final Activity activity;
    private final List<AddressInnerModel> addressList;
    private final LoadingDialog loadingDialog;
    private final SharedPreferences sharedPreferences;
    private final CommonMessageDialog commonMessageDialog;
    private final String calledFrom;

    private String TAG = getClass().getSimpleName();
    private int currentPosition = 0;
    private int previousSelectedPosition = 0;

    public AddressAdapter(Activity activity, List<AddressInnerModel> addressList,
                          String calledFrom, SharedPreferences sharedPreferences, LoadingDialog loadingDialog,
                          CommonMessageDialog commonMessageDialog) {
        this.activity = activity;
        this.addressList = addressList;
        this.sharedPreferences = sharedPreferences;
        this.loadingDialog = loadingDialog;
        this.calledFrom = calledFrom;
        this.commonMessageDialog = commonMessageDialog;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.address_adapter_layout, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        AddressInnerModel addressInnerModel = addressList.get(position);

        holder.addressTextView.setText(CommonMethods.getFullAddress(activity, addressInnerModel));
        if (addressInnerModel.isDefault()) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(MyConstants.DEFAULT_ADDRESS, CommonMethods.getFullAddress(activity, addressInnerModel));
            editor.apply();
        }

        if (calledFrom.equalsIgnoreCase(MyConstants.PROFILE)) {
            if (addressInnerModel.isDefault()) {
                previousSelectedPosition = position;
                CommonMethods.showLog(TAG, "previous : " + previousSelectedPosition);
                holder.makeDefault.setVisibility(View.GONE);
            } else {
                holder.makeDefault.setVisibility(View.VISIBLE);
            }
        } else {
            CommonMethods.showLog(TAG, "Call from Checkout ");
        }

    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, CommonStringResponseInterface {

        @BindView(R.id.addressTextView)
        TextView addressTextView;
        @BindView(R.id.cardView)
        CardView cardView;
        @BindView(R.id.makeDefault)
        TextView makeDefault;
        @BindView(R.id.delete)
        TextView delete;

        MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            makeDefault.setOnClickListener(this);
            delete.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.makeDefault:
                    CommonMethods.showLog(TAG, "Address Position : " + getAdapterPosition());
                    currentPosition = getAdapterPosition();
                    loadingDialog.showDialog();
                    CommonWebServices.setDefaultAddress(sharedPreferences,
                            String.valueOf(addressList.get(currentPosition).getAddress_id()),
                            this);
                    break;
                case R.id.delete:
                    CommonMethods.showLog(TAG, "Cross  Position : " + getAdapterPosition());
                    currentPosition = getAdapterPosition();
                    loadingDialog.showDialog();
                    CommonWebServices.deleteAddress(sharedPreferences,
                            String.valueOf(addressList.get(currentPosition).getAddress_id()),
                            this);
                    break;
            }
        }

        @Override
        public void getCommonStringResponse(String status, CommonStringModel commonStringModel) {
            loadingDialog.hideDialog();
            switch (status) {
                case MyConstants.GO_TO_RESULT:
                    if (commonStringModel.getStatus() != null) {
                        CommonMethods.showLog(TAG, "setDefaultAddress Api Status : " + commonStringModel.getStatus());
                        switch (commonStringModel.getStatus()) {
                            case "1":
                                CommonMethods.showLog(TAG, "setDefaultAddress Api Success");
                                CommonMethods.showLog(TAG, "Current  Position : " + currentPosition);
                                String message = commonStringModel.getMessage();
                                CommonMethods.showLog(TAG, "Default Api Success : " + message);
                                addressList.get(currentPosition).setDefault(true);
                                notifyItemChanged(currentPosition);
                                addressList.get(previousSelectedPosition).setDefault(false);
                                notifyItemChanged(previousSelectedPosition);

                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(MyConstants.DEFAULT_ADDRESS, CommonMethods.getFullAddress(activity, addressList.get(currentPosition)));
                                editor.apply();


                                if (calledFrom.equalsIgnoreCase(MyConstants.CHECKOUT)) {
                                    Intent intent = new Intent();
                                    intent.putExtra(MyConstants.ADDRESS_DATA, addressList.get(currentPosition));
                                    activity.setResult(Activity.RESULT_OK, intent);
                                    activity.finish();
                                }
                                break;

                            case "2":
                                CommonMethods.showLog(TAG, "setDefaultAddress Status 2 : " + commonStringModel.getMessage());
                                commonMessageDialog.showDialog(activity.getString(R.string.some_errors_occurred_text));
                                break;

                            case "11":
                                commonMessageDialog.showDialog(activity.getString(R.string.some_errors_occurred_text));
                                break;
                            case "10":
                                CommonMethods.sessionOut(activity);
                                break;
                            case "0":
                                commonMessageDialog.showDialog(commonStringModel.getMessage());
                                break;
                        }
                    } else {
                        commonMessageDialog.showDialog(activity.getString(R.string.some_errors_occurred_text));
                    }
                    break;

                case MyConstants.GO_TO_RESULT_DELETE_ADDRESS:
                    if (commonStringModel.getStatus() != null) {
                        CommonMethods.showLog(TAG, "Delete Address Api Status : " + commonStringModel.getStatus());
                        switch (commonStringModel.getStatus()) {
                            case "1":
                                CommonMethods.showLog(TAG, "Delete Address Api Success");
                                CommonMethods.showLog(TAG, "Current  Position : " + currentPosition);
                                String message = commonStringModel.getMessage();
                                addressList.remove(currentPosition);
                                notifyItemRemoved(currentPosition);
                                String selectedAddress = "";

                                for (int i = 0; i < addressList.size(); i++) {
                                    if (addressList.get(i).isDefault()) {
                                        selectedAddress = CommonMethods.getFullAddress(activity, addressList.get(currentPosition));
                                    } else {
                                        selectedAddress = "";
                                    }
                                }

                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(MyConstants.DEFAULT_ADDRESS, selectedAddress);
                                editor.apply();

                                break;

                            case "2":
                                CommonMethods.showLog(TAG, "Delete Address Status 2 : " + commonStringModel.getMessage());
                                commonMessageDialog.showDialog(activity.getString(R.string.some_errors_occurred_text));
                                break;

                            case "11":
                                commonMessageDialog.showDialog(activity.getString(R.string.some_errors_occurred_text));
                                break;
                            case "10":
                                CommonMethods.sessionOut(activity);
                                break;
                            case "0":
                                commonMessageDialog.showDialog(commonStringModel.getMessage());
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
    }

}
