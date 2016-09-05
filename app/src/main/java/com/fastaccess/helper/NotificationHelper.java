package com.fastaccess.helper;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;

/**
 * Created by kosh20111 on 9/8/2015. CopyRights @ Innov8tif
 */
public class NotificationHelper {

    public static final int NOTIFICATION_ID = 20111;

    public static void notifyShort(@NonNull Context context, @NonNull String title, @NonNull String msg, @DrawableRes int iconId) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(context)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentTitle(title)
                .setContentText(msg)
                .setSmallIcon(iconId)
                .build();
        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    public static void notifyShort(@NonNull Context context, @NonNull String title, String msg, @DrawableRes int iconId,
                                   @NonNull NotificationCompat.Action action) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(context)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentTitle(title)
                .setContentText(msg)
                .setSmallIcon(iconId)
                .addAction(action)
                .build();
        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    public static void notifyBig(@NonNull Context context, @NonNull String title, @NonNull String msg, @DrawableRes int iconId) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(context)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentText(msg)
                .setSmallIcon(iconId)
                .setStyle(new NotificationCompat.BigTextStyle().setBigContentTitle(title).setSummaryText(msg).bigText(msg))
                .build();
        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    public static void notifyWithImage(@NonNull Context context, @NonNull String title, @NonNull String msg, @DrawableRes int iconId,
                                       @NonNull Bitmap bitmap) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(context)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentTitle(title)
                .setContentText(msg)
                .setSmallIcon(iconId)
                .setStyle(new NotificationCompat.BigPictureStyle().setBigContentTitle(title).setSummaryText(msg).bigPicture(bitmap))
                .build();
        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    public static void cancelNotification(@NonNull Context context, int id) {
        int finalId = id == 0 ? NOTIFICATION_ID : id;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(finalId);
    }
}
