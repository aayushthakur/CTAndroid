package com.example.clevertapintegrationsample.appinbox;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class AppInboxModel implements Parcelable {
    public static final int TEXT_TYPE = 0;
    public static final int IMAGE_TYPE = 1;
    public static final Creator<AppInboxModel> CREATOR = new Creator<AppInboxModel>() {
        @Override
        public AppInboxModel createFromParcel(Parcel in) {
            return new AppInboxModel(in);
        }

        @Override
        public AppInboxModel[] newArray(int size) {
            return new AppInboxModel[size];
        }
    };
    //    public static final int AUDIO_TYPE = 2;
    public int type;
    public long receivedDate;
    public String title;
    public String message;
    public String imageUrl;
    public List<String> tags;
    public AppInboxModel() {

    }

    protected AppInboxModel(Parcel in) {
        type = in.readInt();
        title = in.readString();
        message = in.readString();
        imageUrl = in.readString();
        tags = in.createStringArrayList();
    }

    public long getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(long receivedDate) {
        this.receivedDate = receivedDate;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
