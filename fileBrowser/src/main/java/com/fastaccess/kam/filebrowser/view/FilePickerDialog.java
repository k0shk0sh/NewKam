package com.fastaccess.kam.filebrowser.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fastaccess.kam.filebrowser.R;
import com.fastaccess.kam.filebrowser.controller.adapters.FileListAdapter;
import com.fastaccess.kam.filebrowser.loader.FileListLoader;
import com.fastaccess.kam.filebrowser.model.DialogProperties;
import com.fastaccess.kam.filebrowser.model.FileListItem;
import com.fastaccess.kam.filebrowser.model.MarkedItemList;
import com.fastaccess.kam.filebrowser.utils.ExtensionFilter;

import java.util.ArrayList;
import java.util.List;

public class FilePickerDialog extends AppCompatDialogFragment implements AdapterView.OnItemClickListener, LoaderManager
        .LoaderCallbacks<List<FileListItem>> {
    private Context context;
    private ListView listView;
    private TextView dname;
    private TextView dir_path;
    private ProgressBar progressBar;
    private DialogProperties properties;
    private ExtensionFilter filter;
    private FileListAdapter mFileListAdapter;

    @Override public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
    }

    @NonNull @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
        this.context = getContext();
        properties = new DialogProperties();
        filter = new ExtensionFilter(properties);
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        if (dialog.getWindow() != null) dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_main, container, false);
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        listView = (ListView) view.findViewById(R.id.fileList);
        listView.setOnItemClickListener(this);
        dname = (TextView) view.findViewById(R.id.dname);
        dir_path = (TextView) view.findViewById(R.id.dir_path);
        Button cancel = (Button) view.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        mFileListAdapter = new FileListAdapter(new ArrayList<FileListItem>(), context);
        listView.setAdapter(mFileListAdapter);
        dname.setText(properties.root.getName());
        dir_path.setText(properties.root.getAbsolutePath());
        if (getActivity() != null) {
            getActivity().getSupportLoaderManager().initLoader(100, null, this);
        }
    }

    @Override public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//        FileListItem fitem = (FileListItem) adapterView.getItemAtPosition(i);
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
}
