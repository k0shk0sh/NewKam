package com.fastaccess.ui.modules.details.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;

import com.fastaccess.R;
import com.fastaccess.data.dao.AppsModel;
import com.fastaccess.helper.FileHelper;
import com.fastaccess.ui.base.mvp.presenter.BasePresenter;
import com.fastaccess.ui.modules.details.model.AppDetailsMvp;

import org.apache.commons.io.FileUtils;

import java.io.File;

/**
 * Created by Kosh on 31 Aug 2016, 11:04 PM
 */

public class AppDetailsPresenter extends BasePresenter<AppDetailsMvp.View> implements AppDetailsMvp.Presenter {

    private static final int APP_UNINSTALL_RESULT = 100;

    protected AppDetailsPresenter(@NonNull AppDetailsMvp.View view) {
        super(view);
    }

    public static AppDetailsPresenter with(@NonNull AppDetailsMvp.View view) {
        return new AppDetailsPresenter(view);
    }

    @Override public void onActivityStarted(@Nullable Intent intent) {
        if (intent == null || intent.getExtras() == null) {
            getView().onReceivedBundle(null);
        } else {
            Bundle bundle = intent.getExtras();
            AppsModel app = bundle.getParcelable("app");
            getView().onReceivedBundle(app);
        }
    }

    @Override public void onShare(@NonNull Context context, @NonNull AppsModel app) {
        File file = FileHelper.generateFile(app.getAppName());
        try {
            File path = getView().getApkFile();
            if (path.exists()) {
                FileUtils.copyFile(path, file);
                getView().onShowMessage(context.getString(R.string.file_extracted_successfully) + " ( " + file.getPath() + " )");
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                intent.setType("application/vnd.android.package-archive");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(Intent.createChooser(intent, "Share " + app.getAppName()));
            } else {
                getView().onShowMessage(R.string.app_file_error);
            }
        } catch (Exception e) {
            e.printStackTrace();
            getView().onShowMessage(e.getMessage());
        }
    }

    @Override public void onExtract(@NonNull Context context, @NonNull AppsModel app) {
        File file = FileHelper.generateFile(app.getAppName());
        try {
            File path = getView().getApkFile();
            if (path.exists()) {
                FileUtils.copyFile(path, file);
                getView().onShowMessage(context.getString(R.string.file_extracted_successfully) + " ( " + file.getPath() + " )");
            } else {
                getView().onShowMessage(R.string.app_file_error);
            }
        } catch (Exception e) {
            e.printStackTrace();
            getView().onShowMessage(e.getMessage());
        }
    }

    @Override public void onUninstall(@NonNull Activity activity, @NonNull AppsModel app) {
        Intent intent = new Intent(Intent.ACTION_UNINSTALL_PACKAGE);
        intent.setData(Uri.parse("package:" + app.getPackageName()));
        intent.putExtra(Intent.EXTRA_RETURN_RESULT, true);
        activity.startActivityForResult(intent, APP_UNINSTALL_RESULT);
    }

    @Override public void onBackup(@NonNull Context context, @NonNull AppsModel app) {
        //TODO
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == APP_UNINSTALL_RESULT) {
                getView().onAppUninstalled();
            }
        }
    }

    @Override public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        boolean show = Math.abs(appBarLayout.getTotalScrollRange() + verticalOffset) < 100;
        getView().onAppbarScrolled(show);
    }
}
