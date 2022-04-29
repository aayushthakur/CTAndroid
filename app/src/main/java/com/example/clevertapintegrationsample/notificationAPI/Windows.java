
package com.example.clevertapintegrationsample.notificationAPI;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Windows implements Serializable {

    @SerializedName("deep_link")
    @Expose
    private String deepLink;
    @SerializedName("key")
    @Expose
    private String key;
    private final static long serialVersionUID = 3161223145455646738L;

    public String getDeepLink() {
        return deepLink;
    }

    public void setDeepLink(String deepLink) {
        this.deepLink = deepLink;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}