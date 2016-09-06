package com.fastaccess.provider.service;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.fastaccess.R;
import com.fastaccess.data.dao.ExtractorEventModel;
import com.fastaccess.helper.ApkHelper;
import com.fastaccess.helper.FileHelper;
import com.fastaccess.helper.NotificationHelper;

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
                File dest = FileHelper.generateFile(fileName);
                boolean extracted = ApkHelper.extractApk(src, dest);
                ExtractorEventModel model = new ExtractorEventModel();
                model.setExtracted(extracted);
                model.setForShare(isForShare);
                model.setDestFile(dest);
                model.setAppName(fileName);
                if (isForShare) {
                    Intent shareIntent = ApkHelper.getShareIntent(dest, fileName);
                    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, shareIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    NotificationHelper.notifyShort(this, fileName, getString(R.string.file_extracted_successfully), R.drawable.ic_share,
                            new NotificationCompat.Action(R.drawable.ic_share, getString(R.string.share), pendingIntent), fileName.hashCode());
                } else {
                    NotificationHelper.notifyShort(this, fileName, getString(R.string.file_extracted_successfully), R.drawable.ic_extract);
                }
                stopSelf();
            }
        }
    }
}
