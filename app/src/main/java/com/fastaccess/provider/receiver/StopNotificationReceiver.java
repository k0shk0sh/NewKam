package com.fastaccess.provider.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.fastaccess.BuildConfig;
import com.fastaccess.helper.ApkHelper;
import com.fastaccess.helper.Logger;
import com.fastaccess.helper.NotificationHelper;

import java.io.File;

/**
 * Created by Kosh on 11 Sep 2016, 7:22 PM
 */

public class StopNotificationReceiver extends BroadcastReceiver {

    public static final String STOP_ACTION = BuildConfig.APPLICATION_ID + ".STOP_NOTIFICATION";

    @Override public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equalsIgnoreCase(STOP_ACTION)) {
            Bundle bundle = intent.getExtras();
            String fileName = bundle.getString("fileName");
            File dest = (File) bundle.getSerializable("dest");
            Logger.e(fileName, dest);
            if (dest != null && dest.exists() && fileName != null) {
                Intent shareIntent = ApkHelper.getShareIntent(dest, fileName);
                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//android ignores the pre-setted flag in the shareIntent
                Logger.e("sharing");
                context.startActivity(shareIntent);
            }
            if (fileName != null) {
                NotificationHelper.cancelNotification(context, fileName.hashCode());
            }
        } else {
            Logger.e("received wrong action ?", intent.getAction());
        }
    }
}
