package com.example.clevertapintegrationsample.homescreen;

public class HorizontalListItemsModel {

    String imageURL;
    String imageText;

    public HorizontalListItemsModel(String imageURL, String imageText) {
        this.imageURL = imageURL;
        this.imageText = imageText;
    }

    public String getImageText() {
        return imageText;
    }

    public void setImageText(String imageText) {
        this.imageText = imageText;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }


}
