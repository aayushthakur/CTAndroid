
package com.example.clevertapintegrationsample.notificationAPI;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotificationRequest implements Serializable
{

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("to")
    @Expose
    private To to;
    @SerializedName("tag_group")
    @Expose
    private String tagGroup;
    @SerializedName("respect_frequency_caps")
    @Expose
    private boolean respectFrequencyCaps;
    @SerializedName("content")
    @Expose
    private Content content;
    private final static long serialVersionUID = 1915639016845010758L;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public To getTo() {
        return to;
    }

    public void setTo(To to) {
        this.to = to;
    }

    public String getTagGroup() {
        return tagGroup;
    }

    public void setTagGroup(String tagGroup) {
        this.tagGroup = tagGroup;
    }

    public boolean isRespectFrequencyCaps() {
        return respectFrequencyCaps;
    }

    public void setRespectFrequencyCaps(boolean respectFrequencyCaps) {
        this.respectFrequencyCaps = respectFrequencyCaps;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

}