package com.fastaccess.ui.modules.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.view.View;

import com.fastaccess.BuildConfig;
import com.fastaccess.R;
import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.LibsBuilder;

/**
 * Created by Kosh on 31 Aug 2016, 11:46 AM
 */

public class SettingsFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceClickListener {
    @Override public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.general_settings);
        findPreference("libraries").setOnPreferenceClickListener(this);
        findPreference("version").setSummary(BuildConfig.VERSION_NAME);
        findPreference("size").setSummary("20MB");
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setDivider(ActivityCompat.getDrawable(getActivity(), R.drawable.list_divider));
        setDividerHeight(1);
    }

    @Override public boolean onPreferenceClick(Preference preference) {
        if (preference.getKey().equalsIgnoreCase("libraries")) {
            openLibs();
            return true;
        }
        return false;
    }

    private void openLibs() {
        new LibsBuilder()
                .withFields(R.string.class.getFields())
                .withActivityStyle(Libs.ActivityStyle.LIGHT_DARK_TOOLBAR)
                .withActivityTheme(R.style.AppTheme)
                .withAboutIconShown(true)
                .withAboutVersionShown(true)
                .withAutoDetect(true)
                .withLicenseShown(true)
                .withVersionShown(true)
                .withActivityTitle("Open Source Libs")
//                    .withLibraries("Firebase Cloud Messaging", "Firebase Analytics", "PermissionHelper", "CustomActivityOnCrash", "Event Bus",
//                            "CircularReveal", "CircularFillableLoaders")
                .start(getActivity());
    }

}
