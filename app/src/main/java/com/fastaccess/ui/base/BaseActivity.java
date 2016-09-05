package com.fastaccess.ui.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.fastaccess.App;
import com.fastaccess.BuildConfig;
import com.fastaccess.R;
import com.fastaccess.data.dao.ExtractorEventModel;
import com.fastaccess.helper.ApkHelper;
import com.fastaccess.helper.AppHelper;
import com.fastaccess.helper.ViewHelper;
import com.fastaccess.kam.filebrowser.view.FilePickerDialog;
import com.fastaccess.permission.base.PermissionHelper;
import com.fastaccess.ui.base.mvp.BaseMvp;
import com.fastaccess.ui.modules.permissions.PermissionActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import icepick.Icepick;
import icepick.State;

/**
 * Created by Kosh on 24 May 2016, 8:48 PM
 */

public abstract class BaseActivity extends AppCompatActivity implements BaseMvp.View {
    @LayoutRes protected abstract int layout();

    @Nullable @BindView(R.id.topProgress) ProgressBar topProgress;
    @Nullable @BindView(R.id.toolbar) Toolbar toolbar;
    @State boolean showHideProgress;

    protected abstract boolean isTransparent();

    protected abstract boolean canBack();

    @Override protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (layout() != 0) {
            setContentView(layout());
            ButterKnife.bind(this);
        }
        Icepick.setDebug(BuildConfig.DEBUG);
        if (savedInstanceState != null && !savedInstanceState.isEmpty()) {
            Icepick.restoreInstanceState(this, savedInstanceState);
        }
        setupToolbarAndStatusBar();
        if (PermissionHelper.declinedPermissions(this.getApplicationContext(), PermissionActivity.PERMISSIONS).length != 0) {
            startActivity(new Intent(this, PermissionActivity.class));
            finish();
            return;
        }
        onShowHideProgress(showHideProgress);
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (canBack()) {
            if (item.getItemId() == android.R.id.home) {
                supportFinishAfterTransition();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override protected void onStart() {
        super.onStart();
        App.getInstance().getEventProvider().register(this.getClass().getSimpleName(), this);
    }

    @Override protected void onStop() {
        App.getInstance().getEventProvider().unregister(this.getClass().getSimpleName());
        super.onStop();
    }

    @Override public void onShowMessage(@StringRes int resId) {
        onShowMessage(getString(resId));
    }

    @Override public void onShowMessage(@NonNull String msg) {
        View view = toolbar == null ? findViewById(android.R.id.content) : toolbar;
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show();
    }

    @Override public void onShowHideProgress(boolean show) {
        this.showHideProgress = show;
        if (topProgress != null) {
            topProgress.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN) public void onExtractionEvent(ExtractorEventModel model) {
        onShowHideProgress(model.isShowHideProgress());
        if (model.isShowHideProgress()) return;
        if (model.isExtracted()) {
            if (model.isForShare()) {
                ApkHelper.shareApk(this, model.getDestFile(), model.getAppName());
                return;
            }
            onShowMessageToOpenFile(R.string.file_extracted_successfully);
        } else {
            onShowMessage(R.string.app_file_error);
        }
    }

    protected void onShowMessageToOpenFile(int resId) {
        View view = toolbar == null ? findViewById(android.R.id.content) : toolbar;
        Snackbar.make(view, resId, Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.open_folder), new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        FilePickerDialog dialog = new FilePickerDialog();
                        dialog.show(getSupportFragmentManager(), "FilePickerDialog");
                    }
                })
                .show();
    }

    private void setupToolbarAndStatusBar() {
        if (AppHelper.isLollipopOrHigher()) {
            changeAppColor();
        }
        if (findViewById(R.id.toolbar) != null) {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            if (canBack()) {
                if (getSupportActionBar() != null) {
                    if (toolbar != null) {
                        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                supportFinishAfterTransition();
                            }
                        });
                    }
                    getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                }
            }
        }
    }

    protected void setToolbarIcon(@DrawableRes int res) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator(res);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void changeAppColor() {
        if (AppHelper.isLollipopOrHigher()) {
            if (!isTransparent()) {
                getWindow().setStatusBarColor(ViewHelper.getPrimaryDarkColor(this));
            } else {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            }
        }
    }
}
