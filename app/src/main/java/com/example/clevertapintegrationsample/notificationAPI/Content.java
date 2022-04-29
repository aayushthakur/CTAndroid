
package com.example.clevertapintegrationsample.notificationAPI;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Content implements Serializable
{

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("body")
    @Expose
    private String body;
    @SerializedName("platform_specific")
    @Expose
    private PlatformSpecific platformSpecific;
    private final static long serialVersionUID = 7348130274446332847L;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public PlatformSpecific getPlatformSpecific() {
        return platformSpecific;
    }

    public void setPlatformSpecific(PlatformSpecific platformSpecific) {
        this.platformSpecific = platformSpecific;
    }

}