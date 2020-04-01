package com.napworks.steerlyfeapp.activitiesPackage;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.napworks.steerlyfeapp.R;
import com.napworks.steerlyfeapp.adapterPackage.NearByStoresAdapter;
import com.napworks.steerlyfeapp.dialogPackage.CommonMessageDialog;
import com.napworks.steerlyfeapp.dialogPackage.LoadingDialog;
import com.napworks.steerlyfeapp.interfacePackage.NearByStoresResponseInterface;
import com.napworks.steerlyfeapp.interfacePackage.OnRecyclerItemClickedInterFace;
import com.napworks.steerlyfeapp.modelPackage.NearByStoresMainModel;
import com.napworks.steerlyfeapp.modelPackage.StoreDetailModel;
import com.napworks.steerlyfeapp.utilsPackage.CommonMethods;
import com.napworks.steerlyfeapp.utilsPackage.CommonWebServices;
import com.napworks.steerlyfeapp.utilsPackage.MyConstants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NearByStoresActivity extends ParentActivity implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener, OnRecyclerItemClickedInterFace, NearByStoresResponseInterface {


    private String TAG = getClass().getSimpleName();
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbarTextView)
    TextView toolbarTextView;
    @BindView(R.id.recyclerViewStoresList)
    RecyclerView recyclerViewStoresList;
    NearByStoresAdapter nearByStoresAdapter;
    private FusedLocationProviderClient mFusedLocationClient;
    private GoogleMap mMap;
    int locationRequestCode = 10;
    double currentLatitude = 30.717798, currentLongitude = 76.744853;
    SharedPreferences sharedPreferences;
    BitmapDescriptor myLocationIcon = null;
    BitmapDescriptor selectedChefMarker = null;
    BitmapDescriptor unSelectedChefMarker = null;
    LoadingDialog loadingDialog;
    boolean callLocationService = false;
    private List<StoreDetailModel> storesList;
    CommonMessageDialog commonMessageDialog;
    private String selectedSubStoreId = " ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_by_stores);
        ButterKnife.bind(this);
        storesList = new ArrayList<>();
        commonMessageDialog = new CommonMessageDialog(this);
        myLocationIcon = BitmapDescriptorFactory.fromResource(R.drawable.current_location_icon);
        selectedChefMarker = BitmapDescriptorFactory.fromResource(R.drawable.selected_marker_icon);
        unSelectedChefMarker = BitmapDescriptorFactory.fromResource(R.drawable.unselected_marker_icon);
        loadingDialog = new LoadingDialog(this);
        sharedPreferences = getSharedPreferences(MyConstants.SHARED_PREFERENCE, MODE_PRIVATE);
        CommonMethods.toolbarInitialize(this, toolbar, getString(R.string.nearByStores), toolbarTextView);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        recyclerViewStoresList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        checkPermissionAndCallApis();
    }
    /*
     * Check Permissions And Call apis
     */
    public void checkPermissionAndCallApis() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    locationRequestCode);
        } else {
            if (CommonMethods.checkLocationOnOff(this)) {
                CommonMethods.showLog(TAG, "location enabled");
                getLocation();
            } else {
                CommonMethods.showLog(TAG, "location not enabled");
                locationEnableDialog();
            }
        }
    }

    @Override
    protected void onResume() {
        if (callLocationService) {
            callLocationService = false;
            CommonMethods.showLog(TAG, "onResume");
            if (CommonMethods.checkLocationOnOff(this)) {
                getLocation();

            } else {
                locationEnableDialog();
            }
        }
        super.onResume();
    }

    /*
     * Location Enable Dialog
     */
    public void locationEnableDialog() {

        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle(getString(R.string.location_service));
        adb.setMessage(R.string.gps_network_not_enabled);
        adb.setIcon(R.mipmap.ic_launcher);
        adb.setPositiveButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                callLocationService = true;
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        });
        adb.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        adb.show();
    }


    /*
     * For Location
     */
    public void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLatitude = location.getLatitude();
                    currentLongitude = location.getLongitude();

                    CommonMethods.showLog(TAG, "getLocation  not null");
                    CommonMethods.showLog(TAG, "getLocation lat " + currentLatitude);
                    CommonMethods.showLog(TAG, "getLocation lng " + currentLongitude);


                    loadingDialog.showDialog();
                    CommonWebServices.getNearbyStores(sharedPreferences,
                            currentLatitude,
                            currentLongitude,
                            NearByStoresActivity.this);
                    mMap.addMarker(new MarkerOptions().position(new LatLng(
                            currentLatitude, currentLongitude)).icon(myLocationIcon).title(getString(R.string.my_location)));
                    LatLng currentLatLng = new LatLng(currentLatitude, currentLongitude);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 14));
                } else {
                    CommonMethods.showLog(TAG, "getLocation null");
                    loadingDialog.showDialog();
                    CommonWebServices.getNearbyStores(sharedPreferences,
                            currentLatitude,
                            currentLongitude,
                            NearByStoresActivity.this);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == locationRequestCode) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (CommonMethods.checkLocationOnOff(this)) {
                    getLocation();
                } else {
                    locationEnableDialog();
                }
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setCompassEnabled(false);
        mMap.setOnMarkerClickListener(this);
    }

    /*
     * Set Marker On Map
     */
    public void setMarkerOnMap() {
        if (mMap != null) {
            mMap.clear();
            for (int i = 0; i < storesList.size(); i++) {
                StoreDetailModel innerData = storesList.get(i);
                double lat = innerData.getLat();
                double lng = innerData.getLng();
                if (innerData.isSelected()) {
                    mMap.addMarker(new MarkerOptions().position(new LatLng(
                            lat, lng)).icon(selectedChefMarker).title(i + ""));
                    LatLng currentLatLng = new LatLng(lat, lng);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 16));
                } else {
                    mMap.addMarker(new MarkerOptions().position(new LatLng(
                            lat, lng)).icon(unSelectedChefMarker).title(i + ""));
                }

            }

        }

    }
    /*
     * Handle Marker Click
     */
    @Override
    public boolean onMarkerClick(Marker marker) {
        CommonMethods.showLog(TAG, "Click : " + marker.getTag());
        CommonMethods.showLog(TAG, "Click : " + marker.getTitle());
        if (!TextUtils.isEmpty(marker.getTitle())) {
            try {
                if (!marker.getTitle().equalsIgnoreCase(getString(R.string.my_location))) {
                    int position = Integer.parseInt(marker.getTitle());
                    refreshMarker(position);
                }
            } catch (Exception e) {
                CommonMethods.showLog(TAG, "Marker Click Exception : " + e.getMessage());
            }
        }
        return true;
    }

    @Override
    public void onRecyclerItemClicked(int position, String callFrom) {
        refreshMarker(position);
    }

    /*
     * Refresh Marker
     */
    public void refreshMarker(int position) {
        for (int i = 0; i < storesList.size(); i++) {
            StoreDetailModel innerData = storesList.get(i);
            if (innerData.isSelected()) {
                innerData.setSelected(false);
                nearByStoresAdapter.notifyItemChanged(i);
                break;
            }
        }
        storesList.get(position).setSelected(true);
        mMap.clear();
        setMarkerOnMap();
        nearByStoresAdapter.notifyItemChanged(position);
        recyclerViewStoresList.smoothScrollToPosition(position);
    }

    /*
     * NearBy Store Api Response
     */
    @Override
    public void getNearByStoresResponse(String status, NearByStoresMainModel nearByStoresMainModel) {
        loadingDialog.hideDialog();
        switch (status) {
            case MyConstants.GO_TO_RESULT:
                if (nearByStoresMainModel.getStatus() != null) {
                    CommonMethods.showLog(TAG, "NearBy Stores Api Status : " + nearByStoresMainModel.getStatus());
                    switch (nearByStoresMainModel.getStatus()) {
                        case "1":
                            CommonMethods.showLog(TAG, "NearBy Stores Api Success");
                            storesList.addAll(nearByStoresMainModel.getNearbyStores());
                            checkAndShow();
//                            int position = 0;
//                            for (int i = 0; i < storesList.size(); i++) {
//                                if (storesList.get(i).getSubStoreId().equalsIgnoreCase(selectedSubStoreId)) {
//                                    storesList.get(i).setSelected(true);
//                                    position = i;
//                                    break;
//                                }
//                            }
                            CommonMethods.showLog(TAG, "SIZE : " + storesList.size());
                            int width = CommonMethods.getWidth(this);
                            width = (int) (width / 1.4);
                            nearByStoresAdapter = new NearByStoresAdapter(this, storesList, width, this);
                            recyclerViewStoresList.setAdapter(nearByStoresAdapter);
//                            final int finalPosition = position;
//                            new Handler().postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    runOnUiThread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            recyclerViewStoresList.smoothScrollToPosition(finalPosition);
//                                        }
//                                    });
//                                }
//                            }, 100);
                            setMarkerOnMap();


                            break;

                        case "2":
                            CommonMethods.showLog(TAG, "NearBy Stores Status 2 : " + nearByStoresMainModel.getMessage());
                            commonMessageDialog.showDialog(getString(R.string.some_errors_occurred_text));
                            break;

                        case "11":
                            commonMessageDialog.showDialog(getString(R.string.some_errors_occurred_text));
                            break;
                        case "10":
                            CommonMethods.sessionOut(this);
                            break;
                        case "0":
                            commonMessageDialog.showDialog(nearByStoresMainModel.getMessage());
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

    private void checkAndShow() {
        if (storesList.size()==0)
        {
            commonMessageDialog.showDialog(getString(R.string.storeInfoNotAvailable));
        }
    }
}
