package com.fastaccess.provider.service;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.fastaccess.R;
import com.fastaccess.helper.ApkHelper;
import com.fastaccess.helper.Bundler;
import com.fastaccess.helper.FileHelper;
import com.fastaccess.helper.NotificationHelper;
import com.fastaccess.provider.receiver.StopNotificationReceiver;

import java.io.File;

/**
 * Created by Kosh on 05 Sep 2016, 8:55 PM
 */

public class ExtractorService extends IntentService {

    public ExtractorService() {
        super("ExtractorService");
    }

    @Override protected void onHandleIntent(Intent intent) {
        if (intent == null || intent.getExtras() == null) {
            stopSelf();
        } else {
            File src = (File) intent.getExtras().getSerializable("src");
            boolean isForShare = intent.getExtras().getBoolean("isForShare");
            String fileName = intent.getExtras().getString("name");
            if (src == null || !src.exists()) {
                stopSelf();
            } else {
                if (fileName == null) fileName = src.getName();
                handleFile(src, fileName, isForShare);
            }
        }
    }

    private void handleFile(@NonNull File src, @NonNull String fileName, boolean isForShare) {
        File dest = FileHelper.generateFile(fileName);
        boolean extracted = ApkHelper.extractApk(src, dest);
        if (!extracted) {
            Toast.makeText(this, "Error extracting apk file", Toast.LENGTH_SHORT).show();
            return;
        }
        if (isForShare) {
            Intent shareIntent = new Intent(StopNotificationReceiver.STOP_ACTION);
            shareIntent.putExtras(Bundler
                    .start()
                    .put("dest", dest)
                    .put("fileName", fileName)
                    .end());
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, shareIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationHelper.notifyShort(this, fileName, getString(R.string.file_extracted_successfully), R.drawable.ic_share,
                    new NotificationCompat.Action(R.drawable.ic_share, getString(R.string.share), pendingIntent), fileName.hashCode());
        } else {
            Intent intent = new Intent(StopNotificationReceiver.STOP_ACTION);
            intent.putExtras(Bundler
                    .start()
                    .put("fileName", fileName)
                    .end());
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationHelper.notifyShort(this, fileName, getString(R.string.file_extracted_successfully), R.drawable.ic_extract, fileName
                    .hashCode(), pendingIntent);
        }
        stopSelf();
    }
}
