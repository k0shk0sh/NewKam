package com.fastaccess.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.chrisplus.rootmanager.RootManager;
import com.chrisplus.rootmanager.container.Result;
import com.fastaccess.BuildConfig;
import com.fastaccess.data.dao.AppsModel;
import com.fastaccess.ui.modules.details.view.PermissionsView;

import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.fastaccess.ui.modules.details.presenter.AppDetailsPresenter.APP_UNINSTALL_RESULT;

/**
 * Created by Kosh on 30 Aug 2016, 11:22 PM
 */

public class ApkHelper {

    private ApkHelper() {}

    @Nullable public static Result installApk(@NonNull Context context, @NonNull File filename) {
        if (RootHelper.isDeviceRooted()) {
            return installSilently(filename);
        } else {
            Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.fromFile(filename));
            context.startActivity(intent);
            return null;
        }
    }

    public static Result installSilently(@NonNull File filename) {
        return RootManager.getInstance().installPackage(filename.getPath());
    }

    public static Result installApk(@NonNull Context context, @NonNull String filename) {
        return installApk(context, new File(filename));
    }

    public static Result uninstallApkSilently(@NonNull String apkPath) {
        return RootManager.getInstance().uninstallPackage(apkPath);
    }

    public static Result uninstallApkSilently(@NonNull File apkPath) {
        return uninstallApkSilently(apkPath.getPath());
    }

    public static void uninstallApp(@NonNull Activity context, @NonNull String packageName) {
        Intent intent = new Intent(Intent.ACTION_UNINSTALL_PACKAGE);
        intent.setData(Uri.parse("package:" + packageName));
        intent.putExtra(Intent.EXTRA_RETURN_RESULT, true);
        context.startActivityForResult(intent, APP_UNINSTALL_RESULT);
    }

    public static long getFolderSize(@NonNull File folder) {
        return FileUtils.sizeOf(folder);
    }

    @Nullable public static PermissionInfo getPermissionInfo(@NonNull Context context, @NonNull String permission) {
        try {
            return context.getPackageManager().getPermissionInfo(permission, PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable public static List<PermissionsView> getAppPermissions(@NonNull Context context, String packageName) {
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

    public static boolean extractApk(@NonNull File src, @NonNull File dest) {
        try {
            FileUtils.copyFile(src, dest);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void shareApk(@NonNull Context context, @NonNull File dest, @NonNull String appName) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(dest));
        intent.setType("application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(intent, "Share " + appName));
    }

    public static Intent getShareIntent(@NonNull File dest, @NonNull String appName) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(dest));
        intent.setType("application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return Intent.createChooser(intent, "Share " + appName);
    }

    @NonNull public static List<AppsModel> getInstalledPackages(@NonNull Context context, @NonNull IconCache iconCache) {
        final PackageManager pm = context.getPackageManager();
        Process process;
        List<AppsModel> result = new ArrayList<>();
        BufferedReader bufferedReader = null;
        try {
            process = Runtime.getRuntime().exec("pm list packages");
            bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                final String packageName = line.substring(line.indexOf(':') + 1);
                PackageInfo packageInfo = pm.getPackageInfo(packageName, 0);
                Intent mainIntent = pm.getLaunchIntentForPackage(packageInfo.applicationInfo.packageName);
                if (mainIntent != null) {
                    ResolveInfo resolveInfo = pm.resolveActivity(mainIntent, 0);
                    if (resolveInfo != null) {
                        if (!packageName.equalsIgnoreCase(BuildConfig.APPLICATION_ID)) {
                            AppsModel model = new AppsModel(pm, resolveInfo, iconCache, null);
                            result.add(model);
                        }
                    }
                }
            }
            process.waitFor();
            Collections.sort(result, AppsModel.sortApps());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null)
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return result;
    }
}
