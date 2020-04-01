package com.napworks.steerlyfeapp.utilsPackage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.location.LocationManager;
import android.os.Build;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.format.DateFormat;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.napworks.steerlyfeapp.R;
import com.napworks.steerlyfeapp.activitiesPackage.SplashActivity;
import com.napworks.steerlyfeapp.modelPackage.AddressInnerModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class CommonMethods {
    private static String TAG = "CommonMethods";

    public static void showLog(String tag, String s) {
//        Log.e(tag + " ", s + " ");
    }

    public static void toolbarInitialize(AppCompatActivity activity, Toolbar toolbar, String toolbarText, TextView toolbarTextView) {

        activity.setSupportActionBar(toolbar);
        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            activity.getSupportActionBar().setDisplayShowHomeEnabled(true);
            activity.getSupportActionBar().setDisplayShowTitleEnabled(false);

            activity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_icon);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setTitle("");
        }
        toolbarTextView.setText(toolbarText);
    }

    public static void sessionOut(Activity activity) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
        Toast.makeText(activity, activity.getResources().getString(R.string.session_out_please_login_again), Toast.LENGTH_SHORT).show();
        SharedPreferences sharedPreferences = activity.getSharedPreferences(MyConstants.SHARED_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        activity.finishAffinity();
        activity.startActivity(new Intent(activity, SplashActivity.class));


    }

    public static void logOut(Activity activity) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
        SharedPreferences sharedPreferences = activity.getSharedPreferences(MyConstants.SHARED_PREFERENCE, Context.MODE_PRIVATE);
        String address = sharedPreferences.getString(MyConstants.DEFAULT_ADDRESS, "");
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.putString(MyConstants.DEFAULT_ADDRESS, address);
        editor.apply();
        activity.finishAffinity();
        activity.startActivity(new Intent(activity, SplashActivity.class));


    }

    public static void setPrivacyTermsStyle(final Context context, String completeText, TextView textView) {
        String privacyText = context.getString(R.string.privacy_policy_text);
        String termsText = context.getString(R.string.terms_and_conditions_text);
        int privacyStart = completeText.indexOf(privacyText);
        int privacyEnd = privacyStart + privacyText.length();
        int termsStart = completeText.indexOf(termsText);
        int termsEnd = termsStart + termsText.length();
        SpannableString spannableString = new SpannableString(completeText);
        spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.colorPrimaryDark)),
                privacyStart, privacyEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
//                goToPrivacyTermsActivity(context, MyConstants.PRIVACY_POLICY, false);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        }, privacyStart, privacyEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.colorPrimaryDark)),
                termsStart, termsEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
