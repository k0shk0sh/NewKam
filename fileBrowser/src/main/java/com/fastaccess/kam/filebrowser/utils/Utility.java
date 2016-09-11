/*
 * Copyright (C) 2016 Angad Singh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fastaccess.kam.filebrowser.utils;

import android.webkit.MimeTypeMap;

import com.fastaccess.kam.filebrowser.model.FileListItem;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

/**
 * <p> Created by Angad Singh on 11-07-2016. </p>
 */
public class Utility {
    /**
     * Prepares the list of Files and Folders inside 'inter' Directory. The list can be filtered through extensions. 'filter' reference is the
     * FileFilter. A reference of ArrayList is passed, in case it may contain the ListItem for parent directory. Returns the List of Directories/files
     * in the form of ArrayList.
     */
    public static ArrayList<FileListItem> prepareFileListEntries(ArrayList<FileListItem> internalList, File inter, ExtensionFilter filter) {
        try {
            for (File name : inter.listFiles(filter)) {
                if (name.canRead()) {
                    FileListItem item = new FileListItem();
                    item.setFilename(name.getName());
                    item.setDirectory(name.isDirectory());
                    item.setLocation(name.getAbsolutePath());
                    item.setTime(name.lastModified());
                    internalList.add(item);
                }
            }
            Collections.sort(internalList);
        } catch (NullPointerException e) {
            e.printStackTrace();
            internalList = new ArrayList<>();
        }
        return internalList;
    }

    public static ArrayList<FileListItem> getFileList(File inter, ExtensionFilter filter) {
        ArrayList<FileListItem> internalList = new ArrayList<>();
        return prepareFileListEntries(internalList, inter, filter);
    }

    public static String extension(String file) {
        return MimeTypeMap.getFileExtensionFromUrl(file);
    }


}
