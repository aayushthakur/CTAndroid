
package com.example.clevertapintegrationsample.notificationAPI;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class To implements Serializable
{

    @SerializedName("objectId")
    @Expose
    private List<String> objectId = null;
    private final static long serialVersionUID = 3486919634497836885L;

    public List<String> getObjectId() {
        return objectId;
    }

    public void setObjectId(List<String> objectId) {
        this.objectId = objectId;
    }

}