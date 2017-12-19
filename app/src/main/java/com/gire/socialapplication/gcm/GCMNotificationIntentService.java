package com.gire.socialapplication.gcm;

import android.app.ActivityManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.gire.socialapplication.R;
import com.gire.socialapplication.activity.HomeActivity;
import com.gire.socialapplication.constants.CommonConstants;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.util.List;
import java.util.Random;

/**
 * Created by girish on 7/25/2017.
 */

public class GCMNotificationIntentService extends IntentService {

    public GCMNotificationIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String messageType = gcm.getMessageType(intent);
        String message = intent.getStringExtra("message");

        Boolean forgroundBackgroung = checkApp();
        if(forgroundBackgroung == true){
            Log.d("state","true");
            if(message!=null) {
                generateNotification(getApplicationContext(),message,0);
            }
        }else {
            Log.d("state","false");
            Toast.makeText(getApplicationContext(),"background", Toast.LENGTH_SHORT).show();
            if(message!=null) {
                generateNotification(getApplicationContext(),message,0);
            }
        }

        if (!extras.isEmpty()) {
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {

            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {

            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {

            }
        }
        GcmBroadcastReceiver.completeWakefulIntent(intent);

    }

    private void generateNotification(Context context, String message, int status) {

        Intent notificationIntent;
        notificationIntent = new Intent(context,HomeActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent intent = PendingIntent.getActivity(context, 0, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        NotificationManager notificationManager;
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        builder.setAutoCancel(true);
        builder.setContentTitle("Social APP Update");
        builder.setContentText(message);
        builder.setSmallIcon(getNotificationIcon());
        builder.setPriority(Notification.PRIORITY_HIGH);
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(message));
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round);
        builder.setLargeIcon(bitmap);

        builder.setContentIntent(intent);

        //builder.build();

        // Set Vibrate, Sound and Light
        int defaults = 0;
        defaults = defaults | Notification.DEFAULT_LIGHTS;
        defaults = defaults | Notification.DEFAULT_VIBRATE;
        defaults = defaults | Notification.DEFAULT_SOUND;

        builder.setDefaults(defaults);
        // Set the content for Notification
        builder.setContentText(message);
        builder.setNumber(9999);
        // Set autocancel
        builder.setAutoCancel(true);
        Random random = new Random();
        int m = random.nextInt(9999 - 1000) + 1000;
        notificationManager.notify(m, builder.build());

    }

    private int getNotificationIcon() {
        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.mipmap.ic_launcher_round : R.mipmap.ic_launcher_round;
    }

    public boolean checkApp(){
        ActivityManager am = (ActivityManager) this
                .getSystemService(ACTIVITY_SERVICE);

        // get the info from the currently running task
        List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);

        ComponentName componentInfo = taskInfo.get(0).topActivity;
        if (componentInfo.getPackageName().equalsIgnoreCase("com.gire.socialapplication")) {
            return true;
        } else {
            return false;
        }
    }

}
