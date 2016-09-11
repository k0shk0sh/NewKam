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

package com.fastaccess.kam.filebrowser.controller.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fastaccess.kam.filebrowser.R;
import com.fastaccess.kam.filebrowser.model.FileListItem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * <p> Created by Angad Singh on 09-07-2016. </p>
 */

public class FileListAdapter extends BaseAdapter {
    private ArrayList<FileListItem> listItem;
    private Context context;

    public FileListAdapter(ArrayList<FileListItem> listItem, Context context) {
        this.listItem = listItem;
        this.context = context;
    }

    @Override public int getCount() {
        return listItem.size();
    }

    @Override public FileListItem getItem(int i) {
        return listItem.get(i);
    }

    @Override public long getItemId(int i) {
        return i;
    }

    @Override public View getView(final int i, View view, ViewGroup viewGroup) {
        final ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.dialog_file_list_item, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        final FileListItem item = listItem.get(i);
        if (item.isDirectory()) {
            holder.type_icon.setImageResource(R.mipmap.ic_type_folder);
        } else {
            holder.type_icon.setImageResource(R.mipmap.ic_type_file);
        }
        holder.type_icon.setContentDescription(item.getFilename());
        holder.name.setText(item.getFilename());
        holder.type.setText(Formatter.formatFileSize(holder.type.getContext(), new File(item.getLocation()).length()));
        holder.deleteFolder.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                new AlertDialog.Builder(v.getContext())
                        .setTitle("Delete")
                        .setMessage("Are you sure?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override public void onClick(DialogInterface dialog, int which) {
                                new File(item.getLocation()).delete();
                                listItem.remove(item);
                                notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });
        if (item.getFilename().endsWith(".apk")) {
            holder.deleteFolder.setVisibility(View.VISIBLE);
        } else {
            holder.deleteFolder.setVisibility(View.GONE);
        }
        return view;
    }

    public void insert(List<FileListItem> itemList) {
        listItem.clear();
        listItem.addAll(itemList);
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        ImageView type_icon;
        ImageView deleteFolder;
        TextView name;
        TextView type;

        ViewHolder(View itemView) {
            name = (TextView) itemView.findViewById(R.id.fname);
            type = (TextView) itemView.findViewById(R.id.ftype);
            type_icon = (ImageView) itemView.findViewById(R.id.image_type);
            deleteFolder = (ImageView) itemView.findViewById(R.id.deleteFolder);
        }
    }
}
