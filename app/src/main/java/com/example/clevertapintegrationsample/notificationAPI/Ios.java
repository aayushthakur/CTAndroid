
package com.example.clevertapintegrationsample.notificationAPI;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Ios implements Serializable
{

    @SerializedName("deep_link")
    @Expose
    private String deepLink;
    @SerializedName("sound_file")
    @Expose
    private String soundFile;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("badge_count")
    @Expose
    private int badgeCount;
    @SerializedName("key")
    @Expose
    private String key;
    private final static long serialVersionUID = -2086092793073590032L;

    public String getDeepLink() {
        return deepLink;
    }

    public void setDeepLink(String deepLink) {
        this.deepLink = deepLink;
    }

    public String getSoundFile() {
        return soundFile;
    }

    public void setSoundFile(String soundFile) {
        this.soundFile = soundFile;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getBadgeCount() {
        return badgeCount;
    }

    public void setBadgeCount(int badgeCount) {
        this.badgeCount = badgeCount;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

}