package com.fastaccess.ui.modules.details.view;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.Toast;

import com.bowyer.app.fabtoolbar.FabToolbar;
import com.fastaccess.App;
import com.fastaccess.R;
import com.fastaccess.data.dao.AppsModel;
import com.fastaccess.helper.ApkHelper;
import com.fastaccess.helper.DateHelper;
import com.fastaccess.helper.ViewHelper;
import com.fastaccess.ui.adapter.PermissionsAdapter;
import com.fastaccess.ui.base.BaseActivity;
import com.fastaccess.ui.modules.details.model.AppDetailsMvp;
import com.fastaccess.ui.modules.details.presenter.AppDetailsPresenter;
import com.fastaccess.ui.widgets.FontTextView;
import com.fastaccess.ui.widgets.ShadowTransformer;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import icepick.State;

/**
 * Created by Kosh on 31 Aug 2016, 4:07 PM
 */

public class AppDetailsView extends BaseActivity implements AppDetailsMvp.View {

    @State AppsModel app;
    @State boolean fabBarShowing;
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.fabToolbar) FabToolbar fabToolbar;
    @BindView(R.id.appIcon) ImageView appIcon;
    @BindView(R.id.appName) FontTextView appName;
    @BindView(R.id.versionName) FontTextView versionName;
    @BindView(R.id.versionCode) FontTextView versionCode;
    @BindView(R.id.firstInstall) FontTextView firstInstall;
    @BindView(R.id.lastUpdate) FontTextView lastUpdate;
    @BindView(R.id.appbar) AppBarLayout appbar;
    @BindView(R.id.collapsingToolbarLayout) CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.shadow) View shadow;
    @BindView(R.id.viewPager) ViewPager viewPager;
    private AppDetailsPresenter presenter;

    @OnClick(R.id.fab) void onMenu() {
        fabToolbar.expandFab();
        fabBarShowing = true;
    }

    @OnClick(R.id.closeToolbar) void onCloseFab() {
        fabToolbar.slideOutFab();
        fabBarShowing = false;
    }

    @OnClick(R.id.share) void onShare() {
        getPresenter().onShare(this, app);
        onCloseFab();
    }

    @OnClick(R.id.extract) void onExtract() {
        getPresenter().onExtract(this, app);
        onCloseFab();
    }

    @OnClick(R.id.uninstall) void onUninstall() {
        getPresenter().onUninstall(this, app);
        onCloseFab();
    }

    @OnClick(R.id.backup) void onBackup() {
        getPresenter().onBackup(this, app);
        onCloseFab();
    }

    @Override protected int layout() {
        return R.layout.app_details_layout;
    }

    @Override protected boolean isTransparent() {
        return true;
    }

    @Override protected boolean canBack() {
        return true;
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            getPresenter().onActivityStarted(getIntent());
        } else {
            onReceivedBundle(app);
        }
    }

    @Override public void onReceivedBundle(@Nullable AppsModel model) {
        if (model == null) {
            finish();
            return;
        }
        this.app = model;
        onInitViews();
    }

    @Override public void onInitViews() {
        collapsingToolbarLayout.setTitleEnabled(true);
        setTitle("");
        Bitmap bitmap = App.getInstance().getIconCache().getIcon(app.getPackageName(), app.getActivityInfoName());
        appIcon.setImageBitmap(bitmap);
        appName.setText(app.getAppName());
        versionCode.setText("Version Code\n" + app.getVersionCode());
        versionName.setText("Version Name\n" + app.getVersionName());
        firstInstall.setText("Install Time\n" + DateHelper.getDesiredFormat(DateHelper.DateFormats.D_DDMMYYYYHHMMA_N, app.getFirstInstallTime()));
        lastUpdate.setText("Last Update\n" + DateHelper.getDesiredFormat(DateHelper.DateFormats.D_DDMMYYYYHHMMA_N, app.getLastUpdateTime()));
        fabToolbar.setFab(fab);
        onShowFabToolbar();
        List<PermissionsView> permissionsViewList = ApkHelper.getAppPermissions(this, app.getPackageName());
        if (permissionsViewList != null && !permissionsViewList.isEmpty()) {
            PermissionsAdapter adapter = new PermissionsAdapter(getSupportFragmentManager(), permissionsViewList, ViewHelper.toPx(this, 2));
            ShadowTransformer transformer = new ShadowTransformer(viewPager, adapter);
            viewPager.setAdapter(adapter);
            viewPager.setPageTransformer(false, transformer);
            transformer.enableScaling(true);
            viewPager.setOffscreenPageLimit(permissionsViewList.size());
        }
    }

    @Override public void onAppbarScrolled(boolean show) {
        collapsingToolbarLayout.setTitle(show ? getString(R.string.app_name) : "");
        shadow.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getPresenter().onActivityResult(requestCode, resultCode, data);
    }

    @Override protected void onResume() {
        super.onResume();
        appbar.addOnOffsetChangedListener(getPresenter());
    }

    @Override protected void onPause() {
        appbar.removeOnOffsetChangedListener(getPresenter());
        super.onPause();
    }

    /**
     * view is not attached on orientation change, library does not handle this.
     */
    @Override public void onShowFabToolbar() {
        if (fabBarShowing) {
            fabToolbar.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override public boolean onPreDraw() {
                    onMenu();
                    fabToolbar.getViewTreeObserver().removeOnPreDrawListener(this);
                    return false;
                }
            });
        }
    }

    @Override public void onAppUninstalled() {
        finish();
        Toast.makeText(this, R.string.uninstall_successfully, Toast.LENGTH_LONG).show();
    }

    @NonNull @Override public File getApkFile() {
        if (app.getFilePath() == null) {
            try {
                ApplicationInfo packageInfo = getPackageManager().getApplicationInfo(app.getPackageName(), 0);
                app.setFilePath(packageInfo.sourceDir);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return new File(app.getFilePath());
    }

    @Override public void showMessage(@NonNull String msg) {
        onShowMessage(msg);
    }

    @Override public void showMessage(@StringRes int resId) {
        onShowMessage(resId);
    }

    public AppDetailsPresenter getPresenter() {
        if (presenter == null) presenter = AppDetailsPresenter.with(this);
        return presenter;
    }
}
