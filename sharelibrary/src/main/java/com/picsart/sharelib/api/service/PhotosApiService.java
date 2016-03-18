package com.picsart.sharelib.api.service;

import com.picsart.sharelib.api.model.Token;
import com.picsart.sharelib.api.model.UploadResponse;


import okhttp3.RequestBody;
import retrofit2.Call;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by user on 2/24/16.
 */
public interface PhotosApiService {
    @Multipart
    @POST("files")
    Call<UploadResponse> uploadPhoto(@Part("file\"; filename=\"myFile.gif\" ") RequestBody file, @Query("token") String token);


    @FormUrlEncoded
    @POST("oauth2/token")
    Call<Token> getToken(@Field("client_id") String clientId, @Field("client_secret") String clientSeret, @Field("grant_type") String grantType, @Field("scope") String scope);
}



