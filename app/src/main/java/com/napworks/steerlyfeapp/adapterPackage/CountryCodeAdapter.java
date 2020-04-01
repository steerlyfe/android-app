package com.napworks.steerlyfeapp.adapterPackage;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.napworks.steerlyfeapp.R;
import com.napworks.steerlyfeapp.dialogPackage.CommonMessageDialog;
import com.napworks.steerlyfeapp.modelPackage.CountryModel;
import com.napworks.steerlyfeapp.utilsPackage.MyConstants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CountryCodeAdapter extends RecyclerView.Adapter<CountryCodeAdapter.MyViewHolder> {
    private CommonMessageDialog commonMessageDialog;
    private Activity activity;
    private List<CountryModel> countriesList;
    int currentPosition = 0;

    public CountryCodeAdapter(Activity activity, List<CountryModel> countriesList, CommonMessageDialog commonMessageDialog) {
        this.activity = activity;
        this.commonMessageDialog = commonMessageDialog;
        this.countriesList = countriesList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_country_code, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        CountryModel countryModel = countriesList.get(position);
        holder.countryCode.setText(activity.getString(R.string.plus).concat(countryModel.getCallingCode()));
        holder.countryName.setText(countryModel.getCountryName());
        holder.countryLongCode.setText(countryModel.getLongCode());
    }

    @Override
    public int getItemCount() {
        return countriesList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.countryLongCode)
        TextView countryLongCode;
        @BindView(R.id.countryName)
        TextView countryName;
        @BindView(R.id.countryCode)
        TextView countryCode;
        @BindView(R.id.mainRelative)
        RelativeLayout mainRelative;

        MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            mainRelative.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.mainRelative:
                    currentPosition = getAdapterPosition();
                    if (currentPosition >= 0) {
                        Intent intent = new Intent();
                        intent.putExtra(MyConstants.COUNTRY_CALLING_CODE, countriesList.get(currentPosition).getCallingCode());
                        intent.putExtra(MyConstants.COUNTRY_NAME, countriesList.get(currentPosition).getCountryName());
                        activity.setResult(Activity.RESULT_OK, intent);
                        activity.finish();
                        activity.overridePendingTransition(0, 0);
                    }
                    break;
            }

        }
    }
}
