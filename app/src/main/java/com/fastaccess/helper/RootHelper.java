package com.fastaccess.helper;

import com.chrisplus.rootmanager.RootManager;

import java.io.File;


public final class RootHelper {
    private static final String[] pathList;

    private static final String KEY_SU = "su";

    static {
        pathList = new String[]{
                "/sbin/",
                "/system/bin/",
                "/system/xbin/",
                "/data/local/xbin/",
                "/data/local/bin/",
                "/system/sd/xbin/",
                "/system/bin/failsafe/",
                "/data/local/"
        };
    }

    private static boolean doesFileExists(String value) {
        boolean result = false;
        for (String path : pathList) {
            File file = new File(path + "/" + value);
            result = file.exists();
            if (result) {
                break;
            }
        }
        return result;
    }

    public static boolean isDeviceRooted() {
        return RootManager.getInstance().hasRooted() || doesFileExists(KEY_SU);
    }

    public static boolean obtainRoot() {
        return RootManager.getInstance().obtainPermission();
    }
}

