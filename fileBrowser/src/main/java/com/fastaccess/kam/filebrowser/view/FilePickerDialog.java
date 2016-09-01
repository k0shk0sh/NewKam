package com.fastaccess.kam.filebrowser.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fastaccess.kam.filebrowser.R;
import com.fastaccess.kam.filebrowser.controller.adapters.FileListAdapter;
import com.fastaccess.kam.filebrowser.model.DialogProperties;
import com.fastaccess.kam.filebrowser.model.FileListItem;
import com.fastaccess.kam.filebrowser.model.MarkedItemList;
import com.fastaccess.kam.filebrowser.utils.ExtensionFilter;
import com.fastaccess.kam.filebrowser.utils.Utility;

import java.io.File;
import java.util.ArrayList;

public class FilePickerDialog extends AppCompatDialogFragment implements AdapterView.OnItemClickListener {
    private Context context;
    private ListView listView;
    private TextView dname, dir_path;
    private DialogProperties properties;
    private ArrayList<FileListItem> internalList;
    private ExtensionFilter filter;
    private FileListAdapter mFileListAdapter;

    @Override public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
    }

    @NonNull @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
        this.context = getContext();
        properties = new DialogProperties();
        filter = new ExtensionFilter(properties);
        internalList = new ArrayList<>();
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        if (dialog.getWindow() != null) dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_main, container, false);
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getView() == null) return;
        listView = (ListView) getView().findViewById(R.id.fileList);
        dname = (TextView) getView().findViewById(R.id.dname);
        dir_path = (TextView) getView().findViewById(R.id.dir_path);
        Button cancel = (Button) getView().findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        mFileListAdapter = new FileListAdapter(internalList, context);
        listView.setAdapter(mFileListAdapter);
    }

    @Override public void onStart() {
        super.onStart();
        File currLoc;
        if (properties.root.exists() && properties.root.isDirectory()) {
            currLoc = new File(properties.root.getAbsolutePath());
        } else {
            currLoc = new File(properties.error_dir.getAbsolutePath());
        }
        dname.setText(currLoc.getName());
        dir_path.setText(currLoc.getAbsolutePath());
        internalList.clear();
        internalList = Utility.prepareFileListEntries(internalList, currLoc, filter);
        mFileListAdapter.notifyDataSetChanged();
        listView.setOnItemClickListener(this);
    }

    @Override public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (internalList.size() > i) {
            FileListItem fitem = internalList.get(i);
            if (fitem.isDirectory()) {
                if (new File(fitem.getLocation()).canRead()) {
                    File currLoc = new File(fitem.getLocation());
                    dname.setText(currLoc.getName());
                    dir_path.setText(currLoc.getAbsolutePath());
                    internalList.clear();
                    if (!currLoc.getName().equals(properties.root.getName())) {
                        FileListItem parent = new FileListItem();
                        parent.setFilename("...");
                        parent.setDirectory(true);
                        parent.setLocation(currLoc.getParentFile().getAbsolutePath());
                        parent.setTime(currLoc.lastModified());
                        internalList.add(parent);
                    }
                    internalList = Utility.prepareFileListEntries(internalList, currLoc, filter);
                    mFileListAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(context, "Directory cannot be accessed", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override public void dismiss() {
        MarkedItemList.clearSelectionList();
        internalList.clear();
        super.dismiss();
    }
}
