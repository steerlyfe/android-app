package com.napworks.steerlyfeapp.activitiesPackage;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.napworks.steerlyfeapp.R;
import com.napworks.steerlyfeapp.adapterPackage.CountryCodeAdapter;
import com.napworks.steerlyfeapp.databasePackage.DatabaseMethods;
import com.napworks.steerlyfeapp.dialogPackage.CommonMessageDialog;
import com.napworks.steerlyfeapp.modelPackage.CountryModel;
import com.napworks.steerlyfeapp.utilsPackage.CommonMethods;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CountryCodeActivity extends ParentActivity implements View.OnClickListener, TextWatcher {

    private String TAG = getClass().getSimpleName();

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbarTextView)
    TextView toolbarTextView;
    @BindView(R.id.cancelSearchButton)
    ImageView cancelSearchButton;
    @BindView(R.id.edtSearch)
    EditText edtSearch;
    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;
    @BindView(R.id.messageTextView)
    TextView messageTextView;
    @BindView(R.id.searchLayout)
    RelativeLayout searchLayout;
    DatabaseMethods databaseMethods;

    private CommonMessageDialog commonMessageDialog;
    private CountryCodeAdapter countryCodeAdapter;
    private List<CountryModel> countriesList;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_code);
        ButterKnife.bind(this);
        countriesList = new ArrayList<>();
        commonMessageDialog = new CommonMessageDialog(this);
        CommonMethods.toolbarInitialize(this, toolbar, getString(R.string.chooseCallingCode), toolbarTextView);
        databaseMethods = new DatabaseMethods(this);
        countriesList.addAll(databaseMethods.getAllCountriesList());

        countryCodeAdapter = new CountryCodeAdapter(this, countriesList, commonMessageDialog);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(countryCodeAdapter);

        cancelSearchButton.setOnClickListener(this);
        edtSearch.addTextChangedListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancelSearchButton:
                edtSearch.setText("");
                cancelSearchButton.setVisibility(View.GONE);
                countryCodeAdapter.notifyItemRangeRemoved(0, countriesList.size());
                countriesList.clear();
                countriesList.addAll(databaseMethods.getAllCountriesList());
                countryCodeAdapter.notifyItemRangeInserted(0, countriesList.size());

                break;
        }
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        CommonMethods.showLog(TAG, "Chl rha 2");

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        CommonMethods.showLog(TAG, "Chl rha 1");

    }

    @Override
    public void afterTextChanged(Editable s) {
        CommonMethods.showLog(TAG, "Chl rha" + edtSearch.getText().toString().length());
        if (edtSearch.getText().toString().length() > 1) {
            CommonMethods.showLog(TAG, "abc" + edtSearch.getText().toString().length());
            cancelSearchButton.setVisibility(View.VISIBLE);
            countryCodeAdapter.notifyItemRangeRemoved(0, countriesList.size());
            countriesList.clear();
            countriesList.addAll(databaseMethods.getSearchCountriesList(edtSearch.getText().toString().trim()));
            countryCodeAdapter.notifyItemRangeInserted(0, countriesList.size());
            CommonMethods.showLog(TAG, "Country Size  " + countriesList.size());
        } else {
            cancelSearchButton.setVisibility(View.GONE);
        }

    }
}
