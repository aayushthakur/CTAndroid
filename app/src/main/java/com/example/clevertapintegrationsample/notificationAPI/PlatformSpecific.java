
package com.example.clevertapintegrationsample.notificationAPI;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PlatformSpecific implements Serializable
{

    @SerializedName("windows")
    @Expose
    private Windows windows;
    @SerializedName("ios")
    @Expose
    private Ios ios;
    @SerializedName("android")
    @Expose
    private Android android;
    private final static long serialVersionUID = -9208902894363571965L;

    public Windows getWindows() {
        return windows;
    }

    public void setWindows(Windows windows) {
        this.windows = windows;
    }

    public Ios getIos() {
        return ios;
    }

    public void setIos(Ios ios) {
        this.ios = ios;
    }

    public Android getAndroid() {
        return android;
    }

    public void setAndroid(Android android) {
        this.android = android;
    }

}