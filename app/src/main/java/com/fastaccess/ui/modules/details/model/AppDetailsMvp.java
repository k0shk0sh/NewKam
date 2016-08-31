package com.fastaccess.ui.modules.details.model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.AppBarLayout;

import com.fastaccess.data.dao.AppsModel;

import java.io.File;

/**
 * Created by Kosh on 31 Aug 2016, 11:00 PM
 */

public interface AppDetailsMvp {

    interface View {
        void onReceivedBundle(@Nullable AppsModel model);

        void onInitViews();

        void onAppbarScrolled(boolean show);

        void onShowFabToolbar();

        void onAppUninstalled();

        void onShowMessage(@StringRes int resId);

        void onShowMessage(@NonNull String msg);

        @NonNull File getApkFile();
    }

    interface Presenter extends AppBarLayout.OnOffsetChangedListener {
        void onActivityStarted(@Nullable Intent intent);

        void onShare(@NonNull Context applicationContext, @NonNull AppsModel app);

        void onExtract(@NonNull Context applicationContext, @NonNull AppsModel app);

        void onUninstall(@NonNull Activity activity, @NonNull AppsModel app);

        void onBackup(@NonNull Context applicationContext, @NonNull AppsModel app);

        void onActivityResult(int requestCode, int resultCode, Intent data);
    }
}
