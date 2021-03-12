package com.web.consultpin.fcm;


import android.app.LauncherActivity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.app.preferences.SavePreferences;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.web.consultpin.R;
import com.web.consultpin.Utilclass;
import com.web.consultpin.registration.SplashScreen;

import org.json.JSONObject;

import java.util.Map;

public class MessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);

        System.out.println("My Device token====" + token);
        SavePreferences savePreferences = new SavePreferences();
        savePreferences.savePreferencesData(this, token, Utilclass.device_Token);


    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        System.out.println("Inside messgae recived====>" + remoteMessage);
        super.onMessageReceived(remoteMessage);

        try {
            String loginDetail=new SavePreferences().reterivePreference(MessagingService.this, Utilclass.loginDetail).toString();
            if(loginDetail.length()!=0)
              {
                Map<String, String> map = remoteMessage.getData();
                String npData = map.get("data");
                JSONObject jsonObject = new JSONObject(npData);
                System.out.println("dataValue====" + jsonObject);
                showNotification(jsonObject.getString("title"), jsonObject.getString("message"));
              }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private void showNotification(String title, String message) {
        Intent intent = new Intent(this, SplashScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);


        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);


        Uri defaultSoundUri = Uri.parse("android.resource://" + getPackageName() + "/");
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String CHANNEL_ID = "All";// The id of the channel.
            CharSequence name = "ConsultPin";// The user-visible name of the channel.
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            AudioAttributes attributes = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION).build();
            mChannel.setSound(defaultSoundUri, attributes); // This is IMPORTANT

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
            Notification notification = mBuilder.setSmallIcon(R.mipmap.ic_launcher).setTicker(this.getResources().getString(R.string.app_name)).setWhen(0)
                    .setAutoCancel(true)
                    .setContentTitle(title)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                    .setContentIntent(pendingIntent)
                    .setChannelId(CHANNEL_ID)
                    .setSound(defaultSoundUri)
                    .setSmallIcon(R.drawable.bell)

                    .setContentText(message).build();

            nm.createNotificationChannel(mChannel);
            nm.notify(401, notification);
        } else {
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                    this);
            Notification notification = mBuilder.setSmallIcon(R.drawable.bell).setTicker(getResources().getString(R.string.app_name)).setWhen(0)
                    .setAutoCancel(true)
                    .setContentTitle(title)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.bell)
                    .setSound(defaultSoundUri)
                    .setContentText(message).build();

            nm.notify(401, notification);
        }
    }

}