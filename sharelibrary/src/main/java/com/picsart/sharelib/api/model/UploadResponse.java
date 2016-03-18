package com.picsart.sharelib.api.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by user on 2/25/16.
 */
public class UploadResponse {

    @SerializedName("url")
    private String url;

    public String getUrl() {
        return url;
    }


    public void setUrl(String url) {
        this.url = url;
    }
}
