package com.fastaccess.helper;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.view.View;

import java.io.File;

/**
 * Created by Kosh on 12/12/15 10:51 PM
 */
public class ActivityHelper {

    public static final int REQUEST_CODE = 100;
    public static final int CAMERA_REQUEST_CODE = 101;
    public static final int SELECT_PHOTO_REQUEST = 102;

    @Nullable public static Activity getActivity(@Nullable Context cont) {
        if (cont == null) return null;
        else if (cont instanceof Activity) return (Activity) cont;
        else if (cont instanceof ContextWrapper) return getActivity(((ContextWrapper) cont).getBaseContext());
        return null;
    }

    public static void start(Activity activity, Class cl, View view, String transName) {
        Intent intent = new Intent(activity, cl);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, view, transName);
        if (Build.VERSION.SDK_INT >= 16)
            activity.startActivity(intent, options.toBundle());
        else
            activity.startActivity(intent);
    }

    public static void start(Activity activity, Intent intent, View view, String transName) {
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, view, transName);
        if (Build.VERSION.SDK_INT >= 16)
            activity.startActivity(intent, options.toBundle());
        else
            activity.startActivity(intent);

    }

    public static void startForResult(Activity activity, Class cl, int code, View view, String transName) {
        Intent intent = new Intent(activity, cl);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, view, transName);
        if (Build.VERSION.SDK_INT >= 16)
            activity.startActivityForResult(intent, code, options.toBundle());
        else
            activity.startActivityForResult(intent, code);
    }

    public static void startForResult(Activity activity, Intent cl, int code, View view, String transName) {
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, view, transName);
        if (Build.VERSION.SDK_INT >= 16)
            activity.startActivityForResult(cl, code, options.toBundle());
        else
            activity.startActivityForResult(cl, code);
    }

    public static void startWithExtra(Activity activity, Class cl, Bundle bundle, View view, String transName) {
        Intent intent = new Intent(activity, cl);
        intent.putExtras(bundle);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, view, transName);
        if (Build.VERSION.SDK_INT >= 16)
            activity.startActivity(intent, options.toBundle());
        else
            activity.startActivity(intent);
    }

    public static void startForResult(Fragment activity, Class cl, int code, View view, String transName) {
        Intent intent = new Intent(activity.getContext(), cl);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity.getActivity(), view, transName);
        if (Build.VERSION.SDK_INT >= 16)
            activity.startActivityForResult(intent, code, options.toBundle());
        else
            activity.startActivityForResult(intent, code);
    }

    public static void startForResult(Fragment activity, Intent cl, int code, View view, String transName) {
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity.getActivity(), view, transName);
        if (Build.VERSION.SDK_INT >= 16)
            activity.startActivityForResult(cl, code, options.toBundle());
        else
            activity.startActivityForResult(cl, code);
    }

    public static void startWithExtra(Fragment activity, Class cl, Bundle bundle, View view, String transName) {
        Intent intent = new Intent(activity.getContext(), cl);
        intent.putExtras(bundle);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity.getActivity(), view, transName);
        if (Build.VERSION.SDK_INT >= 16)
            activity.startActivity(intent, options.toBundle());
        else
            activity.startActivity(intent);
    }

    @SafeVarargs public static void start(Activity activity, Class cl, Pair<View, String>... sharedElements) {
        Intent intent = new Intent(activity, cl);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, sharedElements);
        if (Build.VERSION.SDK_INT >= 16)
            activity.startActivity(intent, options.toBundle());
        else
            activity.startActivity(intent);
    }

    @SafeVarargs public static void start(Activity activity, Intent intent, Pair<View, String>... sharedElements) {
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, sharedElements);
        if (Build.VERSION.SDK_INT >= 16)
            activity.startActivity(intent, options.toBundle());
        else
            activity.startActivity(intent);

    }

    public static int getScreenOrientation(Context activity) {
        return activity.getResources().getConfiguration().orientation;
    }

    public static void startActivity(@NonNull Context context, Class className) {
        Intent intent = new Intent(context, className);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    public static void startActivityWithFinish(@NonNull Activity context, Class className) {
        Intent intent = new Intent(context, className);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    public static void startGalleryIntent(@NonNull Activity activity) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        activity.startActivityForResult(intent, SELECT_PHOTO_REQUEST);
    }

    public static void startCameraIntent(@NonNull Activity activity, @NonNull File file) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
            activity.startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
        }
    }

}
