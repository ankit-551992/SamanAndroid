package com.qtech.saman.services;

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
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.qtech.saman.R;
import com.qtech.saman.data.model.User;
import com.qtech.saman.data.model.apis.UserResponse;
import com.qtech.saman.network.WebServicesHandler;
import com.qtech.saman.ui.activities.CustomerSupport.SupportDetailsActivity;
import com.qtech.saman.ui.activities.home.DashboardActivity;
import com.qtech.saman.ui.activities.invoice.InvoiceActivity;
import com.qtech.saman.ui.activities.myaccount.messages.MessagingActivity;
import com.qtech.saman.ui.activities.product.ProductsActivity;
import com.qtech.saman.ui.activities.productdetail.ProductDetailActivity;
import com.qtech.saman.ui.activities.store.StoreDetailActivity;
import com.qtech.saman.utils.Constants;
import com.qtech.saman.utils.GlobalValues;
import com.qtech.saman.utils.SamanApp;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

import me.leolin.shortcutbadger.ShortcutBadger;
import retrofit2.Call;
import retrofit2.Response;

import static com.qtech.saman.utils.Constants.is_firebase_msgnotify;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getName();
    private static final String ADMIN_CHANNEL_ID = "admin_channel";
    NotificationCompat.Builder notificationBuilder;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.e("MESSAGE_NOTIFY", "-000--remoteMessage---getData--" + remoteMessage.getData());
        if (GlobalValues.getNotificationOnOff(getApplicationContext())) {
            Log.e("MESSAGE_NOTIFY", "---00---" + remoteMessage.toString());
            if (remoteMessage.getData().containsKey("IsSupport")) {
                boolean isSupport = Boolean.parseBoolean(remoteMessage.getData().get("IsSupport"));
//                if (GlobalValues.getTypesNotificationOnOff(getApplicationContext(), feedback_notify)) {
//                }
                if (isSupport) {
                    UserSupportReplyNotify(remoteMessage);
                } else {
                    OnlyNotifyItem(remoteMessage);
                }

            } else if (remoteMessage.getData().containsKey("isPromotion")) {
                boolean isPromotion = Boolean.parseBoolean(remoteMessage.getData().get("isPromotion"));
//                if (GlobalValues.getTypesNotificationOnOff(getApplicationContext(), promo_sales_notify)) {
//                }
                if (isPromotion) {
                    promotionSales(remoteMessage);
                } else {
                    OnlyNotifyItem(remoteMessage);
                }
            } else if (remoteMessage.getData().containsKey("IsOrder")) {
                boolean isOrderStatus = Boolean.parseBoolean(remoteMessage.getData().get("IsOrder"));

//                if (GlobalValues.getTypesNotificationOnOff(getApplicationContext(), order_notify)) {
//                }
                if (isOrderStatus) {
                    OrderStatusNotify(remoteMessage);
                } else {
                    OnlyNotifyItem(remoteMessage);
                }
            } else if (remoteMessage.getData().containsKey("IsStock")) {
                boolean IsStock = Boolean.parseBoolean(remoteMessage.getData().get("IsStock"));

//                if (GlobalValues.getTypesNotificationOnOff(getApplicationContext(), Itemback_notify)) {
//                }
                if (IsStock) {
                    stockProductNotify(remoteMessage);
                } else {
                    OnlyNotifyItem(remoteMessage);
                }
            } else if (remoteMessage.getData().containsKey("IsMessage")) {
                boolean IsMessage = Boolean.parseBoolean(remoteMessage.getData().get("IsMessage"));
//                if (GlobalValues.getTypesNotificationOnOff(getApplicationContext(), msg_notify)) {
//                }
                if (IsMessage) {
                    showMessageNotification(remoteMessage);
                } else {
                    OnlyNotifyItem(remoteMessage);
                }
            } else {
                OnlyNotifyItem(remoteMessage);
            }
        }
    }

    private void stockProductNotify(RemoteMessage remoteMessage) {
        Log.e("REMOTE_MESSAGE", "--IsOrder----showNotification-----remoteMessage---" + remoteMessage.getData());
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        //getInvoiceDetailes(orderID, remoteMessage);
        PendingIntent pendingIntent = null;
        Intent promotion_Intent = new Intent(this, ProductDetailActivity.class);
        //promotion_Intent.putExtra("Obj", orderHistoryArrayList);
        promotion_Intent.putExtra("ProductID", Integer.parseInt(remoteMessage.getData().get("ProductId")));
        promotion_Intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        int uniqueInt = (int) (System.currentTimeMillis() & 0xfffffff);
        pendingIntent = PendingIntent.getActivity(this, uniqueInt, promotion_Intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        GetSetNotification(notificationManager, remoteMessage, pendingIntent);

      /*  if (SamanApp.isScreenOpen) {
            Log.e("MESSAGE_NOTIFY", "-OrderStatus----isScreenOpen--remoteMessage-----" + remoteMessage.toString());
        } else {
            Log.e("MESSAGE_NOTIFY", "--OrderStatus----isScreenClose--count----");
            int newCount = GlobalValues.getBadgeCount(getApplicationContext()) + 1;
            GlobalValues.setBadgeCount(getApplicationContext(), newCount);
            ShortcutBadger.applyCount(getApplicationContext(), newCount);
        }*/
    }

    private void OrderStatusNotify(RemoteMessage remoteMessage) {
        Log.e("REMOTE_MESSAGE", "--IsOrder----showNotification-----remoteMessage---" + remoteMessage.getData());

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent pendingIntent = null;
        Intent promotion_Intent = new Intent(this, InvoiceActivity.class);
        promotion_Intent.putExtra("OrderId", Integer.parseInt(remoteMessage.getData().get("OrderId")));
        promotion_Intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        int uniqueInt = (int) (System.currentTimeMillis() & 0xfffffff);
        pendingIntent = PendingIntent.getActivity(this, uniqueInt, promotion_Intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        GetSetNotification(notificationManager, remoteMessage, pendingIntent);

       /* if (SamanApp.isScreenOpen) {
            Log.e("MESSAGE_NOTIFY", "-OrderStatus----isScreenOpen--remoteMessage-----" + remoteMessage.toString());
        } else {
            Log.e("MESSAGE_NOTIFY", "--OrderStatus----isScreenClose--count----");
            int newCount = GlobalValues.getBadgeCount(getApplicationContext()) + 1;
            GlobalValues.setBadgeCount(getApplicationContext(), newCount);
            ShortcutBadger.applyCount(getApplicationContext(), newCount);
        }*/
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
        Log.e("MESSAGE_NOTIFY", "---firebase---token-------" + token);
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

    private void promotionSales(RemoteMessage remoteMessage) {
//        Log.e("Notification", remoteMessage.getData().toString());
        Log.e("MESSAGE_NOTIFY", "--promotionSales---remoteMessage-----" + remoteMessage.toString());

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Intent promotion_Intent = null;
        PendingIntent pendingIntent = null;
        if (remoteMessage.getData().get("type") != null) {
            Integer type_promotion = Integer.parseInt(remoteMessage.getData().get("type"));
            Log.e("MESSAGE_NOTIFY", "--promotionSales---type_promotion-----" + type_promotion);

            if (type_promotion == 0) {
                OnlyNotifyItem(remoteMessage);
            } else {
                if (type_promotion == 1) {
                    // General type
                    promotion_Intent = new Intent(this, DashboardActivity.class);
                    // promotion_Intent.putExtra("orderID", Integer.parseInt(remoteMessage.getData().get("orderID")));
                    promotion_Intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                } else if (type_promotion == 2) {
                    // Product type
                    promotion_Intent = new Intent(this, ProductDetailActivity.class);
                    promotion_Intent.putExtra("ProductID", Integer.parseInt(remoteMessage.getData().get("Ids")));
                    promotion_Intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                } else if (type_promotion == 3) {
                    // Store type
                    promotion_Intent = new Intent(this, StoreDetailActivity.class);
                    promotion_Intent.putExtra("StoreID", Integer.parseInt(remoteMessage.getData().get("Ids")));
                    promotion_Intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                } else if (type_promotion == 4) {
                    // Category type
//                Bundle bundle = new Bundle();
//                bundle.putInt("CategoryID", Integer.parseInt(remoteMessage.getData().get("Ids")));
//                ProductsCategoryFragment fragobj = new ProductsCategoryFragment();
//                fragobj.setArguments(bundle);

                    promotion_Intent = new Intent(this, ProductsActivity.class);
                    promotion_Intent.putExtra("CategoryID", Integer.parseInt(remoteMessage.getData().get("Ids")));
                    promotion_Intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                }
                int uniqueInt = (int) (System.currentTimeMillis() & 0xfffffff);
                pendingIntent = PendingIntent.getActivity(this, uniqueInt, promotion_Intent, PendingIntent.FLAG_UPDATE_CURRENT);
                GetSetNotification(notificationManager, remoteMessage, pendingIntent);
            }
        } else {
            OnlyNotifyItem(remoteMessage);
        }
    }

    private void OnlyNotifyItem(RemoteMessage remoteMessage) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Log.e("MESSAGE_NOTIFY", "--OnlyNotifyItem----" + remoteMessage.toString());
        //Setting up Notification channels for android O and above
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            setupChannels(notificationManager);
        }
        int notificationId = new Random().nextInt(60000);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        notificationBuilder = new NotificationCompat.Builder(this, ADMIN_CHANNEL_ID)
                .setSmallIcon(getNotificationIcon())  //a resource for your custom small icon
//                .setLargeIcon(bitmap)
                .setColor(getResources().getColor(R.color.colorPrimary))
                .setContentTitle(remoteMessage.getData().get("title"))
                .setContentText(remoteMessage.getData().get("message"))
                .setAutoCancel(true)
                .setSound(defaultSoundUri);
        //.setContentIntent(pendingIntent);

        notificationManager.notify(notificationId /* ID of notification */, notificationBuilder.build());
    }

    private void GetSetNotification(NotificationManager notificationManager, RemoteMessage remoteMessage, PendingIntent pendingIntent) {
        //Setting up Notification channels for android O and above
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            setupChannels(notificationManager);
        }

        int notificationId = new Random().nextInt(60000);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        is_firebase_msgnotify = true;
        if (remoteMessage.getData().get("image") != null) {
            String imageUri = remoteMessage.getData().get("image");
            String image = Constants.URLS.BaseURLImages + imageUri;
            Log.e("URL000", "----image----url---" + image);
            Bitmap image_bitmap = getBitmapfromUrl(image);

            notificationBuilder = new NotificationCompat.Builder(this, ADMIN_CHANNEL_ID)
                    .setSmallIcon(getNotificationIcon())  //a resource for your custom small icon
                    .setLargeIcon(image_bitmap)
                    .setColor(getResources().getColor(R.color.colorPrimary))
                    .setContentTitle(remoteMessage.getData().get("title"))
                    .setContentText(remoteMessage.getData().get("message"))
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);
        } else {
            notificationBuilder = new NotificationCompat.Builder(this, ADMIN_CHANNEL_ID)
                    .setSmallIcon(getNotificationIcon())  //a resource for your custom small icon
//                .setLargeIcon(bitmap)
                    .setColor(getResources().getColor(R.color.colorPrimary))
                    .setContentTitle(remoteMessage.getData().get("title"))
                    .setContentText(remoteMessage.getData().get("message"))
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);
        }
        notificationManager.notify(notificationId /* ID of notification */, notificationBuilder.build());
    }

    private int getNotificationIcon() {
        boolean NotificationIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return NotificationIcon ? R.drawable.icon_notify : R.drawable.icon_notify;
    }

    private void showMessageNotification(RemoteMessage remoteMessage) {
        Log.e("REMOTE_MESSAGE", "--showNotification-----remoteMessage---" + remoteMessage.getData());
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notificationIntent;
        notificationIntent = new Intent(this, MessagingActivity.class);
        notificationIntent.putExtra("ConversationID", Integer.parseInt(remoteMessage.getData().get("conversationID")));
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        int uniqueInt = (int) (System.currentTimeMillis() & 0xfffffff);
        final PendingIntent pendingIntent = PendingIntent.getActivity(this, uniqueInt, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        GetSetNotification(notificationManager, remoteMessage, pendingIntent);

        if (SamanApp.isScreenOpen) {
            is_firebase_msgnotify = true;
            sendMessage(remoteMessage.getData().get("IsMessage"), Integer.parseInt(remoteMessage.getData().get("conversationID")));
        } else {
//          Constants.showAlert();
            int newCount = GlobalValues.getBadgeCount(getApplicationContext()) + 1;
            GlobalValues.setBadgeCount(getApplicationContext(), newCount);
            ShortcutBadger.applyCount(getApplicationContext(), newCount);
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
    private void setupChannels(NotificationManager notificationManager) {
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

    private void UserSupportReplyNotify(RemoteMessage remoteMessage) {
        Log.e("MESSAGE_NOTIFY", "-----remoteMessage-----" + remoteMessage.toString());

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notificationIntent;
        //notificationIntent = new Intent(this, PopupActivity.class);
        notificationIntent = new Intent(this, SupportDetailsActivity.class);
        notificationIntent.putExtra("TicketId", Integer.parseInt(remoteMessage.getData().get("TicketId")));
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        int uniqueInt = (int) (System.currentTimeMillis() & 0xfffffff);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, uniqueInt, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        GetSetNotification(notificationManager, remoteMessage, pendingIntent);

        if (SamanApp.isScreenOpen) {
            Log.e("MESSAGE_NOTIFY", "-UserSupport----isScreenOpen--remoteMessage-----" + remoteMessage.toString());
        } else {
            Log.e("MESSAGE_NOTIFY", "--UserSupport----isScreenClose--count----");
            int newCount = GlobalValues.getBadgeCount(getApplicationContext()) + 1;
            GlobalValues.setBadgeCount(getApplicationContext(), newCount);
            ShortcutBadger.applyCount(getApplicationContext(), newCount);
        }
    }
}
