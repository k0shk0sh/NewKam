package com.fastaccess.ui.modules.details.view;

import android.content.pm.PermissionInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.View;

import com.fastaccess.R;
import com.fastaccess.helper.ApkHelper;
import com.fastaccess.helper.Bundler;
import com.fastaccess.helper.InputHelper;
import com.fastaccess.ui.base.BaseFragment;
import com.fastaccess.ui.widgets.FontTextView;
import com.fastaccess.ui.widgets.ShadowTransformer;

import butterknife.BindView;
import icepick.State;

/**
 * Created by Kosh on 31 Aug 2016, 7:00 PM
 */

public class PermissionsView extends BaseFragment {

    @State String permission;
    @BindView(R.id.permissionName) FontTextView permissionName;
    @BindView(R.id.permissionDescription) FontTextView permissionDescription;
    @BindView(R.id.cardView) CardView cardView;

    @Override protected int fragmentLayout() {
        return R.layout.permission_row_item;
    }

    @Override protected boolean isRetainRequired() {
        return false;
    }

    @Override protected void onFragmentCreated(View view, @Nullable Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            permission = getArguments().getString("permission");
        }
        if (InputHelper.isEmpty(permission)) {
            throw new NullPointerException("Permission name is empty.");
        }
        cardView.setMaxCardElevation(cardView.getCardElevation() * ShadowTransformer.CardAdapter.MAX_ELEVATION_FACTOR);
        PermissionInfo info = ApkHelper.getPermissionInfo(getContext(), permission);
        if (info != null) {
            permissionName.setText(info.loadLabel(getContext().getPackageManager()));
            permissionDescription.setText(info.loadDescription(getContext().getPackageManager()));
        }
    }

    public static PermissionsView getInstance(@NonNull String permissionName) {
        PermissionsView fragment = new PermissionsView();
        fragment.setArguments(Bundler.start().put("permission", permissionName).end());
        return fragment;
    }

    public CardView getCardView() {
        return cardView;
    }
}
