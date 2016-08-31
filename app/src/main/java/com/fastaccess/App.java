package com.fastaccess;

import android.app.Application;

import com.fastaccess.helper.IconCache;
import com.fastaccess.helper.PrefHelper;
import com.fastaccess.helper.TypeFaceHelper;
import com.fastaccess.ui.modules.main.view.MainView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Modifier;

import cat.ereza.customactivityoncrash.CustomActivityOnCrash;

/**
 * Created by Kosh on 24 May 2016, 7:51 PM
 */

public class App extends Application {

    private static App instance;
    private FirebaseAnalytics firebaseAnalytics;
    private IconCache iconCache;

    @Override public void onCreate() {
        super.onCreate();
        instance = this;
        PrefHelper.init(this);
        TypeFaceHelper.generateTypeface(this);
        CustomActivityOnCrash.setRestartActivityClass(MainView.class);
        CustomActivityOnCrash.setShowErrorDetails(BuildConfig.DEBUG);
        CustomActivityOnCrash.install(this);
    }

    public static App getInstance() {
        return instance;
    }

    public static Gson gson() {
        return new GsonBuilder()
                .excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
                .serializeNulls()
                .create();
    }

    public FirebaseAnalytics getFirebaseAnalytics() {
        if (firebaseAnalytics == null) {
            firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        }
        return firebaseAnalytics;
    }

    public IconCache getIconCache() {
        if (iconCache == null) {
            iconCache = new IconCache(this.getApplicationContext());
        }
        return iconCache;
    }
}
