package com.napworks.steerlyfeapp.activitiesPackage;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.napworks.steerlyfeapp.R;
import com.napworks.steerlyfeapp.dialogPackage.CommonMessageDialog;
import com.napworks.steerlyfeapp.dialogPackage.LoadingDialog;
import com.napworks.steerlyfeapp.interfacePackage.ProductCategoryResponseInterface;
import com.napworks.steerlyfeapp.interfacePackage.ProductDetailResponseInterface;
import com.napworks.steerlyfeapp.modelPackage.ProductCategoriesMainModel;
import com.napworks.steerlyfeapp.modelPackage.ProductDetailMainModel;
import com.napworks.steerlyfeapp.qRCodeScannerPackage.BarcodeGraphicTracker;
import com.napworks.steerlyfeapp.qRCodeScannerPackage.BarcodeTrackerFactory;
import com.napworks.steerlyfeapp.qRCodeScannerPackage.CameraSource;
import com.napworks.steerlyfeapp.qRCodeScannerPackage.CameraSourcePreview;
import com.napworks.steerlyfeapp.qRCodeScannerPackage.GraphicOverlay;
import com.napworks.steerlyfeapp.utilsPackage.CommonMethods;
import com.napworks.steerlyfeapp.utilsPackage.CommonWebServices;
import com.napworks.steerlyfeapp.utilsPackage.MyConstants;

import java.io.IOException;
import java.io.Serializable;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QrCodeScanner extends AppCompatActivity
        implements BarcodeGraphicTracker.BarcodeUpdateListener,
        CameraSource.AutoFocusCallback, CameraSource.AutoFocusMoveCallback, View.OnClickListener, ProductCategoryResponseInterface {
    String calledFor = "";

    private String TAG = getClass().getSimpleName();


    @BindView(R.id.animatedView)
    View animatedView;
    @BindView(R.id.backArrow)
    ImageView backArrow;


    private static final int RC_HANDLE_GMS = 9001;
    private static final int RC_HANDLE_CAMERA_PERM = 2;
    private CameraSource mCameraSource;
    private ScaleGestureDetector scaleGestureDetector;
    private GestureDetector gestureDetector;
    private boolean checkAvailable = true;
    private boolean autoFocus = true;
    @BindView(R.id.graphicOverlay)
    GraphicOverlay graphicOverlay;
    @BindView(R.id.cameraSourcePreview)
    CameraSourcePreview cameraSourcePreview;
    boolean cameraStatus = false;
    String qrCode = "";

    CommonMessageDialog commonMessageDialog;

    boolean isAnimationCompleted = false;
    boolean isBarcodeDetected = false;
    LoadingDialog loadingDialog;
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code_scanner_updated);
        CommonMethods.showLog(TAG, "QR ACTIVITY CALLS");
        sharedPreferences = getSharedPreferences(MyConstants.SHARED_PREFERENCE, MODE_PRIVATE);
        loadingDialog = new LoadingDialog(this);
        ButterKnife.bind(this);
        commonMessageDialog = new CommonMessageDialog(this);
        gestureDetector = new GestureDetector(this, new QrCodeScanner.CaptureGestureListener());
        scaleGestureDetector = new ScaleGestureDetector(this, new QrCodeScanner.ScaleListener());
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );

        callRequestPermission();
//        imeiNumber.setText("197648078");

        backArrow.setOnClickListener(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        boolean b = scaleGestureDetector.onTouchEvent(e);
        boolean c = gestureDetector.onTouchEvent(e);
        return b || c || super.onTouchEvent(e);
    }

    @SuppressLint("InlinedApi")
    private void createCameraSource() {
        Context context = getApplicationContext();
        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(context).build();
        BarcodeTrackerFactory barcodeFactory = new BarcodeTrackerFactory(graphicOverlay, this);
        barcodeDetector.setProcessor(
                new MultiProcessor.Builder<>(barcodeFactory).build());


        CommonMethods.showLog(TAG, "HEIGHT : " + CommonMethods.getHeight(this));
        CameraSource.Builder builder = new CameraSource.Builder(getApplicationContext(), barcodeDetector)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedPreviewSize(CommonMethods.getWidth(this), CommonMethods.getHeight(this))
                .setRequestedFps(100.0f);

        // make sure that auto focus is an available option
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            builder = builder.setFocusMode(
                    autoFocus ? Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE : null);
//            mCameraSource.autoFocus(this);
//            mCameraSource.setAutoFocusMoveCallback(this);
        }

        mCameraSource = builder
                .setFlashMode(false ? Camera.Parameters.FLASH_MODE_TORCH : null)
                .build();


