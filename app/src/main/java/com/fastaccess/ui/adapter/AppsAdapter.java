package com.fastaccess.ui.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.fastaccess.R;
import com.fastaccess.data.dao.AppsModel;
import com.fastaccess.helper.InputHelper;
import com.fastaccess.ui.adapter.viewholder.AppsViewHolder;
import com.fastaccess.ui.widgets.recyclerview.BaseRecyclerAdapter;
import com.fastaccess.ui.widgets.recyclerview.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Kosh on 30 Aug 2016, 11:42 PM
 */

public class AppsAdapter extends BaseRecyclerAdapter<AppsModel, AppsViewHolder, AppsViewHolder.OnItemClickListener<AppsModel>> implements Filterable {
    private Map<String, Boolean> selection;

    public AppsAdapter(@NonNull List<AppsModel> data, @Nullable AppsViewHolder.OnItemClickListener<AppsModel> listener,
                       Map<String, Boolean> selection) {
        super(data, listener);
        this.selection = selection;
    }

    @Override protected AppsViewHolder viewHolder(ViewGroup parent, int viewType) {
        return new AppsViewHolder(BaseViewHolder.getView(parent, R.layout.app_row_item), this);
    }

    @Override protected void onBindView(AppsViewHolder holder, int position) {
        AppsModel model = getItem(position);
        if (model != null) {
            holder.bind(model, isSelected(model.getComponentName().toShortString()));
        }
    }

    public void select(String packageName, int position, boolean select) {
        if (select) selection.put(packageName, true);
        else selection.remove(packageName);
        notifyItemChanged(position);
    }

    public boolean isSelected(@NonNull String packageName) {
        return selection.get(packageName) != null && selection.get(packageName);
    }

    public boolean hasSelection() {
        return selection != null && !selection.isEmpty();
    }

    public void clearSelection() {
        if (hasSelection()) selection.clear();
        notifyDataSetChanged();
    }

    public int selectionSize() {
        return selection != null ? selection.size() : 0;
    }

    @Override public Filter getFilter() {
        return new Filter() {
            @Override protected FilterResults performFiltering(CharSequence charSequence) {
                final FilterResults oReturn = new FilterResults();
                final List<AppsModel> results = new ArrayList<>();
                if (!InputHelper.isEmpty(charSequence)) {
                    if (!getData().isEmpty()) {
                        for (AppsModel appInfo : getData()) {
                            if (appInfo.getAppName().toLowerCase().contains(charSequence.toString())) {
                                results.add(appInfo);
                            }
                        }
                    }
                    oReturn.values = results;
                    oReturn.count = results.size();
                }
                return oReturn;
            }

            @Override protected void publishResults(CharSequence constraint, FilterResults results) {
                insertItems((List<AppsModel>) results.values);
            }
        };
    }
}
