package com.fastaccess.helper;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.fastaccess.ui.modules.details.view.PermissionsView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kosh on 30 Aug 2016, 11:22 PM
 */

public class ApkHelper {

    private ApkHelper() {}

    public static void installApk(Context context, File filename) {
        Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.fromFile(filename));
        context.startActivity(intent);
    }

    public static void installApk(Context context, String filename) {
        installApk(context, new File(filename));
    }

    public static long getFolderSize(File f) {
        long size = 0;
        if (f.isDirectory()) {
            for (File file : f.listFiles()) {
                size += getFolderSize(file);
            }
        } else {
            size = f.length();
        }
        return size;
    }

    public static boolean isRooted() {
        return RootHelper.isDeviceRooted();
    }

    @Nullable public static PermissionInfo getPermissionInfo(@NonNull Context context, @NonNull String permission) {
        try {
            return context.getPackageManager().getPermissionInfo(permission, PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable public static List<PermissionsView> getAppPermissions(Context context, String packageName) {
        try {
            List<PermissionsView> permissionsViews = new ArrayList<>();
            PackageInfo info = context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_PERMISSIONS);
            for (String permission : info.requestedPermissions) {
                PermissionInfo permissionInfo = getPermissionInfo(context, permission);
                if (!InputHelper.isEmpty(permission) && permissionInfo != null &&
                        !InputHelper.isEmpty(permissionInfo.loadDescription(context.getPackageManager()))) {
                    permissionsViews.add(PermissionsView.getInstance(permission));
                }
            }
            return permissionsViews;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
