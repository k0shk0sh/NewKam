package com.fastaccess.ui.adapter.viewholder;

import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;

import com.fastaccess.R;
import com.fastaccess.data.dao.AppsModel;
import com.fastaccess.ui.widgets.FastBitmapDrawable;
import com.fastaccess.ui.widgets.FontTextView;
import com.fastaccess.ui.widgets.recyclerview.BaseRecyclerAdapter;
import com.fastaccess.ui.widgets.recyclerview.BaseViewHolder;

import butterknife.BindView;

/**
 * Created by Kosh on 30 Aug 2016, 11:42 PM
 */

public class AppsViewHolder extends BaseViewHolder {
    @BindView(R.id.appIcon) ImageView appIcon;
    @BindView(R.id.appName) FontTextView appName;
    @BindView(R.id.cardView) CardView cardView;
    @ColorInt private int selectedColor;
    @ColorInt private int normalColor;

    public AppsViewHolder(@NonNull View itemView, @Nullable BaseRecyclerAdapter adapter) {
        super(itemView, adapter);
        selectedColor = ActivityCompat.getColor(itemView.getContext(), R.color.light_gray);
        normalColor = ActivityCompat.getColor(itemView.getContext(), R.color.cardview_light_background);
        appIcon.setOnClickListener(this);
        appIcon.setOnLongClickListener(this);
    }

    public void bind(AppsModel model, boolean selected) {
        appName.setText(model.getAppName());
        FastBitmapDrawable drawable = new FastBitmapDrawable(model.getBitmap());
        appIcon.setImageDrawable(drawable);
        drawable.setGhostModeEnabled(selected);
        drawable.setPressed(selected);
        cardView.setCardBackgroundColor(selected ? selectedColor : normalColor);
    }
}
