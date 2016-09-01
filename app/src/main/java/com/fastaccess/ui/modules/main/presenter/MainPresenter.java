package com.fastaccess.ui.modules.main.presenter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.fastaccess.App;
import com.fastaccess.R;
import com.fastaccess.data.dao.AppsModel;
import com.fastaccess.kam.filebrowser.view.FilePickerDialog;
import com.fastaccess.provider.loader.AppsLoader;
import com.fastaccess.ui.base.mvp.presenter.BasePresenter;
import com.fastaccess.ui.modules.main.model.MainMvp;

import java.util.List;

/**
 * Created by Kosh on 25 May 2016, 9:20 PM
 */

public class MainPresenter extends BasePresenter<MainMvp.View> implements MainMvp.Presenter {

    private MainPresenter(MainMvp.View view) {
        super(view);
    }

    public static MainPresenter with(MainMvp.View view) {
        return new MainPresenter(view);
    }

    @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        getView().closeOpenDrawer(true);
        switch (item.getItemId()) {
            case R.id.settings:
                getView().onNavigateTo(MainMvp.SETTINGS);
                return true;
            case R.id.kamFolder:
                getView().onNavigateTo(MainMvp.KAM_FOLDER);
                return true;
        }
        return false;
    }

    @Override public boolean isDrawerOpen(@NonNull DrawerLayout drawerLayout) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            getView().closeOpenDrawer(true);
            return true;
        }
        return false;
    }

    @Override public void navigateTo(@NonNull FragmentManager manager, @MainMvp.NavigationMode int mode) {
        switch (mode) {
            case MainMvp.SETTINGS:
                break;
            case MainMvp.KAM_FOLDER:
                FilePickerDialog dialog = new FilePickerDialog();
                dialog.show(manager, "FilePickerDialog");
                break;
        }
    }

    @Override public void openDrawer() {
        getView().closeOpenDrawer(false);
    }

    @Override public Loader<List<AppsModel>> onCreateLoader(int id, Bundle args) {
        getView().onStartLoading();
        return new AppsLoader(App.getInstance().getApplicationContext(), App.getInstance().getIconCache());
    }

    @Override public void onLoadFinished(Loader<List<AppsModel>> loader, List<AppsModel> data) {
        getView().onAppsLoaded(data);
    }

    @Override public void onLoaderReset(Loader<List<AppsModel>> loader) {
        getView().onLoaderReset();
    }

    @Override public void onItemClick(int position, View v, AppsModel item) {
        if (getView().hasSelection()) {
            onItemLongClick(position, v, item);
        } else {
            getView().onOpenDetails(v, item);
        }
    }

    @Override public void onItemLongClick(int position, View v, AppsModel item) {
        getView().setSelection(item.getComponentName().toShortString(), position);
    }

    @Override public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mode.getMenuInflater().inflate(R.menu.actionmode_menu, menu);
        return true;
    }

    @Override public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        if (item.getItemId() == R.id.backup) {
            getView().onBackup();
            return true;
        } else if (item.getItemId() == R.id.selectAll) {
            getView().onSelectAll();
            return true;
        } else if (item.getItemId() == R.id.clear) {
            getView().onClearSelection();
        }
        return false;
    }

    @Override public void onDestroyActionMode(ActionMode mode) {
        getView().onActionModeDestroyed();
    }
}
