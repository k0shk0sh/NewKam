package com.fastaccess.kam.filebrowser.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fastaccess.kam.filebrowser.R;
import com.fastaccess.kam.filebrowser.controller.adapters.FileListAdapter;
import com.fastaccess.kam.filebrowser.loader.FileListLoader;
import com.fastaccess.kam.filebrowser.model.DialogProperties;
import com.fastaccess.kam.filebrowser.model.FileListItem;
import com.fastaccess.kam.filebrowser.model.MarkedItemList;

import java.util.ArrayList;
import java.util.List;

public class FilePickerDialog extends BottomSheetDialogFragment implements LoaderManager.LoaderCallbacks<List<FileListItem>> {
    private RecyclerView listView;
    private TextView directoryName;
    private TextView dirPath;
    private ProgressBar progressBar;
    private FileListAdapter mFileListAdapter;

    @NonNull @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = super.onCreateDialog(savedInstanceState);
        if (isTablet(getContext())) {
            dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override public void onShow(DialogInterface dialogINterface) {
                    BottomSheetDialog d = (BottomSheetDialog) dialogINterface;
                    if (dialog.getWindow() != null) dialog.getWindow().setLayout(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.MATCH_PARENT);
                }
            });
        }
        return dialog;
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_main, container, false);
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        listView = (RecyclerView) view.findViewById(R.id.fileList);
        directoryName = (TextView) view.findViewById(R.id.dname);
        dirPath = (TextView) view.findViewById(R.id.dir_path);
        mFileListAdapter = new FileListAdapter(new ArrayList<FileListItem>(), getContext());
        listView.setAdapter(mFileListAdapter);
        DialogProperties properties = new DialogProperties();
        directoryName.setText(properties.root.getName());
        dirPath.setText(properties.root.getAbsolutePath());
        getLoaderManager().initLoader(100, null, this);
    }

    @Override public void dismiss() {
        MarkedItemList.clearSelectionList();
        super.dismiss();
    }

    @Override public Loader<List<FileListItem>> onCreateLoader(int id, Bundle args) {
        progressBar.setVisibility(View.VISIBLE);
        return new FileListLoader(getContext());
    }

    @Override public void onLoadFinished(Loader<List<FileListItem>> loader, List<FileListItem> data) {
        progressBar.setVisibility(View.GONE);
        if (data != null) {
            mFileListAdapter.insert(data);
        }
    }

    @Override public void onLoaderReset(Loader<List<FileListItem>> loader) {
        progressBar.setVisibility(View.GONE);
    }

    boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration
                .SCREENLAYOUT_SIZE_MASK) >= Configuration
                .SCREENLAYOUT_SIZE_LARGE;
    }
}
