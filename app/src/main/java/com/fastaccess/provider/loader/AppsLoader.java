package com.fastaccess.provider.loader;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.v4.content.AsyncTaskLoader;

import com.fastaccess.BuildConfig;
import com.fastaccess.data.dao.AppsModel;
import com.fastaccess.helper.ApkHelper;
import com.fastaccess.helper.IconCache;
import com.fastaccess.helper.Logger;
import com.fastaccess.provider.receiver.ApplicationsReceiver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AppsLoader extends AsyncTaskLoader<List<AppsModel>> {
    private ApplicationsReceiver mAppsObserver;
    private final PackageManager packageManager;
    private List<AppsModel> appsModelList;
    private IconCache iconCache;

    public AppsLoader(Context ctx, IconCache iconCache) {
        super(ctx);
        packageManager = getContext().getPackageManager();
        this.iconCache = iconCache;
    }

    @Override public List<AppsModel> loadInBackground() {
        try {
            List<AppsModel> entries = new ArrayList<>();
            Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
            mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            List<ResolveInfo> list = packageManager.queryIntentActivities(mainIntent, 0);
            if (list == null || list.isEmpty()) {
                return new ArrayList<>();
            }
            Logger.e(list.size());
            Collections.sort(list, new ResolveInfo.DisplayNameComparator(packageManager));
            String kamPackage = BuildConfig.APPLICATION_ID;
            for (ResolveInfo resolveInfo : list) {
                if (!resolveInfo.activityInfo.applicationInfo.packageName.equalsIgnoreCase(kamPackage)) {
                    AppsModel model = new AppsModel(packageManager, resolveInfo, iconCache, null);
                    entries.add(model);
                }
            }
            Logger.e("final list size : " + entries.size());
            return entries;
        } catch (Exception e) {//catching TransactionTooLargeException,
            e.printStackTrace();
            return ApkHelper.getInstalledPackages(getContext(), iconCache);
        }
    }

    @Override public void deliverResult(List<AppsModel> apps) {
        if (isReset()) {
            if (apps != null) {
                return;
            }
        }
        appsModelList = apps;
        if (isStarted()) {
            super.deliverResult(apps);
        }
    }

    @Override protected void onStartLoading() {
        if (appsModelList != null) {
            deliverResult(appsModelList);
        }
        if (mAppsObserver == null) {
            mAppsObserver = new ApplicationsReceiver(this);
        }
        if (takeContentChanged()) {
            forceLoad();
        } else if (appsModelList == null) {
            forceLoad();
        }


    }

    @Override protected void onStopLoading() {
        cancelLoad();
    }

    @Override protected void onReset() {
        onStopLoading();
        if (appsModelList != null) {
            appsModelList = null;
        }
        if (mAppsObserver != null) {
            getContext().unregisterReceiver(mAppsObserver);
            mAppsObserver = null;
        }
    }

    @Override public void onCanceled(List<AppsModel> apps) {
        super.onCanceled(apps);
    }

    @Override public void forceLoad() {
        super.forceLoad();
    }
}
