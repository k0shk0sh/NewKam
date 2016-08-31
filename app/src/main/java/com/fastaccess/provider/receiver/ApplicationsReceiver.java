package com.fastaccess.provider.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.fastaccess.provider.loader.AppsLoader;

/**
 * Created by kosh on 12/12/2014. CopyRights @ styleme
 */
public class ApplicationsReceiver extends BroadcastReceiver {

    private AppsLoader mLoader;

    public ApplicationsReceiver(AppsLoader loader) {
        mLoader = loader;
        IntentFilter filter = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        filter.addAction(Intent.ACTION_PACKAGE_CHANGED);
        filter.addDataScheme("package");
        mLoader.getContext().registerReceiver(this, filter);
    }

    @Override public void onReceive(Context context, Intent intent) {
        mLoader.onContentChanged();
    }
}
