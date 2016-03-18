package com.picsart.sharelib.api.model;

import com.google.gson.annotations.SerializedName;


public class Token {

    @SerializedName("access_token")
    public String accessToken;

    @SerializedName("expires_in")
    public long expiresIn;

    @SerializedName("token_type")
    public String tokenType;

   /* @SerializedName("url")
    private String url;


    public String getUrl() {
        return url;
    }*/

}
