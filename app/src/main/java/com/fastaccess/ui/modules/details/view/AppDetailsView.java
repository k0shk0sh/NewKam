package com.fastaccess.ui.modules.details.view;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.bowyer.app.fabtoolbar.FabToolbar;
import com.fastaccess.App;
import com.fastaccess.R;
import com.fastaccess.data.dao.AppsModel;
import com.fastaccess.helper.ApkHelper;
import com.fastaccess.helper.DateHelper;
import com.fastaccess.helper.ViewHelper;
import com.fastaccess.ui.adapter.PermissionsAdapter;
import com.fastaccess.ui.base.BaseActivity;
import com.fastaccess.ui.widgets.FontTextView;
import com.fastaccess.ui.widgets.ForegroundImageView;
import com.fastaccess.ui.widgets.ShadowTransformer;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import icepick.State;

/**
 * Created by Kosh on 31 Aug 2016, 4:07 PM
 */

public class AppDetailsView extends BaseActivity implements AppBarLayout.OnOffsetChangedListener {

    @State AppsModel app;
    @State boolean fabBarShowen;
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.closeToolbar) ForegroundImageView closeToolbar;
    @BindView(R.id.fabToolbar) FabToolbar fabToolbar;
    @BindView(R.id.appIcon) ImageView appIcon;
    @BindView(R.id.appName) FontTextView appName;
    @BindView(R.id.versionName) FontTextView versionName;
    @BindView(R.id.versionCode) FontTextView versionCode;
    @BindView(R.id.firstInstall) FontTextView firstInstall;
    @BindView(R.id.lastUpdate) FontTextView lastUpdate;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.appbar) AppBarLayout appbar;
    @BindView(R.id.share) ForegroundImageView share;
    @BindView(R.id.extract) ForegroundImageView extract;
    @BindView(R.id.backup) ForegroundImageView backup;
    @BindView(R.id.collapsingToolbarLayout) CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.shadow) View shadow;
    @BindView(R.id.viewPager) ViewPager viewPager;

    @OnClick(R.id.fab) void onMenu() {
        fabToolbar.expandFab();
        fabBarShowen = true;
    }

    @OnClick(R.id.closeToolbar) void onCloseFab() {
        fabToolbar.slideOutFab();
        fabBarShowen = false;
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

    @Override protected boolean isSecured() {
        return false;
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            app = getIntent().getExtras().getParcelable("app");
        }
        if (app == null) {
            finish();
            return;
        }
        collapsingToolbarLayout.setTitleEnabled(true);
        setTitle("");
        appbar.addOnOffsetChangedListener(this);
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

    @Override protected void onDestroy() {
        appbar.removeOnOffsetChangedListener(this);
        super.onDestroy();
    }

    /**
     * view is not attached on orientation change, library does not handle this.
     */
    private void onShowFabToolbar() {
        if (fabBarShowen) {
            fabToolbar.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override public boolean onPreDraw() {
                    onMenu();
                    fabToolbar.getViewTreeObserver().removeOnPreDrawListener(this);
                    return false;
                }
            });
        }
    }

    @Override public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        boolean canShow = Math.abs(appBarLayout.getTotalScrollRange() + verticalOffset) < 100;
        collapsingToolbarLayout.setTitle(canShow ? getString(R.string.app_name) : "");
        shadow.setVisibility(canShow ? View.VISIBLE : View.GONE);
    }
}
