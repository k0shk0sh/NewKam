package com.fastaccess.kam.filebrowser.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.fastaccess.kam.filebrowser.model.DialogProperties;
import com.fastaccess.kam.filebrowser.model.FileListItem;
import com.fastaccess.kam.filebrowser.utils.ExtensionFilter;
import com.fastaccess.kam.filebrowser.utils.Utility;

import java.util.List;

/**
 * Created by Kosh on 11 Sep 2016, 3:22 PM
 */

public class FileListLoader extends AsyncTaskLoader<List<FileListItem>> {
    private List<FileListItem> files;

    public FileListLoader(Context context) {
        super(context);
    }

    @Override public List<FileListItem> loadInBackground() {
        DialogProperties properties = new DialogProperties();
        ExtensionFilter filter = new ExtensionFilter(properties);
        return Utility.getFileList(properties.root.getAbsoluteFile(), filter);
    }

    @Override public void deliverResult(List<FileListItem> apps) {
        if (isReset()) {
            if (apps != null) {
                return;
            }
        }
        files = apps;
        if (isStarted()) {
            super.deliverResult(apps);
        }
    }

    @Override protected void onStartLoading() {
        if (files != null) {
            deliverResult(files);
        }
        if (takeContentChanged()) {
            forceLoad();
        } else if (files == null || files.isEmpty()) {
            forceLoad();
        }


    }

    @Override protected void onStopLoading() {
        cancelLoad();
    }

    @Override protected void onReset() {
        onStopLoading();
        if (files != null) {
            files = null;
        }
    }

}