//        CommonMethods.showLog(TAG, "MyConstants.USE_FLASH " + MyConstants.USE_FLASH);
    }

    /**
     * Restarts the camera.
     */
    @Override
    protected void onResume() {
        super.onResume();
        CommonMethods.showLog(TAG, "ON RESUME");
        startCameraSource();
//        updateCartValue();
    }

    /**
     * Stops the camera.
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (cameraSourcePreview != null) {
            cameraSourcePreview.stop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cameraSourcePreview != null) {
            cameraSourcePreview.release();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case RC_HANDLE_CAMERA_PERM:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                } else {
                    cameraStatus = true;
                    CommonMethods.showLog(TAG, "onRequestPermissionsResult granted");
                    createCameraSource();
                }
                break;
        }
    }

    private void startCameraSource() throws SecurityException {
        // check that the device has play services available.
        int code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(
                getApplicationContext());
        if (code != ConnectionResult.SUCCESS) {
            Dialog dlg =
                    GoogleApiAvailability.getInstance().getErrorDialog(this, code, RC_HANDLE_GMS);
            dlg.show();
        }
        CommonMethods.showLog(TAG, "startCameraSource");
        if (mCameraSource != null) {
            try {
                CommonMethods.showLog(TAG, "start");
                cameraSourcePreview.start(mCameraSource, graphicOverlay);
            } catch (IOException e) {
                CommonMethods.showLog(TAG, "Unable to start camera source." + e);
                mCameraSource.release();
                mCameraSource = null;
            }
        } else {
            CommonMethods.showLog(TAG, "mCameraSource null");
        }
    }

    private boolean onTap(float rawX, float rawY) {
        return false;
    }

    @Override
    public void onAutoFocus(boolean success) {
        CommonMethods.showLog(TAG, "onAutoFocus success " + success);
    }

    @Override
    public void onAutoFocusMoving(boolean start) {
        CommonMethods.showLog(TAG, "onAutoFocusMoving start " + start);
    }


    @Override
    public void getProductCategoryResponse(String status, ProductCategoriesMainModel productDetailMainModel) {
        loadingDialog.hideDialog();
        switch (status) {
            case MyConstants.GO_TO_RESULT:
                if (productDetailMainModel.getStatus() != null) {
                    CommonMethods.showLog(TAG, "Product Detail Api Status : " + productDetailMainModel.getStatus());
                    switch (productDetailMainModel.getStatus()) {
                        case "1":
                            CommonMethods.showLog(TAG, "Product Detail Api Success");
                            if (productDetailMainModel.getProductList() != null && productDetailMainModel.getProductList().size() > 0) {
                                Intent intent = new Intent(this, BarcodeProductDetailActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                intent.putExtra(MyConstants.PRODUCT_LIST, (Serializable) productDetailMainModel.getProductList());
                                startActivity(intent);
                                finish();
                            } else {
                                commonMessageDialog.showDialog(getString(R.string.noProductsAvailableText));
                            }
                            break;

                        case "2":
                            CommonMethods.showLog(TAG, "Product Detail Status 2 : " + productDetailMainModel.getMessage());
                            commonMessageDialog.showDialog(getString(R.string.some_errors_occurred_text));
                            break;

                        case "11":
                            commonMessageDialog.showDialog(getString(R.string.some_errors_occurred_text));
                            break;
                        case "10":
                            CommonMethods.sessionOut(this);
                            break;
                        case "0":
                            commonMessageDialog.showDialog(productDetailMainModel.getMessage());
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


    private class CaptureGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            return onTap(e.getRawX(), e.getRawY()) || super.onSingleTapConfirmed(e);
        }
    }

    private class ScaleListener implements ScaleGestureDetector.OnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            return false;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            mCameraSource.doZoom(detector.getScaleFactor());
        }
    }

    @Override
    public void onBarcodeDetected(final Barcode barcode) {

        CommonMethods.showLog(TAG, "onBarcodeDetected displayValue " + barcode.displayValue);
        CommonMethods.showLog(TAG, "onBarcodeDetected rawValue " + barcode.rawValue);
        if (cameraStatus) {
            qrCode = barcode.rawValue;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (barcode.rawValue != null) {
                        isBarcodeDetected = true;
                        animatedView.setVisibility(View.GONE);
//                        qrNumberText.setText(getString(R.string.qr_code) + " " + qrCode);
                        Toast.makeText(QrCodeScanner.this, getString(R.string.qr_code_detected), Toast.LENGTH_SHORT).show();
                        cameraStatus = false;
                        loadingDialog.showDialog();
                        CommonWebServices.getProductDetailWithBarcode(sharedPreferences, qrCode, QrCodeScanner.this);
                    }
                }
            });
        }

    }

    public void callRequestPermission() {
        CommonMethods.showLog(TAG, "PERMISSION METHOD CALL ");
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}
                    , RC_HANDLE_CAMERA_PERM);
        } else {
//            handleAnimation();
            slideDown();


            CommonMethods.showLog(TAG, "onRequestPermissionsResult granted");
            createCameraSource();
            cameraStatus = true;
        }
    }

    private void slideDown() {
        if (!isAnimationCompleted) {
            Animation aniSlide = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
            animatedView.setAnimation(aniSlide);
            aniSlide.start();
            aniSlide.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    isAnimationCompleted = true;
                    if (!isBarcodeDetected) {
                        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) animatedView.getLayoutParams();
                        lp.gravity = Gravity.BOTTOM;
                        lp.setMargins(20,0,20,0);
                        animatedView.setLayoutParams(lp);
                        slideUp();
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
        }
        CommonMethods.showLog(TAG, "Works ");
    }

    private void slideUp() {

        if (isAnimationCompleted) {
            CommonMethods.showLog(TAG, "Start Up ");
            Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
            animatedView.setAnimation(animation);
            animation.start();
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    isAnimationCompleted = false;
                    if (!isBarcodeDetected) {
                        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) animatedView.getLayoutParams();
                        lp.gravity = Gravity.TOP;
                        lp.setMargins(20,0,20,0);
                        animatedView.setLayoutParams(lp);
                        slideDown();
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.backArrow) {
            onBackPressed();
        }
    }
}
