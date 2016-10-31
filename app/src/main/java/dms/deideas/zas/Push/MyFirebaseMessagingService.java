package dms.deideas.zas.Push;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;


import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import dms.deideas.zas.Activities.MainActivity;
import dms.deideas.zas.R;

/**
 * Created by bnavarro on 15/07/2016.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //Displaying data in log
        //It is optional
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());

        //Calling method to generate notification
        sendNotification(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());
    }

    //This method is only generating push notification
    //It is same as we did in earlier posts
    private void sendNotification(String messageTitle, String messageBody) {

        messageTitle = (messageTitle != "") ? messageTitle: "Notificación";
        Intent intent = new Intent(this, MainActivity.class);
        // use System.currentTimeMillis() to have a unique ID for the pending intent
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

        if (Build.VERSION.SDK_INT < 16) {
            Notification n  = new Notification.Builder(this)
                    .setContentTitle(messageTitle)
                    .setContentText(messageBody)
                    .setSmallIcon(R.drawable.ic_pedidos_iconorestaurante)
                    .setContentIntent(pIntent)
                    .setAutoCancel(true).getNotification();
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(0, n);
        } else {
            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_pedidos_iconorestaurante);
            Notification n  = new Notification.Builder(this)
                    .setContentTitle("Firebase Push Notification")
                    .setContentText(messageBody)
                    .setSmallIcon(R.drawable.ic_pedidos_iconorestaurante)
                    .setLargeIcon(bm)
                    .setContentIntent(pIntent)
                    .setAutoCancel(true).build();

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(0, n);
        }
    }
}
