package com.fastaccess.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.widget.CardView;
import android.view.ViewGroup;

import com.fastaccess.ui.modules.details.view.PermissionsView;
import com.fastaccess.ui.widgets.ShadowTransformer;

import java.util.List;

/**
 * Created by Kosh on 31 Aug 2016, 7:06 PM
 */

public class PermissionsAdapter extends FragmentStatePagerAdapter implements ShadowTransformer.CardAdapter {

    private List<PermissionsView> permissions;
    private float baseElevation;

    public PermissionsAdapter(FragmentManager fm, List<PermissionsView> permissions, float baseElevation) {
        super(fm);
        this.permissions = permissions;
        this.baseElevation = baseElevation;
    }

    @Override public Fragment getItem(int position) {
        return permissions.get(position);
    }

    @Override public float getBaseElevation() {
        return baseElevation;
    }

    @Override public CardView getCardViewAt(int position) {
        return permissions.get(position).getCardView();
    }

    @Override public int getCount() {
        return permissions.size();
    }

    @Override public Object instantiateItem(ViewGroup container, int position) {
        Object fragment = super.instantiateItem(container, position);
        permissions.set(position, (PermissionsView) fragment);
        return fragment;
    }
}