//                goToPrivacyTermsActivity(context, MyConstants.TERMS_CONDITION, false);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        }, termsStart, termsEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(spannableString);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public static int getWidth(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public static int getHeight(Activity context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    public static float getScreenDensity(Activity activity) {
        DisplayMetrics metrics = activity.getResources().getDisplayMetrics();
        return metrics.density;
    }

    public static boolean checkLocationOnOff(final Context context) {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            if (lm != null) {
                gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            }

        } catch (Exception ex) {
            CommonMethods.showLog(TAG, "Exception   " + ex.getMessage());
        }

        try {
            if (lm != null) {
                network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            }
        } catch (Exception ex) {
            CommonMethods.showLog(TAG, "location_service_on   " + gps_enabled);
        }

        CommonMethods.showLog(TAG, "location_service_on   " + gps_enabled);
        return gps_enabled;
    }


    public static String getDate(long timeStampInMilliSeconds) {
        final String dateFormatString = "dd.MM.yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormatString, Locale.getDefault());
        return simpleDateFormat.format(new Date(timeStampInMilliSeconds * 1000));
    }

    public static String timeFormat(String timeString) {
        String finalTime = "";
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
        SimpleDateFormat format1 = new SimpleDateFormat("hh:mm aa");
        try {
            Date date = sdf.parse(timeString);
            finalTime = format1.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        CommonMethods.showLog(TAG, "Time : " + finalTime);
        return finalTime;
    }

    public static void textGradient(TextView textView, Context context) {

        showLog(TAG, " textview " + textView.getText() + " length " + textView.length());
        double dividedLength = textView.length() / 2.0;

        LinearGradient linearGradient = new LinearGradient(0, 0,
                (float) (textView.getTextSize() * dividedLength), 0, context.getResources().getColor(R.color.gradient_start),
                context.getResources().getColor(R.color.gradient_end),
                Shader.TileMode.CLAMP);
        textView.getPaint().setShader(linearGradient);

    }

    public static String getHourOfDay(long timeStampInMilliSeconds) {
        final String hourString = "HH";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(hourString, Locale.getDefault());
        return simpleDateFormat.format(new Date(timeStampInMilliSeconds));
    }

    public static String getOrderDate(long timeStampInMilliSeconds) {
        final String dateFormatString = "dd MMM yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormatString, Locale.getDefault());
        return simpleDateFormat.format(new Date(timeStampInMilliSeconds * 1000));
    }

    public static String getOrderStatusDate(long timeStampInMilliSeconds) {
        final String dateFormatString = "dd MMM";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormatString, Locale.getDefault());
        return simpleDateFormat.format(new Date(timeStampInMilliSeconds * 1000));
    }

    public static String getOrderHistoryDate(long timeStampInMilliSeconds) {
        final String dateFormatString = "dd.MM.yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormatString, Locale.getDefault());
        return simpleDateFormat.format(new Date(timeStampInMilliSeconds * 1000));
    }


    public static double roundOff(double costValue) {
        return Double.parseDouble(String.format("%.2f", costValue));
    }

    public static String getFullAddress(Activity activity, AddressInnerModel addressInnerModel) {
        String address = "";

        address = addressInnerModel.getName().
                concat("\n").
                concat(activity.getString(R.string.mob)).
                concat(".").
                concat(addressInnerModel.getCalling_code()).
                concat(addressInnerModel.getPhone_number()).
                concat("\n").
                concat(addressInnerModel.getAddress()).
                concat("\n").
                concat(addressInnerModel.getLocality()).
                concat("\n").
                concat(addressInnerModel.getCity()).
                concat(" - ").
                concat(addressInnerModel.getPincode()).
                concat("\n").
                concat(addressInnerModel.getState()).
                concat(", ").
                concat(addressInnerModel.getCountry());

        showLog(TAG, "Address : " + address);
        return address;
    }

    public static String formatTimestamp(String timestamp) {

        Calendar currentTimeCalendar = Calendar.getInstance();

        Long time = Long.parseLong(timestamp);
        time = time * 1000;
        Calendar calendar = GregorianCalendar.getInstance();

        calendar.setTimeInMillis(time);

        if ((int) getDifferenceDays(calendar, currentTimeCalendar) > 0) {
            long daysCount = getDifferenceDays(calendar, currentTimeCalendar);
            if (daysCount > 7) {

                Calendar cal = Calendar.getInstance(Locale.ENGLISH);
                cal.setTimeInMillis(Long.parseLong(timestamp) * 1000);
                String date = DateFormat.format("dd MMM yyyy", cal).toString();
                return date;
            } else {
                return (int) getDifferenceDays(calendar, currentTimeCalendar) + " days ago";
            }


        } else if ((int) getDifferenceHours(calendar, currentTimeCalendar) > 0) {
            return (int) getDifferenceHours(calendar, currentTimeCalendar) + " hours ago";

        } else if ((int) getDifferenceMinutes(calendar, currentTimeCalendar) > 0) {
            return (int) getDifferenceMinutes(calendar, currentTimeCalendar) + " minutes ago";
        } else {
            return " Just now";
        }
    }

    public static long getDifferenceDays(Calendar d1, Calendar d2) {
        long diff = d2.getTime().getTime() - d1.getTime().getTime();
        long daysCount = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
//        CommonMethods.showLog(TAG, "dayCount  " + daysCount);

        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    public static long getDifferenceHours(Calendar d1, Calendar d2) {
        long diff = d2.getTime().getTime() - d1.getTime().getTime();
        return TimeUnit.HOURS.convert(diff, TimeUnit.MILLISECONDS);
    }

    public static long getDifferenceMinutes(Calendar d1, Calendar d2) {
        long diff = d2.getTime().getTime() - d1.getTime().getTime();
        return TimeUnit.MINUTES.convert(diff, TimeUnit.MILLISECONDS);
    }
}
