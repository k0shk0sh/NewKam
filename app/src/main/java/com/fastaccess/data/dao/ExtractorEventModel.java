package com.fastaccess.data.dao;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;

/**
 * Created by Kosh on 05 Sep 2016, 8:59 PM
 */

public class ExtractorEventModel implements Parcelable {

    private boolean extracted;
    private boolean showHideProgress;

    private boolean forShare;
    private File destFile;
    private String appName;

    public boolean isExtracted() {
        return extracted;
    }

    public void setExtracted(boolean extracted) {
        this.extracted = extracted;
    }

    public ExtractorEventModel(boolean extracted) {
        this.extracted = extracted;
    }

    public ExtractorEventModel(boolean extracted, boolean showHideProgress) {
        this.extracted = extracted;
        this.showHideProgress = showHideProgress;
    }

    public ExtractorEventModel() {}

    public boolean isShowHideProgress() {
        return showHideProgress;
    }

    public void setShowHideProgress(boolean showHideProgress) {
        this.showHideProgress = showHideProgress;
    }

    public boolean isForShare() {
        return forShare;
    }

    public void setForShare(boolean forShare) {
        this.forShare = forShare;
    }

    public void setDestFile(File destFile) {
        this.destFile = destFile;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public File getDestFile() {
        return destFile;
    }

    public String getAppName() {
        return appName;
    }

    @Override public int describeContents() { return 0; }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.extracted ? (byte) 1 : (byte) 0);
        dest.writeByte(this.showHideProgress ? (byte) 1 : (byte) 0);
        dest.writeByte(this.forShare ? (byte) 1 : (byte) 0);
        dest.writeSerializable(this.destFile);
        dest.writeString(this.appName);
    }

    protected ExtractorEventModel(Parcel in) {
        this.extracted = in.readByte() != 0;
        this.showHideProgress = in.readByte() != 0;
        this.forShare = in.readByte() != 0;
        this.destFile = (File) in.readSerializable();
        this.appName = in.readString();
    }

    public static final Creator<ExtractorEventModel> CREATOR = new Creator<ExtractorEventModel>() {
        @Override public ExtractorEventModel createFromParcel(Parcel source) {return new ExtractorEventModel(source);}

        @Override public ExtractorEventModel[] newArray(int size) {return new ExtractorEventModel[size];}
    };
}
