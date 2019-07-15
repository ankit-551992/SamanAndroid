package com.algorepublic.saman.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.algorepublic.saman.R;
import com.algorepublic.saman.data.model.User;
import com.algorepublic.saman.data.model.apis.SimpleSuccess;
import com.algorepublic.saman.data.model.apis.UserResponse;
import com.algorepublic.saman.network.WebServicesHandler;
import com.algorepublic.saman.ui.activities.PopupActivity;
import com.algorepublic.saman.ui.activities.home.DashboardActivity;
import com.algorepublic.saman.ui.activities.myaccount.messages.MessagingActivity;
import com.algorepublic.saman.utils.Constants;
import com.algorepublic.saman.utils.GlobalValues;
import com.algorepublic.saman.utils.SamanApp;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

import me.leolin.shortcutbadger.ShortcutBadger;
import retrofit2.Call;
import retrofit2.Response;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getName();
    private static final String ADMIN_CHANNEL_ID = "admin_channel";
    private NotificationManager notificationManager;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (GlobalValues.getNotificationOnOff(getApplicationContext())) {
            if(remoteMessage.getData().containsKey("isFeedback")){
                boolean isFeedback= Boolean.parseBoolean(remoteMessage.getData().get("isFeedback"));
                if(isFeedback){
                    popUp(remoteMessage);
                }else {
                    showNotification(remoteMessage);
                }
            }else {
               showNotification(remoteMessage);
            }
        }
    }

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onNewToken(String token) {
//        Log.d(TAG, "Refreshed token: " + token);
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        super.onNewToken(token);
        GlobalValues.setUserToken(getApplicationContext(), token);

        if (GlobalValues.getUserLoginStatus(getApplicationContext())) {
            User authenticatedUser;
            authenticatedUser = GlobalValues.getUser(getApplicationContext());
            WebServicesHandler.instance.updateDeviceToken(authenticatedUser.getId(), token, new retrofit2.Callback<UserResponse>() {
                @Override
                public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                }

                @Override
                public void onFailure(Call<UserResponse> call, Throwable t) {
                }
            });
        }
    }

    private void showNotification(RemoteMessage remoteMessage) {

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notificationIntent;
        notificationIntent = new Intent(this, MessagingActivity.class);
        notificationIntent.putExtra("ConversationID", Integer.parseInt(remoteMessage.getData().get("conversationID")));
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        int uniqueInt = (int) (System.currentTimeMillis() & 0xfffffff);
        final PendingIntent pendingIntent = PendingIntent.getActivity(this, uniqueInt, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        //Setting up Notification channels for android O and above
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            setupChannels();
        }
        int notificationId = new Random().nextInt(60000);

//        Bitmap bitmap = getBitmapfromUrl(remoteMessage.getData().get("https://images.pexels.com/photos/67636/rose-blue-flower-rose-blooms-67636.jpeg?auto=compress&cs=tinysrgb&h=350")); //obtain the image
//        Bitmap bitmap = getBitmapfromUrl("https://images.pexels.com/photos/67636/rose-blue-flower-rose-blooms-67636.jpeg?auto=compress&cs=tinysrgb&h=350"); //obtain the image
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, ADMIN_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)  //a resource for your custom small icon
//                .setLargeIcon(bitmap)
                .setColor(getResources().getColor(R.color.colorPrimary))
                .setContentTitle(remoteMessage.getData().get("title"))
                .setContentText(remoteMessage.getData().get("message"))
                .setAutoCancel(true)  //dismisses the notification on click
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

//        Log.e("Notification", remoteMessage.getData().toString());

        notificationManager.notify(notificationId /* ID of notification */, notificationBuilder.build());

        if (SamanApp.isScreenOpen) {
            sendMessage(remoteMessage.getData().get("message"), Integer.parseInt(remoteMessage.getData().get("conversationID")));
        }
        else {
//            Constants.showAlert();
            int newCount=GlobalValues.getBadgeCount(getApplicationContext())+1;
            GlobalValues.setBadgeCount(getApplicationContext(),newCount);
            ShortcutBadger.applyCount(getApplicationContext(),newCount);
        }
    }

    private void sendMessage(String message, int id) {
        Intent intent = new Intent("messageReceived");
        // You can also include some extra data.
        intent.putExtra("message", message);
        intent.putExtra("id", id);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupChannels() {
        CharSequence adminChannelName = getString(R.string.notifications_admin_channel_name);
        String adminChannelDescription = getString(R.string.notifications_admin_channel_description);

        NotificationChannel adminChannel;
        adminChannel = new NotificationChannel(ADMIN_CHANNEL_ID, adminChannelName, NotificationManager.IMPORTANCE_LOW);
        adminChannel.setDescription(adminChannelDescription);
        adminChannel.enableLights(true);
        adminChannel.setLightColor(Color.RED);
        adminChannel.enableVibration(true);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(adminChannel);
        }
    }


    //Simple method for image downloading
    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    private void popUp(RemoteMessage remoteMessage) {
//        Log.e("Notification", remoteMessage.getData().toString());

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notificationIntent;
        notificationIntent = new Intent(this, PopupActivity.class);
        notificationIntent.putExtra("orderID", Integer.parseInt(remoteMessage.getData().get("orderID")));
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        int uniqueInt = (int) (System.currentTimeMillis() & 0xfffffff);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, uniqueInt, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        //Setting up Notification channels for android O and above
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            setupChannels();
        }
        int notificationId = new Random().nextInt(60000);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, ADMIN_CHANNEL_ID)
//                .setSmallIcon(R.drawable.ic_logo)  //a resource for your custom small icon
                .setSmallIcon(R.drawable.ic_notification)  //a resource for your custom small icon
//                .setLargeIcon(bitmap)
                .setColor(getResources().getColor(R.color.colorPrimary))
                .setContentTitle(remoteMessage.getData().get("title"))
                .setContentText(remoteMessage.getData().get("message"))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        notificationManager.notify(notificationId /* ID of notification */, notificationBuilder.build());
    }

}
