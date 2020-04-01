package com.napworks.steerlyfeapp.firebasePackage;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.napworks.steerlyfeapp.utilsPackage.CommonMethods;
import com.napworks.steerlyfeapp.utilsPackage.MyConstants;


public class MyFirebaseInstanceIDService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName();
    public MyFirebaseInstanceIDService() {

    }

    public static void getToken(final Context context) {
        try {
            FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                            if (task.isSuccessful())
                            {
                               String refreshedToken = task.getResult().getToken();
                                storeRegIdInPref(refreshedToken, context);
                            }
                        }
                    });
        } catch (Exception e) {
            CommonMethods.showLog(TAG, "error " + e.getMessage());
        }
    }

    private static void storeRegIdInPref(String token, Context context) {
        CommonMethods.showLog(TAG, "NOTIFICATION_TOKEN " + token);
        SharedPreferences sharedPreferences = context.getSharedPreferences(MyConstants.SHARED_PREFERENCE,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(MyConstants.NOTIFICATION_TOKEN, token);
        editor.apply();
    }
}
