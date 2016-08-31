package com.fastaccess.ui.modules.main.model;

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.view.ActionMode;

import com.fastaccess.data.dao.AppsModel;
import com.fastaccess.ui.widgets.recyclerview.BaseViewHolder;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

/**
 * Created by Kosh on 25 May 2016, 9:17 PM
 */

public interface MainMvp {
    int SUGGESTIONS = 0;
    int SETTINGS = 2;

    @IntDef({SUGGESTIONS, SETTINGS})
    @Retention(RetentionPolicy.SOURCE) @interface NavigationMode {}


    interface View {
        void closeOpenDrawer(boolean close);

        void onNavigateTo(@NavigationMode int mode);

        void onStartLoading();

        void onAppsLoaded(@Nullable List<AppsModel> data);

        void onLoaderReset();

        void setSelection(@NonNull String packageName, int position);

        boolean hasSelection();

        void onBackup();

        void onActionModeDestroyed();

        void onSelectAll();

        void onClearSelection();

        void onOpenDetails(@NonNull android.view.View v, @NonNull AppsModel item);
    }

    interface Presenter extends NavigationView.OnNavigationItemSelectedListener,
            LoaderManager.LoaderCallbacks<List<AppsModel>>,
            BaseViewHolder.OnItemClickListener<AppsModel>,
            ActionMode.Callback {
        void openDrawer();

        boolean isDrawerOpen(@NonNull DrawerLayout drawerLayout);

        void navigateTo(@NonNull FragmentManager manager, @NavigationMode int mode);
    }
}
