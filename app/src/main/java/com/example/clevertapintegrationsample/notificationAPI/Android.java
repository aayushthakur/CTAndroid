
package com.example.clevertapintegrationsample.notificationAPI;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Android implements Serializable
{

    @SerializedName("background_image")
    @Expose
    private String backgroundImage;
    @SerializedName("default_sound")
    @Expose
    private boolean defaultSound;
    @SerializedName("deep_link")
    @Expose
    private String deepLink;
    @SerializedName("large_icon")
    @Expose
    private String largeIcon;
    @SerializedName("key")
    @Expose
    private String key;
    @SerializedName("wzrk_cid")
    @Expose
    private String wzrkCid;
    private final static long serialVersionUID = -723632564432318363L;

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public boolean isDefaultSound() {
        return defaultSound;
    }

    public void setDefaultSound(boolean defaultSound) {
        this.defaultSound = defaultSound;
    }

    public String getDeepLink() {
        return deepLink;
    }

    public void setDeepLink(String deepLink) {
        this.deepLink = deepLink;
    }

    public String getLargeIcon() {
        return largeIcon;
    }

    public void setLargeIcon(String largeIcon) {
        this.largeIcon = largeIcon;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getWzrkCid() {
        return wzrkCid;
    }

    public void setWzrkCid(String wzrkCid) {
        this.wzrkCid = wzrkCid;
    }

}