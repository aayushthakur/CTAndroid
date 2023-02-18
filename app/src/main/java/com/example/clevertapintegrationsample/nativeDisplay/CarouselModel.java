package com.example.clevertapintegrationsample.nativeDisplay;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class CarouselModel implements Parcelable {

    public String imageUrl;
    public CarouselModel() {

    }

    protected CarouselModel(Parcel in) {
        imageUrl = in.readString();
    }

    public static final Creator<CarouselModel> CREATOR = new Creator<CarouselModel>() {
        @Override
        public CarouselModel createFromParcel(Parcel in) {
            return new CarouselModel(in);
        }

        @Override
        public CarouselModel[] newArray(int size) {
            return new CarouselModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imageUrl);
    }
}
