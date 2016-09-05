package com.fastaccess.provider.service;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by Kosh on 05 Sep 2016, 10:40 PM
 */

public class BackupService extends IntentService {

    public BackupService() {
        super("BackupService");
    }

    @Override protected void onHandleIntent(Intent intent) {

    }
}
