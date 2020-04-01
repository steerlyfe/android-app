package com.napworks.steerlyfeapp.firebasePackage;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.napworks.steerlyfeapp.R;
import com.napworks.steerlyfeapp.utilsPackage.CommonMethods;
import com.napworks.steerlyfeapp.utilsPackage.MyConstants;


public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    private SharedPreferences sharedPreferences;
    String notificationType = "", bookingId = "";


    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        sharedPreferences = getSharedPreferences(MyConstants.SHARED_PREFERENCE, MODE_PRIVATE);
        if (!TextUtils.isEmpty(sharedPreferences.getString(MyConstants.USER_ID, ""))) {
        }


    }

    private static void sendAppNotification(Context context, String title, String textMessage, Intent intent, int notificationId) {
        CommonMethods.showLog(TAG, "shownotification");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(R.drawable.app_logo);
        builder.setLargeIcon(bitmap);
        builder.setTicker(textMessage);
        builder.setContentTitle(title);
        builder.setContentText(textMessage);
        builder.setAutoCancel(true);
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(textMessage));
        Uri rinftoneUri;
        rinftoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(rinftoneUri);
        builder.setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                builder.setChannelId(String.valueOf(notificationId));
                notificationManager.createNotificationChannel(new NotificationChannel(String.valueOf(notificationId),
                        context.getString(R.string.app_name), NotificationManager.IMPORTANCE_HIGH));
            }
            CommonMethods.showLog(TAG, "notificationManager");
            notificationManager.notify(notificationId, builder.build());
        } else {
            CommonMethods.showLog(TAG, "Else");
        }
    }

}
