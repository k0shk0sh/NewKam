package com.fastaccess.ui.modules.permissions;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;

import com.fastaccess.R;
import com.fastaccess.permission.base.PermissionHelper;
import com.fastaccess.permission.base.activity.BasePermissionActivity;
import com.fastaccess.permission.base.model.PermissionModel;
import com.fastaccess.permission.base.model.PermissionModelBuilder;
import com.fastaccess.ui.modules.main.view.MainView;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Kosh on 31 Aug 2016, 10:24 AM
 */

public class PermissionActivity extends BasePermissionActivity {

    public static final String[] PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    @NonNull @Override protected List<PermissionModel> permissions() {
        return Arrays.asList(
                PermissionModelBuilder
                        .withContext(this.getApplicationContext())
                        .withCanSkip(false)
                        .withExplanationMessage(R.string.write_sdcard_explanation)
                        .withFontType("fonts/app_font.ttf")
                        .withMessage(R.string.write_sdcard_msg)
                        .withTitle(R.string.write_sdcard_title)
                        .withPermissionName(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .withLayoutColorRes(R.color.primary_dark)
                        .withImageResourceId(R.drawable.ic_computing_cloud)
                        .build(),
                PermissionModelBuilder
                        .withContext(this.getApplicationContext())
                        .withCanSkip(false)
                        .withExplanationMessage(R.string.read_sdcard_explanation)
                        .withFontType("fonts/app_font.ttf")
                        .withMessage(R.string.read_sdcard_msg)
                        .withTitle(R.string.read_sdcard_title)
                        .withPermissionName(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withImageResourceId(R.drawable.ic_computing_cloud)
                        .withLayoutColorRes(R.color.primary_dark)
                        .build());
    }

    @Override protected int theme() {
        return R.style.AppTheme;
    }

    @Override protected void onIntroFinished() {
        startActivity(new Intent(this, MainView.class));
        finish();
    }

    @Nullable @Override protected ViewPager.PageTransformer pagerTransformer() {
        return null;
    }

    @Override protected boolean backPressIsEnabled() {
        return false;
    }

    @Override protected void permissionIsPermanentlyDenied(@NonNull String permissionName) {
        new AlertDialog.Builder(this)
                .setTitle(permissionName)
                .setMessage(R.string.denied_permission)
                .setPositiveButton(R.string.open_settings_screen, new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which) {
                        PermissionHelper.openSettingsScreen(PermissionActivity.this);
                    }
                })
                .setNegativeButton(R.string.terminate_app, new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .show();

    }

    @Override protected void onUserDeclinePermission(@NonNull String permissionName) {

    }
}
