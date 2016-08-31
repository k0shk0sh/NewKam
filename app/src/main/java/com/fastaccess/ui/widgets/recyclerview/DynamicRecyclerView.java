package com.fastaccess.ui.widgets.recyclerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Kosh on 9/24/2015. copyrights are reserved
 * <p>
 * recyclerview which will showParentOrSelf/showParentOrSelf itself base on adapter
 */
public class DynamicRecyclerView extends RecyclerView {

    private View emptyView;
    private View parentView;

    private AdapterDataObserver observer = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            showEmptyView();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
            showEmptyView();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
            showEmptyView();
        }
    };

    public DynamicRecyclerView(Context context) {
        super(context);
    }

    public DynamicRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DynamicRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (isInEditMode()) return;
    }

    public void showEmptyView() {
        Adapter<?> adapter = getAdapter();
        if (adapter != null) {
            if (emptyView != null) {
                if (adapter.getItemCount() == 0) {
                    showParentOrSelf(false);
                } else {
                    showParentOrSelf(true);
                }
            }
        } else {
            if (emptyView != null) {
                showParentOrSelf(false);
            }
        }
    }

    @Override public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        if (adapter != null) {
            adapter.registerAdapterDataObserver(observer);
            observer.onChanged();
        }
    }

    private void showParentOrSelf(boolean show) {
        if (parentView == null) {
            setVisibility(show ? VISIBLE : GONE);
        } else {
            parentView.setVisibility(show ? VISIBLE : GONE);
        }
        emptyView.setVisibility(!show ? VISIBLE : GONE);
    }

    public void setEmptyView(View emptyView, @Nullable View parentView) {
        this.emptyView = emptyView;
        this.parentView = parentView;
        showEmptyView();
    }

    public void hideProgress(View view) {
        view.setVisibility(GONE);
    }

    public void showProgress(View view) {
        view.setVisibility(VISIBLE);
    }
}
