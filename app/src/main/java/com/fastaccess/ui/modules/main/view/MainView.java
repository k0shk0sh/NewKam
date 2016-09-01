package com.fastaccess.ui.modules.main.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.MenuItem;
import android.view.View;

import com.fastaccess.R;
import com.fastaccess.data.dao.AppsModel;
import com.fastaccess.helper.ActivityHelper;
import com.fastaccess.helper.AnimHelper;
import com.fastaccess.helper.Bundler;
import com.fastaccess.helper.Logger;
import com.fastaccess.ui.adapter.AppsAdapter;
import com.fastaccess.ui.base.BaseActivity;
import com.fastaccess.ui.modules.details.view.AppDetailsView;
import com.fastaccess.ui.modules.main.model.MainMvp;
import com.fastaccess.ui.modules.main.presenter.MainPresenter;
import com.fastaccess.ui.modules.settings.SettingsActivity;
import com.fastaccess.ui.widgets.FontEditText;
import com.fastaccess.ui.widgets.FontTextView;
import com.fastaccess.ui.widgets.ForegroundImageView;
import com.fastaccess.ui.widgets.recyclerview.DynamicRecyclerView;
import com.pluscubed.recyclerfastscroll.RecyclerFastScroller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import icepick.State;

public class MainView extends BaseActivity implements MainMvp.View {

    @State @MainMvp.NavigationMode int mode;
    @State boolean showActionMode;
    @BindView(R.id.navigation) NavigationView navigation;
    @BindView(R.id.drawerLayout) DrawerLayout drawerLayout;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.searchIcon) ForegroundImageView searchIcon;
    @BindView(R.id.searchEditText) FontEditText searchEditText;
    @BindView(R.id.clear) ForegroundImageView clear;
    @BindView(R.id.appbar) AppBarLayout appbar;
    @BindView(R.id.recycler) DynamicRecyclerView recycler;
    @BindView(R.id.empty_text) FontTextView emptyText;
    @BindView(R.id.empty) NestedScrollView empty;
    @BindView(R.id.progressBar) View progressBar;
    @State HashMap<String, Boolean> selection = new LinkedHashMap<>();
    @BindView(R.id.fastScroll) RecyclerFastScroller fastScroll;
    @BindView(R.id.coordinatorLayout) CoordinatorLayout coordinatorLayout;
    private MainPresenter presenter;
    private AppsAdapter adapter;
    private ActionMode actionMode;

    @OnTextChanged(value = R.id.searchEditText, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED) void onTextChange(Editable s) {
        String text = s.toString();
        if (text.length() == 0) {
            getSupportLoaderManager().initLoader(0, null, getPresenter());
            AnimHelper.animateVisibility(false, clear);
        } else {
            AnimHelper.animateVisibility(true, clear);
            adapter.getFilter().filter(text);
        }
    }

    @OnClick(value = {R.id.searchIcon, R.id.clear}) void onClick(View view) {
        if (view.getId() == R.id.clear) {
            searchEditText.setText("");
        }
    }

    @Override protected int layout() {
        return R.layout.activity_main;
    }

    @Override protected boolean isTransparent() {
        return true;
    }

    @Override protected boolean canBack() {
        return false;
    }

    @Override protected boolean isSecured() {
        return false;
    }

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setToolbarIcon(R.drawable.ic_menu);
        navigation.setNavigationItemSelectedListener(getPresenter());
        recycler.setEmptyView(empty, null);
        emptyText.setText(R.string.no_apps);
        adapter = new AppsAdapter(new ArrayList<AppsModel>(), getPresenter(), selection);
        recycler.setAdapter(adapter);
        fastScroll.attachRecyclerView(recycler);
        fastScroll.attachAppBarLayout(coordinatorLayout, appbar);
        getSupportLoaderManager().initLoader(0, null, getPresenter());
        if (showActionMode) {
            actionMode = startSupportActionMode(getPresenter());
            actionMode.setTitle(getString(R.string.backup) + " ( " + adapter.selectionSize() + " )");
        }
    }

    @Override public void closeOpenDrawer(boolean close) {
        if (close) drawerLayout.closeDrawer(GravityCompat.START);
        else drawerLayout.openDrawer(GravityCompat.START);
    }

    @Override public void onNavigateTo(@MainMvp.NavigationMode int mode) {
        if (mode == MainMvp.SETTINGS) {
            startActivity(new Intent(this, SettingsActivity.class));
            return;
        }
        this.mode = mode;
        getPresenter().navigateTo(getSupportFragmentManager(), mode);
    }

    @Override public void onStartLoading() {
        Logger.e("called");
        recycler.showProgress(progressBar);
    }

    @Override public void onAppsLoaded(@Nullable List<AppsModel> data) {
        recycler.hideProgress(progressBar);
        adapter.insertItems(data);
        if (data != null) {
            Logger.e("called", data.size());
        }
    }

    @Override public void onLoaderReset() {
        Logger.e("called");
        recycler.hideProgress(progressBar);
        adapter.clear();
    }

    @Override public void setSelection(@NonNull String packageName, int position) {
        adapter.select(packageName, position, !adapter.isSelected(packageName));
        if (hasSelection()) {
            if (actionMode == null) {
                actionMode = startSupportActionMode(getPresenter());
            }
            actionMode.setTitle(getString(R.string.backup) + " ( " + adapter.selectionSize() + " )");
            showActionMode = true;
        } else {
            actionMode.finish();
            showActionMode = false;
        }
    }

    @Override public boolean hasSelection() {
        return adapter.hasSelection();
    }

    @Override public void onBackup() {
        if (actionMode != null) actionMode.finish();
    }

    @Override public void onActionModeDestroyed() {
        adapter.clearSelection();
        actionMode = null;
        showActionMode = false;
    }

    @Override public void onSelectAll() {
        if (adapter.selectionSize() == adapter.getData().size()) return;
        List<AppsModel> data = adapter.getData();
        for (int i = 0; i < data.size(); i++) {
            AppsModel model = data.get(i);
            Logger.e(i, model.getComponentName().toShortString());
            if (!adapter.isSelected(model.getComponentName().toShortString())) adapter.select(model.getComponentName().toShortString(), i, true);
        }
        if (actionMode == null) {
            actionMode = startSupportActionMode(getPresenter());
        }
        actionMode.setTitle(getString(R.string.backup) + " ( " + adapter.selectionSize() + " )");
        showActionMode = true;
    }

    @Override public void onClearSelection() {
        actionMode.finish();
    }

    @Override public void onOpenDetails(@NonNull View v, @NonNull AppsModel item) {
        String transitionName = "appIcon";//hardcoded!!
        Intent intent = new Intent(this, AppDetailsView.class);
        intent.putExtras(Bundler.start().put("app", item).end());
        ActivityHelper.start(this, intent, v, transitionName);
    }

    @Override public void onBackPressed() {
        if (!getPresenter().isDrawerOpen(drawerLayout)) {
            super.onBackPressed();
        }
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getPresenter().openDrawer();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public MainPresenter getPresenter() {
        if (presenter == null) presenter = MainPresenter.with(this);
        return presenter;
    }
}
